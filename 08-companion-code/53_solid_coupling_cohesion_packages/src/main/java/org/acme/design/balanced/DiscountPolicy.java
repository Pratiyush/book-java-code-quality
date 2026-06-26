package org.acme.design.balanced;

/**
 * The one abstraction this package keeps — and it earns its keep, because two real implementations
 * exist: {@link Discounts#percentage(int)} and {@link Discounts#none()}.
 *
 * <p>This is OCP and ISP where they pay off: a focused, single-method role interface with a genuine
 * variation behind it, rather than a speculative seam for a change that never comes.
 */
@FunctionalInterface
public interface DiscountPolicy {

    /**
     * Returns the discounted total for an order subtotal, in minor units.
     *
     * @param subtotalMinorUnits the order subtotal in minor units, never negative
     * @return the discounted total in minor units
     */
    long apply(long subtotalMinorUnits);
}
