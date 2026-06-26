package org.acme.concurrency;

/**
 * An expensive singleton built lazily and safely with the initialization-on-demand holder idiom —
 * the recommended alternative to hand-rolled double-checked locking.
 *
 * <p>Double-checked locking is a correctness trap: without a {@code volatile} field a reader can see
 * a partially-constructed object, and even the fixed form draws conflicting static-analysis advice.
 * The holder idiom sidesteps the problem entirely. The nested {@code Holder} class is not loaded
 * until {@link #instance()} first refers to {@code Holder.INSTANCE}; the JVM's class-initialization
 * locking then publishes the {@code static final} field exactly once, fully constructed, with no
 * explicit lock and no {@code volatile}. Laziness and thread-safe publication both fall out of a
 * language guarantee rather than a hand-written check.
 */
public final class LazyResource {

    private final String payload;

    private LazyResource() {
        this.payload = "expensive-resource"; // stands in for a costly one-time initialization
    }

    // tag::lazy-holder[]
    private static final class Holder {
        private static final LazyResource INSTANCE = new LazyResource();
    }

    public static LazyResource instance() {
        return Holder.INSTANCE; // class-init locking publishes INSTANCE once, lazily and safely
    }
    // end::lazy-holder[]

    /**
     * Returns the resource payload.
     *
     * @return the lazily-initialized payload, never {@code null}
     */
    public String payload() {
        return payload;
    }
}
