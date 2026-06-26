/**
 * Chapter 40 companion — taming an inherited disaster with an ordered, incremental remediation playbook,
 * and the type-aware automation engine that scales the craft.
 *
 * <p>This package makes the chapter's two synthesis claims runnable. The {@link
 * org.acme.remediation.RemediationPrioritizer} encodes the central prioritization rule — remediate by
 * churn &times; pain, because debt in frozen code accrues no interest — over a list of {@link
 * org.acme.remediation.DebtItem}. The {@link org.acme.remediation.RemediationPlan} models the ordered
 * playbook (assess/baseline &rarr; gate new code &rarr; safety net &rarr; hotspot paydown &rarr; strangle
 * the unsalvageable &rarr; sustain) and refuses the two failure modes the chapter names loudly: a
 * default big-bang rewrite, and a baseline with no paydown plan. The {@link
 * org.acme.remediation.ProgramHealth} surface reports whether the program is sustained or stalling,
 * because remediation succeeds or fails on sustained commitment more than on tooling.
 *
 * <p>The automation engine appears as the before/after pair under {@link org.acme.remediation.legacy}
 * (the form the OpenRewrite recipe matches) and {@link org.acme.remediation.Modernized} (the form it
 * rewrites to), with the recipe itself in {@code config/rewrite/rewrite.yml}. Automation proposes; the
 * tests and the review dispose.
 */
package org.acme.remediation;
