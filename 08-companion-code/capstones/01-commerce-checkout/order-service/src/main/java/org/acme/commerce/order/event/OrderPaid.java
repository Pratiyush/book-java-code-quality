package org.acme.commerce.order.event;

import org.acme.platform.event.DomainEvent;

/**
 * Recorded when an order is successfully charged (Part: events).
 *
 * @param orderId                the order that was paid
 * @param paymentId              the authorizing payment id
 * @param occurredAtEpochMillis  when the order was paid
 */
public record OrderPaid(String orderId, String paymentId, long occurredAtEpochMillis)
    implements DomainEvent {
}
