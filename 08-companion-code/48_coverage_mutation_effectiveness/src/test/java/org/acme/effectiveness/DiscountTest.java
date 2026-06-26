package org.acme.effectiveness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The strong test — same code, real assertions, so the mutants die.
 *
 * <p>Line coverage of {@link Discount#apply} is identical to {@link DiscountWeakTest}'s: the lines
 * executed are the same. What changes is that this class <em>checks</em> the boundary and the
 * computed value, so the mutations the weak test leaves alive are now caught. That invariance is the
 * chapter's whole point — coverage does not move when assertions are added, but the mutation score
 * does, because coverage records execution and mutation score records detection.
 */
class DiscountTest {

    private final Discount discount = new Discount();
    private final Money price = new Money(1_000L, "USD");

    @Nested
    @DisplayName("the quantity boundary (kills CONDITIONALS_BOUNDARY)")
    class Boundary {

        @Test
        @DisplayName("one below the threshold is charged the full price")
        void belowThresholdIsNotDiscounted() {
            // tag::strong-test[]
            // qty == 9 (below) keeps the full price; qty == 10 (at threshold) is discounted.
            // Pinning BOTH sides of the boundary is what kills the >=/> CONDITIONALS_BOUNDARY mutant.
            assertThat(discount.apply(price, 9, 0.10)).isEqualTo(new Money(1_000L, "USD"));
            assertThat(discount.apply(price, 10, 0.10)).isEqualTo(new Money(900L, "USD"));
            // end::strong-test[]
        }

        @Test
        @DisplayName("at and above the threshold the bulk rate applies")
        void atOrAboveThresholdIsDiscounted() {
            assertThat(discount.apply(price, 10, 0.10).minorUnits()).isEqualTo(900L);
            assertThat(discount.apply(price, 100, 0.10).minorUnits()).isEqualTo(900L);
        }
    }

    @Test
    @DisplayName("the discounted amount is exact price*(1-rate) (kills MATH)")
    void computesTheDiscountedValueExactly() {
        assertThat(discount.apply(price, 10, 0.25)).isEqualTo(new Money(750L, "USD"));
        assertThat(discount.apply(price, 10, 0.0)).isEqualTo(new Money(1_000L, "USD"));
    }

    @Test
    @DisplayName("the observability counter advances only on the discount path")
    void countsOnlyAppliedDiscounts() {
        Discount counted = new Discount();
        counted.apply(price, 5, 0.10);  // below threshold: no discount applied
        counted.apply(price, 20, 0.10); // at/above threshold: one discount applied
        assertThat(counted.discountsApplied()).isEqualTo(1L);
    }

    @Nested
    @DisplayName("the explicit failure path: input validation rejects bad arguments")
    class FailurePath {

        @Test
        @DisplayName("a negative quantity is rejected")
        void rejectsNegativeQuantity() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> discount.apply(price, -1, 0.10));
        }

        @Test
        @DisplayName("a rate outside [0, 1) is rejected")
        void rejectsOutOfRangeRate() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> discount.apply(price, 10, 1.0));
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> discount.apply(price, 10, -0.1));
        }

        @Test
        @DisplayName("a null price is rejected fast")
        void rejectsNullPrice() {
            assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> discount.apply(null, 10, 0.10));
        }
    }
}
