package org.acme.commerce.order;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** The in-memory adapter for {@link OrderRepository} — the runnable default. */
public final class InMemoryOrderRepository implements OrderRepository {

    private final ConcurrentHashMap<String, Order> orders = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        orders.put(order.id(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }
}
