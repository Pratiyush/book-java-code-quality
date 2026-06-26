package org.acme.vthreads;

import java.util.Objects;

/**
 * The immutable outcome of fetching one target in the fan-out.
 *
 * <p>An immutable record (Item 17): once constructed it never changes, so it is automatically safe to
 * publish across the many virtual threads of a fan-out without a lock or a defensive copy. Holding the
 * target id beside its payload keeps a caller from pairing the wrong result with the wrong request.
 *
 * @param target  the target this result is for, never {@code null}
 * @param payload the value fetched for that target, never {@code null}
 * @param millis  how long the fetch took, in milliseconds, never negative
 */
public record FetchResult(String target, String payload, long millis) {

    /**
     * Validates the components so an invalid result can never exist.
     *
     * @throws NullPointerException     if {@code target} or {@code payload} is {@code null}
     * @throws IllegalArgumentException if {@code millis} is negative
     */
    public FetchResult {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(payload, "payload");
        if (millis < 0) {
            throw new IllegalArgumentException("millis must not be negative: " + millis);
        }
    }
}
