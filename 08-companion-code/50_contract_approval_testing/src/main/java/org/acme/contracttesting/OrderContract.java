package org.acme.contracttesting;

import java.util.Objects;
import java.util.Set;

/**
 * The shared, executable contract for the orders boundary: the set of fields a consumer relies on the
 * provider's response to carry.
 *
 * <p>This is the consumer-driven idea in plain Java. The consumer declares what it actually reads (here,
 * the {@code id} and {@code status} fields), and both sides verify a response against the SAME object:
 * the consumer test proves its own expectation is well-formed, and the provider verification replays the
 * contract against a real provider response. Because the contract is exactly what the consumer uses,
 * provider fields no consumer reads can change freely; a field a consumer does read cannot disappear or
 * be renamed without {@link #verify(String)} failing. (Pact captures this with a broker and richer
 * type-matching; the README covers what that adds. The shape of the guarantee is the same.)
 */
public final class OrderContract {

    private final Set<String> requiredFields;

    /**
     * Creates a contract requiring the given response fields.
     *
     * @param requiredFields the field names a consumer relies on, never {@code null} or empty
     * @throws NullPointerException     if {@code requiredFields} is {@code null}
     * @throws IllegalArgumentException if {@code requiredFields} is empty
     */
    public OrderContract(Set<String> requiredFields) {
        Set<String> copy = Set.copyOf(Objects.requireNonNull(requiredFields, "requiredFields"));
        if (copy.isEmpty()) {
            throw new IllegalArgumentException("a contract must require at least one field");
        }
        this.requiredFields = copy;
    }

    /**
     * Verifies that a response satisfies the contract: every required field is present.
     *
     * @param responseBody the response to check against this contract, never {@code null}
     * @throws ContractViolationException if a required field is missing
     */
    // tag::contract-verify[]
    public void verify(String responseBody) {
        Objects.requireNonNull(responseBody, "responseBody");
        for (String field : requiredFields) {
            if (!responseBody.contains("\"" + field + "\":")) {
                throw new ContractViolationException(field, responseBody);
            }
        }
    }
    // end::contract-verify[]

    /**
     * Returns the fields this contract requires.
     *
     * @return an unmodifiable view of the required field names, never {@code null}
     */
    public Set<String> requiredFields() {
        return requiredFields;
    }
}
