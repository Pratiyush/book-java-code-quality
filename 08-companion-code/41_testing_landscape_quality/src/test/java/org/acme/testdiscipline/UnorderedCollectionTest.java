package org.acme.testdiscipline;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Determinism fix: an unordered result checked order-independently.
 *
 * <p>The chapter's flakiness matrix names a {@code HashSet}'s iteration order as a root cause and an
 * order-independent assertion ({@code containsExactlyInAnyOrder}) as the fix. The seats are a
 * {@code Set}, so any assertion that pins their order would be testing the JDK's hashing, not the
 * behaviour. The order-independent matcher asserts exactly the membership the behaviour guarantees and
 * stays green regardless of iteration order.
 */
class UnorderedCollectionTest {

    private final ReservationService service =
        new ReservationService(Clock.fixed(Instant.parse("2026-06-20T12:00:00Z"), ZoneOffset.UTC));

    @Test
    @DisplayName("seat membership is asserted without depending on Set iteration order")
    void seatsAreCheckedOrderIndependently() {
        // tag::order-independent[]
        // The caller passes seats in one order; the stored Set may iterate in another.
        Set<String> requested = new LinkedHashSet<>(Set.of("C3", "A1", "B2"));
        Reservation r = service.confirm("res-1", requested);
        // Assert membership, not order — the matcher the chapter's matrix names for unordered results.
        assertThat(r.seats()).containsExactlyInAnyOrder("A1", "B2", "C3");
        // end::order-independent[]
    }

    @Test
    @DisplayName("the stored seat set is an immutable defensive copy")
    void storedSeatsAreImmutable() {
        Set<String> requested = new LinkedHashSet<>(Set.of("A1", "B2"));
        Reservation r = service.confirm("res-2", requested);

        requested.add("LATE");

        assertThat(r.seats()).containsExactlyInAnyOrder("A1", "B2");
    }
}
