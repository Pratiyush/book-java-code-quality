package org.acme.qualityops.gate;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;
import org.junit.jupiter.api.Test;

/**
 * Exercises quality-gate-service's real outbound adapter ({@link MetricsServiceClient}) and decision
 * logic over real HTTP, against an ingest tier and a metrics tier stood up as their own
 * {@link HttpApp} servers on ephemeral ports — three live services in one JVM, with events flowing
 * ingest → metrics → gate across real seams. The two upstream tiers are built from the shared
 * platform rather than from the other service modules (mirroring the other capstones' flow tests), so
 * gate-service stays independent of them while the JSON contract on each seam is still tested for real.
 *
 * <p>The ingest stub stores posted events and serves them in the {@code {project, count, events}}
 * envelope; the metrics stub reads that envelope over HTTP and performs the real aggregation (latest
 * coverage, summed violations and bugs, composite score) — the same contract the production services
 * implement.
 */
class QualityGateFlowIntegrationTest {

    @Test
    void ingestsThenGatesAcommitToApass() {
        try (HttpApp ingest = stubIngest();
             HttpApp metrics = stubMetrics(ingest);
             HttpApp gate = gateApp(metrics)) {

            ServiceClient client = ServiceClient.withDefaults();
            URI ingestBase = URI.create("http://localhost:" + ingest.port());
            URI gateBase = URI.create("http://localhost:" + gate.port());

            // A clean run: high coverage, no violations or bugs.
            ingestEvent(client, ingestBase, "evt-pass", "checkout", 95, 0, 0);

            ServiceClient.Reply decision = client.postJson(gateBase.resolve("/gate"), Map.of(
                "project", "checkout", "commit", "commit-pass",
                "policy", Map.of("minCoverage", 80, "maxViolations", 5, "maxBugs", 0)));
            assertThat(decision.status()).isEqualTo(200);
            assertThat(decision.jsonObject()).containsEntry("decision", "PASS");
        }
    }

    @Test
    void ingestsThenGatesAcommitToAfailWithReasons() {
        try (HttpApp ingest = stubIngest();
             HttpApp metrics = stubMetrics(ingest);
             HttpApp gate = gateApp(metrics)) {

            ServiceClient client = ServiceClient.withDefaults();
            URI ingestBase = URI.create("http://localhost:" + ingest.port());
            URI gateBase = URI.create("http://localhost:" + gate.port());

            // A run that breaches every threshold: low coverage, too many violations and bugs.
            ingestEvent(client, ingestBase, "evt-fail", "ledger", 55, 12, 3);

            ServiceClient.Reply decision = client.postJson(gateBase.resolve("/gate"), Map.of(
                "project", "ledger", "commit", "commit-fail",
                "policy", Map.of("minCoverage", 80, "maxViolations", 5, "maxBugs", 0)));
            assertThat(decision.status()).isEqualTo(200);
            assertThat(decision.jsonObject()).containsEntry("decision", "FAIL");
            assertThat(decision.body()).contains("coverage 55%");
        }
    }

    private static void ingestEvent(ServiceClient client, URI ingestBase, String id, String project,
                                    int coverage, int violations, int bugs) {
        ServiceClient.Reply reply = client.postJson(ingestBase.resolve("/events"), Map.of(
            "id", id, "project", project, "commit", "abc123", "tool", "jacoco", "timestamp", 1L,
            "metrics", Map.of("coveragePercent", coverage, "violations", violations, "bugs", bugs)));
        assertThat(reply.status()).isEqualTo(201);
    }

    /** A minimal ingest tier: idempotent by id, serving the project envelope contract. */
    private static HttpApp stubIngest() {
        ConcurrentHashMap<String, CopyOnWriteArrayList<Map<String, Object>>> byProject =
            new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Boolean> seenIds = new ConcurrentHashMap<>();
        HttpApp app = new HttpApp("stub-ingest", 0);
        app.post("/events", request -> {
            Map<String, Object> body = request.jsonBody();
            String id = String.valueOf(body.get("id"));
            String project = String.valueOf(body.get("project"));
            boolean created = seenIds.putIfAbsent(id, Boolean.TRUE) == null;
            if (created) {
                // Store the canonical event projection — the real ingest service renames the inbound
                // 'timestamp' to 'occurredAt' on its way out, so the served contract matches.
                Map<String, Object> stored = new LinkedHashMap<>(body);
                stored.put("occurredAt", body.get("timestamp"));
                stored.remove("timestamp");
                byProject.computeIfAbsent(project, key -> new CopyOnWriteArrayList<>()).add(stored);
            }
            return Response.json(created ? 201 : 200, body);
        });
        app.get("/events/{project}", request -> {
            String project = request.pathParam("project");
            List<Map<String, Object>> events =
                new ArrayList<>(byProject.getOrDefault(project, new CopyOnWriteArrayList<>()));
            return Response.ok(Map.of("project", project, "count", events.size(), "events", events));
        });
        return app.start();
    }

    /** A minimal metrics tier: reads the ingest envelope over HTTP and aggregates it for real. */
    private static HttpApp stubMetrics(HttpApp ingest) {
        ServiceClient client = ServiceClient.withDefaults();
        URI ingestBase = URI.create("http://localhost:" + ingest.port());
        HttpApp app = new HttpApp("stub-metrics", 0);
        app.get("/metrics/{project}", request -> {
            String project = request.pathParam("project");
            ServiceClient.Reply reply = client.getJson(ingestBase.resolve("/events/" + project));
            List<?> events = (List<?>) reply.jsonObject().get("events");
            if (events.isEmpty()) {
                return Response.json(404, Map.of("detail", "no metrics for " + project));
            }
            int latestCoverage = 0;
            long latestAt = Long.MIN_VALUE;
            int totalViolations = 0;
            int totalBugs = 0;
            for (Object element : events) {
                Map<?, ?> metrics = (Map<?, ?>) ((Map<?, ?>) element).get("metrics");
                long occurredAt = ((Number) ((Map<?, ?>) element).get("occurredAt")).longValue();
                totalViolations += ((Number) metrics.get("violations")).intValue();
                totalBugs += ((Number) metrics.get("bugs")).intValue();
                if (occurredAt >= latestAt) {
                    latestAt = occurredAt;
                    latestCoverage = ((Number) metrics.get("coveragePercent")).intValue();
                }
            }
            return Response.ok(Map.of(
                "project", project, "eventCount", events.size(),
                "latestCoverage", latestCoverage,
                "totalViolations", totalViolations, "totalBugs", totalBugs,
                "qualityScore", Math.max(0, latestCoverage - totalViolations * 2 - totalBugs * 5)));
        });
        return app.start();
    }

    private static HttpApp gateApp(HttpApp metrics) {
        ServiceClient client = ServiceClient.withDefaults();
        GateService service = new GateService(
            new MetricsServiceClient(client, URI.create("http://localhost:" + metrics.port())),
            new InMemoryDecisionRepository());
        return GateApi.newApp(service, 0).start();
    }
}
