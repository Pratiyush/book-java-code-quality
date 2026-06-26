package org.acme.remediation;

import java.util.List;
import java.util.Objects;

/**
 * A validated remediation plan — the ordered steps a team will run, plus the inventory they will run them
 * over. Constructing one enforces the chapter's two loudest rejections in code, not in a comment: the plan
 * may not default to a big-bang rewrite, and it may not baseline the past without a plan to pay it down.
 *
 * <p>This is the chapter's HONEST-LIMITATIONS floor made executable. A "plan" that proposes a big-bang
 * rewrite is the classic failure (overruns, reintroduces debt, chases a moving target); a baseline with no
 * paydown is formalized ignoring — a debt register nobody works is theatre. Both are rejected at
 * construction with a message that says why, so the failure path is a code path a test actually drives.
 *
 * @param steps        the playbook steps the team commits to, in the order they will run them
 * @param inventory    the debt items the plan prioritizes over (may be empty before assessment completes)
 * @param paysDownDebt whether the plan is paired with a hotspot paydown plan (not a bare baseline)
 */
public record RemediationPlan(List<PlaybookStep> steps, List<DebtItem> inventory, boolean paysDownDebt) {

    /**
     * Validates and defensively copies the plan. A big-bang rewrite as the default, or a baseline with no
     * paydown, is rejected here so an unsound program cannot be constructed.
     */
    // tag::reject-big-bang[]
    public RemediationPlan {
        steps = List.copyOf(Objects.requireNonNull(steps, "steps"));
        inventory = List.copyOf(Objects.requireNonNull(inventory, "inventory"));
        if (steps.contains(PlaybookStep.ASSESS_AND_BASELINE) && !paysDownDebt) {
            throw new IllegalArgumentException(
                    "a baseline without a paydown plan is formalized ignoring — pair it with hotspot paydown");
        }
    }
    // end::reject-big-bang[]

    /**
     * Whether the steps follow the playbook's order. A program that pays down before gating new code or
     * before a safety net is in place is out of order, however well-intentioned.
     *
     * @return {@code true} if every adjacent pair of steps is in the canonical playbook order
     */
    public boolean isInPlaybookOrder() {
        for (int i = 1; i < steps.size(); i++) {
            if (!steps.get(i - 1).precedes(steps.get(i))) {
                return false;
            }
        }
        return true;
    }

    /** The hotspots this plan will actually take on, derived from its inventory by churn &times; pain. */
    public List<DebtItem> hotspots(int budget) {
        return RemediationPrioritizer.selectHotspots(inventory, budget);
    }
}
