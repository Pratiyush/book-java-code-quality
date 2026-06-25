package org.acme.qualityops.metrics;

import java.util.Objects;

/**
 * A quality event as quality-metrics-service consumes it (Part: anti-corruption boundary). This is a
 * deliberately separate, smaller view of the event than quality-ingest-service's own record: this
 * service reads only the fields it aggregates ({@code occurredAt} for ordering, and the three
 * metrics), so it does not depend on the producer's full shape and is unaffected by a field it does
 * not use changing upstream.
 *
 * @param occurredAtEpochMillis  when the run produced the event, used to find the latest
 * @param coveragePercent        the run's coverage, {@code 0..100}
 * @param violations             the run's violation count
 * @param bugs                   the run's bug count
 */
public record IngestedEvent(long occurredAtEpochMillis, int coveragePercent, int violations, int bugs) {

    public IngestedEvent {
        if (coveragePercent < 0) {
            throw new IllegalArgumentException("coveragePercent must not be negative: " + coveragePercent);
        }
    }

    private static int field(java.util.Map<String, Object> body, String name) {
        Object value = body.get(name);
        if (value instanceof Number n) {
            return Math.toIntExact(n.longValue());
        }
        throw new IllegalArgumentException("missing or non-numeric field: " + name);
    }

    /** Builds the view from an ingest-service event body, reading its nested {@code metrics} object. */
    @SuppressWarnings("unchecked") // the ingest contract nests metrics as a JSON object
    public static IngestedEvent fromBody(java.util.Map<String, Object> body) {
        Objects.requireNonNull(body, "body");
        Object rawMetrics = body.get("metrics");
        if (!(rawMetrics instanceof java.util.Map<?, ?>)) {
            throw new IllegalArgumentException("event body is missing its 'metrics' object");
        }
        java.util.Map<String, Object> metrics = (java.util.Map<String, Object>) rawMetrics;
        long occurredAt = body.get("occurredAt") instanceof Number n ? n.longValue() : 0L;
        return new IngestedEvent(
            occurredAt,
            field(metrics, "coveragePercent"),
            field(metrics, "violations"),
            field(metrics, "bugs"));
    }
}
