package org.acme.qualityops.gate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.junit.jupiter.api.Test;

class GateServiceTest {

    private final InMemoryDecisionRepository repository = new InMemoryDecisionRepository();

    private static final GatePolicy POLICY = new GatePolicy(80, 5, 0);

    private GateService serviceWith(ProjectQuality quality) {
        return new GateService(project -> quality, repository);
    }

    @Test
    void passesWhenEveryThresholdIsMet() {
        GateService service = serviceWith(new ProjectQuality(90, 3, 0));
        GateDecision decision = service.decide("checkout", "commit-1", POLICY);
        assertThat(decision.passed()).isTrue();
        assertThat(decision.reasons()).isEmpty();
    }

    @Test
    void failsWithOneReasonPerBreachedThreshold() {
        GateService service = serviceWith(new ProjectQuality(70, 9, 2));
        GateDecision decision = service.decide("checkout", "commit-2", POLICY);
        assertThat(decision.passed()).isFalse();
        assertThat(decision.reasons()).hasSize(3);
        assertThat(decision.reasons().getFirst()).contains("coverage 70%");
    }

    @Test
    void recordsTheDecisionIdempotentlyByProjectAndCommit() {
        GateService service = serviceWith(new ProjectQuality(90, 0, 0));
        GateDecision first = service.decide("checkout", "commit-3", POLICY);
        assertThat(first.passed()).isTrue();
        assertThat(repository.find("checkout", "commit-3")).isPresent();
    }

    @Test
    void aReEvaluationReturnsTheStoredDecisionUnchanged() {
        CountingMetrics metrics = new CountingMetrics(new ProjectQuality(90, 0, 0));
        GateService service = new GateService(metrics, repository);
        GateDecision first = service.decide("checkout", "commit-4", POLICY);
        // Same commit, but a stricter policy that would now FAIL — the recorded PASS must stand.
        GateDecision second = service.decide("checkout", "commit-4", new GatePolicy(100, 0, 0));
        assertThat(second.passed()).isTrue();
        assertThat(second).isEqualTo(first);
        assertThat(metrics.calls).isEqualTo(1);
    }

    @Test
    void rejectsAprojectWithNoMetricsAsUnprocessable() {
        GateService service = new GateService(project -> {
            throw ApiException.unprocessable("metrics-unavailable", "no metrics for " + project);
        }, repository);
        assertThatThrownBy(() -> service.decide("unseen", "commit-5", POLICY))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(422));
    }

    @Test
    void decidesOverHttp() {
        GateService service = serviceWith(new ProjectQuality(95, 1, 0));
        try (HttpApp app = GateApi.newApp(service, 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            ServiceClient.Reply reply = client.postJson(
                URI.create("http://localhost:" + app.port() + "/gate"),
                Map.of("project", "checkout", "commit", "commit-http",
                    "policy", Map.of("minCoverage", 80, "maxViolations", 5, "maxBugs", 0)));
            assertThat(reply.status()).isEqualTo(200);
            assertThat(reply.jsonObject()).containsEntry("decision", "PASS");
        }
    }

    private static final class CountingMetrics implements MetricsPort {
        private final ProjectQuality quality;
        private int calls;

        CountingMetrics(ProjectQuality quality) {
            this.quality = quality;
        }

        @Override
        public ProjectQuality qualityOf(String project) {
            calls++;
            return quality;
        }
    }
}
