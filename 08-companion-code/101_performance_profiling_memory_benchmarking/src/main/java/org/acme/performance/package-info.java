/**
 * Chapter 43 — performance as a quality attribute, measured rather than guessed (Measure, Don't
 * Guess).
 *
 * <p>The package carries three movements of the chapter as runnable code. {@link
 * org.acme.performance.OrderPricing} is a workload whose costly path is deliberately not the one a
 * reader would guess, so a profiler — not intuition — is the instrument that finds it; the same class
 * shows a hot method whose per-call allocation churn is reduced, but only in the form the chapter
 * insists on: after measurement, with the cheaper version proven to produce the identical result.
 * {@link org.acme.performance.PricingBenchmark} is a JMH microbenchmark written twice — once the lying
 * way the JVM quietly defeats, once the honest way — to make the failure mode visible in code.
 *
 * <p>The discipline the package encodes is the chapter's spine: measure, don't guess. Every comment
 * that recommends an optimization also names its cost or its precondition, because the dominant
 * failure of performance work is optimizing the wrong thing on a guess.
 */
package org.acme.performance;
