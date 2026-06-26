package org.acme.design.byfeature.orders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The orders feature, kept whole in one package.
 *
 * <p>The displayed region below shows the cohesion this slicing buys: the data type, the service, and
 * the in-memory storage are one unit, so placing and finding an order is a change that stays inside
 * this package rather than spreading across layer packages.
 */
public final class OrderService {

    // tag::by-feature[]
    // orders feature kept whole: data type, service, and storage in one package
    private final Map<String, Order> store = new HashMap<>();

    public Order place(String id, long totalMinorUnits) {
        Order order = new Order(id, totalMinorUnits);  // Order lives in this same package
        store.put(order.id(), order);
        return order;
    }
    // end::by-feature[]

    /**
     * Finds an order by id.
     *
     * @param id the order identifier
     * @return the order if present, otherwise an empty {@link Optional}
     */
    public Optional<Order> find(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
