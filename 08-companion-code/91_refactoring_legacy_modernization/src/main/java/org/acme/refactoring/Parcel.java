package org.acme.refactoring;

import java.util.Objects;

/**
 * A parcel to be shipped — the input both the legacy calculator and the modern refactor price.
 *
 * <p>It is modelled as a record with a {@link ServiceLevel} enum rather than the legacy method's bare
 * {@code int} grams plus a magic service-level string, so an unknown service level cannot be
 * constructed and the pricing rule branches on a closed set of cases. Expressing the same input as a
 * real type is part of the modern-Java lens the chapter applies to the 2018-era catalog (Chapter 5):
 * the value the legacy code carried as loose primitives becomes a validated value object.
 *
 * @param grams        the parcel weight in grams, at least one
 * @param serviceLevel the requested service level, never {@code null}
 * @param destination  the destination zone, never {@code null}
 */
public record Parcel(int grams, ServiceLevel serviceLevel, String destination) {

    /**
     * Validates the components so a {@code Parcel} is well-formed for its whole lifetime.
     *
     * @throws NullPointerException     if {@code serviceLevel} or {@code destination} is {@code null}
     * @throws IllegalArgumentException if {@code grams} is not at least one
     */
    public Parcel {
        Objects.requireNonNull(serviceLevel, "serviceLevel");
        Objects.requireNonNull(destination, "destination");
        if (grams < 1) {
            throw new IllegalArgumentException("grams must be at least 1: " + grams);
        }
    }
}
