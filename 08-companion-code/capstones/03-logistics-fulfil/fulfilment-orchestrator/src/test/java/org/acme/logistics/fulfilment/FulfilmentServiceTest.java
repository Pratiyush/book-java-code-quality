package org.acme.logistics.fulfilment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.acme.platform.error.ApiException;
import org.junit.jupiter.api.Test;

class FulfilmentServiceTest {

    private final FakeInventory inventory = new FakeInventory(Map.of("sku-a", 5, "sku-b", 3));
    private final FakeShipment shipment = new FakeShipment();

    private FulfilmentService newService() {
        return new FulfilmentService(new InMemoryFulfilmentRepository(), inventory, shipment);
    }

    @Test
    void reservesEveryLineThenCreatesAshipment() {
        FulfilmentService service = newService();
        FulfilmentService.FulfilResult result = service.fulfil(new FulfilmentRequest("ord-1",
            List.of(new FulfilmentRequest.Line("sku-a", 2), new FulfilmentRequest.Line("sku-b", 1))));

        assertThat(result.replayed()).isFalse();
        assertThat(result.fulfilment().shipmentId()).isEqualTo("shp-ord-1");
        assertThat(inventory.available.get("sku-a")).isEqualTo(3);
        assertThat(inventory.available.get("sku-b")).isEqualTo(2);
    }

    @Test
    void compensatesReleasingReservedLinesWhenAlaterLineIsOutOfStock() {
        FulfilmentService service = newService();
        assertThatThrownBy(() -> service.fulfil(new FulfilmentRequest("ord-2",
            List.of(new FulfilmentRequest.Line("sku-a", 2), new FulfilmentRequest.Line("sku-b", 10)))))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(409));

        // sku-a was reserved then released by compensation; no shipment was created.
        assertThat(inventory.available.get("sku-a")).isEqualTo(5);
        assertThat(inventory.available.get("sku-b")).isEqualTo(3);
        assertThat(shipment.created.get()).isZero();
    }

    @Test
    void doesNotReFulfilAnAlreadyFulfilledOrder() {
        FulfilmentService service = newService();
        FulfilmentRequest request = new FulfilmentRequest("ord-3", List.of(new FulfilmentRequest.Line("sku-a", 1)));
        service.fulfil(request);
        FulfilmentService.FulfilResult replay = service.fulfil(request);

        assertThat(replay.replayed()).isTrue();
        assertThat(shipment.created.get()).isEqualTo(1);
        assertThat(inventory.available.get("sku-a")).isEqualTo(4);
    }

    private static final class FakeInventory implements InventoryPort {
        private final ConcurrentHashMap<String, Integer> available = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, Integer> reservedQtyByRef = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, String> skuByRef = new ConcurrentHashMap<>();

        FakeInventory(Map<String, Integer> opening) {
            available.putAll(opening);
        }

        @Override
        public synchronized boolean reserve(String reference, String sku, int quantity) {
            if (reservedQtyByRef.containsKey(reference)) {
                return true;
            }
            int onHand = available.getOrDefault(sku, 0);
            if (onHand < quantity) {
                return false;
            }
            available.put(sku, onHand - quantity);
            reservedQtyByRef.put(reference, quantity);
            skuByRef.put(reference, sku);
            return true;
        }

        @Override
        public synchronized void release(String reference) {
            Integer quantity = reservedQtyByRef.remove(reference);
            if (quantity != null) {
                available.merge(skuByRef.remove(reference), quantity, Integer::sum);
            }
        }
    }

    private static final class FakeShipment implements ShipmentPort {
        private final AtomicInteger created = new AtomicInteger();

        @Override
        public String createShipment(String orderId) {
            created.incrementAndGet();
            return "shp-" + orderId;
        }
    }
}
