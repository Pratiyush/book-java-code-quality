package org.acme.storefront.pricing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Exercises the four null-safety levers the chapter describes: the design-time {@link Optional}
 * return contract, the boundary {@code requireNonNull} guards, the explicit {@code @Nullable} opt-out,
 * and the runtime JEP 358 message on the deliberate failure path. The guarded fix and its broken twin
 * are asserted side by side, and both config profiles are loaded so the optional-config path is
 * covered. Each test asserts the contract holds at runtime; the {@code -Pquality} build asserts the
 * house rules hold at build time.
 */
class DiscountServiceTest {

    private InMemoryPromoCatalog catalog;
    private DiscountService service;

    @BeforeEach
    void seed() {
        catalog = new InMemoryPromoCatalog();
        catalog.register(new Discount("WELCOME10", 1_000L));
        service = new DiscountService(catalog, Optional.of("WELCOME10"));
    }

    @Test
    void findDiscountReturnsPresentForKnownCode() {
        assertThat(service.findDiscount("WELCOME10")).get()
            .extracting(Discount::amountOffMinor).isEqualTo(1_000L);
    }

    @Test
    void findDiscountReturnsEmptyForUnknownCode() {
        assertThat(service.findDiscount("NOPE")).isEmpty();
    }

    @Test
    void findDiscountRejectsNullCodeFailFast() {
        assertThatNullPointerException()
            .isThrownBy(() -> service.findDiscount(null))
            .withMessageContaining("code");
    }

    @Test
    void constructorRejectsNullCatalogFailFast() {
        assertThatNullPointerException()
            .isThrownBy(() -> new DiscountService(null, Optional.empty()))
            .withMessageContaining("catalog");
    }

    @Test
    void constructorRejectsNullDefaultCodeFailFast() {
        assertThatNullPointerException()
            .isThrownBy(() -> new DiscountService(catalog, null))
            .withMessageContaining("defaultCode");
    }

    @Test
    void priceWithDiscountSubtractsAPresentDiscountWithoutGet() {
        Money total = service.priceWithDiscount(Optional.of("WELCOME10"), new Money(5_000L, "USD"));

        assertThat(total.minorUnits()).isEqualTo(4_000L);
        assertThat(service.discountsAppliedCount()).isEqualTo(1L);
        assertThat(service.lookupsWithoutDiscountCount()).isEqualTo(0L);
    }

    @Test
    void priceWithDiscountChargesFullTotalWhenCodeUnknown() {
        Money total = service.priceWithDiscount(Optional.of("NOPE"), new Money(5_000L, "USD"));

        assertThat(total.minorUnits()).isEqualTo(5_000L);
        assertThat(service.discountsAppliedCount()).isEqualTo(0L);
        assertThat(service.lookupsWithoutDiscountCount()).isEqualTo(1L);
    }

    @Test
    void priceWithDiscountFallsBackToTheConfiguredDefaultCode() {
        Money total = service.priceWithDiscount(Optional.empty(), new Money(5_000L, "USD"));

        assertThat(total.minorUnits()).isEqualTo(4_000L);
    }

    @Test
    void defaultCodeOrNullExposesTheNullMarkedOptOut() {
        assertThat(service.defaultCodeOrNull()).isEqualTo("WELCOME10");
        DiscountService noDefault = new DiscountService(catalog, Optional.empty());
        assertThat(noDefault.defaultCodeOrNull()).isNull();
    }

    @Test
    void readinessProbeReportsReady() {
        assertThat(service.isReady()).isTrue();
    }

    @Test
    void guardedCheckoutHandlesTheAbsentDiscount() {
        Checkout checkout = new Checkout(catalog);

        assertThat(checkout.total("WELCOME10", new Money(5_000L, "USD")).minorUnits()).isEqualTo(4_000L);
        assertThat(checkout.total("NOPE", new Money(5_000L, "USD")).minorUnits()).isEqualTo(5_000L);
    }

    @Test
    void brokenCheckoutThrowsAHelpfulNpeOnTheUnguardedDereference() {
        BrokenCheckout broken = new BrokenCheckout(catalog);
        Discount absent = broken.lookupOrNull("NOPE");   // null: the lossy Optional -> @Nullable step

        NullPointerException npe = catchThrowableOfType(
            NullPointerException.class, () -> broken.total(absent, new Money(5_000L, "USD")));

        // JEP 358 (on by default at the anchor) names the exact null expression in the message.
        assertThat(npe).isNotNull();
        assertThat(npe.getMessage()).contains("amountOffMinor");
    }

    @Test
    void brokenCheckoutSucceedsWhenTheDiscountHappensToBePresent() {
        BrokenCheckout broken = new BrokenCheckout(catalog);
        Discount present = broken.lookupOrNull("WELCOME10");

        assertThat(broken.total(present, new Money(5_000L, "USD")).minorUnits()).isEqualTo(4_000L);
    }

    @Test
    void devProfileConfiguresADefaultCode() {
        assertThat(PricingConfig.load("dev").defaultPromoCode()).contains("WELCOME10");
    }

    @Test
    void prodProfileLeavesTheDefaultCodeEmpty() {
        assertThat(PricingConfig.load("prod").defaultPromoCode()).isEmpty();
    }

    @Test
    void typeUsePrecisionDistinguishesTheTwoNullablePlacements() {
        List<String> withNulls = new ArrayList<>(Arrays.asList("WELCOME10", null, "SPRING"));
        TypeUsePrecision present = new TypeUsePrecision(withNulls, List.of("A", "B"));
        assertThat(present.presentCodeCount()).isEqualTo(2L);
        assertThat(present.hasOptionalList()).isTrue();

        TypeUsePrecision absentList = new TypeUsePrecision(List.of(), null);
        assertThat(absentList.hasOptionalList()).isFalse();
    }
}
