package org.acme.perfgate;

/**
 * One measured performance result — the shape a microbenchmark (JMH, Chapter 43) or a macro/load run
 * (Chapter 22) hands the gate. It carries the metric's name, its measured central value, the unit,
 * the direction in which "better" lies, and the half-width of the run-to-run confidence interval.
 *
 * <p>The confidence half-width is the load-bearing field: performance measurement is noisy (Chapter
 * 43), so a single number is not enough to gate on. Multiple runs yield a mean and an interval, and a
 * gate that ignores the interval flaps on jitter — the flaky-gate failure mode (Chapter 20) applied
 * to performance. This type models a result; it does not run a benchmark. Wiring an actual JMH
 * {@code @Benchmark} or a load-runner (Gatling/JMeter/k6 class) into CI is environment-gated and is
 * recorded as REPRO PENDING-RUNTIME in this chapter's gate report — the runnable, tested part of the
 * chapter is the gate logic that consumes a result, not the harness that produces one.
 *
 * @param metric             the metric name, for example {@code "checkout.p99"}, never {@code null}
 * @param value              the measured central value (a mean over several runs)
 * @param unit               the unit the value is in, for example {@code "ms"}, never {@code null}
 * @param direction          which way is better for this metric, never {@code null}
 * @param confidenceHalfWidth the +/- half-width of the measurement's confidence interval, {@code >= 0}
 */
// tag::benchmark-shape[]
public record BenchmarkResult(
        String metric, double value, String unit,
        MetricDirection direction, double confidenceHalfWidth) {
    public BenchmarkResult {            // a benchmark is noisy: carry its interval, not just a number
        if (confidenceHalfWidth < 0.0) {
            throw new IllegalArgumentException("confidenceHalfWidth must be >= 0");
        }
    }
}
// end::benchmark-shape[]
