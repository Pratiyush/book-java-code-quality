package org.acme.smells;

/**
 * A customer's loyalty tier, with the discount it earns expressed in basis points (hundredths of a
 * percent).
 *
 * <p>Modelling the tier as a closed {@code enum} rather than a magic {@link String} or an {@code int}
 * type-code is the fix for two smells the chapter names at once: <em>Primitive Obsession</em> (a string
 * standing in for a real type) and the type-code {@code switch} that a {@code sealed} hierarchy or, as
 * here, an {@code enum} retires. Each constant carries its own discount, so the rule that applies the
 * discount reads the value off the type instead of branching on a literal — the discount basis points
 * are data on the enum, not a ladder of {@code if} statements in a long method.
 */
public enum LoyaltyTier {

    /** No loyalty discount. */
    STANDARD(0),
    /** A modest loyalty discount (5%). */
    SILVER(500),
    /** A larger loyalty discount (10%). */
    GOLD(1000);

    private final int discountBasisPoints;

    LoyaltyTier(int discountBasisPoints) {
        this.discountBasisPoints = discountBasisPoints;
    }

    /**
     * Returns the discount this tier earns, in basis points (hundredths of a percent).
     *
     * @return the discount in basis points, never negative
     */
    public int discountBasisPoints() {
        return discountBasisPoints;
    }
}
