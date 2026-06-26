package org.acme.findings;

/**
 * The triage decision (Chapter 39): map one finding to the one lever it calls for. Every finding is
 * exactly one of {@link Disposition#FIX}, {@link Disposition#SUPPRESS}, or {@link Disposition#BASELINE};
 * the decision the tool cannot make for itself is encoded in the {@link Finding}'s judgment fields.
 *
 * <p>The ordering is the chapter's narrow-to-broad ladder read as a decision: a finding judged a false
 * positive is suppressed at the site (with the reason the {@link Finding} already required); a finding
 * that predates the gate is frozen in the baseline; everything else is a real defect to fix. Breadth is
 * never the default — a baseline is reserved for the explicitly pre-existing case, not used to make a new
 * finding go away.
 */
public final class FindingTriage {

    private FindingTriage() {
    }

    /**
     * Decides the lever for one finding.
     *
     * @param finding the finding to triage
     * @return the disposition: fix it, suppress it with its recorded reason, or baseline pre-existing debt
     */
    // tag::triage-decision[]
    public static Disposition triage(Finding finding) {
        if (finding.falsePositive()) {
            return Disposition.SUPPRESS;   // tool wrong (or cost accepted) — silence the site, with a reason
        }
        if (finding.preExisting()) {
            return Disposition.BASELINE;   // debt that predates the gate — freeze it, then block new debt
        }
        return Disposition.FIX;            // a real, new defect — the only lever that removes it
    }
    // end::triage-decision[]
}
