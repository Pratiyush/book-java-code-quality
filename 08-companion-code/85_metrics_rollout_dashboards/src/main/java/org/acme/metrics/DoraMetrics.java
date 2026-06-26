package org.acme.metrics;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * DORA's four keys, computed from a window of {@link DeploymentRecord deployment records} using the
 * definitional formulas only. The four keys (DORA / the <em>Accelerate State of DevOps</em> research)
 * split into two dimensions:
 *
 * <ul>
 *   <li><b>Throughput</b> — deployment frequency and lead time for changes: how fast value reaches
 *       users.</li>
 *   <li><b>Stability</b> — change-failure rate and failed-deployment recovery time: how safely.</li>
 * </ul>
 *
 * <p>This type deliberately reports the <em>values</em> and not a performance band ("elite", "high",
 * and so on). The bands are edition-specific and the year matters, so they are verified against the
 * pinned State-of-DevOps edition rather than hard-coded here; baking a threshold in would turn a
 * version-specific figure into a timeless-looking fact, which the book does not do. The throughput and
 * stability dimensions are reported together because the research finds they <em>correlate</em> rather
 * than trade off — see {@link #correlatedReport()}, which refuses to hand back one without the other.
 */
public final class DoraMetrics {

    private final List<DeploymentRecord> deployments;
    private final Duration window;

    /**
     * @param deployments the deployment events in the reporting window, defensively copied
     * @param window      the length of the reporting window (for deployment frequency), positive
     */
    public DoraMetrics(List<DeploymentRecord> deployments, Duration window) {
        if (deployments == null) {
            throw new IllegalArgumentException("deployments must not be null");
        }
        if (window == null || window.isZero() || window.isNegative()) {
            throw new IllegalArgumentException("window must be a positive duration");
        }
        this.deployments = List.copyOf(deployments);
        this.window = window;
    }

    /** Deployment frequency: deployments per day across the window (throughput). */
    public double deploymentsPerDay() {
        double days = window.toSeconds() / (double) Duration.ofDays(1).toSeconds();
        return days == 0 ? 0.0 : deployments.size() / days;
    }

    /** Lead time for changes: the mean commit-to-production duration across the window (throughput). */
    public Duration meanLeadTime() {
        if (deployments.isEmpty()) {
            return Duration.ZERO;
        }
        long totalSeconds = deployments.stream().mapToLong(d -> d.leadTime().toSeconds()).sum();
        return Duration.ofSeconds(totalSeconds / deployments.size());
    }

    // tag::change-failure-rate[]
    /** Change-failure rate: the fraction of deployments that caused a failure (stability), in [0, 1]. */
    public double changeFailureRate() {
        if (deployments.isEmpty()) {
            return 0.0;
        }
        long failures = deployments.stream().filter(DeploymentRecord::causedFailure).count();
        return (double) failures / deployments.size();
    }
    // end::change-failure-rate[]

    /** Failed-deployment recovery time: the mean recovery duration over failed deployments (stability). */
    public Duration meanRecoveryTime() {
        List<DeploymentRecord> failures =
                deployments.stream().filter(DeploymentRecord::causedFailure).toList();
        if (failures.isEmpty()) {
            return Duration.ZERO;
        }
        long totalSeconds = failures.stream().mapToLong(d -> d.recoveryTime().toSeconds()).sum();
        return Duration.ofSeconds(totalSeconds / failures.size());
    }

    /**
     * The four keys reported as one unit, throughput beside stability — never one alone. Reporting
     * deployment frequency without the change-failure rate invites the classic gaming (split deploys to
     * juice frequency while stability quietly rots); pairing them is the counter-metric discipline at
     * the delivery level, and reflects the finding that the two dimensions correlate.
     *
     * @return the four key values, throughput and stability together
     */
    public List<MetricValue> correlatedReport() {
        List<MetricValue> report = new ArrayList<>();
        report.add(new MetricValue("deployment_frequency_per_day", deploymentsPerDay()));
        report.add(new MetricValue("lead_time_seconds", meanLeadTime().toSeconds()));
        report.add(new MetricValue("change_failure_rate", changeFailureRate()));
        report.add(new MetricValue("recovery_time_seconds", meanRecoveryTime().toSeconds()));
        return report;
    }

    /** A named numeric metric value — the unit a dashboard tile or a scrape endpoint would carry. */
    public record MetricValue(String name, double value) { }
}
