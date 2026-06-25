package org.acme.qualityops.metrics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The per-project aggregate computed from a project's ingested events (Part: value modelling).
 * Immutable. The composite {@code qualityScore} is a single 0..100 number a gate can threshold on,
 * derived from the latest coverage and the total violations and bugs — see
 * {@link #compositeScore(int, int, int)} for the formula and the weighting rationale.
 *
 * @param project          the project these aggregates are for
 * @param eventCount       how many events fed the aggregate
 * @param latestCoverage   the coverage of the most recent event, {@code 0..100} (0 when no events)
 * @param totalViolations  the sum of violations across the events
 * @param totalBugs        the sum of bugs across the events
 * @param qualityScore     the composite quality score, {@code 0..100}
 */
public record ProjectMetrics(String project, int eventCount, int latestCoverage,
                             int totalViolations, int totalBugs, int qualityScore) {

    private static final int MAX_SCORE = 100;
    private static final int VIOLATION_WEIGHT = 2;
    private static final int BUG_WEIGHT = 5;

    public ProjectMetrics {
        Objects.requireNonNull(project, "project");
    }

    /**
     * The composite score: start from the latest coverage, subtract a fixed penalty per violation and
     * a heavier one per bug, and clamp to {@code [0, 100]}. The weights ({@value #VIOLATION_WEIGHT}
     * per violation, {@value #BUG_WEIGHT} per bug) are this team's cited choice, not a universal
     * truth — a different shop would tune them, which is exactly why they live in one named method.
     */
    public static int compositeScore(int latestCoverage, int totalViolations, int totalBugs) {
        int raw = latestCoverage - totalViolations * VIOLATION_WEIGHT - totalBugs * BUG_WEIGHT;
        return Math.max(0, Math.min(MAX_SCORE, raw));
    }

    /** The JSON projection returned by the API and consumed by quality-gate-service. */
    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("project", project);
        body.put("eventCount", eventCount);
        body.put("latestCoverage", latestCoverage);
        body.put("totalViolations", totalViolations);
        body.put("totalBugs", totalBugs);
        body.put("qualityScore", qualityScore);
        return body;
    }
}
