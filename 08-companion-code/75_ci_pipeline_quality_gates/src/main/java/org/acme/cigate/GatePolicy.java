package org.acme.cigate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * The externalized quality-gate policy, loaded from a profile-selected properties file rather than
 * hard-coded. The profile is chosen at startup by the {@code cigate.profile} system property (default
 * {@code dev}); each profile lives in {@code cigate-<profile>.properties} on the classpath. This is the
 * same idea a framework's {@code %dev} / {@code %prod} config blocks provide — a feature-branch gate
 * tolerates more than the gate guarding the trunk, and neither threshold is compiled in.
 *
 * <p>Two knobs carry the chapter's two policy axes. {@code cleanAsYouCode} decides whether the gate
 * scopes to new and changed code (the modern default that makes a gate adoptable on a legacy codebase)
 * or to the whole repository (which blocks every pull request on inherited debt). {@code blockSeverity}
 * is the severity at which a new finding flips from a warning to a build-breaking block — set it high
 * and the gate stays credible; set it low and the team routes around the noise (Chapter 19).
 *
 * @param cleanAsYouCode {@code true} to gate only new/changed code, {@code false} to gate the whole repo
 * @param blockSeverity  the lowest severity a new finding must reach to block the build, never {@code null}
 */
// tag::gate-policy[]
public record GatePolicy(boolean cleanAsYouCode, Severity blockSeverity) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "cigate.profile";
    private static final String DEFAULT_PROFILE = "dev";
    // end::gate-policy[]

    /**
     * Loads the policy for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}).
     *
     * @return the externalized policy for the active profile, never {@code null}
     */
    public static GatePolicy load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized policy for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static GatePolicy load(String profile) {
        String resource = "cigate-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = GatePolicy.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no policy for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        boolean newCodeOnly = Boolean.parseBoolean(props.getProperty("gate.clean-as-you-code", "true"));
        Severity threshold = Severity.valueOf(props.getProperty("gate.block.severity", "HIGH"));
        return new GatePolicy(newCodeOnly, threshold);
    }
}
