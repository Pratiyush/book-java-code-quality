package org.acme.design.overengineered;

/**
 * A factory whose only job is to hand back the one {@link DiscountPolicy} implementation.
 *
 * <p>The factory layer is the indirection the chapter describes: it adds a seam for a second policy
 * that does not exist, so every wiring change passes through it for no present benefit.
 */
public final class DiscountPolicyFactory {

    private final int percent;

    /**
     * Creates a factory configured with the discount percentage.
     *
     * @param percent the discount percentage in {@code [0, 100]}
     */
    public DiscountPolicyFactory(int percent) {
        this.percent = percent;
    }

    /**
     * Builds the configured discount policy.
     *
     * @return a new {@link DiscountPolicy}, never {@code null}
     */
    public DiscountPolicy create() {
        return new PercentageDiscountPolicy(percent);
    }
}
