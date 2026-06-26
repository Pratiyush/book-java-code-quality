package org.acme.design.bylayer.repository;

/**
 * A minimal order value. Under by-layer slicing the data type lives with the repository layer, away
 * from the service and controller that also work on orders.
 *
 * @param id              the order identifier, never {@code null}
 * @param totalMinorUnits the order total in minor units, never negative
 */
public record Order(String id, long totalMinorUnits) {

    /**
     * Validates the components.
     *
     * @throws IllegalArgumentException if {@code id} is {@code null} or {@code totalMinorUnits} is negative
     */
    public Order {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (totalMinorUnits < 0) {
            throw new IllegalArgumentException("totalMinorUnits must not be negative");
        }
    }
}
