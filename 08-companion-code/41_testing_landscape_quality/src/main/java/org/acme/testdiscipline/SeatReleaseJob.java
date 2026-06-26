package org.acme.testdiscipline;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A small asynchronous task: it releases a held seat back to the pool on a background thread.
 *
 * <p>It exists so the chapter's async-wait flake has something concrete to wait on. The naive test
 * sleeps for a guessed duration and hopes the work finished; the deterministic test polls a condition
 * until it holds. This class exposes the condition ({@link #isReleased()}) so a test can poll it instead
 * of sleeping, which is the JDK form of the Awaitility pattern the chapter's matrix names (the library
 * itself is owned by a later chapter).
 */
public final class SeatReleaseJob {

    private final AtomicBoolean released = new AtomicBoolean(false);

    /**
     * Starts the release on the given executor and returns a future that completes when it is done.
     *
     * @param executor the executor to run the release on, never {@code null}
     * @return a future completing once the seat has been released, never {@code null}
     * @throws NullPointerException if {@code executor} is {@code null}
     */
    public CompletableFuture<Void> startOn(Executor executor) {
        Objects.requireNonNull(executor, "executor");
        return CompletableFuture.runAsync(() -> released.set(true), executor);
    }

    /**
     * Reports whether the seat has been released yet — the condition a deterministic test polls.
     *
     * @return {@code true} once the background release has completed
     */
    public boolean isReleased() {
        return released.get();
    }
}
