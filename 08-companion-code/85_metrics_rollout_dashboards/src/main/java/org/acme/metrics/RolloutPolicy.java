package org.acme.metrics;

/**
 * The baseline-and-ratchet rollout policy: the practical answer to "we enabled the analyzer and got
 * forty thousand findings — now what?". It is what lets a quality gate land on a large legacy codebase
 * without a demoralizing, work-blocking wall, by combining two mechanisms.
 *
 * <ul>
 *   <li><b>Baseline</b> — accept the past, gate the future. The existing finding count is snapshotted as
 *       an accepted baseline; only findings <em>above</em> the baseline (new violations) block the
 *       build. The legacy debt is recorded, not ignored, but it does not block today's work
 *       (clean-as-you-code as an adoption strategy).</li>
 *   <li><b>Ratchet</b> — only improve. A threshold may move in the better direction only: coverage may
 *       not drop, the new-issue count may not rise, complexity ceilings may only tighten. The codebase
 *       converges upward without a big-bang cleanup.</li>
 * </ul>
 *
 * <p>The honest edge the chapter insists on: a baseline without a paydown plan is formalized ignoring,
 * not management. This type {@linkplain #remainingBaselineDebt() exposes the baselined debt} precisely so
 * it stays visible and a ratchet can be set to draw it down, rather than letting it become permanent
 * amnesty.
 *
 * @param baselineFindings the count of pre-existing findings accepted as the baseline, never negative
 */
public record RolloutPolicy(long baselineFindings) {

    /** Compact constructor: a negative baseline is not a count of anything. */
    public RolloutPolicy {
        if (baselineFindings < 0) {
            throw new IllegalArgumentException("baselineFindings must not be negative: " + baselineFindings);
        }
    }

    // tag::baseline-gate[]
    /** Gates the future, not the past: only findings above the accepted baseline block the build. */
    public RolloutDecision gateNewFindings(long currentFindings) {
        long newFindings = currentFindings - baselineFindings;
        return newFindings <= 0
                ? new RolloutDecision.Accepted()
                : new RolloutDecision.Blocked(newFindings + " new finding(s) above the baseline");
    }
    // end::baseline-gate[]

    /**
     * The ratchet: a metric may only move in its improving direction. A value that holds or improves is
     * accepted (and becomes the new floor a caller should record); a regression is blocked with a reason.
     *
     * @param metricName the human name of the metric, for the block reason
     * @param previous   the previously recorded value (the ratchet's current floor)
     * @param current    the new measured value
     * @param direction  which way "better" is for this metric
     * @return an {@link RolloutDecision.Accepted} if it held or improved, else a {@link RolloutDecision.Blocked}
     */
    public RolloutDecision ratchet(
            String metricName, double previous, double current, RatchetDirection direction) {
        boolean improvedOrHeld = direction == RatchetDirection.HIGHER_IS_BETTER
                ? current >= previous
                : current <= previous;
        if (improvedOrHeld) {
            return new RolloutDecision.Accepted();
        }
        return new RolloutDecision.Blocked(
                metricName + " regressed from " + previous + " to " + current
                        + " (ratchet allows " + direction + " only)");
    }

    /** The baselined debt still outstanding — kept visible so it is paid down, not quietly forgotten. */
    public long remainingBaselineDebt() {
        return baselineFindings;
    }

    /** Which direction counts as improvement for a ratcheted metric. */
    public enum RatchetDirection {
        /** Coverage and similar: a higher value is better, so the value may only rise or hold. */
        HIGHER_IS_BETTER,
        /** New-issue count, complexity, and similar: a lower value is better, so it may only fall or hold. */
        LOWER_IS_BETTER
    }
}
