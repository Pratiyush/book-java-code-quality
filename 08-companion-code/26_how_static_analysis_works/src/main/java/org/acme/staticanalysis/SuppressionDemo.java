package org.acme.staticanalysis;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The chapter's second false-positive control — a per-site suppression that records its reason.
 *
 * <p>Where the filter file ({@code config/spotbugs/spotbugs-exclude.xml}) suppresses by location, the
 * {@code @SuppressFBWarnings} annotation suppresses at the exact member and carries the human judgment
 * next to the code: a {@code value} (which pattern) and a {@code justification} (why this instance is
 * safe). This type exists to show that in-code form. {@link #snapshot()} returns a reference to an
 * internal array, which SpotBugs reports as {@code EI_EXPOSE_REP} — a finding that is a genuine risk in
 * general but is safe here, because the array holds an immutable snapshot that is never mutated after
 * construction. The annotation names the pattern and the reason rather than disabling the detector,
 * which is the discipline the chapter argues for: suppress with a reason, never silence the rule.
 */
public final class SuppressionDemo {

    private final int[] dailyCounts;

    /**
     * Creates a snapshot over a defensively-copied counts array.
     *
     * @param counts the per-day counts to snapshot, copied on the way in
     */
    public SuppressionDemo(int[] counts) {
        this.dailyCounts = counts.clone();
    }

    /**
     * Returns the underlying counts array. The reference is shared deliberately for the example; the
     * reason is recorded in the suppression annotation rather than by silencing the detector.
     *
     * @return the snapshot counts (treated as read-only by every caller)
     */
    // tag::justified-suppression[]
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "dailyCounts is an immutable snapshot, never mutated after construction; "
            + "sharing the reference is safe here. Record the reason rather than disable the detector.")
    public int[] snapshot() {
        return dailyCounts;
    }
    // end::justified-suppression[]

    /**
     * A read-only accessor with no exposure — the form that needs no suppression at all.
     *
     * @param day the zero-based day index
     * @return the count for that day
     */
    public int countFor(int day) {
        return dailyCounts[day];
    }
}
