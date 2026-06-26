package org.acme.secgate;

import java.util.Objects;

/**
 * One finding a security stage produced — a SAST injection sink, an SCA known-CVE match, a leaked
 * secret, a DAST runtime misconfiguration. It carries enough for the security gate to aggregate it with
 * the findings of the other stages and decide what to do with it: which stage raised it, the rule or
 * advisory id, how severe it is, whether it sits in new or pre-existing code, and whether it is
 * confirmed exploitable.
 *
 * <p>An immutable value (Item 17): a finding is a fact about a point in time, so it never changes after
 * it is recorded. The rule id is kept verbatim — a CWE number, a CVE id, a rule key — so a gate's
 * verdict points at the exact finding a developer or reviewer can look up, rather than a paraphrase.
 *
 * <p>The {@code exploitable} flag carries one of the chapter's honest edges as a first-class field, not
 * a footnote: a severity number is not the same as exploitability. An unreachable sink or a non-security
 * use of a weak primitive is a judgment a reviewer makes, so a finding that is severe but not confirmed
 * exploitable is routed to a reviewer rather than auto-blocking the build (Chapter 84).
 *
 * @param stage       the security stage that produced the finding, never {@code null}
 * @param ruleId      the raising rule or advisory identifier, for example a CWE or CVE id, never {@code null}
 * @param severity    how serious the finding is, never {@code null}
 * @param scope       whether the finding is in new/changed or pre-existing code, never {@code null}
 * @param exploitable whether the finding is confirmed exploitable, as opposed to severe-but-unjudged
 */
public record SecurityFinding(
        SecurityStage stage, String ruleId, Severity severity, FindingScope scope, boolean exploitable) {

    /** Compact constructor: every non-boolean component is required, so an incomplete finding can never exist. */
    public SecurityFinding {
        Objects.requireNonNull(stage, "stage");
        Objects.requireNonNull(ruleId, "ruleId");
        Objects.requireNonNull(severity, "severity");
        Objects.requireNonNull(scope, "scope");
    }
}
