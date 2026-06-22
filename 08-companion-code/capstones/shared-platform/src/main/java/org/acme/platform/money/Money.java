package org.acme.platform.money;

import java.util.Objects;

/**
 * A minor-unit money amount (Part: value modelling). Money is stored as a {@code long} count of the
 * currency's minor unit (cents) plus an ISO-4217 currency code — never as a {@code double}, because
 * binary floating point cannot represent most decimal money amounts exactly. Amounts are immutable;
 * arithmetic returns new instances and rejects a currency mismatch fail-fast.
 *
 * @param minorUnits the amount in the currency's smallest unit (e.g. cents); may be negative
 * @param currency   the ISO-4217 currency code, e.g. {@code "USD"}
 */
public record Money(long minorUnits, String currency) {

    public Money {
        Objects.requireNonNull(currency, "currency");
        if (currency.length() != 3) {
            throw new IllegalArgumentException("currency must be a 3-letter ISO-4217 code: " + currency);
        }
    }

    public static Money of(long minorUnits, String currency) {
        return new Money(minorUnits, currency);
    }

    public static Money zero(String currency) {
        return new Money(0L, currency);
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(Math.addExact(minorUnits, other.minorUnits), currency);
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        return new Money(Math.subtractExact(minorUnits, other.minorUnits), currency);
    }

    public Money times(long quantity) {
        return new Money(Math.multiplyExact(minorUnits, quantity), currency);
    }

    public boolean isNegative() {
        return minorUnits < 0L;
    }

    public boolean isGreaterThan(Money other) {
        requireSameCurrency(other);
        return minorUnits > other.minorUnits;
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "currency mismatch: " + currency + " vs " + other.currency);
        }
    }

    /** A display form like {@code "12.34 USD"} — for logs and responses, never for arithmetic. */
    public String toDisplayString() {
        long abs = Math.abs(minorUnits);
        long major = abs / 100L;
        long minor = abs % 100L;
        return "%s%d.%02d %s".formatted(minorUnits < 0L ? "-" : "", major, minor, currency);
    }
}
