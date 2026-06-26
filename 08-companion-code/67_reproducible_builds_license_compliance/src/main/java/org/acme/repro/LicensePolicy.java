package org.acme.repro;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The in-code analogue of the license allow/deny gate (the chapter's "stop a banned license shipping"
 * answer). Given the licenses read off the dependency tree, the policy decides pass or block exactly as
 * the {@code license-maven-plugin} does against an allow-list (the {@code -Pquality} profile in
 * {@code pom.xml}). It folds in the chapter's honest limits:
 *
 * <ul>
 *   <li><b>Tuned to distribution mode</b> — the allow-list is an input, not a universal truth. A license
 *       fine for an internal tool can be a problem in a redistributed binary, and {@code AGPL} reaches
 *       even a SaaS deployment, so the set of allowed identifiers belongs to the product's distribution
 *       mode, not to the tool.</li>
 *   <li><b>Scan the full graph</b> — the dangerous case is a permissive direct dependency pulling a
 *       copyleft transitive, so the policy evaluates every license in the resolved set, not just direct
 *       ones (the chapter's transitive-surprise warning).</li>
 * </ul>
 *
 * <p>The gate reports against <em>declared</em> identifiers; detection is imperfect and a declared id is
 * not a legal conclusion. It is <strong>factual, not legal advice</strong>: it flags what to look at, it
 * does not interpret an obligation. The policy is configured once and reused; it is stateless apart from
 * its allow-list, so a given license list always yields the same decision.
 */
public final class LicensePolicy {

    private final Set<String> allowedSpdxIds;

    /**
     * @param allowedSpdxIds the SPDX identifiers permitted for this product's distribution mode
     *                       (e.g. {@code Apache-2.0}, {@code MIT}, {@code BSD-3-Clause})
     */
    public LicensePolicy(Set<String> allowedSpdxIds) {
        this.allowedSpdxIds = Set.copyOf(Objects.requireNonNull(allowedSpdxIds, "allowedSpdxIds"));
    }

    // tag::license-allow-list[]
    /**
     * A license is disallowed when its declared SPDX id is not in the allow-list for this distribution
     * mode. The decision is by identifier against a tuned set, never a blanket category ban.
     */
    public boolean isDisallowed(License license) {
        Objects.requireNonNull(license, "license");
        return !allowedSpdxIds.contains(license.spdxId());
    }
    // end::license-allow-list[]

    /**
     * Evaluates the licenses read off the full dependency graph. Throws {@link DisallowedLicenseException}
     * naming every disallowed entry if any exist — the failure path that fails the build. A tree whose
     * licenses are all allowed returns normally; that is not a clean legal bill of health, only the
     * absence of a <em>declared</em> identifier outside the policy (detection is imperfect; obligations
     * still need counsel).
     *
     * @param licenses the declared licenses of every component in the resolved graph
     * @throws DisallowedLicenseException if one or more licenses fall outside the allow-list
     */
    public void evaluate(List<License> licenses) {
        Objects.requireNonNull(licenses, "licenses");
        List<License> disallowed = licenses.stream().filter(this::isDisallowed).toList();
        if (!disallowed.isEmpty()) {
            throw new DisallowedLicenseException(disallowed);
        }
    }

    /**
     * @return the allowed SPDX identifiers for this policy
     */
    public Set<String> allowedSpdxIds() {
        return allowedSpdxIds;
    }
}
