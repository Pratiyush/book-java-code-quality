package org.acme.checkstyle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A read-only catalog over {@link CatalogItem} entries — the surface tying the module together, written to
 * satisfy the house ruleset (explicit imports, no star import, braces on every block, lowerCamelCase
 * members and locals).
 *
 * <p>The {@link #describe(Object)} method uses a pattern variable, the modern-Java naming surface
 * {@code PatternVariableName} governs: the bound name {@code found} is lowerCamelCase and passes, whereas a
 * pattern variable named {@code Found} would be a violation under that rule.
 */
public final class Catalog {

    private final Map<String, CatalogItem> itemsBySku;

    /**
     * Builds a catalog from the given items, indexed by SKU in insertion order.
     *
     * @param items the catalog items, none null and with distinct SKUs
     * @throws NullPointerException     if {@code items} or any element is null
     * @throws IllegalArgumentException if two items share a SKU
     */
    public Catalog(List<CatalogItem> items) {
        Objects.requireNonNull(items, "items");
        Map<String, CatalogItem> index = new LinkedHashMap<>();
        for (CatalogItem item : items) {
            Objects.requireNonNull(item, "item");
            if (index.putIfAbsent(item.sku(), item) != null) {
                throw new IllegalArgumentException("duplicate sku: " + item.sku());
            }
        }
        this.itemsBySku = Map.copyOf(index);
    }

    /**
     * Looks up an item by SKU. A missing SKU is a defined, benign outcome rather than a thrown exception —
     * the module's failure path: callers handle the empty result instead of catching.
     *
     * @param sku the SKU to look up
     * @return the matching item, or {@link Optional#empty()} if none is present
     */
    public Optional<CatalogItem> findBySku(String sku) {
        return Optional.ofNullable(itemsBySku.get(sku));
    }

    /**
     * Describes a value, naming a catalog item specially. The pattern variable {@code found} is the
     * lowerCamelCase shape {@code PatternVariableName} requires.
     *
     * @param value any value, possibly a {@link CatalogItem}
     * @return a short description
     */
    public String describe(Object value) {
        if (value instanceof CatalogItem found) {
            return found.sku() + " (" + found.name() + ")";
        }
        return String.valueOf(value);
    }

    /**
     * Returns the number of items in the catalog.
     *
     * @return the item count
     */
    public int size() {
        return itemsBySku.size();
    }
}
