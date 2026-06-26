package org.acme.storefront.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import org.acme.storefront.money.catalog.PricedItem;
import org.acme.storefront.money.legacy.LegacyOrderLine;
import org.junit.jupiter.api.Test;

/**
 * Exercises the house invariant the way the chapter describes: each realization fires on the bad fixture
 * ({@link LegacyOrderLine}, which exposes {@code double} money) and is silent on the good one
 * ({@link PricedItem}, which uses {@link Money}). It also checks the codegen comparison, the externalized
 * dev/prod profiles, the observability surface, and the failure path. The ArchUnit realization has its
 * own test ({@code MoneyArchTest}) because it is driven through an imported class graph.
 */
class MoneyRuleTest {

    private static final Currency USD = Currency.getInstance("USD");

    // ----- Realization 1: the hand-written runtime guard -----

    @Test
    void handWrittenGuardBuildsExactMoneyAndRejectsBlankInput() {
        Money money = MoneyGuards.of("19.99", "USD");
        assertThat(money.amount()).isEqualByComparingTo("19.99");

        // The failure path: the guard refuses to build an invalid Money rather than letting it exist.
        assertThatIllegalArgumentException().isThrownBy(() -> MoneyGuards.of("  ", "USD"));
    }

    @Test
    void handWrittenPredicateFiresOnDoubleAndIsSilentOnBigDecimal() {
        assertThat(MoneyGuards.isFloatingPointMoney(double.class)).isTrue();
        assertThat(MoneyGuards.isFloatingPointMoney(Float.class)).isTrue();
        assertThat(MoneyGuards.isFloatingPointMoney(BigDecimal.class)).isFalse();
    }

    // ----- Realization 2: the record compact constructor -----

