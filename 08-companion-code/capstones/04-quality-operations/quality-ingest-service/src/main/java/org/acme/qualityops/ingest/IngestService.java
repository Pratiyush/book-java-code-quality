package org.acme.qualityops.ingest;

import java.util.List;
import java.util.function.LongSupplier;
import org.acme.qualityops.ingest.event.QualityEventIngested;
import org.acme.platform.event.EventBus;

/**
 * Ingest application logic (Part: layering). It records a quality event idempotently and announces a
 * genuinely new one — the idempotency and the publish-only-once rules live here, in one place, rather
 * than being re-implemented at every call site. Every collaborator is injected, so this logic is
 * unit-tested without a network.
 *
 * <p>The clock is injected as a {@link LongSupplier} rather than read from
 * {@code System.currentTimeMillis()} inline, so event timestamps are deterministic under test.
 */
public final class IngestService {

    private final EventRepository repository;
    private final EventBus events;
    private final LongSupplier clock;

    public IngestService(EventRepository repository, EventBus events, LongSupplier clock) {
        this.repository = repository;
        this.events = events;
        this.clock = clock;
    }

    /**
     * Records the event. A second call with an id already seen returns {@code created = false} and
     * does not publish, so a re-delivered webhook neither duplicates the event nor re-fires reactions.
     */
    public IngestResult ingest(QualityEvent event) {
        boolean created = repository.save(event);
        if (created) {
            events.publish(new QualityEventIngested(event.id(), event.project(), clock.getAsLong()));
        }
        return new IngestResult(event, created);
    }

    public List<QualityEvent> eventsFor(String project) {
        return repository.findByProject(project);
    }

    /**
     * The outcome of an ingest call.
     *
     * @param event   the stored event (the existing one on a re-delivery)
     * @param created {@code true} when this call stored a new event, {@code false} when it was a
     *                no-op because the id was already known
     */
    public record IngestResult(QualityEvent event, boolean created) {
    }
}
