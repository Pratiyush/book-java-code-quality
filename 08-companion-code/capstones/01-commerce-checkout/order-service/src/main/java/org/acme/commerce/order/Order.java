package org.acme.commerce.order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.acme.platform.money.Money;

/**
 * An immutable order (Part: immutability). The line items are defensively copied to an unmodifiable
 * list in the compact constructor, and every state change returns a new instance
 * ({@link #paid}, {@link #declined}) rather than mutating this one — so an {@code Order} reference
 * handed to one collaborator can never be changed underneath another.
 *
 * @param id        the order identifier
 * @param items     the order lines; copied defensively, never empty
 * @param total     the computed total
 * @param status    the lifecycle state
 * @param paymentId the authorizing payment id once paid, otherwise {@code null}
 */
public record Order(String id, List<OrderItem> items, Money total, OrderStatus status, String paymentId) {

    public Order {
        Objects.requireNonNull(id, "id");
        items = List.copyOf(items); // defensive, immutable copy (also rejects null elements)
        Objects.requireNonNull(total, "total");
        Objects.requireNonNull(status, "status");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("an order must have at least one item");
        }
    }

    static Order pending(String id, List<OrderItem> items, Money total) {
        return new Order(id, items, total, OrderStatus.PENDING, null);
    }

    Order paid(String authorizingPaymentId) {
        return new Order(id, items, total, OrderStatus.PAID, authorizingPaymentId);
    }

    Order declined() {
        return new Order(id, items, total, OrderStatus.DECLINED, null);
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("status", status.name());
        body.put("totalMinor", total.minorUnits());
        body.put("currency", total.currency());
        body.put("lineCount", items.size());
        if (paymentId != null) {
            body.put("paymentId", paymentId);
        }
        return body;
    }
}
