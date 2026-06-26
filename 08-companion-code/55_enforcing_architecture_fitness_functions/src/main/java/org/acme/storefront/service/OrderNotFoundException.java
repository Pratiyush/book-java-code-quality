package org.acme.storefront.service;

import org.acme.storefront.domain.OrderId;

/**
 * The defined error response for a lookup of an order that does not exist.
 *
 * <p>This is the module's explicit failure path: the service rejects an unknown id with a typed,
 * specific exception rather than returning {@code null} or a generic {@link RuntimeException}, which
 * keeps it clear of the {@code NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS} coding rule as well.
 */
public final class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a missing order id.
     *
     * @param id the order id that was not found, never {@code null}
     */
    public OrderNotFoundException(OrderId id) {
        super("no order with id " + id.value());
    }
}
