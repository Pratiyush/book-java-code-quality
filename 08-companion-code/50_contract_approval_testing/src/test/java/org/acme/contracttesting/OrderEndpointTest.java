package org.acme.contracttesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Exercising the endpoint's behaviour — the other job on the same boundary.
 *
 * <p>Where the contract test asks "do the two sides agree?", this asks "does the endpoint respond
 * correctly?". REST-assured is the library for this against a RUNNING service, with a fluent
 * {@code given().when().get(...).then().statusCode(200).body("id", equalTo(...))} DSL (see the README).
 * Standing up a real endpoint needs Testcontainers or a framework instance, so here the provider is
 * exercised in-JVM with the same shape — request, response, then assertions on status and body — to
 * keep the module green without a running server.
 */
class OrderEndpointTest {

    private final OrderProvider provider =
        new OrderProvider(Map.of("42", new Order("42", "CONFIRMED", 5_000L)), OrderProvider.DEFAULT_ID_FIELD);

    @Test
    @DisplayName("GET an existing order: status OK, body carries the expected id")
    void getExistingOrderRespondsOk() {
        // tag::endpoint-behaviour[]
        // Shape mirrors REST-assured's given/when/then against a running service (README); run in-JVM.
        String body = provider.renderOrder("42");        // when: GET /orders/42

        assertThat(statusFor("42")).isEqualTo(200);      // then: status 200
        assertThat(body).contains("\"id\":\"42\"");      //       body has id == 42
        // end::endpoint-behaviour[]
    }

    @Test
    @DisplayName("GET an unknown order: the endpoint reports not-found, not a wrong body")
    void getUnknownOrderReportsNotFound() {
        assertThat(statusFor("999")).isEqualTo(404);
        assertThatExceptionOfType(OrderNotFoundException.class)
            .isThrownBy(() -> provider.renderOrder("999"))
            .matches(ex -> OrderNotFoundException.CODE.equals(ex.code()));
    }

    /** Maps the provider's outcome to the HTTP status a thin web layer would return. */
    private int statusFor(String id) {
        return provider.findById(id).isPresent() ? 200 : 404;
    }
}
