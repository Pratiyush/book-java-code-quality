package org.acme.release;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A semantic version (Chapter 26 / key 60): {@code MAJOR.MINOR.PATCH} with an optional pre-release
 * suffix, the contract from semver.org that communicates the nature of a change — MAJOR for a breaking
 * change, MINOR for an additive one, PATCH for a fix. The release-readiness gate uses this to enforce
 * one of its preconditions: a release carries a release version, never a development {@code -SNAPSHOT}.
 *
 * <p>The {@code -SNAPSHOT} suffix is Maven's convention for an in-development, not-yet-released build:
 * it is a pre-release identifier, so a coordinate ending in {@code -SNAPSHOT} is by definition not a
 * release. Shipping a snapshot is the mistake the gate exists to catch — a snapshot is a moving target
 * that resolves differently over time, which is exactly the irreproducibility a release must not have
 * (Chapter 29). This type reads only what semver.org establishes — three numeric identifiers and an
 * optional pre-release label — and decides nothing about behaviour; comparing two versions for binary
 * compatibility is the job of the API-diff tools (key 60), not of a version string.
 *
 * @param major    the MAJOR component — incremented for a breaking change
 * @param minor    the MINOR component — incremented for a backward-compatible addition
 * @param patch    the PATCH component — incremented for a backward-compatible fix
 * @param preRelease the pre-release label without its leading hyphen (for example {@code SNAPSHOT} or
 *                   {@code rc.1}), or {@code null} for a release version
 */
public record SemanticVersion(int major, int minor, int patch, String preRelease) {

    /** Maven's pre-release identifier for an in-development build; a version carrying it is not a release. */
    public static final String SNAPSHOT = "SNAPSHOT";

    // MAJOR.MINOR.PATCH with an optional -preRelease tail. A pragmatic subset of the semver.org grammar:
    // enough to separate a release from a pre-release, which is all the release gate needs to decide.
    private static final Pattern SEMVER =
        Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?:-([0-9A-Za-z.-]+))?$");

    /** Compact constructor: the three numeric components are required to be non-negative. */
    public SemanticVersion {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("version components must be non-negative");
        }
    }

    /**
     * Parses a semantic-version string such as {@code 2.4.1} or {@code 2.5.0-SNAPSHOT}.
     *
     * @param text the version text, never {@code null}
     * @return the parsed version, never {@code null}
     * @throws IllegalArgumentException if the text is not a {@code MAJOR.MINOR.PATCH[-preRelease]} version
     */
    public static SemanticVersion parse(String text) {
        var matcher = SEMVER.matcher(Objects.requireNonNull(text, "text"));
        if (!matcher.matches()) {
            throw new IllegalArgumentException("not a semantic version: " + text);
        }
        return new SemanticVersion(
            Integer.parseInt(matcher.group(1)),
            Integer.parseInt(matcher.group(2)),
            Integer.parseInt(matcher.group(3)),
            matcher.group(4));
    }

    /**
     * Whether this is a release version — one with no pre-release suffix at all. A release gate ships
     * only releases; a {@code -SNAPSHOT} or {@code -rc.1} is a pre-release and is rejected.
     *
     * @return {@code true} if this version carries no pre-release identifier
     */
    // tag::semver-release[]
    public boolean isRelease() {
        return preRelease == null;        // a release has no pre-release suffix (semver.org)
    }

    /** Whether this version carries Maven's {@code -SNAPSHOT} development suffix. */
    public boolean isSnapshot() {
        return SNAPSHOT.equals(preRelease); // a -SNAPSHOT is an in-development build, never a release
    }
    // end::semver-release[]

    @Override
    public String toString() {
        String core = major + "." + minor + "." + patch;
        return preRelease == null ? core : core + "-" + preRelease;
    }
}
