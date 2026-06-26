package org.acme.findings;

import java.util.Objects;

/**
 * One static-analysis finding, reduced to the facts a triage decision needs (Chapter 39).
 *
 * <p>A finding carries the analyzer's identity ({@code tool}, {@code ruleKey}, e.g. {@code "SpotBugs"} /
 * {@code "EI_EXPOSE_REP"}) and where it is ({@code location}). The three judgment fields are the ones the
 * tool itself cannot decide: {@code preExisting} (does it predate the gate?), {@code falsePositive} (has
 * the team examined it and judged the tool wrong, or the cost accepted?), and {@code justification} (the
 * recorded reason a suppression carries — the evidence without which a suppression is indistinguishable
 * from hiding a bug).
 *
 * <p>The record is immutable and validates at construction: a blank tool, rule key, or location fails
 * fast, and a finding marked a false positive with no justification is rejected — the type refuses to
 * represent an unjustified suppression at all (the chapter's "suppression is a claim that needs evidence"
 * made unrepresentable, per the upstream-guarantee approach).
 *
 * @param tool          the analyzer that raised it (e.g. {@code "SpotBugs"}, {@code "Checkstyle"})
 * @param ruleKey       the rule / bug-pattern identifier (e.g. {@code "EI_EXPOSE_REP"})
 * @param location      where the finding is (e.g. a class or {@code Class#method})
 * @param preExisting   whether the finding predates the gate (eligible for a baseline)
 * @param falsePositive whether the team has judged the tool wrong here, or accepted the cost
 * @param justification the recorded reason for a suppression; required when {@code falsePositive} is true
 */
public record Finding(
        String tool,
        String ruleKey,
        String location,
        boolean preExisting,
        boolean falsePositive,
        String justification) {

    /** Validates the finding at construction; an unjustified false positive cannot be represented. */
    public Finding {
        requireText(tool, "tool");
        requireText(ruleKey, "ruleKey");
        requireText(location, "location");
        if (falsePositive && isBlank(justification)) {
            throw new IllegalArgumentException(
                    "a finding judged a false positive must carry a justification: " + ruleKey + " at " + location);
        }
    }

    /** A real, newly introduced defect: not pre-existing, not judged a false positive. */
    public static Finding realDefect(String tool, String ruleKey, String location) {
        return new Finding(tool, ruleKey, location, false, false, null);
    }

    /** A finding the team has examined and judged the tool wrong about (or whose cost it accepts). */
    public static Finding judgedFalsePositive(String tool, String ruleKey, String location, String justification) {
        return new Finding(tool, ruleKey, location, false, true, justification);
    }

    /** A finding that predates the gate — debt eligible to be frozen in a baseline. */
    public static Finding legacy(String tool, String ruleKey, String location) {
        return new Finding(tool, ruleKey, location, true, false, null);
    }

    private static void requireText(String value, String field) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }

    private static boolean isBlank(String value) {
        return Objects.isNull(value) || value.isBlank();
    }
}
