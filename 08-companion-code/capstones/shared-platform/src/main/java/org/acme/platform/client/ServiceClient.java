package org.acme.platform.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import org.acme.platform.json.Json;

/**
 * A thin JSON client for service-to-service calls (Part: resilience). One capstone service calls
 * another over plain HTTP through this client rather than reaching into the other's code — the
 * boundary that makes them separate services. Built on the JDK's {@link HttpClient}; every call has
 * a connect and a request timeout so a slow dependency surfaces as a timeout rather than a hung
 * request thread.
 *
 * <p><strong>Honest limitation.</strong> This client does the call and the timeout, and nothing
 * more: no retry, no circuit breaker, no bulkhead. Those belong on this seam in production (the
 * resilience chapter discusses them); they are deliberately omitted here so the boundary stays
 * legible.
 */
public final class ServiceClient {

    private final HttpClient http;
    private final Duration requestTimeout;

    public ServiceClient(Duration connectTimeout, Duration requestTimeout) {
        this.http = HttpClient.newBuilder().connectTimeout(connectTimeout).build();
        this.requestTimeout = requestTimeout;
    }

    public static ServiceClient withDefaults() {
        return new ServiceClient(Duration.ofSeconds(2), Duration.ofSeconds(5));
    }

    /** A GET returning the response status and parsed JSON object. */
    public Reply getJson(URI uri) {
        HttpRequest request = HttpRequest.newBuilder(uri)
            .timeout(requestTimeout)
            .header("Accept", "application/json")
            .GET()
            .build();
        return send(request);
    }

    /** A POST of a JSON object body, returning the response status and parsed JSON object. */
    public Reply postJson(URI uri, Object body) {
        HttpRequest request = HttpRequest.newBuilder(uri)
            .timeout(requestTimeout)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(Json.write(body)))
            .build();
        return send(request);
    }

    private Reply send(HttpRequest request) {
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            return new Reply(response.statusCode(), response.body());
        } catch (IOException e) {
            throw new ServiceCallException("call to " + request.uri() + " failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceCallException("call to " + request.uri() + " was interrupted", e);
        }
    }

    /** A service-to-service reply: the HTTP status and the raw body, parsed to JSON on demand. */
    public record Reply(int status, String body) {

        public boolean isSuccess() {
            return status >= 200 && status < 300;
        }

        public Map<String, Object> jsonObject() {
            return Json.parseObject(body);
        }
    }

    /** Thrown when a service-to-service call cannot complete (I/O failure, timeout, interrupt). */
    public static final class ServiceCallException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ServiceCallException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
