package org.acme.performance;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * A JMH microbenchmark of a small hashing computation, written two ways: the lying way the JVM
 * quietly defeats, and the honest way that survives the JIT. It is the chapter's "naive benchmarks
 * lie" idea as runnable code.
 *
 * <p><strong>This class is compiled, not run, by {@code verify}.</strong> JMH's annotation processor
 * generates the measurement harness at build time, so the benchmark compiling green is the build
 * gate. Running it honestly needs warmup and forks and belongs in a separate, offline run — see the
 * module README for the command. An honest result also depends on the JDK and the hardware it ran on,
 * so any number it prints would have to be reported with that environment, never quoted as a portable
 * fact.
 *
 * <p>The class fixes explicit warmup, measurement, and fork counts rather than relying on JMH's
 * defaults: the defaults move between JMH versions, and stating the counts is part of an honest,
 * reproducible benchmark. Run it with {@code -prof gc} to attach JMH's {@code GCProfiler} and turn the
 * timing number into an allocation diagnosis — the observability seam for this chapter.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 2, warmups = 1)
@State(Scope.Thread)
public class PricingBenchmark {

    /** A compile-time constant the JIT is free to fold away — used only by the lying benchmark. */
    private static final String CONSTANT_SKU = "SKU-CONSTANT-0001";

    /** Hands each trial a distinct input, so successive trials are never accidentally identical. */
    private static final AtomicLong TRIAL_SEQUENCE = new AtomicLong();

    // tag::state-setup[]
    /** The honest input: a NON-final field, so the JIT cannot constant-fold the computation away. */
    private String sku;

    @Setup(Level.Trial)
    public void setUp() {
        // Populate the field once per trial; the optimizer must treat it as variable, not a constant.
        this.sku = "SKU-" + TRIAL_SEQUENCE.incrementAndGet();
    }
    // end::state-setup[]

    // tag::lying-benchmark[]
    @Benchmark
    public void measureWrong() {
        // WRONG: the result is discarded and the input is a constant, so the JIT may delete the work
        // (dead-code elimination + constant folding) and report a time for code that never ran.
        hashOf(CONSTANT_SKU);
    }
    // end::lying-benchmark[]

    // tag::honest-benchmark[]
    @Benchmark
    public long measureRight() {
        // RIGHT: read the input from the non-final @State field and RETURN the result, so the work
        // is observable and the optimizer is not entitled to eliminate it.
        return hashOf(sku);
    }
    // end::honest-benchmark[]

    // tag::blackhole-sink[]
    @Benchmark
    public void measureTwoResults(Blackhole blackhole) {
        // When a benchmark produces more than one value, returning one is not enough: sink each one
        // through the Blackhole so neither is eliminated as dead code.
        blackhole.consume(hashOf(sku));
        blackhole.consume(sku.length());
    }
    // end::blackhole-sink[]

    /**
     * The work under measurement: a small, deterministic hash of the input characters. Pure and
     * side-effect-free, which is exactly why it is vulnerable to elimination unless the benchmark
     * makes its result observable.
     *
     * @param value the input to hash
     * @return a deterministic hash of the input
     */
    private static long hashOf(String value) {
        long hash = 1_125_899_906_842_597L;
        for (int i = 0; i < value.length(); i++) {
            hash = 31L * hash + value.charAt(i);
        }
        return hash;
    }

    /**
     * Runs this benchmark from a plain {@code java} launch — the offline run the README describes.
     * Kept out of {@code verify} (it warms up and forks for tens of seconds); it is how the benchmark
     * is exercised deliberately, not on the unit-test fast path.
     *
     * @param args ignored
     * @throws RunnerException if the JMH runner fails to start the forked benchmark JVMs
     */
    public static void main(String[] args) throws RunnerException {
        // The run counts come from the externalized profile (dev/prod), not from this source, so a
        // quick local run and a thorough run differ by -Dbenchmark.profile, not by an edit here. The
        // class-level @Warmup/@Measurement/@Fork annotations remain the in-source default; this
        // overrides them for the launched run.
        BenchmarkProfile profile = BenchmarkProfile.load();
        Options options = new OptionsBuilder()
            .include(PricingBenchmark.class.getSimpleName())
            .warmupIterations(profile.warmupIterations())
            .measurementIterations(profile.measurementIterations())
            .forks(profile.forks())
            .build();
        new Runner(options).run();
    }
}
