package org.acme.security;

/**
 * Thrown by {@link SecurityGate} when an untrusted request is turned away. It carries a stable
 * {@code code} so a caller branches on the reason rather than parsing a message — the explicit error
 * contract the chapter's failure-path floor calls for.
 */
public final class RejectedRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Creates a rejection with a stable reason code.
     *
     * @param code the stable reason code, for example {@code "malformed-body"}
     */
    public RejectedRequestException(String code) {
        super(code);
        this.code = code;
    }

    /**
     * Creates a rejection with a stable reason code and an underlying cause.
     *
     * @param code  the stable reason code
     * @param cause the underlying cause
     */
    public RejectedRequestException(String code, Throwable cause) {
        super(code, cause);
        this.code = code;
    }

    /**
     * Returns the stable reason code a caller can branch on.
     *
     * @return the reason code
     */
    public String code() {
        return code;
    }
}
