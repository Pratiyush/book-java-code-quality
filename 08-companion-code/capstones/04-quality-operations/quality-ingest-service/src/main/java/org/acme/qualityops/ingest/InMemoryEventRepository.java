package org.acme.qualityops.ingest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The in-memory adapter for {@link EventRepository} — the runnable default (Part: architecture). It
 * keeps every event by id and an insertion-ordered list per project. Backed by concurrency-safe
 * collections because the HTTP layer serves requests on many virtual threads at once; the per-id
 * {@code putIfAbsent} is what makes ingestion idempotent under a concurrent re-delivery. A production
 * deployment supplies a database-backed adapter with a unique constraint on the id instead.
 */
public final class InMemoryEventRepository implements EventRepository {

    private final ConcurrentHashMap<String, QualityEvent> byId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<QualityEvent>> byProject =
        new ConcurrentHashMap<>();

    @Override
    public boolean save(QualityEvent event) {
        QualityEvent existing = byId.putIfAbsent(event.id(), event);
        if (existing != null) {
            return false;
        }
        byProject.computeIfAbsent(event.project(), key -> new CopyOnWriteArrayList<>()).add(event);
        return true;
    }

    @Override
    public Optional<QualityEvent> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public List<QualityEvent> findByProject(String project) {
        CopyOnWriteArrayList<QualityEvent> events = byProject.get(project);
        return events == null ? List.of() : List.copyOf(events);
    }
}
