package org.acme.layered;

/**
 * The substrate an analyzer reads — the program representation it sees. This choice fixes what a tool can
 * possibly detect: a source-text checker reads layout the compiler later erases; a bytecode detector reads
 * what {@code javac} emits that the source never shows; a compile-time checker has the compiler's resolved
 * types the source linters lack. None of these is smarter than another; each simply stands somewhere
 * different. That is why independent studies find Java analyzers barely agree — their coverage is additive,
 * not redundant — and why a coherent stack composes them rather than stacking re-checks of one substrate.
 */
public enum Substrate {

    /** Raw source characters — layout, naming, formatting; erased by compilation. */
    SOURCE_TEXT,

    /** The parsed source tree — structure and smells visible before compilation. */
    SOURCE_AST,

    /** The {@code javac} tree during compilation — carries the compiler's resolved type information. */
    JAVAC_AST,

    /** Compiled bytecode — bug patterns only visible after {@code javac} has run. */
    BYTECODE,

    /** An aggregating platform's own engine over source and bytecode, with trend over time. */
    PLATFORM
}
