package org.acme.vthreads;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import org.acme.vthreads.FanOutFetcher.FetchOutcome;
import org.acme.vthreads.RaceHarness.SharedState;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's virtual-thread mechanisms against the compiled module: the fan-out happy path
 * and its timeout failure path, the observability counters and readiness probe, the pinning fix's
 * correctness, the guarded vs inconsistently-synchronized counters under a forced race, and the
 * structured-concept demo's success and failure. Each test asserts a contract that holds at runtime; the
 * {@code -Pquality} build asserts the lock-discipline defect is also caught by SpotBugs.
 */
class VirtualThreadsTest {

    @Test
    void fanOutFetchesEveryTargetConcurrently() {
        FanOutFetcher fetcher = new FanOutFetcher(target -> "payload-" + target);
        FanOutConfig config = FanOutConfig.forProfile("dev");

        List<FetchOutcome> outcomes = fetcher.fetchAll(config);

        assertThat(outcomes).hasSize(config.targets().size());
        assertThat(outcomes).allMatch(FetchOutcome::succeeded);
        assertThat(outcomes).extracting(o -> o.result().target())
            .containsExactlyElementsOf(config.targets());
        assertThat(fetcher.completedCount()).isEqualTo(config.targets().size());
        assertThat(fetcher.failedCount()).isZero();
        assertThat(fetcher.isReady()).isTrue();
    }

    @Test
    void fanOutBoundsASlowFetchWithTheTimeoutFailurePath() {
        // a backend that blocks far longer than the per-call timeout drives the explicit failure path
        FanOutFetcher fetcher = new FanOutFetcher(target -> {
            Thread.sleep(Duration.ofSeconds(30).toMillis());
            return "never";
        });
        FanOutConfig config = new FanOutConfig(List.of("slow"), 1, Duration.ofMillis(50));

        List<FetchOutcome> outcomes = fetcher.fetchAll(config);

        assertThat(outcomes).hasSize(1);
        assertThat(outcomes.get(0).succeeded()).isFalse();
        assertThat(outcomes.get(0).reason()).isEqualTo("timeout");
        assertThat(fetcher.failedCount()).isEqualTo(1L);
    }

    @Test
    void pinningFixComputesTheSameResultAsTheSynchronizedForm() throws InterruptedException {
        PinningDemo demo = new PinningDemo();
        PinningDemo.Blocking work = PinningDemo.Blocking.sleeping(Duration.ofMillis(1));

        long afterSynchronized = demo.guardedBySynchronized(work);
        long afterReentrantLock = demo.guardedByReentrantLock(work);

        assertThat(afterSynchronized).isEqualTo(1L);
        assertThat(afterReentrantLock).isEqualTo(2L);
        assertThat(demo.servedCount()).isEqualTo(2L);
    }

    @Test
    void blockingFactoryRejectsANegativeDuration() {
        assertThatThrownBy(() -> PinningDemo.Blocking.sleeping(Duration.ofMillis(-1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guardedCounterIsRaceFreeUnderAForcedRace() throws InterruptedException {
        GuardedCounter counter = new GuardedCounter();
        int threads = 2_000;

        CountingRace.run(threads, counter::increment);

        // every increment is atomic, so the count is exactly right regardless of interleaving
        assertThat(counter.current()).isEqualTo(threads);
    }

    @Test
    void inconsistentlySyncedCounterCanLoseUpdatesNeverGainThem() throws InterruptedException {
        SharedState state = new SharedState(new InconsistentlySyncedCounter());
        int threads = 2_000;

        long observed = RaceHarness.raceIncrements(state, threads);

        // the unguarded read makes the race observable; a lost update can only lose, never gain, so the
        // contract that always holds is observed <= expected. The -Pquality build flags the defect
        // statically (SpotBugs IS2_INCONSISTENT_SYNC); the guarded counter above is the fix.
        assertThat(observed).isPositive().isLessThanOrEqualTo(threads);
    }

    @Test
    void structuredConceptReturnsEverySubtaskResult() {
        StructuredConceptDemo demo = new StructuredConceptDemo();
        List<Callable<Integer>> subtasks = List.of(() -> 1, () -> 2, () -> 3);

        List<Integer> results = demo.runAll(subtasks);

        assertThat(results).containsExactly(1, 2, 3);
    }

    @Test
    void structuredConceptFailsTheWholeUnitWhenASubtaskFails() {
        StructuredConceptDemo demo = new StructuredConceptDemo();
        List<Callable<Integer>> subtasks = List.of(
            () -> 1,
            () -> {
                throw new IllegalStateException("subtask blew up");
            });

        StructuredFailureException ex = catchThrowableOfType(
            StructuredFailureException.class, () -> demo.runAll(subtasks));

        assertThat(ex.code()).isEqualTo("subtask-failed");
        assertThat(ex.getCause()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void fanOutConfigDefendsItsTargetListFromCallerMutation() {
        java.util.List<String> input = new java.util.ArrayList<>(List.of("a", "b"));
        FanOutConfig config = new FanOutConfig(input, 2, Duration.ofMillis(100));

        input.clear();                       // mutate the caller's list after construction
        config.targets().clear();            // mutate the returned copy

        assertThat(config.targets()).containsExactly("a", "b");
    }

    @Test
    void fanOutConfigRejectsAnUnknownProfile() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> FanOutConfig.forProfile("staging"));
    }
}
