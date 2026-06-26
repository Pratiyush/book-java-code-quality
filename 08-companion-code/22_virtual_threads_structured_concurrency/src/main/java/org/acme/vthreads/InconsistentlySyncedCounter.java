package org.acme.vthreads;

/**
 * A deliberately inconsistently-synchronized counter — the chapter's lock-discipline counter-example.
 *
 * <p>{@link #increment()} and {@link #currentSynchronized()} are {@code synchronized}, so they hold the
 * intrinsic lock ({@code this}) on every access to {@code count}. {@link #current()} reads the same field
 * <em>without</em> synchronization, breaking that discipline. That is the shape two static detectors are
 * built to catch, each cited to its own source:
 *
 * <ul>
 *   <li>SpotBugs reports it from bytecode as {@code IS2_INCONSISTENT_SYNC} (the {@code MT_CORRECTNESS}
 *       category): a field most of whose accesses are synchronized, read once without the lock.</li>
 *   <li>Error Prone's {@code com.google.errorprone.annotations.concurrent.GuardedBy} check rejects it at
 *       compile time (severity ERROR) when the field is annotated {@code @GuardedBy("this")} and accessed
 *       outside the lock. This module pins no Error Prone plugin, so the guard is documented on the field
 *       rather than annotated; naming the package matters because {@code @GuardedBy} exists in four
 *       packages with different enforcement.</li>
 * </ul>
 *
 * <p>The unsynchronized read is a real Java Memory Model defect, not a style nit: a virtual thread is a
 * {@code java.lang.Thread}, so the visibility rules of JLS chapter 17 apply identically and the read may
 * observe a stale value. {@link GuardedCounter} is the fix. This class is the one place the module's
 * SpotBugs filter carries a narrowly-scoped, reasoned suppression — the finding is the teaching point.
 */
public final class InconsistentlySyncedCounter {

    private long count;                          // @GuardedBy("this") — documented, not annotated

    // tag::guardedby-failure[]
    public synchronized void increment() {       // every WRITE holds the lock (this)
        count++;
    }
    public synchronized long currentSynchronized() {
        return count;                            // a guarded read — the discipline the field declares
    }
    public long current() {
        return count;        // unguarded READ -> IS2_INCONSISTENT_SYNC / Error Prone @GuardedBy ERROR
    }
    // end::guardedby-failure[]

    /**
     * Resets the counter under the lock — used by the deterministic harness between rounds.
     */
    public synchronized void reset() {
        count = 0L;
    }
}
