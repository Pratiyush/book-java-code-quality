package org.acme.readability;

/**
 * A customer's loyalty tier, with the discount it earns expressed in basis points (hundredths of a
 * percent).
 *
 * <p>The discount basis points are carried as data on each constant. The balanced rule reads the value
 * off the type ({@link #discountBasisPoints()}); the deliberately nested and fragmented counter-examples
 * branch on the constant with an {@code if}-ladder instead, which is part of what drives their cognitive
 * complexity up. Keeping the data on the enum is one of the moves the chapter names for lowering the
 * cognitive load of the rule that uses it.
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
