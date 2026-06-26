package org.acme.perfgate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the regression gate: it drives the gate through the whole stack — externalized
 * config ({@link GateConfig#load}), a committed {@link Baseline}, and a current {@link BenchmarkResult}
 * — and asserts each verdict the chapter describes, end to end.
 *
 * <p>Every number below is <strong>synthetic</strong>: hand-chosen to land in a specific gate branch
 * (pass / flag / fail), <em>not</em> a measured benchmark result. The gate logic under test is real;
 * the inputs are fixtures. This keeps the chapter's honesty intact — no fabricated performance claim —
 * while still exercising the runnable comparison on both metric directions and the noise band.
 */
class RegressionGateTest {

    // The prod profile is the strict one: flag at 3%, fail at 10% (illustrative, from the properties).
    private final GateConfig config = GateConfig.load("prod");
    private final RegressionGate gate = new RegressionGate(config);

    /** A latency baseline of 100ms (synthetic, lower-is-better). */
    private static final Baseline LATENCY = new Baseline("checkout.p99", 100.0);

    private static BenchmarkResult latency(double valueMillis, double halfWidth) {
        return new BenchmarkResult("checkout.p99", valueMillis, "ms", MetricDirection.LOWER_IS_BETTER, halfWidth);
    }

    @Test
    @DisplayName("a measurement within tolerance of the baseline passes")
    void passesWithinTolerance() {
        // 101ms vs a 100ms baseline = 1% regression, under the 3% flag tolerance. Synthetic.
        GateVerdict verdict = gate.evaluate(LATENCY, latency(101.0, 0.0));

        assertThat(verdict).isInstanceOf(GateVerdict.Pass.class);
    }

    @Test
    @DisplayName("an improvement (faster than baseline) always passes")
    void passesOnImprovement() {
        // 80ms vs 100ms — faster. A negative regression must never flag or fail. Synthetic.
        GateVerdict verdict = gate.evaluate(LATENCY, latency(80.0, 0.0));

        assertThat(verdict).isInstanceOf(GateVerdict.Pass.class);
    }

    @Test
    @DisplayName("flag-then-investigate: a small regression past the flag band is flagged, not failed")
    void flagsSmallRegression() {
        // 106ms vs 100ms = 6% regression: past the 3% flag band, under the 10% fail band, and the
        // tiny 0.5ms confidence interval cannot explain a 6ms move, so it is a real-but-small diff.
        GateVerdict verdict = gate.evaluate(LATENCY, latency(106.0, 0.5));

        assertThat(verdict).isInstanceOf(GateVerdict.Flag.class);
        assertThat(((GateVerdict.Flag) verdict).reason()).contains("6.0%");
    }

    @Test
    @DisplayName("a large, confident regression past the fail tolerance blocks the build")
    void failsLargeRegression() {
        // 130ms vs 100ms = 30% regression, far past the 10% fail band, and far outside the 1ms
        // confidence interval — a confident regression, the case where hard-block is correct. Synthetic.
        GateVerdict verdict = gate.evaluate(LATENCY, latency(130.0, 1.0));

        assertThat(verdict).isInstanceOf(GateVerdict.Fail.class);
    }

    @Test
    @DisplayName("fail-safe: a regression inside the measurement's noise band is never failed")
    void noisyRegressionIsNotFailed() {
        // 130ms vs 100ms would be a 30% regression — but the confidence half-width is +/- 40ms, so the
        // baseline sits inside the interval: the two are statistically indistinguishable. Hard-blocking
        // here is the flaky-gate trap (Chapter 20), so the gate passes. Synthetic numbers.
        GateVerdict verdict = gate.evaluate(LATENCY, latency(130.0, 40.0));

        assertThat(verdict).isInstanceOf(GateVerdict.Pass.class);
    }

    @Test
    @DisplayName("direction matters: a throughput drop is a regression, a rise is not")
    void throughputIsDirectionAware() {
        Baseline throughput = new Baseline("checkout.tps", 1_000.0); // higher-is-better, synthetic
        BenchmarkResult dropped =
            new BenchmarkResult("checkout.tps", 800.0, "tps", MetricDirection.HIGHER_IS_BETTER, 1.0);
        BenchmarkResult rose =
            new BenchmarkResult("checkout.tps", 1_200.0, "tps", MetricDirection.HIGHER_IS_BETTER, 1.0);

        // 1000 -> 800 = 20% drop in throughput: a regression past the 10% fail band.
        assertThat(gate.evaluate(throughput, dropped)).isInstanceOf(GateVerdict.Fail.class);
        // 1000 -> 1200 = throughput improved: a pass, the same number that would FAIL for latency.
        assertThat(gate.evaluate(throughput, rose)).isInstanceOf(GateVerdict.Pass.class);
    }

    @Test
    @DisplayName("a baseline is ratcheted by replacement, never mutated")
    void baselineRatchetsDeliberately() {
        Baseline moved = LATENCY.movedTo(90.0); // a sanctioned perf win lowers the latency baseline

        assertThat(moved.value()).isEqualTo(90.0);
        assertThat(LATENCY.value()).isEqualTo(100.0); // the original is unchanged — records are immutable
        // After the ratchet, 95ms is now a 5.6% regression against the tighter 90ms baseline.
        assertThat(gate.evaluate(moved, latency(95.0, 0.1))).isInstanceOf(GateVerdict.Flag.class);
    }

    @Test
    @DisplayName("the externalized dev profile is more forgiving than prod")
    void devProfileIsLooser() {
        GateConfig dev = GateConfig.load("dev"); // flag 10%, fail 25% — wider bands than prod
        RegressionGate devGate = new RegressionGate(dev);

        // 106ms = 6% regression: FLAGGED under prod (3% flag), but a clean PASS under dev (10% flag).
        assertThat(devGate.evaluate(LATENCY, latency(106.0, 0.5))).isInstanceOf(GateVerdict.Pass.class);
    }

    @Test
    @DisplayName("an out-of-order tolerance pair is rejected at construction")
    void invalidConfigIsRejected() {
        // fail tolerance below flag tolerance is a configuration error — fail fast.
        assertThatThrownBy(() -> new GateConfig(0.20, 0.05))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("a negative confidence half-width is rejected at construction")
    void invalidResultIsRejected() {
        assertThatThrownBy(() -> latency(100.0, -1.0))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
