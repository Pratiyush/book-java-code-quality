package org.acme.fintech.transfer;

import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.json.Json;

/**
 * A validated transfer instruction (Part: input validation). The factory enforces the rules a
 * transfer cannot break at the boundary: a present idempotency reference, two distinct accounts, and
 * a strictly positive amount — so the orchestrator never has to re-check them.
 *
 * @param reference   the idempotency reference (a retry with the same reference moves money once)
 * @param fromAccount the debited account
 * @param toAccount   the credited account
 * @param amountMinor the amount to move, in minor units (must be positive)
 * @param currency    the transfer currency
 */
public record TransferRequest(String reference, String fromAccount, String toAccount,
                              long amountMinor, String currency) {

    public static TransferRequest fromJson(Map<String, Object> body) {
        String reference = requireText(body, "reference");
        String from = requireText(body, "fromAccount");
        String to = requireText(body, "toAccount");
        String currency = requireText(body, "currency");
        long amount = Json.requireLong(body, "amountMinor");
        if (amount <= 0L) {
            throw ApiException.unprocessable("amount-not-positive", "amountMinor must be positive");
        }
        if (from.equals(to)) {
            throw ApiException.unprocessable("same-account", "fromAccount and toAccount must differ");
        }
        return new TransferRequest(reference, from, to, amount, currency);
    }

    private static String requireText(Map<String, Object> body, String field) {
        Object value = body.get(field);
        if (value instanceof String s && !s.isBlank()) {
            return s;
        }
        throw ApiException.badRequest("field-missing", "missing or blank field: " + field);
    }
}
