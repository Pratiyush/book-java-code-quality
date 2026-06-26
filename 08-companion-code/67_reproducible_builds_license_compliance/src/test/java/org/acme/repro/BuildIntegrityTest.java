package org.acme.repro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's two facets end to end: the reproducible build as a verify-by-rebuilding digest
 * comparison, and the license allow-list gate's pass/block decision including its failure path and its
 * honest limits (transitive surprise, tuned-to-distribution-mode). Mirrors what the build configuration
 * does — a fixed timestamp + the reproducible-build plugin make the artifact deterministic, and the
 * license-maven-plugin gates on an allow-list — at a level a test can drive deterministically and offline.
 */
class BuildIntegrityTest {

    // --- Technical integrity: the reproducible build ---------------------------------------------------

    @Test
    @DisplayName("Verify by rebuilding: two builds of the same source produce a byte-identical artifact")
    void reproducibleBuildProducesIdenticalDigest() {
        ReproducibleArtifact firstBuild = new ReproducibleArtifact("app-1.0.0.jar", "sha256:abc123");
        ReproducibleArtifact rebuild = new ReproducibleArtifact("app-1.0.0.jar", "sha256:abc123");

        assertThat(firstBuild.matches(rebuild)).isTrue();
    }

    @Test
    @DisplayName("Non-determinism creeps back in: a differing digest is the signal a CI verify step catches")
    void differingDigestSignalsNonDeterminism() {
        ReproducibleArtifact firstBuild = new ReproducibleArtifact("app-1.0.0.jar", "sha256:abc123");
        ReproducibleArtifact driftedBuild = new ReproducibleArtifact("app-1.0.0.jar", "sha256:def456");

        assertThat(firstBuild.matches(driftedBuild)).isFalse();
    }

    @Test
    @DisplayName("Reproducibility proves integrity, not correctness — the type says so, in code")
    void reproducibilityIsIntegrityNotCorrectness() {
        ReproducibleArtifact artifact = new ReproducibleArtifact("app-1.0.0.jar", "sha256:abc123");

        assertThat(artifact.isIntegrityNotCorrectness()).isTrue();
        assertThat(artifact.isVerifiable()).isTrue();
        assertThat(artifact.digest()).isEqualTo("sha256:abc123");
    }

    // --- Legal integrity: the license allow-list gate -------------------------------------------------

    @Test
    @DisplayName("A tree of allowed (permissive) licenses passes the policy gate")
    void allowedLicensesPassTheGate() {
        LicensePolicy policy = new LicensePolicy(Set.of("Apache-2.0", "MIT", "BSD-3-Clause"));
        List<License> tree = List.of(
                new License("commons-lang3", "Apache-2.0", LicenseCategory.PERMISSIVE),
                new License("some-lib", "MIT", LicenseCategory.PERMISSIVE));

        assertThat(policy.isDisallowed(tree.get(0))).isFalse();
        policy.evaluate(tree);
    }

    @Test
    @DisplayName("Failure path: a disallowed (strong-copyleft) license fails the gate and names the offender")
    void disallowedLicenseFailsTheGate() {
        LicensePolicy policy = new LicensePolicy(Set.of("Apache-2.0", "MIT"));
        License banned = new License("some-gpl-lib", "GPL-3.0-only", LicenseCategory.STRONG_COPYLEFT);

        assertThat(policy.isDisallowed(banned)).isTrue();
        assertThatExceptionOfType(DisallowedLicenseException.class)
                .isThrownBy(() -> policy.evaluate(List.of(banned)))
                .satisfies(ex -> assertThat(ex.disallowed()).containsExactly(banned));
    }

    @Test
    @DisplayName("Transitive surprise: a copyleft TRANSITIVE under a permissive direct dep still fails")
    void transitiveSurpriseIsCaughtByScanningTheFullGraph() {
        LicensePolicy policy = new LicensePolicy(Set.of("Apache-2.0", "MIT"));
        List<License> graph = List.of(
                new License("permissive-direct", "Apache-2.0", LicenseCategory.PERMISSIVE),   // direct, allowed
                new License("copyleft-transitive", "AGPL-3.0-only", LicenseCategory.STRONG_COPYLEFT)); // pulled in

        assertThatThrownBy(() -> policy.evaluate(graph))
                .isInstanceOf(DisallowedLicenseException.class)
                .satisfies(ex -> {
                    var disallowed = ((DisallowedLicenseException) ex).disallowed();
                    assertThat(disallowed).extracting(License::component)
                            .containsExactly("copyleft-transitive");
                });
    }

    @Test
    @DisplayName("Tuned to distribution mode: the same license can be allowed under a different policy")
    void policyIsTunedToDistributionMode() {
        License lgpl = new License("some-lib", "LGPL-2.1-only", LicenseCategory.WEAK_COPYLEFT);

        // A shipped-binary policy that excludes weak copyleft blocks it...
        LicensePolicy shipped = new LicensePolicy(Set.of("Apache-2.0", "MIT"));
        assertThat(shipped.isDisallowed(lgpl)).isTrue();

        // ...while an internal-tool policy that accepts it passes. The category is factual; the policy is
        // the distribution-mode decision (not legal advice).
        LicensePolicy internal = new LicensePolicy(Set.of("Apache-2.0", "MIT", "LGPL-2.1-only"));
        assertThat(internal.isDisallowed(lgpl)).isFalse();
    }
}
