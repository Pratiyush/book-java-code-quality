package org.acme.secgate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * The externalized security-gate policy, loaded from a profile-selected properties file rather than
 * hard-coded. The profile is chosen at startup by the {@code secgate.profile} system property (default
 * {@code dev}); each profile lives in {@code secgate-<profile>.properties} on the classpath. This is the
 * same idea a framework's {@code %dev} / {@code %prod} config blocks provide — a feature-branch gate
 * tolerates more than the gate guarding the trunk before a release, and neither threshold is compiled in.
 *
 * <p>Three knobs carry the chapter's policy. {@code cleanAsYouCode} decides whether the gate scopes to
 * new and changed code (the scoping that keeps the gate from blocking every pull request on inherited
 * security debt) or to the whole repository. {@code blockSeverity} is the severity at which a new finding
 * blocks the build — set it high and the gate stays credible; set it low and the team routes around the
 * noise (Chapter 19, gate fatigue). {@code requireExploitableToBlock} carries the chapter's "exploitability
 * is a judgment" edge: when on, a severe finding that is not confirmed exploitable is routed to a security
 * reviewer rather than auto-blocking, the middle path between blocking everything and blocking nothing.
 *
 * @param cleanAsYouCode            {@code true} to gate only new/changed code, {@code false} to gate the whole repo
 * @param blockSeverity            the lowest severity a new finding must reach to block the build, never {@code null}
 * @param requireExploitableToBlock {@code true} to route severe-but-unproven findings to a reviewer instead of blocking
 */
// tag::gate-policy[]
public record SecurityGatePolicy(
        boolean cleanAsYouCode, Severity blockSeverity, boolean requireExploitableToBlock) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "secgate.profile";
    private static final String DEFAULT_PROFILE = "dev";
    // end::gate-policy[]

    /**
     * Loads the policy for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}).
     *
     * @return the externalized policy for the active profile, never {@code null}
     */
    public static SecurityGatePolicy load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized policy for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static SecurityGatePolicy load(String profile) {
        String resource = "secgate-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = SecurityGatePolicy.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no policy for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        boolean newCodeOnly = Boolean.parseBoolean(props.getProperty("gate.clean-as-you-code", "true"));
        Severity threshold = Severity.valueOf(props.getProperty("gate.block.severity", "HIGH"));
        boolean exploitableToBlock =
            Boolean.parseBoolean(props.getProperty("gate.require-exploitable-to-block", "true"));
        return new SecurityGatePolicy(newCodeOnly, threshold, exploitableToBlock);
    }
}
