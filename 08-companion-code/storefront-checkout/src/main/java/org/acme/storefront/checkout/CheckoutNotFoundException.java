package org.acme.storefront.checkout;

import java.io.Serial;

/**
 * Thrown when an operation references a checkout token that does not exist.
 *
 * <p>Mapped by the API to a 404 response.
 */
public class CheckoutNotFoundException extends RuntimeException {

    @Serial private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a specific token.
     *
     * @param token the unknown checkout token
     */
    public CheckoutNotFoundException(String token) {
        super("unknown checkout: " + token);
    }
}
