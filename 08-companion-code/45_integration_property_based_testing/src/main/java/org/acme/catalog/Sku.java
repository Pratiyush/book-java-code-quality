package org.acme.catalog;

import java.util.Objects;

/**
 * A stock-keeping unit: a catalog item's stable identifier, made of a department code and a numeric
 * item number (for example {@code GRO-0042}).
 *
 * <p>This is a <em>value object</em>, and it carries the chapter's property-based example: it has a
 * canonical text form ({@link #format()}) and a parser ({@link #parse(String)}), and the two compose
 * into a round-trip invariant — {@code parse(format(sku)).equals(sku)} for every valid {@code Sku}.
 * That invariant is the kind of property a generated-input test asserts: rather than enumerating a few
 * example codes, the property test describes the domain of valid SKUs and checks the round-trip across
 * many generated values, including the boundary item numbers a hand-picked list would miss.
 *
 * @param department the department code, two-or-more upper-case letters, never {@code null}
 * @param itemNumber the item number within the department, in {@code [0, 9999]}
 */
public record Sku(String department, int itemNumber) {

    /** The lowest valid item number. */
    public static final int MIN_ITEM_NUMBER = 0;

    /** The highest valid item number (four decimal digits in the canonical form). */
    public static final int MAX_ITEM_NUMBER = 9999;

    private static final char SEPARATOR = '-';
    private static final int ITEM_DIGITS = 4;

    /**
     * Validates the components at construction so an invalid {@code Sku} can never exist.
     *
     * @throws NullPointerException     if {@code department} is {@code null}
     * @throws IllegalArgumentException if {@code department} is not two-or-more upper-case letters, or
     *                                  {@code itemNumber} is outside {@code [0, 9999]}
     */
    public Sku {
        Objects.requireNonNull(department, "department");
        if (!department.matches("[A-Z]{2,}")) {
            throw new IllegalArgumentException("department must be two-or-more upper-case letters: " + department);
        }
        if (itemNumber < MIN_ITEM_NUMBER || itemNumber > MAX_ITEM_NUMBER) {
            throw new IllegalArgumentException("itemNumber out of range [0, 9999]: " + itemNumber);
        }
    }

    /**
     * Renders the canonical text form: the department, a hyphen, and the zero-padded four-digit item
     * number (for example {@code GRO-0042}).
     *
     * @return the canonical text form, never {@code null}
     */
    // tag::sku-roundtrip[]
    public String format() { // canonical form, e.g. GRO-0042
        return department + SEPARATOR + String.format("%0" + ITEM_DIGITS + "d", itemNumber);
    }

    public static Sku parse(String text) {
        int split = separatorIndex(text);
        return new Sku(text.substring(0, split), Integer.parseInt(text.substring(split + 1)));
    }
    // end::sku-roundtrip[]

    private static int separatorIndex(String text) {
        int split = text.indexOf(SEPARATOR);
        if (split < 0) {
            throw new IllegalArgumentException("missing '-' separator: " + text);
        }
        return split;
    }
}
