package org.acme.refactoring;

import java.util.ArrayList;
import java.util.List;

/**
 * Prices a parcel the way the original code did — a deliberate legacy counter-example, the starting
 * point the chapter's safe-change loop works from.
 *
 * <p>It carries every property that makes legacy code hard to change. Its public constructor
 * {@code new}s up its own rate source inside the object (no seam, Feathers), so as originally written
 * a test could not substitute a known rate. It branches on a magic service-level {@link String} with an
 * {@code if/else} ladder, works in loose {@code int}/{@code long} primitives, signals "no rate" with an
 * in-band zero, and hands its internal mutable surcharge list straight back from
 * {@link #appliedSurcharges()} (the representation-exposure leak SpotBugs reports as
 * {@code EI_EXPOSE_REP}). It also carries a faithfully-reproduced <em>rounding quirk</em>: the surcharge
 * is computed before the weight multiplication and truncates, so some inputs round a cent differently
 * than a naive reading would expect. That quirk is current behaviour, so the characterization test pins
 * it exactly — a characterization test captures what the code <em>does</em>, not what it <em>should</em>
 * do (Feathers), and a quirk faithfully preserved is still the behaviour being protected during the
 * refactor.
 *
 * <p>To make it testable <em>without</em> changing its behaviour, one behaviour-preserving legacy
 * refactoring is applied: {@code RateTable} is extracted as a seam (an interface) and a package-private
 * constructor lets a test parameterize the rate source (Feathers' <em>Parameterize Constructor</em>).
 * The public constructor is unchanged, so production behaviour is identical; the seam exists only so the
 * characterization net can be written. The shipping advice is {@link ShippingCalculator}, never this
 * type.
 */
public final class LegacyShippingCalculator {

    private static final int BASIS_POINTS_DENOMINATOR = 10_000;
    private static final int GRAMS_PER_KILO = 1_000;

    private final RateTable rates;
    private final List<String> appliedSurcharges = new ArrayList<>();

    /**
     * Creates a calculator the way production did — newing up the hard-coded rate source inside the
     * object, with no seam for a test to reach.
     */
    // tag::legacy-no-seam[]
    public LegacyShippingCalculator() {
        this.rates = new BuiltInRateTable();   // no seam: dependency newed up in place, untestable as-is
    }

    // Parameterize Constructor (Feathers): the seam a test injects through, behaviour unchanged.
    LegacyShippingCalculator(RateTable rates) {
        this.rates = rates;
    }
    // end::legacy-no-seam[]

    /**
     * Prices a parcel — the original long, primitive-typed method, preserved as-is.
     *
     * @param grams        the parcel weight in grams
     * @param serviceLevel the service-level string ("STANDARD", "EXPEDITED", "OVERNIGHT")
     * @param destination  the destination zone
     * @return the shipping charge in minor units, or {@code 0} when the destination has no rate
     */
    public long quote(int grams, String serviceLevel, String destination) {
        appliedSurcharges.clear();
        Money baseRate = rates.baseRatePerKilo(destination).orElse(null);
        if (baseRate == null) {
            return 0L;                                   // in-band sentinel: a caller can mistake 0 for free
        }
        long surchargeBps;
        if (serviceLevel.equals("OVERNIGHT")) {          // magic-string if/else ladder over a type code
            surchargeBps = 4_000L;
            appliedSurcharges.add("OVERNIGHT");
        } else if (serviceLevel.equals("EXPEDITED")) {
            surchargeBps = 1_500L;
            appliedSurcharges.add("EXPEDITED");
        } else {
            surchargeBps = 0L;
        }
        // The preserved rounding quirk: the per-kilo surcharge is applied and truncated BEFORE the
        // weight multiplication, so the integer division rounds at a different point than applying the
        // surcharge to the final amount would. This is current behaviour; the characterization test
        // pins it, and the refactor reproduces it exactly rather than "fixing" it under the refactor hat.
        long ratePerKilo = baseRate.minorUnits();
        long surchargedPerKilo = ratePerKilo + ratePerKilo * surchargeBps / BASIS_POINTS_DENOMINATOR;
        return surchargedPerKilo * grams / GRAMS_PER_KILO;
    }

    /**
     * Returns the surcharges applied on the last quote — handing back the internal mutable list directly
     * (the representation-exposure leak the modern path closes with an immutable snapshot).
     *
     * @return the internal surcharge list
     */
    public List<String> appliedSurcharges() {
        return appliedSurcharges;                        // SMELL (EI_EXPOSE_REP): internal list leaks out
    }

    /**
     * The hard-coded rate source the public constructor news up — the dependency that, inlined, left the
     * legacy method without a seam.
     */
    private static final class BuiltInRateTable implements RateTable {
        @Override
        public java.util.Optional<Money> baseRatePerKilo(String destination) {
            return switch (destination) {
                case "ZONE_A" -> java.util.Optional.of(new Money(500L, "USD"));
                case "ZONE_B" -> java.util.Optional.of(new Money(900L, "USD"));
                default -> java.util.Optional.empty();
            };
        }
    }
}
