package org.acme.storefront.pricing;

import java.util.Objects;
import java.util.Optional;

/**
 * The corrected checkout path: it handles the absent discount before computing a total.
 *
 * <p>This is the repair half of the chapter's contrast (its broken twin is {@link BrokenCheckout}).
 * The promo lookup may be absent, so the result is reached through {@code map}/{@code orElse} — the
 * empty case is given a value rather than being dereferenced. The same shape the type system steers a
 * caller toward when the lookup's return is an {@link Optional} and the package is {@code @NullMarked}:
 * there is no reference to dereference unguarded, so the NPE the broken twin throws cannot arise here.
 */
public final class Checkout {

    private final PromoCatalog catalog;

    /**
     * Creates a checkout over the given promo catalog.
     *
     * @param catalog the promo lookup port, never {@code null}
     * @throws NullPointerException if {@code catalog} is {@code null}
     */
    public Checkout(PromoCatalog catalog) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
    }

    /**
     * Computes the order total after applying the named promo code, if it resolves to a discount.
     *
     * @param promoCode  the code the customer entered, never {@code null}
     * @param orderTotal the order total before any discount, never {@code null}
     * @return the order total after any applicable discount, never {@code null}
     * @throws NullPointerException if {@code promoCode} or {@code orderTotal} is {@code null}
     */
    public Money total(String promoCode, Money orderTotal) {
        Objects.requireNonNull(promoCode, "promoCode");
        Objects.requireNonNull(orderTotal, "orderTotal");
        // tag::guarded-fix[]
        Optional<Discount> discount = catalog.lookup(promoCode);
        return discount
            .map(d -> orderTotal.minus(d.amountOffMinor()))   // present: subtract the discount
            .orElse(orderTotal);                              // absent: charge the full total
        // end::guarded-fix[]
    }
}
