package org.acme.logistics.shipment;

import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/** The shipment-service entry point — wiring only. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8102);
        ShipmentService service = new ShipmentService(new InMemoryShipmentRepository());
        HttpApp app = ShipmentApi.newApp(service, port);
        app.start();
    }
}
