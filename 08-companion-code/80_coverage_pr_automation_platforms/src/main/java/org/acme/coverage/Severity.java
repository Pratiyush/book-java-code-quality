package org.acme.coverage;

/**
 * A finding's severity, ordered low to high, so the PR-comment policy can filter below a floor.
 *
 * <p>The order is the ordinal order of the enum constants; {@code INFO < MINOR < MAJOR < CRITICAL}.
 * The policy keeps a finding only when its severity is at or above the configured floor — the
 * severity-filter half of keeping a bot signal rather than noise.
 */
public enum Severity {
    /** Informational; rarely worth a PR comment on its own. */
    INFO,
    /** A minor issue. */
    MINOR,
    /** A significant issue. */
    MAJOR,
    /** A critical issue. */
    CRITICAL;

    /**
     * Whether this severity is at or above {@code floor}.
     *
     * @param floor the minimum severity to keep, never {@code null}
     * @return {@code true} if this severity is at least as high as {@code floor}
     */
    public boolean atLeast(Severity floor) {
        return this.ordinal() >= floor.ordinal();
    }
}
