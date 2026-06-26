package org.acme.cigate;

/**
 * The severity a finding carries, ordered low to high. The quality gate's block-versus-warn decision
 * turns on this together with the finding's scope: the gate blocks only on objective, high-severity
 * findings and warns on the rest, so a red gate always means something real rather than drowning the
 * developer in noise it has learned to ignore (Chapter 19).
 */
public enum Severity {

    /** Informational — never blocks; recorded so a trend can be watched. */
    INFO,

    /** A minor or stylistic issue — warned, not blocked, to keep the gate credible. */
    LOW,

    /** A notable issue — warned by default; a team may raise it to blocking once false positives are low. */
    MEDIUM,

    /** An objective, high-confidence defect or vulnerability — the severity the gate blocks on. */
    HIGH
}
