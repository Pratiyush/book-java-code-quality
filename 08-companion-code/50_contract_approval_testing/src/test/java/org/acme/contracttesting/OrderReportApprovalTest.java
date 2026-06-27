package org.acme.contracttesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Approval testing: pinning a large, structured output against a reviewed baseline.
 *
 * <p>The {@code OrderReport} is rendered, its non-deterministic timestamp scrubbed, and the result
 * compared to a committed {@code order-report.approved.txt} (under {@code src/test/resources/approvals}).
 * The baseline lives in version control precisely so it shows up in pull-request review, where a second
 * person sees the diff. The committed file is copied into a {@link TempDir} so the comparison runs
 * hermetically while still exercising the real read/write path.
 *
 * <p>The honest edge is demonstrated, not just stated: a deliberately-wrong baseline is approved without
 * review, and the test then passes against wrong output — the rubber-stamp risk made concrete. The
 * verifier checks "unchanged," never "correct"; reading the diff is the human half.
 */
class OrderReportApprovalTest {

    private static final List<Order> ORDERS = List.of(
        new Order("42", "CONFIRMED", 5_000L),
        new Order("43", "SHIPPED", 1_200L));

    /** A scrubber that normalizes the report's generated-at timestamp so the snapshot is stable. */
    // tag::scrubber[]
    private static final UnaryOperator<String> SCRUB_TIMESTAMP =
        text -> text.replaceAll("generated-at: .*", "generated-at: <timestamp>");
    // end::scrubber[]

    @Test
    @DisplayName("the report matches its committed, reviewed baseline after scrubbing")
    void reportMatchesApprovedBaseline(@TempDir Path workDir) throws IOException {
        copyCommittedBaseline("order-report.approved.txt", workDir);
        SnapshotVerifier verifier = new SnapshotVerifier(workDir);

        // tag::approval-verify[]
        // Render with a live timestamp; the scrubber normalizes it so it matches the approved baseline.
        String report = OrderReport.render(ORDERS, Instant.now());
        assertThatNoException()
            .isThrownBy(() -> verifier.verify("order-report", report, SCRUB_TIMESTAMP));
        // end::approval-verify[]
    }

    @Test
    @DisplayName("a missing baseline fails by design, so the first run forces a human review")
    void missingBaselineFailsByDesign(@TempDir Path workDir) {
        SnapshotVerifier verifier = new SnapshotVerifier(workDir);
        String report = OrderReport.render(ORDERS, Instant.now());

        assertThatExceptionOfType(SnapshotMismatchException.class)
            .isThrownBy(() -> verifier.verify("order-report", report, SCRUB_TIMESTAMP));
    }

    @Test
    @DisplayName("a changed output fails until a human re-approves the new baseline")
    void changedOutputFailsUntilReapproved(@TempDir Path workDir) throws IOException {
        copyCommittedBaseline("order-report.approved.txt", workDir);
        SnapshotVerifier verifier = new SnapshotVerifier(workDir);

        // One extra order changes the report; the baseline no longer matches, as it should not.
        List<Order> changed = List.of(
            new Order("42", "CONFIRMED", 5_000L),
            new Order("43", "SHIPPED", 1_200L),
            new Order("44", "PENDING", 800L));
        String report = OrderReport.render(changed, Instant.now());

        assertThatExceptionOfType(SnapshotMismatchException.class)
            .isThrownBy(() -> verifier.verify("order-report", report, SCRUB_TIMESTAMP));
    }

    @Test
    @DisplayName("the honest edge: rubber-stamping a wrong baseline makes the test confirm wrong output")
    void rubberStampingAWrongBaselineHidesABug(@TempDir Path workDir) throws IOException {
        SnapshotVerifier verifier = new SnapshotVerifier(workDir);

        // A WRONG total (9999, not 6200) is approved without anyone reading the diff: the failure mode the
        // chapter warns about. From here the suite stays green against wrong output forever — the test
        // verifies "unchanged," not "correct". Shown so the cost is visible, never as a pattern to copy.
        Files.writeString(
            workDir.resolve("order-report.approved.txt"),
            "ORDER REPORT\ngenerated-at: <timestamp>\ncount: 2\n"
                + "  - 42 [CONFIRMED] 5000\n  - 43 [SHIPPED] 1200\ntotal: 9999\n",
            StandardCharsets.UTF_8);

        String report = OrderReport.render(ORDERS, Instant.now());
        assertThatExceptionOfType(SnapshotMismatchException.class)
            .isThrownBy(() -> verifier.verify("order-report", report, SCRUB_TIMESTAMP));

        // The real total is 6200; the rubber-stamped baseline says 9999 — the bug is now the baseline.
        assertThat(report).contains("total: 6200");
    }

    @Test
    @DisplayName("an un-scrubbed timestamp would flake; the scrubber removes the non-determinism")
    void scrubberRemovesNonDeterminism() {
        String first = OrderReport.render(ORDERS, Instant.parse("2026-01-01T00:00:00Z"));
        String second = OrderReport.render(ORDERS, Instant.parse("2026-06-20T12:30:00Z"));

        assertThat(first).isNotEqualTo(second);                       // raw output differs by timestamp...
        assertThat(SCRUB_TIMESTAMP.apply(first))
            .isEqualTo(SCRUB_TIMESTAMP.apply(second));                // ...scrubbed output is identical
    }

    private static void copyCommittedBaseline(String fileName, Path workDir) throws IOException {
        Path source = Path.of("src", "test", "resources", "approvals", fileName);
        Files.createDirectories(workDir);
        Files.copy(source, workDir.resolve(fileName));
    }
}
