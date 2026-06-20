package org.acme.storefront.domain;

import java.util.Objects;

/**
 * A catalog product: an identifier, a display name, and an immutable {@link Money} price.
 *
 * @param id the stable product identifier; must not be blank
 * @param name the human-readable name; must not be blank
 * @param price the unit price; must not be {@code null}
 */
public record Product(String id, String name, Money price) {

    /** Validates the components (fail-fast, Chapter 10). */
    public Product {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(price, "price");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }
}
