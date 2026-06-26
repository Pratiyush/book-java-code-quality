package org.acme.remediation;

/**
 * A health view over the remediation <em>program</em> (Chapter 40). The chapter's hardest claim is
 * sociotechnical: remediation succeeds or fails on sustained commitment far more than on tooling. A
 * program that loses attention stalls in the half-strangled state — the worst of both worlds, the old
 * system not yet replaced and the new one not yet finished. This surface is the readiness signal for that:
 * it reports whether the debt trend is actually moving, so a stalled program shows up instead of being
 * mistaken for a healthy one.
 *
 * <p>It is a reporting surface, not a gate. {@code SUSTAINED} means debt is trending down at or beyond the
 * pace the team committed to; {@code STALLING} means the program is still nominally running but the trend
 * has flattened below that pace — the early signal that attention is drifting and the paydown is falling
 * behind the new debt. It computes a snapshot; it never changes a verdict.
 *
 * @param status        the program's readiness (SUSTAINED or STALLING)
 * @param openHotspots  how many hotspots remain unremediated
 * @param closedThisCycle how many hotspots were paid down in the current cycle
 */
public record ProgramHealth(Status status, int openHotspots, int closedThisCycle) {

    /** Readiness of the remediation program's trend. */
    public enum Status { SUSTAINED, STALLING }

    /**
     * Builds the program-health snapshot. The program is STALLING when fewer hotspots were closed this
     * cycle than the team's agreed minimum pace — the trend has flattened and the program is at risk of
     * the half-strangled state, even though it has not formally stopped.
     *
     * @param openHotspots    hotspots still open
     * @param closedThisCycle hotspots paid down this cycle
     * @param minPace         the smallest per-cycle paydown the team agrees keeps the program honest
     * @return the snapshot; STALLING below the agreed pace, else SUSTAINED
     */
    // tag::program-health[]
    public static ProgramHealth report(int openHotspots, int closedThisCycle, int minPace) {
        Status status = closedThisCycle < minPace ? Status.STALLING : Status.SUSTAINED;  // visible, not hidden
        return new ProgramHealth(status, openHotspots, closedThisCycle);
    }
    // end::program-health[]
}
