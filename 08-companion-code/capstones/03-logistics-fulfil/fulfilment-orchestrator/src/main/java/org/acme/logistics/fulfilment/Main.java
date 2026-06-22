package org.acme.logistics.fulfilment;

import java.net.URI;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/**
 * The fulfilment-orchestrator entry point. Reads its port and the inventory/shipment base URLs from
 * configuration ({@code inventory.url}, {@code shipment.url}), wires the HTTP adapters, and starts.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8103);
        URI inventoryUri = URI.create(config.string("inventory.url", "http://localhost:8101"));
        URI shipmentUri = URI.create(config.string("shipment.url", "http://localhost:8102"));

        ServiceClient client = ServiceClient.withDefaults();
        FulfilmentService service = new FulfilmentService(
            new InMemoryFulfilmentRepository(),
            new InventoryClient(client, inventoryUri),
            new ShipmentClient(client, shipmentUri));

        HttpApp app = FulfilmentApi.newApp(service, port);
        app.start();
    }
}
