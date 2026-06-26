package org.acme.findings;

import java.util.List;
import java.util.Objects;

/**
 * A health view over the gate's "debt about debt" (Chapter 39). Suppressions and baselines are claims the
 * team made at one moment, and they rot — so the chapter argues they must stay VISIBLE and be reviewed,
 * not accumulate silently. This type is the observability surface for that: a readable readiness signal
 * reporting how much of the gate is currently being silenced, so a stale, growing suppression set shows up
 * instead of hiding.
 *
 * <p>It is a reporting surface, not a second gate. {@code READY} means no new findings escaped the ratchet
 * AND the silenced count is within the team's agreed budget; {@code DEGRADED} means the gate is still
 * green but the silenced set has grown past that budget and is due for review — the early signal that the
 * gate is drifting toward theatre. It computes a snapshot; it never changes a verdict.
 *
 * @param status        the readiness status (READY or DEGRADED)
 * @param baselinedCount how many findings are frozen in the baseline
 * @param suppressedCount how many findings are suppressed at a site with a recorded reason
 * @param newFindings   the new findings that escaped the ratchet (empty when the gate is green)
 */
public record GateHealth(
        Status status,
        int baselinedCount,
        int suppressedCount,
        List<Finding> newFindings) {

    /** Readiness of the gate's silenced-debt surface. */
    public enum Status { READY, DEGRADED }

    /** Validates and defensively copies the new-findings list at construction. */
    public GateHealth {
        Objects.requireNonNull(status, "status");
        newFindings = List.copyOf(Objects.requireNonNull(newFindings, "newFindings"));
    }

    /**
     * Builds the health snapshot from the ratchet, the reviewed suppressions, the current findings, and
     * the team's silenced-debt budget.
     *
     * @param ratchet         the baseline ratchet (its size is the frozen count)
     * @param suppressions    the reviewed per-site suppressions, each carrying a reason
     * @param current         the current findings from a fresh analysis run
     * @param silencedBudget  the largest silenced set the team agrees is healthy
     * @return the snapshot; DEGRADED if silenced debt exceeds the budget, else READY
     */
    // tag::gate-health[]
    public static GateHealth report(FindingRatchet ratchet, List<Finding> suppressions,
            List<Finding> current, int silencedBudget) {
        List<Finding> escaped = ratchet.newFindings(current);
        int silenced = ratchet.baselineSize() + suppressions.size();
        Status status = silenced > silencedBudget ? Status.DEGRADED : Status.READY;  // visible, not hidden
        return new GateHealth(status, ratchet.baselineSize(), suppressions.size(), escaped);
    }
    // end::gate-health[]

    /** The total silenced finding count — the number that must trend down, not up. */
    public int silencedTotal() {
        return baselinedCount + suppressedCount;
    }
}
