package org.acme.storefront.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.acme.storefront.domain.PaymentResult;
import org.junit.jupiter.api.Test;

class PaymentSimulatorTest {

    private static final String VALID_CARD = "4242424242424242";
    private static final String INVALID_CHECKSUM_CARD = "4242424242424241";
    private static final String ALWAYS_DECLINE_CARD = "4242424242420000";

    private final PaymentSimulator simulator = new PaymentSimulator(1_000_000L);

    private static PaymentRequest request(String pan, long amountMinor, String key) {
        return new PaymentRequest(pan, amountMinor, "USD", key);
    }

    @Test
    void approvesValidCardWithinCeiling() {
        PaymentResult result = simulator.authorize(request(VALID_CARD, 5_000L, "k-approve"));
        assertThat(result).isInstanceOf(PaymentResult.Approved.class);
        assertThat(((PaymentResult.Approved) result).authCode()).startsWith("AUTH-");
    }

    @Test
    void declinesAmountOverCeiling() {
        PaymentResult result = simulator.authorize(request(VALID_CARD, 1_000_001L, "k-ceiling"));
        assertThat(result)
                .isInstanceOfSatisfying(
                        PaymentResult.Declined.class,
                        d -> assertThat(d.reason()).contains("ceiling"));
    }

    @Test
    void declinesCardFailingChecksum() {
        PaymentResult result = simulator.authorize(request(INVALID_CHECKSUM_CARD, 5_000L, "k-bad"));
        assertThat(result)
                .isInstanceOfSatisfying(
                        PaymentResult.Declined.class,
                        d -> assertThat(d.reason()).contains("checksum"));
    }

    @Test
    void declinesReservedTestCard() {
        PaymentResult result = simulator.authorize(request(ALWAYS_DECLINE_CARD, 5_000L, "k-test"));
        assertThat(result)
                .isInstanceOfSatisfying(
                        PaymentResult.Declined.class,
                        d -> assertThat(d.reason()).contains("always declines"));
    }

    @Test
    void authorizationIsIdempotentByKey() {
        PaymentResult first = simulator.authorize(request(VALID_CARD, 5_000L, "k-same"));
        // a retry with the SAME key but different amount must return the first decision unchanged
        PaymentResult retry = simulator.authorize(request(VALID_CARD, 9_999L, "k-same"));
        assertThat(retry).isEqualTo(first);
    }

    @Test
    void luhnAcceptsValidAndRejectsInvalid() {
        assertThat(PaymentSimulator.isLuhnValid(VALID_CARD)).isTrue();
        assertThat(PaymentSimulator.isLuhnValid(INVALID_CHECKSUM_CARD)).isFalse();
        assertThat(PaymentSimulator.isLuhnValid("not-a-number")).isFalse();
    }
}
