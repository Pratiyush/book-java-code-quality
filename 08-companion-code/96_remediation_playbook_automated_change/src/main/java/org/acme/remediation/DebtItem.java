package org.acme.remediation;

import java.util.Objects;

/**
 * One unit of technical debt in the inherited codebase — a class, a module, or a subsystem — described by
 * the two dimensions the chapter prioritizes on: how much it <em>changes</em> and how much it
 * <em>hurts</em> to change.
 *
 * <p>{@code churn} is a proxy for how often the area is touched (Chapter 2 / the metrics chapters): a high
 * number means active development, a zero means frozen code. {@code complexity} is a proxy for how painful
 * each change is. The chapter's rule reads directly off the product of the two: debt in frozen code (churn
 * near zero) accrues no interest, however high its complexity, because no one pays to read or change it;
 * the same complexity in a weekly-edited file costs every developer every week.
 *
 * @param name        a stable identifier for the area (a class or subsystem name)
 * @param churn       how often the area changes — commits or edits over the window; zero means frozen
 * @param complexity  how painful a change is — a complexity proxy on a positive scale
 * @param underTest   whether the area already has a safety net (characterization tests, Chapter 39)
 */
public record DebtItem(String name, int churn, int complexity, boolean underTest) {

    /** Validates the item; both proxies are non-negative and the name is required. */
    public DebtItem {
        Objects.requireNonNull(name, "name");
        if (churn < 0) {
            throw new IllegalArgumentException("churn must be >= 0, was " + churn);
        }
        if (complexity < 0) {
            throw new IllegalArgumentException("complexity must be >= 0, was " + complexity);
        }
    }

    /**
     * The interest this debt actually accrues — the chapter's churn &times; pain score. Frozen code (churn
     * zero) scores zero however complex it is, which is precisely why the playbook declines to remediate it.
     *
     * @return the prioritization score; higher means remediate sooner
     */
    // tag::churn-pain[]
    public long interest() {
        return (long) churn * complexity;   // debt in frozen code (churn 0) accrues no interest
    }
    // end::churn-pain[]
}
