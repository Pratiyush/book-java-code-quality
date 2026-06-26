package org.acme.coverage;

/**
 * The outcome of the coverage gate on one pull request — a sealed type rather than a bare boolean, so
 * the chapter's three distinct cases are each nameable and each carries its reason.
 *
 * <p>The three cases are the block-versus-warn policy made explicit:
 * <ul>
 *   <li>{@link Pass} — the new code clears the bar and overall coverage does not drop; merge is clear.</li>
 *   <li>{@link Warn} — surfaced but not blocking (for example overall coverage is below an aspirational
 *       target while the ratchet still holds); a finding the developer should see, not one that stops
 *       the merge.</li>
 *   <li>{@link Block} — the new code is under the bar, or overall coverage would drop (the ratchet);
 *       the merge is stopped, and the verdict carries the reason so the failure is actionable.</li>
 * </ul>
 */
public sealed interface CoverageVerdict permits CoverageVerdict.Pass,
        CoverageVerdict.Warn, CoverageVerdict.Block {

    /**
     * A human-readable reason for the verdict, suitable for a PR comment or a build log.
     *
     * @return the reason, never {@code null} or blank
     */
    String reason();

    /**
     * Whether this verdict stops the merge. Only {@link Block} does.
     *
     * @return {@code true} if the gate blocks the merge
     */
    default boolean blocks() {
        return this instanceof Block;
    }

    /** The clear-to-merge outcome. */
    record Pass(String reason) implements CoverageVerdict { }

    /** A surfaced-but-non-blocking outcome. */
    record Warn(String reason) implements CoverageVerdict { }

    /** The merge-stopping outcome; {@code reason} states which rule failed. */
    record Block(String reason) implements CoverageVerdict { }
}
