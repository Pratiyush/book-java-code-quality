package org.acme.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Deterministic resource cleanup with try-with-resources (JLS SE 21 §14.20.3 / Item 9).
 *
 * <p>The garbage collector reclaims memory but does not promptly close handles, so a failing path still has
 * to release what it opened. Try-with-resources gives three guarantees the old {@code try}/{@code finally}
 * idiom did not: resources close in reverse order of opening (LIFO); {@code close()} runs on every path; and
 * if the body throws and a {@code close()} then also throws, the body's exception propagates and the close
 * exception is <em>suppressed</em> (attached via {@link Throwable#addSuppressed}, read via
 * {@link Throwable#getSuppressed}) rather than masking the real failure.
 *
 * <p>The two {@link Channel} resources here record their close order into a shared log, so a test can assert
 * the reverse-order and suppressed-exception semantics rather than merely trust them.
 */
public final class ReceiptWriter {

    private ReceiptWriter() {
    }

    /**
     * Writes a receipt line across two channels opened in order, closed in reverse.
     *
     * @param closeLog records each channel's name as it closes, never {@code null}
     * @param line     the receipt line to write, never {@code null}
     * @throws NullPointerException if {@code closeLog} or {@code line} is {@code null}
     */
    public static void write(List<String> closeLog, String line) {
        Objects.requireNonNull(closeLog, "closeLog");
        Objects.requireNonNull(line, "line");
        // tag::twr-basic[]
        try (Channel first = new Channel("first", closeLog, false);
             Channel second = new Channel("second", closeLog, false)) {
            first.send(line);
            second.send(line);
        } // close() runs on second, then first — reverse order — on every path
        // end::twr-basic[]
    }

    /**
     * Writes a receipt where the body fails and a {@code close()} also fails, so the body's exception
     * propagates and the close exception is collected as suppressed.
     *
     * @param closeLog records each channel's name as it closes, never {@code null}
     * @return the suppressed close exceptions attached to the propagating body failure
     */
    public static List<Throwable> writeWithFailingBodyAndClose(List<String> closeLog) {
        Objects.requireNonNull(closeLog, "closeLog");
        try {
            // tag::suppressed[]
            try (Channel channel = new Channel("only", closeLog, true)) {  // its close() will throw
                channel.send("receipt");
                throw new IllegalStateException("body failed first");      // E1: the real failure
            }                                                              // close() throws E2 here
            // end::suppressed[]
        } catch (IllegalStateException primary) {
            // E1 propagated; the close() failure (E2) is attached to it as suppressed, not lost.
            return List.of(primary.getSuppressed());
        }
    }

    /**
     * A small {@link AutoCloseable} that records its close order and can be told to fail on close, standing
     * in for a file, socket, or JDBC handle.
     */
    static final class Channel implements AutoCloseable {

        private final String name;
        private final List<String> closeLog;
        private final boolean failOnClose;
        private boolean closed;

        Channel(String name, List<String> closeLog, boolean failOnClose) {
            this.name = name;
            this.closeLog = closeLog;
            this.failOnClose = failOnClose;
        }

        void send(String payload) {
            Objects.requireNonNull(payload, "payload");
        }

        @Override
        public void close() {
            if (closed) {
                return;   // idempotent: a quality close() has no effect when called more than once
            }
            closed = true;
            closeLog.add(name);
            if (failOnClose) {
                throw new IllegalStateException("close failed for " + name);
            }
        }

        boolean isClosed() {
            return closed;
        }
    }

    /**
     * Exposes the channel log helper for tests that need a fresh, mutable log.
     *
     * @return a new mutable list to pass as a close log
     */
    public static List<String> newCloseLog() {
        return new ArrayList<>();
    }
}
