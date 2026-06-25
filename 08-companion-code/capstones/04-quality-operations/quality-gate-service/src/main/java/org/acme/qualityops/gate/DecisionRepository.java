package org.acme.qualityops.gate;

import java.util.Optional;

/**
 * The persistence PORT for recorded gate decisions (Part: architecture / hexagonal boundaries). The
 * service depends on this interface, not on any storage technology, so the in-memory adapter that
 * ships can be swapped for a database adapter without touching service logic. The key is the pair
 * {@code (project, commit)}: a commit's gate outcome is recorded once and is stable thereafter.
 */
public interface DecisionRepository {

    /**
     * Records the decision unless one already exists for its {@code (project, commit)}.
     *
     * @return the decision now on record — the freshly stored one, or the pre-existing one if this
     *     {@code (project, commit)} had already been decided
     */
    GateDecision saveIfAbsent(GateDecision decision);

    Optional<GateDecision> find(String project, String commit);
}
