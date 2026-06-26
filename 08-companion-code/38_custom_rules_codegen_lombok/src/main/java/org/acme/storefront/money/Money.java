package org.acme.storefront.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * A monetary amount as a dedicated value type: the second of the five realizations of the house
 * invariant, and the compiler-derived answer in the codegen comparison.
 *
 * <p>The invariant is enforced by the <em>shape of the type</em>, not by a separate rule. The amount
 * component is a {@link BigDecimal}, so a caller cannot construct money from a raw {@code double} — there
 * is no constructor that accepts one. The compact constructor adds the remaining guards (non-null,
 * non-negative, no fractional unit beyond the currency's precision). Because this is a {@code record},
 * the compiler derives the canonical constructor, the accessors, {@code equals}, {@code hashCode}, and
 * {@code toString} — the boilerplate the codegen half of the chapter is about — with no dependency and no
 * generated file. Its scope is exactly the transparent-carrier slice: it cannot extend a class and is
 * shallowly immutable, so builders, mutable entities, and mappers are out of scope (Chapter 17 routes
 * those elsewhere).
 *
 * @param amount   the amount, never {@code null} and never negative
 * @param currency the ISO-4217 currency, never {@code null}
 */
// tag::record-money[]
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative: " + amount);
        }
    }
}
// end::record-money[]
