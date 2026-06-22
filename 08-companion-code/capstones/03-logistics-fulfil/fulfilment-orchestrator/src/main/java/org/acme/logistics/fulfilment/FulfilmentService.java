package org.acme.logistics.fulfilment;

import java.util.ArrayList;
import java.util.List;
import org.acme.platform.error.ApiException;

/**
 * The fulfilment saga (Part: orchestration & compensation). To fulfil an order it must reserve every
 * line and create a shipment — and it must do so atomically in effect: if any line is out of stock it
 * RELEASES the lines it already reserved (the compensating action) and fails the whole fulfilment, so
 * an order is never left half-reserved. A retry for an already-fulfilled order returns the original
 * result rather than reserving and shipping again.
 *
 * <p><strong>Honest limitation.</strong> This is a best-effort saga, not a distributed transaction.
 * Reservations and the shipment are separate calls, so a crash between the last reservation and the
 * shipment create can leave stock reserved without a shipment; a retry re-runs the (idempotent)
 * reservations and creates the (idempotent) shipment, but a reservation orphaned by a permanent
 * failure needs a timeout/reaper to release it. Real sagas pair every step with such a sweeper.
 */
public final class FulfilmentService {

    private final FulfilmentRepository fulfilments;
    private final InventoryPort inventory;
    private final ShipmentPort shipments;

    public FulfilmentService(FulfilmentRepository fulfilments, InventoryPort inventory, ShipmentPort shipments) {
        this.fulfilments = fulfilments;
        this.inventory = inventory;
        this.shipments = shipments;
    }

    public FulfilResult fulfil(FulfilmentRequest request) {
        var existing = fulfilments.findByOrderId(request.orderId());
        if (existing.isPresent()) {
            return new FulfilResult(existing.get(), true);
        }

        List<String> reserved = new ArrayList<>();
        for (FulfilmentRequest.Line line : request.lines()) {
            String reference = request.orderId() + ":" + line.sku();
            if (inventory.reserve(reference, line.sku(), line.quantity())) {
                reserved.add(reference);
            } else {
                compensate(reserved);
                throw ApiException.conflict("out-of-stock",
                    "cannot fulfil " + request.orderId() + ": " + line.sku() + " is out of stock");
            }
        }

        String shipmentId = shipments.createShipment(request.orderId());
        Fulfilment fulfilment = new Fulfilment(request.orderId(), shipmentId);
        fulfilments.save(fulfilment);
        return new FulfilResult(fulfilment, false);
    }

    public java.util.Optional<Fulfilment> find(String orderId) {
        return fulfilments.findByOrderId(orderId);
    }

    private void compensate(List<String> reservedReferences) {
        for (String reference : reservedReferences) {
            inventory.release(reference);
        }
    }

    /**
     * The outcome of a fulfilment.
     *
     * @param fulfilment the completed fulfilment
     * @param replayed   true when the order was already fulfilled (an idempotent replay)
     */
    public record FulfilResult(Fulfilment fulfilment, boolean replayed) {
    }
}
