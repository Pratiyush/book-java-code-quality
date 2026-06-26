package org.acme.metrics;

/**
 * A metric bound to its counter-metric, so the pair can only be reported together. Counter-metrics are
 * the central defence against Goodhart's law: when a measure becomes a target it stops measuring what
 * was wanted, and pairing it with an opposing measure means gaming the first shows up in the second.
 * Deployment frequency is paired with change-failure rate (speed against safety); coverage with mutation
 * score (quantity of tests against their strength); throughput with stability.
 *
 * <p>The type is a record so the pairing is immutable, and {@link #report(double, double)} hands back a
 * {@link Reading} that always carries both values — there is no API here that returns the primary alone.
 * Reporting a number without its counter-metric is the failure mode this type exists to prevent.
 *
 * @param primary the metric being optimised for (its name)
 * @param counter the opposing metric that catches gaming of the primary (its name)
 */
public record CounterMetric(String primary, String counter) {

    /** Compact constructor: a metric paired with itself is no counter-metric at all. */
    public CounterMetric {
        if (primary == null || primary.isBlank()) {
            throw new IllegalArgumentException("primary must not be blank");
        }
        if (counter == null || counter.isBlank()) {
            throw new IllegalArgumentException("counter must not be blank");
        }
        if (primary.equals(counter)) {
            throw new IllegalArgumentException("a metric cannot be its own counter-metric: " + primary);
        }
    }

    // tag::counter-metric[]
    /** Reports the pair together: the primary value is never handed back without its counter-metric. */
    public Reading report(double primaryValue, double counterValue) {
        return new Reading(primary, primaryValue, counter, counterValue);
    }
    // end::counter-metric[]

    /** A paired reading — the primary and its counter-metric, inseparable by construction. */
    public record Reading(String primary, double primaryValue, String counter, double counterValue) { }
}
