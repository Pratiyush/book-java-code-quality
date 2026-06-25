package org.acme.orders;

import java.io.Serial;

/**
 * A recoverable failure: the order could not be read because the underlying store was temporarily
 * unavailable. It is a <em>checked</em> exception (it extends {@link Exception}, not
 * {@link RuntimeException}) because a caller can reasonably be expected to recover — retry, fall back to a
 * cache, or report a transient error (Item 70: "use checked exceptions for conditions from which the caller
 * can reasonably be expected to recover").
 *
 * <p>It is the abstraction-appropriate exception of the order layer (Item 73): the repository translates a
 * low-level store failure into this type and chains the original as the cause, so the order layer's callers
 * never see the storage technology, yet the root cause stays attached for diagnosis.
 */
public final class OrderUnavailableException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a message and the low-level cause it translates.
     *
     * @param message a human-readable explanation
     * @param cause   the underlying failure being translated, preserved for diagnosis
     */
    public OrderUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
