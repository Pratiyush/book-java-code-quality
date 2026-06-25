package org.acme.qualityops.gate;

/**
 * The thresholds a commit is judged against (Part: input validation). Immutable; the compact
 * constructor rejects nonsensical bounds at construction, so the evaluation logic can assume a valid
 * policy. A commit passes when its coverage is at least {@code minCoverage} and its violation and bug
 * totals are each at most their cap.
 *
 * @param minCoverage   the minimum acceptable coverage percent, {@code 0..100}
 * @param maxViolations the maximum acceptable violation total ({@code >= 0})
 * @param maxBugs       the maximum acceptable bug total ({@code >= 0})
 */
public record GatePolicy(int minCoverage, int maxViolations, int maxBugs) {

    private static final int MAX_PERCENT = 100;

    public GatePolicy {
        if (minCoverage < 0 || minCoverage > MAX_PERCENT) {
            throw new IllegalArgumentException("minCoverage must be 0..100: " + minCoverage);
        }
        if (maxViolations < 0) {
            throw new IllegalArgumentException("maxViolations must not be negative: " + maxViolations);
        }
        if (maxBugs < 0) {
            throw new IllegalArgumentException("maxBugs must not be negative: " + maxBugs);
        }
    }
}
