package org.acme.commerce.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.event.EventBus;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;
import org.junit.jupiter.api.Test;

/**
 * Exercises order-service's real outbound adapters ({@link CatalogPricingClient},
 * {@link PaymentGatewayClient}) over real HTTP, against lightweight stand-ins for catalog-service and
 * payment-service. The stubs are built from the shared platform's {@link HttpApp} rather than from
 * the other service modules, so order-service stays independent of them while the JSON contract on
 * each seam is still tested for real.
 */
class CheckoutFlowIntegrationTest {

    @Test
    void placesAndPaysAnOrderAcrossTheRealHttpSeams() {
        try (HttpApp catalog = stubCatalog();
             HttpApp payment = stubPayment();
             HttpApp orders = orderApp(catalog, payment)) {

            ServiceClient client = ServiceClient.withDefaults();
            URI ordersBase = URI.create("http://localhost:" + orders.port());

            ServiceClient.Reply placed = client.postJson(ordersBase.resolve("/orders"),
                Map.of("items", List.of(Map.of("productId", "sku-a", "quantity", 2))));
            assertThat(placed.status()).isEqualTo(201);
            assertThat(placed.jsonObject()).containsEntry("totalMinor", 2000L);
            String orderId = (String) placed.jsonObject().get("id");

            ServiceClient.Reply paid = client.postJson(ordersBase.resolve("/orders/" + orderId + "/pay"),
                Map.of("pan", "4111111111111234", "idempotencyKey", "key-int-1"));
            assertThat(paid.status()).isEqualTo(200);
            assertThat(paid.jsonObject()).containsEntry("status", "PAID");
        }
    }

    @Test
    void declinedPaymentLeavesTheOrderDeclined() {
        try (HttpApp catalog = stubCatalog();
             HttpApp payment = stubPayment();
             HttpApp orders = orderApp(catalog, payment)) {

            ServiceClient client = ServiceClient.withDefaults();
            URI ordersBase = URI.create("http://localhost:" + orders.port());

            String orderId = (String) client.postJson(ordersBase.resolve("/orders"),
                Map.of("items", List.of(Map.of("productId", "sku-a", "quantity", 1))))
                .jsonObject().get("id");

            ServiceClient.Reply declined = client.postJson(ordersBase.resolve("/orders/" + orderId + "/pay"),
                Map.of("pan", "4111111111110000", "idempotencyKey", "key-int-2"));
            assertThat(declined.status()).isEqualTo(402);
            assertThat(declined.jsonObject()).containsEntry("status", "DECLINED");
        }
    }

    private static HttpApp stubCatalog() {
        HttpApp app = new HttpApp("stub-catalog", 0);
        app.get("/products/{id}", request -> Response.ok(Map.of(
            "id", request.pathParam("id"), "name", "stub", "priceMinor", 1000L, "currency", "USD")));
        return app.start();
    }

    private static HttpApp stubPayment() {
        HttpApp app = new HttpApp("stub-payment", 0);
        app.post("/payments", request -> {
            Map<String, Object> body = request.jsonBody();
            boolean declined = String.valueOf(body.get("pan")).endsWith("0000");
            int status = declined ? 402 : 200;
            return Response.json(status, Map.of(
                "id", "pay-stub", "orderId", String.valueOf(body.get("orderId")),
                "status", declined ? "DECLINED" : "APPROVED",
                "detail", declined ? "insufficient funds" : "auth-0001"));
        });
        return app.start();
    }

    private static HttpApp orderApp(HttpApp catalog, HttpApp payment) {
        ServiceClient client = ServiceClient.withDefaults();
        OrderService service = new OrderService(
            new InMemoryOrderRepository(),
            new CatalogPricingClient(client, URI.create("http://localhost:" + catalog.port())),
            new PaymentGatewayClient(client, URI.create("http://localhost:" + payment.port())),
            new EventBus(),
            () -> 0L);
        return OrderApi.newApp(service, 0).start();
    }
}
