package org.acme.cigate;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The quality gate: given the findings a pipeline run produced, it decides whether a change may merge.
 * This is the local equivalent of the gate the CI config in {@code ci/quality-gates.yml} wires as a
 * required status check — the same policy, expressed in code so it can be unit-tested, runs the same
 * way a developer would run {@code mvn verify} locally (Chapter 27, local/CI parity), and reaches the
 * decision the chapter argues for: scope to new code, block narrowly, warn on the rest.
 *
 * <p>The decision is the chapter's two policy axes applied in order. Under {@link GatePolicy} with
 * {@code cleanAsYouCode}, a pre-existing finding is out of scope entirely — it is debt the change did
 * not create, so a change that adds nothing new passes even atop a mountain of legacy debt, and the
 * gate never stops a pull request on inherited problems. Of the findings that remain in scope, one at
 * or above the policy's block severity blocks the build, and a less-severe one is warned (surfaced but
 * not blocking). So a red gate always means a real, new, high-severity problem in code the developer
 * just touched, and a warning is a new-but-minor finding worth a look rather than a hard stop.
 *
 * <p>This gate decides; it does not run the checks. The stages that <em>produce</em> findings (the
 * analyzers, the test/coverage run, the dependency scan) run in CI and are environment-gated; the
 * runnable, unit-tested part here is the policy that consumes their findings and returns a verdict.
 */
public final class QualityGate {

    private final GatePolicy policy;
    private final AtomicLong evaluations = new AtomicLong();
    private final AtomicLong blocks = new AtomicLong();

    /**
     * Creates a gate that decides under the given externalized policy.
     *
     * @param policy the clean-as-you-code and block-severity policy, never {@code null}
     */
    public QualityGate(GatePolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * Decides whether the change that produced {@code findings} may merge.
     *
     * @param findings the findings every stage of the run produced, never {@code null}
     * @return the gate decision — pass, warn, or block — never {@code null}
     */
    public GateDecision evaluate(List<Finding> findings) {
        Objects.requireNonNull(findings, "findings");
        evaluations.incrementAndGet();
        // tag::clean-as-you-code[]
        List<Finding> gated = findings.stream()
            .filter(f -> !policy.cleanAsYouCode() || f.scope() == FindingScope.NEW)  // new code only
            .toList();
        Finding worstBlocking = gated.stream()
            .filter(f -> f.severity().compareTo(policy.blockSeverity()) >= 0)        // block narrowly
            .max(Comparator.comparing(Finding::severity))
            .orElse(null);
        // end::clean-as-you-code[]
        if (worstBlocking != null) {
            blocks.incrementAndGet();
            return new GateDecision.Block(
                "blocking: new " + worstBlocking.severity() + " finding " + worstBlocking.ruleId());
        }
        if (!gated.isEmpty()) {
            return new GateDecision.Warn(gated.size() + " finding(s) to review (none blocking)");
        }
        return new GateDecision.Pass("no new findings at or above " + policy.blockSeverity());
    }

    /**
     * The number of evaluations whose verdict blocked a merge — the gate's headline health metric. A
     * dashboard trends this the way it trends pipeline duration: a block rate that is always zero may
     * mean the gate is too loose, and one that is always high may mean it is too strict (Chapter 38).
     *
     * @return the running count of blocking decisions this gate has returned
     */
    public long blockedCount() {
        return blocks.get();
    }

    /**
     * Whether the gate is ready to decide — a readiness probe over its wired policy. A gate with no
     * policy could only fail open (pass everything), which is the silent way a gate stops gating, so an
     * unconfigured gate reports not-ready rather than waving changes through.
     *
     * @return {@code true} once a policy is wired, the readiness signal a health endpoint would expose
     */
    public boolean isReady() {
        return policy != null;
    }
}
