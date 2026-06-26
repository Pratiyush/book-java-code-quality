package org.acme.design.cycle.orders;

import java.util.HashMap;
import java.util.Map;
import org.acme.design.cycle.notify.OrderNotifier;

/**
 * Places orders and asks the {@code notify} package to announce them.
 *
 * <p>This is one half of the cycle: {@code orders} depends on {@code notify} here, and {@code notify}
 * depends back on {@code orders} to read the order summary. The dependency from here to {@code notify}
 * is the reasonable direction; the back-edge in {@link OrderNotifier} is what closes the loop. The
 * notifier is set after construction rather than passed in — not a style choice but a consequence of
 * the cycle, since each side needs the other to exist first.
 */
public final class OrderService implements OrderSummaries {

    private final Map<String, PlacedOrder> orders = new HashMap<>();
    private OrderNotifier notifier;

    /**
     * Sets the notifier that announces placed orders. Required before {@link #place(PlacedOrder)}.
     *
     * @param orderNotifier the notifier, never {@code null}
     */
    public void setNotifier(OrderNotifier orderNotifier) {
        if (orderNotifier == null) {
            throw new IllegalArgumentException("notifier must not be null");
        }
        this.notifier = orderNotifier;
    }

    /**
     * Records an order and announces it.
     *
     * @param order the order to place, never {@code null}
     * @throws IllegalStateException if the notifier has not been set
     */
    public void place(PlacedOrder order) {
        if (notifier == null) {
            throw new IllegalStateException("notifier not set");
        }
        orders.put(order.id(), order);
        notifier.announce(order.id());
    }

    /**
     * Returns a short summary of a placed order — the method the notifier calls back into.
     *
     * @param orderId the order identifier
     * @return a human-readable summary, or {@code "unknown order"} if none matches
     */
    @Override
    public String summaryOf(String orderId) {
        PlacedOrder order = orders.get(orderId);
        return order == null ? "unknown order" : "order " + order.id() + " for " + order.totalMinorUnits();
    }
}
