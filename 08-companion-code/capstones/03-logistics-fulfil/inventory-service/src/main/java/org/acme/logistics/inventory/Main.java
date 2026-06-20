package org.acme.logistics.inventory;

import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/** The inventory-service entry point — wiring only. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8101);
        InventoryService service = new InventoryService(new InMemoryInventoryRepository());
        HttpApp app = InventoryApi.newApp(service, port);
        app.start();
    }
}
