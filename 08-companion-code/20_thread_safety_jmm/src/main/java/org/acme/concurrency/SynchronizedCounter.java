package org.acme.concurrency;

import com.google.errorprone.annotations.concurrent.GuardedBy;

/**
 * A counter over a multi-field invariant, made correct with the monitor edge — the lock fix for a
 * shape an atomic cannot cover alone.
 *
 * <p>The count and the running maximum must move together: after any increment, {@code max} must
 * equal the largest count seen. That is a two-field invariant, so a single {@code AtomicLong} is not
 * enough; the update is wrapped in one {@code synchronized} block, which establishes the monitor
 * happens-before edge (JLS SE 21 &sect;17.4.5: an unlock happens-before every subsequent lock) — both
 * mutual exclusion and visibility. The {@link GuardedBy} annotation states which lock guards each
 * field; SpotBugs reads it to confirm the discipline holds on every access path, turning a
 * documentation comment into a checked contract. A private final lock object is held rather than
 * {@code this}, so no outside caller can synchronize on the same monitor by accident.
 */
public final class SynchronizedCounter {

    private final Object lock = new Object();

    @GuardedBy("lock")
    private long max;

    // tag::guarded-by[]
    @GuardedBy("lock")
    private long count;

    public void increment() {
        synchronized (lock) {            // the monitor edge: exclusion AND visibility
            count++;
            max = Math.max(max, count);  // count and max move together under one lock
        }
    }
    // end::guarded-by[]

    /**
     * Returns the current count.
     *
     * @return the number of increments observed so far
     */
    public long count() {
        synchronized (lock) {
            return count;
        }
    }

    /**
     * Returns the largest count observed so far.
     *
     * @return the running maximum, always consistent with {@link #count()}
     */
    public long max() {
        synchronized (lock) {
            return max;
        }
    }
}
