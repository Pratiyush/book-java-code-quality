package org.acme.commerce.order;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.json.Json;
import org.acme.platform.money.Money;

/**
 * The HTTP adapter that authorizes a charge by calling payment-service (Part: service composition).
 * It forwards the order's idempotency key so a retried payment is charged at most once — the
 * guarantee lives in payment-service; this adapter's job is to pass the key through and read back the
 * decision, treating a 200 as approved and a 402 as declined.
 */
public final class PaymentGatewayClient implements PaymentPort {

    private final ServiceClient client;
    private final URI paymentBaseUri;

    public PaymentGatewayClient(ServiceClient client, URI paymentBaseUri) {
        this.client = client;
        this.paymentBaseUri = paymentBaseUri;
    }

    @Override
    public PaymentDecision authorize(String orderId, Money amount, String pan, String idempotencyKey) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("orderId", orderId);
        request.put("amountMinor", amount.minorUnits());
        request.put("currency", amount.currency());
        request.put("pan", pan);
        request.put("idempotencyKey", idempotencyKey);

        ServiceClient.Reply reply = client.postJson(paymentBaseUri.resolve("/payments"), request);
        if (reply.status() != 200 && reply.status() != 402) {
            throw new IllegalStateException("payment-service error: HTTP " + reply.status());
        }
        Map<String, Object> body = reply.jsonObject();
        boolean approved = reply.status() == 200;
        return new PaymentDecision(approved, Json.requireString(body, "id"), Json.requireString(body, "detail"));
    }
}
