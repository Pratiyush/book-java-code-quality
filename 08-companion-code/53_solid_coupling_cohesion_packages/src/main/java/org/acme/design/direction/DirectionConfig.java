package org.acme.design.direction;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Reads the active profile's direction policy from the externalized {@code design.properties}.
 *
 * <p>The profile is chosen by the {@code design.profile} system property (default {@code dev}). The
 * matching {@code %<profile>.direction.enforce} key decides whether a wrong-direction dependency is
 * rejected ({@code prod}) or only reported ({@code dev}) — the policy is configuration, not a literal
 * baked into the code.
 */
public final class DirectionConfig {

    private static final String RESOURCE = "/design.properties";
    private static final String DEFAULT_PROFILE = "dev";

    private final boolean enforce;
    private final String profile;

    private DirectionConfig(String profile, boolean enforce) {
        this.profile = profile;
        this.enforce = enforce;
    }

    /**
     * Loads the policy for the active profile (the {@code design.profile} system property).
     *
     * @return the loaded configuration, never {@code null}
     */
    public static DirectionConfig load() {
        return load(System.getProperty("design.profile", DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for an explicit profile.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the loaded configuration, never {@code null}
     */
    public static DirectionConfig load(String profile) {
        Properties properties = new Properties();
        try (InputStream in = DirectionConfig.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("missing classpath resource " + RESOURCE);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("could not read " + RESOURCE, e);
        }
        boolean enforce = Boolean.parseBoolean(properties.getProperty("%" + profile + ".direction.enforce"));
        return new DirectionConfig(profile, enforce);
    }

    /**
     * Whether a wrong-direction dependency is rejected (strict) or only reported (lenient).
     *
     * @return {@code true} when enforcement is on for the active profile
     */
    public boolean enforce() {
        return enforce;
    }

    /**
     * The active profile name.
     *
     * @return the profile name, never {@code null}
     */
    public String profile() {
        return profile;
    }
}
