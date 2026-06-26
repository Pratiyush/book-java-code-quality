package org.acme.findings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Proves the chapter's finding-management discipline as runnable behaviour: the triage decision, the
 * baseline ratchet that grandfathers the past while failing new findings, the reviewed false positive
 * that is load-bearing in the SpotBugs gate, the legacy/clean contrast, the defined failure path, and the
 * gate-health observability surface.
 *
 * <p>Exercising the policy is the point. The real freezing is done by the SpotBugs and Checkstyle config
 * under {@code config/}; these tests verify the rules those configs embody, so the prose, the config, and
 * the code agree.
 */
class FindingManagementTest {

    @Nested
    @DisplayName("Triage: every finding maps to exactly one lever")
    class Triage {

        @Test
        void aRealNewDefectIsFixed() {
            Finding f = Finding.realDefect("SpotBugs", "NP_NULL_ON_SOME_PATH", "Checkout#total");
            assertThat(FindingTriage.triage(f)).isEqualTo(Disposition.FIX);
        }

        @Test
        void aJudgedFalsePositiveIsSuppressed() {
            Finding f = Finding.judgedFalsePositive(
                    "SpotBugs", "EI_EXPOSE_REP", "PriceFormatter#denominationsCents",
                    "fixed denominations, never mutated by callers");
            assertThat(FindingTriage.triage(f)).isEqualTo(Disposition.SUPPRESS);
        }

        @Test
        void preExistingDebtIsBaselined() {
            Finding f = Finding.legacy("SpotBugs", "EI_EXPOSE_REP", "LegacyPriceTable#rawTiers");
            assertThat(FindingTriage.triage(f)).isEqualTo(Disposition.BASELINE);
        }

