package org.acme.storefront.domain;

import java.util.Objects;

/**
 * The outcome of a single payment authorization, modelled as a closed set of cases.
 *
 * <p>This is a sealed-interface "result type" (Chapter 10): a payment either {@link Approved} with
 * an authorization code, or {@link Declined} with a reason. Because the hierarchy is sealed, callers
 * can switch over it exhaustively and the compiler rejects a {@code switch} that misses a case.
 */
public sealed interface PaymentResult permits PaymentResult.Approved, PaymentResult.Declined {

    /**
     * A successful authorization.
     *
     * @param authCode the gateway authorization code
     */
    record Approved(String authCode) implements PaymentResult {
        /** Validates the component. */
        public Approved {
            Objects.requireNonNull(authCode, "authCode");
        }
    }

    /**
     * A declined authorization.
     *
     * @param reason a human-readable decline reason
     */
    record Declined(String reason) implements PaymentResult {
        /** Validates the component. */
        public Declined {
            Objects.requireNonNull(reason, "reason");
        }
    }
}
