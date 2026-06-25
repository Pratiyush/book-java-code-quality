package org.acme.immutability;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Accepts validated orders and exposes them as an immutable snapshot — the module's small service
 * seam, wiring the value types into a runnable shape with an observability surface and an explicit
 * failure path.
 *
 * <p>The book of accepted orders is internal mutable state, so it is defended on the way out: {@code
 * acceptedOrders()} returns a {@link List#copyOf snapshot}, never the live list, which is the
 * collections-level twin of {@link Order}'s defensive copy. Returning {@code unmodifiableList(book)}
 * instead would hand back a read-only <em>view</em> that still changes as new orders arrive — a live
 * window, not a snapshot — which is the exact distinction the chapter draws and the reason {@code
 * copyOf} is used here.
 */
public final class OrderBook {

    private static final Logger LOG = System.getLogger(OrderBook.class.getName());

    private final List<Order> book = new ArrayList<>();

    /** Observability: how many orders were turned away by a precondition (illustrative, Chapter 45). */
    private final AtomicLong rejected = new AtomicLong();

    /**
     * Accepts an order after checking it is well-formed, or rejects it with a typed error.
     *
     * @param order the order to accept, never {@code null}
     * @return the accepted order (the same immutable value), never {@code null}
     * @throws NullPointerException   if {@code order} is {@code null}
     * @throws OrderRejectedException if the order has no line items or mixes currencies
     * @implSpec Validates before any state change, so a rejected order leaves the book untouched.
     */
    public Order accept(Order order) {
        Objects.requireNonNull(order, "order");
        if (order.items().isEmpty()) {
            rejected.incrementAndGet();
            throw new OrderRejectedException("empty-order", "an order needs at least one line item");
        }
        String currency = order.items().get(0).price().currency();
        boolean mixed = order.items().stream()
            .anyMatch(item -> !item.price().currency().equals(currency));
        if (mixed) {
            rejected.incrementAndGet();
            throw new OrderRejectedException("currency-mismatch", "all lines must share a currency");
        }
        book.add(order);
        LOG.log(Level.DEBUG, "accepted order {0}", order.id());
        return order;
    }

    /**
     * Returns the accepted orders as an immutable snapshot.
     *
     * @return an immutable copy of the accepted orders, never {@code null}; later acceptances do not
     *     change a previously returned list
     */
    public List<Order> acceptedOrders() {
        return List.copyOf(book);                   // snapshot, not a live unmodifiable view
    }

    /**
     * Health/observability surface: the running count of orders rejected by a precondition.
     *
     * @return the number of rejected orders since startup, never negative
     */
    public long rejectedCount() {
        return rejected.get();
    }

    /**
     * A readiness probe: the book is ready as soon as it is constructed.
     *
     * @return {@code true} when the book can accept orders
     */
    public boolean isReady() {
        return book != null;
    }
}
