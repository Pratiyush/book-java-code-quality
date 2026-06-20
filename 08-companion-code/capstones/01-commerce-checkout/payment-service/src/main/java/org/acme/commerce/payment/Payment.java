package org.acme.commerce.payment;

import java.util.LinkedHashMap;
import java.util.Map;
import org.acme.platform.money.Money;

/**
 * The record of an authorization attempt (Part: value modelling). It is the stored, replayable
 * result of {@link PaymentService#authorize}: a later retry with the same idempotency key returns
 * this same record rather than charging again.
 *
 * @param id      the payment identifier
 * @param orderId the order this payment is for
 * @param amount  the amount authorized
 * @param status  approved or declined
 * @param detail  an auth code when approved, or a decline reason when declined
 */
public record Payment(String id, String orderId, Money amount, PaymentStatus status, String detail) {

    public boolean isApproved() {
        return status == PaymentStatus.APPROVED;
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("orderId", orderId);
        body.put("amountMinor", amount.minorUnits());
        body.put("currency", amount.currency());
        body.put("status", status.name());
        body.put("detail", detail);
        return body;
    }
}
