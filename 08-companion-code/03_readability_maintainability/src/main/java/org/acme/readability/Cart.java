package org.acme.readability;

import java.util.Objects;

/**
 * The inputs a discount rule prices: a subtotal, the customer's loyalty tier, and two flags that open
 * extra branches (a coupon was applied, a seasonal sale is running).
 *
 * <p>It is a plain immutable carrier. Its job in this module is to give the three forms of the discount
 * rule enough conditions to differ in <em>shape</em> — nested deep, fragmented wide, or balanced — while
 * still computing the identical discount, so the behaviour-preservation test has something with several
 * branches to drive.
 *
 * @param subtotal   the order subtotal before any discount, never {@code null}
 * @param tier       the customer's loyalty tier, never {@code null}
 * @param hasCoupon  whether a coupon was applied to this cart
 * @param seasonSale whether a seasonal sale is currently running
 */
public record Cart(Money subtotal, LoyaltyTier tier, boolean hasCoupon, boolean seasonSale) {

    /**
     * Validates the components so a {@code Cart} is well-formed for its whole lifetime.
     *
     * @throws NullPointerException if {@code subtotal} or {@code tier} is {@code null}
     */
    public Cart {
        Objects.requireNonNull(subtotal, "subtotal");
        Objects.requireNonNull(tier, "tier");
    }
}
