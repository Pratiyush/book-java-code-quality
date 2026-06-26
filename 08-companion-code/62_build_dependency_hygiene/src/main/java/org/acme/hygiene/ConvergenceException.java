package org.acme.hygiene;

import java.util.Objects;

/**
 * Signals that two coordinates with the same {@code groupId:artifactId} key resolved to different
 * versions — the transitive-version conflict the chapter calls a convergence failure.
 *
 * <p>The chapter's point is that this is a <em>hard build event</em>, not a warning to scroll past:
 * unmanaged, Maven's nearest-wins resolution can silently downgrade one of the two, so the honest
 * response is to fail and force a single managed version. This typed exception carries the conflicting
 * key and versions so a caller (or a test) branches on the conflict rather than parsing a message.
 */
public final class ConvergenceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String managementKey;
    private final String existingVersion;
    private final String conflictingVersion;

    /**
     * Creates a convergence failure for one conflicting key.
     *
     * @param managementKey      the {@code groupId:artifactId} that resolved twice, never {@code null}
     * @param existingVersion    the version already recorded for the key, never {@code null}
     * @param conflictingVersion the differing version that triggered the failure, never {@code null}
     */
    public ConvergenceException(String managementKey, String existingVersion, String conflictingVersion) {
        super("dependency convergence failed for " + managementKey + ": "
            + existingVersion + " vs " + conflictingVersion
            + " — pin one version in <dependencyManagement>");
        this.managementKey = Objects.requireNonNull(managementKey, "managementKey");
        this.existingVersion = Objects.requireNonNull(existingVersion, "existingVersion");
        this.conflictingVersion = Objects.requireNonNull(conflictingVersion, "conflictingVersion");
    }

    /** The {@code groupId:artifactId} key whose versions disagreed. */
    public String managementKey() {
        return managementKey;
    }

    /** The version already recorded for the key when the conflict was detected. */
    public String existingVersion() {
        return existingVersion;
    }

    /** The differing version that triggered the failure. */
    public String conflictingVersion() {
        return conflictingVersion;
    }
}
