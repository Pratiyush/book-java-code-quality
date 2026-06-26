package org.acme.remediation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's mechanism: the churn &times; pain prioritization, the ordered playbook, the
 * automation engine's before/after equivalence, the program-health surface, the externalized config
 * profiles, and the failure path that rejects the two unsound plans the chapter names loudly. Each public
 * behavior — including every failure branch — is driven by a test, so the module's claims are verified, not
 * asserted.
 */
@DisplayName("Chapter 40 — remediation playbook & automated change")
class RemediationPlaybookTest {

    @Nested
    @DisplayName("prioritize by churn x pain")
    class Prioritization {

        @Test
        @DisplayName("ranks the active hotspot above more complex but frozen code")
        void ranksByInterestNotAbsoluteComplexity() {
            DebtItem frozenButComplex = new DebtItem("AncientReportGenerator", 0, 90, false);
            DebtItem activeHotspot = new DebtItem("CheckoutService", 30, 25, false);
            DebtItem mild = new DebtItem("AddressFormatter", 4, 6, true);

            List<DebtItem> ranked =
                    RemediationPrioritizer.rankByInterest(List.of(frozenButComplex, activeHotspot, mild));

            // CheckoutService (30*25=750) outranks the more complex but frozen class (0*90=0).
            assertThat(ranked).extracting(DebtItem::name)
                    .containsExactly("CheckoutService", "AddressFormatter", "AncientReportGenerator");
        }

        @Test
        @DisplayName("declines to fix frozen code even when the budget has room")
        void selectsHotspotsAndSkipsFrozen() {
            DebtItem frozen = new DebtItem("AncientReportGenerator", 0, 90, false);
            DebtItem hotspot = new DebtItem("CheckoutService", 30, 25, false);

            List<DebtItem> chosen = RemediationPrioritizer.selectHotspots(List.of(frozen, hotspot), 5);

            // Budget was 5, but frozen code accrues no interest, so it is left alone — not padded in.
            assertThat(chosen).extracting(DebtItem::name).containsExactly("CheckoutService");
        }

        @Test
        @DisplayName("frozen code reports zero interest regardless of complexity")
        void frozenCodeHasNoInterest() {
            assertThat(new DebtItem("Ancient", 0, 1000, false).interest()).isZero();
            assertThat(new DebtItem("Hotspot", 12, 12, false).interest()).isEqualTo(144L);
        }
    }

    @Nested
    @DisplayName("the ordered playbook")
    class Playbook {

        @Test
        @DisplayName("the canonical sequence runs assess -> gate -> net -> hotspots -> strangle -> sustain")
        void canonicalOrder() {
            assertThat(PlaybookStep.values()).containsExactly(
                    PlaybookStep.ASSESS_AND_BASELINE,
                    PlaybookStep.GATE_NEW_CODE,
                    PlaybookStep.SAFETY_NET,
                    PlaybookStep.HOTSPOT_PAYDOWN,
                    PlaybookStep.STRANGLE_UNSALVAGEABLE,
                    PlaybookStep.SUSTAIN);
        }

        @Test
        @DisplayName("a plan in playbook order is accepted; one that pays down before the net is flagged")
        void detectsOutOfOrderPlans() {
            RemediationPlan inOrder = new RemediationPlan(
                    List.of(PlaybookStep.ASSESS_AND_BASELINE, PlaybookStep.GATE_NEW_CODE,
                            PlaybookStep.SAFETY_NET, PlaybookStep.HOTSPOT_PAYDOWN),
                    List.of(), true);
            RemediationPlan outOfOrder = new RemediationPlan(
                    List.of(PlaybookStep.HOTSPOT_PAYDOWN, PlaybookStep.SAFETY_NET),
                    List.of(), true);

            assertThat(inOrder.isInPlaybookOrder()).isTrue();
            assertThat(outOfOrder.isInPlaybookOrder()).isFalse();
        }
    }

