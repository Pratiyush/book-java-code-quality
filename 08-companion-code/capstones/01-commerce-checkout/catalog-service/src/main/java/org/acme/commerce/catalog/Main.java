package org.acme.commerce.catalog;

import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/**
 * The catalog-service entry point. Reads its port from configuration ({@code -Dport=...} or
 * {@code PORT}), wires the in-memory adapter, and starts the HTTP app. Kept to wiring only — no
 * logic — so the runnable boundary is obvious.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8081);
        CatalogService service = new CatalogService(new InMemoryCatalogRepository());
        HttpApp app = CatalogApi.newApp(service, port);
        app.start();
    }
}
