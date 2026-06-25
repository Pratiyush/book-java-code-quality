package org.acme.storefront.readability;

import java.util.Objects;

/**
 * A single line on a customer order — the conventionally-named, conventionally-formatted result of
 * the chapter's readability pass.
 *
 * <p>Every identifier here carries intent and every identifier follows the typographical convention a
 * tool can check: {@code UpperCamelCase} for the type, {@code lowerCamelCase} for the components and
 * the method, {@code CONSTANT_CASE} for the genuine constant. The constant is {@code static final}
 * <em>and</em> deeply immutable, which is what Google Java Style §5.2.4 actually requires of a
 * constant — not merely the {@code static final} modifier. The case is the part Checkstyle settles;
 * that {@code unitPrice} names a <em>price</em> and not, say, a row index is the part only a person
 * reading the domain can settle.
 *
 * @param sku the stock-keeping unit being ordered, never {@code null} or blank
 * @param quantity the number of units ordered, at least one
 * @param unitPrice the price of a single unit, never {@code null}
 */
public record OrderLine(String sku, int quantity, Money unitPrice) {

    // tag::naming-good[]
    /** The largest quantity a single order line accepts (a genuine, deeply-immutable constant). */
    public static final int MAX_QUANTITY_PER_LINE = 999;

    /** Returns this line's total price: the unit price taken {@code quantity} times. */
    public Money lineTotal() {
        return unitPrice.times(quantity);
    }
    // end::naming-good[]

    /**
     * Validates the components so an out-of-range or malformed line can never exist.
     *
     * @throws NullPointerException if {@code sku} or {@code unitPrice} is {@code null}
     * @throws IllegalArgumentException if {@code sku} is blank, or {@code quantity} is outside
     *     {@code [1, MAX_QUANTITY_PER_LINE]}
     */
    public OrderLine {
        Objects.requireNonNull(sku, "sku");
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (sku.isBlank()) {
            throw new IllegalArgumentException("sku must not be blank");
        }
        if (quantity < 1 || quantity > MAX_QUANTITY_PER_LINE) {
            throw new IllegalArgumentException("quantity out of range [1, " + MAX_QUANTITY_PER_LINE + "]: " + quantity);
        }
    }
}
