package org.acme.contracts;

import java.util.Objects;

/**
 * An account holder and its current balance — an immutable value (Item 17).
 *
 * <p>A new balance is produced by returning a fresh {@code Account} rather than mutating this one, so
 * a reference handed to a caller can never be changed underneath them. The identifier and balance are
 * validated at construction, which is the only place an {@code Account} comes into being.
 *
 * @param id      the stable account identifier, never {@code null}
 * @param balance the current balance, never {@code null}
 */
public record Account(String id, Money balance) {

    /**
     * Validates the components so an {@code Account} is well-formed for its whole lifetime.
     *
     * @throws NullPointerException if {@code id} or {@code balance} is {@code null}
     */
    public Account {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(balance, "balance");
    }

    /**
     * Returns a copy of this account with {@code amount} (in minor units) debited from the balance.
     *
     * @param amount the amount to debit, in minor units, never negative
     * @return a new {@code Account} with the reduced balance
     * @throws IllegalArgumentException if {@code amount} is negative or exceeds the balance
     */
    public Account debit(long amount) {
        return new Account(id, balance.minus(amount));
    }

    /**
     * Returns a copy of this account with {@code amount} (in minor units) credited to the balance.
     *
     * @param amount the amount to credit, in minor units, never negative
     * @return a new {@code Account} with the increased balance
     * @throws IllegalArgumentException if {@code amount} is negative
     */
    public Account credit(long amount) {
        return new Account(id, balance.plus(amount));
    }
}
