package org.acme.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

/**
 * Reads the externalized catalog configuration and exposes the profile-selected values.
 *
 * <p>The configuration lives in {@code config.properties}, not in the code, and the active profile is
 * chosen at runtime by the {@code catalog.profile} system property (for example {@code dev} or
 * {@code prod}). Keys are looked up first as {@code %profile.key} (the book's profile convention) and
 * fall back to the unprefixed default, so a profile overrides only what differs. This keeps the bind
 * port, the client timeout, and the generated-case count out of the Java source — the behaviour a
 * deployment would change is data, not code.
 */
public final class CatalogConfig {

    private static final String PROFILE_PROPERTY = "catalog.profile";
    private static final String DEFAULT_PROFILE = "prod";

    private final int serverPort;
    private final Duration clientTimeout;
    private final int generatedCases;

    private CatalogConfig(int serverPort, Duration clientTimeout, int generatedCases) {
        this.serverPort = serverPort;
        this.clientTimeout = clientTimeout;
        this.generatedCases = generatedCases;
    }

    /**
     * Loads the config for the profile named by the {@code catalog.profile} system property, defaulting
     * to {@code prod}.
     *
     * @return the loaded config, never {@code null}
     * @throws UncheckedIOException if the bundled config resource cannot be read
     */
    public static CatalogConfig load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the config for an explicit profile — the seam the test uses to exercise both profiles.
     *
     * @param profile the profile to read (for example {@code dev} or {@code prod}), never {@code null}
     * @return the loaded config, never {@code null}
     * @throws NullPointerException  if {@code profile} is {@code null}
     * @throws UncheckedIOException  if the bundled config resource cannot be read
     */
    public static CatalogConfig load(String profile) {
        Objects.requireNonNull(profile, "profile");
        Properties props = new Properties();
        try (InputStream in = CatalogConfig.class.getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read config.properties", e);
        }
        int port = Integer.parseInt(value(props, profile, "catalog.server.port"));
        Duration timeout = Duration.ofSeconds(Long.parseLong(value(props, profile, "catalog.client.timeout-seconds")));
        int cases = Integer.parseInt(value(props, profile, "property.generated-cases"));
        return new CatalogConfig(port, timeout, cases);
    }

    private static String value(Properties props, String profile, String key) {
        String profiled = props.getProperty("%" + profile + "." + key);
        if (profiled != null) {
            return profiled.strip();
        }
        String fallback = props.getProperty(key);
        if (fallback == null) {
            throw new IllegalStateException("no value for '" + key + "' in profile '" + profile + "' or default");
        }
        return fallback.strip();
    }

    /**
     * Returns the configured server bind port ({@code 0} selects an ephemeral port).
     *
     * @return the bind port for the catalog server
     */
    public int serverPort() {
        return serverPort;
    }

    /**
     * Returns the configured per-request client timeout.
     *
     * @return the client request timeout, never {@code null}
     */
    public Duration clientTimeout() {
        return clientTimeout;
    }

    /**
     * Returns the configured number of generated cases for a property run.
     *
     * @return the generated-case count, never negative
     */
    public int generatedCases() {
        return generatedCases;
    }
}
