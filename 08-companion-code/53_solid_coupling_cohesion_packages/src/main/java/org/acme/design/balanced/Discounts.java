package org.acme.design.balanced;

/**
 * The two real {@link DiscountPolicy} variations, supplied as small factory methods.
 *
 * <p>Because {@code DiscountPolicy} is a functional interface, each variation is a lambda rather than
 * a class-plus-factory pair. The variation that justifies the interface lives here, in one file,
 * instead of being spread across an interface, a factory, and an implementation per policy.
 */
public final class Discounts {

    private Discounts() {
    }

    /**
     * A policy applying a fixed percentage discount.
     *
     * @param percent the discount percentage in {@code [0, 100]}
     * @return a discount policy, never {@code null}
     * @throws IllegalArgumentException if {@code percent} is outside {@code [0, 100]}
     */
    public static DiscountPolicy percentage(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("percent out of range: " + percent);
        }
        return subtotal -> subtotal - (subtotal * percent / 100);
    }

    /**
     * A policy that applies no discount — the second genuine variation.
     *
     * @return a discount policy returning the subtotal unchanged, never {@code null}
     */
    public static DiscountPolicy none() {
        return subtotal -> subtotal;
    }
}
