package org.acme.parity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * The externalized parity policy, loaded from a profile-selected properties file rather than hard-coded.
 * The profile is chosen at startup by the {@code parity.profile} system property (default {@code dev});
 * each profile lives in {@code parity-<profile>.properties} on the classpath. This is the same idea a
 * framework's {@code %dev} / {@code %prod} blocks provide: a developer's machine may treat parity drift
 * as a warning while the trunk's CI treats it as a build-breaking error, and neither choice is compiled
 * in.
 *
 * <p>One knob carries the policy. {@code failOnDrift} decides whether broken parity (a required CI check
 * with no local counterpart) fails fast or is surfaced as a warning for the caller to act on. The
 * {@code localRunner} label records how the local checks are run (for example the wrapper command), so a
 * reported drift can point a developer at where to add the missing check.
 *
 * @param failOnDrift {@code true} to fail fast on broken parity, {@code false} to return it as a warning
 * @param localRunner how the local checks are run, for example {@code "./mvnw -B verify"}, never {@code null}
 */
public record ParityPolicy(boolean failOnDrift, String localRunner) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "parity.profile";
    private static final String DEFAULT_PROFILE = "dev";

    /** Compact constructor: the runner label is required so a drift report is always actionable. */
    public ParityPolicy {
        if (localRunner == null) {
            throw new IllegalArgumentException("localRunner");
        }
    }

    /**
     * Loads the policy for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}).
     *
     * @return the externalized policy for the active profile, never {@code null}
     */
    public static ParityPolicy load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized policy for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static ParityPolicy load(String profile) {
        String resource = "parity-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = ParityPolicy.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no policy for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        boolean failOnDrift = Boolean.parseBoolean(props.getProperty("parity.fail-on-drift", "false"));
        String runner = props.getProperty("parity.local-runner", "./mvnw -B verify");
        return new ParityPolicy(failOnDrift, runner);
    }
}
