package org.acme.fintech.ledger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * One line of a double-entry journal entry (Part: value modelling): an account and a signed amount
 * in minor units. A positive amount increases that account's balance, a negative amount decreases it.
 * A journal entry's lines must sum to zero — that is what makes the entry balanced.
 *
 * @param accountId   the account this line posts to
 * @param amountMinor the signed amount in minor units
 */
public record PostingLine(String accountId, long amountMinor) {

    public PostingLine {
        Objects.requireNonNull(accountId, "accountId");
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("accountId", accountId);
        body.put("amountMinor", amountMinor);
        return body;
    }
}
