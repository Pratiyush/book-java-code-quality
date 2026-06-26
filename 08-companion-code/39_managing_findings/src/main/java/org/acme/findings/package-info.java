/**
 * Chapter 39 companion — keeping a static-analysis gate honest over a real codebase.
 *
 * <p>The package carries the three kinds of code every gated codebase has: {@link
 * org.acme.findings.LegacyPriceTable} (a pre-existing finding, frozen by the SpotBugs baseline), {@link
 * org.acme.findings.PriceFormatter} (one reviewed false positive, suppressed narrowly with a
 * justification at the site), and {@link org.acme.findings.PricingCatalog} (clean new code the ratchet
 * protects). Alongside them is a runnable model of the chapter's decision and discipline: {@link
 * org.acme.findings.Finding}, {@link org.acme.findings.Disposition}, {@link
 * org.acme.findings.FindingTriage} (the triage decision — fix, suppress, accept, or baseline), and {@link
 * org.acme.findings.FindingRatchet} (forbid new findings while grandfathering the frozen ones).
 *
 * <p>The triage and ratchet types are an illustration of the policy, not a replacement for any tool's own
 * baseline/new-code engine. They make the chapter's rules executable and testable; the real freezing is
 * done by the SpotBugs and Checkstyle config under {@code config/}.
 */
package org.acme.findings;
