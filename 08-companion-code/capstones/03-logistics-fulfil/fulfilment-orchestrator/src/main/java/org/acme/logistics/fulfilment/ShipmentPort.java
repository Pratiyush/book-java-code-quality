package org.acme.logistics.fulfilment;

/**
 * The outbound PORT to shipment-service (Part: architecture). Wired to {@link ShipmentClient} in
 * production, faked in tests.
 */
public interface ShipmentPort {

    /** Creates (idempotently) a shipment for the order and returns its id. */
    String createShipment(String orderId);
}
