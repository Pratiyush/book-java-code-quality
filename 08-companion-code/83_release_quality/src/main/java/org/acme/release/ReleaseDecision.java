package org.acme.release;

import java.util.List;

/**
 * The verdict a release-readiness gate returns: either the artifact is ready to ship, or it is blocked
 * with the exact list of preconditions it failed. There is no third state — a release is either green on
 * every required check or it does not go out — which is the discipline the chapter argues for: the
 * release gate is a hard stop, not a checklist someone eyeballs and waves through.
 *
 * <p>A blocked decision names the failed checks rather than returning a bare {@code false}, so the team
 * sees what to fix (cut a release version, add the changelog entry, re-run the red pipeline) instead of a
 * mark with no cause. This is the same actionable-failure shape the static-analysis gate uses for a
 * blocked merge (Chapter 33): a refusal a developer can act on, not a closed door.
 */
// tag::release-decision[]
public sealed interface ReleaseDecision permits ReleaseDecision.Ready, ReleaseDecision.Blocked {
    record Ready() implements ReleaseDecision { }                              // ship it
    record Blocked(List<ReleaseCheck> failures) implements ReleaseDecision {   // these checks failed
        public Blocked {
            failures = List.copyOf(failures);                                  // immutable, never null
        }
    }
}
// end::release-decision[]
