package org.acme.aigovernance;

/**
 * The observability surface for AI adoption: a read-only signal that refuses to report a productivity
 * figure without its counter-metric. The chapter's discipline, straight from the metrics chapters
 * (Chapter 1 / 38): industry surveys report large majorities seeing higher productivity with AI
 * assistants AND a substantial fraction seeing higher risk, so a team that celebrates the velocity
 * number while ignoring its change-failure rate is measuring exactly half the picture — the seductive
 * half — and Goodhart does the rest.
 *
 * <p>This signal takes both numbers as INPUTS and returns a verdict; it bakes in no statistic of its own.
 * That is deliberate and load-bearing: every AI-productivity and AI-review figure the chapter cites is a
 * dated, attributed, often vendor-sourced snapshot (verified at the pin), never a constant to reason from
 * — so the code reasons over a team's OWN measured numbers, not a number copied from a vendor's deck. A
 * dashboard wires this against the team's real DORA change-failure rate (Chapter 38) and its measured
 * delivery speed.
 *
 * @param adoptionPercent     measured share of changes that were AI-assisted, in {@code [0, 100]}
 * @param changeFailureRate   the team's measured DORA change-failure rate, a fraction in {@code [0, 1]}
 * @param baselineFailureRate the change-failure rate before AI adoption, a fraction in {@code [0, 1]}
 */
public record AiAdoptionCounterMetric(
        double adoptionPercent,
        double changeFailureRate,
        double baselineFailureRate) {

    /** Compact constructor: ranges are validated so an impossible measurement can never be reported. */
    public AiAdoptionCounterMetric {
        if (adoptionPercent < 0 || adoptionPercent > 100) {
            throw new IllegalArgumentException("adoptionPercent must be in [0,100], was " + adoptionPercent);
        }
        requireFraction(changeFailureRate, "changeFailureRate");
        requireFraction(baselineFailureRate, "baselineFailureRate");
    }

    private static void requireFraction(double value, String name) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException(name + " must be in [0,1], was " + value);
        }
    }

    /** A coarse verdict pairing adoption with its risk counter-metric. */
    public enum Verdict {
        /** AI adoption is rising and change-failure rate is not — adoption is being governed well. */
        HEALTHY,
        /** Adoption is rising and change-failure rate is rising with it — the counter-metric is firing. */
        RISK_RISING,
        /** No meaningful AI adoption reported — nothing to counter-metric yet. */
        LOW_ADOPTION
    }

    /**
     * Reports the counter-metric verdict: adoption is only "healthy" when it has NOT come with a rise in
     * the change-failure rate over the pre-adoption baseline. Velocity alone never earns a healthy
     * verdict here — that is the whole point.
     *
     * @return the verdict, never {@code null}
     */
    public Verdict verdict() {
        if (adoptionPercent < 10.0) {
            return Verdict.LOW_ADOPTION;
        }
        return changeFailureRate > baselineFailureRate ? Verdict.RISK_RISING : Verdict.HEALTHY;
    }
}
