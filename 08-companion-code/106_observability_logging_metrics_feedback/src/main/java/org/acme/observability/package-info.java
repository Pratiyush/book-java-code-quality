/**
 * Chapter 45 — observability as a code-quality concern (Understanding a Running System).
 *
 * <p>The package demonstrates the three pillars at the smallest size that still teaches them, on the
 * JDK alone. {@link org.acme.observability.StructuredLogger} emits leveled, parameterized,
 * key-value log lines and redacts secrets before they are written;
 * {@link org.acme.observability.CorrelationContext} carries a trace id onto every line so one
 * request can be followed across calls (the role SLF4J's MDC plays in a production stack);
 * {@link org.acme.observability.MetricsRegistry} records counters and timers — the four golden
 * signals — that a scrape endpoint could publish; and
 * {@link org.acme.observability.HealthGauge} turns the error counter into the feedback signal that
 * closes the loop back to a test.
 *
 * <p>The chapter names the real-world facades — SLF4J for logs, Micrometer for metrics, OpenTelemetry
 * for traces. Those libraries are not pinned authority rows for this book, so the code shows the
 * recurring shape (name a signal, record it, scrape it; populate a correlation id, carry it
 * everywhere) with JDK primitives, and the prose attributes the named facades. The same decoupling
 * instinct — stable instrumentation, swappable backend — is what those facades exist to provide.
 */
package org.acme.observability;
