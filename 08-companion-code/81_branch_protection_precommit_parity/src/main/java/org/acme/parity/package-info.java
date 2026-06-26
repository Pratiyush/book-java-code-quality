/**
 * Chapter 35 companion — branch protection, merge queues, trunk-based development, and pre-commit hooks
 * with local&#8596;CI parity (Teeth and Speed).
 *
 * <p>A team can build an excellent quality gate and still watch {@code main} go red half the time: the
 * gate is configured as an optional check so it is merged past under deadline pressure; two
 * individually-green pull requests merge the same afternoon and break {@code main} together; and a
 * developer meets the gate only after pushing, so a formatting nit costs a ten-minute CI round-trip. The
 * gate has no teeth (it is bypassable), no collision safety, and no speed at the keyboard. This chapter
 * is the workflow that fixes all three.
 *
 * <p>This module makes the load-bearing claim — local&#8596;CI parity — runnable and unit-tested, and
 * pairs it with the configuration that gives the gate teeth and speed:
 * <ul>
 *   <li>{@link org.acme.parity.GateParity} — the decision: the local gate set (pre-commit hooks plus the
 *       local build) must reproduce every check CI requires, so "green locally" predicts "green in CI".</li>
 *   <li>{@link org.acme.parity.ParityResult} — in-parity or drifted, the drifted case naming the
 *       CI-required checks with no local counterpart, the "works locally, fails in CI" symptom.</li>
 *   <li>{@link org.acme.parity.GateSet} — a named set of check names (local or CI) compared by exact name.</li>
 *   <li>{@link org.acme.parity.ParityPolicy} — the externalized {@code dev} / {@code prod} policy
 *       (whether drift fails the build, the local-runner label).</li>
 *   <li>{@link org.acme.parity.ParityBrokenException} — the explicit failure path a strict policy takes.</li>
 * </ul>
 *
 * <p>The configuration the chapter teaches lives beside the build, not run by it:
 * {@code config/branch-protection/ruleset.yml} records the protections that give the gate teeth (the
 * quality gate as a required status check, required review, linear history, restricted force-push and
 * deletion), and {@code .pre-commit-config.yaml} records the fast hooks that give it speed (format, a
 * fast lint subset, a secrets scan). Both name hosted/SaaS surfaces (GitHub rulesets, the pre-commit
 * framework) that SOURCE-PIN.md §5 records as rolling, so their identifiers are dated-at-use (2026-06)
 * and flagged for pinning at adoption.
 *
 * <p>Honest edges, carried in the code's comments and its failure path: pre-commit hooks are FEEDBACK,
 * not enforcement — they are bypassable ({@code git commit --no-verify}) and run only where installed,
 * so CI and branch protection remain the gate; trunk-based development is not "just commit to
 * {@code main}" — it demands a trustworthy gate, reliable tests, and feature flags for incomplete work;
 * merge queues add latency and cost and are overkill for a low-merge-rate repo; over-strict protection
 * slows delivery and breeds bypass requests; parity is hard to maintain (local and CI tool versions
 * drift — the wrapper and pins are required, and even then OS/Docker differences can diverge); and
 * branching strategy is context-dependent, with the book crowning no model.
 */
package org.acme.parity;
