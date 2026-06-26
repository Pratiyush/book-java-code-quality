package org.acme.design.cycle.orders;

/**
 * The read surface {@code notify} calls back into. It lives in {@code orders}, and {@code orders}
 * still depends on {@code notify} to announce orders — so introducing it does not break the cycle, it
 * only narrows what the back-edge can reach. (Contrast {@code org.acme.design.inverted}, where the
 * abstraction the stable side owns lets {@code orders} depend on nothing in {@code notify} at all.)
 */
@FunctionalInterface
public interface OrderSummaries {

    /**
     * Returns a short summary of a placed order.
     *
     * @param orderId the order identifier
     * @return a human-readable summary, never {@code null}
     */
    String summaryOf(String orderId);
}
