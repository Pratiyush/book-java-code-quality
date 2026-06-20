package org.acme.storefront.payment;

import java.util.Objects;

/**
 * A request to authorize a payment.
 *
 * @param pan the primary account number (card number); must not be blank
 * @param amountMinor the amount to authorize, in minor units; must be {@code >= 0}
 * @param currency the three-letter currency code; must not be blank
 * @param idempotencyKey a caller-supplied key that makes a retry safe: the same key always yields
 *     the same outcome (must not be blank)
 */
public record PaymentRequest(String pan, long amountMinor, String currency, String idempotencyKey) {

    /** Validates the components (fail-fast, Chapter 10). */
    public PaymentRequest {
        Objects.requireNonNull(pan, "pan");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        if (pan.isBlank()) {
            throw new IllegalArgumentException("pan must not be blank");
        }
        if (currency.isBlank()) {
            throw new IllegalArgumentException("currency must not be blank");
        }
        if (idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("idempotencyKey must not be blank");
        }
        if (amountMinor < 0) {
            throw new IllegalArgumentException("amountMinor must be >= 0: " + amountMinor);
        }
    }
}
