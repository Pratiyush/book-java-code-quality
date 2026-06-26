package org.acme.concurrency;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A shared counter made correct with an atomic — the lock-free fix for {@link RacyCounter}.
 *
 * <p>{@link AtomicLong#incrementAndGet()} performs the read-modify-write as one indivisible
 * compare-and-swap in hardware, so no increment is lost under any interleaving, with no lock to
 * acquire. This is the {@code java.util.concurrent} stance the chapter argues for: reach for a tested
 * building block that already encodes the edge before hand-rolling one. {@code AtomicLong} fits a
 * single shared variable; a multi-field invariant still needs a lock (see {@link SynchronizedCounter}),
 * and under very high contention {@code LongAdder} trades space for throughput.
 */
public final class AtomicCounter {

    private final AtomicLong count = new AtomicLong();

    /**
     * Increments the counter atomically.
     */
    public void increment() {
        count.incrementAndGet();
    }

    /**
     * Returns the current count.
     *
     * @return the number of increments observed so far
     */
    public long count() {
        return count.get();
    }
}
