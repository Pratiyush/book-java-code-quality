package org.acme.refactoring;

import java.util.Objects;

/**
 * The outcome of a shipping quote — a {@code sealed} result with exactly two cases.
 *
 * <p>The legacy calculator signalled "no rate for this destination" by returning a zero amount, an
 * in-band sentinel a caller can mistake for free shipping. The modern refactor models the outcome as a
 * sealed hierarchy — {@link Priced} or {@link Unavailable} — so the two cases are distinct types the
 * compiler tracks. A {@code switch} over a sealed type is exhaustive without a {@code default}, so
 * adding a third outcome later becomes a compile error at every call site rather than a silent
 * fall-through. This is one of the modern-Java idioms (sealed types + pattern matching, Chapter 5) that
 * supersedes a 2018-era manual catalog step: where the catalog might have reached for a type hierarchy
 * and a visitor, the language now expresses the closed set directly.
 */
public sealed interface Quote permits Quote.Priced, Quote.Unavailable {

    /**
     * A successful quote carrying the priced amount.
     *
     * @param amount the shipping charge, never {@code null}
     */
    record Priced(Money amount) implements Quote {

        /**
         * Validates the component.
         *
         * @throws NullPointerException if {@code amount} is {@code null}
         */
        public Priced {
            Objects.requireNonNull(amount, "amount");
        }
    }

    /**
     * A quote that could not be priced because the destination is not served — the modern path's
     * explicit, typed failure outcome, distinct from a zero charge.
     *
     * @param destination the unserved destination zone, never {@code null}
     * @param reason      a stable, machine-readable reason code, never {@code null}
     */
    record Unavailable(String destination, String reason) implements Quote {

        /**
         * Validates the components.
         *
         * @throws NullPointerException if {@code destination} or {@code reason} is {@code null}
         */
        public Unavailable {
            Objects.requireNonNull(destination, "destination");
            Objects.requireNonNull(reason, "reason");
        }
    }
}
