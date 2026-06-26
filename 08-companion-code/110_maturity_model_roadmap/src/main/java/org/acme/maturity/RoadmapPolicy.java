package org.acme.maturity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * The externalized roadmap policy, loaded from a profile-selected properties file rather than hard-coded.
 * The profile is chosen at startup by the {@code maturity.profile} system property (default {@code dev});
 * each profile lives in {@code maturity-<profile>.properties} on the classpath. This is the same idea a
 * framework's {@code %dev} / {@code %prod} config blocks provide — what counts as progress, and when a
 * team should stop adding rather than climb further, are team-and-context decisions, not values compiled
 * in. The chapter's own honesty applies to the assessment itself: this policy is a default to tailor, not
 * a verdict on whether a team is "good".
 *
 * <p>Two knobs carry the chapter's two hardest limitations. {@code requireOutcomes} decides whether a
 * dimension whose outcomes are not improving is allowed to count its claimed stage — the guard against a
 * maturity level becoming a vanity badge (a Goodhart trap; Chapters 1, 38). {@code sustainAtStage} is the
 * stage past which the assessment recommends sustaining and subtracting gates that no longer pay rather
 * than climbing further, because more maturity is not more value past a point (over-governance ossifies;
 * Chapters 26, 33).
 *
 * @param requireOutcomes {@code true} to count a dimension's stage only when its outcomes are improving
 * @param sustainAtStage  the stage at or above which the recommendation is to sustain, never {@code null}
 */
// tag::roadmap-policy[]
public record RoadmapPolicy(boolean requireOutcomes, Stage sustainAtStage) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "maturity.profile";
    private static final String DEFAULT_PROFILE = "dev";
    // end::roadmap-policy[]

    /** Compact constructor: the sustain threshold is always required. */
    public RoadmapPolicy {
        Objects.requireNonNull(sustainAtStage, "sustainAtStage");
    }

    /**
     * Loads the policy for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}).
     *
     * @return the externalized policy for the active profile, never {@code null}
     */
    public static RoadmapPolicy load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized policy for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static RoadmapPolicy load(String profile) {
        String resource = "maturity-" + Objects.requireNonNull(profile, "profile") + ".properties";
        Properties props = new Properties();
        try (InputStream in = RoadmapPolicy.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no policy for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        boolean outcomes = Boolean.parseBoolean(props.getProperty("roadmap.require-outcomes", "true"));
        Stage sustain = Stage.valueOf(props.getProperty("roadmap.sustain-at-stage", "SUSTAIN_EVOLVE"));
        return new RoadmapPolicy(outcomes, sustain);
    }

    /**
     * The stage a rating counts toward maturity under this policy. When {@code requireOutcomes} is set, a
     * dimension whose outcomes are not improving counts only as {@link Stage#FOUNDATIONS} (its claimed
     * stage is discounted as a vanity climb); when it is unset, the claimed stage counts as recorded — the
     * lenient setting a team uses early while it is still wiring outcome measurement up.
     *
     * @param rating the dimension rating to score, never {@code null}
     * @return the stage the rating counts as under this policy, never {@code null}
     */
    public Stage countedStage(DimensionRating rating) {
        Objects.requireNonNull(rating, "rating");
        return requireOutcomes ? rating.effectiveStage() : rating.stage();
    }
}
