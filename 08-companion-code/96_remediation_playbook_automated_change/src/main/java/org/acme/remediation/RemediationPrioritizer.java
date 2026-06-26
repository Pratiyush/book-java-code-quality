package org.acme.remediation;

import java.util.Comparator;
import java.util.List;

/**
 * The remediation prioritizer (Chapter 40, step 4 of the playbook): decide <em>what to fix</em> by churn
 * &times; pain, not by the worst absolute metric, and explicitly decline to fix everything.
 *
 * <p>This is what makes remediation finite and defensible. The codebase has more debt than any team can pay
 * down at once; ranking by interest concentrates effort exactly where it returns — the hotspots that are
 * both high-change and high-complexity — and leaves frozen code alone, because debt no one pays to read or
 * change costs nothing to carry. Fixing the worst class by absolute complexity, when no one ever touches
 * it, is effort spent where it never returns.
 */
public final class RemediationPrioritizer {

    private RemediationPrioritizer() {
    }

    /**
     * Ranks debt items by the interest they accrue, highest first. Ties break on name so the order is
     * stable and reproducible (the same input always yields the same plan).
     *
     * @param items the debt inventory from the assessment step
     * @return the items ordered hotspot-first; an empty list in yields an empty list out
     */
    // tag::churn-pain-rank[]
    public static List<DebtItem> rankByInterest(List<DebtItem> items) {
        return items.stream()
                .sorted(Comparator.comparingLong(DebtItem::interest).reversed()
                        .thenComparing(DebtItem::name))
                .toList();   // hotspots (high churn x high complexity) first — not the worst absolute metric
    }
    // end::churn-pain-rank[]

    /**
     * Selects the hotspots worth remediating now: the top {@code budget} items that actually accrue
     * interest. Items with zero interest are dropped even if the budget has room — the playbook's explicit
     * refusal to fix frozen code rather than a shortage of slots.
     *
     * @param items  the debt inventory
     * @param budget how many areas the team can take on this cycle
     * @return the chosen hotspots, hotspot-first, never including a frozen (zero-interest) item
     */
    // tag::frozen-no-interest[]
    public static List<DebtItem> selectHotspots(List<DebtItem> items, int budget) {
        return rankByInterest(items).stream()
                .filter(item -> item.interest() > 0)   // skip frozen code: no interest, so no remediation
                .limit(budget)
                .toList();
    }
    // end::frozen-no-interest[]
}
