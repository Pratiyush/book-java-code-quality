package org.acme.orders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The over-mock anti-pattern, kept green so the chapter can critique it.
 *
 * <p>This test passes today, but it is the liability from the chapter's hook: it pins the exact
 * internal call order of {@code OrderService}, which is behaviour verification taken too far. The
 * order in which the catalog is read relative to the gateway charge is an implementation detail, not
 * observable behaviour; a refactor that reorders those calls would break this test while changing
 * nothing a caller can see. The happy-path test in {@code OrderServiceTest} asserts the resulting
 * total instead and survives that refactor. The contrast is the chapter's point — both run green, but
 * only one is an asset.
 *
 * <p>The class also documents Mockito's built-in guard against the other over-mock smell, a dead
 * stub: see {@link #wouldFailWithADeadStub()}.
 */
@ExtendWith(MockitoExtension.class)
class OverMockedOrderServiceTest {

    @Mock private PriceCatalog catalog;
    @Mock private PaymentGateway gateway;

    @InjectMocks private OrderService service;

    @Test
    @DisplayName("over-specified: pins the internal call order (brittle, but green)")
    void overSpecifiesTheCallOrder() {
        when(catalog.priceOf("widget")).thenReturn(Optional.of(new Money(2_500L, "USD")));

        service.place("order-1", List.of(new LineItem("widget", 1)));

        // tag::over-mock-smell[]
        // Brittle: this asserts HOW the unit collaborates, not WHAT it produced. A refactor that
        // reorders these internal calls breaks the test though behaviour is unchanged.
        InOrder ordered = inOrder(catalog, gateway);
        ordered.verify(catalog).priceOf("widget");
        ordered.verify(gateway).charge(eq("order-1"), any(Money.class));
        // end::over-mock-smell[]
    }

    /**
     * Demonstrates, in prose rather than as a live failure, Mockito's guard against a dead stub.
     *
     * <p>Adding a stub the production code never calls, for example
     * {@code when(catalog.priceOf("never-read")).thenReturn(Optional.empty());} to a test, makes
     * {@code STRICT_STUBS} (the {@code MockitoExtension} default) fail that test with an
     * {@code UnnecessaryStubbingException} that names the unused stub. The module keeps the dead stub
     * out of the running test so the build stays green; the failure it would cause is the chapter's
     * built-in over-mocking guard, and removing the dead stub is the fix.
     */
    @Test
    @DisplayName("a dead stub would fail under STRICT_STUBS (documented, not triggered)")
    void wouldFailWithADeadStub() {
        when(catalog.priceOf("widget")).thenReturn(Optional.of(new Money(1_000L, "USD")));

        Receipt receipt = service.place("order-2", List.of(new LineItem("widget", 1)));

        org.assertj.core.api.Assertions.assertThat(receipt.total().minorUnits()).isEqualTo(1_000L);
    }
}
