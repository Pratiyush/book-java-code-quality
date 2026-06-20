package org.acme.logistics.fulfilment;

import java.net.URI;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.json.Json;

/** The HTTP adapter to shipment-service (Part: service composition). */
public final class ShipmentClient implements ShipmentPort {

    private final ServiceClient client;
    private final URI shipmentBaseUri;

    public ShipmentClient(ServiceClient client, URI shipmentBaseUri) {
        this.client = client;
        this.shipmentBaseUri = shipmentBaseUri;
    }

    @Override
    public String createShipment(String orderId) {
        ServiceClient.Reply reply =
            client.postJson(shipmentBaseUri.resolve("/shipments"), Map.of("orderId", orderId));
        if (reply.status() != 201 && reply.status() != 200) {
            throw new IllegalStateException("shipment-service error: HTTP " + reply.status());
        }
        return Json.requireString(reply.jsonObject(), "id");
    }
}
