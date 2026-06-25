package org.acme.qualityops.gate;

/**
 * The outbound PORT for reading a project's aggregated quality (Part: architecture).
 * quality-gate-service depends on this interface, not on quality-metrics-service directly — so the
 * decision logic is unit-tested with a fake and wired in production with the HTTP adapter
 * {@link MetricsServiceClient}.
 */
public interface MetricsPort {

    /** The project's aggregate; throws an {@code ApiException} when the project has no metrics. */
    ProjectQuality qualityOf(String project);
}
