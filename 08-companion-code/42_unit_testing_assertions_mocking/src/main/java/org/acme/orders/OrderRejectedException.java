package org.acme.orders;

import java.util.Objects;

/**
 * Thrown when an order cannot be placed for a reason the caller is expected to branch on.
 *
 * <p>The exception carries a stable reason {@link #code()} so a caller switches on the reason rather
 * than parsing a human-readable message. This is the explicit failure path the chapter's
 * honest-limitations floor asks the module to demonstrate in code: a rejected order fails fast, with
 * a typed, testable outcome, before any payment is attempted.
 */
public final class OrderRejectedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Creates a rejection with a stable reason code and a human-readable detail.
     *
     * @param code   the stable reason code (for example {@code "unknown-sku"}), never {@code null}
     * @param detail a human-readable explanation, never {@code null}
     */
    public OrderRejectedException(String code, String detail) {
        super(Objects.requireNonNull(detail, "detail"));
        this.code = Objects.requireNonNull(code, "code");
    }

    /**
     * Returns the stable reason code a caller can branch on.
     *
     * @return the reason code, never {@code null}
     */
    public String code() {
        return code;
    }
}
