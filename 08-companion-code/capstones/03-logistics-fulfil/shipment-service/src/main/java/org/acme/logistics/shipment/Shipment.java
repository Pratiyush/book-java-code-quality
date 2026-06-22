package org.acme.logistics.shipment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An immutable shipment (Part: immutability). {@link #dispatched()} returns a new instance with the
 * advanced status rather than mutating this one.
 *
 * @param id      the shipment id
 * @param orderId the order this shipment fulfils
 * @param status  the lifecycle state
 */
public record Shipment(String id, String orderId, ShipmentStatus status) {

    public Shipment {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(status, "status");
    }

    Shipment dispatched() {
        return new Shipment(id, orderId, ShipmentStatus.DISPATCHED);
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("orderId", orderId);
        body.put("status", status.name());
        return body;
    }
}
