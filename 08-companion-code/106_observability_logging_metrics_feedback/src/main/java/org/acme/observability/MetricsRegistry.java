package org.acme.observability;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * A minimal in-process metrics registry: monotonic counters and a latency timer, keyed by name —
 * enough to expose the four golden signals (latency, traffic, errors, saturation; Google SRE) on a
 * scrape endpoint without a metrics library. A production system publishes to a real backend through
 * a facade (Micrometer, then OpenTelemetry); the shape here — name a meter, record into it, snapshot
 * it — is that idea at the smallest size that still teaches it.
 *
 * <p>Meter names are bounded by construction: a counter is keyed by a stable name like
 * {@code "checkout.requests"}, never by a per-request value. Putting a user id or order id in a meter
 * name is the number-one metrics disaster — one time series per distinct value blows up storage and
 * cost — so high-cardinality slicing belongs in a trace or a log line, not here.
 */
public final class MetricsRegistry {

    private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();

    // tag::metric-counter[]
    /** Adds one to a named counter — traffic and error totals (two of the four golden signals). */
    public void increment(String name) {
        counters.computeIfAbsent(name, key -> new LongAdder()).increment();
    }
    // end::metric-counter[]

    /** The current value of a named counter, or {@code 0} if it has never been incremented. */
    public long count(String name) {
        LongAdder adder = counters.get(name);
        return adder == null ? 0L : adder.sum();
    }

    // tag::metric-timer[]
    /** Records one latency sample (nanoseconds) into a named timer — the latency golden signal. */
    public void recordNanos(String name, long nanos) {
        timers.computeIfAbsent(name, key -> new Timer()).record(nanos);
    }
    // end::metric-timer[]

    /** The mean latency of a named timer in milliseconds, or {@code 0} if it has no samples. */
    public double meanMillis(String name) {
        Timer timer = timers.get(name);
        return timer == null ? 0.0 : timer.meanMillis();
    }

    /** A point-in-time snapshot of every meter — safe to serialize for a scrape endpoint. */
    public Map<String, Object> snapshot() {
        Map<String, Object> out = new TreeMap<>();
        counters.forEach((name, adder) -> out.put(name, adder.sum()));
        timers.forEach((name, timer) -> out.put(name + ".mean_ms", timer.meanMillis()));
        return out;
    }

    /** A thread-safe accumulator for latency samples: total nanoseconds and a sample count. */
    private static final class Timer {
        private final LongAdder totalNanos = new LongAdder();
        private final LongAdder samples = new LongAdder();

        void record(long nanos) {
            totalNanos.add(nanos);
            samples.increment();
        }

        double meanMillis() {
            long n = samples.sum();
            return n == 0 ? 0.0 : (totalNanos.sum() / (double) n) / 1_000_000.0;
        }
    }
}
