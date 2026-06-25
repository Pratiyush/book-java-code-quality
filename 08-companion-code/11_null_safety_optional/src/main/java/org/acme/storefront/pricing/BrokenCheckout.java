package org.acme.storefront.pricing;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * The broken checkout path, kept beside its repair ({@link Checkout}) so the mistake and its fix sit
 * in one module — this is the chapter's honest-limit demo, not a pattern to copy.
 *
 * <p>The {@code total} method accepts a {@code @Nullable Discount} — a discount that may be absent —
 * and dereferences it without a guard. Two things are true at once, and that is the lesson. At
 * <em>runtime</em>, an absent discount makes {@code discount.amountOffMinor()} throw, and because
 * helpful {@code NullPointerException} messages are on by default at the anchor (JEP 358), the message
 * names the exact null expression. At <em>build time</em>, source-and-bytecode analyzers such as
 * Checkstyle and SpotBugs do not reject this code: the value arrives as an annotated parameter, not a
 * provably-null local, so the {@code quality} profile stays green. Closing that gap — turning the
 * unguarded dereference of a {@code @Nullable} value into a build error — is what a nullness checker
 * (NullAway, the Checker Framework) reading the {@code @Nullable} annotation adds, and is the chapter's
 * fourth lever. Removing the {@code @Nullable} here (lying to the checker) would let even that build
 * pass while the runtime NPE returned.
 */
public final class BrokenCheckout {

    private final PromoCatalog catalog;

    /**
     * Creates a checkout over the given promo catalog.
     *
     * @param catalog the promo lookup port, never {@code null}
     * @throws NullPointerException if {@code catalog} is {@code null}
     */
    public BrokenCheckout(PromoCatalog catalog) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
    }

    /**
     * Resolves a code to a discount or {@code null} — the lossy shape that discards the absence the
     * port's {@link java.util.Optional} return was carrying.
     *
     * @param promoCode the code to resolve, never {@code null}
     * @return the discount, or {@code null} when the code is unknown
     */
    public @Nullable Discount lookupOrNull(String promoCode) {
        Objects.requireNonNull(promoCode, "promoCode");
        return catalog.lookup(promoCode).orElse(null);
    }

    /**
     * Computes the order total, dereferencing a possibly-absent discount without checking it first.
     *
     * @param discount   a discount that may be absent, dereferenced unguarded
     * @param orderTotal the order total before any discount, never {@code null}
     * @return the order total after the discount
     * @throws NullPointerException if {@code discount} is {@code null} (the unguarded dereference)
     */
    public Money total(@Nullable Discount discount, Money orderTotal) {
        Objects.requireNonNull(orderTotal, "orderTotal");
        // tag::unguarded-deref[]
        // BUG: `discount` may be null; nothing guards the dereference. An absent discount throws here,
        // with a JEP 358 message naming the exact null expression — see Checkout for the fix.
        return orderTotal.minus(discount.amountOffMinor());
        // end::unguarded-deref[]
    }
}
