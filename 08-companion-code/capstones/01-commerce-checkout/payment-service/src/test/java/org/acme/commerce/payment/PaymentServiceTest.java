package org.acme.commerce.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.acme.platform.money.Money;
import org.junit.jupiter.api.Test;

class PaymentServiceTest {

    private final PaymentService service = new PaymentService(new InMemoryPaymentRepository());

    @Test
    void approvesAnOrdinaryCard() {
        Payment payment = service.authorize(
            new PaymentRequest("ord-1", Money.of(7999, "USD"), "4111111111111234", "key-1"));
        assertThat(payment.isApproved()).isTrue();
        assertThat(payment.detail()).startsWith("auth-");
    }

    @Test
    void declinesACardEndingInTheDeclineSuffix() {
        Payment payment = service.authorize(
            new PaymentRequest("ord-2", Money.of(7999, "USD"), "4111111111110000", "key-2"));
        assertThat(payment.status()).isEqualTo(PaymentStatus.DECLINED);
        assertThat(payment.detail()).isEqualTo("insufficient funds");
    }

    @Test
    void replaysTheFirstResultForaRepeatedIdempotencyKey() {
        PaymentRequest first =
            new PaymentRequest("ord-3", Money.of(500, "USD"), "4111111111111234", "key-3");
        PaymentRequest retry =
            new PaymentRequest("ord-3", Money.of(500, "USD"), "4111111111111234", "key-3");

        Payment original = service.authorize(first);
        Payment replayed = service.authorize(retry);

        assertThat(replayed).isEqualTo(original);
        assertThat(replayed.id()).isEqualTo(original.id());
    }
}
