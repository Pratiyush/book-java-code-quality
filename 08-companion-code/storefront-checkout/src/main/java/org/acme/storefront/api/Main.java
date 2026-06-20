package org.acme.storefront.api;

import java.io.IOException;
import java.time.Clock;
import org.acme.storefront.checkout.Catalog;
import org.acme.storefront.checkout.CheckoutConfig;
import org.acme.storefront.checkout.CheckoutRepository;
import org.acme.storefront.checkout.CheckoutService;
import org.acme.storefront.checkout.TokenGenerator;
import org.acme.storefront.payment.PaymentSimulator;

/**
 * Entry point that wires the flagship checkout microservice and starts it.
 *
 * <p>Run with an optional port argument (defaults to 8080):
 * {@code java -cp target/classes org.acme.storefront.api.Main 8080}.
 */
public final class Main {

    private static final int DEFAULT_PORT = 8080;

    private Main() {
    }

    /**
     * Boots the service.
     *
     * @param args optional single argument: the port to bind
     * @throws IOException if the server socket cannot be bound
     */
    public static void main(String[] args) throws IOException {
        CheckoutConfig config = CheckoutConfig.defaults();
        CheckoutService service =
                new CheckoutService(
                        Catalog.withSampleData(),
                        new CheckoutRepository(),
                        new TokenGenerator(),
                        new PaymentSimulator(config.paymentCeilingMinor()),
                        config,
                        Clock.systemUTC());

        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        CheckoutHttpServer server = new CheckoutHttpServer(service, config, port);
        Runtime.getRuntime().addShutdownHook(new Thread(server::close, "checkout-shutdown"));
        server.start();
    }
}
