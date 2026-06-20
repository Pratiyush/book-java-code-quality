package org.acme.storefront.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A tiny, dependency-free JSON reader/writer — just enough for this example's request and response
 * shapes. A real service would use a maintained library (Jackson, JSON-B); this stays zero-dependency
 * so the flagship module builds with nothing but the JDK.
 *
 * <p>The writer accepts {@code String}, {@code Number}, {@code Boolean}, {@code null}, {@link Map}
 * (object) and {@link Iterable} (array). The reader parses a JSON object into nested
 * {@code Map}/{@code List}/{@code String}/{@code Long}/{@code Double}/{@code Boolean}/{@code null}.
 */
public final class Json {

    private Json() {
    }

    /**
     * Serializes a value to a JSON string.
     *
     * @param value the value (object/array/scalar) to serialize
     * @return the JSON text
     */
    public static String write(Object value) {
        StringBuilder out = new StringBuilder();
        writeValue(out, value);
        return out.toString();
    }

    private static void writeValue(StringBuilder out, Object value) {
        switch (value) {
            case null -> out.append("null");
            case String s -> writeString(out, s);
            case Boolean b -> out.append(b.booleanValue() ? "true" : "false");
            case Number n -> out.append(n.toString());
            case Map<?, ?> map -> writeObject(out, map);
            case Iterable<?> iterable -> writeArray(out, iterable);
            default -> writeString(out, value.toString());
        }
    }

    private static void writeObject(StringBuilder out, Map<?, ?> map) {
        out.append('{');
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                out.append(',');
            }
            first = false;
            writeString(out, String.valueOf(entry.getKey()));
            out.append(':');
            writeValue(out, entry.getValue());
        }
        out.append('}');
    }

    private static void writeArray(StringBuilder out, Iterable<?> iterable) {
        out.append('[');
        boolean first = true;
        for (Object element : iterable) {
            if (!first) {
                out.append(',');
            }
            first = false;
            writeValue(out, element);
        }
        out.append(']');
    }

    private static void writeString(StringBuilder out, String s) {
        out.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        out.append('"');
    }

    /**
     * Parses a JSON object into a map.
     *
     * @param text the JSON text (must be a single object)
     * @return the parsed object as a map
     * @throws JsonParseException if the text is not a well-formed JSON object
     */
    @SuppressWarnings("unchecked") // checked at runtime immediately below: a parsed object is always a Map
    public static Map<String, Object> parseObject(String text) {
        Objects.requireNonNull(text, "text");
        Parser parser = new Parser(text);
        parser.skipWhitespace();
        Object root = parser.readValue();
        parser.skipWhitespace();
        if (!parser.atEnd()) {
            throw new JsonParseException("trailing characters after JSON value");
        }
        if (!(root instanceof Map)) {
            throw new JsonParseException("expected a JSON object");
        }
        return (Map<String, Object>) root;
    }

    /** Thrown when input is not well-formed JSON. */
    public static final class JsonParseException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        JsonParseException(String message) {
            super(message);
        }
    }

    /** A minimal recursive-descent JSON parser. */
    private static final class Parser {
        private final String src;
        private int pos;

        Parser(String src) {
            this.src = src;
        }

        boolean atEnd() {
            return pos >= src.length();
        }

        void skipWhitespace() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) {
                pos++;
            }
        }

        Object readValue() {
            skipWhitespace();
            if (atEnd()) {
                throw new JsonParseException("unexpected end of input");
            }
            char c = src.charAt(pos);
            return switch (c) {
                case '{' -> readObject();
                case '[' -> readArray();
                case '"' -> readString();
                case 't', 'f' -> readBoolean();
                case 'n' -> readNull();
                default -> readNumber();
            };
        }

        private Map<String, Object> readObject() {
            Map<String, Object> object = new LinkedHashMap<>();
            expect('{');
            skipWhitespace();
            if (peek() == '}') {
                pos++;
                return object;
            }
            while (true) {
                skipWhitespace();
                String key = readString();
                skipWhitespace();
                expect(':');
                object.put(key, readValue());
                skipWhitespace();
                char next = next();
                if (next == '}') {
                    return object;
                }
                if (next != ',') {
                    throw new JsonParseException("expected ',' or '}' in object");
                }
            }
        }

        private List<Object> readArray() {
            List<Object> array = new ArrayList<>();
            expect('[');
            skipWhitespace();
            if (peek() == ']') {
                pos++;
                return array;
            }
            while (true) {
                array.add(readValue());
                skipWhitespace();
                char next = next();
                if (next == ']') {
                    return array;
                }
                if (next != ',') {
                    throw new JsonParseException("expected ',' or ']' in array");
                }
            }
        }

        private String readString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (true) {
                if (atEnd()) {
                    throw new JsonParseException("unterminated string");
                }
                char c = src.charAt(pos++);
                if (c == '"') {
                    return sb.toString();
                }
                if (c == '\\') {
                    sb.append(readEscape());
                } else {
                    sb.append(c);
                }
            }
        }

        private char readEscape() {
            if (atEnd()) {
                throw new JsonParseException("unterminated escape");
            }
            char c = src.charAt(pos++);
            return switch (c) {
                case '"' -> '"';
                case '\\' -> '\\';
                case '/' -> '/';
                case 'n' -> '\n';
                case 'r' -> '\r';
                case 't' -> '\t';
                case 'b' -> '\b';
                case 'f' -> '\f';
                case 'u' -> readUnicodeEscape();
                default -> throw new JsonParseException("invalid escape: \\" + c);
            };
        }

        private char readUnicodeEscape() {
            if (pos + 4 > src.length()) {
                throw new JsonParseException("invalid \\u escape");
            }
            String hex = src.substring(pos, pos + 4);
            pos += 4;
            try {
                return (char) Integer.parseInt(hex, 16);
            } catch (NumberFormatException e) {
                throw new JsonParseException("invalid \\u escape: " + hex);
            }
        }

        private Object readNumber() {
            int start = pos;
            while (pos < src.length() && "+-0123456789.eE".indexOf(src.charAt(pos)) >= 0) {
                pos++;
            }
            String token = src.substring(start, pos);
            if (token.isEmpty()) {
                throw new JsonParseException("invalid JSON value at position " + start);
            }
            try {
                if (token.indexOf('.') < 0 && token.indexOf('e') < 0 && token.indexOf('E') < 0) {
                    return Long.parseLong(token);
                }
                return Double.parseDouble(token);
            } catch (NumberFormatException e) {
                throw new JsonParseException("invalid number: " + token);
            }
        }

        private Boolean readBoolean() {
            if (src.startsWith("true", pos)) {
                pos += 4;
                return Boolean.TRUE;
            }
            if (src.startsWith("false", pos)) {
                pos += 5;
                return Boolean.FALSE;
            }
            throw new JsonParseException("invalid literal");
        }

        private Object readNull() {
            if (src.startsWith("null", pos)) {
                pos += 4;
                return null;
            }
            throw new JsonParseException("invalid literal");
        }

        private char peek() {
            if (atEnd()) {
                throw new JsonParseException("unexpected end of input");
            }
            return src.charAt(pos);
        }

        private char next() {
            return src.charAt(pos++);
        }

        private void expect(char c) {
            if (atEnd() || src.charAt(pos) != c) {
                throw new JsonParseException("expected '" + c + "'");
            }
            pos++;
        }
    }
}
