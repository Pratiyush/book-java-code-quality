package org.acme.design.inverted.orders;

import java.util.HashMap;
import java.util.Map;

/**
 * Places orders and signals the {@link OrderEvents} abstraction it owns.
 *
 * <p>Where the cycle variant imports {@code notify}, this class imports nothing from it: it depends
 * on the {@code OrderEvents} interface in its own package. Whoever announces orders implements that
 * interface, so the dependency points inward and the two packages are independent again.
 */
public final class OrderService {

    private final Map<String, PlacedOrder> orders = new HashMap<>();
    private final OrderEvents events;

    /**
     * Creates the service over an events listener.
     *
     * @param events the listener notified when an order is placed, never {@code null}
     */
    public OrderService(OrderEvents events) {
        if (events == null) {
            throw new IllegalArgumentException("events must not be null");
        }
        this.events = events;
    }

    /**
     * Records an order and signals it through the owned abstraction.
     *
     * @param order the order to place, never {@code null}
     */
    public void place(PlacedOrder order) {
        orders.put(order.id(), order);
        events.orderPlaced(order.id());
    }

    /**
     * Returns a short summary of a placed order.
     *
     * @param orderId the order identifier
     * @return a human-readable summary, or {@code "unknown order"} if none matches
     */
    public String summaryOf(String orderId) {
        PlacedOrder order = orders.get(orderId);
        return order == null ? "unknown order" : "order " + order.id() + " for " + order.totalMinorUnits();
    }
}
