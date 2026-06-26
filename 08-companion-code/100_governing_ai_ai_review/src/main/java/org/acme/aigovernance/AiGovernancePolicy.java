package org.acme.aigovernance;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * The externalized AI-in-the-workflow policy, loaded from a profile-selected properties file rather than
 * hard-coded. The profile is chosen at startup by the {@code aigov.profile} system property (default
 * {@code dev}); each profile lives in {@code aigov-<profile>.properties} on the classpath. This is the
 * same idea a framework's {@code %dev} / {@code %prod} config blocks provide — a feature-branch policy can
 * be more permissive than the policy guarding a release, and neither is compiled in. The chapter's point
 * that governance is "policy plus culture, not a rules page" begins with the policy being a real,
 * swappable artifact a team owns and changes deliberately.
 *
 * <p>The policy carries the chapter's load-bearing governance decisions:
 * <ul>
 *   <li>{@code sanctionedTools} — tool selection as a security/compliance decision: only assistants the
 *       organization has vetted (for where the code goes, IP of suggestions, privacy posture) may be
 *       used. An unsanctioned tool is the shadow-AI failure mode the chapter warns about.</li>
 *   <li>{@code requiredChecks} — the AI-specific checks an AI-assisted change must have run, on top of
 *       the normal gates (SAST/SCA/secrets for inherited vulnerabilities; mutation-verified AI tests).</li>
 *   <li>{@code requireDisclosure} — AI use must be recorded (provenance), the discipline this book
 *       applies to itself.</li>
 *   <li>{@code requireAccountableHuman} — a person owns the merge; "the AI did it" is not a defense.</li>
 *   <li>{@code forbidAutoMergeOnAiReview} — the hard line: no auto-merge on an AI approval.</li>
 * </ul>
 *
 * @param sanctionedTools           the vetted assistants permitted, never {@code null}
 * @param requiredChecks            the AI-specific checks an AI-assisted change must have run, never null
 * @param requireDisclosure         whether AI use must be disclosed (provenance)
 * @param requireAccountableHuman   whether a human must own the merge
 * @param forbidAutoMergeOnAiReview whether auto-merge on an AI reviewer's approval alone is forbidden
 */
public record AiGovernancePolicy(
        Set<String> sanctionedTools,
        Set<AiCheck> requiredChecks,
        boolean requireDisclosure,
        boolean requireAccountableHuman,
        boolean forbidAutoMergeOnAiReview) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "aigov.profile";
    private static final String DEFAULT_PROFILE = "dev";

    /**
     * Compact constructor: required collections are non-null and defensively copied behind an unmodifiable
     * view (immutable value, Item 17). The check set keeps {@code EnumSet}'s ordering and compactness but
     * is wrapped so the generated accessor cannot leak a mutable reference a caller could change under the
     * gate — exactly the kind of representation-exposure finding this module's own SpotBugs gate catches.
     */
    public AiGovernancePolicy {
        Objects.requireNonNull(sanctionedTools, "sanctionedTools");
        Objects.requireNonNull(requiredChecks, "requiredChecks");
        sanctionedTools = Set.copyOf(sanctionedTools);
        EnumSet<AiCheck> checks = requiredChecks.isEmpty()
                ? EnumSet.noneOf(AiCheck.class)
                : EnumSet.copyOf(requiredChecks);
        requiredChecks = Collections.unmodifiableSet(checks);
    }

    /**
     * Loads the policy for the profile named by {@link #PROFILE_PROPERTY} (default {@code dev}).
     *
     * @return the externalized policy for the active profile, never {@code null}
     */
    public static AiGovernancePolicy load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the policy for a named profile from its classpath properties file.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}, never {@code null}
     * @return the externalized policy for that profile, never {@code null}
     * @throws IllegalArgumentException if no properties file exists for the profile
     */
    public static AiGovernancePolicy load(String profile) {
        Objects.requireNonNull(profile, "profile");
        String resource = "aigov-" + profile + ".properties";
        Properties props = new Properties();
        try (InputStream in = AiGovernancePolicy.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("no policy for profile: " + profile);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + resource, e);
        }
        return new AiGovernancePolicy(
                parseStrings(props.getProperty("aigov.sanctioned-tools", "")),
                parseChecks(props.getProperty("aigov.required-checks", "")),
                Boolean.parseBoolean(props.getProperty("aigov.require-disclosure", "true")),
                Boolean.parseBoolean(props.getProperty("aigov.require-accountable-human", "true")),
                Boolean.parseBoolean(props.getProperty("aigov.forbid-auto-merge-on-ai-review", "true")));
    }

    private static Set<String> parseStrings(String csv) {
        Set<String> out = new LinkedHashSet<>();
        for (String token : csv.split(",")) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                out.add(trimmed);
            }
        }
        return out;
    }

    private static Set<AiCheck> parseChecks(String csv) {
        Set<AiCheck> out = EnumSet.noneOf(AiCheck.class);
        Arrays.stream(csv.split(","))
            .map(String::trim)
            .filter(token -> !token.isEmpty())
            .map(AiCheck::valueOf)
            .forEach(out::add);
        return out;
    }
}
