package org.acme.design.cycle.notify;

import java.util.ArrayList;
import java.util.List;
import org.acme.design.cycle.orders.OrderSummaries;

/**
 * Announces placed orders — but reaches back into {@code orders} to read the summary.
 *
 * <p>This back-edge is what closes the cycle: {@code orders} already depends on {@code notify}, and
 * the field and call below make {@code notify} depend on {@code orders} in return, fusing the two
 * packages into one unit that cannot be built or tested apart. Reading through the narrower
 * {@code OrderSummaries} surface still leaves the package dependency in place; only the dependency
 * inversion in {@code org.acme.design.inverted} actually removes it.
 */
public final class OrderNotifier {

    // tag::cycle[]
    // back-edge: notify -> orders. orders already depends on notify, so this closes the cycle.
    private final OrderSummaries orders;
    private final List<String> sent = new ArrayList<>();

    public void announce(String orderId) {
        sent.add(orders.summaryOf(orderId)); // the two packages can no longer be separated
    }
    // end::cycle[]

    /**
     * Creates the notifier over the orders read surface it calls summaries through.
     *
     * @param orders the orders summary surface, never {@code null}
     */
    public OrderNotifier(OrderSummaries orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders must not be null");
        }
        this.orders = orders;
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
