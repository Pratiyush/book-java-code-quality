package org.acme.vthreads;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The pinning trap and its fix, dated to the Java 21 anchor this module is built on.
 *
 * <p>On Java 21 (JEP 444) a virtual thread is <em>pinned</em> to its carrier while it executes inside a
 * {@code synchronized} block or method; if it then performs a blocking operation, the carrier and its
 * underlying platform thread are blocked for the duration. Under a large fan-out this starves the
 * carrier pool. {@link #guardedBySynchronized} is that shape: a blocking call held inside a
 * {@code synchronized} region. The documented mitigation is to guard the critical section with a
 * {@link ReentrantLock}, which does not pin — {@link #guardedByReentrantLock} is the fix.
 *
 * <p>The behaviour is version-specific, which is the chapter's point: JEP 491 (Java 24) removed
 * {@code synchronized} pinning, so on Java 24 and later the {@code synchronized} form no longer pins
 * (native and foreign-function calls still do). The advice "prefer a {@code ReentrantLock} around a
 * blocking call" therefore carries the JDK level it applies to. Both methods below compute the same
 * result; only their locking — and, on Java 21, their pinning behaviour — differs. Sonar
 * {@code java:S6906} flags the {@code synchronized}-on-a-virtual-thread shape directly.
 */
public final class PinningDemo {

    private final Object monitor = new Object();
    private final ReentrantLock lock = new ReentrantLock();
    private long served;

    /**
     * Updates shared state around a blocking call while holding the intrinsic monitor.
     *
     * <p>On Java 21 this pins the carrier for the duration of the blocking call. Kept as the dated
     * counter-example the fix below resolves.
     *
     * @param work the blocking unit of work to run inside the critical section, never {@code null}
     * @return the running count of served calls after this one, never negative
     * @throws NullPointerException if {@code work} is {@code null}
     * @throws InterruptedException if the calling thread is interrupted while blocked
     */
    public long guardedBySynchronized(Blocking work) throws InterruptedException {
        Objects.requireNonNull(work, "work");
        // tag::pinning-trap[]
        synchronized (monitor) {        // Java 21: a blocking call inside synchronized PINS the carrier
            work.run();                 // ... the carrier's platform thread is held for the whole call
            served++;
        }
        // end::pinning-trap[]
        return served;
    }

    /**
     * Updates the same shared state around the same blocking call, guarded by a {@link ReentrantLock}.
     *
     * <p>A {@code ReentrantLock} does not pin the carrier, so the virtual thread can unmount on the
     * blocking call and free its carrier. This is the Java 21 mitigation; on Java 24 and later it is no
     * longer necessary for {@code synchronized} (JEP 491).
     *
     * @param work the blocking unit of work to run inside the critical section, never {@code null}
     * @return the running count of served calls after this one, never negative
     * @throws NullPointerException if {@code work} is {@code null}
     * @throws InterruptedException if the calling thread is interrupted while blocked
     */
    public long guardedByReentrantLock(Blocking work) throws InterruptedException {
        Objects.requireNonNull(work, "work");
        // tag::pinning-fix[]
        lock.lock();                    // a ReentrantLock does not pin: the carrier is freed on blocking
        try {
            work.run();
            served++;
        } finally {
            lock.unlock();              // always release in finally, even if the blocking call threw
        }
        // end::pinning-fix[]
        return served;
    }

    /**
     * Returns how many calls have been served across both code paths.
     *
     * @return the running served count, never negative
     */
    public long servedCount() {
        return served;
    }

    /** A blocking unit of work; in production this is an I/O call, here a short, bounded sleep. */
    @FunctionalInterface
    public interface Blocking {
        /**
         * Runs the blocking work.
         *
         * @throws InterruptedException if interrupted while blocked
         */
        void run() throws InterruptedException;

        /**
         * Returns a blocking unit that sleeps for the given duration — a stand-in for a blocking I/O
         * call, so the pinning shape is exercised without a network dependency.
         *
         * @param duration how long to block, never {@code null} or negative
         * @return a blocking unit that sleeps for {@code duration}, never {@code null}
         * @throws NullPointerException     if {@code duration} is {@code null}
         * @throws IllegalArgumentException if {@code duration} is negative
         */
        static Blocking sleeping(Duration duration) {
            Objects.requireNonNull(duration, "duration");
            if (duration.isNegative()) {
                throw new IllegalArgumentException("duration must not be negative: " + duration);
            }
            return () -> Thread.sleep(duration.toMillis());
        }
    }
}
