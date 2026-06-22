package org.acme.logistics.fulfilment;

import java.net.URI;
import java.util.Map;
import org.acme.platform.client.ServiceClient;

/**
 * The HTTP adapter to inventory-service (Part: service composition). A 201 or 200 from a reservation
 * means the stock is secured; a 409 means out of stock. Release is the compensating call and is
 * idempotent on the inventory side.
 */
public final class InventoryClient implements InventoryPort {

    private final ServiceClient client;
    private final URI inventoryBaseUri;

    public InventoryClient(ServiceClient client, URI inventoryBaseUri) {
        this.client = client;
        this.inventoryBaseUri = inventoryBaseUri;
    }

    @Override
    public boolean reserve(String reference, String sku, int quantity) {
        ServiceClient.Reply reply = client.postJson(inventoryBaseUri.resolve("/reservations"),
            Map.of("reference", reference, "sku", sku, "quantity", quantity));
        if (reply.status() == 409) {
            return false;
        }
        if (reply.status() != 201 && reply.status() != 200) {
            throw new IllegalStateException("inventory-service error: HTTP " + reply.status());
        }
        return true;
    }

    @Override
    public void release(String reference) {
        ServiceClient.Reply reply =
            client.postJson(inventoryBaseUri.resolve("/reservations/" + reference + "/release"), Map.of());
        if (!reply.isSuccess() && reply.status() != 404) {
            throw new IllegalStateException("inventory release failed: HTTP " + reply.status());
        }
    }
}
