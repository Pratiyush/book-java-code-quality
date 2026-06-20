package org.acme.storefront.domain;

import java.util.Objects;

/**
 * A line in a shopping cart: a product reference and a positive quantity.
 *
 * @param productId the referenced {@link Product#id()}; must not be blank
 * @param quantity the number of units; must be {@code >= 1}
 */
public record CartItem(String productId, int quantity) {

    /** Validates the components (fail-fast, Chapter 10). */
    public CartItem {
        Objects.requireNonNull(productId, "productId");
        if (productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be >= 1: " + quantity);
        }
    }
}
