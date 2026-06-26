package org.acme.readability;

import java.util.Objects;

/**
 * A money amount in integral minor units (cents), tagged with a currency — an immutable value record.
 *
 * <p>It is here so the discount rule works in real values rather than bare {@code long} primitives
 * standing in for money. Being a record, it derives {@code equals}/{@code hashCode}/{@code toString} by
 * construction (one of the language's own readability moves, Chapter 2: records collapse a data
 * carrier's boilerplate so the intent is obvious), so two equal amounts compare and hash alike — which
 * is what lets a behaviour-preservation test assert that three differently-shaped rules return the same
 * {@code Money}.
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
}
