package org.acme.design.direction;

/**
 * Signals that a proposed dependency points the wrong way — from a more stable unit toward a less
 * stable one — violating the Stable Dependencies Principle the chapter describes.
 *
 * <p>The exception carries a stable reason code so a caller branches on the reason rather than parsing
 * a message, the same error-contract shape the rest of the companion code uses.
 */
public final class UnstableDependencyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Creates the exception with a stable reason code and a human-readable detail.
     *
     * @param code   the stable reason code, never {@code null}
     * @param detail the human-readable detail, never {@code null}
     */
    public UnstableDependencyException(String code, String detail) {
        super(detail);
        this.code = code;
    }

    /**
     * Returns the stable reason code.
     *
     * @return the reason code, never {@code null}
     */
    public String code() {
        return code;
    }
}
