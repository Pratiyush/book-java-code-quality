package org.acme.design.cycle.orders;

/**
 * A minimal placed order: an identifier and a total, shared between the two packages in the cycle.
 *
 * @param id          the order identifier, never {@code null}
 * @param totalMinorUnits the order total in minor units, never negative
 */
public record PlacedOrder(String id, long totalMinorUnits) {

    /**
     * Validates the components.
     *
     * @throws IllegalArgumentException if {@code id} is {@code null} or {@code totalMinorUnits} is negative
     */
    public PlacedOrder {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (totalMinorUnits < 0) {
            throw new IllegalArgumentException("totalMinorUnits must not be negative");
        }
    }
}
