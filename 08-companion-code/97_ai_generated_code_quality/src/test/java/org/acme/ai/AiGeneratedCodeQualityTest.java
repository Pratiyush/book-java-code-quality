package org.acme.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Exercises both halves of the chapter on the storefront domain. The security half proves the reviewed
 * fix binds its input while the AI-drafted counter-example folds the input into the query text (the
 * defect demonstrated, never wired into a running path). The test-generation half shows the contrast
 * the chapter draws: a test generated from the implementation reaches full line coverage while leaving
 * the boundary and arithmetic mutants alive, and a spec-derived test kills them. The {@code -Pquality}
 * build adds the analyzer's view: the AI draft's string-concatenated query carries a narrow, reasoned
 * SpotBugs suppression naming this test, so the deliberate defect ships without weakening the gate.
 */
class AiGeneratedCodeQualityTest {

    @Nested
    @DisplayName("the AI draft goes through the same gate as human code")
    class AiDraftGoesThroughTheGate {

        @Test
        void reviewedFixBindsTheEmailAsDataNotSql() throws Exception {
            FakeConnection connection = new FakeConnection(List.of("c-1", "c-2"));
            ReviewedLookup lookup = new ReviewedLookup(connection.asConnection());

            List<String> ids = lookup.findIdsByEmail("alice@example.test' OR '1'='1");

            assertThat(ids).containsExactly("c-1", "c-2");
            // The reviewed fix binds the value: the query text is the constant '?' form, never the input.
            assertThat(connection.lastSql()).isEqualTo("SELECT id FROM customers WHERE email = ?");
            assertThat(connection.lastBoundValue()).isEqualTo("alice@example.test' OR '1'='1");
        }

        @Test
        void stringConcatenationFoldsTheInputIntoTheQueryText() throws Exception {
            // The AI-drafted counter-example is exercised for behaviour only, to show the inherited
            // defect concretely: the attacker's syntax becomes part of the SQL string, not data.
            FakeConnection connection = new FakeConnection(List.of("c-1"));
            AiDraftedLookup drafted = new AiDraftedLookup(connection.asConnection());

            drafted.findIdsByEmail("x' OR '1'='1");

            assertThat(connection.lastSql())
                .isEqualTo("SELECT id FROM customers WHERE email = 'x' OR '1'='1'");
            assertThat(connection.lastBoundValue()).isNull();
        }
    }

    @Nested
    @DisplayName("the tests-from-code trap: full coverage, mutants survive")
    class AiTestGeneratedFromTheCode {

        private final OrderTotals totals = new OrderTotals();

        @Test
        @DisplayName("executes every line of payableTotal() but asserts only non-null (the trap)")
        void coversEveryLineWithoutCheckingBehaviour() {
            Money below = new Money(4_000L, "USD");
            Money atOrAbove = new Money(5_000L, "USD");
            Money fee = new Money(500L, "USD");
            // tag::weak-test[]
            // Runs the paid-shipping path AND the free-shipping path: full line coverage...
            Money belowResult = totals.payableTotal(below, fee);
            Money atOrAboveResult = totals.payableTotal(atOrAbove, fee);
            // ...but the only assertion is "not null", so every mutant on payableTotal() survives.
            assertThat(belowResult).isNotNull();
            assertThat(atOrAboveResult).isNotNull();
            // end::weak-test[]
        }
    }

    @Nested
    @DisplayName("the spec-derived test: encodes intended behaviour, kills the mutants")
    class SpecDerivedTest {

        private final OrderTotals totals = new OrderTotals();
        private final Money fee = new Money(500L, "USD");

        @Test
        @DisplayName("the free-shipping boundary and the added fee are pinned (kills the mutants)")
        void pinsTheBoundaryAndTheArithmetic() {
            // tag::strong-test[]
            // Below the threshold the fee is added; at the threshold shipping is free. Pinning BOTH
            // sides kills the >=/> boundary mutant, and the exact paid total kills the MATH mutant.
            assertThat(totals.payableTotal(new Money(4_999L, "USD"), fee))
                .isEqualTo(new Money(5_499L, "USD"));
            assertThat(totals.payableTotal(new Money(5_000L, "USD"), fee))
                .isEqualTo(new Money(5_000L, "USD"));
            // end::strong-test[]
        }

