package org.acme.supplychain;

import java.util.Objects;

/**
 * One finding from a dependency scan: a known vulnerability matched to a component. The chapter's two
 * honest limits are first-class fields here, not footnotes:
 *
 * <ul>
 *   <li>{@code reachable} encodes <b>"vulnerable" is not "exploitable"</b>: a finding in a code path the
 *       application never calls is not an emergency. Reachability analysis (some commercial tiers)
 *       narrows this; where it is unknown, a team triages by exposure rather than treating every finding
 *       as a fire.</li>
 *   <li>{@code identifier} is whatever the scanner reports (a CVE id, an advisory id) — the gate does not
 *       invent one, it records what it was given.</li>
 * </ul>
 *
 * @param componentName the affected component's name
 * @param identifier    the finding identifier as reported by the scanner (e.g. a CVE id)
 * @param severity      the finding's severity band (from its CVSS score)
 * @param reachable     whether the affected code path is reachable from this application
 */
public record ScanFinding(String componentName, String identifier, Severity severity, boolean reachable) {

    /** Compact constructor: a finding must name a component, an identifier, and a severity. */
    public ScanFinding {
        Objects.requireNonNull(componentName, "componentName");
        Objects.requireNonNull(identifier, "identifier");
        Objects.requireNonNull(severity, "severity");
    }
}
