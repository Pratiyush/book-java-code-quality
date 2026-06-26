package org.acme.storefront.persistence;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.acme.storefront.domain.Order;
import org.acme.storefront.domain.OrderId;

/**
 * An in-memory {@link OrderRepository}, the storage adapter for the sample.
 *
 * <p>It depends on the domain types it persists and on nothing in the layers above it, which is the
 * one-way dependency the {@code layeredArchitecture()} rule asserts. A real adapter would talk to a
 * database; the contract the architecture cares about is the same either way.
 */
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> store = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        Objects.requireNonNull(order, "order");
        store.put(order.id(), order);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(store.get(id));
    }
}
