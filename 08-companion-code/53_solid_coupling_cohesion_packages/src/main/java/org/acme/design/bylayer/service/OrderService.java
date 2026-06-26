package org.acme.design.bylayer.service;

import java.util.Objects;
import org.acme.design.bylayer.repository.Order;
import org.acme.design.bylayer.repository.OrderRepository;

/**
 * The service layer for orders: the second slice of the same feature.
 *
 * <p>It depends on the repository layer, and the controller layer depends on it. The orders feature
 * therefore lives in three packages at once.
 */
public final class OrderService {

    private final OrderRepository repository;

    /**
     * Creates the service over the repository layer.
     *
     * @param repository the order repository, never {@code null}
     * @throws NullPointerException if {@code repository} is {@code null}
     */
    public OrderService(OrderRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    /**
     * Places an order with the given id and total.
     *
     * @param id              the order identifier, never {@code null}
     * @param totalMinorUnits the order total in minor units, never negative
     * @return the placed order, never {@code null}
     */
    public Order place(String id, long totalMinorUnits) {
        Order order = new Order(id, totalMinorUnits);
        repository.save(order);
        return order;
    }
}
