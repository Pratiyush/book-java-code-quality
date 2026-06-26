package org.acme.findings;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The ratchet (Chapter 39, lever 4: "clean as you code"). A baseline freezes the findings that exist
 * today; the ratchet lets those persist but blocks NEW ones, so the finding set can only shrink. This is
 * the finding-set form of the same idea a SpotBugs {@code baselineFiles} run or a Sonar new-code gate
 * implements — modelled here so a test can prove the behaviour the prose describes.
 *
 * <p>A finding's identity, for baseline purposes, is its rule key plus its location (its {@link
 * #key(Finding)}). The baseline is the set of those keys captured at adoption time. {@link
 * #newFindings(List)} returns the findings whose key is not in the baseline — exactly the set that should
 * fail the build. A finding equal to a baselined one is grandfathered; a genuinely new one is not.
 *
 * <p>The honest edge the chapter states, and this type does not hide: a baseline that freezes a finding
 * also freezes any real bug behind the same key, and a key built from location drifts when code moves, so
 * a refactor can let a real finding slip in under a stale match. The ratchet defers legacy remediation by
 * design; it never forces it. Paying the baseline down is a separate, deliberate act.
 */
public final class FindingRatchet {

    private final Set<String> baseline;

    /**
     * Freezes the given findings as the baseline.
     *
     * @param baselined the findings that exist at adoption time; must not be {@code null}
     */
    public FindingRatchet(List<Finding> baselined) {
        Objects.requireNonNull(baselined, "baselined");
        this.baseline = baselined.stream().map(FindingRatchet::key).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * The findings that must fail the build: those present now but absent from the baseline.
     *
     * @param current the current findings from a fresh analysis run; must not be {@code null}
     * @return the new findings (never {@code null}; empty when nothing new appeared)
     */
    // tag::ratchet[]
    public List<Finding> newFindings(List<Finding> current) {
        Objects.requireNonNull(current, "current");
        return current.stream()
                .filter(finding -> !baseline.contains(key(finding)))  // grandfather the frozen past
                .collect(Collectors.toList());                        // what is left is NEW — it fails
    }
    // end::ratchet[]

    /**
     * Whether a fresh run introduces no new findings — the green condition for the ratchet.
     *
     * @param current the current findings; must not be {@code null}
     * @return {@code true} when every current finding is grandfathered by the baseline
     */
    public boolean passes(List<Finding> current) {
        return newFindings(current).isEmpty();
    }

    /** The number of frozen findings — the count a baseline ratchet drives down over time. */
    public int baselineSize() {
        return baseline.size();
    }

    private static String key(Finding finding) {
        return finding.ruleKey() + "@" + finding.location();
    }
}
