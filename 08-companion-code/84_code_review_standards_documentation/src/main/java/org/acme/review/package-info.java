/**
 * Chapter 84 — the human side of quality (code review, coding standards, documentation).
 *
 * <p>This package is the small, runnable half of a chapter that is mostly process and documentation. The
 * chapter's artifacts that are not code — the PR template, the {@code CODEOWNERS} routing, the review
 * checklist, the ADR — live alongside this module under {@code .github/} and {@code docs/}; the displayed
 * snippets for those are tag regions in the artifacts themselves. This Java carries the part of the
 * documentation standard that <em>is</em> code: {@link org.acme.review.RefundPolicy} is the exemplar of
 * Javadoc-as-contract on a public API (what / params / return / {@code @throws} / nullability, never
 * "what" narration), and {@link org.acme.review.ReviewThroughputHealth} is the observability surface that
 * keeps review size visible against the effective-review zone.
 *
 * <p>The {@code quality} profile holds the module to its own documentation gate (Checkstyle's Javadoc
 * presence rules) and a correctness gate (SpotBugs), so a green run shows both halves the chapter argues:
 * the mechanical floor a tool enforces (a Javadoc is present and well-formed) and the substantive part it
 * cannot (that the contract is true and the code is correct), the latter carried by the tests and the
 * human reviewer the checklist is written for.
 */
package org.acme.review;
