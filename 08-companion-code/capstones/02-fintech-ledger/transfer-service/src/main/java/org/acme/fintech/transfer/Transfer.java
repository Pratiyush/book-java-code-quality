package org.acme.fintech.transfer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A completed transfer record (Part: value modelling) — what transfer-service stores per reference so
 * a retry can return the original outcome rather than moving money again.
 *
 * @param reference   the idempotency reference
 * @param fromAccount the debited account
 * @param toAccount   the credited account
 * @param amountMinor the amount moved
 * @param currency    the currency
 */
public record Transfer(String reference, String fromAccount, String toAccount, long amountMinor,
                       String currency) {

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("reference", reference);
        body.put("fromAccount", fromAccount);
        body.put("toAccount", toAccount);
        body.put("amountMinor", amountMinor);
        body.put("currency", currency);
        body.put("status", "COMPLETED");
        return body;
    }
}
