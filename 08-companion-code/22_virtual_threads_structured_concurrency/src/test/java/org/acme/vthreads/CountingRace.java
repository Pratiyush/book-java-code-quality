package org.acme.vthreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A tiny test-only racer: runs an action on many virtual threads released simultaneously by a latch, so
 * a test can force maximal overlap. The same latch technique {@link RaceHarness} uses, generalized to an
 * arbitrary {@link Runnable} so both the guarded and the inconsistently-synchronized counters can be
 * driven the same way.
 */
final class CountingRace {

    private CountingRace() {
    }

    static void run(int threads, Runnable action) throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> forks = new ArrayList<>();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threads; i++) {
                forks.add(executor.submit(() -> {
                    awaitQuietly(start);
                    action.run();
                }));
            }
            start.countDown();
        }
        for (Future<?> fork : forks) {
            joinQuietly(fork);
        }
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
        } catch (ExecutionException e) {
            throw new IllegalStateException("racing action failed", e.getCause());
        }
    }
}
