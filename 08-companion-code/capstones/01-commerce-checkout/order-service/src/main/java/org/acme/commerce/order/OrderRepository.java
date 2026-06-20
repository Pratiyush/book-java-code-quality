package org.acme.commerce.order;

import java.util.Optional;

/**
 * The persistence PORT for orders (Part: architecture / hexagonal boundaries). {@code save} is an
 * upsert: it both creates an order and stores its later state transitions, so the service never
 * cares whether a write is an insert or an update.
 */
public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(String id);
}
