package org.acme.orders;

import java.util.Objects;

/**
 * An immutable monetary amount in minor units (cents), with its currency attached.
 *
 * <p>The compact constructor is the canonical home for invariant guards (Item 49: "check parameters for
 * validity at the beginning of the method body"). The checks run before the components are assigned, so an
 * invalid {@code Money} can never come into existence — a broken precondition fails fast at the call site
 * with an exception that names the offending value, rather than travelling on to a distant, confusing
 * failure. A {@code null} currency is a {@link NullPointerException} (Item 72's standard exception for a
 * null argument); a negative amount is an {@link IllegalArgumentException} carrying the value.
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
    // tag::guard-clause[]
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (minorUnits < 0) {
            throw new IllegalArgumentException("minorUnits must not be negative: " + minorUnits);
        }
    }
    // end::guard-clause[]
}
