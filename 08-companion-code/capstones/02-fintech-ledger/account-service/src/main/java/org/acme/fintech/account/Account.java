package org.acme.fintech.account;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A ledger account (Part: value modelling). An account is identity plus the currency it holds; the
 * balance is deliberately NOT here — a balance is derived from the ledger's postings, never stored on
 * the account, so the two can never disagree.
 *
 * @param id       the account identifier
 * @param owner    the owning party's name
 * @param currency the ISO-4217 currency this account holds
 */
public record Account(String id, String owner, String currency) {

    public Account {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(currency, "currency");
        if (currency.length() != 3) {
            throw new IllegalArgumentException("currency must be a 3-letter ISO-4217 code: " + currency);
        }
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("owner", owner);
        body.put("currency", currency);
        return body;
    }
}
