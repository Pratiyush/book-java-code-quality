package org.acme.observability;

/**
 * The typed result of a checkout attempt — an explicit failure path rather than an exception thrown
 * into the void or a {@code null} return. A caller branches on the variant; a failure carries a
 * stable reason code so the branch is on the reason, not on a parsed message.
 *
 * <p>This is the error response the observability surfaces describe: a {@link Failure} is what the
 * error counter counts and what the structured logger records at {@code ERROR}, and it is what a
 * production error tracker would capture with its context (Chapter 36's fix-test-gate loop).
 */
public sealed interface CheckoutOutcome permits CheckoutOutcome.Success, CheckoutOutcome.Failure {

    /** A completed checkout, identified by its order reference. */
    record Success(String orderRef, long amountMinorUnits) implements CheckoutOutcome { }

    /** A rejected checkout, carrying a stable reason code for the caller to branch on. */
    record Failure(String reasonCode, String detail) implements CheckoutOutcome { }
}
