package org.acme.storefront.web;

import java.util.Objects;
import org.acme.storefront.domain.Money;
import org.acme.storefront.domain.Order;
import org.acme.storefront.domain.OrderId;
import org.acme.storefront.service.OrderService;

/**
 * The web entry point for orders, the topmost layer of the sample.
 *
 * <p>It calls down into {@code ..service..} and is, in the layered rule, accessed by no other layer
 * ({@code whereLayer("Web").mayNotBeAccessedByAnyLayer()}). It deliberately does not touch
 * {@code ..persistence..}: routing a repository call through the controller would be the cross-layer
 * edge the rule exists to stop. This is a plain class rather than a framework controller so the module
 * stays JDK-only; the dependency direction the architecture cares about is identical either way.
 */
public final class OrderController {

    private final OrderService service;

    /**
     * Creates the controller over the order service.
     *
     * @param service the order service, never {@code null}
     * @throws NullPointerException if {@code service} is {@code null}
     */
    public OrderController(OrderService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Handles a place-order request.
     *
     * @param id    the new order's id, never {@code null}
     * @param total the order total, never {@code null}
     * @return the placed order, never {@code null}
     */
    public Order placeOrder(OrderId id, Money total) {
        return service.place(id, total);
    }

    /**
     * Handles a read-order request.
     *
     * @param id the order id to read, never {@code null}
     * @return the order, never {@code null}
     */
    public Order getOrder(OrderId id) {
        return service.read(id);
    }
}
