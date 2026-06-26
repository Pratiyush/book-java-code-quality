package org.acme.supplychain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's mechanism end to end: the SBOM as a fast query, and the SCA gate's pass/block
 * decision including its two honest limits (reachability and reviewed suppression) and its failure path.
 * Mirrors what the build configuration does — CycloneDX inventories the graph, OWASP Dependency-Check
 * fails the build above a CVSS threshold — at a level a test can drive deterministically and offline.
 */
class SupplyChainTest {

    private ComponentInventory inventory;
    private SbomComponent commonsLang;

    @BeforeEach
    void setUp() {
        inventory = new ComponentInventory();
        commonsLang = new SbomComponent(
                "commons-lang3", "3.18.0", "pkg:maven/org.apache.commons/commons-lang3@3.18.0");
        inventory.record(commonsLang);
    }

    @Test
    @DisplayName("SBOM turns 'are we affected by X?' into a query, not an archaeology project")
    void inventoryAnswersTheLog4ShellQuery() {
        assertThat(inventory.findByName("commons-lang3")).contains(commonsLang);
        assertThat(inventory.findByName("log4j-core")).isEmpty();
    }

    @Test
    @DisplayName("Recording the same component twice is idempotent — an inventory lists each component once")
    void inventoryRecordsEachComponentOnce() {
        inventory.record(commonsLang);
        assertThat(inventory.componentCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Observability + readiness: an empty inventory is not ready; a populated one is")
    void inventoryExposesCountAndReadiness() {
        ComponentInventory empty = new ComponentInventory();
        assertThat(empty.isReady()).isFalse();
        assertThat(empty.componentCount()).isZero();

        assertThat(inventory.isReady()).isTrue();
        assertThat(inventory.componentCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("An SBOM is an inventory, not a defense — the type says so, in code")
    void inventoryIsNotADefense() {
        assertThat(inventory.isInventoryNotDefense()).isTrue();
        assertThat(inventory.components()).containsExactly(commonsLang);
    }

    @Test
    @DisplayName("Failure path: a reachable, unsuppressed, at-or-above-threshold finding fails the gate")
    void gateBlocksOnReachableHighSeverityFinding() {
        VulnerabilityGate gate = new VulnerabilityGate(7.0, Set.of());
        ScanFinding finding = new ScanFinding("log4j-core", "CVE-2021-44228", Severity.CRITICAL, true);

        assertThat(gate.isBlocking(finding)).isTrue();
        assertThatExceptionOfType(UnsuppressedHighSeverityFindingException.class)
                .isThrownBy(() -> gate.evaluate(List.of(finding)))
                .satisfies(ex -> assertThat(ex.blockingFindings()).containsExactly(finding));
    }

    @Test
    @DisplayName("'Vulnerable' is not 'exploitable': an UNREACHABLE high-severity finding does not block")
    void gateTriagesUnreachableFinding() {
        VulnerabilityGate gate = new VulnerabilityGate(7.0, Set.of());
        ScanFinding unreachable = new ScanFinding("some-lib", "CVE-2024-99999", Severity.HIGH, false);

        assertThat(gate.isBlocking(unreachable)).isFalse();
        // A scan of only this finding returns normally — reported, triaged by exposure, not a fire.
        gate.evaluate(List.of(unreachable));
    }

    @Test
    @DisplayName("Reviewed suppression clears a finding from the gate (with a reason, never blanket)")
    void gateHonoursReviewedSuppression() {
        VulnerabilityGate gate = new VulnerabilityGate(7.0, Set.of("CVE-2024-12345"));
        ScanFinding suppressed = new ScanFinding("some-lib", "CVE-2024-12345", Severity.HIGH, true);

        assertThat(gate.isBlocking(suppressed)).isFalse();
        gate.evaluate(List.of(suppressed));
    }

    @Test
    @DisplayName("A finding below the CVSS threshold does not block (the gate fails on severity)")
    void gateLetsBelowThresholdFindingsPass() {
        VulnerabilityGate gate = new VulnerabilityGate(7.0, Set.of());
        ScanFinding medium = new ScanFinding("some-lib", "CVE-2024-22222", Severity.MEDIUM, true);

        assertThat(gate.isBlocking(medium)).isFalse();
        gate.evaluate(List.of(medium));
    }

    @Test
    @DisplayName("A clean gate result is not a clean bill of health — it only means no blocking finding")
    void cleanGateNamesBothBlockersOutOfAMixedScan() {
        VulnerabilityGate gate = new VulnerabilityGate(7.0, Set.of("CVE-2024-12345"));
        List<ScanFinding> mixed = List.of(
                new ScanFinding("a", "CVE-2024-11111", Severity.CRITICAL, true),   // blocks
                new ScanFinding("b", "CVE-2024-12345", Severity.HIGH, true),       // suppressed
                new ScanFinding("c", "CVE-2024-13131", Severity.HIGH, false),      // unreachable
                new ScanFinding("d", "CVE-2024-14141", Severity.LOW, true),        // below threshold
                new ScanFinding("e", "CVE-2024-15151", Severity.HIGH, true));      // blocks

        assertThatThrownBy(() -> gate.evaluate(mixed))
                .isInstanceOf(UnsuppressedHighSeverityFindingException.class)
                .satisfies(ex -> {
                    var blocking = ((UnsuppressedHighSeverityFindingException) ex).blockingFindings();
                    assertThat(blocking).extracting(ScanFinding::identifier)
                            .containsExactly("CVE-2024-11111", "CVE-2024-15151");
                });
    }
}
