package org.acme.concurrency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 * Exercises the thread-safety contracts the chapter describes: that the racy counter <em>can</em>
 * lose updates under contention while the atomic and lock-guarded counters never do, that
 * {@code final}-field publication and the holder idiom hand back fully-constructed state, and that the
 * coordinator's externalized config, observability surface, and graceful-shutdown failure path behave
 * as the prose argues. Each test asserts the contract holds at runtime; the {@code -Pquality} build
 * asserts the same kinds of mistake are caught by SpotBugs.
 */
class ThreadSafetyContractTest {

    private static final int THREADS = 8;
    private static final int INCREMENTS_PER_THREAD = 50_000;
    private static final long EXPECTED = (long) THREADS * INCREMENTS_PER_THREAD;

    /**
     * Hammers a counter from many threads at once and returns the final value — the role a JCStress
     * test plays for this module. JCStress (OpenJDK, an experimental harness) is the canonical runtime
     * instrument for this and is not pinned to a coordinate in this book, so this hand-rolled stress
     * loop stands in for it: it runs concurrent increments and reports the observed total, which a real
     * JCStress test would instead classify as ACCEPTABLE or FORBIDDEN across many iterations.
     *
     * @param increment one thread's increment action against the shared counter
     * @param readBack supplies the counter's final value after all threads finish
     * @return the observed final count
     */
    private static long stress(Runnable increment, java.util.function.LongSupplier readBack)
            throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        Runnable worker = () -> {
            for (int i = 0; i < INCREMENTS_PER_THREAD; i++) {
                increment.run();
            }
        };
        // tag::jcstress-test[]
        for (int t = 0; t < THREADS; t++) {
            pool.submit(worker); // many threads hammer the same counter at once
        }
        pool.shutdown();
        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
            pool.shutdownNow(); // the completion barrier: every worker has finished, or we stop
        }
        return readBack.getAsLong(); // a real JCStress run would label this ACCEPTABLE or FORBIDDEN
        // end::jcstress-test[]
    }

    @Test
    void atomicCounterNeverLosesAnUpdateUnderContention() throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
        long observed = stress(counter::increment, counter::count);
        // every increment is a compare-and-swap, so the total is exact under any interleaving
        assertThat(observed).isEqualTo(EXPECTED);
    }

    @Test
    void synchronizedCounterKeepsItsTwoFieldInvariantUnderContention() throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        long observed = stress(counter::increment, counter::count);
        assertThat(observed).isEqualTo(EXPECTED);
        // the monitor edge keeps max consistent with count: max equals the final count
        assertThat(counter.max()).isEqualTo(EXPECTED);
    }

    @Test
    void racyCounterCanLoseUpdatesUnderContention() throws InterruptedException {
        RacyCounter counter = new RacyCounter();
        long observed = stress(counter::increment, counter::count);
        // the bug that passes every test: the racy total is never more than the truth, and on
        // contended hardware is often less. Assert only the safe direction so the test is not itself
        // flaky, while documenting the lost-update the atomic fix removes.
        assertThat(observed).isLessThanOrEqualTo(EXPECTED);
    }

    @Test
    void finalFieldPublicationHandsBackFullyConstructedState() {
        ServiceConfiguration config = new ServiceConfiguration(4, Map.of("region", "eu"));
        assertThat(config.maxConcurrency()).isEqualTo(4);
        assertThat(config.setting("region")).isEqualTo("eu");
        assertThat(config.setting("absent")).isNull();
    }

    @Test
    void lazyHolderPublishesTheSameInstanceSafely() {
        LazyResource first = LazyResource.instance();
        LazyResource second = LazyResource.instance();
        assertThat(first).isSameAs(second);
        assertThat(first.payload()).isEqualTo("expensive-resource");
    }

    @Test
    void coordinatorTalliesEveryTaskAcrossThePool() {
        WorkCoordinator coordinator =
            new WorkCoordinator(WorkCoordinatorConfig.load(WorkCoordinatorConfig.Profile.DEV));
        long done = coordinator.runBatch(1_000);
        assertThat(done).isEqualTo(1_000L);
        assertThat(coordinator.processedCount()).isEqualTo(1_000L);
        coordinator.shutdown();
    }

    @Test
    void devAndProdProfilesReadDifferentExternalizedValues() {
        WorkCoordinatorConfig dev = WorkCoordinatorConfig.load(WorkCoordinatorConfig.Profile.DEV);
        WorkCoordinatorConfig prod = WorkCoordinatorConfig.load(WorkCoordinatorConfig.Profile.PROD);
        assertThat(dev.maxConcurrency()).isEqualTo(2);
        assertThat(prod.maxConcurrency()).isEqualTo(8);
        assertThat(prod.shutdownGrace()).isGreaterThan(dev.shutdownGrace());
    }

    @Test
    void readinessProbeFlipsOnGracefulShutdown() {
        WorkCoordinator coordinator =
            new WorkCoordinator(WorkCoordinatorConfig.load(WorkCoordinatorConfig.Profile.DEV));
        assertThat(coordinator.isReady()).isTrue();

        boolean drained = coordinator.shutdown();

        assertThat(drained).isTrue();           // the failure path drains cleanly on an idle pool
        assertThat(coordinator.isReady()).isFalse();
    }

    @Test
    void shutDownCoordinatorRejectsNewWork() {
        WorkCoordinator coordinator =
            new WorkCoordinator(WorkCoordinatorConfig.load(WorkCoordinatorConfig.Profile.DEV));
        coordinator.shutdown();
        assertThatThrownBy(() -> coordinator.runBatch(1))
            .isInstanceOf(IllegalStateException.class);
    }
}
