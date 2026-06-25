package org.acme.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

/**
 * A submitted order, carrying its boundary constraints declaratively (Jakarta Validation 3.1).
 *
 * <p>The constraints state the boundary contract on the type itself: a {@code customerId} that is present
 * ({@link NotNull}), at least one line ({@link NotEmpty}), and {@link Valid} to cascade validation into each
 * nested {@link Line}. A {@code Validator} (programmatic, or supplied by a Jakarta REST / Persistence
 * container at a request boundary) reads this metadata and reports each breach as a structured
 * {@code ConstraintViolation} carrying the message, the property path, and the invalid value — a reusable
 * rejection rather than a hand-written {@code if}.
 *
 * <p>Guard clauses and declarative constraints are complementary, not rivals: the compact constructors here
 * still reject {@code null} structurally (so an {@code OrderRequest} is never internally malformed), while
 * the annotations express the request-level rules a boundary enforces and reports uniformly. This module
 * declares the constraints against the pinned Jakarta Validation API; evaluating them needs a constraint
 * engine, which is the documented boundary in the module's build (SOURCE-PIN pins the API, not an engine),
 * so the test asserts the constraint metadata is present rather than running an off-pin engine.
 *
 * @param customerId the customer placing the order, never {@code null}
 * @param lines      the order lines, never {@code null} and never empty
 */
// tag::constraints[]
public record OrderRequest(
        @NotNull String customerId,
        @NotEmpty @Valid List<Line> lines) {

    /** A single requested line: an item and how many of it. */
    public record Line(@NotNull String itemId, @Positive int quantity) { }
    // end::constraints[]

    /**
     * Copies the lines defensively and rejects a {@code null} structurally, so the value is always
     * internally consistent regardless of whether a {@code Validator} has run.
     *
     * @throws NullPointerException if {@code customerId} or {@code lines} (or any line) is {@code null}
     */
    public OrderRequest {
        Objects.requireNonNull(customerId, "customerId");
        lines = List.copyOf(Objects.requireNonNull(lines, "lines"));
    }
}
