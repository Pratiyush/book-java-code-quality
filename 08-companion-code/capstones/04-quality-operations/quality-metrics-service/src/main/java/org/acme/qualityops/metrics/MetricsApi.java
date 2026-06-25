package org.acme.qualityops.metrics;

import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;

/**
 * The HTTP surface of quality-metrics-service (Part: API design). It maps one read route onto
 * {@link MetricsService} and nothing more — no aggregation logic lives here, so the same service can
 * be driven by a different transport without change.
 *
 * <ul>
 *   <li>{@code GET /metrics/{project}}   → 200 the aggregate · 404 when the project has no events
 * </ul>
 */
public final class MetricsApi {

    private MetricsApi() {
    }

    /** Builds (but does not start) the configured app on the given port. */
    public static HttpApp newApp(MetricsService service, int port) {
        HttpApp app = new HttpApp("quality-metrics-service", port);
        app.get("/metrics/{project}", request ->
            Response.ok(service.metricsFor(request.pathParam("project")).toBody()));
        return app;
    }
}
