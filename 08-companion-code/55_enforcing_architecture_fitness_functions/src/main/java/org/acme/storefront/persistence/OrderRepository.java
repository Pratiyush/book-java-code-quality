package org.acme.storefront.persistence;

import java.util.Optional;
import org.acme.storefront.domain.Order;
import org.acme.storefront.domain.OrderId;

/**
 * The persistence port for orders.
 *
 * <p>The persistence layer depends only on {@code ..domain..} (the types it stores and the ids it
 * looks up) and is, in the layered rule, accessible only from {@code ..service..}. Declaring the port
 * as an interface here is what keeps the service free of any concrete storage edge.
 */
public interface OrderRepository {

    /**
     * Stores an order, replacing any existing order with the same id.
     *
     * @param order the order to store, never {@code null}
     */
    void save(Order order);

    /**
     * Looks up an order by id.
     *
     * @param id the order id, never {@code null}
     * @return the order if present, otherwise an empty {@link Optional}, never {@code null}
     */
    Optional<Order> findById(OrderId id);
}
