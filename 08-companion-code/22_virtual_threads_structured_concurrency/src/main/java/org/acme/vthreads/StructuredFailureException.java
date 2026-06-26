package org.acme.vthreads;

import java.io.Serial;
import java.util.Objects;

/**
 * The defined error response when a unit of structured work fails — a subtask threw, or the unit was
 * interrupted. It carries a stable, machine-readable {@code code} beside the cause so a caller branches
 * on the reason rather than parsing a message. This is the structured-concept demo's explicit failure
 * path: one failing fork fails the whole unit, rather than leaving a sibling running unobserved.
 */
public final class StructuredFailureException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Creates a structured-failure with a stable reason code and the underlying cause.
     *
     * @param code  a stable, machine-readable reason code, never {@code null}
     * @param cause the underlying cause, may be {@code null}
     * @throws NullPointerException if {@code code} is {@code null}
     */
    public StructuredFailureException(String code, Throwable cause) {
        super(Objects.requireNonNull(code, "code"), cause);
        this.code = code;
    }

    /**
     * Returns the stable reason code for this failure.
     *
     * @return the reason code, never {@code null}
     */
    public String code() {
        return code;
    }
}
