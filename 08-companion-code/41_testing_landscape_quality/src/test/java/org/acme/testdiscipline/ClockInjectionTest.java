package org.acme.testdiscipline;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Determinism fix: a time-dependent decision made reproducible with an injected, fixed {@link Clock}.
 *
 * <p>The chapter's flakiness matrix names {@code now()} in an assertion as a root cause and an injected
 * {@code Clock} ({@code Clock.fixed}) as the fix. The service reads time only through its clock, so a
 * test supplies a fixed one and pins both the confirmation instant and the expiry boundary exactly —
 * no race with the wall clock, the same result on every run and machine.
 */
class ClockInjectionTest {

    private static final Instant NOON = Instant.parse("2026-06-20T12:00:00Z");

    @Test
    @DisplayName("expiry is decided against a fixed clock, so the boundary is reproducible")
    void expiryIsDeterministicUnderAFixedClock() {
        // tag::clock-fixed[]
        Clock atNoon = Clock.fixed(NOON, ZoneOffset.UTC);
        ReservationService service = new ReservationService(atNoon);
        Reservation r = service.confirm("res-1", Set.of("A1"));
        // Confirmed exactly at noon; a 30-minute hold has NOT yet elapsed at noon.
        assertThat(r.confirmedAt()).isEqualTo(NOON);
        assertThat(service.isExpired(r, Duration.ofMinutes(30))).isFalse();
        // end::clock-fixed[]
    }

    @Test
    @DisplayName("advancing the clock past the hold window flips expiry deterministically")
    void advancingTheClockExpiresTheReservation() {
        ReservationService confirmAtNoon = new ReservationService(Clock.fixed(NOON, ZoneOffset.UTC));
        Reservation r = confirmAtNoon.confirm("res-2", Set.of("B2"));

        Instant elevenMinutesLater = NOON.plus(Duration.ofMinutes(11));
        ReservationService checkLater = new ReservationService(Clock.fixed(elevenMinutesLater, ZoneOffset.UTC));

        assertThat(checkLater.isExpired(r, Duration.ofMinutes(10))).isTrue();
    }
}
