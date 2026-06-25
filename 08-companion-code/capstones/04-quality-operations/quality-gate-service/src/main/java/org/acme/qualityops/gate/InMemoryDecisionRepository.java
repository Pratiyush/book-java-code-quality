package org.acme.qualityops.gate;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The in-memory adapter for {@link DecisionRepository} — the runnable default (Part: architecture).
 * It keys decisions by the {@code (project, commit)} pair, modelled as a small record so equality is
 * value-based. The {@code putIfAbsent} is what makes recording idempotent under a concurrent
 * re-evaluation: the first decision for a commit wins and is returned to every later caller. A
 * production deployment supplies a database-backed adapter with a unique constraint on the pair.
 */
public final class InMemoryDecisionRepository implements DecisionRepository {

    private final ConcurrentHashMap<Key, GateDecision> decisions = new ConcurrentHashMap<>();

    @Override
    public GateDecision saveIfAbsent(GateDecision decision) {
        Key key = new Key(decision.project(), decision.commit());
        GateDecision existing = decisions.putIfAbsent(key, decision);
        return existing != null ? existing : decision;
    }

    @Override
    public Optional<GateDecision> find(String project, String commit) {
        return Optional.ofNullable(decisions.get(new Key(project, commit)));
    }

    /** The composite identity of a decision: a value-based key over {@code (project, commit)}. */
    private record Key(String project, String commit) {
    }
}
