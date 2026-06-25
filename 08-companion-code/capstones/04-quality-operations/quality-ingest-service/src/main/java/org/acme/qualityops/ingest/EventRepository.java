package org.acme.qualityops.ingest;

import java.util.List;
import java.util.Optional;

/**
 * The persistence PORT for ingested quality events (Part: architecture / hexagonal boundaries). The
 * service depends on this interface, not on any storage technology, so the in-memory adapter that
 * ships can be swapped for a database adapter without touching service logic. {@code save} is the
 * idempotency seam: an id already present is not overwritten, so a re-delivered event is a no-op.
 */
public interface EventRepository {

    /**
     * Stores the event unless one with the same id already exists.
     *
     * @return {@code true} if this call stored a new event, {@code false} if the id was already known
     */
    boolean save(QualityEvent event);

    Optional<QualityEvent> findById(String id);

    /** Every event for a project, oldest first. */
    List<QualityEvent> findByProject(String project);
}
