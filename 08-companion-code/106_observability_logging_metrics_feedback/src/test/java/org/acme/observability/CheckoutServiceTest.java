package org.acme.observability;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the observability wiring: it drives {@link CheckoutService} through the whole
 * stack — externalized config, the metrics registry, the correlation context, the structured logger,
 * and the health gauge — and asserts the mechanism the chapter describes, end to end.
 */
class CheckoutServiceTest {

    private final ObservabilityConfig config = ObservabilityConfig.load("prod");
    private final MetricsRegistry metrics = new MetricsRegistry();
    private final CheckoutService service = new CheckoutService(config, metrics);

    @Test
    @DisplayName("a valid checkout records traffic and latency and returns Success")
    void recordsTrafficAndLatencyOnSuccess() {
        CheckoutOutcome outcome = service.checkout("ord_1", 1_500L);

        assertThat(outcome).isInstanceOf(CheckoutOutcome.Success.class);
        assertThat(metrics.count(CheckoutService.REQUESTS)).isEqualTo(1L);
        assertThat(metrics.count(CheckoutService.ERRORS)).isZero();
        assertThat(metrics.meanMillis(CheckoutService.LATENCY)).isGreaterThanOrEqualTo(0.0);
    }

    @Test
    @DisplayName("regression from a production incident: a zero-amount order must be rejected")
    void zeroAmountOrderIsRejected() {
        // The escape that reached production: a zero-amount order checked out for free. This is the
        // failing test written for that incident; the guard in CheckoutService is the fix it protects.
        CheckoutOutcome outcome = service.checkout("ord_2", 0L);

        assertThat(outcome).isInstanceOf(CheckoutOutcome.Failure.class);
        assertThat(((CheckoutOutcome.Failure) outcome).reasonCode()).isEqualTo("invalid-amount");
        assertThat(metrics.count(CheckoutService.ERRORS)).isEqualTo(1L);
    }

    @Test
    @DisplayName("the health gauge stays healthy within the SLO budget and trips when it is burnt")
    void healthGaugeReflectsErrorBudget() {
        HealthGauge gauge =
            new HealthGauge(metrics, config.sloErrorBudget(), CheckoutService.REQUESTS, CheckoutService.ERRORS);

        assertThat(gauge.isHealthy()).isTrue(); // no traffic yet
        for (int i = 0; i < 20; i++) {
            service.checkout("ord_ok_" + i, 100L);
        }
        assertThat(gauge.isHealthy()).isTrue(); // all succeeded, error rate 0

        service.checkout("ord_bad", -1L); // one failure in 21 ~ 4.8%, under the 5% prod budget
        assertThat(gauge.isHealthy()).isTrue();
        for (int i = 0; i < 5; i++) {
            service.checkout("ord_bad_" + i, -1L); // push the error rate over budget
        }
        assertThat(gauge.isHealthy()).isFalse();
    }

    @Test
    @DisplayName("the structured logger redacts secret fields before a line is written")
    void redactsSecrets() {
        Map<String, Object> safe =
            StructuredLogger.redact(Map.of("user", "alice", "password", "hunter2", "token", "abc123"));

        assertThat(safe).containsEntry("user", "alice");
        assertThat(safe).containsEntry("password", "***");
        assertThat(safe).containsEntry("token", "***");
    }

    @Test
    @DisplayName("the correlation id is bound during the work and cleared afterwards")
    void correlationIdIsBoundThenCleared() {
        AtomicReference<String> seen = new AtomicReference<>();

        CorrelationContext.withTraceId("trace-xyz", () -> seen.set(CorrelationContext.currentTraceId()));

        assertThat(seen.get()).isEqualTo("trace-xyz");
        assertThat(CorrelationContext.currentTraceId()).isEqualTo("-"); // cleared — no leak
    }

    @Test
    @DisplayName("an out-of-range error budget is rejected at construction")
    void invalidErrorBudgetIsRejected() {
        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, () -> new ObservabilityConfig(java.lang.System.Logger.Level.INFO, 1.5));
    }
}
