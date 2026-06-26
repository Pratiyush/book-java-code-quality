package org.acme.design.balanced;

/**
 * An order as a value: the data the price is computed from, carried directly by a record.
 *
 * <p>Adding a field (a loyalty tier, a coupon) is a one-line change to this record and the one method
 * that reads it — no interface and no factory to thread it through. That is the cost difference the
 * over-abstracted package makes visible.
 *
 * @param subtotalMinorUnits the order subtotal in minor units, never negative
 * @param itemCount          the number of line items, never negative
 */
public record Order(long subtotalMinorUnits, int itemCount) {

    /**
     * Validates the components so an invalid order cannot exist.
     *
     * @throws IllegalArgumentException if either component is negative
     */
    public Order {
        if (subtotalMinorUnits < 0) {
            throw new IllegalArgumentException("subtotalMinorUnits must not be negative");
        }
        if (itemCount < 0) {
            throw new IllegalArgumentException("itemCount must not be negative");
        }
    }
}
