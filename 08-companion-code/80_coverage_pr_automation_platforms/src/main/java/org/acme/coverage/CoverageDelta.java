package org.acme.coverage;

/**
 * One pull request's coverage picture: how overall coverage moved, and how well the new and changed
 * lines themselves are covered.
 *
 * <p>This is the input the platform computes from the uploaded JaCoCo report and the diff — the same
 * two numbers a Codecov/Coveralls/Sonar PR comment shows. The gate reads it; it asserts nothing on
 * its own beyond keeping the ratios in {@code [0, 1]}.
 *
 * @param overallBefore overall coverage on the base branch, in {@code [0, 1]}
 * @param overallAfter  overall coverage if this pull request merges, in {@code [0, 1]}
 * @param newCodeLines  the count of new or changed lines this pull request adds, never negative
 * @param newCodeCovered the coverage ratio of those new/changed lines, in {@code [0, 1]} (the
 *     diff-coverage / patch number — the new-code focus the chapter argues for)
 */
public record CoverageDelta(
        double overallBefore,
        double overallAfter,
        int newCodeLines,
        double newCodeCovered) {

    /**
     * Canonical constructor with fail-fast range guards (the explicit failure path at the value
     * level): a ratio outside {@code [0, 1]} or a negative line count is a programming error in
     * whatever produced the delta, not a gate outcome, so it is rejected here rather than silently
     * skewing a verdict.
     *
     * @throws IllegalArgumentException if any ratio is outside {@code [0, 1]} or {@code newCodeLines}
     *     is negative
     */
    public CoverageDelta {
        requireRatio(overallBefore, "overallBefore");
        requireRatio(overallAfter, "overallAfter");
        requireRatio(newCodeCovered, "newCodeCovered");
        if (newCodeLines < 0) {
            throw new IllegalArgumentException("newCodeLines must not be negative: " + newCodeLines);
        }
    }

    /**
     * Whether this pull request introduces any new or changed lines at all. A pull request that adds
     * no new code (a rename, a config-only change) cannot lower new-code coverage, so the new-code bar
     * does not apply to it.
     *
     * @return {@code true} if the pull request adds one or more new/changed lines
     */
    public boolean touchesNewCode() {
        return newCodeLines > 0;
    }

    /**
     * The signed change in overall coverage if this pull request merges. Negative means overall
     * coverage would drop — the condition the ratchet fails on.
     *
     * @return {@code overallAfter - overallBefore}
     */
    public double overallChange() {
        return overallAfter - overallBefore;
    }

    private static void requireRatio(double value, String name) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException(name + " must be in [0, 1]: " + value);
        }
    }
}
