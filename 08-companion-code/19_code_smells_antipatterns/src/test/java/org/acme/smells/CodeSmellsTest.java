package org.acme.smells;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Exercises the chapter's two worked smells as runnable assertions.
 *
 * <p>The <strong>behaviour-preservation</strong> tests are the load-bearing ones: they prove the
 * refactored {@link OrderService} returns exactly the same {@link Receipt} as the smelly
 * {@link OrderServiceSmelly} for every loyalty tier and across the free-shipping boundary — Fowler's
 * definition of a refactoring made checkable, the structure changes while the observable result does
 * not. The <strong>failure-path</strong> tests prove the representation-exposure smell is a real
 * latent bug ({@link OrderLeaky} mutates underneath its holder) that the defensive copy in
 * {@link Order} closes, and that both services reject malformed orders with the same typed error.
 */
class CodeSmellsTest {

    private static final String USD = "USD";
    private static final long FREE_SHIPPING_THRESHOLD = 5_000L;
    private static final long FLAT_SHIPPING = 599L;

    private final OrderService refactored =
        new OrderService(FREE_SHIPPING_THRESHOLD, FLAT_SHIPPING);
    private final OrderServiceSmelly smelly =
        new OrderServiceSmelly(FREE_SHIPPING_THRESHOLD, FLAT_SHIPPING);

    private static Order order(LoyaltyTier tier, LineItem... lines) {
        return new Order("A-1", new Customer("c-1", tier), List.of(lines));
    }

    private static LineItem line(String sku, long unitMinor, int qty) {
        return new LineItem(sku, new Money(unitMinor, USD), qty);
    }

    @ParameterizedTest
    @EnumSource(LoyaltyTier.class)
    void refactoringPreservesBehaviourAcrossEveryTier(LoyaltyTier tier) {
        // A subtotal below the free-shipping threshold, so shipping is charged.
        Order small = order(tier, line("sku-1", 1_200L, 2));

        assertThat(refactored.placeOrder(small))
            .isEqualTo(smelly.placeOrder(small));   // same Receipt: structure changed, behaviour did not
    }

    @ParameterizedTest
    @EnumSource(LoyaltyTier.class)
    void refactoringPreservesBehaviourAcrossTheFreeShippingBoundary(LoyaltyTier tier) {
        // A subtotal comfortably above the threshold, so the free-shipping branch is taken.
        Order large = order(tier, line("sku-1", 4_000L, 3));

        assertThat(refactored.placeOrder(large))
            .isEqualTo(smelly.placeOrder(large));
    }

    @Test
    void refactoredServiceComputesTheReceiptTheChapterDescribes() {
        Order gold = order(LoyaltyTier.GOLD, line("sku-1", 3_000L, 2));   // subtotal 6,000

        Receipt receipt = refactored.placeOrder(gold);

        assertThat(receipt.subtotal()).isEqualTo(new Money(6_000L, USD));
        assertThat(receipt.discount()).isEqualTo(new Money(600L, USD));   // 10% of 6,000
        assertThat(receipt.shipping()).isEqualTo(new Money(0L, USD));     // 5,400 >= 5,000 -> free
        assertThat(receipt.total()).isEqualTo(new Money(5_400L, USD));
    }

    @Test
    void leakedListMutationCorruptsTheOrder() {
        List<LineItem> lines = new ArrayList<>();
        lines.add(line("sku-1", 100L, 1));
        OrderLeaky leaky = new OrderLeaky("A-2", lines);

        lines.add(line("sku-2", 200L, 1));                 // mutate the caller's list after construction
        leaky.lines().add(line("sku-3", 300L, 1));         // mutate through the returned reference too

        // The "immutable" order grew two line items — the smell proven a real bug, not just described.
        assertThat(leaky.lines()).hasSize(3);
    }

    @Test
    void defensiveCopyKeepsTheOrderImmuneToCallerMutation() {
        List<LineItem> lines = new ArrayList<>();
        lines.add(line("sku-1", 100L, 1));
        Order order = new Order("A-3", new Customer("c-1", LoyaltyTier.STANDARD), lines);

        lines.add(line("sku-2", 200L, 1));                 // mutate the caller's list
        assertThat(order.lines()).hasSize(1);              // the order did not change

        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> order.lines().add(lines.get(0)));   // accessor copy is unmodifiable
    }

    @Test
    void bothServicesRejectAnEmptyOrderWithTheSameTypedError() {
        Order empty = new Order("A-4", new Customer("c-1", LoyaltyTier.STANDARD), List.of());

        OrderRejectedException fromRefactored = catchThrowableOfType(
            OrderRejectedException.class, () -> refactored.placeOrder(empty));
        OrderRejectedException fromSmelly = catchThrowableOfType(
            OrderRejectedException.class, () -> smelly.placeOrder(empty));

        assertThat(fromRefactored.code()).isEqualTo("empty-order");
        assertThat(fromSmelly.code()).isEqualTo("empty-order");
    }

    @Test
    void bothServicesRejectAMixedCurrencyOrderAndCountIt() {
        Order mixed = new Order("A-5", new Customer("c-1", LoyaltyTier.STANDARD), List.of(
            new LineItem("sku-1", new Money(100L, "USD"), 1),
            new LineItem("sku-2", new Money(200L, "EUR"), 1)));

        assertThatExceptionOfType(OrderRejectedException.class)
            .isThrownBy(() -> refactored.placeOrder(mixed));
        assertThatExceptionOfType(OrderRejectedException.class)
            .isThrownBy(() -> smelly.placeOrder(mixed));
        assertThat(refactored.rejectedCount()).isEqualTo(1L);
        assertThat(smelly.rejectedCount()).isEqualTo(1L);
    }

    @Test
    void placingANullOrderFailsFast() {
        assertThatNullPointerException().isThrownBy(() -> refactored.placeOrder(null));
        assertThatNullPointerException().isThrownBy(() -> smelly.placeOrder(null));
    }

    @Test
    void readinessProbesReportReady() {
        assertThat(refactored.isReady()).isTrue();
        assertThat(smelly.isReady()).isTrue();
    }
}
