package org.acme.orders;

import java.util.Objects;

/**
 * An immutable monetary amount in minor units (cents), with its currency attached.
 *
 * <p>This is a <em>value object</em>: its state never changes after construction, and two instances
 * with the same components are equal. The chapter's doubles section turns on exactly this property —
 * a value object is used real in a test, never mocked, because mocking it would replace verifiable
 * data with a stand-in that proves nothing. The unit tests construct {@code Money} directly.
 *
 * @param minorUnits the amount in the currency's minor unit (for example cents), never negative
 * @param currency   the ISO-4217 currency code, never {@code null}
 */
public record Money(long minorUnits, String currency) {

    /**
     * Validates the components at construction so an invalid {@code Money} can never exist.
     *
     * @throws NullPointerException     if {@code currency} is {@code null}
     * @throws IllegalArgumentException if {@code minorUnits} is negative
     */
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (minorUnits < 0) {
            throw new IllegalArgumentException("minorUnits must not be negative: " + minorUnits);
        }
    }

    /**
     * Returns a new {@code Money} scaled by an integer quantity, in the same currency.
     *
     * @param quantity the number of units to multiply by, never negative
     * @return a new {@code Money} holding the scaled amount
     * @throws IllegalArgumentException if {@code quantity} is negative
     */
    public Money times(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must not be negative: " + quantity);
        }
        return new Money(minorUnits * quantity, currency);
    }
}
