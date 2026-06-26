package org.acme.testdiscipline;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Confirms seat reservations and decides whether a confirmed reservation has expired.
 *
 * <p>The service exists to give the chapter's determinism material something real to act on. Two design
 * choices are deliberate, and each defuses a flake the chapter's root-cause matrix names:
 *
 * <ul>
 *   <li>Time is read through an injected {@link Clock}, never a direct {@code Instant.now()}. A test
 *       constructs the service with {@link Clock#fixed(Instant, java.time.ZoneId)} and asserts against a
 *       known instant, so the time-dependent path is reproducible instead of racing the wall clock.</li>
 *   <li>The confirmed seats are carried in a {@code Set}, so any check of them is order-independent by
 *       construction — the unordered-collection flake cannot arise.</li>
 * </ul>
 *
 * <p>The service validates its input before recording anything, so a bad reservation fails fast with a
 * typed {@link ReservationRejectedException} — the explicit failure path.
 */
public final class ReservationService {

    private static final Logger LOG = System.getLogger(ReservationService.class.getName());

    // tag::clock-injection[]
    private final Clock clock; // injected, never Instant.now(): the time-dependent path stays testable

    /** Creates a service that reads time from {@code clock} (a test supplies a fixed one). */
    public ReservationService(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }
    // end::clock-injection[]

    /** Observability: reservations confirmed since startup (illustrative, Chapter 45/106). */
    private final AtomicLong confirmed = new AtomicLong();

    /** Observability: reservations rejected by a contract guard since startup. */
    private final AtomicLong rejected = new AtomicLong();

    /**
     * Confirms a reservation, stamping it with the current instant read from the injected clock.
     *
     * @param id    the reservation id, never {@code null} and never blank
     * @param seats the seats to hold, never {@code null} and never empty
     * @return the confirmed, immutable reservation, never {@code null}
     * @throws NullPointerException          if {@code id} or {@code seats} is {@code null}
     * @throws ReservationRejectedException  if {@code id} is blank or {@code seats} is empty
     * @implSpec Validates every argument before recording anything, so a rejected reservation is never
     *     counted as confirmed and never stamped.
     */
    public Reservation confirm(String id, Set<String> seats) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(seats, "seats");
        if (id.isBlank()) {
            rejected.incrementAndGet();
            throw new ReservationRejectedException("blank-id", "a reservation needs a non-blank id");
        }
        if (seats.isEmpty()) {
            rejected.incrementAndGet();
            throw new ReservationRejectedException("no-seats", "a reservation needs at least one seat");
        }
        Reservation reservation = new Reservation(id, seats, Instant.now(clock));
        confirmed.incrementAndGet();
        return reservation;
    }

    /**
     * Reports whether a reservation has expired, measured from its confirmation instant against the
     * injected clock.
     *
     * @param reservation the reservation to check, never {@code null}
     * @param holdFor     how long a confirmation is held before it expires, never {@code null} or negative
     * @return {@code true} when the hold window has elapsed as of the clock's current instant
     * @throws NullPointerException     if any argument is {@code null}
     * @throws IllegalArgumentException if {@code holdFor} is negative
     */
    public boolean isExpired(Reservation reservation, Duration holdFor) {
        Objects.requireNonNull(reservation, "reservation");
        Objects.requireNonNull(holdFor, "holdFor");
        if (holdFor.isNegative()) {
            throw new IllegalArgumentException("holdFor must not be negative: " + holdFor);
        }
        Instant deadline = reservation.confirmedAt().plus(holdFor);
        return !Instant.now(clock).isBefore(deadline);
    }

    /**
     * Health/observability surface: the running count of reservations confirmed.
     *
     * @return the number of confirmed reservations since startup, never negative
     */
    public long confirmedCount() {
        return confirmed.get();
    }

    /**
     * Health/observability surface: the running count of reservations rejected by a contract guard.
     *
     * @return the number of rejected reservations since startup, never negative
     */
    public long rejectedCount() {
        return rejected.get();
    }

    /**
     * A readiness probe: the service is ready when its clock is wired.
     *
     * @return {@code true} when the service can confirm reservations
     */
    public boolean isReady() {
        boolean ready = clock != null;
        if (!ready) {
            LOG.log(Level.WARNING, "reservation service not ready: clock is unwired");
        }
        return ready;
    }
}
