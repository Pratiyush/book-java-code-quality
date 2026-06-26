package org.acme.coverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test: load an externalized profile from the classpath and run the whole gate over it,
 * the way the CI step does. This exercises the wiring the chapter teaches end to end —
 * {@code coverage-<profile>.properties} → {@link CoveragePolicy} → {@link CoverageGate} verdict —
 * rather than any one piece in isolation.
 */
class CoverageProfilesIntegrationTest {

    @Test
    @DisplayName("the prod profile loads from the classpath and enforces an 80% new-code bar with a ratchet")
    void prodProfileGatesNewCode() {
        CoveragePolicy prod = CoverageProfiles.load("prod");
        assertThat(prod.newCodeBar()).isEqualTo(0.80);
        assertThat(prod.ratchet()).isTrue();
        assertThat(prod.overallTarget()).isEqualTo(0.85);

        CoverageGate gate = new CoverageGate(prod);
        assertThat(gate.isReady()).isTrue();

        // New code at 70% under the prod 80% bar: blocked end to end.
        CoverageVerdict blocked = gate.evaluate(new CoverageDelta(0.84, 0.85, 50, 0.70));
        assertThat(blocked).isInstanceOf(CoverageVerdict.Block.class);
        assertThat(gate.blockedCount()).isEqualTo(1L);

        // New code at 95%, overall climbing past the target: clear to merge.
        CoverageVerdict passed = gate.evaluate(new CoverageDelta(0.84, 0.86, 50, 0.95));
        assertThat(passed).isInstanceOf(CoverageVerdict.Pass.class);
    }

    @Test
    @DisplayName("the dev profile is gentler than prod but keeps the ratchet")
    void devProfileIsGentler() {
        CoveragePolicy dev = CoverageProfiles.load("dev");
        assertThat(dev.newCodeBar()).isEqualTo(0.70);
        assertThat(dev.ratchet()).isTrue();
        // 75% new code passes the dev bar but would fail prod's.
        CoverageGate devGate = new CoverageGate(dev);
        assertThat(devGate.evaluate(new CoverageDelta(0.74, 0.76, 30, 0.75)).blocks()).isFalse();
    }

    @Test
    @DisplayName("a partial profile falls back to documented defaults for missing keys")
    void partialProfileUsesDefaults() {
        Properties partial = new Properties();
        partial.setProperty("coverage.newCodeBar", "0.90");
        // ratchet and overallTarget omitted -> defaults (ratchet true, target 0.85).
        CoveragePolicy policy = CoveragePolicy.from(partial);
        assertThat(policy.newCodeBar()).isEqualTo(0.90);
        assertThat(policy.ratchet()).isTrue();
        assertThat(policy.overallTarget()).isEqualTo(0.85);
    }

    @Test
    @DisplayName("selecting an unknown profile fails fast rather than gating the wrong way")
    void unknownProfileFailsFast() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> CoverageProfiles.load("does-not-exist"))
            .withMessageContaining("does-not-exist");
    }
}
