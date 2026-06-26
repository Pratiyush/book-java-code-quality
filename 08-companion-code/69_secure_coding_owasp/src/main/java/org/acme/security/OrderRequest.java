package org.acme.security;

import java.util.Objects;

/**
 * A flat data carrier for an incoming storefront order request. It holds only the fields the request
 * is allowed to carry, validated at construction, with no behavior and no polymorphism — the shape a
 * JSON or Protobuf decoder would map untrusted bytes onto when polymorphic typing is disabled. It
 * carries no reference to {@link java.io.Serializable}, so request bytes can never reconstitute an
 * arbitrary object graph through it.
 *
 * @param customerId the ordering customer's id, never {@code null} or blank
 * @param sku        the product code, never {@code null} or blank
 * @param quantity   the ordered quantity, at least one
 */
public record OrderRequest(String customerId, String sku, int quantity) {

    /**
     * Validates the fields so a malformed request cannot become an {@code OrderRequest}.
     *
     * @throws NullPointerException     if {@code customerId} or {@code sku} is {@code null}
     * @throws IllegalArgumentException if a field is blank or {@code quantity} is below one
     */
    public OrderRequest {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(sku, "sku");
        if (customerId.isBlank() || sku.isBlank()) {
            throw new IllegalArgumentException("customerId and sku must not be blank");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1: " + quantity);
        }
    }
}
