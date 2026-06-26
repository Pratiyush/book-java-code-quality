package org.acme.repro;

import java.util.Objects;

/**
 * The in-code analogue of a reproducible build: an artifact identified by the digest of its bytes, where
 * "reproducible" means rebuilding the same source produces a byte-identical artifact and therefore the
 * same digest. This is the property the build configuration enforces for real (a fixed
 * {@code project.build.outputTimestamp} and the reproducible-build plugin in {@code pom.xml}); here it is
 * a data structure a test can drive to show what "verify by rebuilding" decides.
 *
 * <p>The chapter's verification step is {@link #matches(ReproducibleArtifact)}: build twice and compare
 * the digests. The honest edge is encoded, not just stated — {@link #isIntegrityNotCorrectness()} records
 * that a reproducible build proves the bytes are a pure function of source, and <em>nothing</em> about
 * whether those bytes are correct. A reproducible build of buggy code is reliably, verifiably buggy.
 */
public final class ReproducibleArtifact {

    private final String name;
    private final String digest;

    /**
     * @param name   the artifact's coordinate or file name (e.g. {@code app-1.0.0.jar})
     * @param digest the digest of the artifact's bytes (e.g. a SHA-256 hex string); two builds of the same
     *               source are reproducible exactly when this value is identical
     */
    public ReproducibleArtifact(String name, String digest) {
        this.name = Objects.requireNonNull(name, "name");
        this.digest = Objects.requireNonNull(digest, "digest");
        if (digest.isBlank()) {
            throw new IllegalArgumentException("digest must not be blank");
        }
    }

    /**
     * The verification step in code: two artifacts built from the same source are reproducible when their
     * digests match (the in-code analogue of build-twice-and-diff / {@code mvn artifact:compare}). A
     * mismatch is the signal that some non-determinism (a timestamp, file ordering, an unpinned input) has
     * crept back in — the difference a CI verify step treats as a build failure.
     *
     * @param other an artifact built from the same source, independently
     * @return {@code true} when the two are byte-identical (equal digests)
     */
    // tag::repro-verify[]
    public boolean matches(ReproducibleArtifact other) {
        Objects.requireNonNull(other, "other");
        return this.digest.equals(other.digest);   // build twice, same source -> same bytes -> same digest
    }
    // end::repro-verify[]

    /**
     * States the chapter's honest limit in code: reproducibility proves <em>integrity and determinism</em>,
     * not <em>correctness</em>. Always {@code true} — it is a property of what reproducibility is, surfaced
     * so a caller (or a reader) cannot mistake "the build is reproducible" for "the code is right".
     *
     * @return {@code true} — a reproducible build of buggy code is still buggy
     */
    public boolean isIntegrityNotCorrectness() {
        return true;
    }

    /**
     * Readiness probe: a reproducibility claim is verifiable only once the artifact actually has a digest
     * to compare. An artifact with no recorded digest can neither be matched nor attested.
     *
     * @return {@code true} once a non-blank digest is recorded
     */
    public boolean isVerifiable() {
        return !digest.isBlank();
    }

    /**
     * Observability surface: the digest a provenance attestation (Chapter 28) signs over. Publishing it
     * lets a third party rebuild from source and confirm the published artifact matches — the link that
     * gives provenance its meaning. The value is worth watching: a changed digest for unchanged source is
     * exactly the silent decay reproducibility's CI verify step exists to catch.
     *
     * @return the artifact's digest
     */
    public String digest() {
        return digest;
    }

    /**
     * @return the artifact's name
     */
    public String name() {
        return name;
    }
}
