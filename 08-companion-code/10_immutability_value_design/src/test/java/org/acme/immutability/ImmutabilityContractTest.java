package org.acme.immutability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's claims as runnable assertions: the records' derived contracts, the
 * defensive-copy boundary that seals a record's mutable component, the immutable-collection
 * behaviour, and — the load-bearing failure paths — the leak the leaky record allows, the {@code
 * HashMap} a broken {@code hashCode} loses a key in, and the typed rejections the order book raises.
 *
 * <p>Each correct-path test asserts a contract holds at runtime; the {@code -Pquality} build asserts
 * the same kinds of mistake are caught by the analyzers. The counter-example tests assert the bug is
 * real, which is why the discipline matters.
 */
class ImmutabilityContractTest {

    private static final String USD = "USD";

    // tag::contract-test[]
    @Test
    void recordDerivesEqualsAndHashCodeConsistently() {
        Money a = new Money(500L, USD);
        Money b = new Money(500L, USD);

        assertThat(a).isEqualTo(b);                         // derived equals: value equality
        assertThat(a).hasSameHashCodeAs(b);                 // derived hashCode: agrees with equals
        assertThat(Map.of(a, "five")).containsKey(b);       // so it works as a map key
    }
    // end::contract-test[]

    @Test
    void moneyValidatesInItsCompactConstructor() {
        assertThatThrownBy(() -> new Money(-1L, USD))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Money(0L, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void moneyOrdersByComparatorWithoutOverflow() {
        Money small = new Money(1L, USD);
        Money large = new Money(Long.MAX_VALUE, USD);

        // int-subtraction would overflow here; the Comparator combinator keeps the sign correct.
        assertThat(small).isLessThan(large);
        assertThat(large).isGreaterThan(small);
        assertThat(small.compareTo(small)).isZero();
    }

    @Test
    void sealedOrderIsImmuneToCallerMutation() {
        List<LineItem> lines = new ArrayList<>();
        lines.add(new LineItem("sku-1", new Money(100L, USD)));
        Order order = new Order("A-1", lines);

        lines.add(new LineItem("sku-2", new Money(200L, USD)));   // mutate the caller's list
        assertThat(order.items()).hasSize(1);                     // the order did not change

        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> order.items().add(lines.get(0)));   // accessor copy is unmodifiable
    }

    @Test
    void leakyOrderChangesItsMindWhenTheCallerMutatesTheList() {
        List<LineItem> lines = new ArrayList<>();
        lines.add(new LineItem("sku-1", new Money(100L, USD)));
        OrderLeaky order = new OrderLeaky("A-2", lines);

        lines.add(new LineItem("sku-2", new Money(200L, USD)));   // mutate the caller's list
        // The "immutable" order grew a line item — the hook's bug, proven, not just described.
        assertThat(order.items()).hasSize(2);
    }

    // tag::hashmap-loses-key[]
    @Test
    void hashMapLosesAKeyWhenHashCodeIsNotOverridden() {
        var map = new HashMap<BrokenPrice, String>();
        map.put(new BrokenPrice(500L, USD), "five");

        BrokenPrice equalKey = new BrokenPrice(500L, USD);
        assertThat(equalKey).isEqualTo(map.keySet().iterator().next());   // equals: true
        assertThat(map.get(equalKey)).isNull();                           // get: null — key lost
    }
    // end::hashmap-loses-key[]

    @Test
    void recordAsKeyDoesNotLoseTheKey() {
        var map = new HashMap<Money, String>();
        map.put(new Money(500L, USD), "five");

        // The same scenario with a record: derived hashCode agrees with equals, so the key is found.
        assertThat(map.get(new Money(500L, USD))).isEqualTo("five");
    }

    @Test
    void immutableCollectionsThrowOnMutation() {
        Catalog catalog = new Catalog(List.of(new LineItem("sku-1", new Money(100L, USD))));

        assertThat(catalog.featured()).containsExactly("sku-1", "sku-2", "sku-3");
        assertThat(catalog.priceOf("sku-1")).isEqualTo(new Money(1_999L, USD));
        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> catalog.stock().add(new LineItem("x", new Money(1L, USD))));
    }

    @Test
    void catalogSnapshotIsImmuneToCallerMutation() {
        List<LineItem> stock = new ArrayList<>();
        stock.add(new LineItem("sku-1", new Money(100L, USD)));
        Catalog catalog = new Catalog(stock);

        stock.clear();                                  // mutate the caller's list after construction
        assertThat(catalog.stock()).hasSize(1);         // the catalogue kept its snapshot
    }

    @Test
    void orderBookSnapshotDoesNotChangeAfterLaterAcceptances() {
        OrderBook bookSvc = new OrderBook();
        bookSvc.accept(new Order("A-1", List.of(new LineItem("sku-1", new Money(100L, USD)))));
        List<Order> firstSnapshot = bookSvc.acceptedOrders();

        bookSvc.accept(new Order("A-2", List.of(new LineItem("sku-2", new Money(200L, USD)))));

        assertThat(firstSnapshot).hasSize(1);           // snapshot, not a live view
        assertThat(bookSvc.acceptedOrders()).hasSize(2);
    }

    @Test
    void orderBookRejectsAnEmptyOrderWithATypedError() {
        OrderBook bookSvc = new OrderBook();

        OrderRejectedException ex = catchThrowableOfType(
            OrderRejectedException.class, () -> bookSvc.accept(new Order("A-3", List.of())));

        assertThat(ex.code()).isEqualTo("empty-order");
        assertThat(bookSvc.rejectedCount()).isEqualTo(1L);
    }

    @Test
    void orderBookRejectsAMixedCurrencyOrderWithATypedError() {
        OrderBook bookSvc = new OrderBook();
        Order mixed = new Order("A-4", List.of(
            new LineItem("sku-1", new Money(100L, "USD")),
            new LineItem("sku-2", new Money(200L, "EUR"))));

        OrderRejectedException ex = catchThrowableOfType(
            OrderRejectedException.class, () -> bookSvc.accept(mixed));

        assertThat(ex.code()).isEqualTo("currency-mismatch");
    }

    @Test
    void orderBookReadinessProbeReportsReady() {
        assertThat(new OrderBook().isReady()).isTrue();
    }

    @Test
    void orderTotalsAcrossLines() {
        Order order = new Order("A-5", List.of(
            new LineItem("sku-1", new Money(1_999L, USD)),
            new LineItem("sku-2", new Money(4_950L, USD))));

        assertThat(order.total(USD)).isEqualTo(new Money(6_949L, USD));
    }
}
