package org.acme.testdiscipline;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test smell: Assertion Roulette, and the clarity the chapter's catalogue prescribes against it.
 *
 * <p>Assertion Roulette is several bare assertions in one test, so a failure does not say <em>which</em>
 * expectation broke. The chapter's fix is a message per assertion, or one logical assertion gathered
 * with {@code assertAll} so every check runs and all failures are reported together rather than the run
 * stopping at the first. This is the test code's own quality — review-found, not tool-gated, which is the
 * honest boundary the chapter draws.
 */
class AssertionClarityTest {

    private static final Instant NOON = Instant.parse("2026-06-20T12:00:00Z");

    private final ReservationService service = new ReservationService(Clock.fixed(NOON, ZoneOffset.UTC));

    @Test
    @DisplayName("a confirmed reservation reports every component, each check diagnosable")
    void reportsEveryComponentWithoutAssertionRoulette() {
        Reservation r = service.confirm("res-1", Set.of("A1", "B2"));

        // tag::assert-all[]
        // assertAll runs every check and reports all failures, each with its own message — the
        // opposite of Assertion Roulette, where a bare failing line leaves you guessing which broke.
        assertAll("confirmed reservation",
            () -> assertEquals("res-1", r.id(), "id"),
            () -> assertEquals(2, r.seats().size(), "seat count"),
            () -> assertEquals(NOON, r.confirmedAt(), "confirmed-at instant"));
        // end::assert-all[]
    }
}
