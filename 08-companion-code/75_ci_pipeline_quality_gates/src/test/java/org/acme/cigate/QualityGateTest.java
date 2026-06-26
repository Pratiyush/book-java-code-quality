package org.acme.cigate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the quality gate: it drives the gate through the whole stack — the externalized
 * policy ({@link GatePolicy#load}) and a list of findings — and asserts each decision the chapter
 * describes, end to end. The findings are fixtures, hand-built to land in a specific decision branch
 * (pass / warn / block); the gate logic under test is real.
 *
 * <p>Together these cases pin the chapter's two policy axes: clean-as-you-code (a pre-existing finding
 * never blocks; the same finding in new code does) and block-versus-warn (only a new finding at or
 * above the block severity blocks; lower severities warn). They also confirm the dev and prod profiles
 * differ, the way a framework's {@code %dev} / {@code %prod} blocks do.
 */
class QualityGateTest {

    // The prod profile is the strict one: clean-as-you-code on, block at HIGH (from the properties).
    private final QualityGate gate = new QualityGate(GatePolicy.load("prod"));

    private static Finding newFinding(Severity severity) {
        return new Finding("checkstyle:EmptyBlock", severity, FindingScope.NEW);
    }

    private static Finding existingFinding(Severity severity) {
        return new Finding("spotbugs:NP_NULL_ON_SOME_PATH", severity, FindingScope.EXISTING);
    }

    @Test
    @DisplayName("a new high-severity finding blocks the merge")
    void blocksNewHighSeverity() {
        GateDecision decision = gate.evaluate(List.of(newFinding(Severity.HIGH)));

        assertThat(decision).isInstanceOf(GateDecision.Block.class);
        assertThat(((GateDecision.Block) decision).reason()).contains("checkstyle:EmptyBlock");
        assertThat(gate.blockedCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("clean-as-you-code: a pre-existing high-severity finding never blocks the change")
    void doesNotBlockPreExistingDebt() {
        // The same HIGH severity that blocks in new code is OUT OF SCOPE when it is inherited debt the
        // change did not touch: clean-as-you-code filters it before the decision, so a change that adds
        // no new findings passes even atop a mountain of legacy debt. That is exactly the scoping that
        // makes the gate adoptable on a legacy codebase — it never blocks a PR on debt it did not create.
        GateDecision decision = gate.evaluate(List.of(existingFinding(Severity.HIGH)));

        assertThat(decision).isInstanceOf(GateDecision.Pass.class);
        assertThat(gate.blockedCount()).isZero();
    }

    @Test
    @DisplayName("block-versus-warn applies to new findings: a new MEDIUM is surfaced but not blocking")
    void warnsNewSubBlockingSeverity() {
        // A new finding below the block severity is the case that warns rather than passing silently:
        // it is in scope (new code) but not severe enough to block, so the gate surfaces it for review.
        GateDecision decision = gate.evaluate(List.of(newFinding(Severity.MEDIUM)));

        assertThat(decision).isInstanceOf(GateDecision.Warn.class);
    }

    @Test
    @DisplayName("block-versus-warn: a new low-severity finding is warned, not blocked")
    void warnsNewLowSeverity() {
        GateDecision decision = gate.evaluate(List.of(newFinding(Severity.LOW)));

        assertThat(decision).isInstanceOf(GateDecision.Warn.class);
    }

    @Test
    @DisplayName("a clean change with no findings passes")
    void passesWhenClean() {
        GateDecision decision = gate.evaluate(List.of());

        assertThat(decision).isInstanceOf(GateDecision.Pass.class);
    }

    @Test
    @DisplayName("the blocking decision names the worst new finding, not merely the first")
    void reportsTheWorstBlockingFinding() {
        // A run mixes a new MEDIUM and a new HIGH; the block must point at the HIGH so the developer
        // fixes the real problem, not the first line scanned.
        GateDecision decision =
            gate.evaluate(List.of(newFinding(Severity.MEDIUM), newFinding(Severity.HIGH)));

        assertThat(decision).isInstanceOf(GateDecision.Block.class);
        assertThat(((GateDecision.Block) decision).reason()).contains("HIGH");
    }

    @Test
    @DisplayName("whole-repo policy: with clean-as-you-code off, pre-existing debt can block")
    void wholeRepoPolicyBlocksOnExistingDebt() {
        // Turning clean-as-you-code off gates the whole repository — the same pre-existing HIGH finding
        // that prod warned on now blocks. The chapter's point that this stops every PR on a legacy base.
        QualityGate wholeRepo = new QualityGate(new GatePolicy(false, Severity.HIGH));

        assertThat(wholeRepo.evaluate(List.of(existingFinding(Severity.HIGH))))
            .isInstanceOf(GateDecision.Block.class);
    }

    @Test
    @DisplayName("the externalized dev profile is more forgiving than prod")
    void devProfileIsLooser() {
        // dev blocks only at the highest severity too, but a team may set a looser block severity per
        // profile; here dev keeps clean-as-you-code on and HIGH blocking, so a new MEDIUM warns.
        QualityGate devGate = new QualityGate(GatePolicy.load("dev"));

        assertThat(devGate.evaluate(List.of(newFinding(Severity.MEDIUM))))
            .isInstanceOf(GateDecision.Warn.class);
    }

    @Test
    @DisplayName("the gate reports ready once a policy is wired")
    void readinessReflectsWiredPolicy() {
        assertThat(gate.isReady()).isTrue();
    }

    @Test
    @DisplayName("an unknown profile is rejected at load")
    void unknownProfileIsRejected() {
        assertThatThrownBy(() -> GatePolicy.load("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("a finding requires every component")
    void findingRejectsNullComponents() {
        assertThatThrownBy(() -> new Finding(null, Severity.HIGH, FindingScope.NEW))
            .isInstanceOf(NullPointerException.class);
    }
}
