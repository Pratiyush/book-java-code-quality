package org.acme.secgate;

/**
 * The severity a security finding carries, ordered low to high. The security gate's block-versus-warn
 * decision turns on this together with the finding's scope: the gate blocks only on high-severity new
 * findings and routes the rest to a reviewer, so a red gate always means something genuinely serious in
 * code the change just touched rather than drowning the team in noise it learns to ignore (Chapter 19).
 */
public enum Severity {

    /** Informational — never blocks; recorded so a trend can be watched. */
    INFO,

    /** A minor issue — routed for review, not blocked, to keep the gate credible. */
    LOW,

    /** A notable issue — routed for review; a team may raise it to blocking once false positives are low. */
    MEDIUM,

    /** A high-confidence, high-severity vulnerability — the severity the security gate blocks on. */
    HIGH;

    /**
     * Whether this severity is at or above a policy's block threshold.
     *
     * @param threshold the lowest severity that may block the build, never {@code null}
     * @return {@code true} if a finding at this severity reaches the threshold
     */
    public boolean meetsOrExceeds(Severity threshold) {
        return compareTo(threshold) >= 0;
    }
}
