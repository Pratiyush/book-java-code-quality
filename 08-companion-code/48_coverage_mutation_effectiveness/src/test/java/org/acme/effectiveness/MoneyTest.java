package org.acme.effectiveness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Covers the {@link Money} value type, including both sides of each guard branch.
 *
 * <p>These cases exist partly to demonstrate the chapter's BRANCH-coverage point on the supporting
 * type: the constructor's negative-amount and blank-currency guards are decision paths a value-only
 * test would leave half-covered, which BRANCH coverage (unlike LINE) surfaces.
 */
class MoneyTest {

    @Test
    @DisplayName("a valid amount is held exactly")
    void holdsAValidAmount() {
        Money money = new Money(1_000L, "USD");
        assertThat(money.minorUnits()).isEqualTo(1_000L);
        assertThat(money.currency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("a negative amount is rejected")
    void rejectsNegativeAmount() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Money(-1L, "USD"));
    }

    @Test
    @DisplayName("a null currency is rejected")
    void rejectsNullCurrency() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> new Money(100L, null));
    }

    @Test
    @DisplayName("a blank currency is rejected")
    void rejectsBlankCurrency() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Money(100L, "  "));
    }
}
