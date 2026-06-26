package org.acme.parity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the local&#8596;CI parity check: it drives the check through the whole stack —
 * the externalized policy ({@link ParityPolicy#load}) and two gate sets — and asserts each outcome the
 * chapter describes, end to end. The gate sets are fixtures naming the same checks the
 * branch-protection ruleset and the pre-commit config name; the parity logic under test is real.
 *
 * <p>Together these cases pin the chapter's central claim: a local gate set that reproduces every
 * required CI check is in parity (green locally predicts green in CI), and one that misses a required
 * check has drifted — the "works locally, fails in CI" symptom — with the drift naming the exact missing
 * check. They also confirm the dev and prod profiles differ, the way a framework's {@code %dev} /
 * {@code %prod} blocks do: prod fails the build on drift, dev surfaces it as a warning.
 */
class GateParityTest {

    // The required CI checks — the same names the branch-protection ruleset wires as required.
    private static final GateSet CI =
        new GateSet("ci", linked("build-and-lint", "test-and-coverage", "static-and-security", "quality-gate"));

    private static Set<String> linked(String... names) {
        return new LinkedHashSet<>(Set.of(names));
    }

    private final GateParity parity = new GateParity(ParityPolicy.load("prod"));

    @Test
    @DisplayName("local reproduces every required CI check: in parity")
    void inParityWhenLocalCoversCi() {
        // The local gate set runs everything CI requires (and may run more — the wrapper build runs the
        // full verify), so green locally can predict green in CI.
        GateSet local =
            new GateSet("local", linked("build-and-lint", "test-and-coverage", "static-and-security",
                "quality-gate", "spotless-format-check"));

        ParityResult result = parity.check(local, CI);

        assertThat(result).isInstanceOf(ParityResult.InParity.class);
        assertThat(result.inParity()).isTrue();
        assertThat(parity.driftCount()).isZero();
    }

    @Test
    @DisplayName("a required CI check with no local counterpart is drift, named exactly")
    void driftNamesTheMissingCheck() {
        // The local set omits the heavier static-and-security stage CI requires — exactly the gap that
        // surfaces as "works locally, fails in CI". The drift names the missing check so it is actionable.
        GateSet local = new GateSet("local", linked("build-and-lint", "test-and-coverage", "quality-gate"));

        ParityResult result = parity.check(local, CI);

        assertThat(result).isInstanceOf(ParityResult.Drifted.class);
        assertThat(((ParityResult.Drifted) result).missingLocally()).containsExactly("static-and-security");
        assertThat(parity.driftCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("parity is one-directional: extra local checks do not break parity")
    void extraLocalChecksAreFine() {
        // A developer is free to run MORE than CI; parity only requires that every CI-required check has a
        // local counterpart, not that the sets are identical.
        GateSet local =
            new GateSet("local", linked("build-and-lint", "test-and-coverage", "static-and-security",
                "quality-gate", "gitleaks", "checkstyle-fast"));

        assertThat(parity.check(local, CI).inParity()).isTrue();
    }

    @Test
    @DisplayName("the failure path: a strict (prod) policy fails fast on drift, naming the gap")
    void prodFailsFastOnDrift() {
        GateSet local = new GateSet("local", linked("build-and-lint", "test-and-coverage"));

        assertThatThrownBy(() -> parity.enforce(local, CI))
            .isInstanceOf(ParityBrokenException.class)
            .hasMessageContaining("static-and-security")
            .hasMessageContaining("quality-gate");
    }

    @Test
    @DisplayName("the dev profile is more forgiving: drift is surfaced as a warning, not a failure")
    void devProfileSurfacesDriftAsWarning() {
        // dev keeps parity.fail-on-drift=false, so enforce returns the drifted result instead of throwing
        // — feedback at the keyboard, the way a pre-commit hook is feedback rather than enforcement.
        GateParity devParity = new GateParity(ParityPolicy.load("dev"));
        GateSet local = new GateSet("local", linked("build-and-lint", "test-and-coverage"));

        ParityResult result = devParity.enforce(local, CI);

        assertThat(result).isInstanceOf(ParityResult.Drifted.class);
        assertThat(result.inParity()).isFalse();
    }

    @Test
    @DisplayName("a strict policy that finds parity returns normally")
    void enforceReturnsWhenInParity() {
        GateSet local =
            new GateSet("local", linked("build-and-lint", "test-and-coverage", "static-and-security",
                "quality-gate"));

        assertThat(parity.enforce(local, CI).inParity()).isTrue();
    }

    @Test
    @DisplayName("the parity check reports ready once a policy is wired")
    void readinessReflectsWiredPolicy() {
        assertThat(parity.isReady()).isTrue();
    }

    @Test
    @DisplayName("an unknown profile is rejected at load")
    void unknownProfileIsRejected() {
        assertThatThrownBy(() -> ParityPolicy.load("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("the prod profile fails on drift; the dev profile does not")
    void profilesDifferOnFailOnDrift() {
        assertThat(ParityPolicy.load("prod").failOnDrift()).isTrue();
        assertThat(ParityPolicy.load("dev").failOnDrift()).isFalse();
    }

    @Test
    @DisplayName("a gate set rejects a null check name")
    void gateSetRejectsNullCheck() {
        Set<String> withNull = new LinkedHashSet<>();
        withNull.add(null);
        assertThatThrownBy(() -> new GateSet("bad", withNull))
            .isInstanceOf(NullPointerException.class);
    }
}
