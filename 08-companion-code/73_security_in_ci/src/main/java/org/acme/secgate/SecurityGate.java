package org.acme.secgate;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The security gate: given the findings every security stage produced in one run, it aggregates them and
 * decides whether a change may merge. This is the local equivalent of the gate the CI config in
 * {@code ci/security-pipeline.yml} wires across its stages — the same policy, expressed in code so it can
 * be unit-tested, and it reaches the decision the chapter argues for: scope to new code, block narrowly
 * on exploitable high-severity findings, route the rest to a reviewer.
 *
 * <p>The gate's job is the <em>assembly</em>, not the scanning. The stages that <em>produce</em> findings
 * (the SAST, SCA, secrets, and dynamic scans) run in the pipeline and are environment-gated; the
 * runnable, unit-tested part here is the policy that consumes their pooled findings and returns a single
 * verdict. The decision applies the chapter's policy axes in order:
 *
 * <ol>
 *   <li><b>Clean-as-you-code scope.</b> Under {@link SecurityGatePolicy} with {@code cleanAsYouCode}, a
 *       pre-existing finding is out of scope for blocking — it is security debt the change did not create,
 *       so a change that adds nothing new is never blocked on inherited findings (Chapter 19). This is the
 *       single most important tuning decision: a gate that blocks every pull request on legacy findings is
 *       the gate the team disables.</li>
 *   <li><b>Block narrowly.</b> Of the findings in scope, one at or above the policy's block severity that
 *       is confirmed exploitable blocks the build.</li>
 *   <li><b>Route the rest to a reviewer.</b> A severe finding that is not confirmed exploitable (an
 *       unreachable sink, a non-security use of a weak primitive) is routed to a security reviewer rather
 *       than auto-blocking, because exploitability is a judgment a severity number does not capture
 *       (Chapter 84). Sub-blocking findings are routed for review too.</li>
 * </ol>
 *
 * <p>The honest center, carried here in code and not only in prose: a {@link SecurityGateDecision.Pass}
 * means "no detected, known, exploitable high-severity issue in new code", <em>not</em> "secure". The
 * stages catch pattern-matchable known vulnerability classes; they do not find broken-access-control or
 * business-logic flaws, which need threat modeling and design review (Chapter 84) and which a finding with
 * stage {@code null} could never carry because no stage produces them. A green gate is necessary and
 * high-value, and it is one layer in a posture that also includes secure design and periodic assessment.
 */
public final class SecurityGate {

    private final SecurityGatePolicy policy;
    private final AtomicLong evaluations = new AtomicLong();
    private final AtomicLong blocks = new AtomicLong();

    /**
     * Creates a gate that decides under the given externalized policy.
     *
     * @param policy the clean-as-you-code, block-severity, and exploitability policy, never {@code null}
     */
    public SecurityGate(SecurityGatePolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * Decides whether the change that produced {@code findings} across all security stages may merge.
     *
     * @param findings the findings every security stage of the run produced, never {@code null}
     * @return the gate decision — pass, route-to-review, or block — never {@code null}
     */
    public SecurityGateDecision evaluate(List<SecurityFinding> findings) {
        Objects.requireNonNull(findings, "findings");
        evaluations.incrementAndGet();
        // tag::aggregate-and-gate[]
        List<SecurityFinding> inScope = findings.stream()
            .filter(f -> !policy.cleanAsYouCode() || f.scope() == FindingScope.NEW)   // new code only
            .toList();
        SecurityFinding worstBlocking = inScope.stream()
            .filter(f -> f.severity().meetsOrExceeds(policy.blockSeverity()))         // block narrowly
            .filter(f -> f.exploitable() || !policy.requireExploitableToBlock())      // exploitable, else review
            .max(Comparator.comparing(SecurityFinding::severity))
            .orElse(null);
        // end::aggregate-and-gate[]
        if (worstBlocking != null) {
            blocks.incrementAndGet();
            return new SecurityGateDecision.Block("blocking: new exploitable " + worstBlocking.severity()
                + " " + worstBlocking.stage() + " finding " + worstBlocking.ruleId());
        }
        if (!inScope.isEmpty()) {
            return new SecurityGateDecision.Review(
                inScope.size() + " finding(s) routed to a security reviewer (none auto-blocking)");
        }
        return new SecurityGateDecision.Pass("no new exploitable findings at or above " + policy.blockSeverity());
    }

    /**
     * The number of evaluations whose verdict blocked a merge — the security gate's headline health
     * metric. A dashboard trends this the way it trends pipeline duration: a block rate always at zero may
     * mean the gate is too loose, and one always high may mean it is too noisy and is about to be routed
     * around (Chapter 19). The metric is what tells a team the gate's policy needs tuning before the team
     * disables it.
     *
     * @return the running count of blocking decisions this gate has returned
     */
    public long blockedCount() {
        return blocks.get();
    }

    /**
     * Which security stages actually reported a finding in a run — the coverage signal a dashboard surfaces
     * so a stage that silently stopped running is noticed. A stage missing from this set is not proof of a
     * clean result; it can equally mean the stage did not run, which is the failure mode a security gate
     * must make visible rather than read as "secure".
     *
     * @param findings the findings a run produced, never {@code null}
     * @return the set of stages that contributed at least one finding, never {@code null}
     */
    public Set<SecurityStage> stagesReporting(List<SecurityFinding> findings) {
        Objects.requireNonNull(findings, "findings");
        EnumSet<SecurityStage> reporting = EnumSet.noneOf(SecurityStage.class);
        findings.forEach(f -> reporting.add(f.stage()));
        return reporting;
    }

    /**
     * Whether the gate is ready to decide — a readiness probe over its wired policy. A gate with no policy
     * could only fail open (pass everything), which is the silent way a security gate stops gating, so an
     * unconfigured gate reports not-ready rather than waving changes through.
     *
     * @return {@code true} once a policy is wired, the readiness signal a health endpoint would expose
     */
    public boolean isReady() {
        return policy != null;
    }
}
