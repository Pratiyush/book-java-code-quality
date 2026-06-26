package org.acme.supplychain;

import java.io.Serial;
import java.util.List;

/**
 * The failure path in code: thrown when a dependency scan turns up a finding at or above the gate's CVSS
 * threshold that is reachable and not covered by a reviewed suppression. It carries the offending findings
 * so the build output names exactly what blocked it — the in-code analogue of OWASP Dependency-Check
 * failing the build above {@code failBuildOnCVSS} (the {@code -Pscan} profile in {@code pom.xml}).
 *
 * <p>This is the chapter's HONEST-LIMITATIONS floor as a runnable path, not a comment: a clean gate is not
 * a clean bill of health, and a blocking finding stops the build until it is fixed (the bump the update
 * bot would raise) or suppressed with a recorded justification.
 */
public final class UnsuppressedHighSeverityFindingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final transient List<ScanFinding> blockingFindings;

    /**
     * @param blockingFindings the reachable, at-or-above-threshold, unsuppressed findings that failed the gate
     */
    public UnsuppressedHighSeverityFindingException(List<ScanFinding> blockingFindings) {
        super(message(blockingFindings));
        this.blockingFindings = List.copyOf(blockingFindings);
    }

    private static String message(List<ScanFinding> findings) {
        return "dependency scan gate failed: %d blocking finding(s): %s"
                .formatted(findings.size(), findings.stream().map(ScanFinding::identifier).toList());
    }

    /**
     * @return an unmodifiable list of the findings that blocked the gate
     */
    public List<ScanFinding> blockingFindings() {
        return blockingFindings == null ? List.of() : blockingFindings;
    }
}
