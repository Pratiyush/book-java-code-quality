package org.acme.logistics.fulfilment;

/**
 * The outbound PORT to inventory-service (Part: architecture). Wired to {@link InventoryClient} in
 * production, faked in tests.
 */
public interface InventoryPort {

    /** Reserves stock; returns true when secured (or already secured), false when out of stock. */
    boolean reserve(String reference, String sku, int quantity);

    /** Releases a prior reservation (the compensating action). */
    void release(String reference);
}
