package org.acme.design;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.acme.design.inverted.orders.OrderEvents;
import org.junit.jupiter.api.Test;

/**
 * Both the cyclic and the inverted collaborations announce orders at runtime; the difference is
 * structural, not behavioural. The cyclic wiring is awkward because each package needs the other; the
 * inverted variant additionally shows that {@code orders} can be exercised entirely on its own, given
 * any {@link OrderEvents} listener, because it depends on nothing in {@code notify}. Both packages are
 * referenced by fully-qualified name so the two same-named {@code OrderService}/{@code OrderNotifier}
 * types do not collide — itself a small reminder that the cycle pair is hard to keep apart.
 */
class CycleAndInversionTest {

    @Test
    void cyclicCollaborationAnnouncesButNeedsTwoStepWiring() {
        // the cycle cannot be wired by constructor alone: the service is built, the notifier is built
        // around it, then the notifier is set back on the service. The setter is the consequence the
        // cycle forces — neither side can be fully constructed before the other exists.
        org.acme.design.cycle.orders.OrderService orders = new org.acme.design.cycle.orders.OrderService();
        org.acme.design.cycle.notify.OrderNotifier notifier =
            new org.acme.design.cycle.notify.OrderNotifier(orders);
        orders.setNotifier(notifier);

        orders.place(new org.acme.design.cycle.orders.PlacedOrder("c1", 500L));

        assertThat(notifier.sent()).hasSize(1);
        assertThat(notifier.sent().get(0)).contains("c1");
    }

    @Test
    void invertedCollaborationAnnouncesWithCleanOneWayWiring() {
        List<String> placed = new ArrayList<>();
        org.acme.design.inverted.orders.OrderService orders =
            new org.acme.design.inverted.orders.OrderService(placed::add);

        orders.place(new org.acme.design.inverted.orders.PlacedOrder("i1", 750L));

        assertThat(placed).containsExactly("i1");          // orders signals through the owned abstraction
    }

    @Test
    void invertedOrdersRunsAgainstAnyEventsListenerOnItsOwn() {
        // orders depends only on the abstraction it owns, so a test listener stands in for notify
        List<String> placed = new ArrayList<>();
        OrderEvents listener = placed::add;
        org.acme.design.inverted.orders.OrderService orders =
            new org.acme.design.inverted.orders.OrderService(listener);

        orders.place(new org.acme.design.inverted.orders.PlacedOrder("i2", 250L));

        assertThat(placed).containsExactly("i2");
    }
}
