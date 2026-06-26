package org.acme.storefront.domain;

import java.util.Objects;

/**
 * A minor-unit money amount with a currency, the innermost value type in the layering.
 *
 * <p>The domain layer is the centre of the dependency graph: it depends on nothing in this module
 * except the JDK, which is what lets the architecture rules treat {@code ..domain..} as a layer that
 * may be accessed by the others but accesses none of them. Keeping the type a {@code record} with a
 * validating compact constructor is the chapter's separate point (immutability); here it earns its
 * place by having no outward edges for ArchUnit to object to.
 *
 * @param minorUnits the amount in the currency's minor units, never negative
 * @param currency   the ISO currency code, never {@code null}
 */
public record Money(long minorUnits, String currency) {

    /**
     * Validates the amount and currency.
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
     * Returns a new amount with {@code other} added, in the same currency.
     *
     * @param other the amount to add, never {@code null} and of the same currency
     * @return the summed amount, never {@code null}
     * @throws IllegalArgumentException if the currencies differ
     */
    public Money plus(Money other) {
        Objects.requireNonNull(other, "other");
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("currency mismatch: " + currency + " vs " + other.currency);
        }
        return new Money(minorUnits + other.minorUnits, currency);
    }
}
