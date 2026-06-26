package org.acme.storefront;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.acme.storefront.domain.Money;
import org.acme.storefront.domain.Order;
import org.acme.storefront.domain.OrderId;
import org.acme.storefront.persistence.InMemoryOrderRepository;
import org.acme.storefront.persistence.OrderRepository;
import org.acme.storefront.service.OrderNotFoundException;
import org.acme.storefront.service.OrderService;
import org.acme.storefront.web.HealthEndpoint;
import org.acme.storefront.web.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Exercises the layered sample end-to-end through every layer the architecture rules govern: a
 * request enters the {@link OrderController} (web), runs through the {@link OrderService} (service),
 * and lands in the {@link OrderRepository} (persistence) over {@code ..domain..} value types. This is
 * the running behaviour the fitness functions in {@link ArchitectureFitnessTest} protect.
 */
class OrderFlowIntegrationTest {

    private OrderController controller;
    private HealthEndpoint health;
    private OrderService service;

    @BeforeEach
    void wireTheLayers() {
        OrderRepository repository = new InMemoryOrderRepository();
        service = new OrderService(repository);
        controller = new OrderController(service);
        health = new HealthEndpoint(service);
    }

    @Test
    void placesAndReadsAnOrderThroughEveryLayer() {
        OrderId id = new OrderId("order-1");
        Order placed = controller.placeOrder(id, new Money(2_500L, "USD"));

        assertThat(placed.id()).isEqualTo(id);
        assertThat(controller.getOrder(id).total().minorUnits()).isEqualTo(2_500L);
    }

    @Test
    void readingAnUnknownOrderTakesTheFailurePathAndCountsIt() {
        assertThatThrownBy(() -> controller.getOrder(new OrderId("ghost")))
            .isInstanceOf(OrderNotFoundException.class)
            .hasMessageContaining("ghost");

        assertThat(health.notFoundMetric()).isEqualTo(1L);
    }

    @Test
    void healthEndpointReportsReady() {
        assertThat(health.ready()).isTrue();
    }
}
