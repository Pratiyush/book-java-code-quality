package org.acme.ai;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Reads the AI-review gate's tunables for one externalized profile ({@code dev} or {@code prod}) from
 * {@code application.properties}, so the request-body cap and the provenance requirement are
 * configuration a deployment selects rather than literals baked into the code. The profile is chosen by
 * the {@code ai.profile} system property and defaults to {@code dev}.
 *
 * <p>The dev profile keeps the lower-friction posture for fast local feedback; the prod profile
 * requires provenance, matching the chapter's stance that an AI contribution is untrusted until
 * verified. The values are this team's cited choices, not universal constants.
 */
public final class AiReviewProfile {

    private final int maxBodyChars;
    private final boolean requireProvenance;

    private AiReviewProfile(int maxBodyChars, boolean requireProvenance) {
        this.maxBodyChars = maxBodyChars;
        this.requireProvenance = requireProvenance;
    }

    /**
     * Loads the active profile named by the {@code ai.profile} system property (default {@code dev}).
     *
     * @return the loaded profile
     */
    public static AiReviewProfile active() {
        return load(System.getProperty("ai.profile", "dev"));
    }

    /**
     * Loads a named profile from {@code application.properties}.
     *
     * @param profile the profile name, for example {@code "dev"} or {@code "prod"}
     * @return the loaded profile
     * @throws IllegalArgumentException if the profile defines no keys
     */
    public static AiReviewProfile load(String profile) {
        Properties props = new Properties();
        try (InputStream in = AiReviewProfile.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new IllegalStateException("application.properties not found on the classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("cannot read application.properties", e);
        }
        return new AiReviewProfile(
                intValue(props, profile, "ai.request.max-body-chars"),
                booleanValue(props, profile, "ai.require-provenance"));
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
     * Returns whether this profile requires an AI contribution's provenance to be attested.
     *
     * @return {@code true} when provenance is required (the prod posture)
     */
    public boolean requireProvenance() {
        return requireProvenance;
    }

    private static int intValue(Properties props, String profile, String key) {
        return Integer.parseInt(stringValue(props, profile, key));
    }

    private static boolean booleanValue(Properties props, String profile, String key) {
        return Boolean.parseBoolean(stringValue(props, profile, key));
    }

    private static String stringValue(Properties props, String profile, String key) {
        String value = props.getProperty("%" + profile + "." + key);
        if (value == null) {
            throw new IllegalArgumentException("no value for " + key + " in profile " + profile);
        }
        return value.trim();
    }
}
