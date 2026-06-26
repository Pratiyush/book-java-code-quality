package org.acme.checkstyle;

/**
 * Pricing constants — what the {@code ConstantName} naming rule governs.
 *
 * <p>{@code ConstantName} checks {@code static final} field names against its default regex
 * {@code ^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$} — the {@code UPPER_SNAKE} form. The two constants below are in
 * that shape and pass; a constant named {@code freeShippingThreshold} would be a violation under the same
 * rule. The values themselves (the threshold and the rate) are this team's cited business choices, not
 * universal numbers, which is exactly why a naming rule governs their <em>names</em> and never their
 * <em>values</em>.
 */
public final class PricingRules {

    // tag::constant-naming[]
    /** Orders at or above this many cents ship free — a constant in the UPPER_SNAKE shape the rule accepts. */
    public static final long FREE_SHIPPING_THRESHOLD_CENTS = 5_000L;

    /** Flat shipping charge in cents, applied below the free-shipping threshold. */
    public static final long FLAT_SHIPPING_CENTS = 599L;
    // end::constant-naming[]

    private PricingRules() {
    }

    /**
     * Returns the shipping charge in cents for an order subtotal.
     *
     * @param subtotalCents the order subtotal in whole cents, never negative
     * @return {@code 0} at or above {@link #FREE_SHIPPING_THRESHOLD_CENTS}, otherwise
     *         {@link #FLAT_SHIPPING_CENTS}
     */
    public static long shippingFor(long subtotalCents) {
        return subtotalCents >= FREE_SHIPPING_THRESHOLD_CENTS ? 0L : FLAT_SHIPPING_CENTS;
    }
}
