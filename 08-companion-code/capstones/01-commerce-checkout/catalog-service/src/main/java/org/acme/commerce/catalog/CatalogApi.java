package org.acme.commerce.catalog;

import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;

/**
 * The HTTP surface of catalog-service (Part: API design). It maps two read routes onto
 * {@link CatalogService} and nothing more — no business logic lives here, so the same service can be
 * driven by a different transport without change.
 *
 * <ul>
 *   <li>{@code GET /products}        → 200 the catalog
 *   <li>{@code GET /products/{id}}   → 200 a product, 404 when unknown
 * </ul>
 */
public final class CatalogApi {

    private CatalogApi() {
    }

    /** Builds (but does not start) the configured app on the given port. */
    public static HttpApp newApp(CatalogService service, int port) {
        HttpApp app = new HttpApp("catalog-service", port);
        app.get("/products", request ->
            Response.ok(service.list().stream().map(Product::toBody).toList()));
        app.get("/products/{id}", request ->
            Response.ok(service.require(request.pathParam("id")).toBody()));
        return app;
    }
}
