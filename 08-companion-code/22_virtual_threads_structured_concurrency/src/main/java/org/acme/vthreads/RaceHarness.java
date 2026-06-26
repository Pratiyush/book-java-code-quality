package org.acme.vthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A deterministic concurrency harness that exposes a data race repeatably, on virtual threads.
 *
 * <p>Concurrency bugs hide because they need a particular interleaving on particular hardware. Two
 * complementary disciplines drive them out, and this harness stands in for both with stable JDK
 * primitives, because the production stress instrument is not available here. The OpenJDK Java
 * Concurrency Stress harness (jcstress, {@code org.openjdk.jcstress:jcstress-core}) is the real
 * tool — it declares shared state in a {@code @State} class, races {@code @Actor} methods, and grades
 * every observed {@code @Outcome} as {@code ACCEPTABLE}/{@code ACCEPTABLE_INTERESTING}/{@code FORBIDDEN}.
 * That harness is experimental and probabilistic: a green run means the forbidden outcome was not
 * observed on this hardware in the time given, not that it cannot happen. It is not pinned in this
 * book's source set, so it is not added as a dependency; this harness instead <em>forces</em> the racing
 * window deterministically with a {@link CountDownLatch}, which proves the bug for the schedule
 * engineered. The two are complementary, not substitutes: stress <em>samples</em> interleavings,
 * deterministic tests <em>force</em> one. Neither is a {@code Thread.sleep}-timed test, which retires.
 */
public final class RaceHarness {

    private RaceHarness() {
    }

    /**
     * The shared state under test, named for the jcstress {@code @State} role: one counter, raced by
     * many incrementing actors. Held as a record so this harness and a future jcstress {@code @State}
     * test describe the same shape. The {@code actor()} method is the {@code @Actor} role — one
     * increment, run concurrently by every racing virtual thread.
     *
     * @param counter the unguarded counter every actor mutates, never {@code null}
     */
    // tag::jcstress-state-actors[]
    public record SharedState(InconsistentlySyncedCounter counter) {   // the @State role
        void actor() {            // the @Actor role: one increment, raced by every virtual thread
            counter.increment();
        }
    }
    // end::jcstress-state-actors[]

    /**
     * Runs {@code threads} concurrent increments of the given counter, all released at the same instant,
     * and returns the value the counter reports afterwards.
     *
     * @param state   the shared state to race, never {@code null}
     * @param threads the number of racing virtual threads, strictly positive
     * @return the counter value observed after all increments complete
     * @throws InterruptedException if the harness is interrupted while awaiting completion
     */
    public static long raceIncrements(SharedState state, int threads) throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);          // a single gate for all racers
        List<Future<?>> forks = new ArrayList<>();
        // tag::deterministic-latch-test[]
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                forks.add(executor.submit(() -> {
                    awaitQuietly(start);    // park until released, to maximize the racing overlap
                    state.actor();
                }));
            }
            start.countDown();              // release every racer at the same instant
        }
        // end::deterministic-latch-test[]
        for (Future<?> fork : forks) {
            joinQuietly(fork);
        }
        return state.counter().current();
    }

    private static void awaitQuietly(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void joinQuietly(Future<?> fork) {
        try {
            fork.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (java.util.concurrent.ExecutionException e) {
            throw new IllegalStateException("racing increment failed", e.getCause());
        }
    }
}
