package org.acme.maturity;

/**
 * The single next step a maturity assessment recommends. The chapter's point is that a team does not need
 * a hundred-row audit; it needs to know the one most valuable thing to do next, chosen by where the pain
 * actually is rather than by marching to the next stage on a ladder. So the assessment reduces a whole
 * dimension-by-dimension picture to one actionable recommendation, the way the gate chapter reduces a run
 * to one ship / no-ship decision.
 *
 * <p>There are three variants, and the asymmetry carries the chapter's honesty. {@code Advance} is the
 * ordinary case — work the lowest, most painful dimension up a stage, starting where the pain is.
 * {@code RestoreOutcomes} is the vanity-ladder case — a dimension claims an advanced stage but its
 * outcomes are not improving, so the next step is to make that practice actually pay before climbing
 * anywhere, never to add more. {@code Sustain} is the past-the-point case — the team is mature enough that
 * the next valuable move is to keep the practice going and subtract gates that no longer pay, because more
 * maturity is not more value past a point.
 */
// tag::next-step[]
public sealed interface NextStep
    permits NextStep.Advance, NextStep.RestoreOutcomes, NextStep.Sustain {

    /** Work the lowest, most painful dimension up one stage — start where the pain is, not the next rung. */
    record Advance(Dimension focus, Stage from, Stage to, String reason) implements NextStep { }
    // end::next-step[]

    /**
     * A dimension claims an advanced stage but its outcomes are not improving — make that practice pay
     * before climbing. The vanity-ladder antidote: this never recommends adding a stage; it recommends
     * landing the outcome the already-adopted practice was supposed to deliver.
     */
    record RestoreOutcomes(Dimension focus, Stage claimed, String reason) implements NextStep { }

    /**
     * Every dimension is at or past the policy's sustain threshold with outcomes improving — keep the
     * practice going and subtract gates that no longer pay, rather than adding more governance for its own
     * sake. The chapter's "there is no done, only continuous improvement" stated as a recommendation.
     */
    record Sustain(String reason) implements NextStep { }
}
