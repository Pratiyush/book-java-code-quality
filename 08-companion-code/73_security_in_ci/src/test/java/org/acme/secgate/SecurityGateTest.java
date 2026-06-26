package org.acme.secgate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the security gate: it drives the gate through the whole stack — the externalized
 * policy ({@link SecurityGatePolicy#load}) and a pooled list of findings from several security stages —
 * and asserts each decision the chapter describes, end to end. The findings are fixtures, hand-built to
 * land in a specific decision branch (pass / route-to-review / block); the gate logic under test is real.
 *
 * <p>Together these cases pin the chapter's policy axes: clean-as-you-code (a pre-existing finding never
 * blocks; the same finding in new code does), block-versus-review (only a new, exploitable finding at or
 * above the block severity blocks; the rest route to a reviewer), and the security-specific honest edge
 * that the gate is blind to the flaws no stage produces. They also confirm the dev and prod profiles
 * differ, the way a framework's {@code %dev} / {@code %prod} blocks do.
 */
class SecurityGateTest {

    // The dev profile is the feature-branch one: clean-as-you-code on, block at HIGH, require exploitable.
    private final SecurityGate gate = new SecurityGate(SecurityGatePolicy.load("dev"));

    private static SecurityFinding newSastInjection(Severity severity, boolean exploitable) {
        // SAST injection finding (CWE-89, the SQL-injection class), in new code.
        return new SecurityFinding(SecurityStage.SAST, "CWE-89", severity, FindingScope.NEW, exploitable);
    }

    private static SecurityFinding existingScaCve(Severity severity) {
        // SCA known-CVE finding in a dependency, pre-existing debt the change did not touch.
        return new SecurityFinding(SecurityStage.SCA, "CVE-2021-44228", severity, FindingScope.EXISTING, true);
    }

    @Test
    @DisplayName("a new, exploitable, high-severity finding blocks the merge")
    void blocksNewExploitableHighSeverity() {
        SecurityGateDecision decision = gate.evaluate(List.of(newSastInjection(Severity.HIGH, true)));

        assertThat(decision).isInstanceOf(SecurityGateDecision.Block.class);
        assertThat(((SecurityGateDecision.Block) decision).reason()).contains("CWE-89");
        assertThat(((SecurityGateDecision.Block) decision).reason()).contains("SAST");
        assertThat(gate.blockedCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("clean-as-you-code: a pre-existing high-severity finding never blocks the change")
    void doesNotBlockPreExistingDebt() {
        // The same HIGH severity that blocks in new code is OUT OF SCOPE when it is inherited debt the
        // change did not touch: clean-as-you-code filters it before the decision, so a change that adds no
        // new findings passes even atop a mountain of legacy security debt. That scoping is what keeps the
        // gate from blocking every pull request on a legacy codebase — the number-one way a gate dies.
        SecurityGateDecision decision = gate.evaluate(List.of(existingScaCve(Severity.HIGH)));

        assertThat(decision).isInstanceOf(SecurityGateDecision.Pass.class);
        assertThat(gate.blockedCount()).isZero();
    }

    @Test
    @DisplayName("exploitability is a judgment: a new HIGH finding that is not confirmed exploitable routes to review")
    void routesSevereButUnprovenToReview() {
        // A new HIGH finding that is not confirmed exploitable (an unreachable sink, a non-security use of
        // a weak primitive) does NOT auto-block under the dev policy; it is routed to a security reviewer.
        // This is the middle path between blocking everything and blocking nothing (Chapter 84).
        SecurityGateDecision decision = gate.evaluate(List.of(newSastInjection(Severity.HIGH, false)));

        assertThat(decision).isInstanceOf(SecurityGateDecision.Review.class);
        assertThat(gate.blockedCount()).isZero();
    }

    @Test
    @DisplayName("block-versus-review: a new MEDIUM finding is routed to review, not blocked")
    void routesNewSubBlockingSeverityToReview() {
        SecurityGateDecision decision = gate.evaluate(List.of(newSastInjection(Severity.MEDIUM, true)));

        assertThat(decision).isInstanceOf(SecurityGateDecision.Review.class);
    }

    @Test
    @DisplayName("a clean change with no findings passes")
    void passesWhenClean() {
        SecurityGateDecision decision = gate.evaluate(List.of());

        assertThat(decision).isInstanceOf(SecurityGateDecision.Pass.class);
    }

    @Test
    @DisplayName("the gate aggregates findings from several stages and names the worst blocking one")
    void aggregatesAcrossStagesAndNamesTheWorst() {
        // A single run pools findings from three stages: a new MEDIUM secret, a pre-existing SCA CVE, and a
        // new exploitable HIGH SAST finding. The gate aggregates them and blocks on the SAST HIGH — the
        // worst in-scope, exploitable finding — not the first one scanned. This is the assembly the chapter
        // is about: many stages, one decision.
        SecurityGateDecision decision = gate.evaluate(List.of(
            new SecurityFinding(SecurityStage.SECRETS, "leaked-key", Severity.MEDIUM, FindingScope.NEW, true),
            existingScaCve(Severity.HIGH),
            newSastInjection(Severity.HIGH, true)));

        assertThat(decision).isInstanceOf(SecurityGateDecision.Block.class);
        assertThat(((SecurityGateDecision.Block) decision).reason()).contains("SAST");
    }

    @Test
    @DisplayName("green gate is not 'secure': a broken-access-control flaw no stage produces lets the gate pass")
    void greenGateIsNotSecure() {
        // The chapter's honest center, made concrete: the highest-severity classes — broken access control,
        // business-logic abuse — are exactly the ones no SAST/SCA/secrets/DAST stage reliably produces, so
        // they never appear in the findings list. A run that detected nothing PASSES, but a Pass means only
        // "no detected, known, exploitable issue", NOT secure: this deliberately-undetected flaw is still
        // there, needing threat modeling and design review (Chapter 84). The gate is a layer, not the whole.
        SecurityGateDecision decision = gate.evaluate(List.of());  // the BAC flaw is real but undetected

        assertThat(decision).isInstanceOf(SecurityGateDecision.Pass.class);
    }

    @Test
    @DisplayName("the release (prod) profile blocks a new HIGH finding without waiting for an exploitability verdict")
    void prodProfileFailsClosedOnNewHigh() {
        // The trunk/release gate is the conservative one: prod sets require-exploitable-to-block=false, so a
        // new HIGH finding blocks even before exploitability is confirmed (fail-closed), where the dev gate
        // routed the same finding to review. The threshold is policy, externalized per profile, not code.
        SecurityGate releaseGate = new SecurityGate(SecurityGatePolicy.load("prod"));

        assertThat(releaseGate.evaluate(List.of(newSastInjection(Severity.HIGH, false))))
            .isInstanceOf(SecurityGateDecision.Block.class);
    }

    @Test
    @DisplayName("whole-repo policy: with clean-as-you-code off, pre-existing debt can block")
    void wholeRepoPolicyBlocksOnExistingDebt() {
        // Turning clean-as-you-code off gates the whole repository — the same pre-existing HIGH finding the
        // dev profile passed on now blocks. The chapter's point that whole-repo absolutes stop every PR.
        SecurityGate wholeRepo = new SecurityGate(new SecurityGatePolicy(false, Severity.HIGH, false));

        assertThat(wholeRepo.evaluate(List.of(existingScaCve(Severity.HIGH))))
            .isInstanceOf(SecurityGateDecision.Block.class);
    }

    @Test
    @DisplayName("the observability surface reports which stages contributed findings")
    void stagesReportingExposesCoverage() {
        // stagesReporting is the coverage signal: a stage missing from the set may mean it found nothing OR
        // that it did not run — the failure mode a security gate must make visible rather than read as clean.
        var reporting = gate.stagesReporting(List.of(
            newSastInjection(Severity.LOW, true), existingScaCve(Severity.HIGH)));

        assertThat(reporting).containsExactlyInAnyOrder(SecurityStage.SAST, SecurityStage.SCA);
    }

    @Test
    @DisplayName("the static/dynamic split: the static trio runs at the PR, the dynamic pair later")
    void stageOrderingSeparatesStaticFromDynamic() {
        assertThat(SecurityStage.SECRETS.isStatic()).isTrue();
        assertThat(SecurityStage.SAST.isStatic()).isTrue();
        assertThat(SecurityStage.SCA.isStatic()).isTrue();
        assertThat(SecurityStage.DAST.isStatic()).isFalse();
        assertThat(SecurityStage.IAST.isStatic()).isFalse();
    }

    @Test
    @DisplayName("the gate reports ready once a policy is wired")
    void readinessReflectsWiredPolicy() {
        assertThat(gate.isReady()).isTrue();
    }

    @Test
    @DisplayName("an unknown profile is rejected at load")
    void unknownProfileIsRejected() {
        assertThatThrownBy(() -> SecurityGatePolicy.load("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("a finding requires every component")
    void findingRejectsNullComponents() {
        assertThatThrownBy(() -> new SecurityFinding(null, "CWE-89", Severity.HIGH, FindingScope.NEW, true))
            .isInstanceOf(NullPointerException.class);
    }
}
