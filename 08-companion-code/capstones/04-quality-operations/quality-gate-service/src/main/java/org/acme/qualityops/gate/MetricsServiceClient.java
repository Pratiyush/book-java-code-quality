package org.acme.qualityops.gate;

import java.net.URI;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.error.ApiException;

/**
 * The HTTP adapter that reads a project's aggregate by calling quality-metrics-service (Part: service
 * composition). quality-gate-service never recomputes the numbers itself — it asks the service that
 * owns the aggregation, which is the boundary that keeps the gate's job to policy and nothing else.
 *
 * <p>A project the metrics service has never seen surfaces there as a 404, which this adapter
 * translates into a 422 Unprocessable Entity for the gate caller: the request was well-formed, but
 * there is no quality data to gate on, so the gate cannot render a decision.
 */
public final class MetricsServiceClient implements MetricsPort {

    private final ServiceClient client;
    private final URI metricsBaseUri;

    public MetricsServiceClient(ServiceClient client, URI metricsBaseUri) {
        this.client = client;
        this.metricsBaseUri = metricsBaseUri;
    }

    @Override
    public ProjectQuality qualityOf(String project) {
        ServiceClient.Reply reply = client.getJson(metricsBaseUri.resolve("/metrics/" + project));
        if (reply.status() == 404) {
            throw ApiException.unprocessable("metrics-unavailable",
                "no quality metrics for project " + project);
        }
        if (!reply.isSuccess()) {
            throw new IllegalStateException("quality-metrics-service error: HTTP " + reply.status());
        }
        return ProjectQuality.fromBody(reply.jsonObject());
    }
}
