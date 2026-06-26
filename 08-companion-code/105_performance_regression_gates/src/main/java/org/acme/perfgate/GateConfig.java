package org.acme.perfgate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Externalized gate configuration, loaded from a profile-selected properties file rather than
 * hard-coded. The profile is chosen at startup by the {@code perfgate.profile} system property
 * (default {@code dev}); each profile lives in {@code perfgate-<profile>.properties} on the classpath.
 * This is the same idea a framework's {@code %dev} / {@code %prod} config blocks provide — a perf
 * pipeline tolerates more slack while developing than it does protecting a release, and neither
 * threshold is compiled in.
 *
 * <p>Two tolerances, because the gate has three outcomes (see {@link GateVerdict}). A regression
 * within {@code flagTolerance} of the baseline passes; one between {@code flagTolerance} and
 * {@code failTolerance} is flagged for a human; one beyond {@code failTolerance}, and outside the
 * measurement's own noise band, fails the build. The numbers here are <em>illustrative</em>: real
 * tolerances come from real requirements (the p99 users need, Chapter 43), never from round numbers
 * that merely look tidy (Chapter 1).
 *
 * @param flagTolerance the fractional regression below which the gate passes silently, in {@code [0,1]}
 * @param failTolerance the fractional regression at or above which the gate fails, {@code >= flagTolerance}
 */
public record GateConfig(double flagTolerance, double failTolerance) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "perfgate.profile";
    private static final String DEFAULT_PROFILE = "dev";

    /** Compact constructor: tolerances out of range, or a fail band below the flag band, fail fast. */
    public GateConfig {
        if (flagTolerance < 0.0 || flagTolerance > 1.0) {
            throw new IllegalArgumentException("flagTolerance must be in [0, 1]: " + flagTolerance);
        }
        if (failTolerance < flagTolerance || failTolerance > 1.0) {
            throw new IllegalArgumentException(
                "failTolerance must be in [flagTolerance, 1]: " + failTolerance);
        }
    }

    /**
     * Loads the configuration for the profile named by {@link #PROFILE_PROPERTY} (default
     * {@code dev}).
     *
     * @return the externalized configuration for the active profile, never {@code null}
     */
    public static GateConfig load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the configuration for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized configuration for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static GateConfig load(String profile) {
        String resource = "perfgate-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = GateConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no config for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        double flag = Double.parseDouble(props.getProperty("gate.flag.tolerance", "0.05"));
        double fail = Double.parseDouble(props.getProperty("gate.fail.tolerance", "0.10"));
        return new GateConfig(flag, fail);
    }
}
