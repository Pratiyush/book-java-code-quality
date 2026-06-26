package org.acme.refstack;

import java.util.Objects;

/**
 * The outcome one gate stage produced for a change. A stage either cleared or it raised a blocking
 * finding; the finding's severity and scope decide whether it actually blocks under the active ladder,
 * the same two policy axes the rest of the book's gate logic turns on (new-code scope, block severity).
 *
 * <p>An immutable value: a stage outcome is a fact about a point in a run, so it never changes after it
 * is recorded. A {@code clear} outcome carries no finding; a {@code raised} outcome carries the worst
 * finding the stage saw, so the composed verdict can name a real, actionable reason rather than a bare
 * stage name.
 *
 * @param stage          the stage that produced this outcome, never {@code null}
 * @param topSeverity    the worst severity the stage saw, or {@code null} when the stage cleared
 * @param onNewCode      whether the worst finding sits in new/changed code (relevant only when raised)
 * @param detail         a short human-readable detail, never {@code null}
 */
public record StageOutcome(GateStage stage, Severity topSeverity, boolean onNewCode, String detail) {

    /** Compact constructor: the stage and a detail are always required; the severity is null only on clear. */
    public StageOutcome {
        Objects.requireNonNull(stage, "stage");
        Objects.requireNonNull(detail, "detail");
    }

    /**
     * Records a stage that cleared with no finding.
     *
     * @param stage the stage that cleared, never {@code null}
     * @return a clear outcome for the stage, never {@code null}
     */
    public static StageOutcome clear(GateStage stage) {
        return new StageOutcome(stage, null, false, "clear");
    }

    /**
     * Records a stage that raised a finding.
     *
     * @param stage     the stage that raised the finding, never {@code null}
     * @param severity  the worst severity the stage saw, never {@code null}
     * @param onNewCode whether that finding sits in new/changed code
     * @param detail    a short human-readable detail, never {@code null}
     * @return a raised outcome for the stage, never {@code null}
     */
    public static StageOutcome raised(GateStage stage, Severity severity, boolean onNewCode, String detail) {
        return new StageOutcome(stage, Objects.requireNonNull(severity, "severity"), onNewCode, detail);
    }

    /**
     * Whether this stage cleared with no finding.
     *
     * @return {@code true} if the stage produced no finding
     */
    public boolean cleared() {
        return topSeverity == null;
    }
}
