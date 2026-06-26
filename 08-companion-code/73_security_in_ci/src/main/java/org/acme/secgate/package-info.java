/**
 * Chapter 32 companion — assembling the security gate in CI (Making the Security Gate Stick).
 *
 * <p>A team can finish the Part VIII toolbox — SAST on the code, SCA on the dependencies, secrets
 * scanning on every commit, a dynamic scan against staging — and still watch the security program
 * collapse the week it wires all of it into one pipeline set to block on any finding. The build goes
 * permanently red on hundreds of pre-existing and low-severity findings, nobody can merge, and a senior
 * engineer quietly adds {@code continue-on-error} to unblock the team. The gate is now decorative. The
 * tools were not the hard part; assembling them into a gate the team keeps on is. That assembly is three
 * decisions: which testing types to layer, in what pipeline order, and what blocks versus what routes to a
 * reviewer.
 *
 * <p>This module makes the load-bearing one — the gate <em>policy</em> that aggregates the stages'
 * findings — runnable and unit-tested, and pairs it with the pipeline that runs it:
 * <ul>
 *   <li>{@link org.acme.secgate.SecurityGate} — the decision: pool the findings every security stage
 *       produced, scope to new code, block on new exploitable high-severity findings, route the rest to a
 *       security reviewer. The local equivalent of the CI security gate.</li>
 *   <li>{@link org.acme.secgate.SecurityStage} — the five testing types (secrets, SAST, SCA, the
 *       container/IaC scan, and the dynamic DAST/IAST pair), ordered fast-to-slow, layered because each is
 *       blind to what the others see.</li>
 *   <li>{@link org.acme.secgate.SecurityGateDecision} — pass / route-to-review / block, the three-way
 *       verdict whose middle path keeps a red gate meaning something real instead of being turned off.</li>
 *   <li>{@link org.acme.secgate.SecurityFinding} — one finding a stage produced, carrying its stage,
 *       severity, scope, and whether it is confirmed exploitable.</li>
 *   <li>{@link org.acme.secgate.SecurityGatePolicy} — the externalized {@code dev} / {@code prod} policy
 *       (clean-as-you-code on/off, block severity, require-exploitable-to-block).</li>
 * </ul>
 *
 * <p>The pipeline that runs this gate is expressed as illustrative CI configuration in
 * {@code ci/security-pipeline.yml}: secrets scanning at pre-commit and CI, the fast SAST + SCA + secrets
 * trio at the pull request blocking on high severity, a container/IaC scan, and the slow DAST/IAST pair
 * against staging gating the release rather than the pull request. That file is configuration, not run by
 * this module's build; the runnable, tested part of the chapter is the gate policy this package
 * implements. {@code mvn -Pquality verify} runs the same static-analysis stage locally that the config
 * runs in CI, so the two cannot silently drift (Chapter 27, local/CI parity).
 *
 * <p>Honest edges, carried in the code's comments and not only in prose: gate fatigue is the killer, so
 * the gate blocks narrowly (new, high-severity, exploitable) and routes the rest to a reviewer rather than
 * auto-blocking every finding (Chapter 19); a green gate means "no detected, known, exploitable issue on
 * new code", not "secure" — the stages cannot find broken-access-control or business-logic flaws, which
 * need threat modeling and design review (Chapter 84), and the CVE databases the SCA stage reads cannot
 * find zero-days; exploitability is a judgment, so a severity number alone does not auto-block; the
 * dynamic stages cost real setup (a deployed app, scenarios) and a small internal app may rationally skip
 * them; and false positives compound across stages, so without ownership and triage the combined gate
 * becomes noise faster than any single tool would. The gate is one layer in a posture, not the whole.
 */
package org.acme.secgate;
