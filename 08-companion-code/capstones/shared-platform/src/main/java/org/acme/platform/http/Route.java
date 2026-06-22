package org.acme.platform.http;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * One registered route: an HTTP method, a path template that may contain {@code {param}} segments,
 * and the {@link Handler} to invoke. Matching is exact on segment count and on every literal
 * segment; a {@code {name}} segment captures its value into the path parameters.
 */
final class Route {

    private final String method;
    private final String[] template;
    private final Handler handler;

    Route(String method, String pathTemplate, Handler handler) {
        this.method = method;
        this.template = split(pathTemplate);
        this.handler = handler;
    }

    String method() {
        return method;
    }

    Handler handler() {
        return handler;
    }

    static String[] split(String path) {
        String trimmed = path;
        while (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1);
        }
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        if (trimmed.isEmpty()) {
            return new String[0];
        }
        return trimmed.split("/");
    }

    /** Returns the captured path parameters if this route's template matches, else {@code null}. */
    Map<String, String> match(String[] requestSegments) {
        if (requestSegments.length != template.length) {
            return null;
        }
        Map<String, String> params = new LinkedHashMap<>();
        for (int i = 0; i < template.length; i++) {
            String segment = template[i];
            if (segment.startsWith("{") && segment.endsWith("}")) {
                params.put(segment.substring(1, segment.length() - 1), requestSegments[i]);
            } else if (!segment.equals(requestSegments[i])) {
                return null;
            }
        }
        return params;
    }
}
