package org.acme.storefront.money;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * The externalized configuration the money-representation rules read, with a {@code dev} and a
 * {@code prod} profile (EXAMPLES-GUIDE requirement 2: profiles, not hard-coded values). The two
 * profiles differ in the severity at which a breach is reported — {@code WARNING} for adoption,
 * {@code ERROR} for a gate — mirroring the dossier's "ship WARNING first, then promote to ERROR"
 * on-ramp. The same separation the analyzers use between a rule's logic and its configuration is kept
 * here: the rule code reads its severity and field-name pattern from {@code money-policy.properties}
 * rather than embedding them.
 */
public final class MoneyPolicy {

    /** How a reported breach is classified — the configurable knob, not a fixed value. */
    public enum Severity {
        /** Reported as advice; a build adopting the rule does not fail on it yet. */
        WARNING,
        /** Reported as a gate breach; a build fails on it. */
        ERROR
    }

    private static final String RESOURCE = "money-policy.properties";

    private final String profile;
    private final Severity severity;
    private final Pattern moneyNamePattern;

    private MoneyPolicy(String profile, Severity severity, Pattern moneyNamePattern) {
        this.profile = profile;
        this.severity = severity;
        this.moneyNamePattern = moneyNamePattern;
    }

    /**
     * Loads the named profile from {@code money-policy.properties} on the classpath.
     *
     * @param profile the profile name, {@code "dev"} or {@code "prod"}, never {@code null}
     * @return the policy for that profile
     * @throws IllegalArgumentException if the profile is not configured
     */
    public static MoneyPolicy forProfile(String profile) {
        Objects.requireNonNull(profile, "profile");
        Properties props = load();
        String severityKey = profile + ".severity";
        String patternKey = profile + ".moneyNamePattern";
        String severityValue = props.getProperty(severityKey);
        String patternValue = props.getProperty(patternKey);
        if (severityValue == null || patternValue == null) {
            throw new IllegalArgumentException("no money policy configured for profile: " + profile);
        }
        return new MoneyPolicy(profile, Severity.valueOf(severityValue.trim()),
            Pattern.compile(patternValue.trim()));
    }

    /** The production profile: a breach is an {@code ERROR} that fails the gate. */
    public static MoneyPolicy prod() {
        return forProfile("prod");
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = MoneyPolicy.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("missing classpath resource: " + RESOURCE);
            }
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new UncheckedIOException("could not read " + RESOURCE, e);
        }
    }

    /** @return the profile name this policy was loaded from */
    public String profile() {
        return profile;
    }

    /** @return the severity a breach is reported at under this profile */
    public Severity severity() {
        return severity;
    }

    /**
     * Whether a member name looks like money for the name-based source-style rule.
     *
     * @param memberName the field or parameter name, never {@code null}
     * @return {@code true} if the name matches the configured money pattern
     */
    public boolean looksLikeMoney(String memberName) {
        return moneyNamePattern.matcher(Objects.requireNonNull(memberName, "memberName")).matches();
    }
}
