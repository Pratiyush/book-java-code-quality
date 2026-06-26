package org.acme.performance;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Prices a storefront order, and carries two of the chapter's measure-don't-guess movements as
 * runnable code: a workload whose costly path is not the one intuition points at (so a profiler is the
 * instrument that finds it), and a hot method whose per-call allocation is reduced — but only in the
 * disciplined form the chapter insists on.
 *
 * <p>The class is deliberately honest about its own limits. The method a reader would <em>guess</em>
 * is hot, {@link #formatLineLabel}, is trivial; the cost lives in {@link #tieredDiscountMinor}, which
 * recomputes a running subtotal on every step. Which is which is a claim to be settled by a profiler
 * under realistic load (see the module README), not by reading the code — that is the point.
 */
public final class OrderPricing {

    /** A free-shipping threshold in minor units, used by the (cheap, guessed-at) label path. */
    private static final long FREE_SHIPPING_MINOR = 5_000L;

    /** Largest order this service will price in one call; a bounded, fail-fast input contract. */
    private static final int MAX_LINES = 10_000;

    /** Observability: how many orders this instance has priced since startup (illustrative). */
    private final AtomicLong pricedOrders = new AtomicLong();

    /**
     * Prices an order: sums the lines, applies a tiered discount, and returns a quote. The visible
     * shape suggests the label formatting is the work; profiling a realistic workload shows the
     * discount step dominates — the guessed hotspot and the real one, in one method.
     *
     * @param items the order lines, never {@code null} and within the bounded size contract
     * @return a priced quote, never {@code null}
     * @throws NullPointerException     if {@code items} is {@code null}
     * @throws IllegalArgumentException if the order is empty or exceeds {@link #MAX_LINES}
     */
    public Quote priceOrder(List<LineItem> items) {
        Objects.requireNonNull(items, "items");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("cannot price an empty order");
        }
        if (items.size() > MAX_LINES) {
            throw new IllegalArgumentException(
                "order exceeds the " + MAX_LINES + "-line limit: " + items.size());
        }

        long subtotal = 0L;
        int freeShipLines = 0;
        for (LineItem item : items) {
            subtotal += item.extendedMinor();
            // The cheap path a reader's eye lands on first — it looks like the loop's work, and its
            // result feeds a real field, so it is not dead code. It is simply not where time goes.
            if (formatLineLabel(item).endsWith("(free-ship)")) {
                freeShipLines++;
            }
        }

        long discount = tieredDiscountMinor(items);
        pricedOrders.incrementAndGet();
        return new Quote(subtotal, discount, subtotal - discount, freeShipLines);
    }

    /**
     * The method intuition flags as the hot path: a small per-line label. It is cheap. Profiling
     * exists precisely because the eye is a poor instrument for "where does the time go".
     *
     * @param item the line to label
     * @return a short human label for the line
     */
    private String formatLineLabel(LineItem item) {
        String shipping = item.extendedMinor() >= FREE_SHIPPING_MINOR ? "free-ship" : "standard";
        return item.sku() + " x" + item.quantity() + " (" + shipping + ")";
    }

    /**
     * The method that actually dominates under load: it recomputes a running subtotal from scratch on
     * every line (an O(n^2) shape hiding behind an innocent loop), then derives a tier from it. The
     * code reads as ordinary; only a profiler reveals the cost concentrated here rather than in the
     * label path above. Reducing this is worthwhile <em>because measurement says so</em>, not because
     * the source looks expensive.
     *
     * @param items the order lines
     * @return the discount to apply, in minor units
     */
    private long tieredDiscountMinor(List<LineItem> items) {
        long worstCase = 0L;
        for (int i = 0; i < items.size(); i++) {
            long runningSubtotal = 0L;
            for (int j = 0; j <= i; j++) {
                runningSubtotal += items.get(j).extendedMinor();
            }
            worstCase = Math.max(worstCase, runningSubtotal);
        }
        // A simple tier on the largest running subtotal seen: 10% over the free-shipping threshold.
        return worstCase >= FREE_SHIPPING_MINOR ? worstCase / 10L : 0L;
    }

    /**
     * Builds an order summary the churning way: string concatenation inside a loop allocates a new
     * intermediate {@code String} (and its backing array) on every iteration. This is the per-call
     * allocation a profiler flags on a hot path — kept here as the "before" the disciplined version
     * replaces.
     *
     * @param items the order lines
     * @return a one-line summary of the order's SKUs
     */
    public String summaryLineChurning(List<LineItem> items) {
        Objects.requireNonNull(items, "items");
        String summary = "";
        for (LineItem item : items) {
            summary = summary + item.sku() + ":" + item.quantity() + ";";
        }
        return summary;
    }

    /**
     * Builds the same summary the disciplined way. The reduction is applied only where profiling shows
     * the churn matters (a hot path); on a cold path it would trade readability for nothing, which the
     * chapter names as the dominant failure. The {@link #summaryEquivalenceHolds} test proves the
     * cheaper form returns the identical string — an optimization that changes the answer is a bug,
     * not a speed-up.
     *
     * @param items the order lines
     * @return a one-line summary of the order's SKUs, identical to {@link #summaryLineChurning}
     */
    public String summaryLine(List<LineItem> items) {
        Objects.requireNonNull(items, "items");
        // tag::allocation-reduced[]
        // Reduce churn only where profiling says it matters; size the buffer to avoid resize copies.
        StringBuilder summary = new StringBuilder(items.size() * 16);
        for (LineItem item : items) {
            summary.append(item.sku()).append(':').append(item.quantity()).append(';');
        }
        return summary.toString();
        // end::allocation-reduced[]
    }

    /**
     * Health/observability surface: orders priced since startup. A real service would export this as a
     * metric (Chapter 45); here it is the seam where this topic touches observability.
     *
     * @return the number of orders priced since startup, never negative
     */
    public long pricedOrderCount() {
        return pricedOrders.get();
    }

    /**
     * Proves whether a candidate equivalence holds between the two summary forms for one input — the
     * check the chapter requires before trusting any allocation reduction: same answer, then measure.
     *
     * @param items the order lines to compare on
     * @return {@code true} when both summary forms produce the identical string
     */
    boolean summaryEquivalenceHolds(List<LineItem> items) {
        return summaryLineChurning(items).equals(summaryLine(items));
    }
}
