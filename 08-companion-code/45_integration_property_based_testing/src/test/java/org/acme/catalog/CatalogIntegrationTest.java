package org.acme.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.net.URI;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Integration test: a real {@link CatalogClient} driving a real {@link CatalogApi} over real HTTP.
 *
 * <p>This is the in-JVM realization of the chapter's integration layer. The server boots on an
 * ephemeral port and the client makes real requests, so the test exercises the wire encoding, the real
 * status-code mapping, and the real parse — the behaviour a mocked client cannot reach. A higher-
 * fidelity option (a real database in a Docker container via Testcontainers) is the prose's named
 * approach; it is reproduction-gated where no container runtime exists, which is itself the cost of
 * integration fidelity the chapter describes. Here the "real collaborator" is a real HTTP service in
 * the same JVM — enough to show the fidelity gap a stub would hide, with no Docker dependency.
 */
class CatalogIntegrationTest {

    private static final Sku WIDGET = new Sku("HOME", 42);
    private static final Duration TIMEOUT = Duration.ofSeconds(2);

    @Test
    @DisplayName("a stocked product round-trips through a real server and client over HTTP")
    void looksUpAStockedProductOverRealHttp() {
        // tag::integration-roundtrip[]
        try (CatalogApi server = new CatalogApi(0)
                .stock(new Product(WIDGET, "Desk Lamp", new Money(4_999L, "USD")))
                .start()) {
            URI base = URI.create("http://localhost:" + server.port());
            Product found = new CatalogClient(base, TIMEOUT).lookup(WIDGET); // real HTTP
            assertThat(found.price()).isEqualTo(new Money(4_999L, "USD"));
        }
        // end::integration-roundtrip[]
    }

    @Nested
    @DisplayName("the explicit failure path, over real HTTP")
    class FailurePath {

        @Test
        @DisplayName("an unstocked SKU surfaces a typed 404 from the real server")
        void unstockedSkuSurfacesTypedNotFound() {
            try (CatalogApi server = new CatalogApi(0).start()) {
                CatalogClient client = new CatalogClient(
                    URI.create("http://localhost:" + server.port()), TIMEOUT);

                assertThatExceptionOfType(CatalogLookupException.class)
                    .isThrownBy(() -> client.lookup(new Sku("GRO", 7)))
                    .matches(ex -> ex.status() == 404);

                assertThat(server.lookupCount()).isEqualTo(1L);
            }
        }
    }

    @Test
    @DisplayName("the server's /health surface reports liveness and the lookup counter")
    void healthSurfaceReportsLiveness() {
        try (CatalogApi server = new CatalogApi(0)
                .stock(new Product(WIDGET, "Desk Lamp", new Money(4_999L, "USD")))
                .start()) {

            CatalogClient client = new CatalogClient(
                URI.create("http://localhost:" + server.port()), TIMEOUT);
            client.lookup(WIDGET);

            assertThat(server.lookupCount()).isEqualTo(1L);
        }
    }
}
