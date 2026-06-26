package org.acme.toolchain;

/**
 * A single priced line in a storefront cart, the seed domain object the book's companion reference
 * project grows from. The type is deliberately small: its job in this chapter is to give every layer
 * of the assembled toolchain something real to read — source for Checkstyle, bytecode for SpotBugs,
 * branches for coverage, and a compiler that is held to {@code -Xlint:all -Werror}.
 *
 * <p>Money is held as integer cents, never {@code double}, so rounding is exact and SpotBugs has no
 * floating-point currency to flag. The record is immutable and validates in its compact constructor,
 * so an invalid line cannot exist (Chapter 8).
 *
 * @param sku        the catalogue identifier of the item
 * @param unitCents  the unit price in whole cents, zero or greater
 * @param quantity   the number ordered, one or greater
 */
public record LineItem(String sku, long unitCents, int quantity) {

    /**
     * Validates every component so an ill-formed line item is unrepresentable. This is the module's
     * explicit failure path in code: invalid input fails fast and loudly at construction with a
     * precise message, rather than producing a silently wrong total downstream.
     */
    public LineItem {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku must be a non-blank identifier");
        }
        if (unitCents < 0) {
            throw new IllegalArgumentException("unitCents must be zero or greater, was " + unitCents);
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be one or greater, was " + quantity);
        }
    }

    /**
     * The extended price of this line in whole cents: unit price times quantity.
     *
     * @return the line total in cents, always zero or greater
     */
    public long subtotalCents() {
        return unitCents * quantity;
    }
}
