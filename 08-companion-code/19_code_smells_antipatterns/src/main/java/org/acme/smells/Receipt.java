package org.acme.smells;

import java.util.Objects;

/**
 * The outcome of placing an order — an immutable record describing what was charged.
 *
 * <p>Both the smelly and the refactored services return this same value type, which is what makes the
 * behaviour-preservation test possible: a refactoring that changes structure must leave the
 * {@code Receipt} unchanged for every input, and a record's derived {@code equals} lets the test assert
 * exactly that. Returning a typed result rather than a primitive total also keeps the subtotal,
 * discount, shipping, and total legible at the call site instead of forcing the caller to remember
 * which {@code long} meant what.
 *
 * @param orderId  the order identifier, never {@code null}
 * @param subtotal the sum of the line totals before discount or shipping, never {@code null}
 * @param discount the loyalty discount applied, never {@code null}
 * @param shipping the shipping charge applied, never {@code null}
 * @param total    the final amount charged (subtotal minus discount plus shipping), never {@code null}
 */
public record Receipt(String orderId, Money subtotal, Money discount, Money shipping, Money total) {

    /**
     * Validates that every component is present.
     *
     * @throws NullPointerException if any component is {@code null}
     */
    public Receipt {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(subtotal, "subtotal");
        Objects.requireNonNull(discount, "discount");
        Objects.requireNonNull(shipping, "shipping");
        Objects.requireNonNull(total, "total");
    }
}
