package org.acme.storefront.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.acme.storefront.checkout.CheckoutConfig;
import org.acme.storefront.checkout.CheckoutNotFoundException;
import org.acme.storefront.checkout.CheckoutService;
import org.acme.storefront.checkout.PaymentOutcome;
import org.acme.storefront.domain.CartItem;
import org.acme.storefront.domain.Checkout;
import org.acme.storefront.domain.CheckoutStatus;
import org.acme.storefront.payment.PaymentRequest;

/**
 * A minimal HTTP front end for the checkout service, built on the JDK's own {@link HttpServer} with a
 * virtual-thread-per-request executor (Chapter 14) — so the whole flagship demo needs no web
 * framework and no runtime dependency.
 *
 * <p>Routes, each mapping a {@link PaymentOutcome}/resolution case to a precise status:
 * <ul>
 *   <li>{@code POST /checkouts} → 201 created (400 on bad cart / unknown product / malformed JSON)
 *   <li>{@code GET /checkouts/{token}} → 200 summary, 404 unknown, 410 Gone when expired
 *   <li>{@code POST /checkouts/{token}/payment} → 200 approved, 402 declined, 404 unknown,
 *       409 already paid, 410 expired
 *   <li>{@code GET /health} → 200 with liveness and counters
 * </ul>
 *
 * <p>Implements {@link AutoCloseable} so it can be used in a try-with-resources block and in tests.
 */
public final class CheckoutHttpServer implements AutoCloseable {

    private static final System.Logger LOG = System.getLogger(CheckoutHttpServer.class.getName());
    private static final String BASE_PATH = "/checkouts";
    private static final String PAYMENT_SUFFIX = "/payment";

    private final HttpServer server;
    private final CheckoutService service;
    private final CheckoutConfig config;
    private final ExecutorService executor;
    private final AtomicLong checkoutsCreated = new AtomicLong();
    private final AtomicLong paymentsApproved = new AtomicLong();

    /**
     * Creates (but does not start) the server.
     *
     * @param service the checkout service to expose
     * @param config the configuration (used to build checkout URLs)
     * @param port the TCP port to bind, or {@code 0} for an ephemeral port (handy in tests)
     * @throws IOException if the socket cannot be bound
     */
    public CheckoutHttpServer(CheckoutService service, CheckoutConfig config, int port)
            throws IOException {
        this.service = Objects.requireNonNull(service, "service");
        this.config = Objects.requireNonNull(config, "config");
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        server.setExecutor(executor);
    }

    /** Registers routes and starts accepting requests. */
    public void start() {
        // Routes are registered here, not in the constructor, to avoid leaking `this` before the
        // object is fully built (the this-escape hazard from Chapter 13).
        server.createContext("/health", this::handleHealth);
        server.createContext(BASE_PATH, this::handleCheckouts);
        server.start();
        LOG.log(System.Logger.Level.INFO, "checkout service listening on port {0}", port());
    }

