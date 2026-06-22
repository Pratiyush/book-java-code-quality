package org.acme.platform.obs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * A minimal in-process metrics registry (Part: observability). It records monotonically increasing
 * counters keyed by name — enough to expose request totals and outcomes on a {@code /metrics}
 * endpoint without a metrics library. A production system would publish to a real backend
 * (Micrometer, OpenTelemetry); the shape here — name a metric, increment it, scrape it — is the
 * same idea at the smallest size that still teaches it.
 */
public final class Metrics {

    private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

    public void increment(String name) {
        counters.computeIfAbsent(name, key -> new LongAdder()).increment();
    }

    public long count(String name) {
        LongAdder adder = counters.get(name);
        return adder == null ? 0L : adder.sum();
    }

    /** A point-in-time snapshot of every counter — safe to serialize for a scrape endpoint. */
    public Map<String, Object> snapshot() {
        Map<String, Object> out = new java.util.TreeMap<>();
        counters.forEach((name, adder) -> out.put(name, adder.sum()));
        return out;
    }
}
