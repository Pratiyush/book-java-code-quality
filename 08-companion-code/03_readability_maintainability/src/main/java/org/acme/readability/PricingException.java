package org.acme.readability;

import java.io.Serial;
import java.util.Objects;

/**
 * The defined error response when a discount cannot be computed (a cap that is below zero, a cap below
 * the floor). It carries a stable machine-readable {@code code} alongside the human message, so a caller
 * — or an HTTP layer — branches on the reason rather than parsing a string.
 *
 * <p>This is the module's explicit failure path: a broken precondition becomes a typed, documented
 * exception rather than a silent wrong result, which is the HONEST-LIMITATIONS floor expressed in a code
 * path a test actually drives. All three forms of the rule share it, so the three are proven to preserve
 * the failure behaviour as well as the happy path.
 */
public final class PricingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Creates a pricing failure with a stable reason code and a human-readable detail.
     *
     * @param code   a stable, machine-readable reason code, never {@code null}
     * @param detail a human-readable explanation, never {@code null}
     * @throws NullPointerException if {@code code} or {@code detail} is {@code null}
     */
    public PricingException(String code, String detail) {
        super(Objects.requireNonNull(detail, "detail"));
        this.code = Objects.requireNonNull(code, "code");
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
