package org.acme.hygiene;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * One resolved dependency coordinate, pinned to an exact version.
 *
 * <p>A value type for the chapter's hygiene rule "no moving versions": the constructor rejects the
 * non-reproducible version tokens {@code LATEST}, {@code RELEASE}, and open ranges (a leading
 * {@code [}, {@code (}, or a trailing {@code +}) at the call site, so a moving version cannot enter a
 * catalog of pinned coordinates unnoticed. This mirrors at the value level what the Maven Enforcer
 * does at the build level; the two are complementary, not a substitute for one another.
 *
 * @param groupId    the Maven group id, never blank
 * @param artifactId the Maven artifact id, never blank
 * @param version    an exact pinned version, never a range or a moving alias
 */
public record PinnedDependency(String groupId, String artifactId, String version) {

    /** Matches the non-reproducible version forms the chapter bans (ranges and moving aliases). */
    private static final Pattern MOVING_VERSION =
        Pattern.compile("^[\\[(].*|.*[+]$|(?i)LATEST|(?i)RELEASE");

    /**
     * Validates the coordinate and rejects any non-reproducible version.
     *
     * @throws NullPointerException     if any component is {@code null}
     * @throws IllegalArgumentException if any component is blank, or the version is a range or a
     *                                  moving alias ({@code LATEST}/{@code RELEASE}/{@code x.y.+})
     */
    public PinnedDependency {
        Objects.requireNonNull(groupId, "groupId");
        Objects.requireNonNull(artifactId, "artifactId");
        Objects.requireNonNull(version, "version");
        if (groupId.isBlank() || artifactId.isBlank() || version.isBlank()) {
            throw new IllegalArgumentException("coordinate components must not be blank");
        }
        if (MOVING_VERSION.matcher(version).matches()) {
            throw new IllegalArgumentException(
                "version must be pinned, not a range or moving alias: " + version);
        }
    }

    /** The {@code groupId:artifactId} key a single source of version truth is keyed by. */
    public String managementKey() {
        return groupId + ":" + artifactId;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }
}
