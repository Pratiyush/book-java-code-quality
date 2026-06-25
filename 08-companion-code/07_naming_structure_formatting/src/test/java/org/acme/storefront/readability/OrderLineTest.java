package org.acme.storefront.readability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's mechanism made buildable: the conventionally-named {@link OrderLine}
 * computes the same result the cramped before-state would have, and the explicit failure path rejects
 * malformed input fail-fast. The rename and reformat are asserted to be semantics-preserving — the
 * {@code -Pquality} build asserts, in parallel, that the mis-cased names of the before-state would not
 * pass the Checkstyle naming gate at all.
 */
class OrderLineTest {

    private static final Money ONE_DOLLAR = new Money(100L, "USD");

    @Test
    void lineTotalIsUnitPriceTimesQuantity() {
        OrderLine line = new OrderLine("SKU-42", 3, ONE_DOLLAR);

        assertThat(line.lineTotal().minorUnits()).isEqualTo(300L);
        assertThat(line.lineTotal().currency()).isEqualTo("USD");
    }

    @Test
    void acceptsQuantityAtTheConstantCeiling() {
        OrderLine line = new OrderLine("SKU-42", OrderLine.MAX_QUANTITY_PER_LINE, ONE_DOLLAR);

        assertThat(line.quantity()).isEqualTo(999);
    }

    @Test
    void validatedLineBuildsTheSameValueAsTheConstructor() {
        OrderLine line = ReadabilityNotes.validatedLine("SKU-42", 2, ONE_DOLLAR);

        assertThat(line.lineTotal().minorUnits()).isEqualTo(200L);
    }

    @Test
    void failsFastOnBlankSku() {
        assertThatThrownBy(() -> ReadabilityNotes.validatedLine("  ", 1, ONE_DOLLAR))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("sku");
    }

    @Test
    void failsFastOnQuantityAboveTheCeiling() {
        assertThatThrownBy(
                () -> ReadabilityNotes.validatedLine("SKU-42", OrderLine.MAX_QUANTITY_PER_LINE + 1, ONE_DOLLAR))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("quantity");
    }

    @Test
    void failsFastOnNullUnitPrice() {
        assertThatNullPointerException()
            .isThrownBy(() -> ReadabilityNotes.validatedLine("SKU-42", 1, null))
            .withMessageContaining("unitPrice");
    }
}
