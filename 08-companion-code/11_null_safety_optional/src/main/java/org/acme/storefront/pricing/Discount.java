package org.acme.storefront.pricing;

import java.util.Objects;

/**
 * A discount that applies to an order — an immutable value (Item 17).
 *
 * <p>A {@code Discount} is the thing a promo-code lookup either finds or does not. Modelling "found
 * or not" as an {@link java.util.Optional} of this type, rather than a {@code null Discount}, is the
 * chapter's design-time lever: the absent case becomes a state the caller has to acknowledge instead
 * of a reference it can dereference by mistake. The code and amount are validated at construction, the
 * only place a {@code Discount} comes into being.
 *
 * @param code           the promo code that granted this discount, never {@code null}
 * @param amountOffMinor the amount taken off the order total, in minor units, never negative
 */
public record Discount(String code, long amountOffMinor) {

    /**
     * Validates the components so a {@code Discount} is well-formed for its whole lifetime.
     *
     * @throws NullPointerException     if {@code code} is {@code null}
     * @throws IllegalArgumentException if {@code amountOffMinor} is negative
     */
    public Discount {
        Objects.requireNonNull(code, "code");
        if (amountOffMinor < 0) {
            throw new IllegalArgumentException("amountOffMinor must not be negative: " + amountOffMinor);
        }
    }
}