    @Nested
    @DisplayName("the failure path — reject the unsound plan")
    class FailurePath {

        @Test
        @DisplayName("a baseline with no paydown plan is rejected as formalized ignoring")
        void rejectsBaselineWithoutPaydown() {
            assertThatThrownBy(() -> new RemediationPlan(
                    List.of(PlaybookStep.ASSESS_AND_BASELINE, PlaybookStep.GATE_NEW_CODE),
                    List.of(),
                    false))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("formalized ignoring");
        }

        @Test
        @DisplayName("a baseline paired with a paydown plan is accepted")
        void acceptsBaselineWithPaydown() {
            RemediationPlan plan = new RemediationPlan(
                    List.of(PlaybookStep.ASSESS_AND_BASELINE, PlaybookStep.HOTSPOT_PAYDOWN),
                    List.of(new DebtItem("CheckoutService", 30, 25, false)),
                    true);
            assertThat(plan.hotspots(3)).extracting(DebtItem::name).containsExactly("CheckoutService");
        }

        @Test
        @DisplayName("negative churn or complexity is rejected at construction")
        void rejectsNegativeProxies() {
            assertThatThrownBy(() -> new DebtItem("bad", -1, 5, false))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("churn");
        }
    }

    @Nested
    @DisplayName("the automation engine — before/after the recipe")
    class AutomationEngine {

        @Test
        @DisplayName("the modernized form is behavior-equivalent to the legacy form")
        void recipeOutputIsVerified() {
            // Automation proposes; the test disposes — the recipe's result is confirmed, not trusted blind.
            assertThat(org.acme.remediation.Modernized.milestones())
                    .isEqualTo(org.acme.remediation.legacy.LegacyReleaseNotes.milestones())
                    .containsExactly("baseline the past", "gate new code", "pay down hotspots");
        }

        @Test
        @DisplayName("the modernized form is immutable, like the legacy unmodifiable wrapper")
        void modernizedFormIsImmutable() {
            assertThatThrownBy(() -> Modernized.milestones().add("rewrite everything"))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("program health — sustained or stalling")
    class Health {

        @Test
        @DisplayName("a program below its committed pace reports STALLING, not green-by-default")
        void stallingBelowPace() {
            ProgramHealth health = ProgramHealth.report(12, 1, 3);
            assertThat(health.status()).isEqualTo(ProgramHealth.Status.STALLING);
        }

        @Test
        @DisplayName("a program at or above its committed pace reports SUSTAINED")
        void sustainedAtPace() {
            ProgramHealth health = ProgramHealth.report(8, 4, 3);
            assertThat(health.status()).isEqualTo(ProgramHealth.Status.SUSTAINED);
            assertThat(health.openHotspots()).isEqualTo(8);
        }
    }

    @Nested
    @DisplayName("externalized config profiles")
    class ConfigProfiles {

        @Test
        @DisplayName("the dev profile loads from the packaged resource")
        void devProfileLoadsFromResource() {
            RemediationConfig config = RemediationConfig.load();
            assertThat(config.profile()).isEqualTo("dev");
            assertThat(config.paydownBudget()).isEqualTo(3);
            assertThat(config.minPace()).isEqualTo(2);
        }

        @Test
        @DisplayName("the prod profile overrides the dev defaults")
        void prodProfileOverridesDefaults() {
            Properties props = new Properties();
            props.setProperty("paydown.budget", "3");
            props.setProperty("silenced.budget", "25");
            props.setProperty("paydown.min-pace", "2");
            props.setProperty("prod.paydown.budget", "8");
            props.setProperty("prod.silenced.budget", "10");
            props.setProperty("prod.paydown.min-pace", "5");

            RemediationConfig prod = RemediationConfig.from(props, "prod");

            assertThat(prod.paydownBudget()).isEqualTo(8);
            assertThat(prod.silencedBudget()).isEqualTo(10);
            assertThat(prod.minPace()).isEqualTo(5);
        }
    }
}