        @Test
        @DisplayName("the observability counter advances only on the free-shipping path")
        void countsOnlyFreeShippingGrants() {
            OrderTotals counted = new OrderTotals();
            counted.payableTotal(new Money(4_000L, "USD"), fee);  // paid shipping: no grant
            counted.payableTotal(new Money(6_000L, "USD"), fee);  // free shipping: one grant
            assertThat(counted.freeShippingGranted()).isEqualTo(1L);
        }

        @Test
        @DisplayName("a negative amount is rejected (the explicit failure path)")
        void rejectsNegativeAmounts() {
            assertThat(catchThrowableOfType(IllegalArgumentException.class,
                () -> totals.payableTotal(new Money(-1L, "USD"), fee))).isNotNull();
        }
    }

    @Nested
    @DisplayName("the gate's failure path and surfaces")
    class GateFailurePathAndSurfaces {

        @Test
        void gateAcceptsAWellFormedLookupThroughTheReviewedFixOnly() throws Exception {
            FakeConnection connection = new FakeConnection(List.of("c-1"));
            AiReviewGate gate = new AiReviewGate(AiReviewProfile.load("dev"));

            List<String> ids = gate.acceptLookup(connection.asConnection(), "alice@example.test", null);

            assertThat(ids).containsExactly("c-1");
            assertThat(connection.lastSql()).isEqualTo("SELECT id FROM customers WHERE email = ?");
            assertThat(gate.rejectedContributionCount()).isZero();
        }

        @Test
        void gateRejectsAMalformedLookupWithAStableCode() {
            FakeConnection connection = new FakeConnection(List.of());
            AiReviewGate gate = new AiReviewGate(AiReviewProfile.load("dev"));

            RejectedContributionException rejection = catchThrowableOfType(
                RejectedContributionException.class,
                () -> gate.acceptLookup(connection.asConnection(), "no-at-sign", null));

            assertThat(rejection.code()).isEqualTo("malformed-body");
            assertThat(gate.rejectedContributionCount()).isEqualTo(1L);
        }

        @Test
        void prodPostureRejectsAContributionWithNoProvenance() {
            FakeConnection connection = new FakeConnection(List.of());
            AiReviewGate gate = new AiReviewGate(AiReviewProfile.load("prod"));

            RejectedContributionException rejection = catchThrowableOfType(
                RejectedContributionException.class,
                () -> gate.acceptLookup(connection.asConnection(), "alice@example.test", null));

            assertThat(rejection.code()).isEqualTo("provenance-missing");
        }

        @Test
        void gateRejectsAnOversizedLookupBeforeRunningIt() {
            FakeConnection connection = new FakeConnection(List.of());
            AiReviewGate gate = new AiReviewGate(AiReviewProfile.load("dev"));
            String oversized = "x".repeat(AiReviewProfile.load("dev").maxBodyChars() + 1);

            RejectedContributionException rejection = catchThrowableOfType(
                RejectedContributionException.class,
                () -> gate.acceptLookup(connection.asConnection(), oversized, null));

            assertThat(rejection.code()).isEqualTo("body-too-large");
        }

        @Test
        void readinessProbeReportsReadyWhenTheReviewedPathRuns() {
            FakeConnection connection = new FakeConnection(List.of("c-1"));
            assertThat(new AiReviewGate(AiReviewProfile.load("dev"))
                .isReady(connection.asConnection())).isTrue();
        }
    }

    @Nested
    @DisplayName("externalized config profiles")
    class ExternalizedConfig {

        @Test
        void prodRequiresProvenanceWhileDevDoesNot() {
            AiReviewProfile dev = AiReviewProfile.load("dev");
            AiReviewProfile prod = AiReviewProfile.load("prod");

            assertThat(dev.requireProvenance()).isFalse();
            assertThat(prod.requireProvenance()).isTrue();
            assertThat(dev.maxBodyChars()).isEqualTo(prod.maxBodyChars());
        }
    }
}
