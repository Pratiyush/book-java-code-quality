package org.acme.readability;

import java.util.Objects;

/**
 * The discount rule chopped into many one-line methods — a deliberate counter-example, the over-
 * decomposition the chapter's second school (a philosophy of deep modules) warns of.
 *
 * <p>Each fragment is tiny and "does one thing," so this form scores <em>low</em> on per-method cognitive
 * complexity (Sonar {@code java:S3776} measures one method at a time). But following a single idea — how
 * the basis points are built up — means hopping across a dozen named fragments, which is the cost that
 * school names: excessive decomposition adds its own cognitive load even as the per-method number falls.
 * That is the chapter's honesty about the metric: a rule can game a per-method threshold by splitting,
 * without becoming easier to read. It returns exactly the same {@link Money} as the other two forms for
 * every input — proven by the behaviour-preservation test — so the split changed structure, not results.
 * The two schools are presented as a context-dependent trade-off, not a winner: tiny methods aid
 * navigation but can fragment a readable algorithm.
 */
public final class DiscountRulesFragmented implements DiscountRule {

    /** Basis points are hundredths of a percent, so a percentage of an amount divides by 10,000. */
    private static final int BASIS_POINTS_DENOMINATOR = 10_000;

    /** A coupon adds this many basis points on top of the loyalty discount. */
    private static final int COUPON_BASIS_POINTS = 500;

    /** A seasonal sale adds this many basis points on top of the loyalty discount. */
    private static final int SEASON_SALE_BASIS_POINTS = 300;

    // tag::smell-fragmented[]
    @Override
    public Money discountFor(Cart cart, Money cap, long floor) {
        requireInputs(cart, cap);                 // following one idea means hopping across fragments
        requireCapNotBelowFloor(cap, floor);
        long bp = belowFloor(cart, floor) ? 0 : basisPointsFor(cart);
        return cappedMoney(cart, bp, cap);
    }
    // end::smell-fragmented[]

    private void requireInputs(Cart cart, Money cap) {
        Objects.requireNonNull(cart, "cart");
        Objects.requireNonNull(cap, "cap");
    }

    private void requireCapNotBelowFloor(Money cap, long floor) {
        if (cap.minorUnits() < floor) {
            throw new PricingException("cap-below-floor", "discount cap must not be below the floor");
        }
    }

    private boolean belowFloor(Cart cart, long floor) {
        return cart.subtotal().minorUnits() < floor;
    }

    private long basisPointsFor(Cart cart) {
        return tierBasisPoints(cart) + saleBasisPoints(cart) + couponBasisPoints(cart);
    }

    private long tierBasisPoints(Cart cart) {
        return cart.tier().discountBasisPoints();
    }

    private long saleBasisPoints(Cart cart) {
        return cart.seasonSale() ? SEASON_SALE_BASIS_POINTS : 0;
    }

    private long couponBasisPoints(Cart cart) {
        return cart.hasCoupon() ? COUPON_BASIS_POINTS : 0;
    }

    private Money cappedMoney(Cart cart, long bp, Money cap) {
        long raw = rawDiscount(cart, bp);
        return new Money(Math.min(raw, cap.minorUnits()), cart.subtotal().currency());
    }

    private long rawDiscount(Cart cart, long bp) {
        return cart.subtotal().minorUnits() * bp / BASIS_POINTS_DENOMINATOR;
    }
}
