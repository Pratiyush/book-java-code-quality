package org.acme.design.inverted.notify;

import java.util.ArrayList;
import java.util.List;
import org.acme.design.inverted.orders.OrderEvents;
import org.acme.design.inverted.orders.OrderService;

/**
 * Announces placed orders by implementing the {@code orders} package's {@link OrderEvents}.
 *
 * <p>The dependency now runs one way: this package depends on {@code orders}, and {@code orders}
 * depends on nothing here. The class is given the {@link OrderService} to read summaries, but because
 * it satisfies an abstraction the stable side owns, no back-edge is created.
 */
public final class OrderNotifier implements OrderEvents {

    private final OrderService orders;
    private final List<String> sent = new ArrayList<>();

    /**
     * Creates the notifier over the order service it reads summaries from.
     *
     * @param orders the order service, never {@code null}
     */
    public OrderNotifier(OrderService orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders must not be null");
        }
        this.orders = orders;
    }

    @Override
    public void orderPlaced(String orderId) {
        sent.add(orders.summaryOf(orderId));
    }

    /**
     * Returns the summaries announced so far.
     *
     * @return a copy of the sent summaries, never {@code null}
     */
    public List<String> sent() {
        return new ArrayList<>(sent);
    }
}
