package org.acme.fintech.transfer;

import java.net.URI;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/**
 * The transfer-service entry point. Reads its port and the account/ledger base URLs from
 * configuration ({@code account.url}, {@code ledger.url}), wires the HTTP adapters, and starts.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8093);
        URI accountUri = URI.create(config.string("account.url", "http://localhost:8091"));
        URI ledgerUri = URI.create(config.string("ledger.url", "http://localhost:8092"));

        ServiceClient client = ServiceClient.withDefaults();
        TransferService service = new TransferService(
            new InMemoryTransferRepository(),
            new AccountLookupClient(client, accountUri),
            new LedgerClient(client, ledgerUri));

        HttpApp app = TransferApi.newApp(service, port);
        app.start();
    }
}
