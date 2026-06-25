package org.acme.qualityops.gate;

import java.net.URI;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/**
 * The quality-gate-service entry point. It reads its own port plus the metrics service's base URL
 * from configuration ({@code metrics.url}), so the same build runs against a metrics service on any
 * host. Wires the HTTP adapter and the in-memory decision store to the gate logic and starts the
 * app — wiring only, no logic.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8093);
        URI metricsUri = URI.create(config.string("metrics.url", "http://localhost:8092"));

        ServiceClient client = ServiceClient.withDefaults();
        GateService service = new GateService(
            new MetricsServiceClient(client, metricsUri),
            new InMemoryDecisionRepository());
        HttpApp app = GateApi.newApp(service, port);
        app.start();
    }
}
