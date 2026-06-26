package org.acme.observability;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.System.Logger.Level;
import java.util.Properties;

/**
 * Externalized observability configuration, loaded from a profile-selected properties file rather
 * than hard-coded. The profile is chosen at startup by the {@code observability.profile} system
 * property (default {@code dev}); each profile lives in {@code observability-<profile>.properties}
 * on the classpath. This is the same idea a framework's {@code %dev} / {@code %prod} config blocks
 * provide — the log level and the SLO error budget differ between developing and production, and
 * neither value is compiled in.
 *
 * @param logLevel        the floor at or above which log lines are written
 * @param sloErrorBudget  the fraction of failing requests the health gauge treats as the SLO
 *                        threshold — alert on budget burn, not on every blip
 */
public record ObservabilityConfig(Level logLevel, double sloErrorBudget) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "observability.profile";
    private static final String DEFAULT_PROFILE = "dev";

    /** Compact constructor: a budget outside {@code [0, 1]} is a configuration error, fail fast. */
    public ObservabilityConfig {
        if (logLevel == null) {
            throw new IllegalArgumentException("logLevel must not be null");
        }
        if (sloErrorBudget < 0.0 || sloErrorBudget > 1.0) {
            throw new IllegalArgumentException("sloErrorBudget must be in [0, 1]: " + sloErrorBudget);
        }
    }

    /**
     * Loads the configuration for the profile named by {@link #PROFILE_PROPERTY} (default
     * {@code dev}).
     *
     * @return the externalized configuration for the active profile, never {@code null}
     */
    public static ObservabilityConfig load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the configuration for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized configuration for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static ObservabilityConfig load(String profile) {
        String resource = "observability-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = ObservabilityConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no config for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        Level level = Level.valueOf(props.getProperty("log.level", "INFO"));
        double budget = Double.parseDouble(props.getProperty("slo.error.budget", "0.05"));
        return new ObservabilityConfig(level, budget);
    }
}
