package org.acme.coverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The diff-scoped, ratcheting coverage gate: every verdict path the chapter argues for, made into a
 * test so the prose and the build cannot drift.
 */
class CoverageGateTest {

    private final CoveragePolicy policy = new CoveragePolicy(0.80, true, 0.85);
    private final CoverageGate gate = new CoverageGate(policy);

    @Nested
    @DisplayName("new-code focus: gate the changed lines, not the legacy mountain")
    class NewCodeFocus {

        @Test
        @DisplayName("new code below the bar blocks the merge")
        void newCodeUnderBarBlocks() {
            // 60% of new lines covered, bar is 80%: blocked, and the reason is actionable.
            CoverageDelta delta = new CoverageDelta(0.50, 0.55, 40, 0.60);
            CoverageVerdict verdict = gate.evaluate(delta);
            assertThat(verdict).isInstanceOf(CoverageVerdict.Block.class);
            assertThat(verdict.blocks()).isTrue();
            assertThat(verdict.reason()).contains("new-code coverage").contains("60%").contains("80%");
        }

        @Test
        @DisplayName("new code clearing the bar does not block on new-code grounds")
        void newCodeAtBarPasses() {
            // 90% new-code coverage atop a low overall base: the new-code rule is satisfied; overall
            // climbs, so the ratchet holds; overall is still under the aspirational target, so warn.
            CoverageDelta delta = new CoverageDelta(0.50, 0.52, 40, 0.90);
            CoverageVerdict verdict = gate.evaluate(delta);
            assertThat(verdict).isInstanceOf(CoverageVerdict.Warn.class);
        }

        @Test
        @DisplayName("a PR that adds no new code skips the new-code bar entirely")
        void noNewCodeSkipsNewCodeRule() {
            // A rename/config-only PR: zero new lines. Even with newCodeCovered reported as 0.0, the
            // new-code rule must not fire (legacy is never gated), and overall is unchanged and high.
            CoverageDelta delta = new CoverageDelta(0.90, 0.90, 0, 0.0);
            CoverageVerdict verdict = gate.evaluate(delta);
            assertThat(verdict).isInstanceOf(CoverageVerdict.Pass.class);
        }
    }

    @Nested
    @DisplayName("ratchet: overall coverage may only go up")
    class Ratchet {

        @Test
        @DisplayName("a drop in overall coverage blocks even when new code clears the bar")
        void overallDropBlocks() {
            // New code is fully covered, but overall coverage still drops (large untested code moved in
            // around it): the ratchet blocks. This is the case new-code focus alone would miss.
            CoverageDelta delta = new CoverageDelta(0.90, 0.88, 30, 1.00);
            CoverageVerdict verdict = gate.evaluate(delta);
            assertThat(verdict).isInstanceOf(CoverageVerdict.Block.class);
            assertThat(verdict.reason()).contains("ratchet");
        }

        @Test
        @DisplayName("with the ratchet off, a drop no longer blocks")
        void ratchetOffAllowsDrop() {
            CoverageGate noRatchet = new CoverageGate(new CoveragePolicy(0.80, false, 0.85));
            CoverageDelta delta = new CoverageDelta(0.90, 0.88, 30, 1.00);
            CoverageVerdict verdict = noRatchet.evaluate(delta);
            // Overall (0.88) is still above the target (0.85): with the ratchet off this passes.
            assertThat(verdict).isInstanceOf(CoverageVerdict.Pass.class);
        }
    }

    @Nested
    @DisplayName("aspirational target: warn, never block (a floor, not a goal)")
    class AspirationalTarget {

        @Test
        @DisplayName("overall below target warns when both rules are satisfied")
        void belowTargetWarns() {
            CoverageDelta delta = new CoverageDelta(0.70, 0.72, 20, 0.95);
            CoverageVerdict verdict = gate.evaluate(delta);
            assertThat(verdict).isInstanceOf(CoverageVerdict.Warn.class);
            assertThat(verdict.reason()).contains("warn only");
        }

        @Test
        @DisplayName("clearing both rules and the target passes")
        void clearsEverythingPasses() {
            CoverageDelta delta = new CoverageDelta(0.85, 0.90, 20, 0.95);
            CoverageVerdict verdict = gate.evaluate(delta);
            assertThat(verdict).isInstanceOf(CoverageVerdict.Pass.class);
            assertThat(verdict.blocks()).isFalse();
        }
    }

    @Nested
    @DisplayName("observability and readiness")
    class ObservabilityAndReadiness {

        @Test
        @DisplayName("blockedCount advances only on a blocking verdict")
        void blockedCountAdvancesOnlyOnBlock() {
            CoverageGate counted = new CoverageGate(policy);
            counted.evaluate(new CoverageDelta(0.85, 0.90, 20, 0.95)); // pass
            counted.evaluate(new CoverageDelta(0.50, 0.55, 40, 0.60)); // block (new code)
            counted.evaluate(new CoverageDelta(0.90, 0.88, 30, 1.00)); // block (ratchet)
            assertThat(counted.blockedCount()).isEqualTo(2L);
        }

        @Test
        @DisplayName("a policy that enforces something is ready")
        void enforcingPolicyIsReady() {
            assertThat(gate.isReady()).isTrue();
            assertThat(new CoverageGate(new CoveragePolicy(0.0, true, 0.0)).isReady()).isTrue();
        }

        @Test
        @DisplayName("a policy that gates nothing reports not-ready (no fail-open)")
        void nonEnforcingPolicyIsNotReady() {
            CoverageGate openGate = new CoverageGate(new CoveragePolicy(0.0, false, 0.0));
            assertThat(openGate.isReady()).isFalse();
        }
    }

    @Nested
    @DisplayName("the explicit failure path: null arguments are rejected")
    class FailurePath {

        @Test
        @DisplayName("a null policy is rejected at construction")
        void rejectsNullPolicy() {
            assertThatNullPointerException().isThrownBy(() -> new CoverageGate(null));
        }

        @Test
        @DisplayName("a null delta is rejected at evaluation")
        void rejectsNullDelta() {
            assertThatNullPointerException().isThrownBy(() -> gate.evaluate(null));
        }

        @Test
        @DisplayName("an out-of-range ratio is rejected when the delta is built")
        void rejectsOutOfRangeRatio() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new CoverageDelta(1.5, 0.5, 10, 0.9));
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new CoverageDelta(0.5, 0.5, -1, 0.9));
        }
    }
}
