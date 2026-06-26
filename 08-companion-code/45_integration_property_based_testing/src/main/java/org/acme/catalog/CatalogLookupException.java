package org.acme.catalog;

/**
 * Thrown by {@link CatalogClient} when a catalog lookup does not return a product: the SKU is unknown
 * (the server answered {@code 404}) or the server returned a non-success status.
 *
 * <p>The typed failure is the explicit failure path on the client side: the integration test drives a
 * lookup for an unstocked SKU and asserts this exception with its stable {@code status}, so the
 * not-found contract is exercised over real HTTP rather than assumed.
 */
public class CatalogLookupException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** The HTTP status the catalog server returned for the failed lookup. */
    private final int status;

    /**
     * Creates a lookup failure carrying the server's status and a human-readable message.
     *
     * @param status  the HTTP status returned by the catalog server
     * @param message the detail message
     */
    public CatalogLookupException(int status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * Returns the HTTP status the catalog server returned, so a caller can branch on it.
     *
     * @return the HTTP status code
     */
    public int status() {
        return status;
    }
}
