package org.acme.logistics.fulfilment;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;
import org.junit.jupiter.api.Test;

/**
 * Exercises fulfilment-orchestrator's real outbound adapters ({@link InventoryClient},
 * {@link ShipmentClient}) over real HTTP, against shared-platform stubs for inventory-service and
 * shipment-service. It proves the saga end to end: a successful fulfilment, an idempotent replay, and
 * — the important one — that an out-of-stock line triggers a 409 AND the compensating release returns
 * the stock already reserved for that order.
 */
class FulfilmentFlowIntegrationTest {

    @Test
    void fulfilsReplaysAndCompensatesOnOutOfStock() {
        StubInventory inventoryState = new StubInventory();
        try (HttpApp inventory = inventoryState.app();
             HttpApp shipment = stubShipment();
             HttpApp orchestrator = orchestratorApp(inventory, shipment)) {

            ServiceClient client = ServiceClient.withDefaults();
            URI base = URI.create("http://localhost:" + orchestrator.port());

            ServiceClient.Reply fulfilled = client.postJson(base.resolve("/fulfilments"), Map.of(
                "orderId", "ord-1", "lines", List.of(Map.of("sku", "sku-a", "quantity", 2))));
            assertThat(fulfilled.status()).isEqualTo(201);

            ServiceClient.Reply replay = client.postJson(base.resolve("/fulfilments"), Map.of(
                "orderId", "ord-1", "lines", List.of(Map.of("sku", "sku-a", "quantity", 2))));
            assertThat(replay.status()).isEqualTo(200);

            // ord-2 reserves sku-a (1) then fails on sku-b (wants 5, only 1 in stock) -> 409 + compensation.
            ServiceClient.Reply outOfStock = client.postJson(base.resolve("/fulfilments"), Map.of(
                "orderId", "ord-2", "lines", List.of(
                    Map.of("sku", "sku-a", "quantity", 1), Map.of("sku", "sku-b", "quantity", 5))));
            assertThat(outOfStock.status()).isEqualTo(409);

            // After compensation, sku-a is back to 3 (5 - 2 for ord-1; ord-2's reservation released).
            ServiceClient.Reply skuA = client.getJson(
                URI.create("http://localhost:" + inventory.port() + "/inventory/sku-a"));
            assertThat(skuA.jsonObject()).containsEntry("available", 3L);
        }
    }

    private static HttpApp stubShipment() {
        Map<String, String> byOrder = new HashMap<>();
        HttpApp app = new HttpApp("stub-shipment", 0);
        app.post("/shipments", request -> {
            String orderId = String.valueOf(request.jsonBody().get("orderId"));
            synchronized (byOrder) {
                boolean fresh = !byOrder.containsKey(orderId);
                String id = byOrder.computeIfAbsent(orderId, o -> "shp-" + o);
                return Response.json(fresh ? 201 : 200, Map.of("id", id, "orderId", orderId, "status", "CREATED"));
            }
        });
        return app.start();
    }

    private static HttpApp orchestratorApp(HttpApp inventory, HttpApp shipment) {
        ServiceClient client = ServiceClient.withDefaults();
        FulfilmentService service = new FulfilmentService(
            new InMemoryFulfilmentRepository(),
            new InventoryClient(client, URI.create("http://localhost:" + inventory.port())),
            new ShipmentClient(client, URI.create("http://localhost:" + shipment.port())));
        return FulfilmentApi.newApp(service, 0).start();
    }

    /** A small, faithful stand-in for inventory-service: atomic reserve/release with idempotency. */
    private static final class StubInventory {
        private final Object lock = new Object();
        private final Map<String, Integer> available = new HashMap<>(Map.of("sku-a", 5, "sku-b", 1));
        private final Map<String, int[]> reservations = new HashMap<>(); // ref -> {qty, active(1/0)}
        private final Map<String, String> skuByRef = new HashMap<>();

        HttpApp app() {
            HttpApp app = new HttpApp("stub-inventory", 0);
            app.get("/inventory/{sku}", request -> {
                synchronized (lock) {
                    return Response.ok(Map.of("sku", request.pathParam("sku"),
                        "available", available.getOrDefault(request.pathParam("sku"), 0)));
                }
            });
            app.post("/reservations", request -> reserve(request.jsonBody()));
            app.post("/reservations/{reference}/release", request -> release(request.pathParam("reference")));
            return app.start();
        }

        private Response reserve(Map<String, Object> body) {
            String reference = String.valueOf(body.get("reference"));
            String sku = String.valueOf(body.get("sku"));
            int quantity = ((Number) body.get("quantity")).intValue();
            synchronized (lock) {
                int[] existing = reservations.get(reference);
                if (existing != null && existing[1] == 1) {
                    return Response.ok(Map.of("reference", reference, "status", "ALREADY_RESERVED"));
                }
                int onHand = available.getOrDefault(sku, 0);
                if (onHand < quantity) {
                    return Response.json(409, Map.of("type", "out-of-stock", "title", "Conflict", "status", 409));
                }
                available.put(sku, onHand - quantity);
                reservations.put(reference, new int[] {quantity, 1});
                skuByRef.put(reference, sku);
                return Response.created(Map.of("reference", reference, "status", "RESERVED"));
            }
        }

        private Response release(String reference) {
            synchronized (lock) {
                int[] reservation = reservations.get(reference);
                if (reservation == null) {
                    return Response.json(404, Map.of("type", "reservation-unknown", "title", "Not Found",
                        "status", 404));
                }
                if (reservation[1] == 1) {
                    available.merge(skuByRef.get(reference), reservation[0], Integer::sum);
                    reservation[1] = 0;
                }
                return Response.ok(Map.of("reference", reference, "status", "RELEASED"));
            }
        }
    }
}
