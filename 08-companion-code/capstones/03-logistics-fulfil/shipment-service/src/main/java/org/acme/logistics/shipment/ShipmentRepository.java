package org.acme.logistics.shipment;

import java.util.Optional;

/** The persistence PORT for shipments (Part: architecture / hexagonal boundaries). */
public interface ShipmentRepository {

    /**
     * Stores {@code shipment} under its order id only if none exists yet for that order.
     *
     * @return the existing shipment if the order already had one, otherwise empty
     */
    Optional<Shipment> createIfAbsent(Shipment shipment);

    Optional<Shipment> findById(String id);

    void save(Shipment shipment);
}
