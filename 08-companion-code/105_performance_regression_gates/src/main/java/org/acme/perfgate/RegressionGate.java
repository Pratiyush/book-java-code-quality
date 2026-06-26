package org.acme.perfgate;

import java.util.Locale;
import java.util.Objects;

/**
 * The performance-regression gate: it compares a current {@link BenchmarkResult} against a committed
 * {@link Baseline} and returns a {@link GateVerdict} of pass, flag, or fail. The comparison is the
 * honest one the chapter argues for, with three properties doing the work:
 *
 * <ul>
 *   <li><b>Relative to baseline, not absolute.</b> The gate measures the fractional change from the
 *       baseline, so it rides shared-runner noise up and down instead of flapping against a fixed
 *       number (Chapter 43).</li>
 *   <li><b>Direction-aware.</b> A rise is a regression for a lower-is-better metric and an improvement
 *       for a higher-is-better one ({@link MetricDirection}); an improvement is always a pass.</li>
 *   <li><b>Flag-then-investigate, fail-safe.</b> A regression inside the measurement's own confidence
 *       band, or below the flag tolerance, is treated as possible noise and at most flagged; only a
 *       regression past the fail tolerance <em>and</em> larger than the noise band blocks the build.
 *       A gate that hard-blocks on jitter gets disabled, and a disabled gate is no gate (Chapter 20).</li>
 * </ul>
 *
 * <p>This gate is real and unit-tested. It does not run a benchmark or a load test itself: producing a
 * {@link BenchmarkResult} from a JMH harness or a load-runner is environment-gated (REPRO
 * PENDING-RUNTIME, see the gate report). The numbers in the tests are <em>synthetic</em>, chosen to
 * exercise each branch — they are not measured benchmark claims.
 */
public final class RegressionGate {

    private final GateConfig config;

    /**
     * Creates a gate that decides against the given tolerances.
     *
     * @param config the externalized flag/fail tolerances, never {@code null}
     */
    public RegressionGate(GateConfig config) {
        this.config = Objects.requireNonNull(config, "config");
    }

    /**
     * Compares {@code current} against {@code baseline} and returns the verdict.
     *
     * @param baseline the committed baseline for the metric, never {@code null}
     * @param current  the current measurement, never {@code null}
     * @return the gate verdict — pass, flag, or fail — never {@code null}
     */
    // tag::regression-gate[]
    public GateVerdict evaluate(Baseline baseline, BenchmarkResult current) {
        double regression = regressionFraction(baseline.value(), current.value(), current.direction());
        if (regression <= config.flagTolerance() || withinNoiseBand(baseline, current)) {
            return new GateVerdict.Pass(describe("within tolerance", regression)); // ride the noise
        }
        return regression < config.failTolerance()                  // fail-safe: flag before block
            ? new GateVerdict.Flag(describe("small regression — investigate", regression))
            : new GateVerdict.Fail(describe("regression past tolerance", regression));
    }
    // end::regression-gate[]

    /**
     * The fractional regression from {@code baseline} to {@code current}, oriented by direction. A
     * positive result is a regression (worse); zero or negative means as-good-or-better, which the
     * gate always passes.
     */
    private static double regressionFraction(double baseline, double current, MetricDirection direction) {
        if (baseline == 0.0) {
            return 0.0; // no meaningful relative comparison against a zero baseline
        }
        double delta = direction == MetricDirection.LOWER_IS_BETTER
            ? current - baseline   // for latency, a higher current is worse
            : baseline - current;  // for throughput, a lower current is worse
        return delta / Math.abs(baseline);
    }

    /**
     * Whether the regression is inside the measurement's own confidence band — if the baseline value
     * falls within {@code current.value +/- confidenceHalfWidth}, the two are statistically
     * indistinguishable and the apparent regression may be pure noise. Failing the build on that is
     * the flaky-gate trap, so such a result is never failed.
     */
    private static boolean withinNoiseBand(Baseline baseline, BenchmarkResult current) {
        return Math.abs(current.value() - baseline.value()) <= current.confidenceHalfWidth();
    }

    private static String describe(String label, double regression) {
        return String.format(Locale.ROOT, "%s (%.1f%% vs baseline)", label, regression * 100.0);
    }
}
