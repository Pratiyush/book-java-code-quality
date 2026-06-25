package org.acme.immutability;

import java.util.List;
import java.util.Objects;

/**
 * An order, sealed against the leak that {@link OrderLeaky} demonstrates — the correct counterpart of
 * the hook's bug.
 *
 * <p>Records make the component <em>reference</em> {@code final}, but they do not copy or freeze a
 * mutable component. This record closes that gap two ways at once, which is Effective Java Item 17's
 * rule 5 (exclusive access to mutable components) realised: the <strong>compact constructor</strong>
 * replaces the incoming list with an immutable {@link List#copyOf snapshot}, so a caller who keeps
 * the original list cannot mutate the order through it; and a <strong>copying accessor</strong> hands
 * back a snapshot, so a caller cannot mutate the order through what {@code items()} returns. Because
 * {@code copyOf} already returns an unmodifiable list, the accessor here returns the stored snapshot
 * directly — it is already safe to publish.
 *
 * @param id    the order identifier, never {@code null}
 * @param items the order's line items; copied defensively, so later edits to the caller's list have
 *              no effect on the order
 */
public record Order(String id, List<LineItem> items) {

    /**
     * Validates and defensively snapshots the components so the order's state is fixed for its whole
     * lifetime.
     *
     * @throws NullPointerException if {@code id}, {@code items}, or any element is {@code null}
     */
    // tag::sealed-record[]
    public Order {
        Objects.requireNonNull(id, "id");
        items = List.copyOf(items);             // immutable snapshot: the caller's list cannot leak in
    }

    @Override
    public List<LineItem> items() {
        return items;                           // already an unmodifiable copy — safe to hand back
    }
    // end::sealed-record[]

    /**
     * Returns the order's total across every line, in the line currency.
     *
     * <p>Each step allocates a fresh {@link Money} rather than mutating one — the object-per-value
     * cost Item 17 names. For an order it is immaterial; on an allocation-sensitive hot path a mutable
     * accumulator would be the deliberate exception.
     *
     * @param currency the currency to total in, never {@code null}
     * @return the summed amount, never {@code null}
     */
    public Money total(String currency) {
        Money sum = new Money(0L, currency);
        for (LineItem item : items) {
            sum = sum.plus(item.price().minorUnits());
        }
        return sum;
    }
}
