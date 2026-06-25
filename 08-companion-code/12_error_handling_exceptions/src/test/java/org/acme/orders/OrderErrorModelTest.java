package org.acme.orders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's failure-path mechanisms as runtime contracts: the checked-vs-unchecked split, the
 * cause-preserving exception translation, the typed {@link Result} model, the reverse-order/suppressed
 * try-with-resources semantics, the {@link java.lang.ref.Cleaner} backstop, the fail-fast guards, the
 * declarative boundary constraints, and the boundary handler's defined responses. Each test asserts the
 * behaviour the prose describes; the {@code -Pquality} build asserts the same kinds of mistake are caught by
 * the analyzers.
 */
class OrderErrorModelTest {

    private OrderRepository repository;
    private OrderService service;
    private OrderBoundary boundary;

    @BeforeEach
    void seed() {
        repository = new OrderRepository();
        repository.save("o-1", new Money(2_500L, "USD"));
        service = new OrderService(repository, Map.of("sku-1", 1_000L, "sku-2", 250L), 100_000L);
        boundary = new OrderBoundary(service);
    }

    // --- the checked-vs-unchecked decision (Item 70) ---

    @Test
    void brokenPreconditionThrowsUncheckedFailFast() {
        assertThatNullPointerException()
            .isThrownBy(() -> service.readTotal(null, 1))
            .withMessageContaining("orderId");
        assertThatThrownBy(() -> service.readTotal("o-1", 0))
            .isInstanceOf(IllegalArgumentException.class);
        assertThat(service.rejectedByGuardCount()).isEqualTo(1L);
    }

    @Test
    void recoverableFailureIsACheckedExceptionWithCausePreserved() {
        repository.setAvailable(false);

        OrderUnavailableException ex = catchThrowableOfType(
            OrderUnavailableException.class, () -> service.readTotal("o-1", 1));

        assertThat(ex).isNotNull();
        assertThat(ex.getCause())
            .as("the low-level cause must be chained, not discarded (Item 73)")
            .isInstanceOf(StoreAccessException.class);
    }

    @Test
    void readReturnsTheStoredTotalOnTheHappyPath() throws OrderUnavailableException {
        assertThat(service.readTotal("o-1", 1)).get()
            .extracting(Money::minorUnits).isEqualTo(2_500L);
        assertThat(service.readTotal("absent", 1)).isEmpty();
    }

    // --- the typed Result alternative (sealed + exhaustive switch) ---

    @Test
    void pricingReturnsAnOkResultForAValidCart() {
        Result<Money, OrderProblem> result =
            service.price(new OrderRequest("c-1", List.of(new OrderRequest.Line("sku-1", 2))));

        assertThat(result.isOk()).isTrue();
        long total = result.fold(Money::minorUnits, problem -> -1L);
        assertThat(total).isEqualTo(2_000L);
    }

    @Test
    void pricingReturnsTypedProblemsForEachRejectionReason() {
        Result<Money, OrderProblem> empty =
            service.price(new OrderRequest("c-1", List.of()));
        assertThat(empty.fold(money -> "ok", OrderProblem::describe)).isEqualTo("cart is empty");

        Result<Money, OrderProblem> unknown =
            service.price(new OrderRequest("c-1", List.of(new OrderRequest.Line("ghost", 1))));
        assertThat(unknown.fold(money -> "ok", OrderProblem::describe)).contains("unknown item: ghost");
    }

    @Test
    void pricingFlagsAnOrderOverTheCeiling() {
        OrderService tightCeiling =
            new OrderService(repository, Map.of("sku-1", 1_000L), 500L);

        Result<Money, OrderProblem> result =
            tightCeiling.price(new OrderRequest("c-1", List.of(new OrderRequest.Line("sku-1", 1))));

        assertThat(result.isOk()).isFalse();
        assertThat(result.fold(money -> "ok", OrderProblem::describe)).contains("exceeds ceiling");
    }

    // --- try-with-resources: reverse-order close and suppressed (not masked) ---

    @Test
    void resourcesCloseInReverseOrderOfOpening() {
        List<String> closeLog = ReceiptWriter.newCloseLog();

        ReceiptWriter.write(closeLog, "line");

        assertThat(closeLog).containsExactly("second", "first");
    }

    @Test
    void closeExceptionIsSuppressedNotMasked() {
        List<String> closeLog = ReceiptWriter.newCloseLog();

        List<Throwable> suppressed = ReceiptWriter.writeWithFailingBodyAndClose(closeLog);

        assertThat(suppressed)
            .as("the body failure propagates; the close failure is attached as suppressed")
            .singleElement()
            .isInstanceOf(IllegalStateException.class)
            .extracting(Throwable::getMessage).asString().contains("close failed");
    }

    // --- Cleaner backstop (Item 8): close() releases exactly once ---

    @Test
    void closeReleasesTheResourceExactlyOnce() {
        AtomicInteger live = new AtomicInteger();

        NativeCounter handle = new NativeCounter(live);
        assertThat(live.get()).isEqualTo(1);

        handle.close();
        handle.close();   // idempotent: the second close is a no-op

        assertThat(live.get()).isZero();
    }

    // --- fail-fast guards (Item 49) ---

    @Test
    void moneyRejectsAnInvalidAmountAtConstruction() {
        assertThatThrownBy(() -> new Money(-1L, "USD"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("-1");
        assertThatNullPointerException()
            .isThrownBy(() -> new Money(1L, null))
            .withMessageContaining("currency");
    }

    // --- declarative boundary constraints (Jakarta Validation 3.1) ---

    @Test
    void requestComponentsCarryTheDeclaredConstraintAnnotations() throws NoSuchMethodException {
        // A Validator reads constraints from the canonical constructor's parameters (the cascade entry
        // point for a record), so that is where this test confirms the declared metadata landed.
        Constructor<OrderRequest> canonical =
            OrderRequest.class.getDeclaredConstructor(String.class, List.class);
        Annotation[][] byParameter = canonical.getParameterAnnotations();

        assertThat(typesOf(byParameter[0]))
            .as("customerId should carry @NotNull").contains(NotNull.class);
        assertThat(typesOf(byParameter[1]))
            .as("lines should carry @NotEmpty and @Valid to cascade into each Line")
            .contains(NotEmpty.class, Valid.class);
    }

    private static List<Class<? extends Annotation>> typesOf(Annotation[] annotations) {
        return java.util.Arrays.stream(annotations).map(Annotation::annotationType).toList();
    }

    // --- the boundary handler: a defined response for every outcome ---

    @Test
    void boundaryMapsEveryOutcomeToADefinedResponse() {
        assertThat(boundary.handleReadTotal("o-1", 1).status()).isEqualTo(200);
        assertThat(boundary.handleReadTotal("absent", 1).status()).isEqualTo(404);

        repository.setAvailable(false);
        assertThat(boundary.handleReadTotal("o-1", 1).status())
            .as("a recoverable store failure maps to a retryable status, not an escape")
            .isEqualTo(503);
    }
}
