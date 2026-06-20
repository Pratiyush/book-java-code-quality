package org.acme.commerce.order.event;

import org.acme.platform.event.DomainEvent;

/**
 * Recorded when an order is created and priced (Part: events). Past tense, immutable: a subscriber
 * reacts to the fact (emit a metric, notify fulfilment) without the order code knowing who listens.
 *
 * @param orderId                the order that was placed
 * @param totalMinor             the order total in minor units
 * @param currency               the total's currency
 * @param occurredAtEpochMillis  when the order was placed
 */
public record OrderPlaced(String orderId, long totalMinor, String currency, long occurredAtEpochMillis)
    implements DomainEvent {
}