    /**
     * Returns the bound port (useful when {@code 0} was requested).
     *
     * @return the actual listening port
     */
    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.close();
    }

    private void handleHealth(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendError(exchange, 405, "method not allowed");
            return;
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("checkoutsCreated", checkoutsCreated.get());
        body.put("paymentsApproved", paymentsApproved.get());
        sendJson(exchange, 200, body);
    }

    private void handleCheckouts(HttpExchange exchange) throws IOException {
        try {
            route(exchange);
        } catch (CheckoutNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        } catch (Json.JsonParseException | IllegalArgumentException e) {
            // includes UnknownProductException (an IllegalArgumentException) and bad request bodies
            sendError(exchange, 400, e.getMessage());
        } catch (RuntimeException e) {
            LOG.log(System.Logger.Level.ERROR, "unexpected error handling request", e);
            sendError(exchange, 500, "internal error");
        }
    }

    private void route(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String rest = path.substring(BASE_PATH.length());

        if (rest.isEmpty() || "/".equals(rest)) {
            if ("POST".equals(method)) {
                createCheckout(exchange);
            } else {
                sendError(exchange, 405, "method not allowed");
            }
            return;
        }

        String tail = rest.substring(1); // drop leading '/'
        if (tail.endsWith(PAYMENT_SUFFIX)) {
            String token = tail.substring(0, tail.length() - PAYMENT_SUFFIX.length());
            if ("POST".equals(method)) {
                payCheckout(exchange, token);
            } else {
                sendError(exchange, 405, "method not allowed");
            }
            return;
        }

        if ("GET".equals(method)) {
            getCheckout(exchange, tail);
        } else {
            sendError(exchange, 405, "method not allowed");
        }
    }

    private void createCheckout(HttpExchange exchange) throws IOException {
        Map<String, Object> request = readJsonBody(exchange);
        List<CartItem> cart = new ArrayList<>();
        for (Object element : reqList(request, "items")) {
            if (!(element instanceof Map<?, ?> item)) {
                throw new IllegalArgumentException("each item must be an object");
            }
            cart.add(new CartItem(reqString(item, "productId"), reqInt(item, "quantity")));
        }

        Checkout checkout = service.createCheckout(cart);
        checkoutsCreated.incrementAndGet();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("token", checkout.token());
        body.put("checkoutUrl", config.baseUrl() + "/checkout/" + checkout.token());
        body.put("totalMinor", checkout.total().amountMinor());
        body.put("currency", checkout.total().currency());
        body.put("expiresAt", checkout.expiresAt().toString());
        body.put("status", checkout.status().name());
        sendJson(exchange, 201, body);
    }

    private void getCheckout(HttpExchange exchange, String token) throws IOException {
        Optional<Checkout> resolved = service.resolve(token);
        if (resolved.isEmpty()) {
            sendError(exchange, 404, "unknown checkout");
            return;
        }
        Checkout checkout = resolved.get();
        if (checkout.status() == CheckoutStatus.EXPIRED) {
            sendError(exchange, 410, "checkout link expired");
            return;
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("token", checkout.token());
        body.put("status", checkout.status().name());
        body.put("totalMinor", checkout.total().amountMinor());
        body.put("currency", checkout.total().currency());
        body.put("expiresAt", checkout.expiresAt().toString());
        sendJson(exchange, 200, body);
    }

    private void payCheckout(HttpExchange exchange, String token) throws IOException {
        Map<String, Object> request = readJsonBody(exchange);
        PaymentRequest paymentRequest =
                new PaymentRequest(
                        reqString(request, "pan"),
                        reqLong(request, "amountMinor"),
                        reqString(request, "currency"),
                        reqString(request, "idempotencyKey"));

        PaymentOutcome outcome = service.pay(token, paymentRequest);
        switch (outcome) {
            case PaymentOutcome.Approved approved -> {
                paymentsApproved.incrementAndGet();
                Map<String, Object> body = new LinkedHashMap<>();
                body.put("status", "APPROVED");
                body.put("authCode", approved.authCode());
                body.put("checkoutStatus", approved.checkout().status().name());
                sendJson(exchange, 200, body);
            }
            case PaymentOutcome.Declined declined ->
                    sendError(exchange, 402, declined.reason());
            case PaymentOutcome.Expired ignored ->
                    sendError(exchange, 410, "checkout link expired");
            case PaymentOutcome.AlreadyPaid ignored ->
                    sendError(exchange, 409, "checkout already paid");
        }
    }

    private static Map<String, Object> readJsonBody(HttpExchange exchange) throws IOException {
        byte[] bytes = exchange.getRequestBody().readAllBytes();
        return Json.parseObject(new String(bytes, StandardCharsets.UTF_8));
    }

    private void sendJson(HttpExchange exchange, int status, Map<String, Object> body)
            throws IOException {
        byte[] bytes = Json.write(body).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private void sendError(HttpExchange exchange, int status, String message) throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", message);
        sendJson(exchange, status, body);
    }

    private static List<?> reqList(Map<?, ?> map, String key) {
        if (map.get(key) instanceof List<?> list) {
            return list;
        }
        throw new IllegalArgumentException("missing or invalid array field: " + key);
    }

    private static String reqString(Map<?, ?> map, String key) {
        if (map.get(key) instanceof String s && !s.isBlank()) {
            return s;
        }
        throw new IllegalArgumentException("missing or invalid string field: " + key);
    }

    private static long reqLong(Map<?, ?> map, String key) {
        if (map.get(key) instanceof Number n) {
            return n.longValue();
        }
        throw new IllegalArgumentException("missing or invalid number field: " + key);
    }

    private static int reqInt(Map<?, ?> map, String key) {
        return Math.toIntExact(reqLong(map, key));
    }
}
