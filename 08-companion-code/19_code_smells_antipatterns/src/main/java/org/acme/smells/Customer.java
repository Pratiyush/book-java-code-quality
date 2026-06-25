package org.acme.smells;

import java.util.Objects;

/**
 * A customer placing an order — an immutable value record.
 *
 * <p>The {@code loyaltyTier} is modelled as a {@link LoyaltyTier} enum rather than a bare
 * {@link String}, so an unknown tier cannot be constructed and the discount rule branches on a closed
 * set of cases. That is the fix for the <em>Primitive Obsession</em> smell (a string standing in for a
 * real type, Effective Java Item 62) the smelly service would otherwise show.
 *
 * @param id    the customer identifier, never {@code null}
 * @param tier  the loyalty tier, never {@code null}
 */
public record Customer(String id, LoyaltyTier tier) {

    /**
     * Validates the components so a {@code Customer} is well-formed for its whole lifetime.
     *
     * @throws NullPointerException if {@code id} or {@code tier} is {@code null}
     */
    public Customer {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(tier, "tier");
    }
}
