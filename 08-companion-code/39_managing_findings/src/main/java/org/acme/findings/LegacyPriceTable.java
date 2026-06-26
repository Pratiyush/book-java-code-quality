package org.acme.findings;

/**
 * Legacy code that predates the gate — the "frozen past" of Chapter 39.
 *
 * <p>This class holds a price-tier array and returns it directly from {@link #rawTiers()}, so a caller
 * can mutate the table through the returned reference. At this module's SpotBugs threshold that raises
 * {@code EI_EXPOSE_REP} (and the store-side {@code EI_EXPOSE_REP2} for the array taken in by the
 * constructor). The finding is real debt, not a false positive: the right fix is the defensive copy the
 * clean {@link PricingCatalog} makes.
 *
 * <p>It is left as-is on purpose. The chapter's point is that a real codebase carries debt the team
 * cannot fix on day one, so the finding is FROZEN in the SpotBugs baseline ({@code
 * config/spotbugs/spotbugs-exclude.xml}) rather than fixed here — which lets the gate be adopted today
 * and react only to new findings. The baseline is load-bearing: remove its {@code <Match>} for this class
 * and the SpotBugs gate goes red. Freezing defers the fix; it does not pretend the debt is gone.
 */
public final class LegacyPriceTable {

    private final long[] tierCents;

    /**
     * Builds the table over the given tier prices.
     *
     * @param tierCents the price tiers in whole cents
     */
    public LegacyPriceTable(long[] tierCents) {
        // Pre-existing pattern: stores the caller's array reference directly (EI_EXPOSE_REP2). Frozen, not fixed.
        this.tierCents = tierCents;
    }

    /**
     * Returns the internal tier array directly — the exposed-representation finding this module freezes.
     *
     * @return the live internal array (callers can mutate the table through it — the latent bug)
     */
    public long[] rawTiers() {
        // Pre-existing pattern: hands out the internal array (EI_EXPOSE_REP). Frozen in the baseline.
        return tierCents;
    }

    /**
     * The number of price tiers.
     *
     * @return the tier count
     */
    public int size() {
        return tierCents.length;
    }
}
