package org.acme.logistics.inventory;

/**
 * A stock reservation against one SKU (Part: value modelling). Immutable; releasing produces a new
 * {@link #released()} instance rather than mutating this one, so the reservation's history is a
 * sequence of values, not in-place edits.
 *
 * @param reference the idempotency reference (a re-reserve with this reference is a no-op)
 * @param sku       the reserved SKU
 * @param quantity  the reserved quantity
 * @param active    true while the reservation holds stock; false once released
 */
public record Reservation(String reference, String sku, int quantity, boolean active) {

    Reservation released() {
        return new Reservation(reference, sku, quantity, false);
    }
}
