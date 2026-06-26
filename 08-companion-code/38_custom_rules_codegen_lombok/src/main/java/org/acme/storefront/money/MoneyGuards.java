package org.acme.storefront.money;

import java.math.BigDecimal;

/**
 * Realization one: the house invariant as a hand-written runtime guard, with no rule framework at all.
 *
 * <p>This is the shared select&rarr;predicate&rarr;report&rarr;gate shape expressed directly in plain
 * Java: a factory <em>selects</em> the incoming representation, <em>predicates</em> that it is a
 * floating-point type, and <em>reports</em> by throwing so the bad value never becomes a
 * {@link Money}. It is the most portable form — it needs nothing on the classpath — and the most
 * limited: it only fires when this factory is actually called at run time, so a {@code double} stored
 * straight into some other type never reaches it. That limit is exactly why the other four realizations
 * exist, each watching a different artifact.
 */
public final class MoneyGuards {

    private MoneyGuards() {
    }

    /**
     * Builds {@link Money} from a string amount, rejecting the floating-point precision trap at the
     * boundary. {@code BigDecimal.valueOf(double)} is used for the conversion the guard permits, never
     * {@code new BigDecimal(double)} (which captures the binary rounding error) — the same fix the
     * chapter's Refaster example performs.
     *
     * @param amount   the amount as an exact decimal string, never {@code null}
     * @param currency the ISO-4217 currency code, never {@code null}
     * @return the validated money value
     * @throws IllegalArgumentException if the amount is not a valid exact decimal
     */
    // tag::hand-written-guard[]
    public static Money of(String amount, String currency) {
        // select the input, predicate it is exact, report by refusing to build an invalid Money
        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException("money amount must be a non-blank decimal string");
        }
        return new Money(new BigDecimal(amount), java.util.Currency.getInstance(currency));
    }
    // end::hand-written-guard[]

    /**
     * The predicate the guard rests on, exposed so the rule tests can assert it directly: a
     * {@code double}/{@code float} (or their boxed forms) is never an acceptable money representation.
     *
     * @param representationType the declared type a value uses to carry money, never {@code null}
     * @return {@code true} if the type is a floating-point money representation and so breaches the rule
     */
    public static boolean isFloatingPointMoney(Class<?> representationType) {
        return representationType == double.class
            || representationType == float.class
            || representationType == Double.class
            || representationType == Float.class;
    }
}
