package org.acme.smells;

import java.util.List;
import java.util.Objects;

/**
 * An order that encapsulates its line items — the refactored counterpart of {@link OrderLeaky}.
 *
 * <p>A record makes the {@code lines} <em>reference</em> {@code final}, but it does not copy or freeze
 * the list the reference points at. This record closes that gap with the defensive copy of Effective
 * Java Item 50: the <strong>compact constructor</strong> replaces the incoming list with an immutable
 * {@link List#copyOf snapshot}, so a caller who keeps the original list cannot mutate the order through
 * it, and the accessor hands that already-unmodifiable snapshot straight back. That single line is the
 * named refactoring (<em>Encapsulate Collection</em> / defensive copy) for the representation-exposure
 * smell SpotBugs reports as {@code EI_EXPOSE_REP} / {@code EI_EXPOSE_REP2} on {@link OrderLeaky}.
 *
 * @param id       the order identifier, never {@code null}
 * @param customer the customer placing the order, never {@code null}
 * @param lines    the order's line items; copied defensively, so later edits to the caller's list have
 *                 no effect on the order
 */
public record Order(String id, Customer customer, List<LineItem> lines) {

    /**
     * Validates and defensively snapshots the components so the order's state is fixed for its whole
     * lifetime.
     *
     * @throws NullPointerException if {@code id}, {@code customer}, {@code lines}, or any element is
     *                              {@code null}
     */
    // tag::refactor-defensive-copy[]
    public Order {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(customer, "customer");
        lines = List.copyOf(lines);   // immutable snapshot: the caller's list cannot leak in or out
    }
    // end::refactor-defensive-copy[]
}
