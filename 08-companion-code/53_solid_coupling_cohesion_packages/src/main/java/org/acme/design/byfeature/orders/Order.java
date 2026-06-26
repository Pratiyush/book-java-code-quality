package org.acme.design.byfeature.orders;

/**
 * The orders feature's value type — and the narrow surface other features read it through.
 *
 * <p>Under by-feature slicing this type, its service, and its storage live together in one package,
 * so an orders change stays local. {@code billing} depends on this published record only.
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
