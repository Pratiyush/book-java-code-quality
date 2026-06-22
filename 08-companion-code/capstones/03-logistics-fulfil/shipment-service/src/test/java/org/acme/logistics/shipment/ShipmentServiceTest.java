package org.acme.logistics.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ShipmentServiceTest {

    private final ShipmentService service = new ShipmentService(new InMemoryShipmentRepository());

    @Test
    void createsAshipmentForAnOrder() {
        ShipmentService.CreateResult result = service.create("ord-1");
        assertThat(result.replayed()).isFalse();
        assertThat(result.shipment().status()).isEqualTo(ShipmentStatus.CREATED);
    }

    @Test
    void doesNotCreateAsecondShipmentForTheSameOrder() {
        ShipmentService.CreateResult first = service.create("ord-2");
        ShipmentService.CreateResult repeat = service.create("ord-2");
        assertThat(repeat.replayed()).isTrue();
        assertThat(repeat.shipment().id()).isEqualTo(first.shipment().id());
    }

    @Test
    void dispatchAdvancesTheStatus() {
        String id = service.create("ord-3").shipment().id();
        assertThat(service.dispatch(id).status()).isEqualTo(ShipmentStatus.DISPATCHED);
    }
}
