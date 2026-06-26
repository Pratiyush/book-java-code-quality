package org.acme.coverage;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The set of lines a pull request changed, per file — the diff, reduced to what the bot-comment policy
 * needs to decide whether a finding is on a changed line.
 *
 * <p>A finding whose file is not in the diff, or whose line the diff did not touch, is a whole-repo
 * finding the pull request did not cause; posting it as a PR comment is the noise the chapter warns
 * gets the bot muted.
 *
 * @param byFile a map from file path to the set of 1-based line numbers the pull request changed in it
 */
public record ChangedLines(Map<String, Set<Integer>> byFile) {

    /**
     * Canonical constructor taking a defensive, unmodifiable copy so the diff cannot be mutated after
     * the policy is built (representation safety).
     *
     * @throws NullPointerException if {@code byFile} or any contained set is {@code null}
     */
    public ChangedLines {
        Objects.requireNonNull(byFile, "byFile");
        byFile.forEach((file, lines) -> {
            Objects.requireNonNull(file, "file key");
            Objects.requireNonNull(lines, "line set for " + file);
        });
        byFile = Map.copyOf(byFile);
    }

    /**
     * Whether the pull request changed the given line of the given file.
     *
     * @param file the file path
     * @param line the 1-based line number
     * @return {@code true} if that file/line pair is in the diff
     */
    public boolean changed(String file, int line) {
        Set<Integer> lines = byFile.get(file);
        return lines != null && lines.contains(line);
    }
}
