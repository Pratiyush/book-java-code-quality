package org.acme.logistics.shipment;

import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;
import org.acme.platform.json.Json;

/**
 * The HTTP surface of shipment-service (Part: API design).
 *
 * <ul>
 *   <li>{@code POST /shipments}                 → 201 created · 200 replay (order already shipped)
 *   <li>{@code GET  /shipments/{id}}            → 200 a shipment · 404 unknown
 *   <li>{@code POST /shipments/{id}/dispatch}   → 200 dispatched · 404 unknown
 * </ul>
 */
public final class ShipmentApi {

    private ShipmentApi() {
    }

    public static HttpApp newApp(ShipmentService service, int port) {
        HttpApp app = new HttpApp("shipment-service", port);
        app.post("/shipments", request -> {
            String orderId = Json.requireString(request.jsonBody(), "orderId");
            ShipmentService.CreateResult result = service.create(orderId);
            int status = result.replayed() ? 200 : 201;
            return Response.json(status, result.shipment().toBody());
        });
        app.get("/shipments/{id}", request -> Response.ok(service.require(request.pathParam("id")).toBody()));
        app.post("/shipments/{id}/dispatch", request ->
            Response.ok(service.dispatch(request.pathParam("id")).toBody()));
        return app;
    }
}
