package org.acme.refactoring;

/**
 * A shipping service level, with the surcharge it adds expressed in basis points (hundredths of a
 * percent) over the base rate.
 *
 * <p>The legacy calculator branched on a magic service-level {@link String} with an {@code if/else}
 * ladder; modelling the level as a closed {@code enum} that carries its own surcharge lets the modern
 * refactor read the value off the type instead of re-deriving it in a conditional. The surcharge basis
 * points are data on the enum, not a ladder of {@code if} statements — the same value, expressed as a
 * real type (Chapter 5's modern-Java lens on the catalog).
 */
public enum ServiceLevel {

    /** Standard delivery, no surcharge. */
    STANDARD(0),
    /** Expedited delivery, a modest surcharge (15%). */
    EXPEDITED(1500),
    /** Overnight delivery, a larger surcharge (40%). */
    OVERNIGHT(4000);

    private final int surchargeBasisPoints;

    ServiceLevel(int surchargeBasisPoints) {
        this.surchargeBasisPoints = surchargeBasisPoints;
    }

    /**
     * Returns the surcharge this service level adds, in basis points (hundredths of a percent).
     *
     * @return the surcharge in basis points, never negative
     */
    public int surchargeBasisPoints() {
        return surchargeBasisPoints;
    }
}
