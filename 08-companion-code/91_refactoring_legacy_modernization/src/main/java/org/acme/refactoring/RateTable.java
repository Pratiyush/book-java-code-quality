package org.acme.refactoring;

import java.util.Optional;

/**
 * The base shipping rate per destination zone — the <em>seam</em> the legacy calculator lacked.
 *
 * <p>Feathers defines a seam as "a place where you can alter behaviour without editing in that place."
 * The legacy calculator {@code new}s up its own hard-coded rate source inside the method body, so a
 * test cannot substitute a known rate without editing the method — that is precisely the missing seam.
 * Extracting this interface (Feathers' <em>Extract Interface</em> legacy refactoring) creates an
 * <em>object seam</em>: a caller injects a {@code RateTable}, and a test can inject a stub, so the same
 * method becomes testable without dragging in the real rate source. The legacy class keeps its inlined
 * behaviour for the characterization test; the modern {@link ShippingCalculator} takes the seam.
 *
 * <p>The lookup returns an {@link Optional} rather than {@code null} or a sentinel so an unknown zone
 * is a representable, type-checked outcome (Chapter 11) the modern calculator turns into an explicit
 * {@link Quote.Unavailable} result.
 */
public interface RateTable {

    /**
     * Looks up the base rate (minor units per kilogram) for a destination zone.
     *
     * @param destination the destination zone, never {@code null}
     * @return the base rate if the zone is served, otherwise an empty {@link Optional}
     */
    // tag::seam-interface[]
    Optional<Money> baseRatePerKilo(String destination);   // the seam: a test injects a known table
    // end::seam-interface[]
}
