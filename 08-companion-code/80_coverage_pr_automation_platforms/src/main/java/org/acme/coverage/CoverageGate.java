package org.acme.coverage;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The diff-scoped, ratcheting coverage gate — the load-bearing decision the chapter argues for, made
 * runnable so the prose and the build cannot drift.
 *
 * <p>The platform (Codecov/Coveralls/Sonar) computes the {@link CoverageDelta} from the uploaded
 * JaCoCo report and the diff; this gate turns that delta into the chapter's verdict using two rules and
 * one warn-only signal:
 * <ol>
 *   <li><b>New-code focus.</b> New/changed lines must clear {@link CoveragePolicy#newCodeBar()}.
 *       Pre-existing legacy is never gated — a whole-repo absolute would block every pull request on
 *       inherited debt, the number-one way a gate is routed around (Chapter 19).</li>
 *   <li><b>Ratchet.</b> When enabled, overall coverage may not drop; a pull request that lowers it is
 *       blocked, so the curve only bends up.</li>
 *   <li><b>Aspirational overall target.</b> Below {@link CoveragePolicy#overallTarget()} the gate only
 *       <em>warns</em>; making a whole-repo percentage block is what gets it gamed with assertion-free
 *       tests (Goodhart). Coverage is a floor, not a goal.</li>
 * </ol>
 *
 * <p>The gate is the explicit failure path: it returns a sealed {@link CoverageVerdict}, never a bare
 * boolean, and a {@link CoverageVerdict.Block} carries the failing rule so the PR comment is
 * actionable. Its only state is an observability counter of how many merges it has blocked.
 */
public final class CoverageGate {

    private static final Logger LOG = System.getLogger(CoverageGate.class.getName());

    private final CoveragePolicy policy;

    /** Observability: merges blocked since startup — the headline metric a dashboard trends. */
    private final AtomicLong blocked = new AtomicLong();

    /**
     * Creates a gate over an externalized policy.
     *
     * @param policy the loaded coverage policy, never {@code null}
     * @throws NullPointerException if {@code policy} is {@code null}
     */
    public CoverageGate(CoveragePolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * Evaluates one pull request's coverage delta against the policy.
     *
     * @param delta the pull request's coverage picture, never {@code null}
     * @return the verdict — pass, warn, or block — with its reason
     * @throws NullPointerException if {@code delta} is {@code null}
     */
    public CoverageVerdict evaluate(CoverageDelta delta) {
        Objects.requireNonNull(delta, "delta");
        // tag::new-code-gate[]
        if (delta.touchesNewCode() && delta.newCodeCovered() < policy.newCodeBar()) {
            return block("new-code coverage " + pct(delta.newCodeCovered())
                + " is below the bar " + pct(policy.newCodeBar()));   // gate NEW code, not the legacy mountain
        }
        if (policy.ratchet() && delta.overallChange() < 0.0) {
            return block("overall coverage would drop by "
                + pct(-delta.overallChange()) + " (ratchet: it may only go up)");
        }
        // end::new-code-gate[]
        if (delta.overallAfter() < policy.overallTarget()) {
            return new CoverageVerdict.Warn("overall coverage " + pct(delta.overallAfter())
                + " is below the aspirational target " + pct(policy.overallTarget())
                + " (warn only — a floor, not a goal)");
        }
        return new CoverageVerdict.Pass("new code clears the bar and overall coverage holds");
    }

    private CoverageVerdict block(String reason) {
        blocked.incrementAndGet();
        LOG.log(Level.INFO, "coverage gate blocked a merge: {0}", reason);
        return new CoverageVerdict.Block(reason);
    }

    /**
     * Readiness probe over the wired policy. A gate whose new-code bar is zero gates nothing — it can
     * only ever pass, the silent way a gate stops gating — so it reports not-ready rather than waving
     * every change through.
     *
     * @return {@code true} if the policy actually enforces something (a positive new-code bar or the
     *     ratchet)
     */
    public boolean isReady() {
        return policy.newCodeBar() > 0.0 || policy.ratchet();
    }

    /**
     * Observability surface: merges this gate has blocked since startup. A block rate stuck at zero may
     * mean the bar is too low; one stuck high, too strict — the same way pipeline duration is trended.
     *
     * @return the count of blocking verdicts, never negative
     */
    public long blockedCount() {
        return blocked.get();
    }

    private static String pct(double ratio) {
        return Math.round(ratio * 100.0) + "%";
    }
}
