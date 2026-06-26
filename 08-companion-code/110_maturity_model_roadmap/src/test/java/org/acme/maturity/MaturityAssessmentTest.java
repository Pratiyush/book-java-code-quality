package org.acme.maturity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the maturity assessment: it drives the composition through the whole model — the
 * externalized policy ({@link RoadmapPolicy#load}) and a team's per-dimension ratings — and asserts each
 * behaviour the final chapter describes, end to end. The ratings are fixtures, hand-built to land in a
 * specific recommendation (advance / restore-outcomes / sustain); the composition logic under test is
 * real.
 *
 * <p>Together these cases pin the chapter's central honesty: the overall level is the lowest dimension's
 * stage and never an average (so a wall of green cannot hide a fire), the next step starts where the pain
 * is rather than at the next rung, a high stage with stalled outcomes is discounted rather than rewarded
 * (the vanity-ladder guard), and past a threshold the model recommends sustaining rather than climbing.
 * They also confirm the dev and prod profiles differ, the way a framework's {@code %dev} / {@code %prod}
 * blocks do.
 */
class MaturityAssessmentTest {

    // The prod policy is the strict one: outcomes required, sustain from GOVERN_OBSERVE.
    private final MaturityAssessment assessment = new MaturityAssessment(RoadmapPolicy.load("prod"));

    private static DimensionRating improvingAt(Dimension dimension, Stage stage) {
        return DimensionRating.improving(dimension, stage, "outcomes improving on " + dimension);
    }

    @Test
    @DisplayName("the overall level is the lowest dimension's stage, never an average that hides a fire")
    void overallLevelIsTheLowestDimension() {
        // Five dimensions are advanced and one is barely started. An average would report a comfortable
        // middle level; the model reports the floor, so the team sees the fire instead of a wall of green.
        List<DimensionRating> ratings = List.of(
            improvingAt(Dimension.FOUNDATIONS_BUILD, Stage.SUSTAIN_EVOLVE),
            improvingAt(Dimension.STATIC_ANALYSIS_GATE, Stage.GOVERN_OBSERVE),
            improvingAt(Dimension.TESTING_COVERAGE, Stage.DEEPEN),
            improvingAt(Dimension.SECURITY_SUPPLY_CHAIN, Stage.FOUNDATIONS),
            improvingAt(Dimension.ARCHITECTURE_GOVERNANCE, Stage.DEEPEN),
            improvingAt(Dimension.CULTURE_KNOWLEDGE, Stage.GOVERN_OBSERVE));

        assertThat(assessment.overallLevel(ratings)).isEqualTo(Stage.FOUNDATIONS);
    }

    @Test
    @DisplayName("start where the pain is: the next step advances the lowest, most painful dimension")
    void recommendsAdvancingTheLowestDimension() {
        List<DimensionRating> ratings = List.of(
            improvingAt(Dimension.FOUNDATIONS_BUILD, Stage.DEEPEN),
            improvingAt(Dimension.STATIC_ANALYSIS_GATE, Stage.DEEPEN),
            improvingAt(Dimension.SECURITY_SUPPLY_CHAIN, Stage.GATE_BASICS));

        NextStep step = assessment.recommend(ratings);

        assertThat(step).isInstanceOf(NextStep.Advance.class);
        NextStep.Advance advance = (NextStep.Advance) step;
        assertThat(advance.focus()).isEqualTo(Dimension.SECURITY_SUPPLY_CHAIN);
        assertThat(advance.from()).isEqualTo(Stage.GATE_BASICS);
        assertThat(advance.to()).isEqualTo(Stage.DEEPEN);
    }

    @Test
    @DisplayName("the vanity-ladder guard: a high stage with stalled outcomes is told to make it pay, not climb")
    void recommendsRestoringOutcomesBeforeClimbing() {
        // STATIC_ANALYSIS_GATE claims an advanced stage, but its outcomes are not improving — the boxes are
        // ticked and the code is no better. The model refuses to recommend climbing anywhere and instead
        // names the stalled dimension: make the practice pay before adding more. This is the failure path.
        List<DimensionRating> ratings = List.of(
            improvingAt(Dimension.FOUNDATIONS_BUILD, Stage.GATE_BASICS),
            DimensionRating.stalled(Dimension.STATIC_ANALYSIS_GATE, Stage.GOVERN_OBSERVE,
                "every gate installed, but the codebase is no easier to change"));

        NextStep step = assessment.recommend(ratings);

        assertThat(step).isInstanceOf(NextStep.RestoreOutcomes.class);
        NextStep.RestoreOutcomes restore = (NextStep.RestoreOutcomes) step;
        assertThat(restore.focus()).isEqualTo(Dimension.STATIC_ANALYSIS_GATE);
        assertThat(restore.claimed()).isEqualTo(Stage.GOVERN_OBSERVE);
    }

    @Test
    @DisplayName("a stalled dimension is discounted, so it also drops the overall level to its floor")
    void stalledDimensionIsDiscountedInTheLevel() {
        // The same stalled high-stage dimension counts only as FOUNDATIONS under the prod policy, so it
        // cannot prop up the overall level: climbing for the badge is not progress (Chapters 1, 38).
        List<DimensionRating> ratings = List.of(
            improvingAt(Dimension.FOUNDATIONS_BUILD, Stage.GOVERN_OBSERVE),
            DimensionRating.stalled(Dimension.SECURITY_SUPPLY_CHAIN, Stage.SUSTAIN_EVOLVE, "no outcome behind it"));

        assertThat(assessment.overallLevel(ratings)).isEqualTo(Stage.FOUNDATIONS);
    }

    @Test
    @DisplayName("more maturity is not more value: past the sustain threshold the model says sustain, not climb")
    void recommendsSustainPastTheThreshold() {
        // Every dimension is at or past GOVERN_OBSERVE (the prod sustain threshold) with outcomes improving.
        // The next valuable move is to keep the practice going and subtract gates that no longer pay, not to
        // add more governance — over-governance ossifies (Chapters 26, 33).
        List<DimensionRating> ratings = List.of(
            improvingAt(Dimension.FOUNDATIONS_BUILD, Stage.SUSTAIN_EVOLVE),
            improvingAt(Dimension.STATIC_ANALYSIS_GATE, Stage.GOVERN_OBSERVE),
            improvingAt(Dimension.TESTING_COVERAGE, Stage.GOVERN_OBSERVE),
            improvingAt(Dimension.SECURITY_SUPPLY_CHAIN, Stage.GOVERN_OBSERVE),
            improvingAt(Dimension.ARCHITECTURE_GOVERNANCE, Stage.SUSTAIN_EVOLVE),
            improvingAt(Dimension.CULTURE_KNOWLEDGE, Stage.GOVERN_OBSERVE));

        assertThat(assessment.recommend(ratings)).isInstanceOf(NextStep.Sustain.class);
    }

    @Test
    @DisplayName("the dev and prod profiles differ: dev counts a stalled stage that prod discounts")
    void devAndProdProfilesDiffer() {
        // Under dev (require-outcomes off), a stalled advanced stage counts as recorded, so the level is the
        // recorded floor and the recommendation just advances the lowest dimension. Under prod the SAME
        // stalled dimension is discounted to FOUNDATIONS and triggers restore-outcomes. The profile is the
        // only difference — the knob a team turns as it matures from "just show me where I am" to
        // "I measure outcomes now".
        List<DimensionRating> ratings = List.of(
            improvingAt(Dimension.FOUNDATIONS_BUILD, Stage.DEEPEN),
            DimensionRating.stalled(Dimension.TESTING_COVERAGE, Stage.GOVERN_OBSERVE, "coverage up, escapes flat"));

        MaturityAssessment devAssessment = new MaturityAssessment(RoadmapPolicy.load("dev"));
        assertThat(devAssessment.overallLevel(ratings)).isEqualTo(Stage.DEEPEN);
        assertThat(devAssessment.recommend(ratings)).isInstanceOf(NextStep.Advance.class);

        // Prod discounts the stalled dimension and recommends restoring its outcomes instead.
        assertThat(assessment.overallLevel(ratings)).isEqualTo(Stage.FOUNDATIONS);
        assertThat(assessment.recommend(ratings)).isInstanceOf(NextStep.RestoreOutcomes.class);
    }

    @Test
    @DisplayName("the roadmap view lists every stage in default order, each with its practices")
    void roadmapNamesEveryStageAndItsPractices() {
        assertThat(Roadmap.stages()).containsExactly(Stage.values());
        for (Stage stage : Roadmap.stages()) {
            assertThat(stage.practices()).isNotBlank();
            assertThat(Roadmap.describe(stage)).contains("a default not a rung");
        }
    }

    @Test
    @DisplayName("observability: the model counts assessments and trends the lowest dimension stage")
    void observabilitySurfaceTracksAssessments() {
        assertThat(assessment.assessmentsRun()).isZero();
        assertThat(assessment.lowestDimensionStageOrder()).isEqualTo(-1);

        assessment.recommend(List.of(improvingAt(Dimension.FOUNDATIONS_BUILD, Stage.GATE_BASICS)));

        assertThat(assessment.assessmentsRun()).isEqualTo(1);
        assertThat(assessment.lowestDimensionStageOrder()).isEqualTo(Stage.GATE_BASICS.order());
    }

    @Test
    @DisplayName("the model reports ready once a policy is wired")
    void readinessReflectsWiredPolicy() {
        assertThat(assessment.isReady()).isTrue();
    }

    @Test
    @DisplayName("an empty assessment is rejected — there is no level without a dimension")
    void emptyRatingsAreRejected() {
        assertThatThrownBy(() -> assessment.overallLevel(List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("an unknown profile is rejected at load")
    void unknownProfileIsRejected() {
        assertThatThrownBy(() -> RoadmapPolicy.load("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("a dimension rating requires its dimension, stage, and note")
    void dimensionRatingRejectsNullComponents() {
        assertThatThrownBy(() -> DimensionRating.improving(null, Stage.FOUNDATIONS, "x"))
            .isInstanceOf(NullPointerException.class);
    }
}
