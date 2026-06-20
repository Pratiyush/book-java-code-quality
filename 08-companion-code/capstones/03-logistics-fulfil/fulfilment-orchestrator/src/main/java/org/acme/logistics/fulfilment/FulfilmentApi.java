package org.acme.logistics.fulfilment;

import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;

/**
 * The HTTP surface of fulfilment-orchestrator (Part: API design). A fresh fulfilment returns 201; an
 * idempotent replay returns 200; an out-of-stock order returns 409 after the saga has released any
 * lines it reserved.
 *
 * <ul>
 *   <li>{@code POST /fulfilments}            → 201 fulfilled · 200 replay · 409 out of stock
 *   <li>{@code GET  /fulfilments/{orderId}}  → 200 a fulfilment · 404 unknown
 * </ul>
 */
public final class FulfilmentApi {

    private FulfilmentApi() {
    }

    public static HttpApp newApp(FulfilmentService service, int port) {
        HttpApp app = new HttpApp("fulfilment-orchestrator", port);
        app.post("/fulfilments", request -> {
            FulfilmentService.FulfilResult result = service.fulfil(FulfilmentRequest.fromJson(request.jsonBody()));
            int status = result.replayed() ? 200 : 201;
            return Response.json(status, result.fulfilment().toBody());
        });
        app.get("/fulfilments/{orderId}", request -> {
            Fulfilment fulfilment = service.find(request.pathParam("orderId"))
                .orElseThrow(() -> ApiException.notFound("fulfilment-unknown",
                    "no fulfilment for order " + request.pathParam("orderId")));
            return Response.ok(fulfilment.toBody());
        });
        return app;
    }
}
