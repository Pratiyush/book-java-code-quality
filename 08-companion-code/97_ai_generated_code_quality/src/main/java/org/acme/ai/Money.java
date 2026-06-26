package org.acme.ai;

import java.util.Objects;

/**
 * A minimal money value held in integer minor units (for example cents) with an ISO currency code, so
 * the order arithmetic in {@link OrderTotals} is exact rather than binary-floating-point. The module
 * keeps its own small value type rather than importing another chapter's, because no companion module
 * depends on another's code (the shared domain is a narrative motif, not a code dependency).
 *
 * @param minorUnits the amount in minor units (cents), never negative for the values this module uses
 * @param currency   the ISO-4217 currency code, never {@code null}
 */
record Money(long minorUnits, String currency) {

    Money {
        Objects.requireNonNull(currency, "currency");
    }

    /**
     * Adds another amount of the same currency.
     *
     * @param other the amount to add, in the same currency
     * @return the sum
     * @throws IllegalArgumentException if the currencies differ
     */
    Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(minorUnits + other.minorUnits, currency);
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "currency mismatch: " + currency + " vs " + other.currency);
        }
    }
}
