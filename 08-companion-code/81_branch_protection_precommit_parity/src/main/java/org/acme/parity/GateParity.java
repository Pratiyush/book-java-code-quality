package org.acme.parity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The local&#8596;CI parity check: it decides whether the gate set a developer can run locally (the
 * pre-commit hooks plus the local build) reproduces every check CI requires (the required status checks
 * wired into branch protection). This is the chapter's central claim made runnable — "green locally
 * predicts green in CI" — and unit-tested, so the property is pinned by a test rather than assumed.
 *
 * <p>Parity is one direction, deliberately. Every check CI <em>requires</em> must have a local
 * counterpart, so a developer can reproduce the gate's verdict before pushing; the local set may run
 * <em>more</em> than CI (a developer is free to run extra checks) without breaking parity. What breaks
 * parity is a required CI check with no way to run it locally — that is exactly the "works locally,
 * fails in CI" surprise the chapter sets out to remove, so the check reports the missing checks by name.
 *
 * <p>This is FEEDBACK about the workflow's shape, not the enforcement itself. The enforcement is the
 * required CI check and branch protection (config/branch-protection/ruleset.yml); the pre-commit hooks
 * this parity describes are bypassable ({@code git commit --no-verify}) and run only where installed.
 * Confusing the two — treating a bypassable hook as the gate — is the category error the chapter names.
 */
public final class GateParity {

    private final ParityPolicy policy;
    private final AtomicLong checksRun = new AtomicLong();
    private final AtomicLong drifts = new AtomicLong();

    /**
     * Creates a parity check governed by the given externalized policy.
     *
     * @param policy the parity policy (whether drift fails the build, and the local-runner label),
     *               never {@code null}
     */
    public GateParity(ParityPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * Decides whether {@code local} reproduces every check {@code ci} requires.
     *
     * @param local the checks a developer can run locally (pre-commit hooks plus the local build),
     *              never {@code null}
     * @param ci    the checks CI requires (the required status checks), never {@code null}
     * @return {@link ParityResult.InParity} if local covers every required check, otherwise
     *         {@link ParityResult.Drifted} naming the checks missing locally, never {@code null}
     */
    public ParityResult check(GateSet local, GateSet ci) {
        Objects.requireNonNull(local, "local");
        Objects.requireNonNull(ci, "ci");
        checksRun.incrementAndGet();
        // tag::parity-assertion[]
        List<String> missingLocally = ci.checkNames().stream()
            .filter(required -> !local.contains(required))  // a required CI check with no local counterpart
            .toList();
        if (missingLocally.isEmpty()) {
            return new ParityResult.InParity();             // green locally can predict green in CI
        }
        drifts.incrementAndGet();
        return new ParityResult.Drifted(missingLocally);    // "works locally, fails in CI" — broken parity
        // end::parity-assertion[]
    }

    /**
     * Checks parity and, under a strict policy, fails fast on drift — the explicit failure path. A
     * lenient policy returns the drifted result for the caller to surface as a warning instead.
     *
     * @param local the checks a developer can run locally, never {@code null}
     * @param ci    the checks CI requires, never {@code null}
     * @return the parity result (always {@link ParityResult.InParity} when this returns normally under a
     *         strict policy), never {@code null}
     * @throws ParityBrokenException if the policy fails on drift and {@code local} misses a required check
     */
    public ParityResult enforce(GateSet local, GateSet ci) {
        ParityResult result = check(local, ci);
        // tag::feedback-not-enforcement[]
        if (!result.inParity() && policy.failOnDrift()) {
            // The required CI check is the enforcement; a hook is bypassable feedback. Broken parity
            // means the keyboard cannot reproduce the wall — fail fast and name the gap to close.
            throw new ParityBrokenException(((ParityResult.Drifted) result).missingLocally());
        }
        // end::feedback-not-enforcement[]
        return result;
    }

    /**
     * The number of parity checks that found drift — the headline health metric. A dashboard trends this
     * the way it trends pipeline duration: drift creeping above zero means the local and CI gate sets are
     * diverging, the slow erosion that ends in "works locally, fails in CI".
     *
     * @return the running count of drifted results this check has returned
     */
    public long driftCount() {
        return drifts.get();
    }

    /**
     * Whether the parity check is ready to decide — a readiness probe over its wired policy. With no
     * policy it could only fail open (declare parity unconditionally), the silent way a check stops
     * checking, so an unconfigured check reports not-ready rather than waving drift through.
     *
     * @return {@code true} once a policy is wired
     */
    public boolean isReady() {
        return policy != null;
    }
}
