package org.acme.immutability;

import java.util.List;
import java.util.Map;

/**
 * A read-only product catalogue built from the JDK's immutable collection factories — the chapter's
 * collections instrument shown on its own.
 *
 * <p>Three distinct factories appear: {@link List#of} for a fixed sequence, {@link Map#ofEntries}
 * with {@link Map#entry} for a fixed lookup table, and {@link List#copyOf} as the defensive-copy
 * primitive that snapshots an incoming collection. Each returns a <em>conventionally immutable</em>
 * structure: any {@code add}/{@code put}/{@code remove} throws {@code UnsupportedOperationException},
 * the factories reject {@code null} elements/keys/values, and iteration order is intentionally
 * randomised across runs. None of these is a wrapper — they are compact, space-efficient data
 * structures, and an immutable collection is automatically thread-safe.
 *
 * <p>The standing caveat the JDK's own documentation states: <em>an immutable collection of objects
 * is not the same as a collection of immutable objects.</em> {@code List.copyOf} freezes the list,
 * not the things in it; it is safe here because {@link LineItem} and {@link Money} are themselves
 * immutable.
 */
public final class Catalog {

    // tag::immutable-collections[]
    /** A fixed sequence — immutable, null-hostile, throws on any structural change. */
    private static final List<String> FEATURED = List.of("sku-1", "sku-2", "sku-3");

    /** A fixed lookup table, built entry-by-entry; equally immutable and null-hostile. */
    private static final Map<String, Money> PRICE_LIST = Map.ofEntries(
        Map.entry("sku-1", new Money(1_999L, "USD")),
        Map.entry("sku-2", new Money(4_950L, "USD")));
    // end::immutable-collections[]

    private final List<LineItem> stock;

    /**
     * Creates a catalogue over a defensively-copied snapshot of the given stock.
     *
     * @param stock the line items to expose; snapshotted with {@link List#copyOf}, so later edits to
     *              the caller's list do not change this catalogue, never {@code null}
     * @throws NullPointerException if {@code stock} or any element is {@code null}
     */
    public Catalog(List<LineItem> stock) {
        this.stock = List.copyOf(stock);            // immutable snapshot, not a live view
    }

    /**
     * Returns the featured stock-keeping units.
     *
     * @return an immutable list of SKUs, never {@code null}
     */
    public List<String> featured() {
        return FEATURED;
    }

    /**
     * Looks up a unit price by SKU.
     *
     * @param sku the stock-keeping unit to price
     * @return the price if the SKU is listed, otherwise {@code null}
     */
    public Money priceOf(String sku) {
        return PRICE_LIST.get(sku);
    }

    /**
     * Returns the catalogue's stock.
     *
     * @return an immutable list of line items, never {@code null}
     */
    public List<LineItem> stock() {
        return stock;                               // already immutable — safe to publish directly
    }
}
