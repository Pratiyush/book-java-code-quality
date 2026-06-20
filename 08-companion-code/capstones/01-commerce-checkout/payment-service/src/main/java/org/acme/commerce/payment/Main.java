package org.acme.commerce.payment;

import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/** The payment-service entry point — wiring only (port from config, in-memory adapter, start). */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8082);
        PaymentService service = new PaymentService(new InMemoryPaymentRepository());
        HttpApp app = PaymentApi.newApp(service, port);
        app.start();
    }
}
