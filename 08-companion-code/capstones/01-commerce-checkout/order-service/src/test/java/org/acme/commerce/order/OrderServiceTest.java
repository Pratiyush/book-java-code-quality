package org.acme.commerce.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.acme.platform.error.ApiException;
import org.acme.platform.event.EventBus;
import org.acme.platform.money.Money;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private final InMemoryOrderRepository repository = new InMemoryOrderRepository();

    /** A pricing fake: every line is 1000 minor units times its quantity. */
    private final PricingPort pricing = items -> {
        long units = items.stream().mapToLong(OrderItem::quantity).sum();
        return Money.of(1000L * units, "USD");
    };

    private OrderService serviceWith(PaymentPort payment) {
        return new OrderService(repository, pricing, payment, new EventBus(), () -> 1234L);
    }

    @Test
    void placesAnOrderInPendingWithTheComputedTotal() {
        OrderService service = serviceWith(approving());
        Order order = service.place(List.of(new OrderItem("sku-a", 2), new OrderItem("sku-b", 1)));
        assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.total()).isEqualTo(Money.of(3000L, "USD"));
    }

    @Test
    void marksOrderPaidWhenAuthorizationApproves() {
        OrderService service = serviceWith(approving());
        Order placed = service.place(List.of(new OrderItem("sku-a", 1)));
        Order paid = service.pay(placed.id(), "4111111111111234", "key-1");
        assertThat(paid.status()).isEqualTo(OrderStatus.PAID);
        assertThat(paid.paymentId()).isEqualTo("pay-fake");
    }

    @Test
    void marksOrderDeclinedWhenAuthorizationDeclines() {
        OrderService service = serviceWith(declining());
        Order placed = service.place(List.of(new OrderItem("sku-a", 1)));
        Order declined = service.pay(placed.id(), "4111111111110000", "key-2");
        assertThat(declined.status()).isEqualTo(OrderStatus.DECLINED);
        assertThat(declined.paymentId()).isNull();
    }

    @Test
    void doesNotRechargeAnAlreadyPaidOrder() {
        CountingPayment payment = new CountingPayment();
        OrderService service = serviceWith(payment);
        Order placed = service.place(List.of(new OrderItem("sku-a", 1)));
        service.pay(placed.id(), "4111111111111234", "key-3");
        service.pay(placed.id(), "4111111111111234", "key-3");
        assertThat(payment.calls).isEqualTo(1);
    }

    @Test
    void rejectsPayingAnUnknownOrder() {
        OrderService service = serviceWith(approving());
        assertThatThrownBy(() -> service.pay("ord-missing", "4111111111111234", "key-4"))
            .isInstanceOf(ApiException.class)
            .satisfies(e -> assertThat(((ApiException) e).problem().status()).isEqualTo(404));
    }

    private static PaymentPort approving() {
        return (orderId, amount, pan, key) -> new PaymentPort.PaymentDecision(true, "pay-fake", "auth-0001");
    }

    private static PaymentPort declining() {
        return (orderId, amount, pan, key) ->
            new PaymentPort.PaymentDecision(false, "pay-fake", "insufficient funds");
    }

    private static final class CountingPayment implements PaymentPort {
        private int calls;

        @Override
        public PaymentDecision authorize(String orderId, Money amount, String pan, String idempotencyKey) {
            calls++;
            return new PaymentDecision(true, "pay-fake", "auth-0001");
        }
    }
}
