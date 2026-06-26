package org.acme.maturity;

import java.util.List;

/**
 * A runnable, stateless view of the staged adoption roadmap: the five stages in their default order, each
 * with the practices it groups. This is the code form of the chapter's figure — a team reads it to see
 * what is available and roughly in what order it tends to make sense, then uses a {@link MaturityAssessment}
 * to decide where its own pain is.
 *
 * <p>The roadmap is a default order, not a ladder to climb for its own sake; that honesty is stated on
 * every stage's description, so the view can never be read as "higher is always better". The order is a
 * teaching device — the pain-first order from the assessment is what a team actually walks.
 */
public final class Roadmap {

    private Roadmap() {
    }

    /**
     * The stages of the roadmap in their default, cheapest-first order.
     *
     * @return the stages from {@link Stage#FOUNDATIONS} to {@link Stage#SUSTAIN_EVOLVE}, never {@code null}
     */
    // tag::roadmap-stages[]
    public static List<Stage> stages() {
        return List.of(Stage.values());     // FOUNDATIONS .. SUSTAIN_EVOLVE, cheapest first
    }
    // end::roadmap-stages[]

    /**
     * Describes one stage: its position in the default order and the practices it groups. The description
     * states that the order is a default, not a rung to climb for its own sake — the carve-out the chapter
     * insists on, carried in the view itself so the roadmap is never presented as a ladder.
     *
     * @param stage the stage to describe, never {@code null}
     * @return a one-line description of the stage and its practices, never {@code null}
     */
    public static String describe(Stage stage) {
        return "stage " + stage.order() + " (" + stage + ", a default not a rung): " + stage.practices();
    }
}
