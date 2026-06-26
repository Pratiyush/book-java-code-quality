package org.acme.design.bylayer.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The repository layer for orders: stores and looks them up.
 *
 * <p>One slice of the orders feature. The other two slices (service, controller) live in sibling
 * packages, so the feature is spread across the layer packages.
 */
public final class OrderRepository {

    private final Map<String, Order> store = new HashMap<>();

    /**
     * Saves an order.
     *
     * @param order the order to save, never {@code null}
     */
    public void save(Order order) {
        store.put(order.id(), order);
    }

    /**
     * Looks an order up by id.
     *
     * @param id the order identifier
     * @return the order if present, otherwise an empty {@link Optional}
     */
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
