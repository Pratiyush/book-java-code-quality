package org.acme.fintech.transfer;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;
import org.junit.jupiter.api.Test;

/**
 * Exercises transfer-service's real outbound adapters ({@link AccountLookupClient},
 * {@link LedgerClient}) over real HTTP, against shared-platform stubs standing in for account-service
 * and ledger-service. The stub ledger applies and idempotently de-duplicates entries by reference, so
 * the full money-movement contract is tested end to end without depending on the other modules.
 */
class TransferFlowIntegrationTest {

    @Test
    void transfersThenReplaysThenRejectsOverdraft() {
        try (HttpApp accounts = stubAccounts();
             HttpApp ledger = stubLedger();
             HttpApp transfers = transferApp(accounts, ledger)) {

            ServiceClient client = ServiceClient.withDefaults();
            URI base = URI.create("http://localhost:" + transfers.port());

            ServiceClient.Reply first = client.postJson(base.resolve("/transfers"), Map.of(
                "reference", "t-int-1", "fromAccount", "acc-alice", "toAccount", "acc-bob",
                "amountMinor", 25_000L, "currency", "USD"));
            assertThat(first.status()).isEqualTo(201);

            ServiceClient.Reply replay = client.postJson(base.resolve("/transfers"), Map.of(
                "reference", "t-int-1", "fromAccount", "acc-alice", "toAccount", "acc-bob",
                "amountMinor", 25_000L, "currency", "USD"));
            assertThat(replay.status()).isEqualTo(200);

            ServiceClient.Reply overdraft = client.postJson(base.resolve("/transfers"), Map.of(
                "reference", "t-int-2", "fromAccount", "acc-alice", "toAccount", "acc-bob",
                "amountMinor", 1_000_000L, "currency", "USD"));
            assertThat(overdraft.status()).isEqualTo(402);
        }
    }

    private static HttpApp stubAccounts() {
        Map<String, String> currencyById = Map.of("acc-alice", "USD", "acc-bob", "USD");
        HttpApp app = new HttpApp("stub-accounts", 0);
        app.get("/accounts/{id}", request -> {
            String id = request.pathParam("id");
            String currency = currencyById.get(id);
            if (currency == null) {
                return Response.status(404);
            }
            return Response.ok(Map.of("id", id, "owner", "stub", "currency", currency));
        });
        return app.start();
    }

    private static HttpApp stubLedger() {
        ConcurrentHashMap<String, Long> balances = new ConcurrentHashMap<>(Map.of("acc-alice", 100_000L));
        ConcurrentHashMap<String, Boolean> seen = new ConcurrentHashMap<>();
        HttpApp app = new HttpApp("stub-ledger", 0);
        app.get("/accounts/{id}/balance", request -> Response.ok(Map.of(
            "accountId", request.pathParam("id"),
            "balanceMinor", balances.getOrDefault(request.pathParam("id"), 0L))));
        app.post("/entries", request -> {
            Map<String, Object> body = request.jsonBody();
            String reference = String.valueOf(body.get("reference"));
            if (seen.putIfAbsent(reference, Boolean.TRUE) == null && body.get("lines") instanceof List<?> lines) {
                for (Object element : lines) {
                    if (element instanceof Map<?, ?> line && line.get("accountId") instanceof String account
                        && line.get("amountMinor") instanceof Number amount) {
                        balances.merge(account, amount.longValue(), Long::sum);
                    }
                }
            }
            return Response.status(201);
        });
        return app.start();
    }

    private static HttpApp transferApp(HttpApp accounts, HttpApp ledger) {
        ServiceClient client = ServiceClient.withDefaults();
        TransferService service = new TransferService(
            new InMemoryTransferRepository(),
            new AccountLookupClient(client, URI.create("http://localhost:" + accounts.port())),
            new LedgerClient(client, URI.create("http://localhost:" + ledger.port())));
        return TransferApi.newApp(service, 0).start();
    }
}
