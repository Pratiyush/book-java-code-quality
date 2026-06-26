package org.acme.design.overengineered;

/**
 * An abstraction over a single discount calculation — one interface for one implementation.
 *
 * <p>On paper this honours OCP and DIP. In practice there is only one implementation
 * ({@link PercentageDiscountPolicy}) and no second variation in sight, so the interface adds a layer
 * of indirection without buying the substitutability it advertises.
 */
public interface DiscountPolicy {

    /**
     * Returns the discounted total for an order subtotal, in minor units.
     *
     * @param subtotalMinorUnits the order subtotal in minor units, never negative
     * @return the discounted total in minor units
     */
    long apply(long subtotalMinorUnits);
}
