package org.acme.concurrency;

/**
 * A shared counter with a data race — the deliberate counter-example, the bug that passes every test,
 * kept runnable so the lost update is exhibited under contention and flagged by SpotBugs, while
 * {@link org.acme.concurrency.ThreadSafetyContractTest} pins only the safe invariant.
 *
 * <p>This is the hook made concrete. The field is {@code volatile}, so a reader always sees the
 * latest write (the {@code volatile} happens-before edge gives <em>visibility</em>); but
 * {@code count++} is a read-modify-write, not one indivisible step, so two threads can both read the
 * same value, both add one, and one increment is lost (no <em>atomicity</em>). Visibility is not
 * atomicity: a marker the chapter returns to. At this module's threshold SpotBugs raises
 * {@code VO_VOLATILE_INCREMENT} on the {@code increment()} body (and the broader
 * {@code AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE} once {@code count()} reads the field) — verified
 * against the build, so the reviewed suppression is load-bearing, not decorative. The fixes are
 * {@link org.acme.concurrency.AtomicCounter} and {@link org.acme.concurrency.SynchronizedCounter},
 * never this suppression.
 */
public final class RacyCounter {

    private volatile long count;

    /**
     * Increments the counter without establishing atomicity — the data race.
     */
    // tag::racy-counter[]
    public void increment() {
        count++; // SMELL (VO_VOLATILE_INCREMENT): increment of a volatile field is not atomic
    }

    public long count() {
        return count; // a volatile read sees the latest write — but increments may already be lost
    }
    // end::racy-counter[]
}
