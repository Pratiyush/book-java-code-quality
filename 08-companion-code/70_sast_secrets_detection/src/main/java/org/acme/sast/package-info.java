/**
 * Chapter 31 — Catching What You Forgot. The automated detection the secure-coding chapter depends on:
 * static application security testing (SAST) and secrets detection, demonstrated at the smallest size
 * that still teaches them.
 *
 * <p>This chapter is CONFIG-centric. The SAST and secrets tools run in the pipeline, not as runtime
 * dependencies, so their configuration is illustrative and lives outside {@code src/}: a Semgrep rule
 * ({@code config/semgrep/java-injection.yml}) catching the injection sink, a SAST CI workflow
 * ({@code ci/sast-scan.yml}), a gitleaks config ({@code .gitleaks.toml}), and a pre-commit hook
 * ({@code .pre-commit-config.yaml}). None of those files is run by the Maven build. What is runnable and
 * unit-tested here is the load-bearing pair of decisions the chapter argues for:
 *
 * <ul>
 *   <li><b>SAST finds the injection class.</b> {@link org.acme.sast.VulnerableOrderLookup} concatenates
 *       untrusted input into a SQL string — the source-to-sink flow taint analysis traces and that core
 *       SpotBugs (the engine behind FindSecBugs, Chapter 16) raises here. {@link org.acme.sast.OrderLookup}
 *       is the design-out fix: a bound parameter, so the value is data and the class is eliminated.</li>
 *   <li><b>Secrets stay out of source.</b> {@link org.acme.sast.SecretsResolver} reads a credential from
 *       externalized configuration and fails closed when it is absent — the prevention that secrets
 *       detection backstops. {@link org.acme.sast.SastSecretsGate} is the running path, wired to the
 *       fixes, with its failure path and health surfaces.</li>
 * </ul>
 *
 * <p>The honest edges the chapter states, carried here so they show up in code, not only in prose:
 *
 * <ul>
 *   <li><b>SAST finds patterns, not design.</b> Taint analysis traces tainted input to a SQL sink, but it
 *       sails past a broken authorization check or a business-logic flaw, because the code looks
 *       structurally correct (no source-to-sink flow). Those need design review (key 84), not a scanner.</li>
 *   <li><b>SAST produces false positives and false negatives.</b> A flagged sink may be unreachable, and a
 *       model may miss a flow it cannot trace. Findings are triaged, not auto-blocked wholesale.</li>
 *   <li><b>A committed secret is a compromised secret; detection is not remediation.</b> Deleting a leaked
 *       credential from {@code HEAD}, or even rewriting history, does not un-leak it. The only fix is to
 *       rotate it. A pre-commit hook is the only stage that prevents the leak, and it is bypassable
 *       ({@code git commit --no-verify}), so the CI gate and platform push protection are the backstops.</li>
 *   <li><b>Neither tool is a management solution.</b> SAST does not fix the code and secrets detection does
 *       not store the credential; the real fixes are secure coding (Chapter 30) and a secret manager.</li>
 * </ul>
 *
 * <p>This module realizes the storefront demo (DEMO-CATALOG): an order lookup and the session credential
 * the storefront needs, kept safe. It crowns no SAST or secrets tool — each is cited to its own docs.
 */
package org.acme.sast;
