package org.acme.sast;

/**
 * Thrown when a required credential is not present in externalized configuration. The resolver fails
 * closed on this rather than falling back to a default or an empty value: a missing secret is an
 * operator error to surface, not a condition to paper over with a hardcoded fallback — a hardcoded
 * fallback is exactly the leaked-credential class this chapter exists to detect.
 */
final class MissingSecretException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    MissingSecretException(String message) {
        super(message);
    }
}
