package org.acme.observability;

/**
 * The feedback signal: a health gauge that reads the live error rate off the metrics registry and
 * compares it to the SLO error budget from {@link ObservabilityConfig}. It is what a readiness probe
 * or an alert rule would evaluate — and it alerts on budget <em>burn</em>, not on every blip, so a
 * single failure on an otherwise-healthy service does not wake anyone (the alert-fatigue twin of
 * gate fatigue, Chapter 19).
 *
 * <p>This closes the loop the chapter draws: the error counter that production increments becomes a
 * signal the team can act on, and the action — when a real failure is found — is to write the test
 * that prevents its recurrence, not merely to watch the gauge.
 */
public final class HealthGauge {

    private final MetricsRegistry metrics;
    private final double errorBudget;
    private final String requestsMeter;
    private final String errorsMeter;

    /**
     * Creates a gauge over two counters in {@code metrics}.
     *
     * @param metrics       the registry the request and error counters live in, never {@code null}
     * @param errorBudget   the SLO threshold (fraction of failing requests) to alert on burn against
     * @param requestsMeter the name of the total-requests counter
     * @param errorsMeter   the name of the failed-requests counter
     */
    public HealthGauge(MetricsRegistry metrics, double errorBudget, String requestsMeter, String errorsMeter) {
        this.metrics = metrics;
        this.errorBudget = errorBudget;
        this.requestsMeter = requestsMeter;
        this.errorsMeter = errorsMeter;
    }

    /** The error rate over all requests, in {@code [0, 1]}; {@code 0} before any request. */
    public double errorRate() {
        long requests = metrics.count(requestsMeter);
        long errors = metrics.count(errorsMeter);
        return requests == 0 ? 0.0 : (double) errors / requests;
    }

    // tag::health-gauge[]
    /** {@code true} while the error rate stays within the SLO budget — the readiness signal. */
    public boolean isHealthy() {
        return errorRate() <= errorBudget; // alert on budget burn, not on every blip
    }
    // end::health-gauge[]
}
