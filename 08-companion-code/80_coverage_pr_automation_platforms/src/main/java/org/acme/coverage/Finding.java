package org.acme.coverage;

import java.util.Objects;

/**
 * One analyzer finding a bot might post on a pull request: a file, the line it sits on, its severity,
 * and a message.
 *
 * <p>The {@code line} is the key field for diff-scoping — the PR-comment policy posts a finding only
 * when its line is one the pull request actually changed, the discipline that keeps an incremental PR
 * from being told about whole-repo findings it did not cause.
 *
 * @param file     the path the finding is in, never {@code null} or blank
 * @param line     the 1-based line number the finding sits on, positive
 * @param severity the finding's severity, never {@code null}
 * @param message  the human-readable message, never {@code null} or blank
 */
public record Finding(String file, int line, Severity severity, String message) {

    /**
     * Canonical constructor with fail-fast guards.
     *
     * @throws NullPointerException     if {@code file}, {@code severity}, or {@code message} is {@code null}
     * @throws IllegalArgumentException if {@code file} or {@code message} is blank or {@code line} is not positive
     */
    public Finding {
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(severity, "severity");
        Objects.requireNonNull(message, "message");
        if (file.isBlank()) {
            throw new IllegalArgumentException("file must not be blank");
        }
        if (message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        if (line <= 0) {
            throw new IllegalArgumentException("line must be positive: " + line);
        }
    }
}
