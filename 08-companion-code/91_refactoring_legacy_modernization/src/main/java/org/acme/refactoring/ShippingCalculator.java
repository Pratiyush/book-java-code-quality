package org.acme.refactoring;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Prices a parcel — the modernized refactor of {@link LegacyShippingCalculator}, behaviour-for-behaviour
 * identical and expressed in modern Java.
 *
 * <p>The same computation the legacy method performed is here, but the structure is different: the rate
 * source arrives through the extracted {@link RateTable} seam (constructor injection, not an inlined
 * {@code new}); the input is a {@link Parcel} record carrying a {@link ServiceLevel} enum, so the magic
 * service-level string and its {@code if/else} ladder are gone; the surcharge basis points are read off
 * the enum; the "no rate" outcome is a typed {@link Quote.Unavailable} rather than an in-band zero; and
 * the applied surcharges are returned as an immutable snapshot. These are the modern-Java idioms the
 * chapter applies to the 2018-era catalog (records, sealed types + pattern matching, {@code Optional},
 * streams — Chapter 5), each replacing a manual step the catalog once spelled out by hand.
 *
 * <p>The one thing that does <em>not</em> change is the result: the rounding quirk the legacy method
 * carries (surcharge applied per-kilo and truncated before the weight multiplication) is reproduced
 * exactly, because a refactoring preserves behaviour — quirk and all. {@code SafeChangeTest} proves the
 * modern {@code Priced} amount equals the legacy {@code long} for every parcel. Modernizing the
 * <em>idiom</em> while preserving the <em>behaviour</em> is the refactor; changing the rounding would be
 * a behaviour change wearing the wrong hat, a separate, separately-reviewed decision.
 */
public final class ShippingCalculator {

    private static final Logger LOG = System.getLogger(ShippingCalculator.class.getName());

    private static final int BASIS_POINTS_DENOMINATOR = 10_000;
    private static final int GRAMS_PER_KILO = 1_000;

    private final RateTable rates;

    /** Observability: how many quotes were returned as unavailable (illustrative, Chapter 45). */
    private final AtomicLong unavailable = new AtomicLong();

    /**
     * Creates a calculator over an injected rate source — the seam the legacy class lacked.
     *
     * @param rates the rate source to price against, never {@code null}
     * @throws NullPointerException if {@code rates} is {@code null}
     */
    public ShippingCalculator(RateTable rates) {
        this.rates = Objects.requireNonNull(rates, "rates");
    }

    /**
     * Prices a parcel, returning a typed quote.
     *
     * @param parcel the parcel to price, never {@code null}
     * @return a {@link Quote.Priced} with the charge, or a {@link Quote.Unavailable} when the
     *     destination has no rate, never {@code null}
     * @throws NullPointerException if {@code parcel} is {@code null}
     */
    // tag::modern-refactor[]
    public Quote quote(Parcel parcel) {
        Objects.requireNonNull(parcel, "parcel");
        return rates.baseRatePerKilo(parcel.destination())
            .map(rate -> price(rate, parcel))                       // present -> price it
            .map(amount -> (Quote) new Quote.Priced(amount))
            .orElseGet(() -> unavailableFor(parcel.destination())); // empty -> typed Unavailable
    }
    // end::modern-refactor[]

    /**
     * Computes the charge, reproducing the legacy rounding exactly (surcharge per-kilo, truncated, before
     * the weight multiplication) so the refactor preserves behaviour.
     *
     * @param baseRate the base rate per kilo for the destination
     * @param parcel   the parcel being priced
     * @return the shipping charge, never {@code null}
     */
    private Money price(Money baseRate, Parcel parcel) {
        long ratePerKilo = baseRate.minorUnits();
        long surchargeBps = parcel.serviceLevel().surchargeBasisPoints();
        long surchargedPerKilo = ratePerKilo + ratePerKilo * surchargeBps / BASIS_POINTS_DENOMINATOR;
        long charge = surchargedPerKilo * parcel.grams() / GRAMS_PER_KILO;
        return new Money(charge, baseRate.currency());
    }

    /**
     * Records and returns the typed unavailable outcome for an unserved destination.
     *
     * @param destination the unserved destination zone
     * @return the {@link Quote.Unavailable} result, never {@code null}
     */
    private Quote unavailableFor(String destination) {
        unavailable.incrementAndGet();
        LOG.log(Level.DEBUG, "no rate for destination {0}", destination);
        return new Quote.Unavailable(destination, "no-rate-for-zone");
    }

    /**
     * Returns the surcharges that applied to a parcel — an immutable snapshot, so the result cannot leak
     * the calculator's state (the representation-exposure leak the legacy accessor carries, closed here).
     *
     * @param parcel the parcel to describe, never {@code null}
     * @return the applied surcharge names, an unmodifiable list, never {@code null}
     */
    public List<String> appliedSurcharges(Parcel parcel) {
        Objects.requireNonNull(parcel, "parcel");
        // tag::modern-immutable-snapshot[]
        return parcel.serviceLevel() == ServiceLevel.STANDARD
            ? List.of()
            : List.of(parcel.serviceLevel().name());   // unmodifiable: no internal list to leak
        // end::modern-immutable-snapshot[]
    }

    /**
     * Health/observability surface: how many quotes were returned as unavailable since startup.
     *
     * @return the unavailable-quote count, never negative
     */
    public long unavailableCount() {
        return unavailable.get();
    }
}
