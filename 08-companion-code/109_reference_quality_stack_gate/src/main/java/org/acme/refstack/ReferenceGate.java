package org.acme.refstack;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The reference gate: it composes the outcomes of the four-stage ladder into one ship / no-ship verdict.
 * Where Chapter 33's gate decides a single stage's findings, this capstone gate is the synthesis — it
 * takes what every stage of the run produced and returns the one decision a required status check reads
 * at the merge line, so the whole layered stack and the whole ladder reduce to a single answer rather
 * than a wall of per-tool reports.
 *
 * <p>The composition applies the chapter's three gate-design axes in order, under the externalized
 * {@link GateLadder}. A stage earlier than the ladder's enforced floor is advisory — surfaced, never
 * blocking — which is what lets a team adopt the stack incrementally (Chapters 38, 40). Of the enforced
 * stages, a finding is in scope only if it is on new code (clean-as-you-code) or the ladder gates the
 * whole repo; an in-scope finding at or above the block severity makes the change not ship, and anything
 * below is advisory. So a no-ship verdict always means a real, new, high-severity problem at a stage the
 * team chose to enforce, and a ship verdict means the mechanical floor is clear — not that the code is
 * good, which is human review's call (Chapters 37, 1).
 *
 * <p>This gate composes; it does not run the checks. The stages that <em>produce</em> outcomes (the
 * analyzers, the test/coverage run, the dependency and security scans) run in the build and the
 * pipeline; the runnable, unit-tested part here is the composition that consumes their outcomes and
 * returns the verdict.
 */
public final class ReferenceGate {

    private final GateLadder ladder;
    private final AtomicLong evaluations = new AtomicLong();
    private final AtomicLong noShips = new AtomicLong();

    /**
     * Creates a gate that composes under the given externalized ladder.
     *
     * @param ladder the enforce-from, clean-as-you-code, and block-severity ladder, never {@code null}
     */
    public ReferenceGate(GateLadder ladder) {
        this.ladder = Objects.requireNonNull(ladder, "ladder");
    }

    /**
     * Composes the stage outcomes of one pipeline run into a single ship / no-ship verdict.
     *
     * @param outcomes the outcome each stage of the run produced, never {@code null}
     * @return the composed verdict — ship or no-ship — never {@code null}
     */
    public ShipVerdict evaluate(List<StageOutcome> outcomes) {
        Objects.requireNonNull(outcomes, "outcomes");
        evaluations.incrementAndGet();
        // tag::compose-verdict[]
        List<StageOutcome> blocking = outcomes.stream()
            .filter(o -> !o.cleared())                                   // a finding was raised
            .filter(o -> ladder.enforces(o.stage()))                    // at an enforced stage
            .filter(o -> !ladder.cleanAsYouCode() || o.onNewCode())     // on new code (or whole-repo)
            .filter(o -> o.topSeverity().compareTo(ladder.blockSeverity()) >= 0)  // severe enough
            .toList();
        if (blocking.isEmpty()) {
            return new ShipVerdict.Ship("no blocking findings at or above " + ladder.blockSeverity());
        }
        // end::compose-verdict[]
        noShips.incrementAndGet();
        StageOutcome first = blocking.get(0);
        String reason = "no-ship: " + blocking.size() + " blocking stage(s); first is "
            + first.stage() + " (" + first.topSeverity() + ")";
        return new ShipVerdict.NoShip(reason, blocking);
    }

    /**
     * The number of evaluations whose verdict was no-ship — the gate's headline health metric. A
     * dashboard trends this the way it trends pipeline duration: a no-ship rate stuck at zero may mean
     * the ladder is too loose, and one stuck high may mean it is too strict or too noisy (Chapter 19).
     *
     * @return the running count of no-ship verdicts this gate has returned
     */
    public long noShipCount() {
        return noShips.get();
    }

    /**
     * Whether the gate is ready to compose a verdict — a readiness probe over its wired ladder. A gate
     * with no ladder could only fail open (ship everything), the silent way a gate stops gating, so an
     * unconfigured gate reports not-ready rather than waving changes through.
     *
     * @return {@code true} once a ladder is wired, the readiness signal a health endpoint would expose
     */
    public boolean isReady() {
        return ladder != null;
    }
}
