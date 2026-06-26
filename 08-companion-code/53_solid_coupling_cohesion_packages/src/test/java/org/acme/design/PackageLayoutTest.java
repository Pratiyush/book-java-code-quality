package org.acme.design;

import static org.assertj.core.api.Assertions.assertThat;

import org.acme.design.bylayer.controller.OrderController;
import org.acme.design.bylayer.repository.OrderRepository;
import org.acme.design.bylayer.service.OrderService;
import org.acme.design.byfeature.billing.BillingService;
import org.acme.design.byfeature.orders.Order;
import org.junit.jupiter.api.Test;

/**
 * Both package layouts place an order; the difference is where the code lives. The by-layer flow
 * crosses three packages (controller, service, repository); the by-feature flow keeps the orders
 * feature whole and shares with billing only through the published {@link Order} type.
 */
class PackageLayoutTest {

    @Test
    void byLayerFlowCrossesThreePackagesToPlaceAnOrder() {
        OrderRepository repository = new OrderRepository();
        OrderService service = new OrderService(repository);
        OrderController controller = new OrderController(service);

        String result = controller.placeOrder("o1", 1_500L);

        assertThat(result).isEqualTo("placed o1");
        assertThat(repository.findById("o1")).isPresent();
    }

    @Test
    void byFeatureKeepsOrdersWholeAndSharesWithBillingThroughOnePublishedType() {
        org.acme.design.byfeature.orders.OrderService orders =
            new org.acme.design.byfeature.orders.OrderService();
        Order placed = orders.place("o2", 2_000L);

        BillingService billing = new BillingService();
        long tax = billing.taxFor(placed, 10);

        assertThat(orders.find("o2")).contains(placed);
        assertThat(tax).isEqualTo(200L);
    }
}
