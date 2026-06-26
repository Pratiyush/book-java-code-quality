/**
 * Chapter 38 — measuring a quality program so it helps rather than harms (Knowing Whether It Works).
 *
 * <p>The package makes the chapter's three surfaces runnable at the smallest size that still teaches
 * them, on the JDK alone.
 *
 * <ul>
 *   <li><b>Which metrics</b> — {@link org.acme.metrics.DoraMetrics} computes DORA's four keys
 *       (deployment frequency, lead time for changes, change-failure rate, failed-deployment recovery
 *       time) from {@link org.acme.metrics.DeploymentRecord deployment records}, using the
 *       definitional formulas only; it asserts no performance band, because the bands are
 *       edition-specific and verified against the pinned State-of-DevOps edition rather than compiled
 *       in. {@link org.acme.metrics.CounterMetric} pairs an outcome metric with its counter-metric
 *       (throughput with stability) and refuses to report one without the other, so gaming one shows
 *       up in the other. {@link org.acme.metrics.MetricKind} marks a metric as an outcome, a quality
 *       trend, or vanity, and carries the system-versus-individual scope.</li>
 *   <li><b>How to roll out</b> — {@link org.acme.metrics.RolloutPolicy} is the baseline-and-ratchet
 *       engine: it accepts the legacy past as a baseline, gates only <em>new</em> findings, and lets a
 *       threshold move in the better direction only. A regression is a
 *       {@link org.acme.metrics.RolloutDecision.Blocked blocked} decision, not an exception thrown
 *       into the void.</li>
 *   <li><b>How to present</b> — {@link org.acme.metrics.DashboardSpec} is the "good dashboard" model:
 *       every tile carries a counter-metric and a new-code lens, and a request to rank individuals is
 *       refused, because a leaderboard weaponizes the dashboard (Goodhart, and morale).</li>
 * </ul>
 *
 * <p>The chapter attributes the named authorities — DORA / the <em>Accelerate State of DevOps</em>
 * report for the four keys and the throughput/stability finding, and SPACE (Forsgren et al.) for the
 * multi-dimensional productivity frame. Neither is a pinned authority row for this book, so the code
 * shows the recurring discipline — measure outcomes not outputs, pair every metric with a
 * counter-metric, gate the future not the past, never rank people — with JDK primitives, and the prose
 * carries the citations. Every number here is treated as a question, never a verdict.
 */
package org.acme.metrics;
