package org.acme.storefront.domain;

import java.util.Objects;

/**
 * An order with an identifier and a total, the aggregate the other layers move around.
 *
 * @param id    the order identifier, never {@code null}
 * @param total the order total, never {@code null}
 */
public record Order(OrderId id, Money total) {

    /**
     * Validates the order.
     *
     * @throws NullPointerException if {@code id} or {@code total} is {@code null}
     */
    public Order {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(total, "total");
    }
}
