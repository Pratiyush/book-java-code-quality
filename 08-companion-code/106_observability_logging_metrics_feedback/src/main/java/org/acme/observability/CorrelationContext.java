package org.acme.observability;

/**
 * A thread-bound correlation (trace) id, carried onto every log line so one request can be followed
 * across the calls it makes. In a production stack this is SLF4J's MDC, populated by OpenTelemetry
 * from the active span; here it is a {@link ThreadLocal} doing the same job on the JDK alone — the
 * three pillars correlate by trace id, and this is where the id lives.
 *
 * <p>The id must be cleared when the request ends, or it leaks onto the next request the thread
 * serves (the classic MDC bug). {@link #withTraceId(String, Runnable)} sets it, runs the work, and
 * clears it in a {@code finally}, so the cleanup cannot be forgotten.
 */
public final class CorrelationContext {

    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();
    private static final String NONE = "-";

    private CorrelationContext() {
    }

    // tag::correlation-id[]
    /** Runs {@code work} with {@code traceId} bound, clearing it afterwards so it cannot leak. */
    public static void withTraceId(String traceId, Runnable work) {
        TRACE_ID.set(traceId);
        try {
            work.run();
        } finally {
            TRACE_ID.remove();
        }
    }
    // end::correlation-id[]

    /** The current thread's trace id, or {@code "-"} when none is bound. */
    public static String currentTraceId() {
        String id = TRACE_ID.get();
        return id == null ? NONE : id;
    }
}
