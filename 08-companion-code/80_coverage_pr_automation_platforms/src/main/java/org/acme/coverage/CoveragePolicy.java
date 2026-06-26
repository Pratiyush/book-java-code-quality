package org.acme.coverage;

import java.util.Objects;
import java.util.Properties;

/**
 * The externalized coverage-gate policy — the two thresholds and the ratchet switch the gate reads,
 * loaded from a profile so they are configuration, not hard-coded constants.
 *
 * <p>The chapter's three policy levers, in one value:
 * <ul>
 *   <li>{@code newCodeBar} — the minimum coverage required of new/changed lines (the diff-coverage /
 *       patch threshold; new-code focus). Pre-existing legacy is out of scope.</li>
 *   <li>{@code ratchet} — when {@code true}, overall coverage may not drop; a pull request that lowers
 *       it is blocked, bending the curve up without a big-bang test-writing project.</li>
 *   <li>{@code overallTarget} — an aspirational whole-repo number that only <em>warns</em> when unmet
 *       (never blocks), so it is a dashboard signal, not a gate that gets gamed.</li>
 * </ul>
 *
 * @param newCodeBar    the minimum new-code coverage ratio in {@code [0, 1]}
 * @param ratchet       whether overall coverage is forbidden to drop
 * @param overallTarget the aspirational overall coverage ratio in {@code [0, 1]} (warn-only)
 */
public record CoveragePolicy(double newCodeBar, boolean ratchet, double overallTarget) {

    /**
     * Canonical constructor with range guards.
     *
     * @throws IllegalArgumentException if either ratio is outside {@code [0, 1]}
     */
    public CoveragePolicy {
        requireRatio(newCodeBar, "newCodeBar");
        requireRatio(overallTarget, "overallTarget");
    }

    /**
     * Loads a policy from properties, one externalized profile (for example {@code dev} or {@code prod}).
     * Missing keys fall back to the documented defaults so a partial profile still loads.
     *
     * @param props the profile properties, never {@code null}
     * @return the policy described by {@code props}
     * @throws NullPointerException if {@code props} is {@code null}
     */
    public static CoveragePolicy from(Properties props) {
        Objects.requireNonNull(props, "props");
        double bar = readRatio(props, "coverage.newCodeBar", 0.80);
        boolean ratchet = Boolean.parseBoolean(props.getProperty("coverage.ratchet", "true"));
        double target = readRatio(props, "coverage.overallTarget", 0.85);
        return new CoveragePolicy(bar, ratchet, target);
    }

    private static double readRatio(Properties props, String key, double fallback) {
        String raw = props.getProperty(key);
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        return Double.parseDouble(raw.trim());
    }

    private static void requireRatio(double value, String name) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException(name + " must be in [0, 1]: " + value);
        }
    }
}
