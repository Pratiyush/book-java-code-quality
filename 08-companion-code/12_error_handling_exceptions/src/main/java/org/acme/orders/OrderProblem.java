package org.acme.orders;

/**
 * The closed set of reasons an order request can be rejected, used as the error type of a
 * {@link Result}. Modelling the failures as a sealed set (rather than as free-form strings or a single
 * exception) lets a caller branch exhaustively on the reason and lets the compiler check that every reason
 * is handled.
 */
public sealed interface OrderProblem
        permits OrderProblem.EmptyCart, OrderProblem.UnknownItem, OrderProblem.AmountTooLarge {

    /** The cart carried no line items. */
    record EmptyCart() implements OrderProblem { }

    /** A line item referenced an item id the catalogue does not contain. */
    record UnknownItem(String itemId) implements OrderProblem { }

    /** The order total exceeded the per-order ceiling. */
    record AmountTooLarge(long total, long ceiling) implements OrderProblem { }

    /**
     * Renders this problem as a stable, human-readable message.
     *
     * @return a description of the problem, never {@code null}
     */
    default String describe() {
        return switch (this) {
            case EmptyCart ignored -> "cart is empty";
            case UnknownItem unknown -> "unknown item: " + unknown.itemId();
            case AmountTooLarge tooLarge ->
                "total " + tooLarge.total() + " exceeds ceiling " + tooLarge.ceiling();
        };
    }
}
