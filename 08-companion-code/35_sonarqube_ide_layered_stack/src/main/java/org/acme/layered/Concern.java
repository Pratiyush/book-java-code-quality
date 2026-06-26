package org.acme.layered;

/**
 * A quality concern a stack must cover. The composition rule of the chapter is to assign each concern to
 * exactly ONE primary owner, so every finding comes from a single place rather than the same style nit
 * being reported by three tools. These are the concerns the chapter's one-owner-per-concern map names;
 * the map is a deliberate choice, not the only valid one, so the owner of a concern is configuration, not
 * law (a team may let one tool own a concern another could also cover).
 */
public enum Concern {

    /** Layout and whitespace — owned by a formatter so it is mechanical, not a review argument. */
    FORMATTING,

    /** Naming and convention — owned by a source-text/AST style checker. */
    STYLE_CONVENTION,

    /** Source smells and duplication — owned by a source-AST smell detector. */
    SOURCE_SMELLS,

    /** Compile-time correctness with auto-fix — owned by a check bound to {@code javac}. */
    COMPILE_CORRECTNESS,

    /** Bytecode bug patterns — owned by a detector that reads compiled bytecode. */
    BYTECODE_BUGS,

    /** Null-safety — owned by a dedicated null-checking tool. */
    NULL_SAFETY,

    /** Trend, debt, and the gate over time — owned by an aggregating platform. */
    TREND_DEBT_GATE
}
