package org.acme.orders;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Places an order: prices each line through a query port, charges the total through a command port,
 * and returns a receipt.
 *
 * <p>The service exists to give the chapter's three disciplines something real to act on. Its two
 * collaborators are deliberately different kinds:
 *
 * <ul>
 *   <li>{@link PriceCatalog} is a <em>query</em> — the unit tests {@code stub} it.</li>
 *   <li>{@link PaymentGateway} is a <em>command</em> — the unit tests {@code verify} it.</li>
 * </ul>
 *
 * <p>Both are interfaces the application owns, so doubling them is sound. The {@link Money} totals it
 * computes are value objects, used real. The service validates its input before any collaborator is
 * touched, so a rejected order never reaches the payment gateway — the explicit failure path.
 */
public final class OrderService {

    private static final Logger LOG = System.getLogger(OrderService.class.getName());

    private final PriceCatalog catalog;
    private final PaymentGateway gateway;

    /** Observability: orders successfully placed since startup (illustrative, Chapter 45). */
    private final AtomicLong placed = new AtomicLong();

    /** Observability: orders rejected by a contract guard since startup. */
    private final AtomicLong rejected = new AtomicLong();

    /**
     * Creates a service over a price-catalog query port and a payment-gateway command port.
     *
     * @param catalog the price lookup port, never {@code null}
     * @param gateway the payment port, never {@code null}
     * @throws NullPointerException if either port is {@code null}
     */
    public OrderService(PriceCatalog catalog, PaymentGateway gateway) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.gateway = Objects.requireNonNull(gateway, "gateway");
    }

    /**
     * Prices and charges an order, returning a receipt for the charged total.
     *
     * @param orderId the id to record on the receipt and charge against, never {@code null}
     * @param items   the order lines, never {@code null} and never empty
     * @return a receipt describing the charged total, never {@code null}
     * @throws NullPointerException     if {@code orderId} or {@code items} is {@code null}
     * @throws OrderRejectedException   if {@code items} is empty or a SKU is not stocked
     * @throws PaymentDeclinedException if the gateway declines the charge
     * @implSpec Validates every argument and prices every line before charging, so a rejected order
     *     leaves the payment gateway untouched.
     */
    public Receipt place(String orderId, List<LineItem> items) {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(items, "items");
        if (items.isEmpty()) {
            rejected.incrementAndGet();
            throw new OrderRejectedException("empty-order", "an order must have at least one line");
        }

        Money total = total(items);
        gateway.charge(orderId, total);
        placed.incrementAndGet();
        return new Receipt(orderId, total);
    }

    /**
     * Sums the priced lines into a single total.
     *
     * @param items the order lines, never empty
     * @return the order total, never {@code null}
     * @throws OrderRejectedException if a SKU is not stocked by the catalog
     */
    private Money total(List<LineItem> items) {
        Money running = new Money(0L, "USD");
        for (LineItem item : items) {
            Money unit = catalog.priceOf(item.sku())
                .orElseThrow(() -> {
                    rejected.incrementAndGet();
                    return new OrderRejectedException("unknown-sku", "not stocked: " + item.sku());
                });
            running = new Money(running.minorUnits() + unit.times(item.quantity()).minorUnits(),
                running.currency());
        }
        return running;
    }

    /**
     * Health/observability surface: the running count of orders successfully placed.
     *
     * @return the number of placed orders since startup, never negative
     */
    public long placedCount() {
        return placed.get();
    }

    /**
     * Health/observability surface: the running count of orders rejected by a contract guard.
     *
     * @return the number of rejected orders since startup, never negative
     */
    public long rejectedCount() {
        return rejected.get();
    }

    /**
     * A readiness probe: the service is ready when both collaborator ports are wired.
     *
     * @return {@code true} when the service can place orders
     */
    public boolean isReady() {
        boolean ready = catalog != null && gateway != null;
        if (!ready) {
            LOG.log(Level.WARNING, "order service not ready: a collaborator port is unwired");
        }
        return ready;
    }
}
