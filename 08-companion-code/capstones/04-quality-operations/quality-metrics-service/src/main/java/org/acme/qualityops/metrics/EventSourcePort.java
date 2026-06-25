package org.acme.qualityops.metrics;

import java.util.List;

/**
 * The outbound PORT for reading a project's events (Part: architecture). quality-metrics-service
 * depends on this interface, not on quality-ingest-service directly — so the aggregation logic is
 * unit-tested with a fake and wired in production with the HTTP adapter {@link IngestEventsClient}.
 */
public interface EventSourcePort {

    /** Every ingested event for the project, in arrival order (possibly empty). */
    List<IngestedEvent> eventsFor(String project);
}
