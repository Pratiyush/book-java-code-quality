package org.acme.repro;

import java.io.Serial;
import java.util.List;

/**
 * The license gate's failure path in code: thrown when a dependency carries a license outside the allowed
 * policy. It carries the offending entries so the build output names exactly which component and which
 * declared license blocked it — the in-code analogue of the {@code license-maven-plugin} failing the build
 * on a disallowed license (the {@code -Pquality} profile in {@code pom.xml}).
 *
 * <p>This is the chapter's HONEST-LIMITATIONS floor as a runnable path: a disallowed license stops the
 * build until the dependency is removed, replaced, or the policy is deliberately changed with review. The
 * gate reports what was <em>declared</em>; whether a specific obligation actually applies to a specific
 * distribution is a question for legal counsel, not this exception — the gate is factual, not legal advice.
 */
public final class DisallowedLicenseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final transient List<License> disallowed;

    /**
     * @param disallowed the dependency licenses that fell outside the allow policy
     */
    public DisallowedLicenseException(List<License> disallowed) {
        super(message(disallowed));
        this.disallowed = List.copyOf(disallowed);
    }

    private static String message(List<License> disallowed) {
        return "license policy gate failed: %d disallowed license(s): %s"
                .formatted(disallowed.size(),
                        disallowed.stream().map(l -> l.component() + " (" + l.spdxId() + ")").toList());
    }

    /**
     * @return an unmodifiable list of the licenses that failed the policy
     */
    public List<License> disallowed() {
        return disallowed == null ? List.of() : disallowed;
    }
}
