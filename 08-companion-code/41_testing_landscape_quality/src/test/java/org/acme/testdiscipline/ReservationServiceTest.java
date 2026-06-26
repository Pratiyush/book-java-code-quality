package org.acme.testdiscipline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The reservation service end to end: the happy path, the explicit failure path, the readiness probe,
 * and the externalized config profiles.
 *
 * <p>This is the module's integration-style test (EXAMPLES-GUIDE req. 3): it wires the service with a
 * fixed clock and drives the mechanism the chapter's determinism material rests on, including the typed
 * failure path the honest-limitations floor asks the module to demonstrate in code.
 */
class ReservationServiceTest {

    private static final Instant NOON = Instant.parse("2026-06-20T12:00:00Z");

    private final ReservationService service = new ReservationService(Clock.fixed(NOON, ZoneOffset.UTC));

    @Test
    @DisplayName("confirms a reservation, stamps it from the clock, and counts it")
    void confirmsAndCountsAReservation() {
        Reservation r = service.confirm("res-1", Set.of("A1", "B2"));

        assertThat(r.confirmedAt()).isEqualTo(NOON);
        assertThat(service.confirmedCount()).isEqualTo(1L);
        assertThat(service.isExpired(r, Duration.ofMinutes(15))).isFalse();
    }

    @Test
    @DisplayName("the readiness probe reports ready when the clock is wired")
    void readinessProbeReportsReady() {
        assertThat(service.isReady()).isTrue();
    }

    @Nested
    @DisplayName("the explicit failure path")
    class FailurePath {

        @Test
        @DisplayName("a blank id is rejected with a typed, branchable code, before anything is recorded")
        void rejectsABlankId() {
            assertThatExceptionOfType(ReservationRejectedException.class)
                .isThrownBy(() -> service.confirm("  ", Set.of("A1")))
                .matches(ex -> "blank-id".equals(ex.code()));

            assertThat(service.confirmedCount()).isZero();
            assertThat(service.rejectedCount()).isEqualTo(1L);
        }

        @Test
        @DisplayName("a seatless reservation is rejected with a typed, branchable code")
        void rejectsAnEmptySeatSet() {
            assertThatExceptionOfType(ReservationRejectedException.class)
                .isThrownBy(() -> service.confirm("res-2", Set.of()))
                .matches(ex -> "no-seats".equals(ex.code()));

            assertThat(service.confirmedCount()).isZero();
        }

        @Test
        @DisplayName("a negative hold window is rejected by the expiry check")
        void rejectsANegativeHoldWindow() {
            Reservation r = service.confirm("res-3", Set.of("C3"));

            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.isExpired(r, Duration.ofSeconds(-1)));
        }
    }

    @Nested
    @DisplayName("externalized configuration profiles")
    class ConfigProfiles {

        @Test
        @DisplayName("the dev profile reads its own overridden hold window")
        void devProfileOverridesHoldWindow() {
            ReservationConfig dev = ReservationConfig.load("dev");

            assertThat(dev.holdWindow()).isEqualTo(Duration.ofSeconds(30));
            assertThat(dev.asyncPollTimeout()).isEqualTo(Duration.ofSeconds(5));
        }

        @Test
        @DisplayName("the prod profile reads its own overridden hold window")
        void prodProfileOverridesHoldWindow() {
            ReservationConfig prod = ReservationConfig.load("prod");

            assertThat(prod.holdWindow()).isEqualTo(Duration.ofSeconds(900));
            assertThat(prod.asyncPollTimeout()).isEqualTo(Duration.ofSeconds(2));
        }
    }
}
