package org.acme.ai;

/**
 * Signals that an AI-generated contribution was turned away by the {@link AiReviewGate} before it could
 * be accepted, carrying a stable reason code rather than a free-text message a caller would have to
 * parse. The codes are part of the gate's contract: {@code body-too-large}, {@code malformed-body}, and
 * {@code provenance-missing}.
 *
 * <p>Rejecting a contribution the gate cannot make safe is the chapter's stance made concrete: an AI
 * draft is untrusted until verified, and what cannot be verified is refused rather than merged.
 */
public final class RejectedContributionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Creates a rejection with a stable reason code.
     *
     * @param code the stable reason code, never {@code null}
     */
    public RejectedContributionException(String code) {
        super("contribution rejected: " + code);
        this.code = code;
    }

    /**
     * Creates a rejection with a stable reason code and an underlying cause.
     *
     * @param code  the stable reason code, never {@code null}
     * @param cause the underlying cause
     */
    public RejectedContributionException(String code, Throwable cause) {
        super("contribution rejected: " + code, cause);
        this.code = code;
    }

    /**
     * Returns the stable reason code for this rejection.
     *
     * @return the reason code
     */
    public String code() {
        return code;
    }
}
