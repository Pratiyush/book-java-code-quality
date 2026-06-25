package org.acme.smells;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Prices and places an order — the refactored service that resolves the Long Method smell
 * {@link OrderServiceSmelly} demonstrates.
 *
 * <p>The public {@link #placeOrder} reads as a short recipe: validate, then total, discount, ship, and
 * record. Each step is its own small, named, private method (Fowler's <em>Extract Function</em>), and
 * the early {@code throw} guards (<em>guard clauses</em> / <em>Replace Nested Conditional with Guard
 * Clauses</em>) flatten the nesting the smelly version carries. The result is the same {@link Receipt}
 * for every input — proven by {@code CodeSmellsTest} — at a fraction of the cognitive complexity Sonar
 * {@code java:S3776} measures. Extracting is not free: pushed too far it yields the opposite
 * <em>Middle Man</em> smell, so each method here earns its name by holding one whole idea.
 */
public final class OrderService {

    private static final Logger LOG = System.getLogger(OrderService.class.getName());

    /** Basis points are hundredths of a percent, so a percentage of an amount divides by 10,000. */
    private static final int BASIS_POINTS_DENOMINATOR = 10_000;

    private final long freeShippingThreshold;
    private final long flatShippingCharge;

    /** Observability: how many orders were turned away by a precondition (illustrative, Chapter 45). */
    private final AtomicLong rejected = new AtomicLong();

    /**
     * Creates a service with the externalized pricing policy.
     *
     * @param freeShippingThreshold the post-discount subtotal at or above which shipping is free, in
     *                              minor units, never negative
     * @param flatShippingCharge    the flat shipping charge below the threshold, in minor units, never
     *                              negative
     * @throws IllegalArgumentException if either amount is negative
     */
    public OrderService(long freeShippingThreshold, long flatShippingCharge) {
        if (freeShippingThreshold < 0 || flatShippingCharge < 0) {
            throw new IllegalArgumentException("shipping amounts must not be negative");
        }
        this.freeShippingThreshold = freeShippingThreshold;
        this.flatShippingCharge = flatShippingCharge;
    }

    /**
     * Prices and places an order, returning a receipt of what was charged.
     *
     * @param order the order to place, never {@code null}
     * @return the receipt for the placed order, never {@code null}
     * @throws NullPointerException   if {@code order} is {@code null}
     * @throws OrderRejectedException if the order has no lines or mixes currencies
     * @implSpec Validates before any pricing, so a rejected order changes no state and increments only
     *     the rejected counter.
     */
    // tag::refactor-extract[]
    public Receipt placeOrder(Order order) {
        requirePlaceable(order);
        String currency = order.lines().get(0).price().currency();
        Money subtotal = subtotal(order.lines(), currency);
        Money discount = discountFor(order.customer().tier(), subtotal);
        Money shipping = shippingFor(subtotal, discount, currency);
        return receipt(order.id(), subtotal, discount, shipping);
    }
    // end::refactor-extract[]

    /**
     * Validates that an order can be placed, or rejects it with a typed error.
     *
     * @param order the order to check
     * @throws NullPointerException   if {@code order} is {@code null}
     * @throws OrderRejectedException if the order is empty or mixes currencies
     */
    private void requirePlaceable(Order order) {
        Objects.requireNonNull(order, "order");
        if (order.lines().isEmpty()) {
            rejected.incrementAndGet();
            throw new OrderRejectedException("empty-order", "an order needs at least one line item");
        }
        String currency = order.lines().get(0).price().currency();
        boolean mixed = order.lines().stream()
            .anyMatch(line -> !line.price().currency().equals(currency));
        if (mixed) {
            rejected.incrementAndGet();
            throw new OrderRejectedException("currency-mismatch", "all lines must share a currency");
        }
    }

    /**
     * Totals the line items.
     *
     * @param lines    the order's lines
     * @param currency the order currency
     * @return the subtotal, never {@code null}
     */
    private Money subtotal(List<LineItem> lines, String currency) {
        Money sum = new Money(0L, currency);
        for (LineItem line : lines) {
            sum = sum.plus(line.lineTotal().minorUnits());
        }
        return sum;
    }

    /**
     * Computes the loyalty discount for a tier against a subtotal.
     *
     * @param tier     the customer's loyalty tier
     * @param subtotal the order subtotal
     * @return the discount amount, never {@code null}
     */
    private Money discountFor(LoyaltyTier tier, Money subtotal) {
        long amount = subtotal.minorUnits() * tier.discountBasisPoints() / BASIS_POINTS_DENOMINATOR;
        return new Money(amount, subtotal.currency());
    }

    /**
     * Computes shipping from the post-discount subtotal.
     *
     * @param subtotal the order subtotal
     * @param discount the loyalty discount
     * @param currency the order currency
     * @return the shipping charge, never {@code null}
     */
    private Money shippingFor(Money subtotal, Money discount, String currency) {
        long afterDiscount = subtotal.minorUnits() - discount.minorUnits();
        long charge = afterDiscount >= freeShippingThreshold ? 0L : flatShippingCharge;
        return new Money(charge, currency);
    }

    /**
     * Assembles the receipt from the priced parts.
     *
     * @param orderId  the order id
     * @param subtotal the subtotal
     * @param discount the discount
     * @param shipping the shipping charge
     * @return the receipt, never {@code null}
     */
    private Receipt receipt(String orderId, Money subtotal, Money discount, Money shipping) {
        long total = subtotal.minorUnits() - discount.minorUnits() + shipping.minorUnits();
        Money charged = new Money(total, subtotal.currency());
        LOG.log(Level.DEBUG, "placed order {0} charged {1}", orderId, charged.minorUnits());
        return new Receipt(orderId, subtotal, discount, shipping, charged);
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
