package org.acme.logistics.fulfilment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A completed fulfilment record (Part: value modelling) — stored per order so a retried fulfilment
 * returns the original outcome rather than reserving and shipping again.
 *
 * @param orderId    the fulfilled order
 * @param shipmentId the shipment created for it
 */
public record Fulfilment(String orderId, String shipmentId) {

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("orderId", orderId);
        body.put("shipmentId", shipmentId);
        body.put("status", "FULFILLED");
        return body;
    }
}
