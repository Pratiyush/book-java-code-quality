package org.acme.observability;

import java.lang.System.Logger.Level;
import java.util.Map;
import java.util.Objects;

/**
 * A checkout service instrumented as the chapter prescribes: every call is timed and counted, logged
 * with its correlation id, and its failures are both counted and returned as a typed outcome. It
 * wires the three pillars together — a {@link StructuredLogger} for events, a {@link MetricsRegistry}
 * for the golden signals, and (through the registry) the {@link HealthGauge} feedback signal — so a
 * latency or error trend leads back to the request that caused it.
 *
 * <p>The validation here also records the feedback loop the chapter closes. An early version let a
 * zero-amount order through to payment; the escape was found in production, a failing test was written
 * for it (see the test suite), and the guard below is the fix that test now protects. The highest-
 * signal tests come from real incidents, and this is one made concrete.
 */
public final class CheckoutService {

    /** Stable meter names — bounded by construction, never keyed by a per-request value. */
    public static final String REQUESTS = "checkout.requests";
    public static final String ERRORS = "checkout.errors";
    public static final String LATENCY = "checkout.latency";

    private final StructuredLogger log;
    private final MetricsRegistry metrics;

    /**
     * Creates a service that records into the given registry and logs at the configured level.
     *
     * @param config  the externalized observability configuration, never {@code null}
     * @param metrics the registry the golden-signal meters live in, never {@code null}
     */
    public CheckoutService(ObservabilityConfig config, MetricsRegistry metrics) {
        Objects.requireNonNull(config, "config");
        this.metrics = Objects.requireNonNull(metrics, "metrics");
        this.log = new StructuredLogger(CheckoutService.class, config.logLevel());
    }

    /** Checks out one order, recording traffic, latency, and errors; returns a typed outcome. */
    // tag::instrumented-method[]
    public CheckoutOutcome checkout(String orderRef, long amountMinorUnits) {
        long start = System.nanoTime();
        metrics.increment(REQUESTS);
        try {
            return process(orderRef, amountMinorUnits);
        } finally {
            metrics.recordNanos(LATENCY, System.nanoTime() - start);
        }
    }
    // end::instrumented-method[]

    private CheckoutOutcome process(String orderRef, long amountMinorUnits) {
        Objects.requireNonNull(orderRef, "orderRef");
        // The guard the production incident taught us to add: a non-positive amount is a failure,
        // not a free checkout. Without it, the escape reached payment.
        if (amountMinorUnits <= 0) {
            return reject(orderRef, "invalid-amount", "amount must be positive: " + amountMinorUnits);
        }
        log.log(Level.INFO, "checkout.ok", Map.of("order_ref", orderRef, "amount", amountMinorUnits));
        return new CheckoutOutcome.Success(orderRef, amountMinorUnits);
    }

    private CheckoutOutcome reject(String orderRef, String reasonCode, String detail) {
        metrics.increment(ERRORS);
        // ERROR with the trace id and a reason — what the error tracker would capture in production.
        log.log(Level.ERROR, "checkout.rejected",
            Map.of("order_ref", orderRef, "reason", reasonCode, "detail", detail));
        return new CheckoutOutcome.Failure(reasonCode, detail);
    }
}
