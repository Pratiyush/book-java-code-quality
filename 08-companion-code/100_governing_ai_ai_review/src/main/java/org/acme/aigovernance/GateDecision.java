package org.acme.aigovernance;

/**
 * The verdict the AI-usage gate returns for an AI-assisted change: either policy permits the merge, or
 * it does not. There are two, not three: where the quality gate has a middle "warn" outcome (Chapter 33),
 * an AI-governance precondition is binary. A change either satisfies the policy ("only policy can ship
 * it") or it is blocked; there is no "warn and merge anyway" for a missing security scan or an undisclosed
 * AI contribution, because those are the controls the policy exists to require.
 *
 * <p>Each decision carries the human-readable reason that produced it, so a block is actionable on the
 * pull request ("blocked: AI-assisted change used an unsanctioned tool") rather than a bare red mark the
 * author cannot act on. The reason is what lets governance be a feedback loop the chapter argues for and
 * not a wall: it tells the author exactly which control to satisfy.
 */
// tag::gate-decision[]
public sealed interface GateDecision permits GateDecision.Permit, GateDecision.Block {

    /** Policy is satisfied — the human gate and the AI-specific controls are all in place. */
    record Permit(String reason) implements GateDecision { }

    /** A policy precondition is unmet — the change cannot merge until it is satisfied. */
    record Block(String reason) implements GateDecision { }
}
// end::gate-decision[]
