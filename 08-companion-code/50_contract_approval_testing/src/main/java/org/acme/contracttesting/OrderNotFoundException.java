package org.acme.contracttesting;

import java.io.Serial;
import java.util.Objects;

/**
 * The defined error response when the provider is asked for an order it does not have.
 *
 * <p>It carries a stable machine-readable {@code code} alongside the human message, so a caller — or an
 * HTTP layer — can branch on the reason and map it to a status (a 404) rather than parse a string. This
 * is the module's explicit failure path: an unknown id becomes a typed, documented exception rather than
 * a {@code null} that surfaces as a confusing error further on.
 */
public final class OrderNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /** The stable reason code carried by every instance of this exception. */
    public static final String CODE = "order-not-found";

    /**
     * Creates a not-found rejection for the given order id.
     *
     * @param id the order id that was not found, never {@code null}
     * @throws NullPointerException if {@code id} is {@code null}
     */
    public OrderNotFoundException(String id) {
        super("no order " + Objects.requireNonNull(id, "id"));
    }

    /**
     * Returns the stable reason code for this rejection.
     *
     * @return the reason code, never {@code null}
     */
    public String code() {
        return CODE;
    }
}
