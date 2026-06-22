package org.acme.fintech.ledger;

import java.util.List;
import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/**
 * The ledger-service entry point. After wiring, it posts a single balanced opening entry so the demo
 * accounts have funds: it credits Alice and Bob and debits the house equity account by the same
 * total, keeping the books balanced from the first instant.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8092);
        LedgerService service = new LedgerService(new InMemoryLedgerRepository(), System::currentTimeMillis);
        seedOpeningBalances(service);
        HttpApp app = LedgerApi.newApp(service, port);
        app.start();
    }

    static void seedOpeningBalances(LedgerService service) {
        service.post("opening-0001", "USD", List.of(
            new PostingLine("acc-alice", 100_000L),
            new PostingLine("acc-bob", 50_000L),
            new PostingLine("acc-house", -150_000L)));
    }
}
