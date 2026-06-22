package org.acme.logistics.inventory;

/**
 * The persistence PORT for stock (Part: architecture / hexagonal boundaries). Reserve and release are
 * the only ways stock moves, and both must be atomic with respect to the available count — that is
 * the contract that prevents two orders from reserving the same last unit.
 */
public interface InventoryRepository {

    /** The outcome of a reserve attempt. */
    enum ReserveOutcome { RESERVED, ALREADY_RESERVED, INSUFFICIENT }

    /** The outcome of a release attempt. */
    enum ReleaseOutcome { RELEASED, ALREADY_RELEASED, UNKNOWN }

    /** Atomically reserves {@code quantity} of {@code sku}; idempotent on {@code reference}. */
    ReserveOutcome reserve(String reference, String sku, int quantity);

    /** Returns previously-reserved stock; idempotent on {@code reference}. */
    ReleaseOutcome release(String reference);

    int available(String sku);
}
