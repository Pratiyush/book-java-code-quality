package org.acme.platform.event;

/**
 * Marker for an event that records something that already happened in the domain (Part: events).
 * Implementations are immutable records named in the past tense — {@code OrderPlaced},
 * {@code FundsReserved} — so a subscriber reacts to a fact, never to a command.
 */
public interface DomainEvent {

    /** The instant the event occurred, as epoch milliseconds. */
    long occurredAtEpochMillis();
}
