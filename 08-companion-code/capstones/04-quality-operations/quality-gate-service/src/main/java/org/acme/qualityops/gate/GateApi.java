package org.acme.qualityops.gate;

import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Request;
import org.acme.platform.http.Response;
import org.acme.platform.json.Json;

/**
 * The HTTP surface of quality-gate-service (Part: API design). It parses the gate request, delegates
 * to {@link GateService}, and returns the decision.
 *
 * <ul>
 *   <li>{@code POST /gate}   → 200 the decision ({@code PASS} or {@code FAIL}) · 422 when the project
 *       has no metrics to gate on
 * </ul>
 *
 * <p>A {@code FAIL} is a {@code 200}, not an HTTP error: the gate evaluated successfully and the
 * verdict is in the body. An error status is reserved for a request the gate could not decide at all
 * (a malformed body is 400; a project with no metrics is 422).
 */
public final class GateApi {

    private GateApi() {
    }

    /** Builds (but does not start) the configured app on the given port. */
    public static HttpApp newApp(GateService service, int port) {
        HttpApp app = new HttpApp("quality-gate-service", port);
        app.post("/gate", request -> gate(service, request));
        return app;
    }

    private static Response gate(GateService service, Request request) {
        Map<String, Object> body = request.jsonBody();
        GateDecision decision = service.decide(
            Json.requireString(body, "project"),
            Json.requireString(body, "commit"),
            parsePolicy(body));
        return Response.ok(decision.toBody());
    }

    private static GatePolicy parsePolicy(Map<String, Object> body) {
        Object rawPolicy = body.get("policy");
        if (!(rawPolicy instanceof Map<?, ?> policy)) {
            throw ApiException.badRequest("policy-missing", "'policy' must be an object");
        }
        return new GatePolicy(
            intField(policy, "minCoverage"),
            intField(policy, "maxViolations"),
            intField(policy, "maxBugs"));
    }

    private static int intField(Map<?, ?> object, String field) {
        if (object.get(field) instanceof Number n) {
            return Math.toIntExact(n.longValue());
        }
        throw ApiException.badRequest("field-missing", "missing or non-numeric policy field: " + field);
    }
}
