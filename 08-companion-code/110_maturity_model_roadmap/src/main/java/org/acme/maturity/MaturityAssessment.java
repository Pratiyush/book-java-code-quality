package org.acme.maturity;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The maturity assessment: it composes a team's per-dimension ratings into an overall maturity level and
 * the one next step worth taking. Where the chapter's figure shows the staged roadmap, this is the
 * runnable model of it — it consumes where a team honestly stands on each {@link Dimension} and returns
 * where to start, under the externalized {@link RoadmapPolicy}.
 *
 * <p>Two design choices encode the chapter's hardest limitations directly, so the model cannot be read as
 * a vanity ladder. First, the overall level is the <em>lowest</em> counted stage across the dimensions,
 * never an average: an average lets a wall of green on five dimensions hide a fire on the sixth, which is
 * exactly the "we're Level 4 and still miserable" trap; the minimum keeps the team's real floor visible.
 * Second, the next step targets the lowest, most painful dimension — start where the pain is, not the next
 * rung — and refuses to recommend climbing when a dimension's outcomes have stalled or when the team is
 * already past the policy's sustain threshold.
 *
 * <p>This model assesses; it does not run any check. The practices that <em>produce</em> a dimension's
 * stage (the analyzers, the tests, the security scans, the review culture) live in their own chapters and
 * the team's own pipeline; the runnable, unit-tested part here is the composition that turns an honest
 * self-assessment into a level and a next step.
 */
public final class MaturityAssessment {

    private final RoadmapPolicy policy;
    private final AtomicLong assessments = new AtomicLong();
    private volatile int lowestStageOrder = -1;

    /**
     * Creates an assessment that composes under the given externalized policy.
     *
     * @param policy the require-outcomes and sustain-at-stage policy, never {@code null}
     */
    public MaturityAssessment(RoadmapPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * The overall maturity level for a set of dimension ratings: the lowest stage that counts under the
     * policy, across every rated dimension. The minimum, not the average, is the chapter's discipline — a
     * team is only as mature as its weakest dimension, and reporting an average would hide the very gap a
     * team most needs to see.
     *
     * @param ratings the team's per-dimension ratings, never {@code null} or empty
     * @return the overall maturity level, never {@code null}
     * @throws IllegalArgumentException if {@code ratings} is empty
     */
    public Stage overallLevel(List<DimensionRating> ratings) {
        requireNonEmpty(ratings);
        // tag::overall-level[]
        return ratings.stream()
            .map(policy::countedStage)        // a stalled dimension is discounted (anti-vanity)
            .min(Comparator.comparingInt(Stage::order))   // the LOWEST, never an average
            .orElseThrow();                   // ratings is non-empty, checked above
        // end::overall-level[]
    }

    /**
     * Composes the team's ratings into the one next step worth taking. The order of the checks is the
     * chapter's priority: a stalled outcome on an advanced dimension is addressed before any climbing (do
     * not chase the badge); otherwise, if every dimension is at or past the sustain threshold with outcomes
     * improving, sustain rather than add; otherwise, advance the lowest, most painful dimension by one
     * stage — start where the pain is.
     *
     * @param ratings the team's per-dimension ratings, never {@code null} or empty
     * @return the recommended next step, never {@code null}
     * @throws IllegalArgumentException if {@code ratings} is empty
     */
    public NextStep recommend(List<DimensionRating> ratings) {
        requireNonEmpty(ratings);
        assessments.incrementAndGet();
        recordLowest(overallLevel(ratings));

        // tag::recommend[]
        return ratings.stream()
            .filter(r -> policy.requireOutcomes() && !r.outcomeImproving()
                && r.stage().order() > Stage.FOUNDATIONS.order())
            .findFirst()
            .<NextStep>map(r -> new NextStep.RestoreOutcomes(r.dimension(), r.stage(),
                "outcomes on " + r.dimension() + " have not improved — make it pay before climbing"))
            .orElseGet(() -> advanceOrSustain(ratings));
        // end::recommend[]
    }

    private NextStep advanceOrSustain(List<DimensionRating> ratings) {
        DimensionRating weakest = ratings.stream()
            .min(Comparator.comparingInt(r -> policy.countedStage(r).order()))
            .orElseThrow();
        Stage at = policy.countedStage(weakest);
        if (at.order() >= policy.sustainAtStage().order()) {
            return new NextStep.Sustain(
                "every dimension is at or past " + policy.sustainAtStage()
                + " with outcomes improving — sustain, and subtract gates that no longer pay");
        }
        return new NextStep.Advance(weakest.dimension(), at, at.next(),
            "start where the pain is: " + weakest.dimension() + " is the lowest dimension, at " + at);
    }

    /**
     * The number of assessments run — the model's headline health metric. A dashboard trends this and the
     * lowest-dimension stage together: assessment is itself a practice (measure to find where the pain is),
     * and a team that has stopped assessing has stopped improving (Chapter 38).
     *
     * @return the running count of assessments this model has composed
     */
    public long assessmentsRun() {
        return assessments.get();
    }

    /**
     * The order of the lowest dimension stage seen in the most recent assessment, or {@code -1} before any
     * assessment has run — the trend a dashboard watches climb over time as a team works its weakest
     * dimension up. It is the floor, not the average, deliberately: the metric a team improves by raising
     * its lowest dimension, not by adding more to its strongest.
     *
     * @return the lowest stage order from the last assessment, or {@code -1} if none has run
     */
    public int lowestDimensionStageOrder() {
        return lowestStageOrder;
    }

    /**
     * Whether the model is ready to compose a recommendation — a readiness probe over its wired policy. A
     * model with no policy could not decide what counts as progress, so an unconfigured model reports
     * not-ready rather than returning a meaningless level.
     *
     * @return {@code true} once a policy is wired, the readiness signal a health endpoint would expose
     */
    public boolean isReady() {
        return policy != null;
    }

    private void recordLowest(Stage lowest) {
        lowestStageOrder = lowest.order();
    }

    private static void requireNonEmpty(List<DimensionRating> ratings) {
        if (Objects.requireNonNull(ratings, "ratings").isEmpty()) {
            throw new IllegalArgumentException("at least one dimension rating is required");
        }
    }
}
