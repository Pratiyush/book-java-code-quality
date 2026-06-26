package org.acme.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the measurement program: it drives the whole stack the chapter describes —
 * externalized config, DORA's four keys, the counter-metric pairing, the baseline-and-ratchet rollout
 * policy, and the dashboard spec — and asserts each mechanism end to end, including the failure paths
 * (a ratchet blocking a regression, and the dashboard refusing vanity and individual ranking).
 */
class MetricsRolloutTest {

    private static final Instant T0 = Instant.parse("2026-06-20T00:00:00Z");

    @Test
    @DisplayName("DORA's four keys are computed from deployment records, throughput beside stability")
    void computesDoraFourKeys() {
        DoraMetrics dora = new DoraMetrics(
                List.of(
                        DeploymentRecord.successful(T0, Duration.ofHours(2)),
                        DeploymentRecord.successful(T0.plusSeconds(3600), Duration.ofHours(4)),
                        DeploymentRecord.failed(T0.plusSeconds(7200), Duration.ofHours(6), Duration.ofMinutes(30)),
                        DeploymentRecord.successful(T0.plusSeconds(10_800), Duration.ofHours(8))),
                Duration.ofDays(1));

        assertThat(dora.deploymentsPerDay()).isEqualTo(4.0); // four deployments across a one-day window
        assertThat(dora.meanLeadTime()).isEqualTo(Duration.ofHours(5)); // (2+4+6+8)/4 hours
        assertThat(dora.changeFailureRate()).isEqualTo(0.25); // one failure in four
        assertThat(dora.meanRecoveryTime()).isEqualTo(Duration.ofMinutes(30)); // over the single failure
        assertThat(dora.correlatedReport()).hasSize(4); // throughput AND stability, never one alone
    }

    @Test
    @DisplayName("a counter-metric reports the primary and its counter together, never alone")
    void counterMetricPairsBothValues() {
        CounterMetric pair = new CounterMetric("deployment_frequency", "change_failure_rate");

        CounterMetric.Reading reading = pair.report(4.0, 0.05);

        assertThat(reading.primaryValue()).isEqualTo(4.0);
        assertThat(reading.counter()).isEqualTo("change_failure_rate");
        assertThat(reading.counterValue()).isEqualTo(0.05); // speed is never reported without safety
    }

    @Test
    @DisplayName("a metric cannot be its own counter-metric")
    void counterMetricRejectsSelfPairing() {
        assertThatThrownBy(() -> new CounterMetric("velocity", "velocity"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("own counter-metric");
    }

    @Test
    @DisplayName("the baseline gates the future, not the past: legacy is accepted, new findings block")
    void baselineAcceptsPastGatesFuture() {
        RolloutPolicy policy = new RolloutPolicy(40_000); // the legacy wall, accepted as the baseline

        assertThat(policy.gateNewFindings(40_000).allowed()).isTrue(); // no new findings — accepted
        assertThat(policy.gateNewFindings(39_990).allowed()).isTrue(); // legacy paid down — accepted

        RolloutDecision regressed = policy.gateNewFindings(40_003);
        assertThat(regressed.allowed()).isFalse(); // three NEW findings above the baseline — blocked
        assertThat(((RolloutDecision.Blocked) regressed).reason()).contains("3 new finding");
    }

    @Test
    @DisplayName("the ratchet allows improvement and holds, and blocks a regression (the failure path)")
    void ratchetOnlyAllowsImprovement() {
        RolloutPolicy policy = new RolloutPolicy(0);

        // coverage may only rise or hold
        assertThat(policy.ratchet("coverage", 0.80, 0.82, RolloutPolicy.RatchetDirection.HIGHER_IS_BETTER)
                .allowed()).isTrue();
        RolloutDecision coverageDropped =
                policy.ratchet("coverage", 0.80, 0.79, RolloutPolicy.RatchetDirection.HIGHER_IS_BETTER);
        assertThat(coverageDropped.allowed()).isFalse();
        assertThat(((RolloutDecision.Blocked) coverageDropped).reason()).contains("regressed");

        // a new-issue count may only fall or hold
        assertThat(policy.ratchet("new_issues", 5, 3, RolloutPolicy.RatchetDirection.LOWER_IS_BETTER)
                .allowed()).isTrue();
        assertThat(policy.ratchet("new_issues", 5, 7, RolloutPolicy.RatchetDirection.LOWER_IS_BETTER)
                .allowed()).isFalse();
    }

    @Test
    @DisplayName("a baseline keeps its debt visible so it is paid down, not turned into amnesty")
    void baselineDebtStaysVisible() {
        RolloutPolicy policy = new RolloutPolicy(40_000);

        assertThat(policy.remainingBaselineDebt()).isEqualTo(40_000); // recorded, not hidden — pair with paydown
    }

    @Test
    @DisplayName("the dashboard accepts a paired outcome trend on a new-code lens")
    void dashboardAcceptsGoodTile() {
        DashboardSpec dashboard = new DashboardSpec()
                .addTile(new DashboardSpec.Tile(
                        "deployment frequency",
                        MetricKind.OUTCOME,
                        new CounterMetric("deployment_frequency", "change_failure_rate"),
                        true,
                        false))
                .addTile(new DashboardSpec.Tile(
                        "new-code coverage",
                        MetricKind.QUALITY_TREND,
                        new CounterMetric("coverage", "mutation_score"),
                        true,
                        false));

        assertThat(dashboard.tileCount()).isEqualTo(2);
        assertThat(dashboard.snapshot()).allMatch(DashboardSpec.Tile::newCodeLens);
    }

    @Test
    @DisplayName("the dashboard refuses a vanity tile (the failure path)")
    void dashboardRefusesVanityTile() {
        DashboardSpec dashboard = new DashboardSpec();

        assertThatThrownBy(() -> dashboard.addTile(new DashboardSpec.Tile(
                "lines of code",
                MetricKind.VANITY,
                new CounterMetric("lines_of_code", "review_findings"),
                false,
                false)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vanity");
    }

    @Test
    @DisplayName("the dashboard refuses an individual leaderboard (metrics measure the system, not people)")
    void dashboardRefusesIndividualLeaderboard() {
        DashboardSpec dashboard = new DashboardSpec();

        assertThatThrownBy(() -> dashboard.addTile(new DashboardSpec.Tile(
                "commits per engineer",
                MetricKind.OUTCOME,
                new CounterMetric("commits", "defect_escape_rate"),
                true,
                true))) // individual-scoped — refused regardless of kind
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("leaderboard");
    }

    @Test
    @DisplayName("externalized config differs by profile: dev warns, prod enforces the ratchet")
    void configProfilesDifferDevVersusProd() {
        MetricsConfig dev = MetricsConfig.load("dev");
        MetricsConfig prod = MetricsConfig.load("prod");

        assertThat(dev.ratchetEnforced()).isFalse(); // warn-then-block: dev introduces the gate
        assertThat(prod.ratchetEnforced()).isTrue(); // prod blocks once trust is built
        assertThat(prod.changeFailureRateAlertThreshold())
                .isLessThan(dev.changeFailureRateAlertThreshold()); // tighter in prod
    }

    @Test
    @DisplayName("an out-of-range alert threshold is rejected at construction (fail fast)")
    void invalidThresholdIsRejected() {
        assertThatThrownBy(() -> new MetricsConfig(true, 1.5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[0, 1]");
    }
}
