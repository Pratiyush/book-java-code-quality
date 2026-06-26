package org.acme.release;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the release-readiness gate: it drives the gate through the whole stack — the
 * externalized policy ({@link ReleasePolicy#load}), a {@link ReleaseCandidate}, and the
 * {@link ReleaseDecision} — and asserts each outcome the chapter describes, end to end. The candidates
 * are fixtures hand-built to satisfy or fail a specific precondition; the gate logic under test is real.
 *
 * <p>Together these cases pin the chapter's release gates: a release ships only when every precondition
 * the active profile requires is met (a release version not a {@code -SNAPSHOT}, a changelog entry, CI
 * green on the commit, signed with an SBOM, smoke-tested), and a blocked release names exactly what
 * failed. They also confirm the dev and prod profiles require different sets, the way a framework's
 * {@code %dev} / {@code %prod} blocks do.
 */
class ReleaseReadinessTest {

    // The prod profile is the strict one: every release gate required (from the properties file).
    private final ReleaseReadiness gate = new ReleaseReadiness(ReleasePolicy.load("prod"));

    private static ReleaseCandidate fullyReady(String version) {
        return new ReleaseCandidate(SemanticVersion.parse(version), true, true, true, true);
    }

    @Test
    @DisplayName("a fully-prepared release version passes every prod gate")
    void readyWhenAllPreconditionsMet() {
        ReleaseDecision decision = gate.evaluate(fullyReady("2.4.0"));

        assertThat(decision).isInstanceOf(ReleaseDecision.Ready.class);
        assertThat(gate.blockedCount()).isZero();
    }

    @Test
    @DisplayName("a -SNAPSHOT version is blocked: a release must carry a release version (semver, key 60)")
    void blocksSnapshotVersion() {
        // Everything else is in order, but the version is a development snapshot — the gate blocks and
        // names the failed check, so the team knows to cut a release version rather than ship a snapshot
        // (a moving target that resolves differently over time — the irreproducibility a release must not have).
        ReleaseCandidate snapshot = new ReleaseCandidate(
            SemanticVersion.parse("2.5.0-SNAPSHOT"), true, true, true, true);

        ReleaseDecision decision = gate.evaluate(snapshot);

        assertThat(decision).isInstanceOf(ReleaseDecision.Blocked.class);
        assertThat(((ReleaseDecision.Blocked) decision).failures())
            .containsExactly(ReleaseCheck.RELEASE_VERSION);
        assertThat(gate.blockedCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("a missing changelog entry blocks the release")
    void blocksMissingChangelogEntry() {
        ReleaseCandidate noChangelog = new ReleaseCandidate(
            SemanticVersion.parse("2.4.1"), false, true, true, true);

        ReleaseDecision decision = gate.evaluate(noChangelog);

        assertThat(decision).isInstanceOf(ReleaseDecision.Blocked.class);
        assertThat(((ReleaseDecision.Blocked) decision).failures())
            .containsExactly(ReleaseCheck.CHANGELOG_ENTRY);
    }

    @Test
    @DisplayName("a red pipeline on the release commit blocks the release")
    void blocksWhenCiNotGreen() {
        ReleaseCandidate redCi = new ReleaseCandidate(
            SemanticVersion.parse("2.4.1"), true, false, true, true);

        assertThat(gate.evaluate(redCi)).isInstanceOf(ReleaseDecision.Blocked.class);
    }

    @Test
    @DisplayName("a blocked release names every failed precondition, not merely the first")
    void blockedDecisionListsAllFailures() {
        // A snapshot with no changelog, a red pipeline, unsigned, and un-smoke-tested fails every gate;
        // the block must list them all so the team fixes the release in one pass, not one rejection at a time.
        ReleaseCandidate broken = new ReleaseCandidate(
            SemanticVersion.parse("2.5.0-SNAPSHOT"), false, false, false, false);

        ReleaseDecision decision = gate.evaluate(broken);

        assertThat(decision).isInstanceOf(ReleaseDecision.Blocked.class);
        assertThat(((ReleaseDecision.Blocked) decision).failures())
            .containsExactlyInAnyOrder(
                ReleaseCheck.RELEASE_VERSION,
                ReleaseCheck.CHANGELOG_ENTRY,
                ReleaseCheck.CI_GREEN,
                ReleaseCheck.SIGNED_WITH_SBOM,
                ReleaseCheck.SMOKE_TESTED);
    }

    @Test
    @DisplayName("the externalized dev profile is more forgiving than prod")
    void devProfileRequiresFewerChecks() {
        // The same unsigned, un-smoke-tested candidate that prod blocks is ready under dev, which (per its
        // properties file) requires only a release version and a changelog entry — progressive ceremony.
        ReleaseReadiness devGate = new ReleaseReadiness(ReleasePolicy.load("dev"));
        ReleaseCandidate stagingBuild = new ReleaseCandidate(
            SemanticVersion.parse("2.4.0"), true, false, false, false);

        assertThat(devGate.evaluate(stagingBuild)).isInstanceOf(ReleaseDecision.Ready.class);
        assertThat(gate.evaluate(stagingBuild)).isInstanceOf(ReleaseDecision.Blocked.class);
    }

    @Test
    @DisplayName("the dev and prod profiles require different precondition sets")
    void profilesDifferInRequiredChecks() {
        assertThat(ReleasePolicy.load("prod").required())
            .contains(ReleaseCheck.SIGNED_WITH_SBOM, ReleaseCheck.SMOKE_TESTED);
        assertThat(ReleasePolicy.load("dev").required())
            .doesNotContain(ReleaseCheck.SIGNED_WITH_SBOM, ReleaseCheck.SMOKE_TESTED);
    }

    @Test
    @DisplayName("the gate reports ready once a policy is wired")
    void readinessReflectsWiredPolicy() {
        assertThat(gate.isReady()).isTrue();
    }

    @Test
    @DisplayName("an unknown profile is rejected at load")
    void unknownProfileIsRejected() {
        assertThatThrownBy(() -> ReleasePolicy.load("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
