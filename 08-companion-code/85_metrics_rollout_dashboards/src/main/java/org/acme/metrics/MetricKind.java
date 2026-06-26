package org.acme.metrics;

/**
 * How a metric relates to value, which decides whether it belongs in a quality program at all.
 *
 * <ul>
 *   <li>{@link #OUTCOME} — measures value safely delivered (DORA's keys, a SPACE dimension). The kind
 *       a program is built on.</li>
 *   <li>{@link #QUALITY_TREND} — a quality proxy worth watching <em>as a trend</em> with a
 *       counter-metric: defect-escape rate, MTTR, flaky-test rate, debt-ratio trend, new-code coverage
 *       trend. Useful as a direction, never as a target.</li>
 *   <li>{@link #VANITY} — counts activity, not value, and is trivially gamed: lines of code, commit
 *       count, PR count, raw velocity or story points as a productivity measure. The kind a program
 *       refuses to gate or display.</li>
 * </ul>
 *
 * <p>The classification is what lets the rest of the module push back in code rather than in a comment:
 * a {@link DashboardSpec} refuses a {@code VANITY} tile, and the same surfaces refuse an
 * individual-scoped metric, because — by the chapter's firmest rule — metrics measure the system, not
 * people.
 */
public enum MetricKind {

    /** Measures value safely delivered — the kind a quality program is built on. */
    OUTCOME,

    /** A quality proxy worth watching as a trend with a counter-metric, never as a target. */
    QUALITY_TREND,

    /** Counts activity, not value, and is trivially gamed — the kind a program refuses. */
    VANITY;

    /** {@code true} for the kinds a quality program may legitimately gate or display on a dashboard. */
    public boolean isUsable() {
        return this != VANITY;
    }
}
