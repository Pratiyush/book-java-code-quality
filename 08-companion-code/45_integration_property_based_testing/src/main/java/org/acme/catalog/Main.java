package org.acme.catalog;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * Boots the catalog service for a manual run, so the {@code /health} surface and a real lookup can be
 * observed live.
 *
 * <p>The bind port comes from the externalized {@link CatalogConfig} (the {@code catalog.profile} system
 * property selects {@code dev} or {@code prod}). The {@code %prod} profile binds the published port
 * {@code 8080}; {@code %dev} binds an ephemeral one. Run:
 *
 * <pre>{@code
 *   mvn -q -DskipTests compile exec:java -Dexec.mainClass=org.acme.catalog.Main -Dcatalog.profile=prod
 *   # then: GET http://localhost:8080/health   and   GET http://localhost:8080/catalog/HOME-0042
 * }</pre>
 */
public final class Main {

    private static final Logger LOG = System.getLogger(Main.class.getName());

    private Main() {
    }

    /**
     * Starts the catalog server, stocks one product, and blocks until interrupted.
     *
     * @param args ignored
     * @throws InterruptedException if the run is interrupted while serving
     */
    public static void main(String[] args) throws InterruptedException {
        CatalogConfig config = CatalogConfig.load();
        try (CatalogApi server = new CatalogApi(config.serverPort())
                .stock(new Product(new Sku("HOME", 42), "Desk Lamp", new Money(4_999L, "USD")))
                .start()) {
            LOG.log(Level.INFO, () -> "catalog up on http://localhost:" + server.port()
                + " (try /health and /catalog/HOME-0042)");
            Thread.currentThread().join();
        }
    }
}
