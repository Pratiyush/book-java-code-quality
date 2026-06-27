package org.acme.storefront.money;

/**
 * One reported breach of the house invariant, in a form shared by every realization that reports rather
 * than throws (the reflective inspector, and the rule tests). It is the "report" step of the shared
 * select&rarr;predicate&rarr;report&rarr;gate shape, made into a value so a caller can collect, count, and
 * assert on findings the way an analyzer report lets a dashboard consume them (Chapter 45).
 *
 * @param where    a human-readable location of the breach (a member or type description)
 * @param message  the explanation of why it breaches the invariant
 * @param severity the configured severity at which it was reported
 */
public record MoneyViolation(String where, String message, MoneyPolicy.Severity severity) {
}
