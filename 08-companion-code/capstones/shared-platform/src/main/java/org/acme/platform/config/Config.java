package org.acme.platform.config;

import java.util.Optional;

/**
 * Typed, externalized configuration (Part: configuration). A service reads its settings here rather
 * than hard-coding ports, URLs, or timeouts — so the same build runs differently per environment.
 * Lookup precedence is JVM system property first (e.g. {@code -Dport=8080}), then environment
 * variable in {@code UPPER_SNAKE_CASE} ({@code PORT}), then the caller's default. Every read is
 * typed and fails fast when a present value cannot be parsed.
 */
public final class Config {

    private final java.util.function.Function<String, String> source;

    Config(java.util.function.Function<String, String> source) {
        this.source = source;
    }

    /** A config backed by the live JVM system properties and process environment. */
    public static Config fromEnvironment() {
        return new Config(Config::lookupSystem);
    }

    /** A config backed by an explicit map — used by tests, never by production code. */
    public static Config fromMap(java.util.Map<String, String> values) {
        java.util.Map<String, String> copy = java.util.Map.copyOf(values);
        return new Config(copy::get);
    }

    private static String lookupSystem(String key) {
        String prop = System.getProperty(key);
        if (prop != null) {
            return prop;
        }
        return System.getenv(toEnvKey(key));
    }

    private static String toEnvKey(String key) {
        return key.toUpperCase(java.util.Locale.ROOT).replace('.', '_').replace('-', '_');
    }

    public String string(String key, String fallback) {
        String value = source.apply(key);
        return value == null ? fallback : value;
    }

    public int integer(String key, int fallback) {
        String value = source.apply(key);
        if (value == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("config '" + key + "' is not an integer: " + value, e);
        }
    }

    public long duration(String key, long fallbackMillis) {
        String value = source.apply(key);
        if (value == null) {
            return fallbackMillis;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("config '" + key + "' is not a millis value: " + value, e);
        }
    }

    public Optional<String> optional(String key) {
        return Optional.ofNullable(source.apply(key));
    }
}
