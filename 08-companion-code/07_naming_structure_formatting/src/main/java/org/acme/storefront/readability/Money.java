package org.acme.storefront.readability;

import java.util.Objects;

/**
 * An immutable monetary amount in minor units (cents) with its currency attached.
 *
 * <p>Self-contained on purpose: this module depends on no other companion module, so it carries its
 * own small value type. The name says what it is and the components say what they hold — the
 * intention-revealing-naming point of the chapter at the type level, not just the field level.
 *
 * @param minorUnits the amount in the currency's minor unit (for example cents), never negative
 * @param currency the ISO-4217 currency code, never {@code null}
 */
public record Money(long minorUnits, String currency) {

    /**
     * Validates the components at construction so an invalid {@code Money} can never exist.
     *
     * @throws NullPointerException if {@code currency} is {@code null}
     * @throws IllegalArgumentException if {@code minorUnits} is negative
     */
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (minorUnits < 0) {
            throw new IllegalArgumentException("minorUnits must not be negative: " + minorUnits);
        }
    }

    /**
     * Returns a new {@code Money} scaled by a whole quantity, in the same currency.
     *
     * @param quantity the multiplier, never negative
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
