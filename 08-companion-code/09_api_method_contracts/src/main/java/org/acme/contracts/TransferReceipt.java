package org.acme.contracts;

import java.util.Objects;

/**
 * The immutable result of a completed transfer (Item 17). Returning a value object rather than a
 * status code or an out-parameter keeps the outcome self-describing and makes the return un-ignorable
 * in spirit: the caller holds the new balances and the moved amount in one place.
 *
 * @param fromAccountId the debited account's id
 * @param toAccountId   the credited account's id
 * @param amount        the amount moved
 */
public record TransferReceipt(String fromAccountId, String toAccountId, Money amount) {

    /**
     * Validates the receipt's components.
     *
     * @throws NullPointerException if any component is {@code null}
     */
    public TransferReceipt {
        Objects.requireNonNull(fromAccountId, "fromAccountId");
        Objects.requireNonNull(toAccountId, "toAccountId");
        Objects.requireNonNull(amount, "amount");
    }
}
