package org.acme.catalog;

import java.util.Objects;

/**
 * A catalog product: a {@link Sku}, a display name, and a unit {@link Money} price.
 *
 * <p>The thing the catalog stores and the real collaborator returns over HTTP. It is the payload whose
 * wire encoding the integration test exercises against a real server and client — the round trip the
 * chapter's fidelity argument turns on.
 *
 * @param sku   the product's stock-keeping unit, never {@code null}
 * @param name  the display name, never {@code null} or blank
 * @param price the unit price, never {@code null}
 */
public record Product(Sku sku, String name, Money price) {

    /**
     * Validates the components at construction.
     *
     * @throws NullPointerException     if any component is {@code null}
     * @throws IllegalArgumentException if {@code name} is blank
     */
    public Product {
        Objects.requireNonNull(sku, "sku");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(price, "price");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }
}
