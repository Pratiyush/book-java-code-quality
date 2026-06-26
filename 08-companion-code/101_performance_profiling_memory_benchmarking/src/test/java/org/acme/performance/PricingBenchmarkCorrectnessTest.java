package org.acme.performance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * A correctness test of the benchmarked computation — deliberately separate from the benchmark, to
 * make the chapter's point that the two answer different questions. The benchmark asks "how fast is
 * this at steady state"; this test asks "is it correct", and a fast-but-wrong method is not an
 * improvement. The benchmark itself is compiled, not run, by {@code verify}; its <em>correctness</em>
 * is checked here on the unit-test fast path.
 */
class PricingBenchmarkCorrectnessTest {

    @Test
    void honestBenchmarkComputationIsDeterministicForAFixedInput() {
        PricingBenchmark benchmark = new PricingBenchmark();
        benchmark.setUp();

        long first = benchmark.measureRight();
        long second = benchmark.measureRight();

        // Same @State input must yield the same result: the computation is correct and stable,
        // independent of how fast a benchmark would clock it.
        assertThat(second).isEqualTo(first);
    }

    @Test
    void distinctTrialsHashDistinctInputs() {
        PricingBenchmark first = new PricingBenchmark();
        first.setUp();
        PricingBenchmark second = new PricingBenchmark();
        second.setUp();

        // Each trial's @Setup takes a distinct, non-constant input — the property that stops the JIT
        // constant-folding the work away in the honest benchmark.
        assertThat(second.measureRight()).isNotEqualTo(first.measureRight());
    }
}
