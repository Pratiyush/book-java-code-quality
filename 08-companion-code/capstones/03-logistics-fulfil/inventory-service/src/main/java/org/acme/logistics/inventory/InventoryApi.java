package org.acme.logistics.inventory;

import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Request;
import org.acme.platform.http.Response;
import org.acme.platform.json.Json;

/**
 * The HTTP surface of inventory-service (Part: API design). A successful reservation returns 201; an
 * idempotent re-reservation returns 200; insufficient stock returns 409 — so the orchestrator can
 * branch on the status alone.
 *
 * <ul>
 *   <li>{@code GET  /inventory/{sku}}                 → 200 available count
 *   <li>{@code POST /reservations}                    → 201 reserved · 200 already reserved · 409 out of stock
 *   <li>{@code POST /reservations/{reference}/release}→ 200 released/already released · 404 unknown
 * </ul>
 */
public final class InventoryApi {

    private InventoryApi() {
    }

    public static HttpApp newApp(InventoryService service, int port) {
        HttpApp app = new HttpApp("inventory-service", port);
        app.get("/inventory/{sku}", request -> Response.ok(Map.of(
            "sku", request.pathParam("sku"), "available", service.available(request.pathParam("sku")))));
        app.post("/reservations", request -> reserve(service, request));
        app.post("/reservations/{reference}/release", request -> release(service, request));
        return app;
    }

    private static Response reserve(InventoryService service, Request request) {
        Map<String, Object> body = request.jsonBody();
        String reference = Json.requireString(body, "reference");
        String sku = Json.requireString(body, "sku");
        int quantity = Math.toIntExact(Json.requireLong(body, "quantity"));
        InventoryRepository.ReserveOutcome outcome = service.reserve(reference, sku, quantity);
        return switch (outcome) {
            case RESERVED -> Response.created(reservationBody(reference, sku, quantity, "RESERVED"));
            case ALREADY_RESERVED -> Response.ok(reservationBody(reference, sku, quantity, "ALREADY_RESERVED"));
            case INSUFFICIENT -> throw ApiException.conflict("out-of-stock",
                "insufficient stock for " + sku + " (requested " + quantity + ")");
        };
    }

    private static Response release(InventoryService service, Request request) {
        String reference = request.pathParam("reference");
        InventoryRepository.ReleaseOutcome outcome = service.release(reference);
        return switch (outcome) {
            case RELEASED, ALREADY_RELEASED -> Response.ok(Map.of(
                "reference", reference, "status", outcome.name()));
            case UNKNOWN -> throw ApiException.notFound("reservation-unknown",
                "no reservation with reference " + reference);
        };
    }

    private static Map<String, Object> reservationBody(String reference, String sku, int quantity, String status) {
        return Map.of("reference", reference, "sku", sku, "quantity", quantity, "status", status);
    }
}
