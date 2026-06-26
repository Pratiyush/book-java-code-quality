package org.acme.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The property-based layer: an invariant asserted over generated inputs, plus a runnable demonstration
 * of shrinking to a minimal counterexample.
 *
 * <p>The chapter's named Java property-based library, jqwik, is in maintenance mode and is not on this
 * build's pinned dependency set; it is cited in the prose and recorded in {@code 09-flags/} as
 * cited-not-built (see the module README). The two ideas it provides — generated inputs and shrinking —
 * are realized here with the JDK only: {@link SkuGenerator} generates the inputs (seeded, so a failure
 * replays), a {@code @MethodSource} feeds them to one test body as the invariant check, and
 * {@link Shrinker} reduces a known failure to its minimal counterexample.
 */
class SkuPropertyTest {

    /** A seeded sample of generated SKUs, including the boundary item numbers (0 and 9999). */
    static Stream<Sku> generatedSkus() {
        List<Sku> sample = SkuGenerator.seeded(20_260_620L).sample(200);
        return sample.stream();
    }

    @DisplayName("round-trip invariant: parse(format(sku)) equals sku, over generated inputs")
    // tag::property-roundtrip[]
    @ParameterizedTest(name = "round-trips {0}")
    @MethodSource("generatedSkus")
    void parseOfFormatRoundTrips(Sku generated) {
        // The invariant holds for ALL valid SKUs, not the few an example test would enumerate.
        assertThat(Sku.parse(generated.format())).isEqualTo(generated);
    }
    // end::property-roundtrip[]

    /**
     * A naive item-number validator with a deliberately seeded bug: it only accepts up to three digits,
     * so it wrongly rejects any item number of 1000 or more. The real domain allows {@code [0, 9999]}.
     * A property over the full domain finds this; an example suite that only tried small numbers would
     * not. The minimal counterexample is exactly 1000 — the first item number the bug rejects.
     *
     * @param itemNumber a candidate item number
     * @return {@code true} if the naive validator accepts it (buggy above 999)
     */
    private static boolean naiveValidatorAccepts(int itemNumber) {
        return itemNumber >= 0 && itemNumber <= 999;
    }

    @Test
    @DisplayName("shrinking reduces a found failure to the minimal counterexample (1000)")
    void shrinkingReportsTheMinimalCounterexample() {
        // The property: "every valid item number is accepted by the validator". Its negation fails for
        // any itemNumber the naive validator rejects. Generation finds a large failing value; shrinking
        // converges on the smallest one, which is far easier to debug than the random value found first.
        int largeFailingValue = Sku.MAX_ITEM_NUMBER; // 9999 — found by generation, fails the naive check
        assertThat(naiveValidatorAccepts(largeFailingValue)).isFalse();

        int minimal = Shrinker.shrinkToMinimum(
            largeFailingValue, Sku.MIN_ITEM_NUMBER, itemNumber -> !naiveValidatorAccepts(itemNumber));

        // Shrinking turns "fails somewhere in [0, 9999]" into the precise minimal counterexample.
        assertThat(minimal).isEqualTo(1000);
    }

    @Test
    @DisplayName("a fixed seed makes the generated sample reproducible (no flaky property)")
    void seedingMakesGenerationReproducible() {
        List<Sku> first = SkuGenerator.seeded(42L).sample(50);
        List<Sku> second = SkuGenerator.seeded(42L).sample(50);
        assertThat(first).isEqualTo(second);
    }
}
