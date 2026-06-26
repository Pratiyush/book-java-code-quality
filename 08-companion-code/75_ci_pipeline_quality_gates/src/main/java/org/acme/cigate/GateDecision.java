package org.acme.cigate;

/**
 * The outcome a quality gate returns for a change. There are three, not two, and the middle one is the
 * chapter's policy point: block on the objective, high-severity, new findings, warn on the subjective
 * or pre-existing ones, and pass otherwise. A gate that blocks on everything gets routed around (the
 * number-one failure); a gate that blocks on nothing is meaningless; the three-way decision is what
 * keeps a red gate meaning "something real, in code you just touched" so the team keeps it on.
 *
 * <p>Each decision carries the human-readable reason that produced it, so a block or a warning is
 * actionable on the pull request rather than a bare red mark a developer cannot act on.
 */
// tag::block-vs-warn[]
public sealed interface GateDecision permits GateDecision.Pass, GateDecision.Warn, GateDecision.Block {
    record Pass(String reason) implements GateDecision { }  // nothing to act on — merge may proceed
    record Warn(String reason) implements GateDecision { }  // surfaced, not blocking — keeps credibility
    record Block(String reason) implements GateDecision { } // new high-severity finding — fail the build
}
// end::block-vs-warn[]
