package org.acme.release;

import java.util.Objects;

/**
 * The thing a team is about to release, described by the facts a release gate decides on. It is the
 * input to {@link ReleaseReadiness}: the version being cut, and the observed state of each release
 * precondition — whether CI was green on the release commit, whether the changelog has an entry for the
 * version, whether the artifact is signed with an SBOM, and whether the staged build passed its smoke
 * test. These booleans stand in for steps that, in a real pipeline, are evidence gathered from other
 * systems (the CI run's status, the signing step's attestation, the smoke job's exit code); the gate's
 * job is to decide, not to gather, so the candidate carries the evidence and the gate applies the policy.
 *
 * <p>An immutable value (Item 17): a candidate is a snapshot of one release attempt at one moment, so it
 * never changes after it is assembled.
 *
 * @param version    the version being released, never {@code null}
 * @param changelogHasEntry whether the changelog carries an entry for {@code version}
 * @param ciGreen    whether every CI gate was green on the release commit
 * @param signedWithSbom whether the artifact is signed/attested with an SBOM (Part VII, key 66)
 * @param smokeTested whether a smoke test passed against the staged build
 */
public record ReleaseCandidate(
    SemanticVersion version,
    boolean changelogHasEntry,
    boolean ciGreen,
    boolean signedWithSbom,
    boolean smokeTested) {

    /** Compact constructor: the version is required, so a candidate without one can never exist. */
    public ReleaseCandidate {
        Objects.requireNonNull(version, "version");
    }

    /**
     * Reports whether the observed state satisfies a single named check. The gate calls this for each
     * check its policy requires.
     *
     * @param check the precondition to test, never {@code null}
     * @return {@code true} if this candidate satisfies that check
     */
    public boolean satisfies(ReleaseCheck check) {
        return switch (Objects.requireNonNull(check, "check")) {
            case RELEASE_VERSION -> version.isRelease();
            case CHANGELOG_ENTRY -> changelogHasEntry;
            case CI_GREEN -> ciGreen;
            case SIGNED_WITH_SBOM -> signedWithSbom;
            case SMOKE_TESTED -> smokeTested;
        };
    }
}
