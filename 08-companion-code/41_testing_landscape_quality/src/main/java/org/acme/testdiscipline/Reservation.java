package org.acme.testdiscipline;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * An immutable seat reservation: an id, the seats it holds, and the instant it was confirmed.
 *
 * <p>This is a <em>value object</em>: its state never changes after construction, and two instances
 * with the same components are equal. Two of its properties are what the chapter's determinism material
 * turns on. The {@link #seats()} are a {@code Set}, so a test that checks them must not depend on
 * iteration order — the unordered-collection flake the chapter's matrix names. The {@link #confirmedAt()}
 * instant is supplied by the service from an injected clock rather than read from the wall clock, so a
 * test can pin it exactly.
 *
 * @param id          the reservation id, never blank
 * @param seats       the confirmed seat labels, never {@code null} and never empty; copied defensively
 * @param confirmedAt the instant the reservation was confirmed, never {@code null}
 */
public record Reservation(String id, Set<String> seats, Instant confirmedAt) {

    /**
     * Validates and defensively copies the components so an invalid or mutable {@code Reservation} can
     * never exist.
     *
     * @throws NullPointerException     if any component is {@code null}
     * @throws IllegalArgumentException if {@code id} is blank or {@code seats} is empty
     */
    public Reservation {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(seats, "seats");
        Objects.requireNonNull(confirmedAt, "confirmedAt");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (seats.isEmpty()) {
            throw new IllegalArgumentException("a reservation must hold at least one seat");
        }
        // Defensive copy in: an immutable snapshot the caller cannot mutate after construction.
        seats = Set.copyOf(seats);
    }
}
