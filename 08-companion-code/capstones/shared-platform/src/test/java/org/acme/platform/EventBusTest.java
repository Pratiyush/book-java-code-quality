package org.acme.platform;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import org.acme.platform.event.DomainEvent;
import org.acme.platform.event.EventBus;
import org.junit.jupiter.api.Test;

class EventBusTest {

    record ThingHappened(String id, long occurredAtEpochMillis) implements DomainEvent { }

    @Test
    void deliversToEverySubscriberOfTheType() {
        EventBus bus = new EventBus();
        AtomicInteger first = new AtomicInteger();
        AtomicInteger second = new AtomicInteger();
        bus.subscribe(ThingHappened.class, e -> first.incrementAndGet());
        bus.subscribe(ThingHappened.class, e -> second.incrementAndGet());

        bus.publish(new ThingHappened("x", 0L));

        assertThat(first.get()).isEqualTo(1);
        assertThat(second.get()).isEqualTo(1);
    }

    @Test
    void isolatesAFailingSubscriber() {
        EventBus bus = new EventBus();
        AtomicInteger survivor = new AtomicInteger();
        bus.subscribe(ThingHappened.class, e -> {
            throw new IllegalStateException("bad handler");
        });
        bus.subscribe(ThingHappened.class, e -> survivor.incrementAndGet());

        bus.publish(new ThingHappened("x", 0L));

        assertThat(survivor.get()).isEqualTo(1);
    }
}
