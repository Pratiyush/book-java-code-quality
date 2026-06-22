package org.acme.commerce.order;

import java.util.Objects;

/**
 * A single order line: a product and how many of it (Part: input validation). The compact
 * constructor rejects a non-positive quantity at construction, so no later code has to re-check it.
 *
 * @param productId the catalog product id
 * @param quantity  the number ordered (must be positive)
 */
public record OrderItem(String productId, int quantity) {

    public OrderItem {
        Objects.requireNonNull(productId, "productId");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
    }
}
