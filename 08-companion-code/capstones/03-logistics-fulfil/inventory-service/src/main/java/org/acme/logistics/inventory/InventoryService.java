package org.acme.logistics.inventory;

import org.acme.platform.error.ApiException;

/** Inventory application logic (Part: layering) — validates input, delegates atomic moves to the port. */
public final class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public InventoryRepository.ReserveOutcome reserve(String reference, String sku, int quantity) {
        if (quantity <= 0) {
            throw ApiException.unprocessable("quantity-not-positive", "quantity must be positive");
        }
        return repository.reserve(reference, sku, quantity);
    }

    public InventoryRepository.ReleaseOutcome release(String reference) {
        return repository.release(reference);
    }

    public int available(String sku) {
        return repository.available(sku);
    }
}
