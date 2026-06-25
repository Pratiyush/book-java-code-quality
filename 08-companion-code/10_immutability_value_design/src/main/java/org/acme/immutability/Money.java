package org.acme.immutability;

import java.util.Comparator;
import java.util.Objects;

/**
 * A monetary amount in minor units (cents) with its currency attached — the chapter's worked value
 * type, expressed as a record so the language derives the contracts instead of a developer writing
 * them.
 *
 * <p>This is Effective Java Item 17 ("minimize mutability") realised by JEP 395: the components are
 * {@code final}, the record is implicitly {@code final}, and {@code equals}/{@code hashCode}/{@code
 * toString} are derived component-by-component, so the {@code HashMap}-loses-a-key failure that
 * {@link BrokenPrice} demonstrates cannot occur here. The value is therefore safe to share, cache,
 * and use as a map key without a defensive thought.
 *
 * <p>Records do <em>not</em> derive {@link Comparable}, so the natural ordering is written by hand —
 * but with {@link Comparator} combinators rather than {@code int} subtraction, which keeps the
 * {@code compareTo} contract's sign rule intact and cannot overflow. Ordering is consistent with
 * {@code equals} here, so no "inconsistent with equals" caveat is documented (unlike {@code
 * BigDecimal}, whose {@code compareTo} ignores scale).
 *
 * @param minorUnits the amount in the currency's minor unit (for example cents), never negative
 * @param currency   the ISO-4217 currency code, never {@code null}
 */
// tag::value-money[]
public record Money(long minorUnits, String currency) implements Comparable<Money> {

    public Money {                                  // compact constructor: validate, then store
        Objects.requireNonNull(currency, "currency");
        if (minorUnits < 0) {
            throw new IllegalArgumentException("minorUnits must not be negative: " + minorUnits);
        }
    }
    // end::value-money[]

    private static final Comparator<Money> ORDER =
        Comparator.comparing(Money::currency).thenComparingLong(Money::minorUnits);

    /**
     * Orders by currency, then by amount, using {@link Comparator} combinators so the result obeys
     * the {@code compareTo} sign contract without the overflow an {@code int} subtraction risks.
     *
     * @param other the amount to compare against, never {@code null}
     * @return a negative integer, zero, or a positive integer as this amount is less than, equal to,
     *     or greater than {@code other}
     */
    @Override
    public int compareTo(Money other) {
        return ORDER.compare(this, other);
    }

    /**
     * Returns a new {@code Money} increased by {@code amount}, in the same currency.
     *
     * @param amount the amount to add, in minor units, never negative
     * @return a new {@code Money} holding the increased amount
     * @throws IllegalArgumentException if {@code amount} is negative
     */
    public Money plus(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative: " + amount);
        }
        return new Money(minorUnits + amount, currency);
    }
}
