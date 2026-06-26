package org.acme.readability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Exercises the chapter's three-forms-of-one-rule demo as runnable assertions.
 *
 * <p>The <strong>behaviour-preservation</strong> tests are the load-bearing ones: they prove the
 * deeply-nested {@link DiscountRulesNested}, the over-fragmented {@link DiscountRulesFragmented}, and the
 * balanced {@link DiscountRules} return exactly the same {@link Money} for every loyalty tier, both flags,
 * and across the floor and cap boundaries — the chapter's measurement point made checkable, the cognitive
 * complexity (Sonar {@code java:S3776}) of the three differs while the observable result does not. The
 * <strong>failure-path</strong> tests prove all three reject a negative cap and a cap below the floor with
 * the same typed {@link PricingException}, and the {@link PricingService} tests exercise the externalized
 * policy, the rejection counter, and the readiness probe.
 */
class DiscountRulesTest {

    private static final String USD = "USD";
    private static final long FLOOR = 1_000L;
    private static final Money CAP = new Money(2_000L, USD);

    private final DiscountRule nested = new DiscountRulesNested();
    private final DiscountRule fragmented = new DiscountRulesFragmented();
    private final DiscountRule balanced = new DiscountRules();

    /** Every combination of tier, coupon, and seasonal-sale flag — the full input space of the rule. */
    static List<Arguments> carts() {
        List<Arguments> args = new ArrayList<>();
        long[] subtotals = {500L, 10_000L, 100_000L};   // below floor, mid, large (to exercise the cap)
        for (LoyaltyTier tier : LoyaltyTier.values()) {
            for (boolean coupon : new boolean[] {false, true}) {
                for (boolean sale : new boolean[] {false, true}) {
                    for (long subtotal : subtotals) {
                        args.add(Arguments.of(
                            new Cart(new Money(subtotal, USD), tier, coupon, sale)));
                    }
                }
            }
        }
        return args;
    }

    @ParameterizedTest
    @MethodSource("carts")
    void allThreeFormsReturnTheIdenticalDiscount(Cart cart) {
        Money fromNested = nested.discountFor(cart, CAP, FLOOR);
        Money fromFragmented = fragmented.discountFor(cart, CAP, FLOOR);
        Money fromBalanced = balanced.discountFor(cart, CAP, FLOOR);

        // Same Money across all three shapes: cognitive complexity changed, behaviour did not.
        assertThat(fromFragmented).isEqualTo(fromNested);
        assertThat(fromBalanced).isEqualTo(fromNested);
    }

    @Test
    void theFormsComputeTheDiscountTheChapterDescribes() {
        // GOLD (1000bp) + sale (300bp) + coupon (500bp) = 1800bp of a 10,000 subtotal = 1,800.
        Cart gold = new Cart(new Money(10_000L, USD), LoyaltyTier.GOLD, true, true);

        assertThat(balanced.discountFor(gold, CAP, FLOOR)).isEqualTo(new Money(1_800L, USD));
    }

    @Test
    void belowTheFloorNoFormAppliesADiscount() {
        Cart tiny = new Cart(new Money(500L, USD), LoyaltyTier.GOLD, true, true);   // 500 < 1,000 floor

        assertThat(nested.discountFor(tiny, CAP, FLOOR)).isEqualTo(new Money(0L, USD));
        assertThat(fragmented.discountFor(tiny, CAP, FLOOR)).isEqualTo(new Money(0L, USD));
        assertThat(balanced.discountFor(tiny, CAP, FLOOR)).isEqualTo(new Money(0L, USD));
    }

    @Test
    void everyFormClampsToTheCap() {
        // GOLD+sale+coupon on a large subtotal would far exceed the 2,000 cap; all three clamp to it.
        Cart big = new Cart(new Money(100_000L, USD), LoyaltyTier.GOLD, true, true);

        assertThat(nested.discountFor(big, CAP, FLOOR)).isEqualTo(CAP);
        assertThat(fragmented.discountFor(big, CAP, FLOOR)).isEqualTo(CAP);
        assertThat(balanced.discountFor(big, CAP, FLOOR)).isEqualTo(CAP);
    }

    @Test
    void everyFormRejectsACapBelowTheFloorWithTheSameTypedError() {
        Cart cart = new Cart(new Money(10_000L, USD), LoyaltyTier.SILVER, false, false);
        Money lowCap = new Money(500L, USD);   // a 500 cap under a 1,000 floor is an impossible policy

        PricingException fromNested =
            catchThrowableOfType(PricingException.class, () -> nested.discountFor(cart, lowCap, FLOOR));
        PricingException fromFragmented =
            catchThrowableOfType(PricingException.class, () -> fragmented.discountFor(cart, lowCap, FLOOR));
        PricingException fromBalanced =
            catchThrowableOfType(PricingException.class, () -> balanced.discountFor(cart, lowCap, FLOOR));

        assertThat(fromNested.code()).isEqualTo("cap-below-floor");
        assertThat(fromFragmented.code()).isEqualTo("cap-below-floor");
        assertThat(fromBalanced.code()).isEqualTo("cap-below-floor");
    }

    @Test
    void everyFormFailsFastOnANullCart() {
        assertThatNullPointerException().isThrownBy(() -> nested.discountFor(null, CAP, FLOOR));
        assertThatNullPointerException().isThrownBy(() -> fragmented.discountFor(null, CAP, FLOOR));
        assertThatNullPointerException().isThrownBy(() -> balanced.discountFor(null, CAP, FLOOR));
    }

    @Test
    void serviceCountsRejectionsThroughTheFailurePath() {
        // A 500 cap under a 1,000 floor is an impossible policy: every request hits the failure path.
        PricingService service = new PricingService(balanced, new Money(500L, USD), FLOOR);
        Cart cart = new Cart(new Money(10_000L, USD), LoyaltyTier.GOLD, false, false);

        assertThatExceptionOfType(PricingException.class).isThrownBy(() -> service.priceDiscount(cart));
        assertThat(service.rejectedCount()).isEqualTo(1L);
    }

    @Test
    void serviceAppliesTheExternalizedPolicyAndReportsReady() {
        PricingService service = new PricingService(balanced, CAP, FLOOR);
        Cart cart = new Cart(new Money(10_000L, USD), LoyaltyTier.SILVER, false, false);

        assertThat(service.isReady()).isTrue();
        assertThat(service.priceDiscount(cart)).isEqualTo(new Money(500L, USD));   // 5% of 10,000
        assertThat(service.rejectedCount()).isEqualTo(0L);
    }
}
