package org.acme.findings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Locale;

/**
 * Formats prices and exposes the fixed denomination ladder — the type that carries the chapter's one
 * reviewed false positive (Chapter 39, lever 1: suppress at the site, with a reason).
 *
 * <p>{@link #denominationsCents()} returns the class's internal {@code long[]} directly. SpotBugs raises
 * {@code EI_EXPOSE_REP} on it, because in general a returned array lets a caller mutate internal state.
 * Here the team has examined this exact site and judged the finding a false positive for this class: the
 * array holds fixed currency denominations, is never written after construction, and its callers are
 * trusted in-process formatting code that reads it and does not mutate it. The judgement is recorded as a
 * narrow {@link SuppressFBWarnings} with a {@code justification} on that one method.
 *
 * <p>The discipline is the narrowness. The suppression names the single pattern on the single method and
 * carries its reason. The wrong fix — the tempting one — is {@code @SuppressFBWarnings("EI_EXPOSE_REP")}
 * on the whole class, which would also silence a NEW exposed-representation bug added elsewhere in the
 * class later, turning the detector off for a whole type while looking like a one-line tidy. The
 * suppression is load-bearing: remove it and the SpotBugs gate goes red on this method.
 */
public final class PriceFormatter {

    private static final long[] DENOMINATIONS_CENTS = {1L, 5L, 10L, 25L, 100L, 500L, 1_000L, 2_000L};

    private final long[] denominationsCents;

    /** Builds a formatter over a private copy of the fixed denomination ladder. */
    public PriceFormatter() {
        this.denominationsCents = DENOMINATIONS_CENTS.clone();
    }

    // tag::reviewed-suppression[]
    // Reviewed false positive: this array is fixed currency denominations, never written after
    // construction, read by trusted in-process callers. Judged safe at THIS site — suppressed narrowly,
    // with a reason, not by disabling EI_EXPOSE_REP for the class or the project.
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "fixed denominations, never mutated by callers")
    public long[] denominationsCents() {
        return denominationsCents;
    }
    // end::reviewed-suppression[]

    /**
     * Formats a cents amount as a plain decimal string (for example {@code 599} becomes {@code "5.99"}).
     *
     * @param priceCents the amount in whole cents, never negative
     * @return the amount rendered with two decimal places
     * @throws IllegalArgumentException if {@code priceCents} is negative
     */
    public String format(long priceCents) {
        if (priceCents < 0L) {
            throw new IllegalArgumentException("priceCents must not be negative: " + priceCents);
        }
        return String.format(Locale.ROOT, "%d.%02d", priceCents / 100L, priceCents % 100L);
    }
}
