package org.acme.performance;

import java.util.Objects;

/**
 * One line of a storefront order: a SKU, a unit price in minor units (cents), and a quantity.
 *
 * <p>An immutable record — a small, short-lived value of the kind the generational hypothesis is
 * built around (most objects die young). Whether constructing one actually allocates on the heap is a
 * question for measurement, not assertion: the JIT may scalar-replace a {@code LineItem} that never
 * escapes the method that builds it, which is one of the chapter's reasons to profile allocation
 * rather than guess at it.
 *
 * @param sku        the stock-keeping unit, never {@code null} or blank
 * @param unitMinor  the unit price in minor units (cents), never negative
 * @param quantity   the quantity ordered, at least one
 */
public record LineItem(String sku, long unitMinor, int quantity) {

    /** Validates the line at construction; a value type that cannot hold a nonsensical state. */
    public LineItem {
        Objects.requireNonNull(sku, "sku");
        if (sku.isBlank()) {
            throw new IllegalArgumentException("sku must not be blank");
        }
        if (unitMinor < 0) {
            throw new IllegalArgumentException("unitMinor must not be negative: " + unitMinor);
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1: " + quantity);
        }
    }

    /** The extended price of the line (unit price times quantity), in minor units. */
    public long extendedMinor() {
        return unitMinor * quantity;
    }
}
