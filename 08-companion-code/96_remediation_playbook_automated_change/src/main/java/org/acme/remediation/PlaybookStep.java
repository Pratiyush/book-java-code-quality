package org.acme.remediation;

/**
 * The remediation playbook as an ordered sequence (Chapter 40). The order is the strategy: reaching for
 * techniques at random does not tame an inherited disaster. Declaring the constants in order makes {@link
 * #values()} the canonical sequence the plan validates against, so a plan that skips the safety net or
 * pays down before gating new code is a detectable error rather than a matter of taste.
 *
 * <p>Each constant names a step and points (in prose, not in code — there is no cross-module coupling) at
 * the chapter that owns the technique it sequences.
 */
public enum PlaybookStep {

    // tag::playbook-order[]
    ASSESS_AND_BASELINE,        // stand the tools up report-only, baseline the past, measure hotspots
    GATE_NEW_CODE,              // new-code gates: no new debt, even before old debt is paid
    SAFETY_NET,                 // characterization tests and seams before changing anything
    HOTSPOT_PAYDOWN,            // pay down hotspots first (churn x pain) — refactor, automate, migrate
    STRANGLE_UNSALVAGEABLE,     // strangler-fig only what is genuinely beyond refactoring
    SUSTAIN;                    // dashboards, culture, ownership — make the trend stick
    // end::playbook-order[]

    /**
     * Whether this step must precede {@code other} in any sound program. Earlier in the declared sequence
     * means earlier in the playbook.
     *
     * @param other the step to compare against
     * @return {@code true} if this step comes strictly before {@code other}
     */
    public boolean precedes(PlaybookStep other) {
        return ordinal() < other.ordinal();
    }
}
