package org.acme.contracttesting;

import java.util.Objects;

/**
 * An immutable order: the domain value the provider serves and the consumer reads.
 *
 * <p>Kept as a record so it cannot drift after construction and needs no defensive copy when it travels
 * across the boundary. The {@code status} is a stable, machine-readable token (for example
 * {@code "CONFIRMED"}) rather than free text, so both sides can agree on it in the contract.
 *
 * @param id     the order identifier, never {@code null}
 * @param status the order status token, never {@code null}
 * @param total  the order total in minor units (cents), never negative
 */
public record Order(String id, String status, long total) {

    /**
     * Validates the components at construction so an invalid {@code Order} can never exist.
     *
     * @throws NullPointerException     if {@code id} or {@code status} is {@code null}
     * @throws IllegalArgumentException if {@code total} is negative
     */
    public Order {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(status, "status");
        if (total < 0) {
            throw new IllegalArgumentException("total must not be negative: " + total);
        }
    }
}
