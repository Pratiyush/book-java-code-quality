package org.acme.catalog;

import java.util.Objects;

/**
 * Pure in-process pricing logic: scales a unit price by a quantity, with a per-line quantity cap.
 *
 * <p>This is the subject of the chapter's <em>parameterized</em> test — a finite, known set of input
 * cases (a quantity table) checked by one test body. It is pure logic with no collaborator, which is
 * exactly the kind of code a fast unit test covers and a container would only slow down; the
 * parameterized test widens how many known cases run, not which unimagined kinds.
 */
public final class PriceList {

    /** The largest quantity a single order line may carry. */
    public static final int MAX_LINE_QUANTITY = 1000;

    private PriceList() {
    }

    /**
     * Returns the line total for {@code quantity} units at {@code unitPrice}.
     *
     * @param unitPrice the price of one unit, never {@code null}
     * @param quantity  the number of units, in {@code [1, 1000]}
     * @return the line total, never {@code null}
     * @throws NullPointerException     if {@code unitPrice} is {@code null}
     * @throws IllegalArgumentException if {@code quantity} is outside {@code [1, 1000]}
     */
    public static Money lineTotal(Money unitPrice, int quantity) {
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (quantity < 1 || quantity > MAX_LINE_QUANTITY) {
            throw new IllegalArgumentException("quantity out of range [1, 1000]: " + quantity);
        }
        return new Money(unitPrice.minorUnits() * quantity, unitPrice.currency());
    }
}
