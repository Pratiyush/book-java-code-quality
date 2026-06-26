package org.acme.cigate;

import java.util.Objects;

/**
 * One finding a stage of the pipeline produced — a Checkstyle violation, a SpotBugs bug, a dependency
 * vulnerability, an uncovered new line. It carries enough for the quality gate to decide what to do
 * with it: which check raised it, how severe it is, and whether it sits in new or pre-existing code.
 *
 * <p>An immutable value (Item 17): a finding is a fact about a point in time, so it never changes after
 * it is recorded. The rule id is kept verbatim so a gate's verdict points at the exact rule a developer
 * can look up, rather than a paraphrase — a finding the team cannot trace is a finding the team ignores.
 *
 * @param ruleId   the raising rule's identifier, for example a Checkstyle or SpotBugs id, never {@code null}
 * @param severity how serious the finding is, never {@code null}
 * @param scope    whether the finding is in new/changed or pre-existing code, never {@code null}
 */
public record Finding(String ruleId, Severity severity, FindingScope scope) {

    /** Compact constructor: every component is required, so an incomplete finding can never exist. */
    public Finding {
        Objects.requireNonNull(ruleId, "ruleId");
        Objects.requireNonNull(severity, "severity");
        Objects.requireNonNull(scope, "scope");
    }
}
