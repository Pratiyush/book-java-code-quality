package org.acme.refstack;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * The externalized gate-ladder policy, loaded from a profile-selected properties file rather than
 * hard-coded. The profile is chosen at startup by the {@code refstack.profile} system property (default
 * {@code dev}); each profile lives in {@code refstack-<profile>.properties} on the classpath. This is
 * the same idea a framework's {@code %dev} / {@code %prod} config blocks provide — a feature-branch
 * ladder enforces fewer stages than the ladder guarding the trunk, and neither setting is compiled in,
 * because the right ladder is a team-and-context decision (the capstone's honesty: this is a starting
 * point to tailor, not a verdict).
 *
 * <p>Three knobs carry the chapter's three gate-design axes. {@code enforceFrom} is the earliest stage
 * whose blocking findings actually block the ship verdict — stages before it are advisory, so a team can
 * adopt the stack incrementally by moving this later early on and ratcheting it earlier over time
 * (Chapters 38, 40). {@code cleanAsYouCode} decides whether the gate scopes to new and changed code (the
 * default that makes the stack adoptable on a legacy codebase) or to the whole repository.
 * {@code blockSeverity} is the severity at which a finding flips from advisory to ship-blocking.
 *
 * @param enforceFrom    the earliest stage whose findings block the ship verdict, never {@code null}
 * @param cleanAsYouCode {@code true} to block only on new/changed code, {@code false} for the whole repo
 * @param blockSeverity  the lowest severity a finding must reach to block, never {@code null}
 */
// tag::gate-ladder[]
public record GateLadder(GateStage enforceFrom, boolean cleanAsYouCode, Severity blockSeverity) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "refstack.profile";
    private static final String DEFAULT_PROFILE = "dev";
    // end::gate-ladder[]

    /** Compact constructor: every component is required, so an incomplete ladder can never exist. */
    public GateLadder {
        Objects.requireNonNull(enforceFrom, "enforceFrom");
        Objects.requireNonNull(blockSeverity, "blockSeverity");
    }

    /**
     * Loads the ladder for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}).
     *
     * @return the externalized ladder for the active profile, never {@code null}
     */
    public static GateLadder load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the ladder for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized ladder for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static GateLadder load(String profile) {
        String resource = "refstack-" + Objects.requireNonNull(profile, "profile") + ".properties";
        Properties props = new Properties();
        try (InputStream in = GateLadder.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no ladder for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        GateStage from = GateStage.valueOf(props.getProperty("ladder.enforce-from", "PR_FAST"));
        boolean newCodeOnly = Boolean.parseBoolean(props.getProperty("ladder.clean-as-you-code", "true"));
        Severity threshold = Severity.valueOf(props.getProperty("ladder.block.severity", "HIGH"));
        return new GateLadder(from, newCodeOnly, threshold);
    }

    /**
     * Whether a stage's findings are enforced (can block) under this ladder, or only advisory. A stage
     * earlier than {@link #enforceFrom} is advisory: its findings are surfaced but never block the ship
     * verdict, which is how a team adopts the stack incrementally without the gate blocking on a stage it
     * has not yet tuned.
     *
     * @param stage the stage to test, never {@code null}
     * @return {@code true} if findings from {@code stage} can block the ship verdict
     */
    public boolean enforces(GateStage stage) {
        return Objects.requireNonNull(stage, "stage").order() >= enforceFrom.order();
    }
}
