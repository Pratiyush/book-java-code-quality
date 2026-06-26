package org.acme.findings;

/**
 * The four things a static-analysis finding can be, and the one lever each calls for (Chapter 39's
 * triage tree). A finding is exactly one of these; conflating them — reaching for a broad suppression
 * when the finding is real, or fixing nothing when it is debt — is the recurring hazard the chapter
 * names.
 */
public enum Disposition {

    /** A real defect: fix it. No suppression, no baseline. */
    FIX,

    /** The tool is wrong here, or it is right but the cost is accepted: suppress at the site, with a reason. */
    SUPPRESS,

    /** Pre-existing debt too large to fix now: freeze it in the baseline, then block new debt. */
    BASELINE
}
