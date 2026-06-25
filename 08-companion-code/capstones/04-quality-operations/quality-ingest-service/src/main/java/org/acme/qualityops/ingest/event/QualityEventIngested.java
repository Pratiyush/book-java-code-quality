package org.acme.qualityops.ingest.event;

import org.acme.platform.event.DomainEvent;

/**
 * Recorded when a new quality event is first ingested (Part: events). Past tense, immutable: a
 * subscriber reacts to the fact (increment a metric, invalidate a cached aggregate) without the
 * ingest code knowing who listens. It is published only on a genuinely new event, never on an
 * idempotent re-delivery, so a downstream count of ingestions stays accurate.
 *
 * @param eventId                the id of the event that was ingested
 * @param project                the project the event is for
 * @param occurredAtEpochMillis  when the event was ingested
 */
public record QualityEventIngested(String eventId, String project, long occurredAtEpochMillis)
    implements DomainEvent {
}