    @Test
    void recordEnforcesTheInvariantByItsComponentTypeAndCompactConstructor() {
        Money money = new Money(new BigDecimal("5.00"), USD);
        assertThat(money.currency()).isEqualTo(USD);

        // There is no constructor taking a double; the compact constructor adds the remaining guards.
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Money(new BigDecimal("-1"), USD));
    }

    // ----- Realization 3: the Error Prone-style declarative fence -----

    @Test
    void errorProneFenceIsCompileTimeOnlyAndAppliedToTheBannedFactory() throws Exception {
        var fenced = LegacyMoneyFactory.class.getDeclaredMethod("fromDouble", double.class, String.class);

        // The fence is a compile-time concern: @RestrictedApi declares no @Retention, so it is
        // class-retained by default and invisible to runtime reflection (the reason it costs nothing at
        // run time and its dependency is `provided` scope). The annotation is therefore NOT runtime-visible
        // on the method...
        assertThat(fenced.isAnnotationPresent(com.google.errorprone.annotations.RestrictedApi.class))
            .as("class-retained, so not visible to runtime reflection")
            .isFalse();
        // ...yet the @RestrictedApi descriptor IS present in the compiled class file, which is the metadata
        // an Error Prone build reads to flag every non-allowlisted caller.
        assertThat(classFileContains(LegacyMoneyFactory.class,
            "Lcom/google/errorprone/annotations/RestrictedApi;"))
            .as("the fence is applied at the class-file level")
            .isTrue();

        // The single sanctioned adapter is on the allowlist and runs correctly at run time.
        assertThat(LegacyMoneyFactory.fromLegacyFeed(2.5, "USD").amount()).isEqualByComparingTo("2.5");
    }

    /** Reads a compiled class file and reports whether the given (UTF-8) descriptor appears in it. */
    private static boolean classFileContains(Class<?> type, String descriptor) throws Exception {
        String resource = type.getName().replace('.', '/') + ".class";
        try (var in = type.getClassLoader().getResourceAsStream(resource)) {
            assertThat(in).as("compiled class file on the classpath").isNotNull();
            byte[] bytes = in.readAllBytes();
            return new String(bytes, java.nio.charset.StandardCharsets.ISO_8859_1).contains(descriptor);
        }
    }

    // ----- Realization 5: the reflective inspector (stand-in for a Checkstyle/PMD/SpotBugs check) -----

    @Test
    void reflectiveInspectorFiresOnTheBadFixtureAndIsSilentOnTheGoodOne() {
        MoneyApiInspector inspector = new MoneyApiInspector(MoneyPolicy.prod());

        List<MoneyViolation> onLegacy = inspector.inspect(LegacyOrderLine.class);
        assertThat(onLegacy).extracting(MoneyViolation::where).contains("LegacyOrderLine.priceUsd()");

        assertThat(inspector.inspect(PricedItem.class)).isEmpty();
    }

    @Test
    void reflectiveInspectorOnlySeesItsOwnArtifact() {
        // The honest limit (the chapter's reason layering matters): the inspector reads declared public
        // members, so a double reached only through a generic erased at run time is invisible to it.
        MoneyApiInspector inspector = new MoneyApiInspector(MoneyPolicy.prod());
        assertThat(inspector.inspect(ErasedMoneyHolder.class)).isEmpty();
    }

    /** A holder whose money escapes through an erased generic — invisible to the reflective inspector. */
    private static final class ErasedMoneyHolder {
        private final List<Double> amounts = List.of(1.0);

        @SuppressWarnings("unused")
        List<Double> amounts() {
            return amounts;
        }
    }

    // ----- Codegen comparison: hand-written boilerplate vs. compiler-derived record -----

    @Test
    void handWrittenBoilerplateAndRecordAgreeValueForValue() {
        BigDecimal amount = new BigDecimal("42.00");
        HandWrittenMoney byHand = new HandWrittenMoney(amount, USD);
        Money byRecord = new Money(amount, USD);

        // The generator's correctness claim, checked rather than assumed: the same components yield the
        // same value semantics whether the boilerplate is written by hand or derived by the compiler.
        assertThat(byHand.amount()).isEqualByComparingTo(byRecord.amount());
        assertThat(byHand.currency()).isEqualTo(byRecord.currency());
        assertThat(byHand).isEqualTo(new HandWrittenMoney(amount, USD));
        assertThat(byRecord).isEqualTo(new Money(amount, USD));
    }

    // ----- Externalized config profiles -----

    @Test
    void devProfileReportsAsWarningAndProdAsError() {
        assertThat(MoneyPolicy.forProfile("dev").severity()).isEqualTo(MoneyPolicy.Severity.WARNING);
        assertThat(MoneyPolicy.forProfile("prod").severity()).isEqualTo(MoneyPolicy.Severity.ERROR);
        assertThat(MoneyPolicy.prod().looksLikeMoney("priceUsd")).isTrue();
        assertThat(MoneyPolicy.prod().looksLikeMoney("name")).isFalse();
    }

    @Test
    void unknownProfileIsRejected() {
        assertThatIllegalArgumentException().isThrownBy(() -> MoneyPolicy.forProfile("staging"));
    }

    // ----- Observability / health surface -----

    @Test
    void healthSurfaceReportsReadyAndCountsBreaches() {
        MoneyPolicy policy = MoneyPolicy.prod();
        MoneyPolicyHealth health = new MoneyPolicyHealth(policy);
        assertThat(health.isReady()).isTrue();

        List<MoneyViolation> found = new MoneyApiInspector(policy).inspect(LegacyOrderLine.class);
        health.record(found);
        assertThat(health.reportedViolationCount()).isEqualTo(found.size());
        assertThat(health.reportedViolationCount()).isPositive();
    }

    @Test
    void severityCarriesFromThePolicyIntoTheReportedViolation() {
        List<MoneyViolation> prodFindings =
            new MoneyApiInspector(MoneyPolicy.prod()).inspect(LegacyOrderLine.class);
        assertThat(prodFindings).isNotEmpty()
            .allMatch(v -> v.severity() == MoneyPolicy.Severity.ERROR);

        List<MoneyViolation> devFindings =
            new MoneyApiInspector(MoneyPolicy.forProfile("dev")).inspect(LegacyOrderLine.class);
        assertThat(devFindings).isNotEmpty()
            .allMatch(v -> v.severity() == MoneyPolicy.Severity.WARNING);
    }
}
