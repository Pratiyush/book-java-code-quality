package org.acme.qualityops.ingest;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.acme.qualityops.ingest.event.QualityEventIngested;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.event.EventBus;
import org.acme.platform.http.HttpApp;
import org.junit.jupiter.api.Test;

class IngestServiceTest {

    private final InMemoryEventRepository repository = new InMemoryEventRepository();
    private final EventBus events = new EventBus();
    private final IngestService service = new IngestService(repository, events, () -> 1234L);

    private static QualityEvent event(String id, String project) {
        return new QualityEvent(id, project, "abc123", "jacoco", 1L, new QualityMetrics(90, 3, 1));
    }

    @Test
    void storesAndReturnsANewEvent() {
        IngestService.IngestResult result = service.ingest(event("evt-1", "checkout"));
        assertThat(result.created()).isTrue();
        assertThat(service.eventsFor("checkout")).hasSize(1);
    }

    @Test
    void reIngestingTheSameIdIsAnIdempotentNoOp() {
        service.ingest(event("evt-1", "checkout"));
        IngestService.IngestResult second = service.ingest(event("evt-1", "checkout"));
        assertThat(second.created()).isFalse();
        assertThat(service.eventsFor("checkout")).hasSize(1);
    }

    @Test
    void publishesOnlyOnAGenuinelyNewEvent() {
        AtomicInteger published = new AtomicInteger();
        events.subscribe(QualityEventIngested.class, e -> published.incrementAndGet());
        service.ingest(event("evt-1", "checkout"));
        service.ingest(event("evt-1", "checkout"));
        assertThat(published.get()).isEqualTo(1);
    }

    @Test
    void keepsEventsPerProject() {
        service.ingest(event("evt-1", "checkout"));
        service.ingest(event("evt-2", "ledger"));
        assertThat(service.eventsFor("checkout")).hasSize(1);
        assertThat(service.eventsFor("ledger")).hasSize(1);
        assertThat(service.eventsFor("unknown")).isEmpty();
    }

    @Test
    void ingestsAndListsOverHttp() {
        try (HttpApp app = IngestApi.newApp(service, 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            URI base = URI.create("http://localhost:" + app.port());

            ServiceClient.Reply first = client.postJson(base.resolve("/events"), Map.of(
                "id", "evt-http-1", "project", "checkout", "commit", "abc123", "tool", "jacoco",
                "timestamp", 1L,
                "metrics", Map.of("coveragePercent", 88, "violations", 2, "bugs", 0)));
            assertThat(first.status()).isEqualTo(201);

            ServiceClient.Reply retry = client.postJson(base.resolve("/events"), Map.of(
                "id", "evt-http-1", "project", "checkout", "commit", "abc123", "tool", "jacoco",
                "timestamp", 1L,
                "metrics", Map.of("coveragePercent", 88, "violations", 2, "bugs", 0)));
            assertThat(retry.status()).isEqualTo(200);

            ServiceClient.Reply listed = client.getJson(base.resolve("/events/checkout"));
            assertThat(listed.status()).isEqualTo(200);
            assertThat(listed.jsonObject())
                .containsEntry("project", "checkout")
                .containsEntry("count", 1L);
            assertThat(listed.body()).contains("evt-http-1");
        }
    }

    @Test
    void rejectsAnEventMissingMetrics() {
        try (HttpApp app = IngestApi.newApp(service, 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            ServiceClient.Reply reply = client.postJson(
                URI.create("http://localhost:" + app.port() + "/events"),
                Map.of("id", "evt-bad", "project", "checkout", "commit", "abc123",
                    "tool", "jacoco", "timestamp", 1L));
            assertThat(reply.status()).isEqualTo(400);
        }
    }

    @Test
    void returnsAnEmptyEnvelopeForAnUnknownProjectOverHttp() {
        try (HttpApp app = IngestApi.newApp(service, 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            ServiceClient.Reply reply =
                client.getJson(URI.create("http://localhost:" + app.port() + "/events/nope"));
            assertThat(reply.status()).isEqualTo(200);
            assertThat(reply.jsonObject())
                .containsEntry("project", "nope")
                .containsEntry("count", 0L);
        }
    }
}
