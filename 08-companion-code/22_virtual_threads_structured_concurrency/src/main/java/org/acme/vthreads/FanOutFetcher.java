package org.acme.vthreads;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Fetches every configured target concurrently, one virtual thread per target — the stable,
 * GA surface of JEP 444 (Java 21). Each fetch is an independent blocking call, which is the workload
 * virtual threads are built for: the blocking call parks a cheap virtual thread instead of tying up a
 * scarce platform thread, so the simple thread-per-task style scales.
 *
 * <p>The fan-out reads as straight-line code rather than a callback chain, which is the chapter's
 * quality argument. A back-pressure {@link Semaphore} caps in-flight fetches at the configured
 * {@code maxConcurrent}: virtual threads make launching tasks cheap, but a downstream pool is finite,
 * so the cap bounds concurrency rather than launching an unbounded number of forks.
 *
 * <p><strong>Failure path.</strong> A fetch that exceeds the per-call timeout is cancelled and recorded
 * as a {@link FetchOutcome#failure failure} with a stable reason, rather than blocking the fan-out
 * forever — the explicit, tested failure path the chapter's honest-limitations floor requires in code.
 *
 * <p><strong>Observability.</strong> {@link #completedCount()} and {@link #failedCount()} expose running
 * counters, and {@link #isReady()} is a readiness probe over the wired backend.
 */
public final class FanOutFetcher {

    private static final Logger LOG = System.getLogger(FanOutFetcher.class.getName());

    private final Backend backend;
    private final AtomicLong completed = new AtomicLong();
    private final AtomicLong failed = new AtomicLong();

    /** A blocking source for one target's payload; the seam an HTTP or datastore client slots behind. */
    @FunctionalInterface
    public interface Backend {
        /**
         * Fetches one target's payload, blocking until it is available.
         *
         * @param target the target to fetch, never {@code null}
         * @return the payload for that target, never {@code null}
         * @throws InterruptedException if the fetching thread is interrupted while blocked
         */
        String fetch(String target) throws InterruptedException;
    }

    /**
     * The outcome of one target's fetch: either a value or a stable failure reason.
     *
     * @param result the value fetched, or {@code null} when {@code reason} is set
     * @param reason a stable failure reason, or {@code null} when {@code result} is set
     */
    public record FetchOutcome(FetchResult result, String reason) {
        static FetchOutcome success(FetchResult result) {
            return new FetchOutcome(Objects.requireNonNull(result), null);
        }

        static FetchOutcome failure(String reason) {
            return new FetchOutcome(null, Objects.requireNonNull(reason));
        }

        /**
         * Reports whether this outcome carries a value.
         *
         * @return {@code true} when the fetch succeeded
         */
        public boolean succeeded() {
            return result != null;
        }
    }

    /**
     * Creates a fetcher over the given blocking backend.
     *
     * @param backend the blocking source of payloads, never {@code null}
     * @throws NullPointerException if {@code backend} is {@code null}
     */
    public FanOutFetcher(Backend backend) {
        this.backend = Objects.requireNonNull(backend, "backend");
    }

    /**
     * Fetches every configured target concurrently and returns the outcomes in target order.
     *
     * @param config the externalized fan-out configuration, never {@code null}
     * @return one outcome per configured target, in the configured order, never {@code null}
     * @throws NullPointerException if {@code config} is {@code null}
     */
    public List<FetchOutcome> fetchAll(FanOutConfig config) {
        Objects.requireNonNull(config, "config");
        List<String> targets = config.targets();
        Semaphore inFlight = new Semaphore(config.maxConcurrent());
        Duration timeout = config.perCallTimeout();
        List<FetchOutcome> outcomes = new ArrayList<>();
        // tag::vthread-fanout[]
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<FetchResult>> futures = new ArrayList<>();
            for (String target : targets) {            // one virtual thread per task — never pooled
                futures.add(executor.submit(() -> fetchOne(target, inFlight)));
            }
            for (Future<FetchResult> future : futures) {
                outcomes.add(join(future, timeout));   // collect; the try-block joins all on close
            }
        }
        // end::vthread-fanout[]
        return outcomes;
    }

    private FetchResult fetchOne(String target, Semaphore inFlight) throws InterruptedException {
        // The semaphore caps how many fetches run at once so the fan-out respects downstream
        // back-pressure; the per-call timeout is enforced where the result is joined (see join).
        inFlight.acquire();
        try {
            long start = System.nanoTime();
            String payload = backend.fetch(target);
            long millis = Duration.ofNanos(System.nanoTime() - start).toMillis();
            return new FetchResult(target, payload, millis);
        } finally {
            inFlight.release();
        }
    }

    private FetchOutcome join(Future<FetchResult> future, Duration timeout) {
        try {
            FetchResult result = future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            completed.incrementAndGet();
            return FetchOutcome.success(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
            failed.incrementAndGet();
            return FetchOutcome.failure("interrupted");
        } catch (ExecutionException e) {
            LOG.log(Level.WARNING, "fetch failed", e.getCause());
            failed.incrementAndGet();
            return FetchOutcome.failure("backend-error");
        } catch (TimeoutException e) {
            future.cancel(true);          // the explicit failure path: a slow fetch is bounded, not hung
            failed.incrementAndGet();
            return FetchOutcome.failure("timeout");
        }
    }

    /**
     * Health/observability surface: the running count of fetches that returned a value.
     *
     * @return the number of completed fetches since startup, never negative
     */
    public long completedCount() {
        return completed.get();
    }

    /**
     * Health/observability surface: the running count of fetches that failed or timed out.
     *
     * @return the number of failed fetches since startup, never negative
     */
    public long failedCount() {
        return failed.get();
    }

    /**
     * A readiness probe: the fetcher is ready when its backend port is wired. The backend is required at
     * construction (see the constructor's {@code requireNonNull}), so this seam is always ready once the
     * object exists; a production probe would additionally check the downstream connection (a ping or a
     * pooled-connection check), which is out of scope for this teaching module.
     *
     * @return {@code true} when the fetcher can serve a fan-out
     */
    public boolean isReady() {
        return backend != null;
    }
}
