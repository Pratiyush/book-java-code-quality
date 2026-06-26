package org.acme.readability;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Applies a {@link DiscountRule} under an externalized pricing policy, with a small observability and
 * health surface.
 *
 * <p>The service exists so the three rule forms are more than static functions: it holds the policy (the
 * discount cap and the no-discount floor) as configuration rather than constants, counts the carts it
 * turns away through the rule's failure path, and answers a readiness probe — the seams the later
 * observability chapter (Chapter 45) builds on. The rule it runs is injected, so the same service drives
 * the nested, the fragmented, and the balanced form identically, which is what lets the
 * behaviour-preservation test swap the rule and assert the result does not move.
 */
public final class PricingService {

    private static final Logger LOG = System.getLogger(PricingService.class.getName());

    private final DiscountRule rule;
    private final Money cap;
    private final long floor;

    /** Observability: how many carts were turned away by the rule's failure path (Chapter 45). */
    private final AtomicLong rejected = new AtomicLong();

    /**
     * Creates a service with the externalized pricing policy.
     *
     * @param rule  the discount rule to apply, never {@code null}
     * @param cap   the maximum discount allowed, never {@code null}
     * @param floor the minimum subtotal below which no discount applies, in minor units, never negative
     * @throws NullPointerException     if {@code rule} or {@code cap} is {@code null}
     * @throws IllegalArgumentException if {@code floor} is negative
     */
    public PricingService(DiscountRule rule, Money cap, long floor) {
        this.rule = Objects.requireNonNull(rule, "rule");
        this.cap = Objects.requireNonNull(cap, "cap");
        if (floor < 0) {
            throw new IllegalArgumentException("floor must not be negative: " + floor);
        }
        this.floor = floor;
    }

    /**
     * Prices a cart by applying the configured rule under the configured policy.
     *
     * @param cart the cart to price, never {@code null}
     * @return the discount amount, in the cart's currency, never {@code null}
     * @throws NullPointerException if {@code cart} is {@code null}
     * @throws PricingException     if the policy rejects the request (propagated from the rule)
     */
    public Money priceDiscount(Cart cart) {
        Objects.requireNonNull(cart, "cart");
        try {
            Money discount = rule.discountFor(cart, cap, floor);
            LOG.log(Level.DEBUG, "priced discount {0} for tier {1}", discount.minorUnits(), cart.tier());
            return discount;
        } catch (PricingException e) {
            rejected.incrementAndGet();
            throw e;
        }
    }

    /**
     * Health/observability surface: the running count of carts rejected by the rule's failure path.
     *
     * @return the number of rejected carts since startup, never negative
     */
    public long rejectedCount() {
        return rejected.get();
    }

    /**
     * A readiness probe: the service is ready as soon as it is constructed with a valid policy.
     *
     * @return {@code true} when the service can price carts
     */
    public boolean isReady() {
        return cap.minorUnits() >= 0 && floor >= 0;
    }
}
