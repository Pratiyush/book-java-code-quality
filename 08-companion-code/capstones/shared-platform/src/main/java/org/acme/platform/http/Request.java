package org.acme.platform.http;

import java.util.Map;
import java.util.Optional;
import org.acme.platform.json.Json;

/**
 * An immutable view of an inbound HTTP request (Part: API design). It exposes only what a handler
 * needs — the path parameters captured from the route template, the query string, and the body —
 * through accessor methods, never the backing maps, so a handler cannot accidentally mutate request
 * state. The body is decoded once into a string and parsed on demand.
 */
public final class Request {

    private final String method;
    private final String path;
    private final Map<String, String> pathParams;
    private final Map<String, String> queryParams;
    private final String body;

    Request(String method, String path, Map<String, String> pathParams,
            Map<String, String> queryParams, String body) {
        this.method = method;
        this.path = path;
        this.pathParams = Map.copyOf(pathParams);
        this.queryParams = Map.copyOf(queryParams);
        this.body = body;
    }

    public String method() {
        return method;
    }

    public String path() {
        return path;
    }

    /** A required path parameter captured by the route template (e.g. {@code {id}}). */
    public String pathParam(String name) {
        String value = pathParams.get(name);
        if (value == null) {
            throw new IllegalArgumentException("no such path parameter: " + name);
        }
        return value;
    }

    public Optional<String> queryParam(String name) {
        return Optional.ofNullable(queryParams.get(name));
    }

    public String rawBody() {
        return body;
    }

    /** Parses the body as a JSON object, failing fast when it is absent or malformed. */
    public Map<String, Object> jsonBody() {
        if (body == null || body.isBlank()) {
            throw new Json.JsonParseException("empty request body");
        }
        return Json.parseObject(body);
    }
}
