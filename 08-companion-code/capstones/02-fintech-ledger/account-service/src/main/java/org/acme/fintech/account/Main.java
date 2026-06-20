package org.acme.fintech.account;

import org.acme.platform.config.Config;
import org.acme.platform.http.HttpApp;

/** The account-service entry point — wiring only. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Config config = Config.fromEnvironment();
        int port = config.integer("port", 8091);
        AccountService service = new AccountService(new InMemoryAccountRepository());
        HttpApp app = AccountApi.newApp(service, port);
        app.start();
    }
}
