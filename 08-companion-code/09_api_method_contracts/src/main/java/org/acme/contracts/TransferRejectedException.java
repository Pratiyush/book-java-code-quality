package org.acme.contracts;

import java.io.Serial;
import java.util.Objects;

/**
 * The defined error response when a transfer cannot be honoured (an unknown account, an insufficient
 * balance). It carries a stable machine-readable {@code code} alongside the human message, so a
 * caller — or an HTTP layer — can branch on the reason rather than parse a string. This is the
 * chapter's explicit failure path: a broken contract becomes a typed, documented exception rather
 * than a silent wrong result.
 */
public final class TransferRejectedException extends RuntimeException {

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
    public TransferRejectedException(String code, String detail) {
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
