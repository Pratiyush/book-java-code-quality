package org.acme.qualityops.gate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The recorded outcome of evaluating one commit against a policy (Part: immutability). Immutable; the
 * {@code reasons} list is defensively copied to an unmodifiable list in the compact constructor, so a
 * decision handed to one collaborator can never be changed underneath another. A PASS carries an
 * empty reasons list; a FAIL carries one human-readable reason per breached threshold.
 *
 * @param project the project judged
 * @param commit  the commit judged
 * @param passed  whether the commit met the policy
 * @param reasons the breached-threshold explanations; empty on a pass, copied defensively
 */
public record GateDecision(String project, String commit, boolean passed, List<String> reasons) {

    public GateDecision {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(commit, "commit");
        reasons = List.copyOf(reasons); // defensive, immutable copy (also rejects null elements)
    }

    /**
     * Evaluates a project's quality against a policy, collecting one reason per breached threshold.
     * The result is PASS exactly when no threshold is breached.
     */
    public static GateDecision evaluate(String project, String commit, ProjectQuality quality,
                                        GatePolicy policy) {
        List<String> reasons = new java.util.ArrayList<>();
        if (quality.coverage() < policy.minCoverage()) {
            reasons.add("coverage " + quality.coverage() + "% is below the minimum "
                + policy.minCoverage() + "%");
        }
        if (quality.violations() > policy.maxViolations()) {
            reasons.add("violations " + quality.violations() + " exceed the maximum "
                + policy.maxViolations());
        }
        if (quality.bugs() > policy.maxBugs()) {
            reasons.add("bugs " + quality.bugs() + " exceed the maximum " + policy.maxBugs());
        }
        return new GateDecision(project, commit, reasons.isEmpty(), reasons);
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("project", project);
        body.put("commit", commit);
        body.put("decision", passed ? "PASS" : "FAIL");
        body.put("reasons", reasons);
        return body;
    }
}
