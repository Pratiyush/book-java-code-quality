package org.acme.design.bylayer.controller;

import java.util.Objects;
// tag::by-layer[]
import org.acme.design.bylayer.repository.Order;        // the orders feature is spread
import org.acme.design.bylayer.service.OrderService;    // across three layer packages

/** The controller slice of the orders feature — one of three packages a change to orders touches. */
public final class OrderController {

    private final OrderService service;
    // end::by-layer[]

    /**
     * Creates the controller over the service layer.
     *
     * @param service the order service, never {@code null}
     * @throws NullPointerException if {@code service} is {@code null}
     */
    public OrderController(OrderService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Handles a request to place an order.
     *
     * @param id              the order identifier, never {@code null}
     * @param totalMinorUnits the order total in minor units, never negative
     * @return a short confirmation summary, never {@code null}
     */
    public String placeOrder(String id, long totalMinorUnits) {
        Order placed = service.place(id, totalMinorUnits);
        return "placed " + placed.id();
    }
}
