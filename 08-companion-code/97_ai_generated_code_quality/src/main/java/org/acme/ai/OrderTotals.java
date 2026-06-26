package org.acme.ai;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Computes an order's payable total: the goods subtotal plus shipping, where shipping is waived at or
 * above a free-shipping threshold. This is the small, behaviour-rich method the test-generation half of
 * the chapter puts under both a generated-from-code test and a spec-derived test.
 *
 * <p>The method is deliberately full of the things a mutation operator can change: a relational
 * boundary ({@code subtotal >= FREE_SHIPPING_THRESHOLD}, the {@code CONDITIONALS_BOUNDARY} target), an
 * addition on the paid-shipping path ({@code subtotal + shippingFee}, the {@code MATH} target), and an
 * early {@code return} on the free-shipping path (a returns-mutator target). A test can <em>execute</em>
 * every one of those lines while <em>asserting</em> nothing about them — the precise gap the chapter's
 * tests-from-code trap exploits: {@code AiTestFromCode} reaches full line coverage yet leaves the
 * boundary and the arithmetic unchecked, while {@code SpecDerivedTotalsTest} pins each one down.
 *
 * <p>The class is stateless except for an illustrative counter that doubles as the observability
 * surface (Chapter 45): how many totals it has computed with shipping waived since startup.
 */
public final class OrderTotals {

    private static final Logger LOG = System.getLogger(OrderTotals.class.getName());

    /** The subtotal at and above which shipping is free. */
    public static final long FREE_SHIPPING_THRESHOLD = 5_000L;

    /** Observability: totals computed with shipping waived since startup (illustrative, Chapter 45). */
    private final AtomicLong freeShippingGranted = new AtomicLong();

    /**
     * Returns the payable total for a goods subtotal and a shipping fee, waiving shipping at or above
     * the free-shipping threshold.
     *
     * @param subtotal    the goods subtotal, never {@code null}
     * @param shippingFee the shipping fee charged below the threshold, never {@code null}
     * @return the payable total, never {@code null}
     * @throws NullPointerException     if either argument is {@code null}
     * @throws IllegalArgumentException if either amount is negative or the currencies differ
     * @implSpec At or above {@link #FREE_SHIPPING_THRESHOLD} the subtotal is returned unchanged (the
     *     free-shipping early-return path); below it the shipping fee is added.
     */
    public Money payableTotal(Money subtotal, Money shippingFee) {
        validate(subtotal, shippingFee);
        // tag::under-test[]
        if (subtotal.minorUnits() >= FREE_SHIPPING_THRESHOLD) { // CONDITIONALS_BOUNDARY mutates >= to >
            freeShippingGranted.incrementAndGet();
            return subtotal;                                    // free-shipping path: no fee added
        }
        return subtotal.plus(shippingFee);                      // MATH mutates the addition here
        // end::under-test[]
    }

    /**
     * Validates the inputs before any computation — the explicit failure path.
     *
     * @throws NullPointerException     if either argument is {@code null}
     * @throws IllegalArgumentException if either amount is negative
     */
    private static void validate(Money subtotal, Money shippingFee) {
        Objects.requireNonNull(subtotal, "subtotal");
        Objects.requireNonNull(shippingFee, "shippingFee");
        if (subtotal.minorUnits() < 0L || shippingFee.minorUnits() < 0L) {
            LOG.log(Level.WARNING, "rejecting negative amount in order total");
            throw new IllegalArgumentException("amounts must not be negative");
        }
    }

    /**
     * Health/observability surface: the running count of totals computed with shipping waived.
     *
     * @return the number of free-shipping grants since startup, never negative
     */
    public long freeShippingGranted() {
        return freeShippingGranted.get();
    }
}
