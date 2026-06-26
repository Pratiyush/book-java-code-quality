package org.acme.orders;

import java.util.Objects;

/**
 * The immutable result of a successfully placed order.
 *
 * <p>An order's outcome expressed as data the tests can assert on directly — the AssertJ, Hamcrest
 * and built-in styles in the test class all assert against {@link #total()}. Returning a value
 * object, rather than mutating a passed-in argument, is what lets the happy-path test use plain state
 * verification.
 *
 * @param orderId the placed order's id, never {@code null}
 * @param total   the charged total, never {@code null}
 */
public record Receipt(String orderId, Money total) {

    /**
     * Validates the components at construction.
     *
     * @throws NullPointerException if {@code orderId} or {@code total} is {@code null}
     */
    public Receipt {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(total, "total");
    }
}
