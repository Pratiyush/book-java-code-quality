package org.acme.commerce.payment;

import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.json.Json;
import org.acme.platform.money.Money;

/**
 * A validated authorization request (Part: input validation). The factory enforces every invariant
 * once, at the boundary, so the rest of the service can trust the values: a positive amount, a
 * present card number, and a non-blank idempotency key (the field that makes a retry safe).
 *
 * @param orderId        the order to charge
 * @param amount         the amount to authorize (must be positive)
 * @param pan            the card primary account number (test data only)
 * @param idempotencyKey the client-supplied key that makes a retry return the first result
 */
public record PaymentRequest(String orderId, Money amount, String pan, String idempotencyKey) {

    /** Parses and validates a request from a JSON body, failing fast with a 400/422 problem. */
    public static PaymentRequest fromJson(Map<String, Object> body) {
        String orderId = requireText(body, "orderId");
        String pan = requireText(body, "pan");
        String idempotencyKey = requireText(body, "idempotencyKey");
        String currency = requireText(body, "currency");
        long amountMinor = Json.requireLong(body, "amountMinor");
        if (amountMinor <= 0L) {
            throw ApiException.unprocessable("amount-not-positive", "amountMinor must be positive");
        }
        return new PaymentRequest(orderId, Money.of(amountMinor, currency), pan, idempotencyKey);
    }

    private static String requireText(Map<String, Object> body, String field) {
        Object value = body.get(field);
        if (value instanceof String s && !s.isBlank()) {
            return s;
        }
        throw ApiException.badRequest("field-missing", "missing or blank field: " + field);
    }
}
