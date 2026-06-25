package org.acme.orders;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The order store. It owns the one place a low-level storage failure is turned into an
 * abstraction-appropriate domain exception.
 *
 * <p>The lookup demonstrates exception translation (Item 73): a {@link StoreAccessException} from the
 * persistence layer is caught and rethrown as an {@link OrderUnavailableException}, with the original passed
 * as the cause so the stack trace stays intact (Item 73 / PMD {@code PreserveStackTrace}, Sonar
 * {@code java:S1166}). The caller of the order layer never sees the storage technology, yet a diagnostician
 * still has the root cause one {@code getCause()} away. A genuine absence (no such order) is returned as an
 * empty {@link Optional} rather than thrown, keeping "not found" off the exception channel (Item 69).
 */
public final class OrderRepository {

    private final ConcurrentMap<String, Money> totals = new ConcurrentHashMap<>();
    private volatile boolean available = true;

    /**
     * Stores the total for an order id.
     *
     * @param orderId the order identifier, never {@code null}
     * @param total   the order total, never {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
    public void save(String orderId, Money total) {
        totals.put(Objects.requireNonNull(orderId, "orderId"), Objects.requireNonNull(total, "total"));
    }

    /**
     * Finds the total for an order, translating any low-level storage failure into a domain exception.
     *
     * @param orderId the order identifier, never {@code null}
     * @return the total if the order exists, otherwise an empty {@code Optional} — never {@code null}
     * @throws NullPointerException       if {@code orderId} is {@code null}
     * @throws OrderUnavailableException  if the store could not be read (a recoverable, transient failure)
     */
    public Optional<Money> findTotal(String orderId) throws OrderUnavailableException {
        Objects.requireNonNull(orderId, "orderId");
        // tag::exception-translation[]
        try {
            return Optional.ofNullable(readFromStore(orderId));
        } catch (StoreAccessException cause) {
            // Translate to the order layer's abstraction; chain the cause so the trace survives (Item 73).
            throw new OrderUnavailableException("order store unavailable for " + orderId, cause);
        }
        // end::exception-translation[]
    }

    /**
     * Simulates the low-level read. A real adapter would issue a query here and could fail with a checked
     * storage exception.
     *
     * @param orderId the order identifier
     * @return the stored total, or {@code null} when the order is absent
     * @throws StoreAccessException if the store is currently unavailable
     */
    private Money readFromStore(String orderId) throws StoreAccessException {
        if (!available) {
            throw new StoreAccessException("connection refused");
        }
        return totals.get(orderId);
    }

    /**
     * Flips the store into an unavailable state, so a test can exercise the translation path.
     *
     * @param value {@code true} to make subsequent reads fail with a {@link StoreAccessException}
     */
    public void setAvailable(boolean value) {
        this.available = value;
    }

    /**
     * Health surface: whether the store is currently reachable.
     *
     * @return {@code true} when reads are expected to succeed
     */
    public boolean isAvailable() {
        return available;
    }
}
