package org.acme.qualityops.ingest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Request;
import org.acme.platform.http.Response;
import org.acme.platform.json.Json;

/**
 * The HTTP surface of quality-ingest-service (Part: API design). It parses the event, delegates to
 * {@link IngestService}, and maps idempotency onto status codes — 201 when a new event is stored, 200
 * when the id was already known so a retry is safe to repeat.
 *
 * <ul>
 *   <li>{@code POST /events}             → 201 a new event · 200 an already-ingested one
 *   <li>{@code GET  /events/{project}}   → 200 the project's events (a self-describing envelope)
 * </ul>
 *
 * <p>The list endpoint returns an object envelope ({@code {project, count, events:[...]}}) rather than
 * a bare array — the shape every cross-service read in these capstones uses, so the consumer parses
 * one object the same way it parses any other response.
 */
public final class IngestApi {

    private IngestApi() {
    }

    /** Builds (but does not start) the configured app on the given port. */
    public static HttpApp newApp(IngestService service, int port) {
        HttpApp app = new HttpApp("quality-ingest-service", port);
        app.post("/events", request -> ingest(service, request));
        app.get("/events/{project}", request -> listEvents(service, request));
        return app;
    }

    private static Response listEvents(IngestService service, Request request) {
        String project = request.pathParam("project");
        List<Map<String, Object>> events =
            service.eventsFor(project).stream().map(QualityEvent::toBody).toList();
        Map<String, Object> envelope = new LinkedHashMap<>();
        envelope.put("project", project);
        envelope.put("count", events.size());
        envelope.put("events", events);
        return Response.ok(envelope);
    }

    private static Response ingest(IngestService service, Request request) {
        QualityEvent event = parseEvent(request);
        IngestService.IngestResult result = service.ingest(event);
        int status = result.created() ? 201 : 200;
        return Response.json(status, result.event().toBody());
    }

    private static QualityEvent parseEvent(Request request) {
        Map<String, Object> body = request.jsonBody();
        Object rawMetrics = body.get("metrics");
        if (!(rawMetrics instanceof Map<?, ?> metrics)) {
            throw ApiException.badRequest("metrics-missing", "'metrics' must be an object");
        }
        return new QualityEvent(
            Json.requireString(body, "id"),
            Json.requireString(body, "project"),
            Json.requireString(body, "commit"),
            Json.requireString(body, "tool"),
            Json.requireLong(body, "timestamp"),
            parseMetrics(metrics));
    }

    private static QualityMetrics parseMetrics(Map<?, ?> metrics) {
        return new QualityMetrics(
            intField(metrics, "coveragePercent"),
            intField(metrics, "violations"),
            intField(metrics, "bugs"));
    }

    private static int intField(Map<?, ?> object, String field) {
        if (object.get(field) instanceof Number n) {
            return Math.toIntExact(n.longValue());
        }
        throw ApiException.badRequest("field-missing", "missing or non-numeric metric field: " + field);
    }
}
