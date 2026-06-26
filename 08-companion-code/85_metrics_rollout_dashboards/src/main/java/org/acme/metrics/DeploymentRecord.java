package org.acme.metrics;

import java.time.Duration;
import java.time.Instant;

/**
 * One deployment event — the raw, system-level fact the DORA four keys are computed from. A delivery
 * pipeline already records these (when a change deployed, how long it took from commit to production,
 * whether it caused a failure, how long any resulting incident took to recover). DORA is derived from
 * the stream of these events, never declared by hand.
 *
 * <p>Every field is a team-or-system fact about a <em>change</em>, not an attribution to a person: there
 * is deliberately no author here. That is the chapter's "metrics measure the system, not people" rule
 * expressed in the data model — there is nothing to rank an individual by.
 *
 * @param deployedAt   when the change reached production
 * @param leadTime     time from code committed to that change running in production (lead time for
 *                     changes), never negative
 * @param causedFailure whether this deployment caused a failure in production (a rollback, hotfix, or
 *                     incident) — the input to the change-failure rate
 * @param recoveryTime if {@code causedFailure}, how long service took to recover; {@link Duration#ZERO}
 *                     otherwise — the input to failed-deployment recovery time
 */
public record DeploymentRecord(
        Instant deployedAt, Duration leadTime, boolean causedFailure, Duration recoveryTime) {

    /** Compact constructor: the durations are facts about elapsed time, so negatives are rejected. */
    public DeploymentRecord {
        if (deployedAt == null) {
            throw new IllegalArgumentException("deployedAt must not be null");
        }
        if (leadTime == null || leadTime.isNegative()) {
            throw new IllegalArgumentException("leadTime must be a non-negative duration");
        }
        if (recoveryTime == null || recoveryTime.isNegative()) {
            throw new IllegalArgumentException("recoveryTime must be a non-negative duration");
        }
        if (!causedFailure && !recoveryTime.isZero()) {
            throw new IllegalArgumentException("recoveryTime must be zero when no failure was caused");
        }
    }

    /** A successful deployment: it caused no failure, so its recovery time is zero. */
    public static DeploymentRecord successful(Instant deployedAt, Duration leadTime) {
        return new DeploymentRecord(deployedAt, leadTime, false, Duration.ZERO);
    }

    /** A failed deployment that took {@code recoveryTime} to recover from. */
    public static DeploymentRecord failed(Instant deployedAt, Duration leadTime, Duration recoveryTime) {
        return new DeploymentRecord(deployedAt, leadTime, true, recoveryTime);
    }
}
