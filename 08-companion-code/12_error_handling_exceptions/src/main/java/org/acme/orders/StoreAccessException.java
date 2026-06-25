package org.acme.orders;

import java.io.Serial;

/**
 * A low-level storage failure raised by the persistence adapter (standing in for the checked exceptions a
 * real datastore client throws — a {@code SQLException}, an {@code IOException}). It is deliberately
 * technology-flavoured: the order layer should not let this type leak to its callers, but should translate
 * it into an abstraction-appropriate {@link OrderUnavailableException} (Item 73).
 */
public final class StoreAccessException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates the low-level failure with a message.
     *
     * @param message a human-readable explanation of the storage failure
     */
    public StoreAccessException(String message) {
        super(message);
    }
}
