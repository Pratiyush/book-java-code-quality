package org.acme.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * The parameterized layer: one test body over a finite, hand-picked table of known input cases.
 *
 * <p>This is the chapter's cheapest step beyond an example test. The inputs are still chosen by the
 * author — a {@code @CsvSource} table of quantities and expected totals, a {@code @ValueSource} of
 * rejected quantities — so the parameterized test widens <em>how many</em> known cases run, not
 * <em>which</em> unimagined kinds; that second gap is what the property test ({@link SkuPropertyTest})
 * closes. Each row is reported as its own case, which removes the copy-pasted near-identical tests the
 * chapter names as a smell.
 */
class PriceListParameterizedTest {

    @DisplayName("line total = unit price x quantity, over a table of known cases")
    // tag::parameterized-table[]
    @ParameterizedTest(name = "{0} x {1}c = {2}c")
    @CsvSource({"1, 500, 500", "2, 500, 1000", "3, 250, 750", "10, 999, 9990"})
    void lineTotalOverAKnownTable(int quantity, long unitMinor, long expectedMinor) {
        Money total = PriceList.lineTotal(new Money(unitMinor, "USD"), quantity);
        assertThat(total.minorUnits()).isEqualTo(expectedMinor);
    }
    // end::parameterized-table[]

    @DisplayName("a quantity outside [1, 1000] is rejected (the boundary cases)")
    @ParameterizedTest(name = "quantity {0} is rejected")
    @ValueSource(ints = {0, -1, 1001})
    void rejectsOutOfRangeQuantity(int quantity) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> PriceList.lineTotal(new Money(500L, "USD"), quantity));
    }
}
