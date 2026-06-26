package org.acme.contracttesting;

import java.io.Serial;
import java.util.Objects;

/**
 * Raised when a provider response does not satisfy a consumer-driven {@link OrderContract} — a required
 * field the consumer reads is missing from the response.
 *
 * <p>This is the failure the contract exists to surface: it is what a green consumer suite cannot see on
 * its own and what provider verification catches. It names the missing field so the diagnosis points
 * straight at the breaking change (for example a field renamed from {@code id} to {@code orderId}).
 */
public final class ContractViolationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String missingField;

    /**
     * Creates a violation for a missing required field.
     *
     * @param missingField the contracted field absent from the response, never {@code null}
     * @param responseBody the response that failed the contract, never {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
    public ContractViolationException(String missingField, String responseBody) {
        super("contract violated: response is missing required field \""
            + Objects.requireNonNull(missingField, "missingField") + "\": "
            + Objects.requireNonNull(responseBody, "responseBody"));
        this.missingField = missingField;
    }

    /**
     * Returns the name of the contracted field that was missing.
     *
     * @return the missing field name, never {@code null}
     */
    public String missingField() {
        return missingField;
    }
}
