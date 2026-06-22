package org.acme.commerce.order;

import java.net.URI;
import java.util.logging.Logger;
import org.acme.commerce.order.event.OrderPaid;
import org.acme.commerce.order.event.OrderPlaced;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.config.Config;
import org.acme.platform.event.EventBus;
import org.acme.platform.http.HttpApp;

/**
 * The order-service entry point. It reads its own port plus the catalog and payment base URLs from
 * configuration ({@code catalog.url}, {@code payment.url}), so the same build runs against services
 * on any host. The event bus is wired to log the domain events — the smallest possible subscriber,
 * standing in for whatever a real deployment would do (publish to a broker, update a read model).
 */
public final class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8083);
        URI catalogUri = URI.create(config.string("catalog.url", "http://localhost:8081"));
        URI paymentUri = URI.create(config.string("payment.url", "http://localhost:8082"));

        ServiceClient client = ServiceClient.withDefaults();
        EventBus events = new EventBus();
        events.subscribe(OrderPlaced.class, e -> LOG.info(() -> "order placed: " + e.orderId()));
        events.subscribe(OrderPaid.class, e -> LOG.info(() -> "order paid: " + e.orderId()));

        OrderService service = new OrderService(
            new InMemoryOrderRepository(),
            new CatalogPricingClient(client, catalogUri),
            new PaymentGatewayClient(client, paymentUri),
            events,
            System::currentTimeMillis);

        HttpApp app = OrderApi.newApp(service, port);
        app.start();
    }
}
