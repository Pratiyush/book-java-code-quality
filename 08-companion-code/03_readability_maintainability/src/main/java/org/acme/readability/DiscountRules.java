package org.acme.readability;

import java.util.Objects;

/**
 * The discount rule in its balanced form — the readable target between the deeply-nested
 * {@link DiscountRulesNested} and the over-fragmented {@link DiscountRulesFragmented}.
 *
 * <p>{@link #discountFor} reads top to bottom: guard the preconditions, take the early exit below the
 * floor, build the basis points additively (the tier carries its own discount, so there is no tier
 * {@code if}-ladder), then clamp to the cap. The nesting the nested form pays for is flattened into
 * guard clauses, and the logic the fragmented form scatters across a dozen methods stays in one body the
 * reader can follow. It scores low on cognitive complexity (Sonar {@code java:S3776}) <em>and</em> holds
 * one whole idea per line — the chapter's reminder that a low score with terrible structure is still
 * unreadable, so the number is a prompt, not the goal.
 *
 * <p>This is one position, not a verdict: where a rule grows many independent cases, a flat
 * {@code switch} or a sealed hierarchy may read more clearly than additive arithmetic, and where a step
 * is genuinely reused, extracting it earns its name. The balanced form is what fits <em>this</em> rule.
 */
public final class DiscountRules implements DiscountRule {

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
        // tag::refactor-balanced[]
        requireCapNotBelowFloor(cap, floor);                 // guard clauses: no deep nesting
        String currency = cart.subtotal().currency();
        if (cart.subtotal().minorUnits() < floor) {
            return new Money(0L, currency);                  // early exit reads as "nothing applies"
        }
        long basisPoints = cart.tier().discountBasisPoints() // the tier carries its own discount
            + (cart.seasonSale() ? SEASON_SALE_BASIS_POINTS : 0)
            + (cart.hasCoupon() ? COUPON_BASIS_POINTS : 0);
        return new Money(cappedRaw(cart, basisPoints, cap), currency);
        // end::refactor-balanced[]
    }

    /**
     * Computes the raw percentage discount and clamps it to the cap.
     *
     * @param cart        the cart being priced
     * @param basisPoints the total discount in basis points
     * @param cap         the maximum discount allowed
     * @return the discount in minor units, never exceeding the cap
     */
    private long cappedRaw(Cart cart, long basisPoints, Money cap) {
        long raw = cart.subtotal().minorUnits() * basisPoints / BASIS_POINTS_DENOMINATOR;
        return Math.min(raw, cap.minorUnits());
    }

    /**
     * Validates the discount cap, or rejects it with a typed error.
     *
     * @param cap   the discount cap
     * @param floor the minimum subtotal below which no discount applies
     * @throws PricingException if the cap is below the floor
     */
    private void requireCapNotBelowFloor(Money cap, long floor) {
        if (cap.minorUnits() < floor) {
            throw new PricingException("cap-below-floor", "discount cap must not be below the floor");
        }
    }
}
