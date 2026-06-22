package org.acme.commerce.payment;

import java.util.Optional;

/**
 * The persistence PORT for payments (Part: architecture / hexagonal boundaries). It is keyed by the
 * client's idempotency key, which is what lets {@link PaymentService} guarantee at-most-once
 * charging: {@code putIfAbsent} stores the first authorization for a key and returns any existing
 * one, atomically. A real adapter would back this with a unique constraint on the key column.
 */
public interface PaymentRepository {

    /**
     * Stores {@code payment} under {@code idempotencyKey} only if no payment is stored there yet.
     *
     * @return the existing payment if the key was already present, otherwise empty
     */
    Optional<Payment> putIfAbsent(String idempotencyKey, Payment payment);

    Optional<Payment> findById(String id);
}
