package org.acme.metrics;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Externalized rollout-and-dashboard configuration, loaded from a profile-selected properties file
 * rather than hard-coded. The profile is chosen at startup by the {@code metrics.profile} system
 * property (default {@code dev}); each profile lives in {@code metrics-<profile>.properties} on the
 * classpath. This is the same {@code dev} / {@code prod} split a framework's config blocks provide: a
 * rollout warns while it is being introduced and blocks once trust is built, and the dashboard's alert
 * threshold differs between developing and production.
 *
 * <p>Note what is <em>not</em> here: any DORA performance band. The
 * {@code changeFailureRateAlertThreshold} is a deployment's own chosen alert level, not a claim about
 * what counts as an "elite" or "high" team — those bands are edition-specific and verified against the
 * pinned State-of-DevOps edition, never compiled in.
 *
 * @param ratchetEnforced                 whether a ratchet regression blocks the build (prod) or only
 *                                        warns while the gate is being introduced (dev) — the
 *                                        warn-then-block sequence the rollout uses to build trust
 * @param changeFailureRateAlertThreshold the change-failure rate at or above which the dashboard's
 *                                        stability tile flags, in {@code [0, 1]} — a deployment's chosen
 *                                        alert level, not a DORA band
 */
public record MetricsConfig(boolean ratchetEnforced, double changeFailureRateAlertThreshold) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "metrics.profile";
    private static final String DEFAULT_PROFILE = "dev";

    /** Compact constructor: a rate outside {@code [0, 1]} is a configuration error, fail fast. */
    public MetricsConfig {
        if (changeFailureRateAlertThreshold < 0.0 || changeFailureRateAlertThreshold > 1.0) {
            throw new IllegalArgumentException(
                    "changeFailureRateAlertThreshold must be in [0, 1]: " + changeFailureRateAlertThreshold);
        }
    }

    /** Loads the configuration for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}). */
    public static MetricsConfig load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the configuration for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized configuration for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static MetricsConfig load(String profile) {
        String resource = "metrics-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = MetricsConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no config for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        boolean enforced = Boolean.parseBoolean(props.getProperty("ratchet.enforced", "true"));
        double threshold = Double.parseDouble(props.getProperty("change.failure.rate.alert.threshold", "0.15"));
        return new MetricsConfig(enforced, threshold);
    }
}
