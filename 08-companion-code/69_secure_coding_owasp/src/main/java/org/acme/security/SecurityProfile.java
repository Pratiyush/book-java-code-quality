package org.acme.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Reads the security tunables for one externalized profile ({@code dev} or {@code prod}) from
 * {@code application.properties}, so the request-body cap and the PBKDF2 work factor are
 * configuration a deployment selects rather than literals baked into the code. The profile is chosen
 * by the {@code security.profile} system property and defaults to {@code dev}.
 *
 * <p>The dev profile keeps the work factor low for fast tests; the prod profile uses the stronger,
 * dated baseline. Neither file holds a key or a secret — those stay out of source entirely.
 */
public final class SecurityProfile {

    private final int maxBodyChars;
    private final int pbkdf2Iterations;

    private SecurityProfile(int maxBodyChars, int pbkdf2Iterations) {
        this.maxBodyChars = maxBodyChars;
        this.pbkdf2Iterations = pbkdf2Iterations;
    }

    /**
     * Loads the active profile named by the {@code security.profile} system property (default
     * {@code dev}).
     *
     * @return the loaded profile
     */
    public static SecurityProfile active() {
        return load(System.getProperty("security.profile", "dev"));
    }

    /**
     * Loads a named profile from {@code application.properties}.
     *
     * @param profile the profile name, for example {@code "dev"} or {@code "prod"}
     * @return the loaded profile
     * @throws IllegalArgumentException if the profile defines no keys
     */
    public static SecurityProfile load(String profile) {
        Properties props = new Properties();
        try (InputStream in = SecurityProfile.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new IllegalStateException("application.properties not found on the classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("cannot read application.properties", e);
        }
        return new SecurityProfile(
                intValue(props, profile, "security.request.max-body-chars"),
                intValue(props, profile, "security.password.pbkdf2-iterations"));
    }

    /**
     * Returns the externalized request-body character cap for this profile.
     *
     * @return the maximum body size in characters
     */
    public int maxBodyChars() {
        return maxBodyChars;
    }

    /**
     * Returns the externalized PBKDF2 iteration count (work factor) for this profile.
     *
     * @return the PBKDF2 iteration count
     */
    public int pbkdf2Iterations() {
        return pbkdf2Iterations;
    }

    private static int intValue(Properties props, String profile, String key) {
        String value = props.getProperty("%" + profile + "." + key);
        if (value == null) {
            throw new IllegalArgumentException("no value for " + key + " in profile " + profile);
        }
        return Integer.parseInt(value.trim());
    }
}
