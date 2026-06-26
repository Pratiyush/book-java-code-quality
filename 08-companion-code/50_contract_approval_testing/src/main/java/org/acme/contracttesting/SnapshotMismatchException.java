package org.acme.contracttesting;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Raised by {@link SnapshotVerifier} when produced output does not match its approved baseline, or when
 * no baseline exists yet.
 *
 * <p>The message names both files so a developer can open them in a diff tool — the review step that is
 * the whole value of approval testing. It points at the {@code received} file the human inspects and the
 * {@code approved} file they would promote it to once the change is confirmed correct.
 */
public final class SnapshotMismatchException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a mismatch naming the snapshot and the two files involved.
     *
     * @param name     the snapshot name, never {@code null}
     * @param approved the expected baseline file (which may not exist yet), never {@code null}
     * @param received the freshly written received file, never {@code null}
     * @throws NullPointerException if any argument is {@code null}
     */
    public SnapshotMismatchException(String name, Path approved, Path received) {
        super("snapshot \"" + Objects.requireNonNull(name, "name") + "\" did not match its baseline; review "
            + Objects.requireNonNull(received, "received") + " against "
            + Objects.requireNonNull(approved, "approved"));
    }
}
