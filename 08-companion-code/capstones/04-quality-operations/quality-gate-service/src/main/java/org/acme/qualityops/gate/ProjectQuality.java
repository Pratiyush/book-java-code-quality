package org.acme.qualityops.gate;

import java.util.Map;
import java.util.Objects;

/**
 * The aggregated quality of a project as quality-gate-service consumes it (Part: anti-corruption
 * boundary). It is this service's own, smaller view of quality-metrics-service's aggregate — only the
 * three numbers a policy thresholds on — so the gate does not depend on the producer's full shape.
 *
 * @param coverage   the project's latest coverage percent
 * @param violations the project's total violations
 * @param bugs       the project's total bugs
 */
public record ProjectQuality(int coverage, int violations, int bugs) {

    private static int field(Map<String, Object> body, String name) {
        Object value = body.get(name);
        if (value instanceof Number n) {
            return Math.toIntExact(n.longValue());
        }
        throw new IllegalArgumentException("missing or non-numeric field: " + name);
    }

    /** Builds the view from a metrics-service aggregate body. */
    public static ProjectQuality fromBody(Map<String, Object> body) {
        Objects.requireNonNull(body, "body");
        return new ProjectQuality(
            field(body, "latestCoverage"),
            field(body, "totalViolations"),
            field(body, "totalBugs"));
    }
}
