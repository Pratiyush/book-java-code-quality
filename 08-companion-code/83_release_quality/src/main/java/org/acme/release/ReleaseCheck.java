package org.acme.release;

/**
 * One precondition a release artifact must satisfy before it ships — the release gates the chapter names,
 * made enumerable so a policy can require a subset of them and a blocked decision can say exactly which
 * ones failed. These are artifact-level checks: they verify the release is the green, traceable thing the
 * pipeline produced, not that the code is correct (the analyzers, tests, and human review upstream own
 * that — a green release gate is not good code).
 */
public enum ReleaseCheck {

    /** The version is a release, not a {@code -SNAPSHOT} or other pre-release (semver, key 60). */
    RELEASE_VERSION,

    /** The changelog carries an entry for the version being released (Keep a Changelog). */
    CHANGELOG_ENTRY,

    /** Every CI gate was green on the exact release commit (the pipeline of Chapters 33–35). */
    CI_GREEN,

    /** The artifact is signed and attested with an SBOM generated (SLSA / cosign, Part VII, key 66). */
    SIGNED_WITH_SBOM,

    /** A smoke test passed against the staged build — the packaged artifact starts and serves. */
    SMOKE_TESTED
}
