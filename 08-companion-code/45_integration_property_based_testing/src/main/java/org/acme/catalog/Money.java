package org.acme.catalog;

import java.util.Objects;

/**
 * An immutable monetary amount in minor units (cents), with its currency attached.
 *
 * <p>A value object, used real in the tests: the property and parameterized tests construct
 * {@code Money} directly rather than doubling it, because mocking data proves nothing.
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
}
