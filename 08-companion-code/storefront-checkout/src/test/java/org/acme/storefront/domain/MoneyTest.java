package org.acme.storefront.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void plusAddsSameCurrency() {
        assertThat(new Money(100L, "USD").plus(new Money(50L, "USD")))
                .isEqualTo(new Money(150L, "USD"));
    }

    @Test
    void timesMultiplies() {
        assertThat(new Money(45L, "USD").times(3)).isEqualTo(new Money(135L, "USD"));
    }

    @Test
    void zeroIsZeroAmount() {
        assertThat(Money.zero("USD").amountMinor()).isZero();
    }

    @Test
    void rejectsNegativeAmount() {
        assertThatThrownBy(() -> new Money(-1L, "USD"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNonThreeLetterCurrency() {
        assertThatThrownBy(() -> new Money(1L, "US"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void plusRejectsCurrencyMismatch() {
        assertThatThrownBy(() -> new Money(1L, "USD").plus(new Money(1L, "EUR")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("currency mismatch");
    }

    @Test
    void timesOverflowFailsFast() {
        assertThatThrownBy(() -> new Money(Long.MAX_VALUE, "USD").times(2))
                .isInstanceOf(ArithmeticException.class);
    }
}
