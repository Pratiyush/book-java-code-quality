package org.acme.review;

import java.util.Objects;

/**
 * The exemplar of the chapter's documentation standard: Javadoc as a <em>contract</em> on the public API,
 * not narration of obvious code. A storefront refund rule small enough to read in one sitting, documented
 * the way the chapter argues a public surface should be — every public type and method states what it
 * does, what it requires of its inputs (including nullability), what it guarantees about its output, and
 * which exceptions it raises and when. That is the contract a caller relies on without reading the body.
 *
 * <p>What this class deliberately does <strong>not</strong> do is restate the obvious in prose: there is
 * no {@code // increment i} narration and no Javadoc that merely repeats a getter's name. The honest edge
 * the chapter names lives here too — the {@code quality} profile's Checkstyle Javadoc rules confirm these
 * comments are present and well-formed; they cannot confirm the prose is true. That a documented method
 * actually behaves as documented is what the tests assert and what a human reviewer judges.
 */
public final class RefundPolicy {

    /** Refund window: a line returned within this many days of purchase is eligible. */
    public static final int REFUND_WINDOW_DAYS = 30;

    private final long linePriceCents;

    /**
     * Creates a refund policy for one order line at a fixed price.
     *
     * @param linePriceCents the line's price in cents; must be {@code >= 0}
     * @throws IllegalArgumentException if {@code linePriceCents} is negative
     */
    public RefundPolicy(long linePriceCents) {
        if (linePriceCents < 0) {
            throw new IllegalArgumentException("linePriceCents must be >= 0, was " + linePriceCents);
        }
        this.linePriceCents = linePriceCents;
    }

    // tag::javadoc-contract[]
    /**
     * Returns the refund due for this line when it is returned, in cents.
     *
     * @param daysSincePurchase whole days since purchase; must be {@code >= 0}
     * @return the refund in cents, never negative; {@code 0} once the window has closed
     * @throws IllegalArgumentException if {@code daysSincePurchase} is negative
     */
    public long refundCents(int daysSincePurchase) {
        // end::javadoc-contract[]
        if (daysSincePurchase < 0) {
            throw new IllegalArgumentException(
                "daysSincePurchase must be >= 0, was " + daysSincePurchase);
        }
        // The failure path is a defined, benign value, not an exception: outside the window the refund is
        // zero -- a caller handles a number, never a thrown control-flow signal for an ordinary outcome.
        if (daysSincePurchase > REFUND_WINDOW_DAYS) {
            return 0L;
        }
        return linePriceCents;
    }

    /**
     * Returns whether a return at the given age is still within the refund window.
     *
     * @param daysSincePurchase whole days since purchase; must be {@code >= 0}
     * @return {@code true} if a refund is due, {@code false} once the window has closed
     * @throws IllegalArgumentException if {@code daysSincePurchase} is negative
     */
    public boolean isWithinWindow(int daysSincePurchase) {
        return refundCents(daysSincePurchase) > 0L;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RefundPolicy policy && policy.linePriceCents == this.linePriceCents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(linePriceCents);
    }
}
