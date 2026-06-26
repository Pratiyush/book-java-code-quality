package org.acme.storefront.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * The codegen comparison, hand-written side: the same value type as {@link Money}, but with the
 * canonical constructor, accessors, {@code equals}, {@code hashCode}, and {@code toString} written out
 * by hand. It carries the same correct representation (a {@link BigDecimal} amount, so it still satisfies
 * the house invariant), so the comparison is purely about <em>who writes the boilerplate</em>.
 *
 * <p>This is the volume the chapter's codegen half is about: every component must be repeated in four
 * places, and the classic latent bug is an {@code equals}/{@code hashCode} that silently forgets a field
 * added later. {@link Money}, a {@code record}, has the compiler derive all of it with none of that risk;
 * the new-file processors (AutoValue, Immutables, MapStruct) and Lombok are other approaches to removing
 * the same toil, described in the chapter prose because none is a SOURCE-PIN row. The module's test
 * asserts this hand-written type and the record agree value-for-value, which is the correctness claim a
 * generator makes — here checked rather than assumed.
 */
public final class HandWrittenMoney {

    private final BigDecimal amount;
    private final Currency currency;

    /**
     * @param amount   the amount, never {@code null} and never negative
     * @param currency the currency, never {@code null}
     */
    public HandWrittenMoney(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount, "amount");
        this.currency = Objects.requireNonNull(currency, "currency");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative: " + amount);
        }
    }

    /** @return the amount */
    public BigDecimal amount() {
        return amount;
    }

    /** @return the currency */
    public Currency currency() {
        return currency;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HandWrittenMoney that)) {
            return false;
        }
        return amount.equals(that.amount) && currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return "HandWrittenMoney[amount=" + amount + ", currency=" + currency + "]";
    }
}
