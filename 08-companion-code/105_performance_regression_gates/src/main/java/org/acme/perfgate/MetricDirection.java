package org.acme.perfgate;

/**
 * Which way is better for a measured metric. A regression gate cannot decide whether a number went
 * the wrong way without knowing this: a larger latency is a regression, while a larger throughput is
 * an improvement. The same percentage move means opposite things for the two directions, so the
 * direction travels with the metric rather than being assumed.
 */
public enum MetricDirection {

    /** Lower is better — latency, allocation rate, startup time. A rise past tolerance regresses. */
    LOWER_IS_BETTER,

    /** Higher is better — throughput. A fall past tolerance regresses. */
    HIGHER_IS_BETTER
}
