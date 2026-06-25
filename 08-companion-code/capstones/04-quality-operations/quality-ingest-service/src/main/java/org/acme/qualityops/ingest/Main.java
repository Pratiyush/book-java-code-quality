package org.acme.qualityops.ingest;

import java.util.logging.Logger;
import org.acme.qualityops.ingest.event.QualityEventIngested;
import org.acme.platform.config.Config;
import org.acme.platform.event.EventBus;
import org.acme.platform.http.HttpApp;

/**
 * The quality-ingest-service entry point. Reads its port from configuration ({@code -Dport=...} or
 * {@code PORT}), wires the in-memory adapter, and starts the HTTP app. The event bus is wired to log
 * each new ingestion — the smallest possible subscriber, standing in for whatever a real deployment
 * would do (publish to a broker, invalidate a cached aggregate). Kept to wiring only — no logic.
 */
public final class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8091);

        EventBus events = new EventBus();
        events.subscribe(QualityEventIngested.class,
            e -> LOG.info(() -> "quality event ingested: " + e.eventId() + " for " + e.project()));

        IngestService service = new IngestService(
            new InMemoryEventRepository(), events, System::currentTimeMillis);
        HttpApp app = IngestApi.newApp(service, port);
        app.start();
    }
}
