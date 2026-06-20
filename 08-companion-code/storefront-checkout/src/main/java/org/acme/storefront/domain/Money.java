package org.acme.storefront.domain;

import java.util.Objects;

/**
 * An immutable monetary amount held in <em>minor units</em> (for example, cents) and tagged with a
 * three-letter currency code.
 *
 * <p>Money is a value type (Chapter 8): it has no identity, its state never changes after construction,
 * and equality is by value. Arithmetic is overflow-checked and currency-checked, failing fast
 * (Chapter 10) rather than producing a silently wrong total.
 *
 * @param amountMinor the amount in minor units; must be {@code >= 0}
 * @param currency a three-letter currency code (for example, {@code "USD"}); must not be {@code null}
 */
public record Money(long amountMinor, String currency) {

    /**
     * Validates the components. Defensive checks live at the top of the canonical constructor
     * (Effective Java Item 49) so an invalid {@code Money} can never be constructed.
     */
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (currency.length() != 3) {
            throw new IllegalArgumentException("currency must be a 3-letter code: " + currency);
        }
        if (amountMinor < 0) {
            throw new IllegalArgumentException("amountMinor must be >= 0: " + amountMinor);
        }
    }

    /**
     * Returns a zero amount in the given currency.
     *
     * @param currency the currency code
     * @return {@code Money} of zero
     */
    public static Money zero(String currency) {
        return new Money(0L, currency);
    }

    /**
     * Returns the sum of this amount and {@code other}.
     *
     * @param other the amount to add (same currency)
     * @return a new {@code Money} carrying the sum
     * @throws IllegalArgumentException if the currencies differ
     * @throws ArithmeticException if the sum overflows a {@code long}
     */
    public Money plus(Money other) {
        Objects.requireNonNull(other, "other");
        requireSameCurrency(other);
        return new Money(Math.addExact(amountMinor, other.amountMinor), currency);
    }

    /**
     * Returns this amount multiplied by a non-negative quantity.
     *
     * @param quantity the multiplier; must be {@code >= 0}
     * @return a new {@code Money} carrying the product
     * @throws ArithmeticException if the product overflows a {@code long}
     */
    public Money times(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0: " + quantity);
        }
        return new Money(Math.multiplyExact(amountMinor, (long) quantity), currency);
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "currency mismatch: " + currency + " vs " + other.currency);
        }
    }
}
