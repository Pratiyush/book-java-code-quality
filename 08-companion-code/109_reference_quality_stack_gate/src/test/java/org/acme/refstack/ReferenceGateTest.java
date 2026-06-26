package org.acme.refstack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the reference gate: it drives the composition through the whole stack — the
 * externalized ladder ({@link GateLadder#load}) and the stage outcomes of a run — and asserts each
 * verdict the capstone describes, end to end. The stage outcomes are fixtures, hand-built to land in a
 * specific branch (ship / no-ship); the composition logic under test is real.
 *
 * <p>Together these cases pin the chapter's three gate-design axes: the enforce-from stage (an advisory
 * stage never blocks; the same finding at an enforced stage does), clean-as-you-code (a whole-repo
 * finding never blocks under the new-code scope; the same finding on new code does), and block-versus-
 * advisory severity. They also confirm the dev and prod profiles differ, the way a framework's
 * {@code %dev} / {@code %prod} blocks do, and that the prod ladder enforces the earliest stage.
 */
class ReferenceGateTest {

    // The prod ladder is the strict one: enforce from PRE_COMMIT, clean-as-you-code on, block at HIGH.
    private final ReferenceGate gate = new ReferenceGate(GateLadder.load("prod"));

    private static StageOutcome blockingAt(GateStage stage) {
        return StageOutcome.raised(stage, Severity.HIGH, true, "a new high-severity finding");
    }

    @Test
    @DisplayName("a new high-severity finding at an enforced stage makes the change not ship")
    void noShipOnEnforcedHighSeverity() {
        ShipVerdict verdict = gate.evaluate(List.of(blockingAt(GateStage.PR_FAST)));

        assertThat(verdict).isInstanceOf(ShipVerdict.NoShip.class);
        assertThat(((ShipVerdict.NoShip) verdict).reason()).contains("PR_FAST");
        assertThat(gate.noShipCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("a clean run across every stage ships")
    void shipsWhenEveryStageClears() {
        ShipVerdict verdict = gate.evaluate(List.of(
            StageOutcome.clear(GateStage.PRE_COMMIT),
            StageOutcome.clear(GateStage.PR_FAST),
            StageOutcome.clear(GateStage.MAIN_NIGHTLY),
            StageOutcome.clear(GateStage.MERGE)));

        assertThat(verdict).isInstanceOf(ShipVerdict.Ship.class);
        assertThat(gate.noShipCount()).isZero();
    }

    @Test
    @DisplayName("clean-as-you-code: a high-severity finding on pre-existing code never blocks the ship")
    void doesNotBlockPreExistingDebt() {
        // The same HIGH severity that blocks on new code is OUT OF SCOPE when it is inherited debt the
        // change did not touch: clean-as-you-code filters it before the verdict, so a change that adds no
        // new findings ships even atop a mountain of legacy debt. That scoping is what makes the whole
        // stack adoptable on a legacy codebase — it never blocks a change on debt it did not create.
        StageOutcome onOldCode = StageOutcome.raised(GateStage.MAIN_NIGHTLY, Severity.HIGH, false, "legacy debt");

        ShipVerdict verdict = gate.evaluate(List.of(onOldCode));

        assertThat(verdict).isInstanceOf(ShipVerdict.Ship.class);
    }

    @Test
    @DisplayName("block-versus-advisory: a new MEDIUM finding is surfaced but ships")
    void shipsOnSubBlockingSeverity() {
        // A new finding below the block severity is in scope (new code, enforced stage) but not severe
        // enough to block, so it is advisory and the change ships — the noise stays out of the no-ship.
        StageOutcome medium = StageOutcome.raised(GateStage.PR_FAST, Severity.MEDIUM, true, "a new minor finding");

        ShipVerdict verdict = gate.evaluate(List.of(medium));

        assertThat(verdict).isInstanceOf(ShipVerdict.Ship.class);
    }

    @Test
    @DisplayName("incremental adoption: a dev ladder treats an early stage as advisory, so it ships")
    void devLadderTreatsEarlyStageAsAdvisory() {
        // The dev profile enforces from PR_FAST, so a PRE_COMMIT finding is advisory — the same finding
        // that blocks under prod (which enforces from PRE_COMMIT) ships under dev. This is the knob a
        // team uses to adopt the stack incrementally rather than turning every stage on at once.
        ReferenceGate devGate = new ReferenceGate(GateLadder.load("dev"));

        ShipVerdict verdict = devGate.evaluate(List.of(blockingAt(GateStage.PRE_COMMIT)));

        assertThat(verdict).isInstanceOf(ShipVerdict.Ship.class);
        // Under prod the SAME pre-commit finding does not ship, confirming the profiles differ.
        assertThat(gate.evaluate(List.of(blockingAt(GateStage.PRE_COMMIT))))
            .isInstanceOf(ShipVerdict.NoShip.class);
    }

    @Test
    @DisplayName("the no-ship verdict carries every blocking stage, not merely the first")
    void noShipCarriesAllBlockingStages() {
        // Two stages block; the verdict names the first and carries both, so the failure is actionable
        // across the whole run rather than hiding the second problem behind the first.
        ShipVerdict verdict = gate.evaluate(List.of(
            blockingAt(GateStage.PR_FAST),
            blockingAt(GateStage.MAIN_NIGHTLY)));

        assertThat(verdict).isInstanceOf(ShipVerdict.NoShip.class);
        assertThat(((ShipVerdict.NoShip) verdict).blocking()).hasSize(2);
    }

    @Test
    @DisplayName("the assembled stack lists every layer in concern order, each with an alternative")
    void referenceStackNamesEveryLayerAndAlternative() {
        // The carve-out in code: every layer carries a named alternative, so the recommendation states
        // its trade-off rather than crowning a tool. No layer may be left without one.
        assertThat(ReferenceStack.layers()).containsExactly(StackLayer.values());
        for (StackLayer layer : ReferenceStack.layers()) {
            assertThat(layer.alternative()).isNotBlank();
            assertThat(ReferenceStack.describe(layer)).contains("alternative:");
        }
    }

    @Test
    @DisplayName("the gate reports ready once a ladder is wired")
    void readinessReflectsWiredLadder() {
        assertThat(gate.isReady()).isTrue();
    }

    @Test
    @DisplayName("an unknown profile is rejected at load")
    void unknownProfileIsRejected() {
        assertThatThrownBy(() -> GateLadder.load("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("a stage outcome requires its stage and detail")
    void stageOutcomeRejectsNullComponents() {
        assertThatThrownBy(() -> StageOutcome.raised(null, Severity.HIGH, true, "x"))
            .isInstanceOf(NullPointerException.class);
    }
}
