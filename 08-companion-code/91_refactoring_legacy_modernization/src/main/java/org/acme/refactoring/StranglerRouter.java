package org.acme.refactoring;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;

/**
 * Routes each quote to the legacy or the modern calculator behind a flag — the strangler-fig pattern at
 * system scale, sketched small.
 *
 * <p>Fowler's strangler fig grows a new implementation around the old one and routes functionality from
 * old to new incrementally, so the system delivers value throughout and there is no big-bang cutover.
 * This router is the façade that decides, per request, which implementation serves it: with the flag
 * off it calls the legacy {@link LegacyShippingCalculator}; with the flag on it calls the modern
 * {@link ShippingCalculator}. The flag is the rollback lever — a migrated slice can be turned back off
 * if the new path misbehaves (the feature-flag discipline of Chapter 35) — and because the
 * behaviour-preservation test proves the two paths agree, flipping it changes the implementation, not
 * the result.
 *
 * <p>The honest edge the chapter names lives here too: a strangler must be <em>finished</em>. While both
 * paths run, the team maintains the legacy calculator, the modern one, and this routing layer at once;
 * the worst outcome is a half-strangled stall where the cutover never completes. This façade is the
 * mechanism, not a licence to leave it dual-running forever.
 */
public final class StranglerRouter {

    private static final Logger LOG = System.getLogger(StranglerRouter.class.getName());

    private final LegacyShippingCalculator legacy;
    private final ShippingCalculator modern;
    private final boolean routeToModern;

    /**
     * Creates a router over both implementations and the cutover flag.
     *
     * @param legacy        the legacy calculator (the old growth), never {@code null}
     * @param modern        the modern calculator (the new growth), never {@code null}
     * @param routeToModern {@code true} to route this slice to the modern path, {@code false} to keep it
     *                      on the legacy path (the rollback position)
     * @throws NullPointerException if {@code legacy} or {@code modern} is {@code null}
     */
    public StranglerRouter(LegacyShippingCalculator legacy, ShippingCalculator modern, boolean routeToModern) {
        this.legacy = Objects.requireNonNull(legacy, "legacy");
        this.modern = Objects.requireNonNull(modern, "modern");
        this.routeToModern = routeToModern;
    }

    /**
     * Quotes a parcel through whichever implementation the flag selects, returning the same typed
     * {@link Quote} either way so callers do not see which path served them.
     *
     * @param parcel the parcel to price, never {@code null}
     * @return the quote, never {@code null}
     * @throws NullPointerException if {@code parcel} is {@code null}
     */
    public Quote quote(Parcel parcel) {
        Objects.requireNonNull(parcel, "parcel");
        if (routeToModern) {
            LOG.log(Level.DEBUG, "routing parcel to modern path: {0}", parcel.destination());
            return modern.quote(parcel);
        }
        LOG.log(Level.DEBUG, "routing parcel to legacy path: {0}", parcel.destination());
        long charged = legacy.quote(parcel.grams(), parcel.serviceLevel().name(), parcel.destination());
        return charged == 0L
            ? new Quote.Unavailable(parcel.destination(), "no-rate-for-zone")  // adapt the in-band zero
            : new Quote.Priced(new Money(charged, "USD"));
    }

    /**
     * Describes a quote for display, using a pattern-matching {@code switch} over the sealed
     * {@link Quote} — exhaustive without a {@code default}, so a new outcome becomes a compile error here.
     *
     * @param quote the quote to describe, never {@code null}
     * @return a human-readable line, never {@code null}
     * @throws NullPointerException if {@code quote} is {@code null}
     */
    public String describe(Quote quote) {
        Objects.requireNonNull(quote, "quote");
        // tag::sealed-switch[]
        return switch (quote) {
            case Quote.Priced(Money amount) ->
                "charged " + amount.minorUnits() + " " + amount.currency();
            case Quote.Unavailable(String zone, String reason) ->
                "unavailable for " + zone + " (" + reason + ")";
        };
        // end::sealed-switch[]
    }
}
