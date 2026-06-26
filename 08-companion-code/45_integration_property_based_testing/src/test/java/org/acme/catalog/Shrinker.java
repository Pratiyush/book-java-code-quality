package org.acme.catalog;

import java.util.function.IntPredicate;

/**
 * A minimal integer shrinker, standing in for the shrinking half of a property-based test.
 *
 * <p>When a property fails on some generated input, a raw failing value (a 4,000-character string, a
 * seven-digit number) is hard to debug. Shrinking is the feature that makes property-based testing
 * debuggable: it reduces a failing input to the <em>smallest</em> input that still fails, the minimal
 * counterexample. A dedicated library (jqwik) does this for arbitrary generated types; this class shows
 * the idea on a single dimension — an integer — using a delta-debugging-style search toward zero, so
 * the chapter's shrinking concept is realized runnably with no extra dependency.
 *
 * <p>{@code failsFor} is the property's negation: it returns {@code true} for an input that breaks the
 * property. The shrinker assumes a known failing seed and returns the minimal failing value at or above
 * the lower bound.
 */
final class Shrinker {

    private Shrinker() {
    }

    /**
     * Shrinks a known-failing integer toward {@code lowerBound}, returning the smallest value at or
     * above the bound for which {@code failsFor} still holds.
     *
     * <p>The search halves the gap between the lower bound and the current failing value, keeping the
     * smaller value whenever it still fails — the same converge-to-the-minimal-counterexample behaviour
     * a property-based library performs, here on one integer dimension.
     *
     * @param failingSeed a value already known to fail the property (to make {@code failsFor} true)
     * @param lowerBound  the smallest value the search may return
     * @param failsFor    the property's negation: {@code true} when the input breaks the property
     * @return the minimal failing value at or above {@code lowerBound}
     * @throws IllegalArgumentException if {@code failingSeed} does not actually fail, or
     *                                  {@code failingSeed} is below {@code lowerBound}
     */
    static int shrinkToMinimum(int failingSeed, int lowerBound, IntPredicate failsFor) {
        if (failingSeed < lowerBound) {
            throw new IllegalArgumentException("failingSeed " + failingSeed + " is below lowerBound " + lowerBound);
        }
        if (!failsFor.test(failingSeed)) {
            throw new IllegalArgumentException("failingSeed " + failingSeed + " does not fail the property");
        }
        int high = failingSeed;   // a value known to fail
        int low = lowerBound;     // the floor the search may return
        while (high - low > 1) {
            int mid = low + (high - low) / 2;
            if (failsFor.test(mid)) {
                high = mid;       // still fails smaller — keep shrinking
            } else {
                low = mid;        // mid passes — the boundary is above it
            }
        }
        return failsFor.test(low) ? low : high;
    }
}
