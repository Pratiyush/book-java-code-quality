package org.acme.commerce.order;

import java.util.List;
import java.util.function.LongSupplier;
import org.acme.commerce.order.event.OrderPaid;
import org.acme.commerce.order.event.OrderPlaced;
import org.acme.platform.error.ApiException;
import org.acme.platform.event.EventBus;
import org.acme.platform.id.Ids;
import org.acme.platform.money.Money;

/**
 * The order orchestrator (Part: composition). It is the only place that knows the checkout flow:
 * price the order against catalog-service, record it, then charge it through payment-service and
 * record the outcome. Every collaborator is an injected port, so this logic is unit-tested with
 * fakes and never reaches across a network in a test.
 *
 * <p>The clock is injected as a {@link LongSupplier} rather than read from
 * {@code System.currentTimeMillis()} inline, so event timestamps are deterministic under test.
 */
public final class OrderService {

    private final OrderRepository repository;
    private final PricingPort pricing;
    private final PaymentPort payment;
    private final EventBus events;
    private final LongSupplier clock;

    public OrderService(OrderRepository repository, PricingPort pricing, PaymentPort payment,
                        EventBus events, LongSupplier clock) {
        this.repository = repository;
        this.pricing = pricing;
        this.payment = payment;
        this.events = events;
        this.clock = clock;
    }

    /** Prices and records a new order in {@code PENDING}, then announces it. */
    public Order place(List<OrderItem> items) {
        if (items.isEmpty()) {
            throw ApiException.unprocessable("order-empty", "an order must have at least one item");
        }
        Money total = pricing.priceOrder(items);
        Order order = Order.pending(Ids.prefixed("ord"), items, total);
        repository.save(order);
        events.publish(new OrderPlaced(order.id(), total.minorUnits(), total.currency(), clock.getAsLong()));
        return order;
    }

    /**
     * Charges an order. A second call on an already-paid order returns it unchanged (so a retried
     * pay request does not re-charge); otherwise it authorizes and records PAID or DECLINED.
     */
    public Order pay(String orderId, String pan, String idempotencyKey) {
        Order order = repository.findById(orderId)
            .orElseThrow(() -> ApiException.notFound("order-unknown", "no order with id " + orderId));
        if (order.status() == OrderStatus.PAID) {
            return order;
        }
        PaymentPort.PaymentDecision decision =
            payment.authorize(order.id(), order.total(), pan, idempotencyKey);
        if (decision.approved()) {
            Order paid = order.paid(decision.paymentId());
            repository.save(paid);
            events.publish(new OrderPaid(paid.id(), decision.paymentId(), clock.getAsLong()));
            return paid;
        }
        Order declined = order.declined();
        repository.save(declined);
        return declined;
    }

    public Order require(String orderId) {
        return repository.findById(orderId)
            .orElseThrow(() -> ApiException.notFound("order-unknown", "no order with id " + orderId));
    }
}
