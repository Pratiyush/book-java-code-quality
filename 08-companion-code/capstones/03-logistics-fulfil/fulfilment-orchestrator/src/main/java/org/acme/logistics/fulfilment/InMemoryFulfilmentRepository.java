package org.acme.logistics.fulfilment;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** The in-memory adapter for {@link FulfilmentRepository} — the runnable default. */
public final class InMemoryFulfilmentRepository implements FulfilmentRepository {

    private final ConcurrentHashMap<String, Fulfilment> byOrderId = new ConcurrentHashMap<>();

    @Override
    public Optional<Fulfilment> findByOrderId(String orderId) {
        return Optional.ofNullable(byOrderId.get(orderId));
    }

    @Override
    public void save(Fulfilment fulfilment) {
        byOrderId.put(fulfilment.orderId(), fulfilment);
    }
}
