package org.acme.contracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A batch of transfer instructions submitted together.
 *
 * <p>Demonstrates Item 50 ("make defensive copies when needed"): the constructor copies the incoming
 * list and the getter returns a copy, so a caller holding the original list — or the returned one —
 * cannot reach in and mutate the batch's internal state afterwards. Skipping either copy is exactly
 * what SpotBugs reports as {@code EI_EXPOSE_REP2} (storing an external mutable object) and
 * {@code EI_EXPOSE_REP} (returning an internal mutable field). When a stored type is already
 * immutable, these copies would be pure overhead and immutability is the better tool; here the
 * element type is a mutable {@link List}, so the copies earn their cost.
 */
public final class TransferBatch {

    private final List<TransferInstruction> instructions;

    /**
     * Creates a batch, copying the caller's list so later edits to it cannot change this batch.
     *
     * @param instructions the instructions to run together, never {@code null}
     * @throws NullPointerException if the list or any element is {@code null}
     */
    // tag::defensive-copy[]
    public TransferBatch(List<TransferInstruction> instructions) {
        this.instructions = new ArrayList<>(Objects.requireNonNull(instructions, "instructions"));
        this.instructions.forEach(i -> Objects.requireNonNull(i, "instruction"));
    }

    public List<TransferInstruction> instructions() {
        return new ArrayList<>(instructions); // copy out, so callers cannot mutate internal state
    }
    // end::defensive-copy[]

    /**
     * A single instruction within a batch.
     *
     * @param fromAccountId the account to debit
     * @param toAccountId   the account to credit
     * @param amount        the amount to move, in minor units
     */
    public record TransferInstruction(String fromAccountId, String toAccountId, long amount) {

        /**
         * Validates the instruction's identifiers.
         *
         * @throws NullPointerException if either id is {@code null}
         */
        public TransferInstruction {
            Objects.requireNonNull(fromAccountId, "fromAccountId");
            Objects.requireNonNull(toAccountId, "toAccountId");
        }
    }
}
