package org.acme.contracttesting;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Renders a multi-line, human-readable order report — the kind of large, structured output that is
 * tedious and brittle to assert field by field, and so is a natural fit for approval testing.
 *
 * <p>The report deliberately carries one non-deterministic element, a generated-at timestamp, so the
 * companion snapshot test has something real to scrub. Without scrubbing, the timestamp would change the
 * output on every run and the approval test would flake; with it, the test pins the stable content.
 */
public final class OrderReport {

    private OrderReport() {
        // utility holder; not instantiable
    }

    /**
     * Renders a report listing the given orders with a header and a total line.
     *
     * @param orders      the orders to include, never {@code null}
     * @param generatedAt the time the report was generated, never {@code null}
     * @return the rendered multi-line report
     * @throws NullPointerException if either argument is {@code null}
     */
    public static String render(List<Order> orders, Instant generatedAt) {
        Objects.requireNonNull(orders, "orders");
        Objects.requireNonNull(generatedAt, "generatedAt");
        long total = orders.stream().mapToLong(Order::total).sum();
        String lines = orders.stream()
            .map(o -> "  - " + o.id() + " [" + o.status() + "] " + o.total())
            .collect(Collectors.joining("\n"));
        return "ORDER REPORT\n"
            + "generated-at: " + generatedAt + "\n"
            + "count: " + orders.size() + "\n"
            + lines + "\n"
            + "total: " + total + "\n";
    }
}
