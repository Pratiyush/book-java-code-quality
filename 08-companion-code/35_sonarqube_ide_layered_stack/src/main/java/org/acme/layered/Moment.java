package org.acme.layered;

/**
 * The moment an analyzer runs, ordered earliest (and cheapest feedback) to latest. The moment fixes
 * latency, not reach: the author-time first line gives the fastest feedback but guarantees nothing about
 * what reaches the repository, while the CI moment is the gate. A coherent stack runs each owner at its
 * cheapest moment and orders the whole thing cheap-first / fail-fast, so a broken change fails as early as
 * it can — the compile-bound checks before the source pass, the bytecode pass after compilation, the
 * heavier platform last. The enum's declaration order is that ordering; {@link #compareTo} reflects it.
 */
public enum Moment {

    /** At the keystroke, in the editor — the fastest feedback, but local and settings-dependent. */
    AUTHOR_TIME,

    /** During compilation — a check bound to {@code javac} that can fail the build earliest. */
    COMPILE,

    /** A source pass over the checked-out tree — style and smells, before the bytecode is read. */
    SOURCE_PASS,

    /** After compilation — a pass over the emitted bytecode. */
    POST_COMPILE,

    /** In CI, above the build — the platform aggregates, trends, and applies the gate. */
    CI
}
