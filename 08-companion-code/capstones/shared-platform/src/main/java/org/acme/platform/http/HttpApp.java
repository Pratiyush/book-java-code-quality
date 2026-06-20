package org.acme.platform.http;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.acme.platform.error.ApiException;
import org.acme.platform.error.ProblemDetails;
import org.acme.platform.json.Json;
import org.acme.platform.obs.Metrics;

/**
 * A small HTTP application built on the JDK's own {@link HttpServer} with a virtual-thread-per-request
 * executor (Part: concurrency) — so every capstone service runs with no web framework and no runtime
 * dependency. Register routes with {@link #get}, {@link #post}, {@link #put}, {@link #delete}; the
 * app owns the socket, dispatching, JSON encoding, metrics, and a uniform RFC 7807 error mapping, so
 * a {@link Handler} stays pure application logic.
 *
 * <p>Two endpoints are always present: {@code GET /health} (liveness) and {@code GET /metrics}
 * (the counter snapshot). Unmapped failures become {@code 500}; an {@link ApiException} carries its
 * own status; a malformed body or bad argument becomes {@code 400}.
 *
 * <p>Implements {@link AutoCloseable} for try-with-resources in {@code main} and in tests.
 */
public final class HttpApp implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(HttpApp.class.getName());

    private final String name;
    private final HttpServer server;
    private final java.util.concurrent.ExecutorService executor;
    private final List<Route> routes = new ArrayList<>();
    private final Metrics metrics = new Metrics();

    /**
     * Creates (but does not start) an app bound to {@code port} ({@code 0} for an ephemeral port,
     * which tests use to avoid clashes).
     */
    public HttpApp(String name, int port) {
        this.name = Objects.requireNonNull(name, "name");
        this.executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor();
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new IllegalStateException("could not bind " + name + " on port " + port, e);
        }
        this.server.setExecutor(executor);
    }

    public HttpApp get(String template, Handler handler) {
        return route("GET", template, handler);
    }

    public HttpApp post(String template, Handler handler) {
        return route("POST", template, handler);
    }

    public HttpApp put(String template, Handler handler) {
        return route("PUT", template, handler);
    }

    public HttpApp delete(String template, Handler handler) {
        return route("DELETE", template, handler);
    }

    public HttpApp route(String method, String template, Handler handler) {
        routes.add(new Route(method, template, handler));
        return this;
    }

    public Metrics metrics() {
        return metrics;
    }

    /** Registers the dispatcher and built-in endpoints, then starts accepting requests. */
    public HttpApp start() {
        server.createContext("/", this::dispatch);
        server.start();
        LOG.log(Level.INFO, () -> name + " listening on port " + port());
        return this;
    }

    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.close();
    }

    private void dispatch(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        metrics.increment("http.requests");
        try {
            Response response = resolve(method, path, exchange);
            write(exchange, response);
            metrics.increment("http.status." + response.status());
        } catch (ApiException e) {
            write(exchange, Response.problem(e.problem()));
            metrics.increment("http.status." + e.problem().status());
        } catch (Json.JsonParseException | IllegalArgumentException e) {
            write(exchange, Response.problem(
                ProblemDetails.of(400, "bad-request", "Bad Request", e.getMessage())));
            metrics.increment("http.status.400");
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, e, () -> name + " failed handling " + method + " " + path);
            write(exchange, Response.problem(
                ProblemDetails.of(500, "internal-error", "Internal Server Error", "unexpected error")));
            metrics.increment("http.status.500");
        }
    }

    private Response resolve(String method, String path, HttpExchange exchange) throws IOException {
        if ("GET".equals(method) && "/health".equals(path)) {
            return health();
        }
        if ("GET".equals(method) && "/metrics".equals(path)) {
            return Response.ok(metrics.snapshot());
        }
        String[] segments = Route.split(path);
        boolean pathMatchedWrongMethod = false;
        for (Route candidate : routes) {
            Map<String, String> params = candidate.match(segments);
            if (params == null) {
                continue;
            }
            if (!candidate.method().equals(method)) {
                pathMatchedWrongMethod = true;
                continue;
            }
            Request request = new Request(method, path, params, query(exchange), body(exchange));
            return candidate.handler().handle(request);
        }
        if (pathMatchedWrongMethod) {
            return Response.problem(
                ProblemDetails.of(405, "method-not-allowed", "Method Not Allowed", method + " " + path));
        }
        return Response.problem(
            ProblemDetails.of(404, "not-found", "Not Found", "no route for " + method + " " + path));
    }

    private Response health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("service", name);
        body.put("requests", metrics.count("http.requests"));
        return Response.ok(body);
    }

    private static String body(HttpExchange exchange) throws IOException {
        byte[] bytes = exchange.getRequestBody().readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static Map<String, String> query(HttpExchange exchange) {
        Map<String, String> params = new LinkedHashMap<>();
        String raw = exchange.getRequestURI().getRawQuery();
        if (raw == null || raw.isEmpty()) {
            return params;
        }
        for (String pair : raw.split("&")) {
            int eq = pair.indexOf('=');
            if (eq > 0) {
                params.put(
                    java.net.URLDecoder.decode(pair.substring(0, eq), StandardCharsets.UTF_8),
                    java.net.URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8));
            }
        }
        return params;
    }

    private static void write(HttpExchange exchange, Response response) throws IOException {
        byte[] bytes = response.body().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", response.contentType());
        // A 204/empty body must send -1 length, not 0, per the HttpServer contract.
        int length = bytes.length == 0 ? -1 : bytes.length;
        exchange.sendResponseHeaders(response.status(), length == -1 ? -1 : bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            if (bytes.length > 0) {
                out.write(bytes);
            }
        }
    }
}
