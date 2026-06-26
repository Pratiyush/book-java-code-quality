package org.acme.contracttesting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The consumer-driven contract, both halves, on one boundary.
 *
 * <p>The consumer declares what it reads as an {@link OrderContract}; the provider verification replays
 * that contract against a real provider response. The chapter's load-bearing point is the failure path:
 * renaming the provider's {@code id} field fails the provider verification (a consumer-breaking change
 * caught) while the provider's own shape test still passes — a contract sees what a one-sided test
 * cannot.
 */
class OrderContractTest {

    private static final Order ORDER_42 = new Order("42", "CONFIRMED", 5_000L);

    // tag::consumer-contract[]
    // The consumer drives the contract: it declares exactly the fields it reads.
    private final OrderContract contract = new OrderContract(Set.of("id", "status"));
    // end::consumer-contract[]

    @Test
    @DisplayName("consumer side: the recorded expectation is itself well-formed")
    void consumerExpectationIsWellFormed() {
        OrderClient client = new OrderClient();
        String agreedResponse = "{\"id\":\"42\",\"status\":\"CONFIRMED\",\"total\":5000}";

        contract.verify(agreedResponse);                       // the consumer's expectation holds...
        assertThat(client.readOrderId(agreedResponse)).isEqualTo("42"); // ...and the client can read it
    }

    @Test
    @DisplayName("provider side: a real provider response satisfies the contract")
    void providerHonoursTheContract() {
        OrderProvider provider = new OrderProvider(Map.of("42", ORDER_42), OrderProvider.DEFAULT_ID_FIELD);

        // tag::provider-verify[]
        // Replay the consumer's contract against what the real provider renders.
        String realResponse = provider.renderOrder("42");
        assertThatNoException().isThrownBy(() -> contract.verify(realResponse));
        // end::provider-verify[]
    }

    @Nested
    @DisplayName("the failure path: a consumer-breaking rename")
    class FieldRenameBreaksTheConsumer {

        /** The same provider, but rendering the identifier under a renamed field. */
        private final OrderProvider renamed = new OrderProvider(Map.of("42", ORDER_42), "orderId");

        @Test
        @DisplayName("provider verification CATCHES the rename id -> orderId")
        void providerVerificationCatchesTheRename() {
            String renamedResponse = renamed.renderOrder("42");

            assertThatExceptionOfType(ContractViolationException.class)
                .isThrownBy(() -> contract.verify(renamedResponse))
                .matches(ex -> "id".equals(ex.missingField()));
        }

        @Test
        @DisplayName("but the provider's own shape test does NOT — both fields are still present")
        void providersOwnShapeTestStillPasses() {
            String renamedResponse = renamed.renderOrder("42");

            // A one-sided shape check the provider owns: "two fields and a total". It passes after the
            // rename, because orderId and status are both there — which is exactly why it cannot see the
            // consumer break. Only the consumer-driven contract, which names `id`, catches it.
            assertThat(renamedResponse).contains("\"orderId\":", "\"status\":", "\"total\":");
        }

        @Test
        @DisplayName("and the live consumer would break at runtime on the renamed response")
        void liveConsumerBreaksOnTheRename() {
            String renamedResponse = renamed.renderOrder("42");

            assertThatExceptionOfType(ContractViolationException.class)
                .isThrownBy(() -> new OrderClient().readOrderId(renamedResponse));
        }
    }
}
