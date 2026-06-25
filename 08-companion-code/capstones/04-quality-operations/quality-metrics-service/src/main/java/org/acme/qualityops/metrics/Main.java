package org.acme.qualityops.metrics;

import java.net.URI;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/**
 * The quality-metrics-service entry point. It reads its own port plus the ingest service's base URL
 * from configuration ({@code ingest.url}), so the same build runs against an ingest service on any
 * host. Wires the HTTP adapter to the metrics logic and starts the app — wiring only, no logic.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8092);
        URI ingestUri = URI.create(config.string("ingest.url", "http://localhost:8091"));

        ServiceClient client = ServiceClient.withDefaults();
        MetricsService service = new MetricsService(new IngestEventsClient(client, ingestUri));
        HttpApp app = MetricsApi.newApp(service, port);
        app.start();
    }
}
