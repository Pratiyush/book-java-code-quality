package org.acme.storefront.money;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The observability surface for the money-representation rules: a readiness probe over the loaded policy
 * and a running count of breaches the rules have reported. A custom rule's natural observable signal is
 * its finding stream — the same place a stock analyzer's report shows up, which a dashboard consumes
 * (Chapter 45). This type makes that signal a small in-process seam the later observability chapter can
 * build on, rather than something the build only writes to a report file.
 */
public final class MoneyPolicyHealth {

    private final MoneyPolicy policy;
    private final AtomicLong reportedViolations = new AtomicLong();

    /**
     * @param policy the active policy this surface reports on, never {@code null}
     */
    public MoneyPolicyHealth(MoneyPolicy policy) {
        this.policy = java.util.Objects.requireNonNull(policy, "policy");
    }

    /**
     * Records a batch of reported breaches into the running count.
     *
     * @param violations the breaches to count, never {@code null}
     * @return the new total
     */
    public long record(List<MoneyViolation> violations) {
        return reportedViolations.addAndGet(violations.size());
    }

    /** @return the number of breaches reported through this surface since start */
    public long reportedViolationCount() {
        return reportedViolations.get();
    }

    /**
     * Readiness: the rules are ready to run once a policy is loaded and its severity is resolved.
     *
     * @return {@code true} when the policy is usable
     */
    public boolean isReady() {
        return policy.severity() != null;
    }
}
