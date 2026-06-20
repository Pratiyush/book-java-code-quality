package org.acme.commerce.payment;

import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;

/**
 * The HTTP surface of payment-service (Part: API design). An approved authorization returns 200; a
 * declined one returns 402 Payment Required with the reason — distinct statuses so a caller branches
 * on the HTTP status, not on parsing a string.
 *
 * <ul>
 *   <li>{@code POST /payments}       → 200 approved, 402 declined, 400/422 invalid request
 *   <li>{@code GET  /payments/{id}}  → 200 a payment, 404 when unknown
 * </ul>
 */
public final class PaymentApi {

    private PaymentApi() {
    }

    public static HttpApp newApp(PaymentService service, int port) {
        HttpApp app = new HttpApp("payment-service", port);
        app.post("/payments", request -> {
            Payment payment = service.authorize(PaymentRequest.fromJson(request.jsonBody()));
            int status = payment.isApproved() ? 200 : 402;
            return Response.json(status, payment.toBody());
        });
        app.get("/payments/{id}", request -> {
            Payment payment = service.find(request.pathParam("id"))
                .orElseThrow(() -> ApiException.notFound("payment-unknown",
                    "no payment with id " + request.pathParam("id")));
            return Response.ok(payment.toBody());
        });
        return app;
    }
}
