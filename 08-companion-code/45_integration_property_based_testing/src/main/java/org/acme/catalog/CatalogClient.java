package org.acme.catalog;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * A real outbound client for the product catalog, built on the JDK's own {@link HttpClient}.
 *
 * <p>This is the collaborator the integration test exercises against a real {@link CatalogApi}: every
 * call goes over real HTTP, so the test covers the wire encoding, the real status-code mapping, and the
 * real parse — the fidelity a mocked client cannot give. A unit test would stub this client and assert
 * a result; the integration test runs it for real and so catches an encoding or status-handling bug
 * the stub would hide.
 *
 * <p>The request timeout is the client-side failure edge: a slow or unreachable catalog surfaces as a
 * timeout rather than an indefinite hang, mapped to a typed {@link CatalogLookupException}.
 */
public final class CatalogClient {

    private final HttpClient http;
    private final URI baseUri;
    private final Duration requestTimeout;

    /**
     * Creates a client against the catalog at {@code baseUri} with the given per-request timeout.
     *
     * @param baseUri        the catalog's base URI (for example {@code http://localhost:8080}),
     *                       never {@code null}
     * @param requestTimeout the per-request timeout, never {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
    public CatalogClient(URI baseUri, Duration requestTimeout) {
        this.baseUri = Objects.requireNonNull(baseUri, "baseUri");
        this.requestTimeout = Objects.requireNonNull(requestTimeout, "requestTimeout");
        this.http = HttpClient.newBuilder()
            .connectTimeout(requestTimeout)
            .build();
    }

    /**
     * Looks up a product by SKU over real HTTP.
     *
     * @param sku the SKU to look up, never {@code null}
     * @return the product the catalog returned, never {@code null}
     * @throws NullPointerException     if {@code sku} is {@code null}
     * @throws CatalogLookupException   if the SKU is unstocked ({@code 404}) or the server returns any
     *                                  non-success status, or the request times out / fails
     */
    public Product lookup(Sku sku) {
        Objects.requireNonNull(sku, "sku");
        HttpRequest request = HttpRequest.newBuilder()
            .uri(baseUri.resolve("/catalog/" + sku.format()))
            .timeout(requestTimeout)
            .GET()
            .build();
        HttpResponse<String> response = send(request, sku);
        if (response.statusCode() != 200) {
            throw new CatalogLookupException(response.statusCode(),
                "catalog lookup for " + sku.format() + " returned " + response.statusCode());
        }
        return fromJson(response.body());
    }

    private HttpResponse<String> send(HttpRequest request, Sku sku) {
        try {
            return http.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (java.io.IOException e) {
            throw new CatalogLookupException(503, "catalog unreachable for " + sku.format() + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CatalogLookupException(503, "catalog lookup interrupted for " + sku.format());
        }
    }

    private static Product fromJson(String body) {
        Map<String, String> fields = Json.readObject(body);
        return new Product(
            Sku.parse(fields.get("sku")),
            fields.get("name"),
            new Money(Long.parseLong(fields.get("priceMinor")), fields.get("currency")));
    }
}
