package org.acme.refactoring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Exercises the chapter's safe-change loop as runnable assertions, at the scales the module realizes.
 *
 * <p>The <strong>characterization</strong> test is the precondition: it pins what
 * {@link LegacyShippingCalculator} <em>actually does</em> today — including a rounding quirk a naive
 * reading would get wrong — through a seam ({@link RateTable}) injected via the legacy class's
 * parameterized constructor, so the legacy method is tested without editing it. The
 * <strong>behaviour-preservation</strong> tests are the load-bearing ones: they prove the modernized
 * {@link ShippingCalculator} returns the identical charge as the legacy method for every service level
 * and across the served/unserved boundary — Fowler's definition of a refactoring made checkable, the
 * structure changes (record, sealed types, {@code Optional}, streams) while the observable result,
 * quirk included, does not. The <strong>failure-path</strong> tests prove the modern path turns an
 * unserved destination into a typed {@link Quote.Unavailable} rather than an in-band zero, and that the
 * legacy representation leak is a real latent bug the refactor closes. The <strong>strangler</strong>
 * tests drive both routes through {@link StranglerRouter} and prove the flag flips the implementation,
 * not the result — including the rollback position.
 */
class SafeChangeTest {

    private static final String USD = "USD";

    /**
     * A test double for the seam (Feathers): a known, in-memory rate table so a characterization test
     * can pin behaviour without the real rate source. Two zones are served; everything else is unserved.
     */
    private static final RateTable STUB_RATES = destination -> switch (destination) {
        case "ZONE_A" -> Optional.of(new Money(500L, USD));
        case "ZONE_B" -> Optional.of(new Money(900L, USD));
        default -> Optional.empty();
    };

    private final LegacyShippingCalculator legacy = new LegacyShippingCalculator(STUB_RATES);
    private final ShippingCalculator modern = new ShippingCalculator(STUB_RATES);

    @Test
    void characterizationTestPinsTheLegacyRoundingQuirk() {
        // ZONE_A base 500/kilo, EXPEDITED (+15%), 333 g. The legacy method applies the surcharge
        // per-kilo and truncates BEFORE multiplying by weight: (500 + 75) * 333 / 1000 = 191.
        // Applying the surcharge to the final charge instead would give 190 — so 191 is a quirk of the
        // legacy ordering, captured here as current behaviour (what it DOES, not what it SHOULD do).
        // tag::characterization-test[]
        long charged = legacy.quote(333, "EXPEDITED", "ZONE_A");   // pin what the code DOES today

        assertThat(charged).isEqualTo(191L);   // a quirk (naive math says 190) preserved, not "fixed"
        // end::characterization-test[]
    }

    @Test
    void characterizationTestPinsTheServedZoneAndSurchargeBaseline() {
        assertThat(legacy.quote(2000, "STANDARD", "ZONE_A")).isEqualTo(1_000L);  // 500 * 2000/1000
        assertThat(legacy.quote(2000, "OVERNIGHT", "ZONE_A")).isEqualTo(1_400L); // (500+200) * 2000/1000
        assertThat(legacy.appliedSurcharges()).containsExactly("OVERNIGHT");
    }

    @ParameterizedTest
    @EnumSource(ServiceLevel.class)
    void refactoringPreservesBehaviourForEveryServiceLevel(ServiceLevel level) {
        Parcel parcel = new Parcel(1_500, level, "ZONE_A");

        Quote quote = modern.quote(parcel);

        // The modern result must equal the legacy charge for the same input — quirk and all.
        long legacyCharge = legacy.quote(parcel.grams(), level.name(), parcel.destination());
        assertThat(quote).isInstanceOf(Quote.Priced.class);
        assertThat(((Quote.Priced) quote).amount()).isEqualTo(new Money(legacyCharge, USD));
    }

