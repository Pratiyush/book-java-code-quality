package org.acme.maturity;

import java.util.Objects;

/**
 * One team's honest rating of one {@link Dimension}: the {@link Stage} it has reached, and whether the
 * outcomes that dimension is supposed to improve are actually improving. The two fields encode the
 * chapter's central distinction — between adopting practices and getting the outcomes they exist for.
 * A high stage with poor outcomes is the vanity-ladder trap the chapter exists to prevent: the boxes are
 * ticked but the code is not easier to change, so the assessment must not reward it.
 *
 * <p>An immutable value: a rating is a fact about where a team stands at a point in time. The
 * {@code outcomeImproving} flag is the antidote to maturity-as-a-badge — a dimension counts as real
 * progress only when adopting its practices moved an outcome the team cares about (fewer incidents, safer
 * delivery, a more changeable codebase), never merely because a tool was installed.
 *
 * @param dimension        the dimension this rates, never {@code null}
 * @param stage            the stage the team has reached on this dimension, never {@code null}
 * @param outcomeImproving whether the outcomes this dimension targets are actually improving
 * @param note             a short human-readable note on the rating, never {@code null}
 */
public record DimensionRating(Dimension dimension, Stage stage, boolean outcomeImproving, String note) {

    /** Compact constructor: a rating always names its dimension, its stage, and a note. */
    public DimensionRating {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(stage, "stage");
        Objects.requireNonNull(note, "note");
    }

    /**
     * Records a rating whose outcomes are improving — practices adopted on this dimension are moving the
     * outcomes they exist for, so the stage reflects real progress rather than a ticked box.
     *
     * @param dimension the dimension being rated, never {@code null}
     * @param stage     the stage reached on this dimension, never {@code null}
     * @param note      a short human-readable note, never {@code null}
     * @return a rating marked outcome-aligned, never {@code null}
     */
    public static DimensionRating improving(Dimension dimension, Stage stage, String note) {
        return new DimensionRating(dimension, stage, true, note);
    }

    /**
     * Records a rating whose practices are adopted but whose outcomes are not improving — the vanity-ladder
     * signal: the stage looks advanced, but the code or the work is no better, so the assessment treats the
     * dimension as not having delivered the outcome the stage is supposed to indicate (the Goodhart trap).
     *
     * @param dimension the dimension being rated, never {@code null}
     * @param stage     the stage reached on this dimension, never {@code null}
     * @param note      a short human-readable note, never {@code null}
     * @return a rating marked not outcome-aligned, never {@code null}
     */
    public static DimensionRating stalled(Dimension dimension, Stage stage, String note) {
        return new DimensionRating(dimension, stage, false, note);
    }

    /**
     * The stage this rating counts toward the overall maturity picture. A dimension whose outcomes are not
     * improving does not count its claimed stage past {@link Stage#FOUNDATIONS}: climbing for the badge is
     * not progress, so a stalled dimension is treated as no further along than the foundations it stands on.
     * This is the rule that stops a team from reporting a high overall level by installing tools whose
     * outcomes never landed.
     *
     * @return the stage that counts toward maturity — the claimed stage when outcomes improve, otherwise
     *     {@link Stage#FOUNDATIONS}
     */
    public Stage effectiveStage() {
        return outcomeImproving ? stage : Stage.FOUNDATIONS;
    }
}
