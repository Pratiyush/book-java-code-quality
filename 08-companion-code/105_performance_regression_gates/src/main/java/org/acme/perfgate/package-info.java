/**
 * Chapter 44 companion — the performance-regression gate (The Thousand Cuts).
 *
 * <p>Performance dies one imperceptible cut at a time: each pull request adds three milliseconds, all
 * below a reviewer's notice, until the p99 has tripled and no one can name the commit that did it. A
 * regression gate notices the three milliseconds a human cannot, by comparing a current measurement
 * against a committed baseline and flagging or failing when it regresses past a tolerance — which
 * makes performance a fitness function (Chapter 26) like coverage or security.
 *
 * <p>The module models that gate and implements its logic, runnably and unit-tested:
 * <ul>
 *   <li>{@link org.acme.perfgate.BenchmarkResult} — the noisy measurement a benchmark or load run
 *       hands the gate, carrying its confidence interval, not just a number.</li>
 *   <li>{@link org.acme.perfgate.Baseline} — the committed baseline, compared against relatively and
 *       ratcheted deliberately.</li>
 *   <li>{@link org.acme.perfgate.RegressionGate} — the comparison: relative-to-baseline,
 *       direction-aware, fail-safe.</li>
 *   <li>{@link org.acme.perfgate.GateVerdict} — pass / flag / fail, the flag-then-investigate
 *       posture that keeps a noisy gate from being disabled.</li>
 *   <li>{@link org.acme.perfgate.GateConfig} — externalized {@code dev} / {@code prod} tolerances.</li>
 * </ul>
 *
 * <p>What is deliberately <em>not</em> here: a running JMH harness or a load-runner. Producing a
 * measurement is environment-gated (a stable perf environment, a load tool of the Gatling/JMeter/k6
 * class); this chapter <em>protects</em> measured performance rather than measuring it (that is the
 * previous chapter's job). The gate logic is real; the numbers exercising it in the test suite are
 * synthetic and labelled as such — they are not measured benchmark claims.
 *
 * <p>Honest edges, carried in the code's comments: the gate compares relative-to-baseline because
 * absolute thresholds flap on runner noise (the flaky-gate trap, Chapter 20); it flags-then-
 * investigates small diffs rather than hard-blocking; the meaningful level is the macro/load metric
 * users feel (a microbenchmark alone misleads); the tolerances are illustrative, set from real
 * requirements (Chapter 43), not round numbers; a green gate means "no regression," not "fast enough"
 * (the baseline itself must be good); and it complements production monitoring (Chapter 45), it does
 * not replace it.
 */
package org.acme.perfgate;
