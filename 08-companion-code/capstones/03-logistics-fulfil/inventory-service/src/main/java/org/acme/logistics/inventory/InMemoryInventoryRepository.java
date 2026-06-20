package org.acme.logistics.inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * The in-memory adapter for {@link InventoryRepository} — the runnable default (Part: concurrency).
 * Reserve and release are {@code synchronized} so the check-available-then-decrement step is atomic:
 * two concurrent reservations for the last unit cannot both succeed. A coarse monitor is the simplest
 * thing that is provably correct here; a production adapter would use a conditional UPDATE (e.g.
 * {@code SET available = available - ? WHERE sku = ? AND available >= ?}) to push the same atomicity
 * into the database.
 */
public final class InMemoryInventoryRepository implements InventoryRepository {

    private final Map<String, Integer> available = new HashMap<>();
    private final Map<String, Reservation> byReference = new HashMap<>();

    public InMemoryInventoryRepository() {
        available.put("sku-keyboard", 5);
        available.put("sku-mouse", 3);
        available.put("sku-monitor", 1);
    }

    @Override
    public synchronized ReserveOutcome reserve(String reference, String sku, int quantity) {
        Reservation existing = byReference.get(reference);
        if (existing != null && existing.active()) {
            // An active reservation under this reference already holds the stock — idempotent no-op.
            // A *released* reference (after compensation) falls through and may re-acquire below.
            return ReserveOutcome.ALREADY_RESERVED;
        }
        int onHand = available.getOrDefault(sku, 0);
        if (onHand < quantity) {
            return ReserveOutcome.INSUFFICIENT;
        }
        available.put(sku, onHand - quantity);
        byReference.put(reference, new Reservation(reference, sku, quantity, true));
        return ReserveOutcome.RESERVED;
    }

    @Override
    public synchronized ReleaseOutcome release(String reference) {
        Reservation reservation = byReference.get(reference);
        if (reservation == null) {
            return ReleaseOutcome.UNKNOWN;
        }
        if (!reservation.active()) {
            return ReleaseOutcome.ALREADY_RELEASED;
        }
        available.merge(reservation.sku(), reservation.quantity(), Integer::sum);
        byReference.put(reference, reservation.released());
        return ReleaseOutcome.RELEASED;
    }

    @Override
    public synchronized int available(String sku) {
        return available.getOrDefault(sku, 0);
    }
}
