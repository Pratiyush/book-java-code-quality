package org.acme.testdiscipline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Determinism fix: an async result awaited by polling a condition, never by sleeping a guessed time.
 *
 * <p>The chapter's flakiness matrix names {@code Thread.sleep} as the async-wait root cause and "poll
 * and proceed the instant the condition holds" as the fix (Awaitility is the library form, owned by a
 * later chapter; this shows the JDK-only principle). A fixed sleep is flaky in both directions: too
 * short and it races the work, too long and it wastes time on every run. Polling a real condition under
 * a hard {@code assertTimeoutPreemptively} budget removes the guess.
 */
class AsyncWaitTest {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @AfterEach
    void shutdownExecutor() {
        executor.shutdownNow();
    }

    @Test
    @DisplayName("await the background release by polling the condition, bounded by a timeout")
    void awaitsByPollingNotSleeping() {
        SeatReleaseJob job = new SeatReleaseJob();
        job.startOn(executor);

        // tag::poll-not-sleep[]
        // Poll the real condition under a hard budget — proceed the instant it holds.
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> pollUntil(job::isReleased));
        assertThat(job.isReleased()).isTrue();
        // end::poll-not-sleep[]
    }

    /**
     * Polls a condition every few milliseconds until it holds, returning as soon as it does. The outer
     * {@code assertTimeoutPreemptively} caps the total wait, so a never-true condition fails fast rather
     * than hanging the suite.
     *
     * @param condition the condition to wait for, never {@code null}
     * @throws InterruptedException if the polling thread is interrupted while waiting
     */
    private static void pollUntil(BooleanSupplier condition) throws InterruptedException {
        while (!condition.getAsBoolean()) {
            TimeUnit.MILLISECONDS.sleep(5);
        }
    }
}
