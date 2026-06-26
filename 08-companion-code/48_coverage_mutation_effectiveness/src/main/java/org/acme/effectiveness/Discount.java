package org.acme.effectiveness;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Applies a bulk discount above a quantity threshold — the one behaviour-rich method the chapter puts
 * under both coverage and mutation testing.
 *
 * <p>The method is deliberately small but full of the things a mutation operator can change: a
 * relational boundary ({@code qty >= THRESHOLD}, the {@code CONDITIONALS_BOUNDARY} target), an
 * arithmetic expression ({@code price * (1 - rate)}, the {@code MATH} target), and an early
 * {@code return} on the no-discount path (a returns-mutator target). A test can <em>execute</em>
 * every one of those lines while <em>asserting</em> nothing about them — which is the precise gap
 * the chapter measures: {@link DiscountWeakTest} reaches full line coverage yet leaves the boundary
 * and the arithmetic unchecked, while {@link DiscountTest} pins each one down.
 *
 * <p>The class is stateless except for an illustrative counter that doubles as the observability
 * surface (Chapter 45): how many discounted prices it has computed since startup.
 */
public final class Discount {

    private static final Logger LOG = System.getLogger(Discount.class.getName());

    /** The quantity at and above which the bulk rate applies. */
    public static final long THRESHOLD = 10L;

    /** Observability: discounted prices computed since startup (illustrative, Chapter 45). */
    private final AtomicLong discountsApplied = new AtomicLong();

    /**
     * Returns the unit price after a bulk discount, or the original price below the threshold.
     *
     * @param unitPrice the per-unit price, never {@code null}
     * @param quantity  the order quantity, never negative
     * @param rate      the bulk discount rate in {@code [0, 1)}, e.g. {@code 0.10} for ten percent
     * @return the price each unit is charged at, never {@code null}
     * @throws NullPointerException     if {@code unitPrice} is {@code null}
     * @throws IllegalArgumentException if {@code quantity} is negative or {@code rate} is outside
     *     {@code [0, 1)}
     * @implSpec Below {@link #THRESHOLD} the original price is returned unchanged (the early-return
     *     path); at or above it the price is scaled by {@code (1 - rate)} in exact minor-unit math.
     */
    public Money apply(Money unitPrice, long quantity, double rate) {
        validate(unitPrice, quantity, rate);
        // tag::under-test[]
        if (quantity < THRESHOLD) {       // CONDITIONALS_BOUNDARY mutates >= to > here
            return unitPrice;             // early-return path: no discount below the threshold
        }
        long discounted = Math.round(unitPrice.minorUnits() * (1.0 - rate)); // MATH mutates * and -
        discountsApplied.incrementAndGet();
        return new Money(discounted, unitPrice.currency());
        // end::under-test[]
    }

    /**
     * Validates the discount inputs before any computation — the explicit failure path.
     *
     * @throws NullPointerException     if {@code unitPrice} is {@code null}
     * @throws IllegalArgumentException if {@code quantity} is negative or {@code rate} is outside
     *     {@code [0, 1)}
     */
    private static void validate(Money unitPrice, long quantity, double rate) {
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (quantity < 0L) {
            throw new IllegalArgumentException("quantity must not be negative: " + quantity);
        }
        if (rate < 0.0 || rate >= 1.0) {
            LOG.log(Level.WARNING, "rejecting out-of-range discount rate: {0}", rate);
            throw new IllegalArgumentException("rate must be in [0, 1): " + rate);
        }
    }

    /**
     * Health/observability surface: the running count of discounted prices computed.
     *
     * @return the number of discounts applied since startup, never negative
     */
    public long discountsApplied() {
        return discountsApplied.get();
    }
}
