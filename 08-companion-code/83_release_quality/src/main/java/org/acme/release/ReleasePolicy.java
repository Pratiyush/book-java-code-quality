package org.acme.release;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * The externalized set of release preconditions a profile requires, loaded from a profile-selected
 * properties file rather than hard-coded. The profile is chosen at startup by the {@code release.profile}
 * system property (default {@code dev}); each profile lives in {@code release-<profile>.properties} on the
 * classpath. This is the same idea a framework's {@code %dev} / {@code %prod} config blocks provide: an
 * internal preview build can ship with fewer ceremonies than a production release, and neither set of
 * required checks is compiled in.
 *
 * <p>The point the two profiles make is the chapter's: progressive ceremony. A {@code dev} pre-release to
 * a staging environment may require only that the version and changelog are in order, while a {@code prod}
 * release requires the full set — CI green on the commit, signed with an SBOM (Part VII), and smoke-tested
 * on the staged build. The gate enforces whatever the active profile lists; it does not bake one team's
 * release bar into the code.
 */
public final class ReleasePolicy {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "release.profile";
    private static final String DEFAULT_PROFILE = "dev";

    private final Set<ReleaseCheck> required;

    private ReleasePolicy(Set<ReleaseCheck> required) {
        this.required = EnumSet.copyOf(required);
    }

    /**
     * Loads the policy for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}).
     *
     * @return the externalized policy for the active profile, never {@code null}
     */
    public static ReleasePolicy load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized policy for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static ReleasePolicy load(String profile) {
        String resource = "release-" + Objects.requireNonNull(profile, "profile") + ".properties";
        var props = new java.util.Properties();
        try (InputStream in = ReleasePolicy.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no policy for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        EnumSet<ReleaseCheck> required = EnumSet.noneOf(ReleaseCheck.class);
        for (ReleaseCheck check : ReleaseCheck.values()) {
            // Each check is opt-in per profile via release.require.<CHECK>=true (default false).
            String key = "release.require." + check.name();
            if (Boolean.parseBoolean(props.getProperty(key, "false"))) {
                required.add(check);
            }
        }
        return new ReleasePolicy(required);
    }

    /**
     * The set of preconditions this profile requires before a release may ship.
     *
     * @return an immutable view of the required checks, never {@code null}
     */
    public Set<ReleaseCheck> required() {
        return EnumSet.copyOf(required);
    }
}
