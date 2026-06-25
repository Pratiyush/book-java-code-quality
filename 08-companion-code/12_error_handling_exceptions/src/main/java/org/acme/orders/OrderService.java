package org.acme.orders;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Places and reads orders, demonstrating the chapter's central decision in one type: which <em>kind</em> of
 * throwable fits which failure (Item 70).
 *
 * <p>Two failures sit side by side. A broken precondition — a {@code null} argument, a negative quantity —
 * is a <em>programming error</em> and is signalled with an <em>unchecked</em> exception thrown fail-fast at
 * the entry of the method; no caller is expected to catch it, because the right response is to fix the call.
 * A store that is temporarily unreachable is a <em>recoverable</em> condition and is signalled with a
 * <em>checked</em> {@link OrderUnavailableException} the caller must acknowledge — retry, fall back, or
 * report. The validation of a submitted cart, whose every rejection reason belongs to a closed set, is
 * returned as a {@link Result} value rather than thrown, because the reasons are ordinary expected outcomes
 * rather than exceptional ones (Item 69).
 */
public final class OrderService {

    private final OrderRepository repository;
    private final Map<String, Long> catalogue;
    private final long perOrderCeiling;

    /** Observability: orders rejected by a precondition guard since startup (illustrative, Chapter 45). */
    private final AtomicLong rejectedByGuard = new AtomicLong();

    /**
     * Creates a service over the given store and catalogue.
     *
     * @param repository      the order store, never {@code null}
     * @param catalogue       item id to unit price (minor units), never {@code null}
     * @param perOrderCeiling the largest total a single order may reach, in minor units, never negative
     * @throws NullPointerException     if {@code repository} or {@code catalogue} is {@code null}
     * @throws IllegalArgumentException if {@code perOrderCeiling} is negative
     */
    public OrderService(OrderRepository repository, Map<String, Long> catalogue, long perOrderCeiling) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.catalogue = Map.copyOf(Objects.requireNonNull(catalogue, "catalogue"));
        if (perOrderCeiling < 0) {
            throw new IllegalArgumentException("perOrderCeiling must not be negative: " + perOrderCeiling);
        }
        this.perOrderCeiling = perOrderCeiling;
    }

    /**
     * Reads the total of a placed order.
     *
     * @param orderId the order to read, never {@code null}
     * @param quantity the line quantity the caller is reconciling, must be positive
     * @return the order total if the order exists, otherwise an empty {@code Optional}
     * @throws NullPointerException      if {@code orderId} is {@code null} (a programming error)
     * @throws IllegalArgumentException  if {@code quantity} is not positive (a programming error)
     * @throws OrderUnavailableException if the store is temporarily unreachable (recoverable)
     */
    // tag::checked-vs-unchecked[]
    public Optional<Money> readTotal(String orderId, int quantity) throws OrderUnavailableException {
        Objects.requireNonNull(orderId, "orderId");      // programming error -> unchecked, fail-fast
        if (quantity <= 0) {
            rejectedByGuard.incrementAndGet();
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
        return repository.findTotal(orderId);            // recoverable failure -> checked, declared
    }
    // end::checked-vs-unchecked[]

    /**
     * Validates and prices a submitted cart, returning a closed set of outcomes as a value.
     *
     * @param request the submitted cart, never {@code null}
     * @return an {@link Result.Ok} carrying the priced total, or an {@link Result.Err} carrying the reason
     * @throws NullPointerException if {@code request} is {@code null}
     */
    public Result<Money, OrderProblem> price(OrderRequest request) {
        Objects.requireNonNull(request, "request");
        if (request.lines().isEmpty()) {
            return new Result.Err<>(new OrderProblem.EmptyCart());
        }
        long total = 0;
        for (OrderRequest.Line line : request.lines()) {
            Long unitPrice = catalogue.get(line.itemId());
            if (unitPrice == null) {
                return new Result.Err<>(new OrderProblem.UnknownItem(line.itemId()));
            }
            total += unitPrice * line.quantity();
        }
        if (total > perOrderCeiling) {
            return new Result.Err<>(new OrderProblem.AmountTooLarge(total, perOrderCeiling));
        }
        return new Result.Ok<>(new Money(total, "USD"));
    }

    /**
     * Observability surface: the running count of orders turned away by a precondition guard.
     *
     * @return the number of guard-rejected calls since startup, never negative
     */
    public long rejectedByGuardCount() {
        return rejectedByGuard.get();
    }
}
