package org.acme.storefront.readability;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;

/**
 * Records the before-state of the readability pass and exposes the module's explicit failure path.
 *
 * <p>The {@code naming-bad} region below is the file a reviewer would stall on, kept inside a comment
 * on purpose: were it real code, the Checkstyle naming modules would reject {@code orderthing},
 * {@code maxRetries} and {@code calc} at build time, which is exactly the point — the gate refuses the
 * mis-cased names. {@link OrderLine} is the same intent after a human has done the part no regex can:
 * renamed {@code data} to what it actually holds. The case is mechanical; the meaning is not.
 */
public final class ReadabilityNotes {

    private static final Logger LOG = System.getLogger(ReadabilityNotes.class.getName());

    /*
     * The before-state, shown as text so the build stays green (real declarations with these names
     * would fail the Checkstyle naming gate — which is the lesson, not an accident):
     *
     * tag::naming-bad[]
     * public class orderthing {                 // type not UpperCamelCase
     *   static final int maxRetries = 3;        // a real constant, not CONSTANT_CASE
     *   private java.util.List data;            // legal case, but a name that says nothing
     *   public int calc(int X){return X*data.size();} // cramped, X is not lowerCamelCase
     * }
     * end::naming-bad[]
     */
    private ReadabilityNotes() {
        // utility holder; no instances
    }

    /**
     * The explicit failure path: build one validated order line, fail-fast on a malformed one.
     *
     * <p>Demonstrates that the rename and reformat are semantics-preserving — the validated type still
     * rejects bad input at the call site rather than letting it flow downstream. A blank SKU or an
     * out-of-range quantity throws here, at construction, not deep in a later calculation.
     *
     * @param sku the stock-keeping unit, never {@code null} or blank
     * @param quantity the number of units, in {@code [1, MAX_QUANTITY_PER_LINE]}
     * @param unitPrice the unit price, never {@code null}
     * @return the validated order line
     * @throws NullPointerException if {@code sku} or {@code unitPrice} is {@code null}
     * @throws IllegalArgumentException if {@code sku} is blank or {@code quantity} is out of range
     */
    public static OrderLine validatedLine(String sku, int quantity, Money unitPrice) {
        Objects.requireNonNull(unitPrice, "unitPrice");
        try {
            return new OrderLine(sku, quantity, unitPrice);
        } catch (IllegalArgumentException rejected) {
            LOG.log(Level.WARNING, "rejected order line for sku {0}: {1}", sku, rejected.getMessage());
            throw rejected;
        }
    }
}
