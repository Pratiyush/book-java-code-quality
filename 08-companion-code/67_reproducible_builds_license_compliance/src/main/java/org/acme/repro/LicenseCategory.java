package org.acme.repro;

/**
 * The obligation category a license falls into. The chapter states these bands factually: they describe
 * the <em>shape</em> of a license's obligations, not whether a given use is compliant. Interpreting a
 * specific obligation for a specific distribution is a question for legal counsel — this enum is
 * <strong>factual, not legal advice</strong> (the chapter's prominent caveat, encoded as a type).
 *
 * <p>The same license can be acceptable in one distribution mode and a problem in another (an internal
 * tool that never ships versus a redistributed binary versus a network/SaaS deployment), so a category is
 * an input to a policy decision, never the decision itself.
 */
public enum LicenseCategory {

    /** Permissive (e.g. {@code Apache-2.0}, {@code MIT}, {@code BSD-3-Clause}): attribution only. */
    PERMISSIVE,

    /** Weak copyleft (e.g. {@code LGPL-2.1-only}, {@code MPL-2.0}, {@code EPL-2.0}): file/library share-alike. */
    WEAK_COPYLEFT,

    /**
     * Strong copyleft (e.g. {@code GPL-3.0-only}, {@code AGPL-3.0-only}): derivative-work obligations.
     * {@code AGPL} notably reaches network/SaaS use, not only shipped binaries.
     */
    STRONG_COPYLEFT,

    /** Category not determined from metadata: missing, ambiguous, or unrecognised. Triage by hand. */
    UNKNOWN
}
