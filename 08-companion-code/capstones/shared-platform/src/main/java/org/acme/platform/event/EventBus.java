package org.acme.platform.event;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A small in-process publish/subscribe bus for domain events (Part: events). Within one service it
 * decouples the code that records a fact ("an order was placed") from the code that reacts to it
 * ("decrement stock", "increment a metric"), so neither needs to import the other. Delivery is
 * synchronous on the publishing thread.
 *
 * <p><strong>Honest limitation.</strong> This bus is in-process and gives no durability or
 * cross-service delivery: if the JVM dies between publish and handling, the event is lost, and a
 * subscriber in another service never sees it. The capstones use it for in-service decoupling and
 * use plain HTTP calls for cross-service communication; a production system would put a broker
 * (Kafka, a queue) on that seam. A subscriber that throws is isolated and logged so one bad handler
 * cannot stop the others — but that also means a failed reaction is not retried.
 */
public final class EventBus {

    private static final Logger LOG = Logger.getLogger(EventBus.class.getName());

    private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Consumer<?>>> subscribers =
        new ConcurrentHashMap<>();

    /** Registers a handler for every event of the given type. */
    public <E extends DomainEvent> void subscribe(Class<E> eventType, Consumer<E> handler) {
        Objects.requireNonNull(eventType, "eventType");
        Objects.requireNonNull(handler, "handler");
        subscribers.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(handler);
    }

    /** Delivers an event to every handler registered for its exact runtime type. */
    @SuppressWarnings("unchecked") // every handler in a type's list was registered with that type
    public <E extends DomainEvent> void publish(E event) {
        Objects.requireNonNull(event, "event");
        List<Consumer<?>> handlers = subscribers.get(event.getClass());
        if (handlers == null) {
            return;
        }
        for (Consumer<?> handler : handlers) {
            try {
                ((Consumer<E>) handler).accept(event);
            } catch (RuntimeException e) {
                LOG.log(Level.WARNING, e, () -> "event subscriber failed for " + event.getClass().getSimpleName());
            }
        }
    }
}
