/**
 * Chapter 2 — readability and the metrics that measure it (The Number That Lies).
 *
 * <p>The package turns the chapter's load-bearing measurement idea into one buildable module: cyclomatic
 * complexity (McCabe, 1976) answers <em>"how many tests do I need?"</em> by counting execution paths,
 * while cognitive complexity (G. Ann Campbell / SonarSource, rule {@code java:S3776}) answers <em>"how
 * hard is this to read?"</em> and increments more for nesting. To show the two metrics part company, one
 * discount rule is written three ways behind a single {@link org.acme.readability.DiscountRule} contract,
 * all returning the identical {@link org.acme.readability.Money} for every input:
 *
 * <ul>
 *   <li><strong>Deeply nested.</strong> {@link org.acme.readability.DiscountRulesNested} packs the
 *       branches several levels deep — the shape {@code java:S3776} scores high; spelling the tier out as
 *       a branch ladder raises its cyclomatic path count above the balanced form's as well.</li>
 *   <li><strong>Over-fragmented.</strong> {@link org.acme.readability.DiscountRulesFragmented} splits the
 *       same logic across many one-line methods — low per-method cognitive score, but following one idea
 *       means hopping between fragments, the cost a philosophy of deep modules names.</li>
 *   <li><strong>Balanced.</strong> {@link org.acme.readability.DiscountRules} flattens the nesting into
 *       guard clauses and keeps the logic in one readable body, with the tier carrying its own
 *       discount.</li>
 * </ul>
 *
 * <p>The pairing carries the chapter's honesty about measurement: the cognitive score and the result are
 * independent axes (the number can change while behaviour does not), the threshold is a convention tools
 * disagree on, and a low score with poor structure is still unreadable — so a metric is a question, not a
 * verdict. The contested prescriptions (tiny functions versus deep modules) are presented as a context-
 * dependent trade-off, with neither form crowned. {@link org.acme.readability.PricingService} wraps a
 * chosen rule under externalized policy and exposes the counter and readiness probe the later
 * observability chapter builds on.
 */
package org.acme.readability;
