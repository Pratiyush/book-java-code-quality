package org.acme.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The release-readiness gate: given a {@link ReleaseCandidate}, it decides whether the artifact may ship.
 * This is the load-bearing decision the chapter argues for, made runnable and unit-tested — the local
 * equivalent of the release gate the workflow in {@code ci/release.yml} wires as the last step before a
 * tag is cut. It assumes what the whole chapter assumes: a defect may have slipped every upstream gate,
 * so this gate verifies the release <em>artifact</em> is the green, traceable, reversible thing the
 * pipeline produced — it does not re-check the code (that was the pipeline's job and human review's).
 *
 * <p>The decision is the chapter's release preconditions applied against the active profile's policy. The
 * gate collects every required check the candidate fails; if the list is empty the release is
 * {@link ReleaseDecision.Ready}, otherwise it is {@link ReleaseDecision.Blocked} naming exactly those
 * failures. So a blocked release always says what to fix — cut a release version rather than a snapshot,
 * add the missing changelog entry, re-run the red pipeline, sign the artifact — rather than a bare
 * refusal. A {@code Ready} verdict means the artifact cleared the gates; it does not mean the code is
 * correct, which is the chapter's honest ceiling (release quality limits the blast radius of a defect, it
 * does not prevent one — that is the rest of the book).
 *
 * <p>This gate decides; it does not gather the evidence. The signals it reads (CI status, the signing
 * attestation, the smoke-job result) are produced by other steps in the release pipeline and arrive on
 * the {@link ReleaseCandidate}; the runnable, unit-tested part here is the policy that consumes them.
 */
public final class ReleaseReadiness {

    private final ReleasePolicy policy;
    private final AtomicLong evaluations = new AtomicLong();
    private final AtomicLong blocked = new AtomicLong();

    /**
     * Creates a gate that decides under the given externalized policy.
     *
     * @param policy the profile's required-check policy, never {@code null}
     */
    public ReleaseReadiness(ReleasePolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * Decides whether {@code candidate} may be released under the wired policy.
     *
     * @param candidate the release candidate and its observed precondition state, never {@code null}
     * @return the release decision — ready, or blocked with the failed checks — never {@code null}
     */
    public ReleaseDecision evaluate(ReleaseCandidate candidate) {
        Objects.requireNonNull(candidate, "candidate");
        evaluations.incrementAndGet();
        // tag::release-readiness[]
        List<ReleaseCheck> failures = new ArrayList<>();
        for (ReleaseCheck check : policy.required()) {   // only the checks this profile requires
            if (!candidate.satisfies(check)) {           // a release version, changelog, CI green, …
                failures.add(check);                     // collect every precondition that fails
            }
        }
        // end::release-readiness[]
        if (failures.isEmpty()) {
            return new ReleaseDecision.Ready();          // every required check passed — ship it
        }
        blocked.incrementAndGet();
        return new ReleaseDecision.Blocked(failures);    // hard stop — names exactly what failed
    }

    /**
     * The number of evaluations whose verdict blocked a release — the gate's headline health metric. A
     * dashboard trends this the way it trends change-failure rate (DORA, key 85): a block rate that is
     * always zero may mean the gate is too loose, and one that is always high may mean the upstream
     * pipeline is leaving releases un-shippable.
     *
     * @return the running count of blocking decisions this gate has returned
     */
    public long blockedCount() {
        return blocked.get();
    }

    /**
     * Whether the gate is ready to decide — a readiness probe over its wired policy. A gate with no
     * policy could only fail open (release everything), which is the silent way a release gate stops
     * gating, so an unconfigured gate reports not-ready rather than waving artifacts out the door.
     *
     * @return {@code true} once a policy is wired, the readiness signal a health endpoint would expose
     */
    public boolean isReady() {
        return policy != null;
    }
}
