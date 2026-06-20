package org.acme.storefront.checkout;

import java.util.Objects;
import org.acme.storefront.domain.Checkout;

/**
 * The business outcome of attempting to pay for a checkout, as a closed set of cases (Chapter 10).
 *
 * <p>Distinct from a raw {@link org.acme.storefront.domain.PaymentResult}: it folds in the checkout's
 * own state, so the API can map each case to a precise HTTP status. A caller switches over it
 * exhaustively.
 */
public sealed interface PaymentOutcome
        permits PaymentOutcome.Approved,
                PaymentOutcome.Declined,
                PaymentOutcome.Expired,
                PaymentOutcome.AlreadyPaid {

    /**
     * Payment authorized and the checkout marked paid.
     *
     * @param checkout the now-paid checkout
     * @param authCode the gateway authorization code
     */
    record Approved(Checkout checkout, String authCode) implements PaymentOutcome {
        /** Validates the components. */
        public Approved {
            Objects.requireNonNull(checkout, "checkout");
            Objects.requireNonNull(authCode, "authCode");
        }
    }

    /**
     * The gateway declined the payment.
     *
     * @param reason the decline reason
     */
    record Declined(String reason) implements PaymentOutcome {
        /** Validates the component. */
        public Declined {
            Objects.requireNonNull(reason, "reason");
        }
    }

    /** The checkout link had already expired (maps to 410 Gone). */
    record Expired() implements PaymentOutcome {}

    /** The checkout had already been paid (maps to 409 Conflict). */
    record AlreadyPaid() implements PaymentOutcome {}
}
