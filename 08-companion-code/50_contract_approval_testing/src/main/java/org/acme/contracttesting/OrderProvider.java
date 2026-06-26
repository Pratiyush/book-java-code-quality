package org.acme.contracttesting;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The provider side of the boundary: it owns order data and renders an order as a small JSON-shaped
 * response, standing in for {@code GET /orders/{id}} on a running service.
 *
 * <p>This is the side a consumer depends on. The response field name for the identifier is held in one
 * place — {@code idFieldName} — so the chapter's failure path can be shown without rewriting the class:
 * renaming it from {@code "id"} to {@code "orderId"} is exactly the tidy, fully-covered change from the
 * hook that breaks a consumer. The provider's own shape test does not notice (the field is still there,
 * just renamed); the consumer-driven contract does.
 */
public final class OrderProvider {

    private static final Logger LOG = System.getLogger(OrderProvider.class.getName());

    /** The default JSON field name for an order's identifier — the name the consumer relies on. */
    public static final String DEFAULT_ID_FIELD = "id";

    private final Map<String, Order> orders;
    private final String idFieldName;

    /** Observability: how many lookups missed (illustrative, Chapter 45). */
    private final AtomicLong notFoundCount = new AtomicLong();

    /**
     * Creates a provider over a fixed set of orders, rendering the identifier under {@code idFieldName}.
     *
     * @param orders      the orders this provider serves, keyed by id, never {@code null}
     * @param idFieldName the JSON field name to render the identifier under, never {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
    public OrderProvider(Map<String, Order> orders, String idFieldName) {
        this.orders = Map.copyOf(Objects.requireNonNull(orders, "orders"));
        this.idFieldName = Objects.requireNonNull(idFieldName, "idFieldName");
    }

    /**
     * Looks up an order by id.
     *
     * @param id the order id to resolve, never {@code null}
     * @return the order if one exists, otherwise an empty {@code Optional} — never {@code null}
     * @throws NullPointerException if {@code id} is {@code null}
     */
    public Optional<Order> findById(String id) {
        Objects.requireNonNull(id, "id");
        Optional<Order> found = Optional.ofNullable(orders.get(id));
        if (found.isEmpty()) {
            notFoundCount.incrementAndGet();
            LOG.log(Level.DEBUG, "no order {0}", id);
        }
        return found;
    }

    /**
     * Renders an order as the JSON-shaped response a consumer reads over the wire.
     *
     * @param id the order id to serve, never {@code null}
     * @return the response body
     * @throws OrderNotFoundException if no order has that id
     */
    // tag::provider-render[]
    public String renderOrder(String id) {
        Order order = findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return "{\"" + idFieldName + "\":\"" + order.id() + "\","
            + "\"status\":\"" + order.status() + "\","
            + "\"total\":" + order.total() + "}";
    }
    // end::provider-render[]

    /**
     * Health/observability surface: the running count of lookups that missed.
     *
     * @return the number of not-found lookups since startup, never negative
     */
    public long notFoundCount() {
        return notFoundCount.get();
    }

    /**
     * A readiness probe: the provider is ready once it has orders to serve.
     *
     * @return {@code true} when the provider can serve at least one order
     */
    public boolean isReady() {
        return !orders.isEmpty();
    }
}
