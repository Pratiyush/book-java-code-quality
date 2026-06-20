package org.acme.fintech.transfer;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.json.Json;

/**
 * The HTTP adapter to ledger-service (Part: service composition). It reads an account balance and
 * posts the transfer as a single balanced journal entry — a negative line on the source and an equal
 * positive line on the destination — forwarding the transfer reference so the ledger applies it once.
 */
public final class LedgerClient implements LedgerPort {

    private final ServiceClient client;
    private final URI ledgerBaseUri;

    public LedgerClient(ServiceClient client, URI ledgerBaseUri) {
        this.client = client;
        this.ledgerBaseUri = ledgerBaseUri;
    }

    @Override
    public long balanceOf(String accountId) {
        ServiceClient.Reply reply = client.getJson(ledgerBaseUri.resolve("/accounts/" + accountId + "/balance"));
        if (!reply.isSuccess()) {
            throw new IllegalStateException("ledger-service error: HTTP " + reply.status());
        }
        return Json.requireLong(reply.jsonObject(), "balanceMinor");
    }

    @Override
    public void postTransfer(String reference, String fromAccount, String toAccount, long amountMinor,
                             String currency) {
        Map<String, Object> entry = Map.of(
            "reference", reference,
            "currency", currency,
            "lines", List.of(
                Map.of("accountId", fromAccount, "amountMinor", -amountMinor),
                Map.of("accountId", toAccount, "amountMinor", amountMinor)));
        ServiceClient.Reply reply = client.postJson(ledgerBaseUri.resolve("/entries"), entry);
        if (reply.status() != 201) {
            throw new IllegalStateException("ledger post failed: HTTP " + reply.status());
        }
    }
}
