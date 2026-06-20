package org.acme.storefront.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.acme.storefront.checkout.Catalog;
import org.acme.storefront.checkout.CheckoutConfig;
import org.acme.storefront.checkout.CheckoutRepository;
import org.acme.storefront.checkout.CheckoutService;
import org.acme.storefront.checkout.TokenGenerator;
import org.acme.storefront.payment.PaymentSimulator;
import org.junit.jupiter.api.Test;

class CheckoutHttpServerTest {

    private static final String VALID_CARD = "4242424242424242";

    private CheckoutHttpServer newServer() throws IOException {
        CheckoutConfig config = CheckoutConfig.defaults();
        CheckoutService service =
                new CheckoutService(
                        Catalog.withSampleData(),
                        new CheckoutRepository(),
                        new TokenGenerator(),
                        new PaymentSimulator(config.paymentCeilingMinor()),
                        config,
                        Clock.systemUTC());
        return new CheckoutHttpServer(service, config, 0);
    }

    private static HttpResponse<String> post(int port, String path, Map<String, Object> body)
            throws IOException, InterruptedException {
        HttpRequest request =
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(Json.write(body)))
                        .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> get(int port, String path)
            throws IOException, InterruptedException {
        HttpRequest request =
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static Map<String, Object> cart(String productId, int quantity) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("productId", productId);
        item.put("quantity", quantity);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("items", List.of(item));
        return body;
    }

    @Test
    void healthReportsUp() throws Exception {
        try (CheckoutHttpServer server = newServer()) {
            server.start();
            HttpResponse<String> response = get(server.port(), "/health");
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(Json.parseObject(response.body())).containsEntry("status", "UP");
        }
    }

    @Test
    void createThenResolveThenPay() throws Exception {
        try (CheckoutHttpServer server = newServer()) {
            server.start();
            int port = server.port();

            HttpResponse<String> created = post(port, "/checkouts", cart("BOOK-EJ", 2));
            assertThat(created.statusCode()).isEqualTo(201);
            Map<String, Object> createdBody = Json.parseObject(created.body());
            assertThat(createdBody).containsEntry("totalMinor", 9_000L);
            String token = (String) createdBody.get("token");

            HttpResponse<String> resolved = get(port, "/checkouts/" + token);
            assertThat(resolved.statusCode()).isEqualTo(200);
            assertThat(Json.parseObject(resolved.body())).containsEntry("status", "PENDING");

            Map<String, Object> payment = new LinkedHashMap<>();
            payment.put("pan", VALID_CARD);
            payment.put("amountMinor", 9_000L);
            payment.put("currency", "USD");
            payment.put("idempotencyKey", "http-pay-1");
            HttpResponse<String> paid = post(port, "/checkouts/" + token + "/payment", payment);
            assertThat(paid.statusCode()).isEqualTo(200);
            assertThat(Json.parseObject(paid.body())).containsEntry("status", "APPROVED");
        }
    }

    @Test
    void unknownCheckoutIs404() throws Exception {
        try (CheckoutHttpServer server = newServer()) {
            server.start();
            assertThat(get(server.port(), "/checkouts/nope").statusCode()).isEqualTo(404);
        }
    }

    @Test
    void emptyCartIs400() throws Exception {
        try (CheckoutHttpServer server = newServer()) {
            server.start();
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("items", List.of());
            assertThat(post(server.port(), "/checkouts", body).statusCode()).isEqualTo(400);
        }
    }
}