    @ParameterizedTest
    @EnumSource(ServiceLevel.class)
    void refactoringPreservesTheQuirkOnTheQuirkExposingInput(ServiceLevel level) {
        // The 333 g input is where the legacy ordering truncates differently; the refactor must match.
        Parcel parcel = new Parcel(333, level, "ZONE_B");

        long legacyCharge = legacy.quote(parcel.grams(), level.name(), parcel.destination());
        Quote quote = modern.quote(parcel);

        assertThat(((Quote.Priced) quote).amount().minorUnits()).isEqualTo(legacyCharge);
    }

    @Test
    void modernPathReturnsATypedUnavailableForAnUnservedZone() {
        Quote quote = modern.quote(new Parcel(1_000, ServiceLevel.STANDARD, "ZONE_Z"));

        assertThat(quote).isInstanceOf(Quote.Unavailable.class);
        assertThat(((Quote.Unavailable) quote).reason()).isEqualTo("no-rate-for-zone");
        assertThat(modern.unavailableCount()).isEqualTo(1L);   // observability surface incremented
    }

    @Test
    void legacyPathSignalsTheSameUnservedZoneWithAnInBandZero() {
        // The behaviour is preserved across the refactor, but the SHAPE improves: legacy returns 0
        // (a sentinel a caller can mistake for free shipping); the modern path returns a typed outcome.
        assertThat(legacy.quote(1_000, "STANDARD", "ZONE_Z")).isEqualTo(0L);
    }

    @Test
    void legacyCalculatorLeaksItsInternalSurchargeList() {
        legacy.quote(1_000, "EXPEDITED", "ZONE_A");

        List<String> leaked = legacy.appliedSurcharges();
        leaked.add("INJECTED");   // mutate the calculator's state through the returned reference

        // The leak is a real bug, not just described: the next read sees the injected entry.
        assertThat(legacy.appliedSurcharges()).contains("INJECTED");
    }

    @Test
    void modernCalculatorHandsBackAnUnmodifiableSnapshot() {
        Parcel parcel = new Parcel(1_000, ServiceLevel.EXPEDITED, "ZONE_A");

        List<String> surcharges = modern.appliedSurcharges(parcel);

        assertThat(surcharges).containsExactly("EXPEDITED");
        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> surcharges.add("INJECTED"));   // no internal list to leak
    }

    @Test
    void stranglerRoutesToTheModernPathWhenTheFlagIsOn() {
        StranglerRouter router = new StranglerRouter(legacy, modern, true);
        Parcel parcel = new Parcel(1_500, ServiceLevel.OVERNIGHT, "ZONE_A");

        Quote routed = router.quote(parcel);

        assertThat(routed).isEqualTo(modern.quote(parcel));   // same result the modern path computes
    }

    @Test
    void stranglerRollsBackToTheLegacyPathWhenTheFlagIsOff() {
        StranglerRouter router = new StranglerRouter(legacy, modern, false);
        Parcel parcel = new Parcel(1_500, ServiceLevel.OVERNIGHT, "ZONE_A");

        Quote routed = router.quote(parcel);

        // Rolled back to legacy, but adapted to the same typed Quote — and it agrees with the modern
        // path, which is exactly why the cutover is safe to flip either way.
        long legacyCharge = legacy.quote(parcel.grams(), parcel.serviceLevel().name(), parcel.destination());
        assertThat(routed).isEqualTo(new Quote.Priced(new Money(legacyCharge, USD)));
    }

    @Test
    void stranglerAdaptsTheLegacyInBandZeroToATypedUnavailable() {
        StranglerRouter router = new StranglerRouter(legacy, modern, false);

        Quote routed = router.quote(new Parcel(1_000, ServiceLevel.STANDARD, "ZONE_Z"));

        assertThat(routed).isInstanceOf(Quote.Unavailable.class);
    }

    @Test
    void describeUsesTheExhaustiveSealedSwitch() {
        StranglerRouter router = new StranglerRouter(legacy, modern, true);

        assertThat(router.describe(new Quote.Priced(new Money(1_400L, USD))))
            .isEqualTo("charged 1400 USD");
        assertThat(router.describe(new Quote.Unavailable("ZONE_Z", "no-rate-for-zone")))
            .isEqualTo("unavailable for ZONE_Z (no-rate-for-zone)");
    }
}
