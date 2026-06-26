package org.acme.storefront.domain;

import java.util.Objects;

/**
 * A typed order identifier.
 *
 * @param value the non-blank identifier string, never {@code null}
 */
public record OrderId(String value) {

    /**
     * Validates the identifier.
     *
     * @throws NullPointerException     if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} is blank
     */
    public OrderId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("order id must not be blank");
        }
    }
}
