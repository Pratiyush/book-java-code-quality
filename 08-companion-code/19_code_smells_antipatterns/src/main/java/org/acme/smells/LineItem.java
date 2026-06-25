package org.acme.smells;

import java.util.Objects;

/**
 * A single line on an order — an immutable value record carrying a SKU, a unit price, and a quantity.
 *
 * <p>Being a record, it carries the derived {@code equals}/{@code hashCode}/{@code toString} the
 * domain relies on, and its components are themselves immutable, so a list of {@code LineItem} is a
 * collection of immutable values. The line total is behaviour on the data rather than a free-standing
 * calculation elsewhere — the move away from the <em>Data Class</em> smell (Fowler) that PMD
 * {@code DataClass} names.
 *
 * @param sku      the stock-keeping unit, never {@code null}
 * @param price    the unit price, never {@code null}
 * @param quantity the number of units, at least one
 */
public record LineItem(String sku, Money price, int quantity) {

    /**
     * Validates the components so a {@code LineItem} is well-formed for its whole lifetime.
     *
     * @throws NullPointerException     if {@code sku} or {@code price} is {@code null}
     * @throws IllegalArgumentException if {@code quantity} is not at least one
     */
    public LineItem {
        Objects.requireNonNull(sku, "sku");
        Objects.requireNonNull(price, "price");
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1: " + quantity);
        }
    }

    /**
     * Returns the total for this line — the unit price times the quantity.
     *
     * @return the line total, never {@code null}
     */
    public Money lineTotal() {
        return price.times(quantity);
    }
}
