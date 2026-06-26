/**
 * Chapter 41 companion (The Draft That Looks Like a Deliverable — the Part XII umbrella). The
 * chapter's stance is that AI-generated code is a draft, not a deliverable: it goes through the same
 * quality gate as human code, plus a few AI-specific checks. This package shows that stance in code on
 * the storefront domain, in two demonstrations.
 *
 * <p>The first is the security half. {@link org.acme.ai.AiDraftedLookup} is an AI-drafted customer
 * lookup that builds its SQL by string concatenation — the vulnerability-inheritance pattern the
 * chapter describes, the same injection class as OWASP Top 10:2025 A03 (CWE-89). It is a deliberate
 * teaching counter-example, exercised only for behavior by a test and never wired into a running path.
 * {@link org.acme.ai.ReviewedLookup} is the reviewed fix that binds the value as a
 * {@link java.sql.PreparedStatement} parameter. {@link org.acme.ai.AiReviewGate} is the running path,
 * wired to the reviewed fix alone.
 *
 * <p>The second is the test-generation half. {@link org.acme.ai.OrderTotals} is a small piece of
 * order arithmetic with a boundary and a computation. The accompanying tests show the contrast the
 * chapter draws: a test generated <em>from</em> the implementation reaches full line coverage while
 * asserting almost nothing, so the boundary and arithmetic mutants survive (the tests-from-code trap);
 * a spec-derived test encodes the intended behavior independently and kills those mutants. Mutation
 * testing (Chapter 23) is the check that exposes the hollow test.
 *
 * <p>Every AI statistic in the chapter prose is a dated, attributed snapshot; none is embedded in this
 * code, because a figure baked into code would silently go stale. The code demonstrates the structural
 * mechanisms, which do not date, not the figures, which do.
 */
package org.acme.ai;
