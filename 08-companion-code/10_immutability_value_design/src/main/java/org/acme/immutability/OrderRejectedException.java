package org.acme.immutability;

import java.io.Serial;
import java.util.Objects;

/**
 * The defined error response when an order cannot be accepted (an empty order, a currency mismatch).
 * It carries a stable machine-readable {@code code} alongside the human message, so a caller — or an
 * HTTP layer — branches on the reason rather than parsing a string.
 *
 * <p>This is the module's explicit failure path: a broken precondition becomes a typed, documented
 * exception rather than a silent wrong result, which is the HONEST-LIMITATIONS floor expressed in a
 * code path a test actually drives.
 */
public final class OrderRejectedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Creates a rejection with a stable reason code and a human-readable detail.
     *
     * @param code   a stable, machine-readable reason code, never {@code null}
     * @param detail a human-readable explanation, never {@code null}
     * @throws NullPointerException if {@code code} or {@code detail} is {@code null}
     */
    public OrderRejectedException(String code, String detail) {
        super(Objects.requireNonNull(detail, "detail"));
        this.code = Objects.requireNonNull(code, "code");
    }

    /**
     * Returns the stable reason code for this rejection.
     *
     * @return the reason code, never {@code null}
     */
    public String code() {
        return code;
    }
}
