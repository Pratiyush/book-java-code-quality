package org.acme.fintech.transfer;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.json.Json;

/** The HTTP adapter to account-service (Part: service composition). */
public final class AccountLookupClient implements AccountPort {

    private final ServiceClient client;
    private final URI accountBaseUri;

    public AccountLookupClient(ServiceClient client, URI accountBaseUri) {
        this.client = client;
        this.accountBaseUri = accountBaseUri;
    }

    @Override
    public Optional<AccountInfo> lookup(String accountId) {
        ServiceClient.Reply reply = client.getJson(accountBaseUri.resolve("/accounts/" + accountId));
        if (reply.status() == 404) {
            return Optional.empty();
        }
        if (!reply.isSuccess()) {
            throw new IllegalStateException("account-service error: HTTP " + reply.status());
        }
        Map<String, Object> body = reply.jsonObject();
        return Optional.of(new AccountInfo(Json.requireString(body, "id"), Json.requireString(body, "currency")));
    }
}
