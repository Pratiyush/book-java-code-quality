package org.acme.commerce.order;

import org.acme.platform.money.Money;

/**
 * The outbound PORT for authorizing payment (Part: architecture). Wired in production to
 * {@link PaymentGatewayClient}; faked in tests.
 */
public interface PaymentPort {

    PaymentDecision authorize(String orderId, Money amount, String pan, String idempotencyKey);

    /**
     * The outcome of an authorization as order-service sees it.
     *
     * @param approved  whether the charge succeeded
     * @param paymentId the payment id (present on both approve and decline)
     * @param detail    the auth code or the decline reason
     */
    record PaymentDecision(boolean approved, String paymentId, String detail) {
    }
}
