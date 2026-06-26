package org.acme.design.byfeature.billing;

import java.util.Objects;
import org.acme.design.byfeature.orders.Order;

/**
 * The billing feature, kept whole in its own package.
 *
 * <p>It needs orders data, and under by-feature slicing that sharing is deliberate: billing reaches
 * the orders feature only through the published {@link Order} record, not by touching its service or
 * storage. The cost the chapter names is exactly this — cross-feature reuse has to go through a narrow
 * surface rather than being free, which is the trade for the cohesion the slicing buys.
 */
public final class BillingService {

    /**
     * Computes tax for an order at the given rate.
     *
     * @param order      the order to bill, read through the orders feature's published type
     * @param taxPercent the tax percentage in {@code [0, 100]}
     * @return the tax due in minor units
     * @throws NullPointerException     if {@code order} is {@code null}
     * @throws IllegalArgumentException if {@code taxPercent} is outside {@code [0, 100]}
     */
    public long taxFor(Order order, int taxPercent) {
        Objects.requireNonNull(order, "order");
        if (taxPercent < 0 || taxPercent > 100) {
            throw new IllegalArgumentException("taxPercent out of range: " + taxPercent);
        }
        return order.totalMinorUnits() * taxPercent / 100;
    }
}
