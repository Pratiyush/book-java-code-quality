package org.acme.design.overengineered;

/**
 * The one and only implementation of {@link DiscountPolicy}.
 *
 * <p>The percentage is wired in through the constructor and reached only through a factory, so a
 * change to how the rate is supplied ripples through the interface, the factory, and the service.
 */
public final class PercentageDiscountPolicy implements DiscountPolicy {

    private final int percent;

    /**
     * Creates a policy applying a fixed percentage discount.
     *
     * @param percent the discount percentage in {@code [0, 100]}
     */
    public PercentageDiscountPolicy(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("percent out of range: " + percent);
        }
        this.percent = percent;
    }

    @Override
    public long apply(long subtotalMinorUnits) {
        return subtotalMinorUnits - (subtotalMinorUnits * percent / 100);
    }
}
