package org.acme.storefront.checkout;

import java.io.Serial;

/**
 * Thrown when a cart references a product id the catalog does not know.
 *
 * <p>It extends {@link IllegalArgumentException} because an unknown product is a bad <em>argument</em>
 * from the caller — a client error, mapped by the API to a 4xx response (Chapter 10: throw an
 * exception appropriate to the abstraction).
 */
public class UnknownProductException extends IllegalArgumentException {

    @Serial private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for a specific product id.
     *
     * @param productId the unknown product id
     */
    public UnknownProductException(String productId) {
        super("unknown product: " + productId);
    }
}
