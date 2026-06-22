package org.acme.platform.error;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An RFC 7807 {@code application/problem+json} error body (Part: API error contracts). One uniform
 * error shape across every capstone service means a client parses failures the same way regardless
 * of which service produced them — the opposite of each endpoint inventing its own error JSON.
 *
 * @param type   a URI reference identifying the problem kind (here, a stable slug)
 * @param title  a short, human-readable summary that does not change between occurrences
 * @param status the HTTP status code
 * @param detail an occurrence-specific explanation
 */
public record ProblemDetails(String type, String title, int status, String detail) {

    public static ProblemDetails of(int status, String type, String title, String detail) {
        return new ProblemDetails(type, title, status, detail);
    }

    /** The JSON object body, ready for {@code org.acme.platform.json.Json.write}. */
    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", type);
        body.put("title", title);
        body.put("status", status);
        body.put("detail", detail);
        return body;
    }
}
