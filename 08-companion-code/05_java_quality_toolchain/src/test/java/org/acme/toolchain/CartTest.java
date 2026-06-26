package org.acme.toolchain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's mechanism end to end: the assembled toolchain gating a real aggregate. The
 * test drives the cart total, the explicit failure path, the defensive copy, and the readiness probe,
 * so JaCoCo records branch coverage over the same code Checkstyle and SpotBugs read.
 */
@DisplayName("Cart — the toolchain's analysis subject")
class CartTest {

    @Test
    @DisplayName("totals the lines in whole cents")
    void totalsLinesInCents() {
        var cart = new Cart(List.of(
                new LineItem("SKU-1", 1299, 2),
                new LineItem("SKU-2", 500, 1)));

        assertThat(cart.totalCents()).isEqualTo(3098L);
        assertThat(cart.size()).isEqualTo(2);
        assertThat(cart.isReady()).isTrue();
    }

    @Test
    @DisplayName("an empty cart is not ready to check out")
    void emptyCartIsNotReady() {
        var cart = new Cart(List.of());

        assertThat(cart.totalCents()).isZero();
        assertThat(cart.isReady()).isFalse();
    }

    @Test
    @DisplayName("rejects an invalid line item at construction (the failure path)")
    void rejectsInvalidLineItem() {
        assertThatThrownBy(() -> new LineItem("  ", 100, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("sku");
        assertThatThrownBy(() -> new LineItem("SKU-3", -1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unitCents");
        assertThatThrownBy(() -> new LineItem("SKU-3", 100, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity");
    }

    @Test
    @DisplayName("copies its lines defensively, so the source list cannot mutate it")
    void copiesLinesDefensively() {
        var source = new ArrayList<LineItem>();
        source.add(new LineItem("SKU-1", 1000, 1));
        var cart = new Cart(source);

        source.add(new LineItem("SKU-2", 9999, 9));

        assertThat(cart.size()).isEqualTo(1);
        assertThat(cart.totalCents()).isEqualTo(1000L);
    }
}
