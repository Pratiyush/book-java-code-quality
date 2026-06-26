package org.acme.design.inverted.orders;

/**
 * The abstraction the stable {@code orders} package owns; {@code notify} implements it, so the
 * dependency now points into {@code orders} rather than back out of it.
 */
// tag::dip-inversion[]
@FunctionalInterface
public interface OrderEvents {        // owned by the stable side; notify implements this

    /** Called when an order has been placed. */
    void orderPlaced(String orderId); // dependency now points INTO orders, breaking the cycle
}
// end::dip-inversion[]
