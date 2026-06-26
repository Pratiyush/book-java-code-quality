package org.acme.refactoring;

import java.util.Objects;

/**
 * A money amount in integral minor units (cents), tagged with a currency — an immutable value record.
 *
 * <p>Both the legacy calculator and the modern refactor speak in this one value type, which is what
 * makes the behaviour-preservation test possible: a refactoring that changes structure must leave the
 * computed {@code Money} unchanged for every input, and a record's derived {@code equals} lets a test
 * assert exactly that. Working in integral minor units rather than a {@code double} also keeps the
 * legacy rounding quirk a deliberate, reproducible fact the characterization test can pin, rather than
 * floating-point noise.
 *
 * @param minorUnits the amount in integral minor units (cents), never negative
 * @param currency   the ISO currency code, never {@code null}
 */
public record Money(long minorUnits, String currency) {

    /**
     * Validates the components so a {@code Money} is well-formed for its whole lifetime.
     *
     * @throws IllegalArgumentException if {@code minorUnits} is negative
     * @throws NullPointerException     if {@code currency} is {@code null}
     */
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (minorUnits < 0) {
            throw new IllegalArgumentException("minorUnits must not be negative: " + minorUnits);
        }
    }

    /**
     * Returns a new amount that is this amount plus the given minor units, in the same currency.
     *
     * @param addend the minor units to add, never negative
     * @return a fresh {@code Money}, never {@code null}
     */
    public Money plus(long addend) {
        return new Money(minorUnits + addend, currency);
    }
}
