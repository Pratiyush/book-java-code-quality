package org.acme.orders;

import java.util.Objects;

/**
 * One line of an order: a SKU and a quantity.
 *
 * <p>A small immutable value object the tests build directly. Like {@link Money}, it is used real in
 * the unit tests rather than mocked.
 *
 * @param sku      the stock-keeping unit being ordered, never {@code null}
 * @param quantity how many units, strictly positive
 */
public record LineItem(String sku, int quantity) {

    /**
     * Validates the components at construction.
     *
     * @throws NullPointerException     if {@code sku} is {@code null}
     * @throws IllegalArgumentException if {@code quantity} is not strictly positive
     */
    public LineItem {
        Objects.requireNonNull(sku, "sku");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
    }
}
