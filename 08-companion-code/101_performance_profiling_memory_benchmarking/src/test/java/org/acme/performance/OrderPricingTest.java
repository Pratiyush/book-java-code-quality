package org.acme.performance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Exercises the pricing mechanism end to end: the happy path, the bounded-input failure paths, the
 * observability counter, and — the chapter's load-bearing check — that reducing allocation does not
 * change the answer. An optimization that returns a different result is a bug, not a speed-up, so the
 * cheaper summary form is held to the same output as the churning one before any claim about it.
 */
class OrderPricingTest {

    private OrderPricing pricing;

    @BeforeEach
    void setUp() {
        pricing = new OrderPricing();
    }

    @Test
    void pricesAnOrderAndCountsFreeShippingLines() {
        List<LineItem> order = List.of(
            new LineItem("BOOK-1", 1_200L, 1),     // 1_200 < free-ship threshold
            new LineItem("DESK-9", 6_000L, 1));    // 6_000 >= free-ship threshold

        Quote quote = pricing.priceOrder(order);

        assertThat(quote.subtotalMinor()).isEqualTo(7_200L);
        assertThat(quote.freeShipLines()).isEqualTo(1);
        assertThat(quote.totalMinor()).isEqualTo(quote.subtotalMinor() - quote.discountMinor());
    }

    @Test
    void rejectsAnEmptyOrderFailFast() {
        assertThatThrownBy(() -> pricing.priceOrder(List.of()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("empty");
    }

    @Test
    void rejectsAnOrderOverTheBoundedLimit() {
        List<LineItem> tooMany = IntStream.range(0, 10_001)
            .mapToObj(i -> new LineItem("SKU-" + i, 100L, 1))
            .toList();

        assertThatThrownBy(() -> pricing.priceOrder(tooMany))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("limit");
    }

    @Test
    void reducedSummaryReturnsTheIdenticalAnswerAsTheChurningForm() {
        List<LineItem> order = List.of(
            new LineItem("BOOK-1", 1_200L, 2),
            new LineItem("DESK-9", 6_000L, 1),
            new LineItem("PEN-42", 300L, 5));

        assertThat(pricing.summaryLine(order)).isEqualTo(pricing.summaryLineChurning(order));
        assertThat(pricing.summaryEquivalenceHolds(order)).isTrue();
    }

    @Test
    void reducedSummaryMatchesOnAnEmptyList() {
        List<LineItem> empty = new ArrayList<>();

        assertThat(pricing.summaryLine(empty)).isEqualTo(pricing.summaryLineChurning(empty));
    }

    @Test
    void observabilityCounterTracksPricedOrders() {
        List<LineItem> order = List.of(new LineItem("BOOK-1", 1_200L, 1));

        assertThat(pricing.pricedOrderCount()).isZero();
        pricing.priceOrder(order);
        pricing.priceOrder(order);
        assertThat(pricing.pricedOrderCount()).isEqualTo(2L);
    }
}
