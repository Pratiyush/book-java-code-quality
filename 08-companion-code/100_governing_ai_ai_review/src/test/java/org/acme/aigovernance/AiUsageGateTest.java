package org.acme.aigovernance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the AI-usage gate: it drives the gate through the whole stack — the externalized
 * policy ({@link AiGovernancePolicy#load}) and a change's governance state ({@link ChangeContext}) — and
 * asserts each decision the chapter describes, end to end. The change contexts are fixtures, hand-built to
 * land in a specific decision branch; the gate logic under test is real.
 *
 * <p>Together these cases pin the chapter's governance shape: the "only policy can ship it" preconditions
 * (sanctioned tool, AI-specific checks, disclosure, accountable human, no auto-merge on AI approval), the
 * AI-review intent ceiling (a missed bug it cannot infer), and the counter-metric (productivity reported
 * only with its risk). They also confirm the dev and prod profiles differ, the way a framework's
 * {@code %dev} / {@code %prod} blocks do.
 */
class AiUsageGateTest {

    // The prod profile is the strict one: full AI-specific check set, all flags on (from the properties).
    private final AiUsageGate gate = new AiUsageGate(AiGovernancePolicy.load("prod"));

    private static ChangeContext compliantAiChange() {
        return new ChangeContext(
                true,
                "vetted-assistant-a",
                EnumSet.allOf(AiCheck.class),
                true,
                "alice",
                false);
    }

    @Nested
    class OnlyPolicyCanShipIt {

        @Test
        @DisplayName("a fully compliant AI-assisted change is permitted")
        void permitsCompliantChange() {
            GateDecision decision = gate.evaluate(compliantAiChange());

            assertThat(decision).isInstanceOf(GateDecision.Permit.class);
            assertThat(gate.blockedCount()).isZero();
        }

        @Test
        @DisplayName("a change that did not use AI is outside the AI-specific policy")
        void permitsHumanOnlyChange() {
            // Ordinary human work still passes the normal gates (Parts IV-IX); this AI-specific gate does
            // not constrain it. It is permitted here even with no checks reported and no disclosure.
            GateDecision decision = gate.evaluate(ChangeContext.humanOnly("bob"));

            assertThat(decision).isInstanceOf(GateDecision.Permit.class);
        }

        @Test
        @DisplayName("an unsanctioned AI tool is shadow AI and is blocked")
        void blocksUnsanctionedTool() {
            ChangeContext shadow = new ChangeContext(
                    true, "some-personal-account-tool", EnumSet.allOf(AiCheck.class), true, "alice", false);

            GateDecision decision = gate.evaluate(shadow);

            assertThat(decision).isInstanceOf(GateDecision.Block.class);
            assertThat(((GateDecision.Block) decision).reason()).contains("shadow AI");
            assertThat(gate.blockedCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("a missing AI-specific check blocks (the security stack is mandatory for AI code)")
        void blocksWhenRequiredCheckMissing() {
            // prod requires the full set; drop mutation-verification of AI tests and the gate blocks,
            // naming the missing check so the author knows exactly which control to satisfy.
            Set<AiCheck> withoutMutation = EnumSet.of(AiCheck.SAST, AiCheck.SCA, AiCheck.SECRETS);
            ChangeContext change = new ChangeContext(
                    true, "vetted-assistant-a", withoutMutation, true, "alice", false);

            GateDecision decision = gate.evaluate(change);

            assertThat(decision).isInstanceOf(GateDecision.Block.class);
            assertThat(((GateDecision.Block) decision).reason()).contains("MUTATION_VERIFIED_TESTS");
        }

        @Test
        @DisplayName("undisclosed AI use is blocked (provenance required)")
        void blocksUndisclosedAiUse() {
            ChangeContext change = new ChangeContext(
                    true, "vetted-assistant-a", EnumSet.allOf(AiCheck.class), false, "alice", false);

            assertThat(gate.evaluate(change)).isInstanceOf(GateDecision.Block.class);
        }

        @Test
        @DisplayName("no accountable human is blocked — 'the AI did it' is not a defense")
        void blocksWhenNoAccountableHuman() {
            ChangeContext change = new ChangeContext(
                    true, "vetted-assistant-a", EnumSet.allOf(AiCheck.class), true, "  ", false);

            GateDecision decision = gate.evaluate(change);

            assertThat(decision).isInstanceOf(GateDecision.Block.class);
            assertThat(((GateDecision.Block) decision).reason()).contains("not a defense");
        }

        @Test
        @DisplayName("the hard line: auto-merge on an AI approval is blocked")
        void blocksAutoMergeOnAiReview() {
            ChangeContext change = new ChangeContext(
                    true, "vetted-assistant-a", EnumSet.allOf(AiCheck.class), true, "alice", true);

            GateDecision decision = gate.evaluate(change);

            assertThat(decision).isInstanceOf(GateDecision.Block.class);
            assertThat(((GateDecision.Block) decision).reason()).contains("keep the human gate");
        }
    }

    @Nested
    class TheProfilesDiffer {

        @Test
        @DisplayName("the externalized dev profile is more permissive than prod")
        void devProfileIsLooser() {
            // dev does NOT require mutation-verified AI tests, so a change with only the security stack —
            // which prod blocks — is permitted under dev. The %dev / %prod difference, externalized.
            AiUsageGate devGate = new AiUsageGate(AiGovernancePolicy.load("dev"));
            Set<AiCheck> securityOnly = EnumSet.of(AiCheck.SAST, AiCheck.SCA, AiCheck.SECRETS);
            ChangeContext change = new ChangeContext(
                    true, "vetted-assistant-a", securityOnly, true, "alice", false);

            assertThat(devGate.evaluate(change)).isInstanceOf(GateDecision.Permit.class);
        }

        @Test
        @DisplayName("an unknown profile is rejected at load")
        void unknownProfileIsRejected() {
            assertThatThrownBy(() -> AiGovernancePolicy.load("nope"))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("the gate reports ready once a constraining policy is wired")
        void readinessReflectsWiredPolicy() {
            assertThat(gate.isReady()).isTrue();
        }
    }

    @Nested
    class AiReviewIsAnAugmentation {

        @Test
        @DisplayName("an AI reviewer produces three outcomes — and the third is the intent ceiling")
        void classifiesTheThreeOutcomes() {
            AiReviewOutcome realCatch =
                AiReviewOutcome.classify(true, true, "flagged a real null-deref on the new path");
            AiReviewOutcome falsePositive =
                AiReviewOutcome.classify(true, false, "flagged a 'bug' that the contract allows");
            AiReviewOutcome missed =
                AiReviewOutcome.classify(false, true, "off-by-one the reviewer could not infer intent for");

            assertThat(realCatch.kind()).isEqualTo(AiReviewOutcome.Kind.REAL_CATCH);
            assertThat(falsePositive.kind()).isEqualTo(AiReviewOutcome.Kind.FALSE_POSITIVE);
            assertThat(missed.kind()).isEqualTo(AiReviewOutcome.Kind.MISSED_BUG);

            // The intent ceiling: only the missed bug is the outcome AI review cannot rule out, which is
            // why it augments but never BECOMES the gate.
            assertThat(realCatch.isIntentCeiling()).isFalse();
            assertThat(falsePositive.isIntentCeiling()).isFalse();
            assertThat(missed.isIntentCeiling()).isTrue();
        }
    }

    @Nested
    class CounterMetricProductivityWithRisk {

        @Test
        @DisplayName("adoption with no rise in change-failure rate is healthy")
        void healthyWhenRiskDoesNotRise() {
            AiAdoptionCounterMetric metric = new AiAdoptionCounterMetric(60.0, 0.12, 0.12);

            assertThat(metric.verdict()).isEqualTo(AiAdoptionCounterMetric.Verdict.HEALTHY);
        }

        @Test
        @DisplayName("adoption WITH a rising change-failure rate fires the counter-metric")
        void riskRisingWhenFailureRateClimbs() {
            // Velocity alone never earns a healthy verdict: adoption is up, but so is change-failure rate
            // over the pre-adoption baseline, which is exactly the half a velocity-only dashboard hides.
            AiAdoptionCounterMetric metric = new AiAdoptionCounterMetric(60.0, 0.20, 0.12);

            assertThat(metric.verdict()).isEqualTo(AiAdoptionCounterMetric.Verdict.RISK_RISING);
        }

        @Test
        @DisplayName("an out-of-range measurement is rejected")
        void rejectsImpossibleMeasurement() {
            assertThatThrownBy(() -> new AiAdoptionCounterMetric(140.0, 0.1, 0.1))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new AiAdoptionCounterMetric(50.0, 1.4, 0.1))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FailFastGuards {

        @Test
        @DisplayName("the gate rejects a null change")
        void gateRejectsNullChange() {
            assertThatThrownBy(() -> gate.evaluate(null)).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("a change context requires its non-null components")
        void changeContextRejectsNulls() {
            assertThatThrownBy(() -> new ChangeContext(true, null, Set.of(), true, "alice", false))
                .isInstanceOf(NullPointerException.class);
        }
    }
}
