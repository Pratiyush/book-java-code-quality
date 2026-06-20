package org.acme.commerce.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.junit.jupiter.api.Test;

class CatalogServiceTest {

    private final CatalogService service = new CatalogService(new InMemoryCatalogRepository());

    @Test
    void returnsAseededProduct() {
        Product product = service.require("sku-keyboard");
        assertThat(product.name()).isEqualTo("Mechanical keyboard");
        assertThat(product.price().minorUnits()).isEqualTo(7999L);
    }

    @Test
    void rejectsUnknownProductAsNotFound() {
        assertThatThrownBy(() -> service.require("sku-missing"))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(404));
    }

    @Test
    void servesAproductOverHttp() {
        try (HttpApp app = CatalogApi.newApp(service, 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            ServiceClient.Reply reply =
                client.getJson(URI.create("http://localhost:" + app.port() + "/products/sku-mouse"));
            assertThat(reply.status()).isEqualTo(200);
            assertThat(reply.jsonObject())
                .containsEntry("id", "sku-mouse")
                .containsEntry("priceMinor", 2999L);
        }
    }

    @Test
    void returns404OverHttpForUnknownProduct() {
        try (HttpApp app = CatalogApi.newApp(service, 0).start()) {
            ServiceClient client = ServiceClient.withDefaults();
            ServiceClient.Reply reply =
                client.getJson(URI.create("http://localhost:" + app.port() + "/products/nope"));
            assertThat(reply.status()).isEqualTo(404);
        }
    }
}
