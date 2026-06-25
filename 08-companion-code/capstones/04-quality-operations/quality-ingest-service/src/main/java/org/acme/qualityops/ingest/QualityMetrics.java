package org.acme.qualityops.ingest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The measured quality of one CI run (Part: value modelling). Immutable; the compact constructor
 * rejects an out-of-range coverage and negative counts at construction, so no downstream
 * aggregation has to re-check them. Coverage is a whole percent in {@code [0, 100]} — the smallest
 * representation that still teaches the idea; a production system would carry more precision.
 *
 * @param coveragePercent line/branch coverage as a whole percent, {@code 0..100}
 * @param violations      the count of static-analysis style violations (e.g. Checkstyle)
 * @param bugs            the count of probable-bug findings (e.g. SpotBugs)
 */
public record QualityMetrics(int coveragePercent, int violations, int bugs) {

    private static final int MAX_PERCENT = 100;

    public QualityMetrics {
        if (coveragePercent < 0 || coveragePercent > MAX_PERCENT) {
            throw new IllegalArgumentException("coveragePercent must be 0..100: " + coveragePercent);
        }
        if (violations < 0) {
            throw new IllegalArgumentException("violations must not be negative: " + violations);
        }
        if (bugs < 0) {
            throw new IllegalArgumentException("bugs must not be negative: " + bugs);
        }
    }

    /** The JSON projection embedded in an event body and read back by quality-metrics-service. */
    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("coveragePercent", coveragePercent);
        body.put("violations", violations);
        body.put("bugs", bugs);
        return body;
    }
}
