package org.acme.perfgate;

/**
 * The outcome of a regression gate. There are three, not two, and the third is the chapter's central
 * point: a perf gate's measurement may be noise, so the gate must fail <em>safe</em> when it cannot be
 * sure. A correctness gate is deterministic (a failing test is a real failure, so block); a perf gate
 * is statistical (a "failure" may be the runner having a bad minute), so a small or
 * within-the-noise-band regression is <em>flagged</em> for a human, not hard-blocked. A gate that
 * hard-blocks on jitter gets disabled — and a disabled gate is the worst outcome, false confidence
 * while the thousand cuts continue (Chapter 20).
 *
 * <p>Each verdict carries the human-readable reason the gate produced it, so a flag or a failure is
 * actionable rather than a bare red mark.
 */
// tag::gate-pass-fail[]
public sealed interface GateVerdict permits GateVerdict.Pass, GateVerdict.Flag, GateVerdict.Fail {
    record Pass(String reason) implements GateVerdict { } // within tolerance — no action
    record Flag(String reason) implements GateVerdict { } // small/uncertain — investigate, don't block
    record Fail(String reason) implements GateVerdict { } // large, confident regression — block
}
// end::gate-pass-fail[]
