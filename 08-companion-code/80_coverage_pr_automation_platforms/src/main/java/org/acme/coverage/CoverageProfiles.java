package org.acme.coverage;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Loads a {@link CoveragePolicy} from an externalized profile on the classpath, so the gate's
 * thresholds are configuration ({@code coverage-dev.properties} / {@code coverage-prod.properties}),
 * not constants baked into the code.
 *
 * <p>The profile name comes from the {@code coverage.profile} system property, defaulting to
 * {@code dev} — the same shape the peer key-33 module uses for its gate policy. Selecting a profile
 * that has no properties file is a configuration error and fails fast rather than silently falling
 * back to a default that would gate the wrong way.
 */
public final class CoverageProfiles {

    private static final String DEFAULT_PROFILE = "dev";

    private CoverageProfiles() {
    }

    /**
     * Loads the policy for the profile named by the {@code coverage.profile} system property (default
     * {@code dev}).
     *
     * @return the policy from that profile
     */
    public static CoveragePolicy load() {
        return load(System.getProperty("coverage.profile", DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for a named profile from {@code coverage-<profile>.properties} on the classpath.
     *
     * @param profile the profile name, never {@code null} or blank
     * @return the policy described by that profile's properties
     * @throws NullPointerException     if {@code profile} is {@code null}
     * @throws IllegalArgumentException if {@code profile} is blank or has no properties file
     */
    public static CoveragePolicy load(String profile) {
        Objects.requireNonNull(profile, "profile");
        if (profile.isBlank()) {
            throw new IllegalArgumentException("profile must not be blank");
        }
        String resource = "coverage-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = CoverageProfiles.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no coverage profile on the classpath: " + resource);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read coverage profile: " + resource, e);
        }
        return CoveragePolicy.from(props);
    }
}
