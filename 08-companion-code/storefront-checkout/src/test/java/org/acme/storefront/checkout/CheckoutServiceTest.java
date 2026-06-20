package org.acme.storefront.checkout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.acme.storefront.domain.CartItem;
import org.acme.storefront.domain.Checkout;
import org.acme.storefront.domain.CheckoutStatus;
import org.acme.storefront.payment.PaymentRequest;
import org.acme.storefront.payment.PaymentSimulator;
import org.junit.jupiter.api.Test;

class CheckoutServiceTest {

    private static final String VALID_CARD = "4242424242424242";
    private static final String DECLINE_CARD = "4242424242420000";
    private static final Instant T0 = Instant.parse("2026-06-20T10:00:00Z");
    private static final Duration TTL = Duration.ofMinutes(15);

    // shared collaborators so two services (at different clocks) act on the same store
    private final Catalog catalog = Catalog.withSampleData();
    private final CheckoutRepository repository = new CheckoutRepository();
    private final TokenGenerator tokens = new TokenGenerator();
    private final PaymentSimulator payments = new PaymentSimulator(1_000_000L);
    private final CheckoutConfig config =
            new CheckoutConfig(TTL, "https://shop.test", "USD", 1_000_000L);

    private CheckoutService serviceAt(Instant now) {
        return new CheckoutService(
                catalog, repository, tokens, payments, config, Clock.fixed(now, ZoneOffset.UTC));
    }

    private static List<CartItem> twoBooks() {
        return List.of(new CartItem("BOOK-EJ", 2)); // 4500 * 2 = 9000
    }

    @Test
    void createComputesTotalAndIsPending() {
        Checkout checkout = serviceAt(T0).createCheckout(twoBooks());
        assertThat(checkout.total().amountMinor()).isEqualTo(9_000L);
        assertThat(checkout.status()).isEqualTo(CheckoutStatus.PENDING);
        assertThat(checkout.expiresAt()).isEqualTo(T0.plus(TTL));
    }

    @Test
    void unknownProductIsRejected() {
        assertThatThrownBy(() -> serviceAt(T0).createCheckout(List.of(new CartItem("NOPE", 1))))
                .isInstanceOf(UnknownProductException.class);
    }

    @Test
    void resolveReturnsPendingWithinTtl() {
        Checkout created = serviceAt(T0).createCheckout(twoBooks());
        Checkout resolved = serviceAt(T0.plusSeconds(60)).resolve(created.token()).orElseThrow();
        assertThat(resolved.status()).isEqualTo(CheckoutStatus.PENDING);
    }

    @Test
    void resolveFlipsToExpiredAfterTtl() {
        Checkout created = serviceAt(T0).createCheckout(twoBooks());
        Checkout resolved =
                serviceAt(T0.plus(TTL).plusSeconds(1)).resolve(created.token()).orElseThrow();
        assertThat(resolved.status()).isEqualTo(CheckoutStatus.EXPIRED);
    }

    @Test
    void resolveUnknownTokenIsEmpty() {
        assertThat(serviceAt(T0).resolve("does-not-exist")).isEmpty();
    }

    @Test
    void payApprovesAndMarksPaid() {
        CheckoutService service = serviceAt(T0);
        Checkout created = service.createCheckout(twoBooks());
        PaymentOutcome outcome =
                service.pay(created.token(), new PaymentRequest(VALID_CARD, 9_000L, "USD", "pay-1"));
        assertThat(outcome).isInstanceOf(PaymentOutcome.Approved.class);
        PaymentOutcome.Approved approved = (PaymentOutcome.Approved) outcome;
        assertThat(approved.checkout().status()).isEqualTo(CheckoutStatus.PAID);
        assertThat(approved.authCode()).startsWith("AUTH-");
    }

    @Test
    void payDeclinesOnAmountMismatch() {
        CheckoutService service = serviceAt(T0);
        Checkout created = service.createCheckout(twoBooks());
        PaymentOutcome outcome =
                service.pay(created.token(), new PaymentRequest(VALID_CARD, 1L, "USD", "pay-mismatch"));
        assertThat(outcome).isInstanceOf(PaymentOutcome.Declined.class);
    }

    @Test
    void payDeclinesOnTestCard() {
        CheckoutService service = serviceAt(T0);
        Checkout created = service.createCheckout(twoBooks());
        PaymentOutcome outcome =
                service.pay(
                        created.token(), new PaymentRequest(DECLINE_CARD, 9_000L, "USD", "pay-decline"));
        assertThat(outcome)
                .isInstanceOfSatisfying(
                        PaymentOutcome.Declined.class,
                        d -> assertThat(d.reason()).contains("always declines"));
    }

    @Test
    void payOnExpiredCheckoutIsExpired() {
        Checkout created = serviceAt(T0).createCheckout(twoBooks());
        PaymentOutcome outcome =
                serviceAt(T0.plus(TTL).plusSeconds(1))
                        .pay(created.token(), new PaymentRequest(VALID_CARD, 9_000L, "USD", "pay-late"));
        assertThat(outcome).isInstanceOf(PaymentOutcome.Expired.class);
    }

    @Test
    void payTwiceIsAlreadyPaid() {
        CheckoutService service = serviceAt(T0);
        Checkout created = service.createCheckout(twoBooks());
        service.pay(created.token(), new PaymentRequest(VALID_CARD, 9_000L, "USD", "pay-once"));
        PaymentOutcome second =
                service.pay(created.token(), new PaymentRequest(VALID_CARD, 9_000L, "USD", "pay-twice"));
        assertThat(second).isInstanceOf(PaymentOutcome.AlreadyPaid.class);
    }

    @Test
    void payUnknownTokenThrows() {
        assertThatThrownBy(
                        () ->
                                serviceAt(T0)
                                        .pay("nope", new PaymentRequest(VALID_CARD, 1L, "USD", "k")))
                .isInstanceOf(CheckoutNotFoundException.class);
    }
}
