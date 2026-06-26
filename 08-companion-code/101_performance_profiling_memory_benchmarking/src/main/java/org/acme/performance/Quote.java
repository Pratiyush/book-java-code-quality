package org.acme.performance;

/**
 * The priced result of an order: the subtotal, the discount applied, the total, and a count of lines
 * that qualified for free shipping.
 *
 * <p>An immutable record returned from {@link OrderPricing#priceOrder}. Returning a value (rather than
 * computing and discarding) is also what keeps the pricing work observable — the same principle a JMH
 * benchmark relies on so the JIT may not eliminate the computation as dead code.
 *
 * @param subtotalMinor  the sum of the line extended prices, in minor units
 * @param discountMinor  the discount applied, in minor units
 * @param totalMinor     the payable total after discount, in minor units
 * @param freeShipLines  how many lines qualified for free shipping
 */
public record Quote(long subtotalMinor, long discountMinor, long totalMinor, int freeShipLines) {

    /** Validates the quote's internal consistency at construction. */
    public Quote {
        if (subtotalMinor < 0 || discountMinor < 0 || totalMinor < 0 || freeShipLines < 0) {
            throw new IllegalArgumentException("a quote carries no negative amounts");
        }
    }
}
