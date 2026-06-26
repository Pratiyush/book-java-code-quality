package org.acme.toolchain;

import java.util.List;

/**
 * An immutable storefront cart: the small aggregate the assembled toolchain analyses end to end. It
 * holds a defensive copy of its line items so no caller can mutate the cart's internals after
 * construction — the representation-exposure discipline SpotBugs checks for at the bytecode level
 * (Chapter 16), kept true here on the code's own merits rather than by a suppression.
 *
 * <p>Alongside the total it exposes a small observability surface: {@link #size()} is the headline
 * metric a dashboard would trend, and {@link #isReady()} is a readiness probe over the cart's state.
 */
public final class Cart {

    private final List<LineItem> lines;

    /**
     * Creates a cart over a snapshot of the supplied lines.
     *
     * @param lines the line items to hold; copied defensively, so later edits to the argument do not
     *              change this cart
     */
    public Cart(List<LineItem> lines) {
        if (lines == null) {
            throw new IllegalArgumentException("lines must not be null");
        }
        // List.copyOf rejects null elements and yields an unmodifiable snapshot.
        this.lines = List.copyOf(lines);
    }

    /**
     * The cart total in whole cents: the sum of every line's subtotal.
     *
     * @return the total in cents, always zero or greater
     */
    public long totalCents() {
        return lines.stream().mapToLong(LineItem::subtotalCents).sum();
    }

    /**
     * The number of lines in the cart — the headline metric for this aggregate.
     *
     * @return the line count, zero or greater
     */
    public int size() {
        return lines.size();
    }

    /**
     * A readiness probe over the cart: a cart with no lines is empty rather than ready to check out.
     * A gate or health view reads this to distinguish "wired but empty" from "ready", instead of
     * inferring readiness from a total that is also zero for an empty cart.
     *
     * @return {@code true} when the cart holds at least one line
     */
    public boolean isReady() {
        return !lines.isEmpty();
    }
}
