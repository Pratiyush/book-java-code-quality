package org.acme.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Runs many increments across a worker pool and tallies them on a thread-safe counter — the chapter's
 * edges wired into one runnable service.
 *
 * <p>The pool is sized from {@link WorkCoordinatorConfig} (externalized config), the shared tally is
 * an {@link AtomicCounter} (so concurrent workers lose no increment), and the executor's documented
 * happens-before edges carry each task's writes back to the caller after the futures complete. The
 * service exposes a small observability surface — {@link #isReady()} as a readiness probe and
 * {@link #processedCount()} as a metric — and a real failure path: {@link #shutdown()} drains
 * in-flight work for the configured grace period, then falls back to {@code shutdownNow()} if the
 * pool will not stop, so a non-daemon pool can never keep the JVM alive (Error Prone
 * {@code FutureReturnValueIgnored} territory, made an explicit lifecycle).
 */
public final class WorkCoordinator {

    private final WorkCoordinatorConfig config;
    private final ExecutorService executor;
    private final AtomicCounter processed = new AtomicCounter();
    private final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Creates a coordinator with a fixed pool sized from the given configuration.
     *
     * @param config the externalized configuration, never {@code null}
     */
    public WorkCoordinator(WorkCoordinatorConfig config) {
        this.config = config;
        this.executor = Executors.newFixedThreadPool(config.maxConcurrency());
    }

    /**
     * Submits {@code taskCount} unit-of-work tasks, each incrementing the shared tally once, and
     * waits for all of them to finish.
     *
     * @param taskCount the number of tasks to run, never negative
     * @return the number of tasks that completed
     * @throws IllegalStateException if the coordinator has been shut down
     * @throws IllegalArgumentException if {@code taskCount} is negative
     */
    public long runBatch(int taskCount) {
        if (!running.get()) {
            throw new IllegalStateException("coordinator is shut down");
        }
        if (taskCount < 0) {
            throw new IllegalArgumentException("taskCount must not be negative: " + taskCount);
        }
        List<Future<?>> futures = new ArrayList<>(taskCount);
        for (int i = 0; i < taskCount; i++) {
            futures.add(executor.submit(processed::increment));
        }
        for (Future<?> future : futures) {
            join(future);
        }
        return processed.count();
    }

    private static void join(Future<?> future) {
        try {
            future.get(); // the executor edge makes the task's increment visible here
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while awaiting a task", e);
        } catch (java.util.concurrent.ExecutionException e) {
            throw new IllegalStateException("a worker task failed", e.getCause());
        }
    }

    /**
     * Reports whether the coordinator is accepting work — a readiness probe.
     *
     * @return {@code true} until {@link #shutdown()} has run
     */
    public boolean isReady() {
        return running.get() && !executor.isShutdown();
    }

    /**
     * Returns the running tally of processed tasks — a metric surface.
     *
     * @return the number of increments tallied across all batches
     */
    public long processedCount() {
        return processed.count();
    }

    /**
     * Stops the coordinator on the orderly path: refuse new work, wait the configured grace period for
     * in-flight tasks to drain, then force termination if the pool has not stopped.
     *
     * @return {@code true} if the pool drained within the grace period, {@code false} if it had to be
     *     forced via {@code shutdownNow()}
     */
    public boolean shutdown() {
        running.set(false);
        executor.shutdown(); // stop accepting new tasks; let in-flight ones finish
        try {
            long grace = config.shutdownGrace().toMillis();
            if (executor.awaitTermination(grace, TimeUnit.MILLISECONDS)) {
                return true;
            }
            executor.shutdownNow(); // grace expired — interrupt the stragglers
            return false;
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
