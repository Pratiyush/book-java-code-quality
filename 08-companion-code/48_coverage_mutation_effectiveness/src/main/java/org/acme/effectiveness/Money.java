package org.acme.effectiveness;

import java.util.Objects;

/**
 * A minimal money value object in minor units (cents), so the discount arithmetic the chapter
 * mutates stays in exact integer math rather than binary floating point.
 *
 * <p>The type is an immutable {@code record} used real throughout the module — never mocked. It
 * exists only to give {@link Discount} a value to compute on, in exact integer math, so the
 * discount arithmetic PITest's {@code MATH} mutator targets stays free of floating-point noise.
 *
 * @param minorUnits the amount in the currency's minor unit (cents), never negative
 * @param currency   the ISO currency code, never {@code null} or blank
 */
public record Money(long minorUnits, String currency) {

    /**
     * Canonical constructor with fail-fast guards (the explicit failure path at the value level).
     *
     * @throws NullPointerException     if {@code currency} is {@code null}
     * @throws IllegalArgumentException if {@code minorUnits} is negative or {@code currency} is blank
     */
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (minorUnits < 0L) {
            throw new IllegalArgumentException("amount must not be negative: " + minorUnits);
        }
        if (currency.isBlank()) {
            throw new IllegalArgumentException("currency must not be blank");
        }
    }
}
