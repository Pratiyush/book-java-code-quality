package org.acme.catalog;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A minimal product-catalog HTTP service, built on the JDK's own {@link HttpServer} with a
 * virtual-thread-per-request executor (Chapter 22) — so the module needs no web framework and no
 * runtime dependency.
 *
 * <p>This is the <em>real collaborator</em> the integration test runs against. Booting it on an
 * ephemeral port ({@code 0}) and driving it with a real {@link CatalogClient} over real HTTP exercises
 * the wire encoding, the real status-code mapping, and the real parse — the behaviour a mocked client
 * cannot reach. It is the in-JVM realization of the pyramid's middle layer; a Docker-backed real
 * database is the higher-fidelity option the chapter's prose describes and the README records as
 * reproduction-gated where no runtime is available.
 *
 * <p>Routes:
 * <ul>
 *   <li>{@code GET /catalog/{sku}} → 200 with the product JSON, 404 when the SKU is unstocked,
 *       400 when the SKU text is malformed</li>
 *   <li>{@code GET /health} → 200 liveness with the lookup counter (the observability surface)</li>
 * </ul>
 *
 * <p>Implements {@link AutoCloseable} so it can be used in a try-with-resources block in {@code main}
 * and in tests.
 */
public final class CatalogApi implements AutoCloseable {

    private static final Logger LOG = System.getLogger(CatalogApi.class.getName());
    private static final String CATALOG_PREFIX = "/catalog/";

    private final HttpServer server;
    private final ExecutorService executor;
    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong lookups = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();

    /**
     * Creates (but does not start) a catalog server bound to {@code port} ({@code 0} for an ephemeral
     * port, which tests use to avoid clashes).
     *
     * @param port the TCP port to bind, or {@code 0} for an ephemeral one
     * @throws IllegalStateException if the port cannot be bound
     */
    public CatalogApi(int port) {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new IllegalStateException("could not bind catalog on port " + port, e);
        }
        this.server.setExecutor(executor);
        this.server.createContext("/", this::dispatch);
    }

    /**
     * Stocks a product, so a lookup for its SKU returns it.
     *
     * @param product the product to stock, never {@code null}
     * @return this server, for chaining
     */
    public CatalogApi stock(Product product) {
        Objects.requireNonNull(product, "product");
        products.put(product.sku().format(), product);
        return this;
    }

    /** Starts accepting requests. @return this server, for chaining */
    public CatalogApi start() {
        server.start();
        LOG.log(Level.INFO, () -> "catalog listening on port " + port());
        return this;
    }

    /**
     * Returns the bound port (useful after binding to {@code 0}).
     *
     * @return the actual TCP port the server is listening on
     */
    public int port() {
        return server.getAddress().getPort();
    }

    /**
     * Observability surface: the running count of lookups served since startup.
     *
     * @return the number of {@code GET /catalog/*} requests handled, never negative
     */
    public long lookupCount() {
        return lookups.get();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.close();
    }

    private void dispatch(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        try {
            if ("/health".equals(path)) {
                writeJson(exchange, 200, health());
                return;
            }
            if (path.startsWith(CATALOG_PREFIX) && "GET".equals(exchange.getRequestMethod())) {
                handleLookup(exchange, path.substring(CATALOG_PREFIX.length()));
                return;
            }
            writeJson(exchange, 404, Map.of("error", "not-found", "path", path));
        } catch (RuntimeException e) {
            LOG.log(Level.ERROR, () -> "catalog failed handling " + path, e);
            writeJson(exchange, 500, Map.of("error", "internal-error"));
        }
    }

    private void handleLookup(HttpExchange exchange, String rawSku) throws IOException {
        lookups.incrementAndGet();
        Sku sku;
        try {
            sku = Sku.parse(rawSku);
        } catch (IllegalArgumentException e) {
            writeJson(exchange, 400, Map.of("error", "bad-sku", "detail", e.getMessage()));
            return;
        }
        Optional<Product> found = Optional.ofNullable(products.get(sku.format()));
        if (found.isEmpty()) {
            misses.incrementAndGet();
            writeJson(exchange, 404, Map.of("error", "unknown-sku", "sku", sku.format()));
            return;
        }
        writeJson(exchange, 200, toJsonMap(found.get()));
    }

    private Map<String, Object> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("stocked", products.size());
        body.put("lookups", lookups.get());
        body.put("misses", misses.get());
        return body;
    }

    private static Map<String, Object> toJsonMap(Product product) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("sku", product.sku().format());
        body.put("name", product.name());
        body.put("priceMinor", product.price().minorUnits());
        body.put("currency", product.price().currency());
        return body;
    }

    private static void writeJson(HttpExchange exchange, int status, Map<String, ?> body) throws IOException {
        byte[] bytes = Json.write(body).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
