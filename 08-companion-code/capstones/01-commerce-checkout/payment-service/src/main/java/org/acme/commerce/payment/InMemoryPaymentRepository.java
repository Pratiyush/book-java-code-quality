package org.acme.commerce.payment;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The in-memory adapter for {@link PaymentRepository} — the runnable default (Part: concurrency).
 * Both maps are {@link ConcurrentHashMap}s, and {@link #putIfAbsent} relies on the map's atomic
 * {@code putIfAbsent} so that two concurrent retries with the same idempotency key cannot both win:
 * exactly one stores its payment, the other sees it and returns it. This is the in-memory analogue
 * of a unique-key constraint.
 */
public final class InMemoryPaymentRepository implements PaymentRepository {

    private final ConcurrentHashMap<String, Payment> byKey = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Payment> byId = new ConcurrentHashMap<>();

    @Override
    public Optional<Payment> putIfAbsent(String idempotencyKey, Payment payment) {
        Payment existing = byKey.putIfAbsent(idempotencyKey, payment);
        if (existing != null) {
            return Optional.of(existing);
        }
        byId.put(payment.id(), payment);
        return Optional.empty();
    }

    @Override
    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }
}
