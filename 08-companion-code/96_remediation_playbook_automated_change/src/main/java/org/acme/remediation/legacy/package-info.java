/**
 * The "before" form for the automation engine — the legacy idiom an OpenRewrite recipe matches and
 * rewrites. It is isolated in its own package on purpose: it carries the older idioms deliberately, so the
 * static-analysis gate baselines this package rather than failing on it (the chapter's "freeze the legacy,
 * gate the new" applied to the module itself). The modernized counterpart lives one package up as {@code
 * org.acme.remediation.Modernized}. Both compile green on the anchor runtime; the recipe that turns one
 * into the other is in {@code config/rewrite/rewrite.yml}, run opt-in and verified by the tests.
 */
package org.acme.remediation.legacy;
