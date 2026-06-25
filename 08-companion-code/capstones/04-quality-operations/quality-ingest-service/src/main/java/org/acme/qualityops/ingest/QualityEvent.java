package org.acme.qualityops.ingest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * One quality signal emitted by a CI run for a single commit (Part: value modelling). Immutable; the
 * {@code id} is the idempotency key — re-POSTing the same id is a no-op rather than a duplicate, so a
 * webhook delivered twice does not double-count. Every field is validated in the compact constructor,
 * so an ingested event is always well-formed by the time it reaches the repository.
 *
 * @param id                     the client-supplied idempotency key for this signal
 * @param project                the project the signal is for
 * @param commit                 the commit SHA the run measured
 * @param tool                   the tool that produced the signal (e.g. {@code "jacoco"})
 * @param occurredAtEpochMillis  when the CI run produced the signal
 * @param metrics                the measured quality
 */
public record QualityEvent(String id, String project, String commit, String tool,
                           long occurredAtEpochMillis, QualityMetrics metrics) {

    public QualityEvent {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(commit, "commit");
        Objects.requireNonNull(tool, "tool");
        Objects.requireNonNull(metrics, "metrics");
    }

    /** The JSON projection returned by the API and consumed by quality-metrics-service. */
    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("project", project);
        body.put("commit", commit);
        body.put("tool", tool);
        body.put("occurredAt", occurredAtEpochMillis);
        body.put("metrics", metrics.toBody());
        return body;
    }
}
