package org.acme.logistics.shipment;

import java.util.Optional;
import org.acme.platform.error.ApiException;
import org.acme.platform.id.Ids;

/** Shipment application logic (Part: layering) — idempotent creation, dispatch, lookup. */
public final class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    /** Creates a shipment for an order; a repeat for the same order returns the original. */
    public CreateResult create(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw ApiException.badRequest("order-missing", "orderId is required");
        }
        Shipment shipment = new Shipment(Ids.prefixed("shp"), orderId, ShipmentStatus.CREATED);
        Optional<Shipment> existing = repository.createIfAbsent(shipment);
        return existing.map(s -> new CreateResult(s, true)).orElseGet(() -> new CreateResult(shipment, false));
    }

    public Shipment dispatch(String id) {
        Shipment shipment = require(id);
        Shipment dispatched = shipment.dispatched();
        repository.save(dispatched);
        return dispatched;
    }

    public Shipment require(String id) {
        return repository.findById(id)
            .orElseThrow(() -> ApiException.notFound("shipment-unknown", "no shipment with id " + id));
    }

    /**
     * The outcome of a create call.
     *
     * @param shipment the shipment
     * @param replayed true when the order already had a shipment (an idempotent replay)
     */
    public record CreateResult(Shipment shipment, boolean replayed) {
    }
}
