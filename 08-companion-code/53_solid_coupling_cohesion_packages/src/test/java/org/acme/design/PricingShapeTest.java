package org.acme.design;

import static org.assertj.core.api.Assertions.assertThat;

import org.acme.design.balanced.Discounts;
import org.acme.design.balanced.Order;
import org.acme.design.balanced.OrderPricer;
import org.acme.design.overengineered.OrderPricingService;
import org.junit.jupiter.api.Test;

/**
 * The over-abstracted and balanced shapes reach the same result. The chapter's point is the cost of
 * getting there, not a difference in output — so the test asserts equivalence, leaving the ceremony
 * difference to be read in the two packages.
 */
class PricingShapeTest {

    @Test
    void overAbstractedAndBalancedShapesPriceIdentically() {
        long overAbstracted = OrderPricingService.priceOrder(10_000L, 10);
        long balanced = OrderPricer.priceOrder(new Order(10_000L, 3), Discounts.percentage(10));

        assertThat(overAbstracted).isEqualTo(9_000L);
        assertThat(balanced).isEqualTo(overAbstracted);
    }

    @Test
    void balancedNoOpPolicyIsTheSecondRealVariation() {
        long total = OrderPricer.priceOrder(new Order(10_000L, 3), Discounts.none());

        assertThat(total).isEqualTo(10_000L);
    }
}
