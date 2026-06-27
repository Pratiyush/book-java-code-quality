package org.acme.readability;

import java.util.Objects;

/**
 * The discount rule packed into one deeply-nested method — a deliberate counter-example, the high-
 * cognitive-complexity shape {@link DiscountRules} resolves.
 *
 * <p>{@link #discountFor} computes the correct discount, but it does so in a single body that nests the
 * floor check, the seasonal-sale check, the loyalty tier, and the coupon several levels deep. Cognitive
 * complexity (Sonar {@code java:S3776}) increments more for each level of nesting, so this form scores
 * high even though its branch count — and therefore its cyclomatic complexity, McCabe's path count — is
 * the same as the balanced form's. That gap is the chapter's measurement point: the two metrics answer
 * different questions, and nesting is where they diverge. It compiles, it passes its tests, and it
 * returns exactly the same {@link Money} as the other two forms for every input — a high score is a
 * reading cost, not a wrong answer. The house Checkstyle and SpotBugs gate does not measure method
 * length or complexity, so this method is not flagged here; a different tool measures a different thing.
 */
public final class DiscountRulesNested implements DiscountRule {

    /** Basis points are hundredths of a percent, so a percentage of an amount divides by 10,000. */
    private static final int BASIS_POINTS_DENOMINATOR = 10_000;

    /** A coupon adds this many basis points on top of the loyalty discount. */
    private static final int COUPON_BASIS_POINTS = 500;

    /** A seasonal sale adds this many basis points on top of the loyalty discount. */
    private static final int SEASON_SALE_BASIS_POINTS = 300;

    @Override
    public Money discountFor(Cart cart, Money cap, long floor) {
        Objects.requireNonNull(cart, "cart");
        Objects.requireNonNull(cap, "cap");
        if (cap.minorUnits() < floor) {
            throw new PricingException("cap-below-floor", "discount cap must not be below the floor");
        }
        long bp;
        if (cart.subtotal().minorUnits() >= floor) {
            if (cart.tier() == LoyaltyTier.GOLD) {        // one method, nesting deeper
                bp = 1000;
                // tag::smell-nested[]
                if (cart.seasonSale()) {
                    bp += SEASON_SALE_BASIS_POINTS;
                    if (cart.hasCoupon()) {               // four levels deep: the cognitive cost
                        bp += COUPON_BASIS_POINTS;
                    }
                } else if (cart.hasCoupon()) {
                    bp += COUPON_BASIS_POINTS;
                }
                // end::smell-nested[]
            } else if (cart.tier() == LoyaltyTier.SILVER) {
                bp = 500;
                if (cart.seasonSale()) {
                    bp += SEASON_SALE_BASIS_POINTS;
                    if (cart.hasCoupon()) {
                        bp += COUPON_BASIS_POINTS;
                    }
                } else if (cart.hasCoupon()) {
                    bp += COUPON_BASIS_POINTS;
                }
            } else {
                bp = 0;
                if (cart.seasonSale()) {
                    bp += SEASON_SALE_BASIS_POINTS;
                }
                if (cart.hasCoupon()) {
                    bp += COUPON_BASIS_POINTS;
                }
            }
        } else {
            bp = 0;
        }
        long raw = cart.subtotal().minorUnits() * bp / BASIS_POINTS_DENOMINATOR;
        long capped = Math.min(raw, cap.minorUnits());
        return new Money(capped, cart.subtotal().currency());
    }
}
