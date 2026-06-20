package org.acme.fintech.ledger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Request;
import org.acme.platform.http.Response;
import org.acme.platform.json.Json;

/**
 * The HTTP surface of ledger-service (Part: API design). An unbalanced set of lines is well-formed
 * JSON but cannot become a {@link JournalEntry}, so it maps to 422 Unprocessable Entity — distinct
 * from a 400 for malformed input.
 *
 * <ul>
 *   <li>{@code POST /entries}                  → 201 posted, 422 when the lines do not balance
 *   <li>{@code GET  /entries/{reference}}      → 200 an entry, 404 unknown
 *   <li>{@code GET  /accounts/{id}/balance}    → 200 the account balance in minor units
 * </ul>
 */
public final class LedgerApi {

    private LedgerApi() {
    }

    public static HttpApp newApp(LedgerService service, int port) {
        HttpApp app = new HttpApp("ledger-service", port);
        app.post("/entries", request -> postEntry(service, request));
        app.get("/entries/{reference}", request -> {
            JournalEntry entry = service.find(request.pathParam("reference"))
                .orElseThrow(() -> ApiException.notFound("entry-unknown",
                    "no entry with reference " + request.pathParam("reference")));
            return Response.ok(entry.toBody());
        });
        app.get("/accounts/{id}/balance", request -> Response.ok(Map.of(
            "accountId", request.pathParam("id"),
            "balanceMinor", service.balance(request.pathParam("id")))));
        return app;
    }

    private static Response postEntry(LedgerService service, Request request) {
        Map<String, Object> body = request.jsonBody();
        String reference = Json.requireString(body, "reference");
        String currency = Json.requireString(body, "currency");
        List<PostingLine> lines = parseLines(body);
        try {
            return Response.created(service.post(reference, currency, lines).toBody());
        } catch (IllegalArgumentException e) {
            throw ApiException.unprocessable("entry-unbalanced", e.getMessage());
        }
    }

    private static List<PostingLine> parseLines(Map<String, Object> body) {
        Object raw = body.get("lines");
        if (!(raw instanceof List<?> list) || list.isEmpty()) {
            throw ApiException.badRequest("lines-missing", "'lines' must be a non-empty array");
        }
        List<PostingLine> lines = new ArrayList<>();
        for (Object element : list) {
            if (!(element instanceof Map<?, ?> line)) {
                throw ApiException.badRequest("line-malformed", "each line must be an object");
            }
            Object accountId = line.get("accountId");
            Object amount = line.get("amountMinor");
            if (!(accountId instanceof String account) || account.isBlank() || !(amount instanceof Number n)) {
                throw ApiException.badRequest("line-malformed", "each line needs accountId and amountMinor");
            }
            lines.add(new PostingLine(account, n.longValue()));
        }
        return lines;
    }
}
