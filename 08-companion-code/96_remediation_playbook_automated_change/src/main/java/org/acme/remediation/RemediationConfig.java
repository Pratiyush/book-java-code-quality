package org.acme.remediation;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Loads the externalized remediation-program configuration (Chapter 40) with profile resolution. The
 * thresholds that tune the playbook — how many hotspots a cycle takes on, the silenced-debt budget, the
 * minimum paydown pace — live in {@code remediation.properties}, not in code, so a deployment changes them
 * without recompiling. A {@code dev} profile pilots the playbook on one team; a {@code prod} profile runs it
 * estate-wide at a faster pace with a tighter budget.
 *
 * <p>Resolution is profile-prefixed-key-overrides-default: when the {@code prod} profile is active, {@code
 * prod.paydown.budget} wins over the unprefixed {@code paydown.budget}; under {@code dev} the unprefixed
 * default applies. An unknown key is a configuration error, surfaced as an exception rather than a silent
 * zero.
 *
 * @param profile        the active profile name (for example {@code dev} or {@code prod})
 * @param paydownBudget  how many hotspots a remediation cycle takes on
 * @param silencedBudget the largest silenced-debt set the team agrees is healthy
 * @param minPace        the smallest per-cycle paydown that keeps the program honest
 */
public record RemediationConfig(String profile, int paydownBudget, int silencedBudget, int minPace) {

    /** The classpath resource the configuration is read from. */
    public static final String RESOURCE = "/remediation.properties";

    /** Validates the resolved configuration. */
    public RemediationConfig {
        Objects.requireNonNull(profile, "profile");
    }

    /**
     * Loads the configuration from the default classpath resource, honouring the {@code remediation.profile}
     * key.
     *
     * @return the resolved configuration for the active profile
     */
    public static RemediationConfig load() {
        Properties props = read();
        return from(props, props.getProperty("remediation.profile", "dev"));
    }

    /**
     * Resolves the configuration for a named profile from already-loaded properties. Visible for the test,
     * which drives both profiles without touching the file selection.
     *
     * @param props   the loaded properties
     * @param profile the profile to resolve for
     * @return the resolved configuration
     */
    static RemediationConfig from(Properties props, String profile) {
        return new RemediationConfig(
                profile,
                intValue(props, profile, "paydown.budget"),
                intValue(props, profile, "silenced.budget"),
                intValue(props, profile, "paydown.min-pace"));
    }

    private static int intValue(Properties props, String profile, String key) {
        String value = props.getProperty(profile + "." + key, props.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("missing config key: " + key + " (profile " + profile + ")");
        }
        return Integer.parseInt(value.trim());
    }

    private static Properties read() {
        Properties props = new Properties();
        try (InputStream in = RemediationConfig.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("config resource not found on classpath: " + RESOURCE);
            }
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + RESOURCE, e);
        }
    }
}
