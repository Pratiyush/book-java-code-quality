package org.acme.logistics.shipment;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The in-memory adapter for {@link ShipmentRepository} — the runnable default. {@code createIfAbsent}
 * uses the order-id map's atomic {@code putIfAbsent} so a retried fulfilment never produces a second
 * shipment for the same order.
 */
public final class InMemoryShipmentRepository implements ShipmentRepository {

    private final ConcurrentHashMap<String, Shipment> byOrderId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Shipment> byId = new ConcurrentHashMap<>();

    @Override
    public Optional<Shipment> createIfAbsent(Shipment shipment) {
        Shipment existing = byOrderId.putIfAbsent(shipment.orderId(), shipment);
        if (existing != null) {
            return Optional.of(existing);
        }
        byId.put(shipment.id(), shipment);
        return Optional.empty();
    }

    @Override
    public Optional<Shipment> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public void save(Shipment shipment) {
        byId.put(shipment.id(), shipment);
        byOrderId.put(shipment.orderId(), shipment);
    }
}
