package org.acme.platform.http;

import org.acme.platform.error.ProblemDetails;
import org.acme.platform.json.Json;

/**
 * An immutable HTTP response (Part: API design). The body is serialized to its final string form at
 * construction, so a {@code Response} holds no mutable state and is safe to pass around or build in
 * one place and write in another. Factories cover the shapes the capstones return: a JSON body, an
 * RFC 7807 problem, and an empty status.
 *
 * @param status      the HTTP status code
 * @param contentType the {@code Content-Type} header value
 * @param body        the already-serialized response body (may be empty)
 */
public record Response(int status, String contentType, String body) {

    private static final String JSON_TYPE = "application/json; charset=utf-8";
    private static final String PROBLEM_TYPE = "application/problem+json; charset=utf-8";

    /** A JSON response; {@code value} is serialized immediately by {@link Json#write}. */
    public static Response json(int status, Object value) {
        return new Response(status, JSON_TYPE, Json.write(value));
    }

    /** A {@code 200 OK} JSON response. */
    public static Response ok(Object value) {
        return json(200, value);
    }

    /** A {@code 201 Created} JSON response. */
    public static Response created(Object value) {
        return json(201, value);
    }

    /** An RFC 7807 problem response; the status is taken from the problem. */
    public static Response problem(ProblemDetails problem) {
        return new Response(problem.status(), PROBLEM_TYPE, Json.write(problem.toBody()));
    }

    /** An empty-bodied response carrying only a status (e.g. {@code 204 No Content}). */
    public static Response status(int status) {
        return new Response(status, JSON_TYPE, "");
    }
}
