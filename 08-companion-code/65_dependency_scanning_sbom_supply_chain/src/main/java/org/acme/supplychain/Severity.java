package org.acme.supplychain;

/**
 * Finding severity, mapped to the CVSS bands an SCA gate keys on. A scanner emits a CVSS score per
 * finding; a gate fails the build above a chosen threshold (the chapter's "fail on a severity threshold").
 * The bands here follow the common CVSS v3 qualitative ranges so the gate's threshold is legible.
 */
public enum Severity {

    /** CVSS 0.1–3.9. */
    LOW(3.9),
    /** CVSS 4.0–6.9. */
    MEDIUM(6.9),
    /** CVSS 7.0–8.9. */
    HIGH(8.9),
    /** CVSS 9.0–10.0. */
    CRITICAL(10.0);

    private final double maxScore;

    Severity(double maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * The top of this band's CVSS range — used to compare a finding against a numeric gate threshold.
     *
     * @return the maximum CVSS score in this band
     */
    public double maxScore() {
        return maxScore;
    }

    /**
     * Whether a finding in this band meets or exceeds a CVSS threshold (e.g. a gate set to fail at 7.0
     * blocks {@link #HIGH} and {@link #CRITICAL}). The comparison is against the band's top score, so a
     * band is "at or above" a threshold when any score in it could be.
     *
     * @param cvssThreshold the gate's fail threshold (e.g. {@code 7.0})
     * @return {@code true} if this band reaches the threshold
     */
    public boolean meetsOrExceeds(double cvssThreshold) {
        return maxScore >= cvssThreshold;
    }
}
