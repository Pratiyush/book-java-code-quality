package org.acme.qualityops.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.junit.jupiter.api.Test;

class MetricsServiceTest {

    @Test
    void aggregatesLatestCoverageAndSumsViolationsAndBugs() {
        EventSourcePort source = project -> List.of(
            new IngestedEvent(100L, 70, 1, 0),
            new IngestedEvent(300L, 90, 2, 1),   // latest by timestamp
            new IngestedEvent(200L, 80, 0, 0));
        MetricsService service = new MetricsService(source);

        ProjectMetrics metrics = service.metricsFor("checkout");

        assertThat(metrics.eventCount()).isEqualTo(3);
        assertThat(metrics.latestCoverage()).isEqualTo(90);
        assertThat(metrics.totalViolations()).isEqualTo(3);
        assertThat(metrics.totalBugs()).isEqualTo(1);
        // 90 - 3*2 - 1*5 = 79
        assertThat(metrics.qualityScore()).isEqualTo(79);
    }

    @Test
    void clampsTheCompositeScoreToZero() {
        EventSourcePort source = project -> List.of(new IngestedEvent(1L, 10, 50, 50));
        MetricsService service = new MetricsService(source);
        assertThat(service.metricsFor("noisy").qualityScore()).isZero();
    }

    @Test
    void rejectsAprojectWithNoEventsAsNotFound() {
        MetricsService service = new MetricsService(project -> List.of());
        assertThatThrownBy(() -> service.metricsFor("unseen"))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(404));
    }

    @Test
    void servesAnAggregateOverHttp() {
        EventSourcePort source = project -> List.of(new IngestedEvent(1L, 85, 4, 0));
        try (HttpApp app = MetricsApi.newApp(new MetricsService(source), 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            ServiceClient.Reply reply =
                client.getJson(URI.create("http://localhost:" + app.port() + "/metrics/checkout"));
            assertThat(reply.status()).isEqualTo(200);
            assertThat(reply.jsonObject())
                .containsEntry("latestCoverage", 85L)
                .containsEntry("totalViolations", 4L)
                // 85 - 4*2 - 0 = 77
                .containsEntry("qualityScore", 77L);
        }
    }

    @Test
    void returns404OverHttpForAprojectWithNoEvents() {
        try (HttpApp app = MetricsApi.newApp(new MetricsService(project -> List.of()), 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            ServiceClient.Reply reply =
                client.getJson(URI.create("http://localhost:" + app.port() + "/metrics/nope"));
            assertThat(reply.status()).isEqualTo(404);
        }
    }

    @Test
    void readsTheIngestEnvelopeShapeThroughTheRealClient() {
        try (HttpApp ingestStub = stubIngest()) {
            ServiceClient client = ServiceClient.withDefaults();
            IngestEventsClient source = new IngestEventsClient(
                client, URI.create("http://localhost:" + ingestStub.port()));
            MetricsService service = new MetricsService(source);

            ProjectMetrics metrics = service.metricsFor("checkout");
            assertThat(metrics.eventCount()).isEqualTo(1);
            assertThat(metrics.latestCoverage()).isEqualTo(88);
        }
    }

    private static HttpApp stubIngest() {
        HttpApp app = new HttpApp("stub-ingest", 0);
        app.get("/events/{project}", request -> org.acme.platform.http.Response.ok(Map.of(
            "project", request.pathParam("project"),
            "count", 1,
            "events", List.of(Map.of(
                "id", "evt-1", "project", request.pathParam("project"), "commit", "abc",
                "tool", "jacoco", "occurredAt", 5L,
                "metrics", Map.of("coveragePercent", 88, "violations", 1, "bugs", 0))))));
        return app.start();
    }
}
