package org.acme.refstack;

/**
 * The severity a stage's worst finding carries, ordered low to high. The composed ship decision turns on
 * this together with the finding's scope: the gate blocks only on objective, high-severity findings on
 * new code and treats the rest as advisory, so a no-ship verdict always means something real rather than
 * drowning the developer in noise the team has learned to ignore (Chapter 19).
 *
 * <p>This enum is defined in this module rather than shared from another: each companion module is
 * self-contained and imports no other module's code, so a reader can build this chapter on its own.
 */
public enum Severity {

    /** Informational — never blocks; recorded so a trend can be watched. */
    INFO,

    /** A minor or stylistic issue — advisory, not blocking, to keep the gate credible. */
    LOW,

    /** A notable issue — advisory by default; a team may raise it to blocking once false positives are low. */
    MEDIUM,

    /** An objective, high-confidence defect or vulnerability — the severity the gate blocks on. */
    HIGH
}
