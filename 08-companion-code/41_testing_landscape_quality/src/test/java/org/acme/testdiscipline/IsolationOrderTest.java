package org.acme.testdiscipline;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * The determinism foundation: per-method isolation, with a randomised method order to hunt coupling.
 *
 * <p>JUnit Jupiter's default lifecycle creates a new instance of the test class before each method, so
 * instance fields cannot leak state from one test into another; a hidden order dependency cannot ride
 * on shared mutable state. The chapter pairs that default with {@code MethodOrderer.Random}: running
 * methods in a different order each build makes any latent coupling fail visibly instead of passing by
 * luck. Both annotations are made explicit here, on a class whose mutable field is reset by the
 * lifecycle rather than by the test author.
 */
// tag::per-method-isolation[]
@TestInstance(TestInstance.Lifecycle.PER_METHOD) // a fresh instance per method: no state leaks
@TestMethodOrder(MethodOrderer.Random.class)     // shuffle the order to surface hidden coupling
class IsolationOrderTest {

    private int callsOnThisInstance; // mutable, yet safe — the lifecycle resets it per method
    // end::per-method-isolation[]

    private final ReservationService service =
        new ReservationService(Clock.fixed(Instant.parse("2026-06-20T12:00:00Z"), ZoneOffset.UTC));

    @Test
    @DisplayName("first method: the per-instance counter starts at zero")
    void counterStartsAtZeroRegardlessOfRunOrder() {
        callsOnThisInstance++;
        service.confirm("res-a", Set.of("A1"));

        assertThat(callsOnThisInstance).isEqualTo(1);
    }

    @Test
    @DisplayName("second method: the per-instance counter ALSO starts at zero, in any order")
    void counterIsNotInheritedFromAnotherMethod() {
        callsOnThisInstance++;
        service.confirm("res-b", Set.of("B2"));

        // If the instance were shared, a random order could make this 2. The default lifecycle
        // guarantees a fresh instance, so it is always 1 — the test is order-independent by design.
        assertThat(callsOnThisInstance).isEqualTo(1);
    }
}
