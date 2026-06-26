/**
 * Chapter 100 companion — governing AI in the development workflow (Only Policy Can Ship It, the Part XII
 * closer).
 *
 * <p>The chapter's thesis, attributed to Sonatype, is that "AI can write code, but only policy can ship
 * it": AI raises productivity and risk together, so a team needs a deliberate policy for HOW AI is used —
 * not a ban (which drives shadow AI), not a free-for-all (which ships unverified code at scale). This
 * module makes the load-bearing, runnable half of that policy real and unit-tested, and pairs it with the
 * policy documents the chapter's companion spec calls for:
 *
 * <ul>
 *   <li>{@link org.acme.aigovernance.AiUsageGate} — the gate that decides whether policy permits an
 *       AI-assisted change to merge: a sanctioned tool, the required AI-specific checks run, disclosure,
 *       an accountable human, and the hard line — no auto-merge on an AI approval. The "only policy can
 *       ship it" clause, in code.</li>
 *   <li>{@link org.acme.aigovernance.AiGovernancePolicy} — the externalized {@code dev} / {@code prod}
 *       policy (sanctioned tools, required checks, disclosure / accountability / no-auto-merge flags),
 *       selected by the {@code aigov.profile} system property, never compiled in.</li>
 *   <li>{@link org.acme.aigovernance.GateDecision} — permit or block, the binary AI-precondition verdict,
 *       each carrying the actionable reason a pull request shows the author.</li>
 *   <li>{@link org.acme.aigovernance.AiReviewOutcome} — the three outcomes of an AI code review comment
 *       against ground truth: a real catch, a false positive, and a missed bug. The third is the intent
 *       ceiling — the defect the reviewer cannot infer — which is why AI review augments, never replaces,
 *       the human gate.</li>
 *   <li>{@link org.acme.aigovernance.AiAdoptionCounterMetric} — the observability surface that refuses to
 *       report a productivity figure without its change-failure-rate counter-metric (Chapter 38).</li>
 * </ul>
 *
 * <p>The one-page AI-in-the-workflow policy and the counter-metric dashboard note the chapter describes
 * live as documentation under {@code docs/}; the illustrative pipeline step that gates AI-assisted changes
 * is configuration under {@code ci/}. Those files are not run by this module's build; the runnable, tested
 * part of the chapter is the gate and the metric this package implements. {@code mvn -Pquality verify}
 * holds this (AI-written) code to the same deterministic gates the policy it describes would mandate — the
 * book practicing what it preaches.
 *
 * <p>Honest edges, carried in the code's comments: the gate enforces that CONTROLS are in place, never
 * that the code is correct — governance reduces risk, it does not eliminate it, and the human review for
 * intent still has to happen; AI review misses defects it cannot infer (the intent ceiling), so it is an
 * augmentation, not a gate; AI reviewing AI compounds correlated blind spots, so independence (a human,
 * and/or a different-model tool) is the safeguard — the same different-model rule this book applies to its
 * own originality and red-team checks; productivity is only counter-metric'd with risk, never celebrated
 * alone; every statistic the chapter cites is a dated, attributed, often vendor-sourced snapshot, so this
 * code bakes in none of them and reasons over a team's own measured numbers; and this is governance, not
 * legal advice — AI-IP and regulatory questions (the EU AI Act, the licence of generated code) need
 * counsel.
 */
package org.acme.aigovernance;
