package org.acme.storefront.service;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.acme.storefront.domain.Money;
import org.acme.storefront.domain.Order;
import org.acme.storefront.domain.OrderId;
import org.acme.storefront.persistence.OrderRepository;

/**
 * Places and reads orders, the service layer of the sample.
 *
 * <p>It depends downward on {@code ..domain..} and {@code ..persistence..} and upward on nothing —
 * the direction the {@code layeredArchitecture()} rule pins. It also holds the module's failure path
 * and observability surface: an unknown order id is rejected with {@link OrderNotFoundException}, and
 * a running count of those rejections is exposed for a metric or health view.
 */
public final class OrderService {

    private static final Logger LOG = System.getLogger(OrderService.class.getName());

    private final OrderRepository repository;

    /** Observability: how many reads were rejected because the order did not exist. */
    private final AtomicLong notFoundCount = new AtomicLong();

    /**
     * Creates the service over a repository port.
     *
     * @param repository the order store, never {@code null}
     * @throws NullPointerException if {@code repository} is {@code null}
     */
    public OrderService(OrderRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    /**
     * Places an order for the given total.
     *
     * @param id    the new order's id, never {@code null}
     * @param total the order total, never {@code null}
     * @return the placed order, never {@code null}
     */
    public Order place(OrderId id, Money total) {
        Order order = new Order(id, total);
        repository.save(order);
        LOG.log(Level.DEBUG, "placed order {0}", id.value());
        return order;
    }

    /**
     * Reads an order, rejecting an unknown id on the explicit failure path.
     *
     * @param id the order id to read, never {@code null}
     * @return the order, never {@code null}
     * @throws NullPointerException   if {@code id} is {@code null}
     * @throws OrderNotFoundException if no order has that id
     */
    public Order read(OrderId id) {
        Objects.requireNonNull(id, "id");
        return repository.findById(id).orElseThrow(() -> {
            notFoundCount.incrementAndGet();
            return new OrderNotFoundException(id);
        });
    }

    /**
     * Health/observability surface: the running count of not-found rejections.
     *
     * @return the number of reads rejected as not-found since startup, never negative
     */
    public long notFoundCount() {
        return notFoundCount.get();
    }
}
