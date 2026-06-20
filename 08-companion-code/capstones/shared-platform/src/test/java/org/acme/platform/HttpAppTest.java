package org.acme.platform;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;
import org.junit.jupiter.api.Test;

class HttpAppTest {

    @Test
    void routesPathParametersAndReturnsJson() {
        try (HttpApp app = new HttpApp("widget-service", 0)) {
            app.get("/widgets/{id}", request ->
                Response.ok(Map.of("id", request.pathParam("id"), "status", "FOUND")));
            app.start();
            ServiceClient client = ServiceClient.withDefaults();

            ServiceClient.Reply reply = client.getJson(uri(app, "/widgets/abc-123"));

            assertThat(reply.status()).isEqualTo(200);
            assertThat(reply.jsonObject()).containsEntry("id", "abc-123").containsEntry("status", "FOUND");
        }
    }

    @Test
    void mapsApiExceptionToProblemJson() {
        try (HttpApp app = new HttpApp("widget-service", 0)) {
            app.get("/widgets/{id}", request -> {
                throw ApiException.notFound("widget-unknown", "no widget " + request.pathParam("id"));
            });
            app.start();
            ServiceClient client = ServiceClient.withDefaults();

            ServiceClient.Reply reply = client.getJson(uri(app, "/widgets/missing"));

            assertThat(reply.status()).isEqualTo(404);
            assertThat(reply.jsonObject())
                .containsEntry("type", "widget-unknown")
                .containsEntry("status", 404L);
        }
    }

    @Test
    void returns404ForUnknownRouteAnd405ForWrongMethod() {
        try (HttpApp app = new HttpApp("widget-service", 0)) {
            app.get("/widgets/{id}", request -> Response.ok(Map.of("ok", true)));
            app.start();
            ServiceClient client = ServiceClient.withDefaults();

            assertThat(client.getJson(uri(app, "/unknown")).status()).isEqualTo(404);
            assertThat(client.postJson(uri(app, "/widgets/1"), Map.of()).status()).isEqualTo(405);
        }
    }

    @Test
    void alwaysExposesHealth() {
        try (HttpApp app = new HttpApp("widget-service", 0)) {
            app.start();
            ServiceClient client = ServiceClient.withDefaults();

            ServiceClient.Reply reply = client.getJson(uri(app, "/health"));

            assertThat(reply.status()).isEqualTo(200);
            assertThat(reply.jsonObject()).containsEntry("status", "UP").containsEntry("service", "widget-service");
        }
    }

    private static URI uri(HttpApp app, String path) {
        return URI.create("http://localhost:" + app.port() + path);
    }
}
