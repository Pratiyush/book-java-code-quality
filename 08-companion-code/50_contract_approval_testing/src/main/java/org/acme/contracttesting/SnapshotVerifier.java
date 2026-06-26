package org.acme.contracttesting;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A minimal approval/snapshot verifier: it pins output against a reviewed baseline on disk, the same
 * mechanism ApprovalTests.Java provides (see the README for what that library adds in production).
 *
 * <p>On each call it writes the produced text to a {@code <name>.received.txt} file, compares it to the
 * committed {@code <name>.approved.txt}, and fails if they differ or if the approved file does not yet
 * exist. The first run therefore fails by design: a human reads the {@code received} file and, if it is
 * correct, promotes it to {@code approved} and commits it. Scrubbers normalize non-deterministic content
 * (a timestamp, a GUID) before the comparison so the test does not flake.
 *
 * <p>The headline risk is built into the workflow, not the tool: approving a {@code received} file
 * without reading it bakes a wrong baseline in, and every later run then confirms the wrong output. The
 * verifier pins "unchanged," never "correct" — reviewing the diff is the human half that makes it worth
 * running.
 */
public final class SnapshotVerifier {

    private final Path approvedDir;

    /**
     * Creates a verifier that reads and writes snapshot files under the given directory.
     *
     * @param approvedDir the directory holding {@code *.approved.txt} baselines, never {@code null}
     * @throws NullPointerException if {@code approvedDir} is {@code null}
     */
    public SnapshotVerifier(Path approvedDir) {
        this.approvedDir = Objects.requireNonNull(approvedDir, "approvedDir");
    }

    /**
     * Verifies {@code produced} against the committed baseline named {@code name}, after scrubbing.
     *
     * @param name     the snapshot name (a file-name stem), never {@code null}
     * @param produced the freshly produced output to check, never {@code null}
     * @param scrubber normalizes non-deterministic content before comparison, never {@code null}
     * @throws SnapshotMismatchException if no baseline exists yet or the scrubbed output differs
     */
    // tag::snapshot-verify[]
    public void verify(String name, String produced, UnaryOperator<String> scrubber) {
        String scrubbed = scrubber.apply(produced);
        Path received = write(name + ".received.txt", scrubbed);
        Path approved = approvedDir.resolve(name + ".approved.txt");
        if (!Files.exists(approved) || !read(approved).equals(scrubbed)) {
            throw new SnapshotMismatchException(name, approved, received);
        }
    }
    // end::snapshot-verify[]

    private Path write(String fileName, String content) {
        try {
            Files.createDirectories(approvedDir);
            Path target = approvedDir.resolve(fileName);
            Files.writeString(target, content, StandardCharsets.UTF_8);
            return target;
        } catch (IOException e) {
            throw new UncheckedIOException("could not write snapshot " + fileName, e);
        }
    }

    private static String read(Path file) {
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            return String.join("\n", lines) + "\n";
        } catch (IOException e) {
            throw new UncheckedIOException("could not read snapshot " + file, e);
        }
    }
}
