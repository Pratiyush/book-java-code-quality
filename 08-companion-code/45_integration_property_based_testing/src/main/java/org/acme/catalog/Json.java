package org.acme.catalog;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A deliberately tiny JSON codec for flat string-keyed objects, just enough for the catalog payload,
 * so the module needs no JSON library on the runtime classpath.
 *
 * <p>It writes and reads a single-level object whose values are strings or numbers. It is not a
 * general JSON parser; it exists so the integration test exercises a real encode-over-the-wire and
 * decode-on-the-other-side without pulling a dependency the chapter does not discuss.
 */
final class Json {

    private Json() {
    }

    /**
     * Writes a flat object as JSON. String values are quoted and escaped; everything else is rendered
     * by {@link String#valueOf(Object)} (numbers, booleans).
     *
     * @param body the flat object to render, never {@code null}
     * @return the JSON text
     */
    static String write(Map<String, ?> body) {
        StringBuilder out = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, ?> entry : body.entrySet()) {
            if (!first) {
                out.append(',');
            }
            first = false;
            out.append('"').append(escape(entry.getKey())).append("\":");
            Object value = entry.getValue();
            if (value instanceof Number || value instanceof Boolean) {
                out.append(value);
            } else {
                out.append('"').append(escape(String.valueOf(value))).append('"');
            }
        }
        return out.append('}').toString();
    }

    /**
     * Reads a flat JSON object into a map of string values. Numbers are kept as their text form; the
     * caller parses what it needs. Nested objects and arrays are out of scope.
     *
     * @param text the JSON text to read, never {@code null}
     * @return a map of the object's fields, in encounter order
     */
    static Map<String, String> readObject(String text) {
        Map<String, String> result = new LinkedHashMap<>();
        String trimmed = text.trim();
        if (trimmed.length() < 2 || trimmed.charAt(0) != '{') {
            return result;
        }
        String inner = trimmed.substring(1, trimmed.length() - 1).trim();
        if (inner.isEmpty()) {
            return result;
        }
        for (String pair : splitTopLevel(inner)) {
            int colon = pair.indexOf(':');
            if (colon < 0) {
                continue;
            }
            String key = unquote(pair.substring(0, colon).trim());
            String value = unquote(pair.substring(colon + 1).trim());
            result.put(key, value);
        }
        return result;
    }

    private static String[] splitTopLevel(String inner) {
        // The catalog payload has no nested commas, so a plain split on commas outside quotes is enough.
        java.util.List<String> parts = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        for (int i = 0; i < inner.length(); i++) {
            char c = inner.charAt(i);
            if (c == '"' && (i == 0 || inner.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            if (c == ',' && !inString) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());
        return parts.toArray(new String[0]);
    }

    private static String unquote(String raw) {
        if (raw.length() >= 2 && raw.charAt(0) == '"' && raw.charAt(raw.length() - 1) == '"') {
            return raw.substring(1, raw.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return raw;
    }

    private static String escape(String raw) {
        return raw.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
