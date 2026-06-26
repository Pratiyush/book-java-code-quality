package org.acme.checkstyle;

import java.util.Objects;

/**
 * A catalog line item — the record whose component names the {@code RecordComponentName} naming rule
 * governs (a modern-Java surface Checkstyle's naming family covers, alongside {@code PatternVariableName}).
 *
 * <p>The component names {@code sku}, {@code name}, and {@code priceCents} are lowerCamelCase, which is the
 * shape {@code RecordComponentName} accepts; a component named, say, {@code Price_Cents} would be a
 * violation. Prices are held as integer cents to keep the value exact, so no rule here depends on types
 * Checkstyle cannot see.
 *
 * @param sku        the stock-keeping unit, a non-blank identifier
 * @param name       the human-readable product name
 * @param priceCents the price in whole cents, never negative
 */
public record CatalogItem(String sku, String name, long priceCents) {

    /**
     * Validates and normalizes the components.
     *
     * @throws NullPointerException     if {@code sku} or {@code name} is null
     * @throws IllegalArgumentException if {@code sku} is blank or {@code priceCents} is negative
     */
    public CatalogItem {
        Objects.requireNonNull(sku, "sku");
        Objects.requireNonNull(name, "name");
        if (sku.isBlank()) {
            throw new IllegalArgumentException("sku must not be blank");
        }
        if (priceCents < 0L) {
            throw new IllegalArgumentException("priceCents must not be negative: " + priceCents);
        }
    }
}
