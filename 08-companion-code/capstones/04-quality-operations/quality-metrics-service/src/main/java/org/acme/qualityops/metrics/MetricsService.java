package org.acme.qualityops.metrics;

import java.util.List;
import org.acme.platform.error.ApiException;

/**
 * Metrics application logic (Part: composition). It reads a project's events through the injected
 * {@link EventSourcePort} and folds them into a {@link ProjectMetrics} aggregate — latest coverage by
 * timestamp, summed violations and bugs, and the composite score. The aggregation is pure and lives
 * in one place, so it is unit-tested with a fake source and never reaches across a network in a test.
 *
 * <p>A project with no events is a 404 rather than a zeroed aggregate: "no data yet" and "measured at
 * zero" are different facts, and a gate must not silently pass a project it has never seen.
 */
public final class MetricsService {

    private final EventSourcePort source;

    public MetricsService(EventSourcePort source) {
        this.source = source;
    }

    /** The aggregate for a project, or a 404 problem when the project has no events. */
    public ProjectMetrics metricsFor(String project) {
        List<IngestedEvent> events = source.eventsFor(project);
        if (events.isEmpty()) {
            throw ApiException.notFound("project-unknown", "no quality events for project " + project);
        }
        int latestCoverage = 0;
        long latestAt = Long.MIN_VALUE;
        int totalViolations = 0;
        int totalBugs = 0;
        for (IngestedEvent event : events) {
            totalViolations += event.violations();
            totalBugs += event.bugs();
            if (event.occurredAtEpochMillis() >= latestAt) {
                latestAt = event.occurredAtEpochMillis();
                latestCoverage = event.coveragePercent();
            }
        }
        int score = ProjectMetrics.compositeScore(latestCoverage, totalViolations, totalBugs);
        return new ProjectMetrics(project, events.size(), latestCoverage, totalViolations, totalBugs, score);
    }
}
