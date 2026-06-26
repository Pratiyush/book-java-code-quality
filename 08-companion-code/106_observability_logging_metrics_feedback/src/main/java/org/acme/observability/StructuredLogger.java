package org.acme.observability;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A small structured logger over {@link java.lang.System.Logger}, the JDK logging API. It writes
 * leveled, key-value log lines — queryable, not free text — and stamps every line with the current
 * correlation id ({@link CorrelationContext}). Before any line is written it runs a redaction pass
 * that replaces the values of known-secret fields, so a password or token never reaches the log even
 * if a caller passes one: redaction is mandatory, not decorative (logging a secret is a breach,
 * Chapter 31).
 *
 * <p>In a production stack this role is SLF4J with a JSON encoder and a redaction filter; the shape
 * is the same. The line is built by appending fields rather than concatenating a message string, so
 * the work is skipped entirely when the level is disabled (parameterized, not string-soup).
 */
public final class StructuredLogger {

    /** Field names whose values are always replaced before a line is written. */
    private static final Set<String> SECRET_KEYS = Set.of("password", "token", "secret", "ssn", "card");
    private static final String REDACTED = "***";

    private final Logger delegate;
    private final Level threshold;

    /**
     * Creates a logger for {@code owner}, writing at or above {@code threshold}.
     *
     * @param owner     the class the log lines are attributed to, never {@code null}
     * @param threshold the level floor from {@link ObservabilityConfig}, never {@code null}
     */
    public StructuredLogger(Class<?> owner, Level threshold) {
        this.delegate = System.getLogger(owner.getName());
        this.threshold = threshold;
    }

    // tag::structured-log[]
    /** Writes one leveled, key-value line stamped with the trace id; redacts any secret field. */
    public void log(Level level, String event, Map<String, Object> fields) {
        if (level.getSeverity() < threshold.getSeverity()) {
            return; // level disabled — build nothing (parameterized, not string-soup)
        }
        Map<String, Object> safe = redact(fields);
        delegate.log(level, "event={0} trace_id={1} {2}",
            event, CorrelationContext.currentTraceId(), render(safe));
    }
    // end::structured-log[]

    // tag::redaction[]
    /** Replaces the value of any known-secret field; logging a secret is a breach, not a style nit. */
    static Map<String, Object> redact(Map<String, Object> fields) {
        Map<String, Object> safe = new LinkedHashMap<>();
        fields.forEach((key, value) ->
            safe.put(key, SECRET_KEYS.contains(key) ? REDACTED : value));
        return safe;
    }
    // end::redaction[]

    private static String render(Map<String, Object> fields) {
        return fields.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining(" "));
    }
}
