package org.acme.orders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The same fact, asserted in the styles the chapter contrasts.
 *
 * <p>Three of the four styles from the chapter's table are compiled here: JUnit's built-in
 * {@code Assertions}, AssertJ's fluent {@code assertThat}, and Hamcrest's matcher
 * {@code assertThat}. The fourth, Truth, is shown in the README and the prose for comparison rather
 * than compiled. The point is not which style wins — the chapter crowns none — but that each states
 * the same expectation, and the most-read line on failure is only as good as the expectation written
 * into it.
 */
class AssertionStylesTest {

    private static final Money TOTAL = new Money(5_000L, "USD");

    @Test
    @DisplayName("the same equality fact in three assertion styles")
    void sameFactThreeStyles() {
        // tag::four-assertion-styles[]
        // JUnit built-in: zero extra dependency, basic failure message.
        assertEquals(5_000L, TOTAL.minorUnits());
        // AssertJ: fluent, IDE-discoverable after assertThat(.
        org.assertj.core.api.Assertions.assertThat(TOTAL.minorUnits()).isEqualTo(5_000L);
        // Hamcrest: matcher composition, reads declaratively.
        assertThat(TOTAL.minorUnits(), is(equalTo(5_000L)));
        // end::four-assertion-styles[]
    }

    @Test
    @DisplayName("exceptions: assertThrows (built-in) and assertThatThrownBy (AssertJ)")
    void exceptionStyles() {
        assertThrows(IllegalArgumentException.class, () -> new Money(-1L, "USD"));
        org.assertj.core.api.Assertions
            .assertThatThrownBy(() -> new Money(-1L, "USD"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("minorUnits");
    }

    @Test
    @DisplayName("assertAll reports several built-in failures at once (soft assertions)")
    void softAssertionsWithAssertAll() {
        Money total = new Money(5_000L, "USD");
        assertAll("total",
            () -> assertEquals(5_000L, total.minorUnits()),
            () -> assertEquals("USD", total.currency()));
    }
}
