package org.acme.platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.acme.platform.money.Money;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void addsAmountsInTheSameCurrency() {
        Money sum = Money.of(1099, "USD").plus(Money.of(1, "USD"));
        assertThat(sum.minorUnits()).isEqualTo(1100L);
        assertThat(sum.toDisplayString()).isEqualTo("11.00 USD");
    }

    @Test
    void rejectsCurrencyMismatch() {
        assertThatThrownBy(() -> Money.of(100, "USD").plus(Money.of(100, "EUR")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("currency mismatch");
    }

    @Test
    void rejectsMalformedCurrencyCode() {
        assertThatThrownBy(() -> Money.of(100, "DOLLARS"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void multipliesByQuantity() {
        assertThat(Money.of(250, "USD").times(3)).isEqualTo(Money.of(750, "USD"));
    }

    @Test
    void displaysNegativeAmountsWithoutDoubledSign() {
        assertThat(Money.of(-1099, "USD").toDisplayString()).isEqualTo("-10.99 USD");
    }
}
