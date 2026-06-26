package org.acme.testdiscipline;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

/**
 * Reads the externalized reservation configuration and exposes the profile-selected values.
 *
 * <p>The configuration lives in {@code config.properties}, not in the code, and the active profile is
 * chosen at runtime by the {@code reservation.profile} system property (for example {@code dev} or
 * {@code prod}). Keys are looked up first as {@code %profile.key} (the book's profile convention) and
 * fall back to the unprefixed default, so a profile overrides only what differs. This keeps the
 * seat-hold window and the async poll budget out of the Java source — the behaviour a deployment would
 * change is data, not code.
 */
public final class ReservationConfig {

    private static final String PROFILE_PROPERTY = "reservation.profile";
    private static final String DEFAULT_PROFILE = "prod";

    private final Duration holdWindow;
    private final Duration asyncPollTimeout;

    private ReservationConfig(Duration holdWindow, Duration asyncPollTimeout) {
        this.holdWindow = holdWindow;
        this.asyncPollTimeout = asyncPollTimeout;
    }

    /**
     * Loads the config for the profile named by the {@code reservation.profile} system property,
     * defaulting to {@code prod}.
     *
     * @return the loaded config, never {@code null}
     * @throws UncheckedIOException if the bundled config resource cannot be read
     */
    public static ReservationConfig load() {
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
    public static ReservationConfig load(String profile) {
        Objects.requireNonNull(profile, "profile");
        Properties props = new Properties();
        try (InputStream in = ReservationConfig.class.getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read config.properties", e);
        }
        Duration hold = Duration.ofSeconds(Long.parseLong(value(props, profile, "reservation.hold-window-seconds")));
        Duration poll = Duration.ofSeconds(Long.parseLong(value(props, profile, "async.poll-timeout-seconds")));
        return new ReservationConfig(hold, poll);
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
     * Returns the configured seat-hold window before a confirmation expires.
     *
     * @return the hold window, never {@code null}
     */
    public Duration holdWindow() {
        return holdWindow;
    }

    /**
     * Returns the configured budget a deterministic poll is allowed before it gives up.
     *
     * @return the async poll timeout, never {@code null}
     */
    public Duration asyncPollTimeout() {
        return asyncPollTimeout;
    }
}
