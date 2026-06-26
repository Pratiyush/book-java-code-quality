package org.acme.orders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The right double for the job, under one JUnit Jupiter harness.
 *
 * <p>{@code PriceCatalog} is a query, so it is stubbed; {@code PaymentGateway} is a command, so the
 * interaction with it is verified; {@code Money} is a value object, so it is used real. The class
 * runs under {@code MockitoExtension}, whose default strictness is {@code STRICT_STUBS} — an unused
 * stub would fail the build, which is the chapter's built-in over-mocking guard.
 */
// tag::mockito-setup[]
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private PriceCatalog catalog;   // a query port → stubbed
    @Mock private PaymentGateway gateway; // a command port → verified

    @InjectMocks private OrderService service; // the unit under test, wired with the mocks
    // end::mockito-setup[]

    private static final Money WIDGET_PRICE = new Money(2_500L, "USD");

    @Test
    @DisplayName("places an order: prices each line, charges the total, returns a receipt")
    void placesAnOrderOnTheHappyPath() {
        // tag::aaa-structure[]
        // Arrange
        when(catalog.priceOf("widget")).thenReturn(Optional.of(WIDGET_PRICE));
        // Act
        Receipt receipt = service.place("order-1", List.of(new LineItem("widget", 2)));
        // Assert
        assertThat(receipt.total()).isEqualTo(new Money(5_000L, "USD"));
        // end::aaa-structure[]
    }

    @Test
    @DisplayName("a query collaborator reads cleanest as a stub (state verification)")
    void stubsTheQueryCollaborator() {
        // tag::stub-a-query[]
        // PriceCatalog answers a question, so stub it and assert the resulting state.
        when(catalog.priceOf("widget")).thenReturn(Optional.of(WIDGET_PRICE));

        Receipt receipt = service.place("order-1", List.of(new LineItem("widget", 3)));

        assertThat(receipt.total().minorUnits()).isEqualTo(7_500L);
        // end::stub-a-query[]
    }

    @Test
    @DisplayName("a command collaborator's interaction is verified (behaviour verification)")
    void verifiesTheCommandCollaborator() {
        when(catalog.priceOf("widget")).thenReturn(Optional.of(WIDGET_PRICE));

        service.place("order-9", List.of(new LineItem("widget", 1)));

        // tag::verify-a-command[]
        // PaymentGateway's side effect is the point, so verify the interaction happened.
        verify(gateway).charge(eq("order-9"), any(Money.class));
        // end::verify-a-command[]
    }

    @Test
    @DisplayName("a value object is used real, never mocked")
    void usesValueObjectsReal() {
        // tag::value-not-mocked[]
        // Money is a value object: construct it directly. Mocking it would prove nothing.
        Money price = new Money(1_000L, "USD");
        when(catalog.priceOf("widget")).thenReturn(Optional.of(price));

        Receipt receipt = service.place("order-1", List.of(new LineItem("widget", 4)));

        assertThat(receipt.total()).isEqualTo(new Money(4_000L, "USD"));
        // end::value-not-mocked[]
    }

    @Nested
    @DisplayName("the explicit failure path")
    class FailurePath {

        @Test
        @DisplayName("an empty order is rejected fast, before the gateway is touched")
        void rejectsAnEmptyOrderBeforeCharging() {
            assertThatExceptionOfType(OrderRejectedException.class)
                .isThrownBy(() -> service.place("order-1", List.of()))
                .matches(ex -> "empty-order".equals(ex.code()));

            verifyNoInteractions(gateway);
            assertThat(service.rejectedCount()).isEqualTo(1L);
        }

        @Test
        @DisplayName("an unknown SKU is rejected with a typed, branchable code")
        void rejectsAnUnknownSku() {
            when(catalog.priceOf("ghost")).thenReturn(Optional.empty());

            assertThatExceptionOfType(OrderRejectedException.class)
                .isThrownBy(() -> service.place("order-1", List.of(new LineItem("ghost", 1))))
                .matches(ex -> "unknown-sku".equals(ex.code()));

            verify(gateway, never()).charge(any(), any());
        }

        @Test
        @DisplayName("a declined charge surfaces the gateway's typed exception")
        void surfacesADeclinedCharge() {
            when(catalog.priceOf("widget")).thenReturn(Optional.of(WIDGET_PRICE));
            doThrow(new PaymentDeclinedException("card expired"))
                .when(gateway).charge(eq("order-7"), any(Money.class));

            assertThatExceptionOfType(PaymentDeclinedException.class)
                .isThrownBy(() -> service.place("order-7", List.of(new LineItem("widget", 1))));

            assertThat(service.placedCount()).isZero();
        }
    }

    @Test
    @DisplayName("the readiness probe reports ready when both ports are wired")
    void readinessProbeReportsReady() {
        assertThat(service.isReady()).isTrue();
    }
}
