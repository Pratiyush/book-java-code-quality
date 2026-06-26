/**
 * Chapter 33 companion — the CI pipeline and its quality gate (A Gate the Team Keeps On).
 *
 * <p>A team can build every gate in this book — the analyzers, the test and coverage suites, the
 * architecture rules, the dependency and security scans — and still watch the quality program collapse
 * the month they wire all of it into one pipeline, set everything to block, and run it on every pull
 * request. Not one gate was wrong; the pipeline was never designed. Designing it so the team keeps it on
 * is three decisions: order the stages for fast feedback, scope the policy to new code, and keep the
 * whole thing fast enough that nobody routes around it.
 *
 * <p>This module makes the load-bearing one — the gate <em>policy</em> — runnable and unit-tested, and
 * pairs it with the pipeline that runs it:
 * <ul>
 *   <li>{@link org.acme.cigate.QualityGate} — the decision: scope to new code (clean-as-you-code),
 *       block on new high-severity findings, warn on the rest. The local equivalent of the CI gate.</li>
 *   <li>{@link org.acme.cigate.GateDecision} — pass / warn / block, the three-way verdict that keeps a
 *       red gate meaning something real instead of being turned off.</li>
 *   <li>{@link org.acme.cigate.Finding} — one finding a stage produced, carrying its severity and
 *       whether it sits in new or pre-existing code.</li>
 *   <li>{@link org.acme.cigate.GatePolicy} — the externalized {@code dev} / {@code prod} policy
 *       (clean-as-you-code on/off, block severity).</li>
 * </ul>
 *
 * <p>The pipeline that runs this gate is expressed as illustrative CI configuration in
 * {@code ci/quality-gates.yml}: ordered fail-fast stages (compile and fast lint, then unit tests and
 * coverage, then heavier static analysis and a dependency scan), a pull-request-fast versus main-full
 * split that defers the expensive checks off the critical path, dependency caching, and parallel
 * execution. That file is configuration, not run by this module's build; the runnable, tested part of
 * the chapter is the gate policy this package implements. {@code mvn -Pquality verify} runs the same
 * static-analysis stage locally that the config runs in CI, so the two cannot silently drift.
 *
 * <p>Honest edges, carried in the code's comments: the gate blocks new-code findings only, because
 * whole-repo absolutes block every pull request on inherited debt (Chapter 19); a green gate means "no
 * detected policy violations on new code," not good code — design and logic still need human review
 * (Chapter 84); a gate measures a proxy, so one gated on a number gets gamed (coverage percentage to
 * assertion-free tests, Chapter 23); the cache that keeps the pipeline fast can false-green on a bad key
 * (a clean build on the trunk backstops it); and no pipeline fixes a culture that rubber-stamps red
 * builds (Chapter 1) — the gate enforces policy, it does not create the will to care.
 */
package org.acme.cigate;
