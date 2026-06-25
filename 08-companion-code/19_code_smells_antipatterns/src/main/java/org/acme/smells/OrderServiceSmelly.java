package org.acme.smells;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Prices and places an order in one long, deeply-nested method — a deliberate counter-example, the
 * <em>Long Method</em> smell {@link OrderService} resolves.
 *
 * <p>{@link #placeOrder} does everything inline: it validates, totals the lines, branches on the
 * loyalty tier with an {@code if/else} ladder over the tier (the type-code branch a {@code switch} or
 * an {@code enum} method retires), computes shipping, and assembles the receipt — all in one body that
 * nests several levels deep. That is the shape Sonar {@code java:S3776} (cognitive complexity, default
 * 15) and PMD {@code NcssCount} (method, default 60) measure. It compiles, it passes its tests, and it
 * returns exactly the same {@link Receipt} as the refactored service for every input — which is the
 * chapter's point: a smell is a cost paid at the next change, not a bug today. The house Checkstyle and
 * SpotBugs gate does not measure method length or complexity, so this method is not flagged here; a
 * different tool sees a different smell.
 */
public final class OrderServiceSmelly {

    private static final Logger LOG = System.getLogger(OrderServiceSmelly.class.getName());

    private static final int BASIS_POINTS_DENOMINATOR = 10_000;

    private final long freeShippingThreshold;
    private final long flatShippingCharge;

    /** Observability: how many orders were turned away by a precondition (illustrative, Chapter 45). */
    private final AtomicLong rejected = new AtomicLong();

    /**
     * Creates a service with the externalized pricing policy.
     *
     * @param freeShippingThreshold the post-discount subtotal at or above which shipping is free
     * @param flatShippingCharge    the flat shipping charge below the threshold
     * @throws IllegalArgumentException if either amount is negative
     */
    public OrderServiceSmelly(long freeShippingThreshold, long flatShippingCharge) {
        if (freeShippingThreshold < 0 || flatShippingCharge < 0) {
            throw new IllegalArgumentException("shipping amounts must not be negative");
        }
        this.freeShippingThreshold = freeShippingThreshold;
        this.flatShippingCharge = flatShippingCharge;
    }

    /**
     * Prices and places an order — everything in one long, nested body.
     *
     * @param order the order to place, never {@code null}
     * @return the receipt for the placed order, never {@code null}
     * @throws NullPointerException   if {@code order} is {@code null}
     * @throws OrderRejectedException if the order has no lines or mixes currencies
     */
    public Receipt placeOrder(Order order) {
        if (order == null) {
            throw new NullPointerException("order");
        }
        if (order.lines().isEmpty()) {
            rejected.incrementAndGet();
            throw new OrderRejectedException("empty-order", "an order needs at least one line item");
        }
        String currency = order.lines().get(0).price().currency();
        long subtotal = 0L;
        for (LineItem line : order.lines()) {
            if (!line.price().currency().equals(currency)) {
                rejected.incrementAndGet();
                throw new OrderRejectedException("currency-mismatch", "lines must share a currency");
            } else {
                subtotal = subtotal + line.price().minorUnits() * line.quantity();
            }
        }
        // tag::smell-long-method[]
        long discount;                                        // ... one method, still going ...
        if (order.customer().tier() == LoyaltyTier.GOLD) {    // a type-code branch, nested deep
            discount = subtotal * 1000 / BASIS_POINTS_DENOMINATOR;
        } else if (order.customer().tier() == LoyaltyTier.SILVER) {
            discount = subtotal * 500 / BASIS_POINTS_DENOMINATOR;
        } else {
            discount = 0L;
        }
        // end::smell-long-method[]
        long shipping;
        if (subtotal - discount >= freeShippingThreshold) {
            shipping = 0L;
        } else {
            shipping = flatShippingCharge;
        }
        long total = subtotal - discount + shipping;
        LOG.log(Level.DEBUG, "placed order {0} charged {1}", order.id(), total);
        return new Receipt(order.id(),
            new Money(subtotal, currency),
            new Money(discount, currency),
            new Money(shipping, currency),
            new Money(total, currency));
    }

    /**
     * Health/observability surface: the running count of orders rejected by a precondition.
     *
     * @return the number of rejected orders since startup, never negative
     */
    public long rejectedCount() {
        return rejected.get();
    }

    /**
     * A readiness probe: the service is ready as soon as it is constructed with a valid policy.
     *
     * @return {@code true} when the service can place orders
     */
    public boolean isReady() {
        return freeShippingThreshold >= 0 && flatShippingCharge >= 0;
    }
}
