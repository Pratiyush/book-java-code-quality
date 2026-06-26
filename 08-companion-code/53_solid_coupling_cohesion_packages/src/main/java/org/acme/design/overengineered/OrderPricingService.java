package org.acme.design.overengineered;

import java.util.Objects;

/**
 * Prices an order by delegating to an injected {@link DiscountPolicy}.
 *
 * <p>The service depends on the abstraction, not the implementation — DIP on paper. The cost is
 * visible the moment a field has to be added: a new input (say a loyalty tier) has to be threaded
 * through the factory, the interface, and the implementation before it reaches the one place that
 * uses it. The displayed region below is the wiring a caller writes to get a price.
 */
public final class OrderPricingService {

    private final DiscountPolicy discountPolicy;

    /**
     * Creates a pricing service over a discount policy.
     *
     * @param discountPolicy the policy to apply, never {@code null}
     * @throws NullPointerException if {@code discountPolicy} is {@code null}
     */
    public OrderPricingService(DiscountPolicy discountPolicy) {
        this.discountPolicy = Objects.requireNonNull(discountPolicy, "discountPolicy");
    }

    // tag::over-abstracted[]
    /** Wiring an order price: a factory builds the one policy, which the service then depends on. */
    public static long priceOrder(long subtotalMinorUnits, int discountPercent) {
        DiscountPolicyFactory factory = new DiscountPolicyFactory(discountPercent);
        DiscountPolicy policy = factory.create();          // one interface, one implementation
        OrderPricingService service = new OrderPricingService(policy);
        return service.total(subtotalMinorUnits);
    }
    // end::over-abstracted[]

    /**
     * Returns the discounted total for a subtotal.
     *
     * @param subtotalMinorUnits the order subtotal in minor units, never negative
     * @return the discounted total in minor units
     */
    public long total(long subtotalMinorUnits) {
        return discountPolicy.apply(subtotalMinorUnits);
    }
}
