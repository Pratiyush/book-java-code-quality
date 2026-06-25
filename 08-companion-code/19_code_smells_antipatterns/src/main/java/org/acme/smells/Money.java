package org.acme.smells;

import java.util.Objects;

/**
 * A money amount in integral minor units (cents), tagged with a currency — an immutable value record.
 *
 * <p>It exists so the order domain works in real values rather than {@code long} primitives standing in
 * for money, which is the <em>Primitive Obsession</em> smell the chapter names (Effective Java Item 62:
 * avoid strings — and bare numbers — where a real type belongs). Being a record, it derives
 * {@code equals}/{@code hashCode}/{@code toString} by construction, so two equal amounts compare and
 * hash alike and a behaviour-preservation test can assert receipts are equal across a refactoring.
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

    /**
     * Returns a new amount equal to this amount multiplied by a non-negative count, same currency.
     *
     * @param count the multiplier, never negative
     * @return a fresh {@code Money}, never {@code null}
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public Money times(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must not be negative: " + count);
        }
        return new Money(minorUnits * count, currency);
    }
}
