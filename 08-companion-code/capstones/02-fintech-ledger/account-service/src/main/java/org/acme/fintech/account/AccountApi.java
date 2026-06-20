package org.acme.fintech.account;

import java.util.Map;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Response;
import org.acme.platform.json.Json;

/**
 * The HTTP surface of account-service (Part: API design).
 *
 * <ul>
 *   <li>{@code GET  /accounts}        → 200 all accounts
 *   <li>{@code GET  /accounts/{id}}   → 200 an account, 404 when unknown
 *   <li>{@code POST /accounts}        → 201 the opened account
 * </ul>
 */
public final class AccountApi {

    private AccountApi() {
    }

    public static HttpApp newApp(AccountService service, int port) {
        HttpApp app = new HttpApp("account-service", port);
        app.get("/accounts", request ->
            Response.ok(service.list().stream().map(Account::toBody).toList()));
        app.get("/accounts/{id}", request ->
            Response.ok(service.require(request.pathParam("id")).toBody()));
        app.post("/accounts", request -> {
            Map<String, Object> body = request.jsonBody();
            Account account = service.open(Json.requireString(body, "owner"), Json.requireString(body, "currency"));
            return Response.created(account.toBody());
        });
        return app;
    }
}
