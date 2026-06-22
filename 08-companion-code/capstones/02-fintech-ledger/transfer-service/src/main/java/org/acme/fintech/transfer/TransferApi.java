package org.acme.fintech.transfer;

import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;

/**
 * The HTTP surface of transfer-service (Part: API design). A fresh transfer returns 201; an
 * idempotent replay of the same reference returns 200 — so a client can tell whether its retry did
 * the work or merely confirmed it.
 *
 * <ul>
 *   <li>{@code POST /transfers}              → 201 created · 200 replay · 402 insufficient funds ·
 *       404 unknown account · 409 currency mismatch · 422 invalid amount/same account
 *   <li>{@code GET  /transfers/{reference}}  → 200 a transfer · 404 unknown
 * </ul>
 */
public final class TransferApi {

    private TransferApi() {
    }

    public static HttpApp newApp(TransferService service, int port) {
        HttpApp app = new HttpApp("transfer-service", port);
        app.post("/transfers", request -> {
            TransferService.TransferResult result = service.transfer(TransferRequest.fromJson(request.jsonBody()));
            int status = result.replayed() ? 200 : 201;
            return Response.json(status, result.transfer().toBody());
        });
        app.get("/transfers/{reference}", request -> {
            Transfer transfer = service.find(request.pathParam("reference"))
                .orElseThrow(() -> ApiException.notFound("transfer-unknown",
                    "no transfer with reference " + request.pathParam("reference")));
            return Response.ok(transfer.toBody());
        });
        return app;
    }
}
