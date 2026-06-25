package org.acme.orders;

import java.lang.ref.Cleaner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A handle whose underlying resource must be released, using a {@link Cleaner} as a backstop (Item 8).
 *
 * <p>The primary release path is still {@link #close()} called deterministically (this type is
 * {@link AutoCloseable}, so it belongs in a try-with-resources header). The {@code Cleaner} registers a
 * cleaning action that runs only after the object becomes phantom-reachable — possibly long later, possibly
 * only at JVM exit — so it is a safety net for a {@code close()} that was forgotten, never the mechanism
 * relied on for prompt release. The cleaning action holds <em>no</em> reference back to this object (it
 * captures only the separate {@code State}), which is the contract that lets the object become reachable for
 * cleaning at all.
 *
 * <p>An {@code AtomicInteger} stands in for a native handle so a test can observe that the resource is
 * released exactly once whether {@code close()} runs or the cleaner fires.
 */
public final class NativeCounter implements AutoCloseable {

    private static final Cleaner CLEANER = Cleaner.create();

    private final State state;
    private final Cleaner.Cleanable cleanable;

    /**
     * Allocates the resource and registers its cleaning action.
     *
     * @param liveResources a shared counter incremented on allocation and decremented on release
     */
    // tag::cleaner-backstop[]
    public NativeCounter(AtomicInteger liveResources) {
        this.state = new State(liveResources);
        liveResources.incrementAndGet();
        // Backstop only: runs if close() is never called. The action captures State, not `this`.
        this.cleanable = CLEANER.register(this, state);
    }
    // end::cleaner-backstop[]

    /**
     * Releases the resource now (the primary, deterministic path).
     */
    @Override
    public void close() {
        cleanable.clean();   // runs State.run() at most once, even if the cleaner later fires too
    }

    /**
     * The releasable resource, kept separate from the owning object so the cleaning action cannot pin it.
     */
    private static final class State implements Runnable {

        private final AtomicInteger liveResources;
        private boolean released;

        State(AtomicInteger liveResources) {
            this.liveResources = liveResources;
        }

        @Override
        public void run() {
            if (!released) {
                released = true;
                liveResources.decrementAndGet();
            }
        }
    }
}
