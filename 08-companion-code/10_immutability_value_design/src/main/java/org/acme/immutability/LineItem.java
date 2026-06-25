package org.acme.immutability;

import java.util.Objects;

/**
 * A single line on an order — an immutable value record.
 *
 * <p>Being a record, it carries the derived {@code equals}/{@code hashCode}/{@code toString} the
 * collections in this module rely on, and its components are themselves immutable ({@link String}
 * and {@link Money}), so a list of {@code LineItem} is a collection of immutable objects, not merely
 * an immutable collection of mutable ones.
 *
 * @param sku   the stock-keeping unit, never {@code null}
 * @param price the unit price, never {@code null}
 */
public record LineItem(String sku, Money price) {

    /**
     * Validates the components so a {@code LineItem} is well-formed for its whole lifetime.
     *
     * @throws NullPointerException if {@code sku} or {@code price} is {@code null}
     */
    public LineItem {
        Objects.requireNonNull(sku, "sku");
        Objects.requireNonNull(price, "price");
    }
}
