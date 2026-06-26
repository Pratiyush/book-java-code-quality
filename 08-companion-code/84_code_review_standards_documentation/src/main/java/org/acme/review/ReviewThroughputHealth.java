package org.acme.review;

/**
 * The observability surface for the human gate: a small, read-only health signal over review throughput.
 * The chapter's recurring warning is that a process metric measures activity, never quality — a review
 * count says reviews happened, not that they caught anything. This signal is deliberately modest: it
 * reports whether the median pull-request size stays inside the effective-review zone the evidence
 * describes, so a team can see review drifting toward "too large to actually review" before defect
 * detection quietly collapses. It changes no verdict and gates nothing; it makes a trend visible.
 */
public final class ReviewThroughputHealth {

    /**
     * The upper bound of the effective-review zone, in changed lines, per Cohen / SmartBear: defect
     * detection drops sharply once review size passes roughly this many lines.
     */
    public static final int EFFECTIVE_REVIEW_CEILING_LINES = 300;

    private ReviewThroughputHealth() {
    }

    /** A coarse health verdict: review size is healthy, drifting, or past the effective zone. */
    public enum Status {
        /** Median PR size is inside the effective-review zone. */
        HEALTHY,
        /** Median PR size is in the upper half of the zone — worth watching. */
        WATCH,
        /** Median PR size is past the zone, where review degrades to a skim. */
        DEGRADED
    }

    /**
     * Reports a review-size health verdict from the median changed-line count over a recent window.
     *
     * @param medianChangedLines the median changed lines per reviewed PR; must be {@code >= 0}
     * @return the verdict: {@link Status#HEALTHY}, {@link Status#WATCH}, or {@link Status#DEGRADED}
     * @throws IllegalArgumentException if {@code medianChangedLines} is negative
     */
    public static Status report(int medianChangedLines) {
        if (medianChangedLines < 0) {
            throw new IllegalArgumentException(
                "medianChangedLines must be >= 0, was " + medianChangedLines);
        }
        if (medianChangedLines > EFFECTIVE_REVIEW_CEILING_LINES) {
            return Status.DEGRADED;
        }
        if (medianChangedLines > EFFECTIVE_REVIEW_CEILING_LINES / 2) {
            return Status.WATCH;
        }
        return Status.HEALTHY;
    }
}
