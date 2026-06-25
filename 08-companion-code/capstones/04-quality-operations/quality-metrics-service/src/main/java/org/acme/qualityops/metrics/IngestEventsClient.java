package org.acme.qualityops.metrics;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.acme.platform.client.ServiceClient;

/**
 * The HTTP adapter that reads a project's events from quality-ingest-service (Part: service
 * composition). quality-metrics-service never reaches into the ingest service's storage — it asks
 * over plain HTTP, which is the boundary that keeps the two independent. The response is the ingest
 * service's envelope ({@code {project, count, events:[...]}}); each event in the list is mapped
 * through {@link IngestedEvent#fromBody} into this service's own, smaller view.
 */
public final class IngestEventsClient implements EventSourcePort {

    private final ServiceClient client;
    private final URI ingestBaseUri;

    public IngestEventsClient(ServiceClient client, URI ingestBaseUri) {
        this.client = client;
        this.ingestBaseUri = ingestBaseUri;
    }

    @Override
    public List<IngestedEvent> eventsFor(String project) {
        ServiceClient.Reply reply = client.getJson(ingestBaseUri.resolve("/events/" + project));
        if (!reply.isSuccess()) {
            throw new IllegalStateException("quality-ingest-service error: HTTP " + reply.status());
        }
        Object raw = reply.jsonObject().get("events");
        if (!(raw instanceof List<?> array)) {
            throw new IllegalStateException("ingest response is missing its 'events' array");
        }
        List<IngestedEvent> events = new ArrayList<>();
        for (Object element : array) {
            if (!(element instanceof Map<?, ?>)) {
                throw new IllegalStateException("each event must be a JSON object");
            }
            events.add(IngestedEvent.fromBody(asObject(element)));
        }
        return events;
    }

    @SuppressWarnings("unchecked") // guarded by the instanceof Map check immediately above
    private static Map<String, Object> asObject(Object element) {
        return (Map<String, Object>) element;
    }
}
