package org.acme.commerce.payment;

import org.acme.platform.id.Ids;

/**
 * Payment authorization logic (Part: idempotency). The core guarantee is at-most-once charging: an
 * authorization is keyed by the client's idempotency key, so a retry — a duplicate submit, a network
 * timeout that hid a success — returns the original outcome instead of charging a second time.
 *
 * <p>The authorizer itself is a deterministic simulator (no real gateway): a card whose number ends
 * in {@code 0000} is declined for insufficient funds, every other card is approved. That keeps the
 * capstone reproducible while still exercising both the approved and declined paths.
 *
 * <p><strong>Honest limitation.</strong> Idempotency here is scoped to one process's repository. A
 * real deployment needs the key's uniqueness enforced in the shared datastore (a unique constraint),
 * because two service instances behind a load balancer do not share this in-memory map.
 */
public final class PaymentService {

    private static final String DECLINE_SUFFIX = "0000";

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    /** Authorizes a request, or replays the prior result if the idempotency key was already used. */
    public Payment authorize(PaymentRequest request) {
        Payment candidate = decide(request);
        return repository.putIfAbsent(request.idempotencyKey(), candidate).orElse(candidate);
    }

    public java.util.Optional<Payment> find(String id) {
        return repository.findById(id);
    }

    private static Payment decide(PaymentRequest request) {
        String id = Ids.prefixed("pay");
        if (request.pan().endsWith(DECLINE_SUFFIX)) {
            return new Payment(id, request.orderId(), request.amount(),
                PaymentStatus.DECLINED, "insufficient funds");
        }
        return new Payment(id, request.orderId(), request.amount(),
            PaymentStatus.APPROVED, "auth-" + Ids.token(4));
    }
}
