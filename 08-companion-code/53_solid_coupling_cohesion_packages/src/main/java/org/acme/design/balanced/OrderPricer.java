package org.acme.design.balanced;

import java.util.Objects;

/**
 * Prices an {@link Order} with a {@link DiscountPolicy}, with no layer the variation does not require.
 *
 * <p>The displayed region below is the whole wiring a caller writes: a record for the data and a
 * chosen policy, no factory in between. Set this beside the over-abstracted package's wiring to see
 * the same result reached with less indirection.
 */
public final class OrderPricer {

    private OrderPricer() {
    }

    // tag::balanced[]
    /** Pricing an order: a record carries the data, a policy is chosen directly — no factory layer. */
    public static long priceOrder(Order order, DiscountPolicy policy) {
        Objects.requireNonNull(order, "order");
        Objects.requireNonNull(policy, "policy");
        return policy.apply(order.subtotalMinorUnits());
    }
    // end::balanced[]
}
