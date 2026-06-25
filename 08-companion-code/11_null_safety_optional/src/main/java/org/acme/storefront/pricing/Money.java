package org.acme.storefront.pricing;

import java.util.Objects;

/**
 * An immutable monetary amount in minor units (cents), with its currency attached.
 *
 * <p>Item 17 ("minimize mutability") in one type: the state never changes after construction, so the
 * amount is automatically thread-safe and never needs a defensive copy when it travels in or out of
 * another object. Holding the currency next to the amount keeps a caller from combining two different
 * currencies by accident — a contract the type carries rather than the documentation. Inside this
 * {@code @NullMarked} package the {@code currency} component is non-null by default; the constructor
 * still guards it so the failure is loud at the boundary even when an unchecked caller ignores the
 * signature.
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
     * Returns a new {@code Money} reduced by {@code amount}, clamped at zero, in the same currency.
     *
     * @param amount the amount to subtract, in minor units, never negative
     * @return a new {@code Money} holding the reduced amount, never below zero
     * @throws IllegalArgumentException if {@code amount} is negative
     */
    public Money minus(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must not be negative: " + amount);
        }
        return new Money(Math.max(0L, minorUnits - amount), currency);
    }
}
