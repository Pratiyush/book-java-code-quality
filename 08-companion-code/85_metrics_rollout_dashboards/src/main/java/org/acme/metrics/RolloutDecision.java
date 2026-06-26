package org.acme.metrics;

/**
 * The typed result of a rollout gate check — an explicit decision rather than an exception thrown into
 * the void or a bare boolean. A caller branches on the variant; a {@link Blocked} decision carries a
 * reason a developer can act on, which is the whole point of a gate that builds trust (Chapter 16: a
 * gate whose failures are inscrutable gets bypassed).
 *
 * <p>The {@link Blocked} variant is the module's failure path made concrete: a baseline-and-ratchet
 * rollout exists precisely to <em>block</em> a regression on new code while accepting the legacy past,
 * and the block is a value the build can report, not a stack trace.
 */
public sealed interface RolloutDecision permits RolloutDecision.Accepted, RolloutDecision.Blocked {

    /** Whether the change may proceed through the gate. */
    boolean allowed();

    /** The change passes the gate: no new finding above the baseline, no ratchet moved backwards. */
    record Accepted() implements RolloutDecision {
        @Override
        public boolean allowed() {
            return true;
        }
    }

    /** The change is blocked, with a developer-actionable reason (a new finding, or a ratchet regression). */
    record Blocked(String reason) implements RolloutDecision {
        /** Compact constructor: a block with no reason is exactly the inscrutable gate to avoid. */
        public Blocked {
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("a blocked decision must carry a reason");
            }
        }

        @Override
        public boolean allowed() {
            return false;
        }
    }
}
