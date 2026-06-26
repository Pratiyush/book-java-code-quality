package org.acme.orders;

import java.util.Objects;

/**
 * Thrown by a {@link PaymentGateway} when it declines a charge.
 *
 * <p>The chapter uses this to show a command port being stubbed to <em>fail</em>: a test programs
 * {@code doThrow(new PaymentDeclinedException(...)).when(gateway).charge(...)} and asserts the unit's
 * behaviour on the failure, rather than only on the happy path. Modelling the decline as a typed
 * exception keeps that test asserting a real contract instead of a parsed string.
 */
public final class PaymentDeclinedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a decline with a human-readable reason.
     *
     * @param reason why the charge was declined, never {@code null}
     */
    public PaymentDeclinedException(String reason) {
        super(Objects.requireNonNull(reason, "reason"));
    }
}
