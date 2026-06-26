package org.acme.vthreads;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The race-free counterpart to {@link InconsistentlySyncedCounter} — the fix the chapter recommends.
 *
 * <p>Where the counter-example synchronizes its writes but leaks one unsynchronized read, this type
 * removes the question of lock discipline by holding the value in an {@link AtomicLong}: every read and
 * every write is atomic and correctly published, so SpotBugs reports no {@code IS2_INCONSISTENT_SYNC}
 * and Error Prone's {@code @GuardedBy} check has nothing to reject. The same fix could be reached by
 * guarding every access — read included — with the same lock; the atomic is the smaller, allocation-free
 * form for a single counter. The Java Memory Model rules are unchanged for virtual threads, so the fix
 * is the same one a platform thread needs: cheap threads do not relax the obligation.
 */
public final class GuardedCounter {

    private final AtomicLong count = new AtomicLong();

    /**
     * Increments the counter atomically.
     */
    public void increment() {
        count.incrementAndGet();
    }

    /**
     * Reads the counter atomically.
     *
     * @return the current count, correctly published across threads, never negative
     */
    public long current() {
        return count.get();
    }

    /**
     * Resets the counter to zero — used by the deterministic harness between rounds.
     */
    public void reset() {
        count.set(0L);
    }
}