        @Test
        void aFalsePositiveWithoutAReasonCannotBeRepresented() {
            // The type refuses to model an unjustified suppression — a suppression is a claim needing evidence.
            assertThatThrownBy(() -> new Finding("SpotBugs", "EI_EXPOSE_REP", "X#y", false, true, "  "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("justification");
        }
    }

    @Nested
    @DisplayName("Ratchet: freeze the past, block the future")
    class Ratchet {

        private final Finding legacyA = Finding.legacy("SpotBugs", "EI_EXPOSE_REP", "LegacyPriceTable#rawTiers");
        private final Finding legacyB = Finding.legacy("Checkstyle", "MagicNumber", "LegacyPriceTable#size");

        @Test
        void aRunWithOnlyBaselinedFindingsPasses() {
            FindingRatchet ratchet = new FindingRatchet(List.of(legacyA, legacyB));
            // The current run reports exactly the frozen set — nothing new escaped.
            assertThat(ratchet.passes(List.of(legacyA, legacyB))).isTrue();
            assertThat(ratchet.newFindings(List.of(legacyA, legacyB))).isEmpty();
            assertThat(ratchet.baselineSize()).isEqualTo(2);
        }

        @Test
        void aNewFindingFailsTheRatchetWhileLegacyIsGrandfathered() {
            FindingRatchet ratchet = new FindingRatchet(List.of(legacyA, legacyB));
            Finding fresh = Finding.realDefect("SpotBugs", "EI_EXPOSE_REP", "PricingCatalog#priceTiers");
            // The same rule key at a NEW location is not grandfathered: this is the whole value proposition.
            List<Finding> escaped = ratchet.newFindings(List.of(legacyA, legacyB, fresh));
            assertThat(ratchet.passes(List.of(legacyA, legacyB, fresh))).isFalse();
            assertThat(escaped).containsExactly(fresh);
        }

        @Test
        void fixingALegacyFindingShrinksWhatTheRatchetWouldCarry() {
            // Drive the baseline down: a run missing legacyB has nothing new, and a tighter baseline holds.
            FindingRatchet tightened = new FindingRatchet(List.of(legacyA));
            assertThat(tightened.passes(List.of(legacyA))).isTrue();
            assertThat(tightened.baselineSize()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("The reviewed false positive is load-bearing and narrow")
    class ReviewedSuppression {

        @Test
        void theSuppressedAccessorStillWorks() {
            // PriceFormatter#denominationsCents carries the one @SuppressFBWarnings(justification=...).
            // The SpotBugs gate proves the suppression is load-bearing; here we prove the code is correct.
            PriceFormatter formatter = new PriceFormatter();
            assertThat(formatter.denominationsCents()).containsExactly(1L, 5L, 10L, 25L, 100L, 500L, 1_000L, 2_000L);
            assertThat(formatter.format(599L)).isEqualTo("5.99");
        }

        @Test
        void formattingRejectsNegativeAmounts() {
            assertThatThrownBy(() -> new PriceFormatter().format(-1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("must not be negative");
        }
    }

    @Nested
    @DisplayName("Legacy vs. clean: the same shape, with and without the finding")
    class LegacyVersusClean {

        @Test
        void theLegacyTableExposesItsArrayWhileTheCleanCatalogDoesNot() {
            long[] tiers = {199L, 499L, 999L};

            // Legacy: the returned array IS the internal one (the baselined EI_EXPOSE_REP).
            LegacyPriceTable legacy = new LegacyPriceTable(tiers);
            assertThat(legacy.rawTiers()).isSameAs(legacy.rawTiers());

            // Clean: priceTiers() returns a fresh copy every call — no exposed representation.
            PricingCatalog clean = new PricingCatalog(Map.of("SKU-1", 199L), tiers);
            assertThat(clean.priceTiers()).isNotSameAs(clean.priceTiers());
            assertThat(clean.priceTiers()).containsExactly(199L, 499L, 999L);
        }
    }

    @Nested
    @DisplayName("The defined failure path")
    class FailurePath {

        @Test
        void anUnknownSkuReturnsEmptyRatherThanThrowing() {
            PricingCatalog catalog = new PricingCatalog(Map.of("SKU-1", 1_499L), new long[] {0L});
            assertThat(catalog.priceFor("SKU-1")).contains(1_499L);
            assertThat(catalog.priceFor("MISSING")).isEmpty();
        }

        @Test
        void aNegativePriceFailsFastAtConstruction() {
            assertThatThrownBy(() -> new PricingCatalog(Map.of("BAD", -1L), new long[] {0L}))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("must not be negative");
        }
    }

    @Nested
    @DisplayName("Gate health: silenced debt stays visible")
    class Health {

        private final Finding legacy = Finding.legacy("SpotBugs", "EI_EXPOSE_REP", "LegacyPriceTable#rawTiers");
        private final Finding suppressed = Finding.judgedFalsePositive(
                "SpotBugs", "EI_EXPOSE_REP", "PriceFormatter#denominationsCents", "fixed, never mutated");

        @Test
        void aGreenGateWithinBudgetReportsReady() {
            FindingRatchet ratchet = new FindingRatchet(List.of(legacy));
            GateHealth health = GateHealth.report(ratchet, List.of(suppressed), List.of(legacy), 5);
            assertThat(health.status()).isEqualTo(GateHealth.Status.READY);
            assertThat(health.silencedTotal()).isEqualTo(2);
            assertThat(health.newFindings()).isEmpty();
        }

        @Test
        void aSilencedSetOverBudgetReportsDegradedWithoutChangingGreen() {
            FindingRatchet ratchet = new FindingRatchet(List.of(legacy));
            // Budget of 1; two findings are silenced (one baselined, one suppressed) — over budget.
            GateHealth health = GateHealth.report(ratchet, List.of(suppressed), List.of(legacy), 1);
            assertThat(health.status()).isEqualTo(GateHealth.Status.DEGRADED);
            assertThat(health.newFindings()).isEmpty(); // still green; health is a signal, not a verdict
        }

        @Test
        void anEscapedNewFindingShowsUpInHealth() {
            FindingRatchet ratchet = new FindingRatchet(List.of(legacy));
            Finding fresh = Finding.realDefect("SpotBugs", "EI_EXPOSE_REP", "PricingCatalog#priceTiers");
            GateHealth health = GateHealth.report(ratchet, List.of(suppressed), List.of(legacy, fresh), 5);
            assertThat(health.newFindings()).containsExactly(fresh);
        }
    }
}
