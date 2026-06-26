package org.acme.effectiveness;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The coverage-padding test — the chapter's failure path made tactile.
 *
 * <p>It calls {@link Discount#apply} on both sides of the threshold, so every line of the method
 * runs and JaCoCo records full <em>line</em> coverage for it. But it asserts only that the result is
 * non-null: it never checks the boundary ({@code qty == 9} versus {@code qty == 10}) and never checks
 * the discounted value. Run PITest over the module with this test alone and the
 * {@code CONDITIONALS_BOUNDARY}, {@code MATH}, and returns mutants on {@code apply} all
 * <strong>survive</strong> — covered, executed, and yet untested. That surviving-mutant-on-a-covered-
 * line is the precise thing coverage cannot see, and the reason the chapter exists.
 *
 * <p>This class is kept in the suite on purpose: it documents, as runnable code, that a green line
 * number is not a quality verdict. The mutants it leaves alive are killed by {@link DiscountTest},
 * whose assertions do the checking this one omits.
 */
class DiscountWeakTest {

    private final Discount discount = new Discount();

    @Test
    @DisplayName("executes every line of apply() but asserts nothing about the result (the padding trap)")
    void coversEveryLineWithoutCheckingBehaviour() {
        Money price = new Money(1_000L, "USD");
        // tag::weak-test[]
        // Runs the below-threshold path AND the discount path: full line coverage...
        Money belowThreshold = discount.apply(price, 9, 0.10);
        Money atThreshold = discount.apply(price, 10, 0.10);
        // ...but the only assertion is "not null", so every mutant on apply() survives.
        assertThat(belowThreshold).isNotNull();
        assertThat(atThreshold).isNotNull();
        // end::weak-test[]
    }
}
