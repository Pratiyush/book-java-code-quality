package org.acme.storefront.pricing;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * Resolves the discount that applies to an order, demonstrating the design-time and boundary levers
 * of null-safety from the chapter in one service.
 *
 * <p>Absence is modelled three ways on purpose, so each idiom is visible: the lookup returns an
 * {@link Optional} (a result that may not exist), the constructor {@code requireNonNull}s its required
 * collaborator (a value that must never be absent), and a single accessor opts out of the package's
 * {@code @NullMarked} default with an explicit {@code @Nullable} return (the rare place null is the
 * honest answer). The service holds only an injected {@link PromoCatalog} port — an interface, shared
 * collaborator state, not a mutable representation to defend by copying.
 */
public final class DiscountService {

    private static final Logger LOG = System.getLogger(DiscountService.class.getName());

    private final PromoCatalog catalog;

    /**
     * The configured default promo code, or empty when the deployment sets none. Held as an
     * {@link Optional} field's resolved form — a non-null reference that is either a code or empty —
     * rather than a {@code @Nullable String}, so the absence decision is made once at construction.
     */
    private final Optional<String> defaultCode;

    /** Observability: how many lookups resolved to the present (discount-found) branch. */
    private long discountsApplied;

    /** Observability: how many lookups resolved to the empty (no-discount) branch. */
    private long lookupsWithoutDiscount;

    /**
     * Creates a service over the given promo catalog and an externally-configured default code.
     *
     * @param catalog     the promo lookup port, never {@code null}
     * @param defaultCode the deployment's default code, or empty when none is configured
     * @throws NullPointerException if {@code catalog} or {@code defaultCode} is {@code null}
     */
    // tag::require-nonnull[]
    public DiscountService(PromoCatalog catalog, Optional<String> defaultCode) {
        // fail fast at the boundary: a missing collaborator throws here, not frames later
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.defaultCode = Objects.requireNonNull(defaultCode, "defaultCode");
    }
    // end::require-nonnull[]

    /**
     * Looks up the discount a promo code grants.
     *
     * @param code the promo code to resolve, never {@code null}
     * @return the discount if the code is known, otherwise an empty {@code Optional} — never
     *     {@code null}
     */
    // tag::optional-return[]
    public Optional<Discount> findDiscount(String code) {
        Objects.requireNonNull(code, "code");
        // absence is in the return type: callers handle the empty case, never dereference a null
        return catalog.lookup(code);
    }
    // end::optional-return[]

    /**
     * Applies the best discount available for a code to an order total, falling back to the configured
     * default code when the caller supplies none.
     *
     * <p>The result is computed without ever calling {@link Optional#get()}: {@code map} transforms
     * the present case and {@code orElse} supplies the absent one, so there is no unguarded access to
     * defend against. A present discount increments one counter and an absent lookup the other, so the
     * branch the code took is observable.
     *
     * @param requestedCode the code the customer entered, or empty to use the configured default
     * @param orderTotal    the order total before any discount, never {@code null}
     * @return the order total after any applicable discount, never {@code null}
     * @throws NullPointerException if {@code requestedCode} or {@code orderTotal} is {@code null}
     */
    public Money priceWithDiscount(Optional<String> requestedCode, Money orderTotal) {
        Objects.requireNonNull(requestedCode, "requestedCode");
        Objects.requireNonNull(orderTotal, "orderTotal");
        Optional<String> code = requestedCode.or(() -> defaultCode);
        // tag::optional-map[]
        return code.flatMap(catalog::lookup)
            .map(discount -> applyDiscount(orderTotal, discount))
            .orElseGet(() -> noDiscount(orderTotal));
        // end::optional-map[]
    }

    /**
     * Returns the configured default promo code, or {@code null} when none is configured — the one
     * place this {@code @NullMarked} package opts out, marked explicitly so a checker treats the
     * absence as part of the contract rather than a bug.
     *
     * @return the default promo code, or {@code null} if the deployment configured none
     */
    // tag::nullable-return[]
    public @Nullable String defaultCodeOrNull() {   // explicit opt-out of the @NullMarked default
        return defaultCode.orElse(null);
    }
    // end::nullable-return[]

    private Money applyDiscount(Money orderTotal, Discount discount) {
        discountsApplied++;
        LOG.log(Level.DEBUG, "applied discount {0} to order", discount.code());
        return orderTotal.minus(discount.amountOffMinor());
    }

    private Money noDiscount(Money orderTotal) {
        lookupsWithoutDiscount++;
        LOG.log(Level.DEBUG, "no discount applied to order");
        return orderTotal;
    }

    /**
     * Health/observability surface: how many lookups resolved to a present discount since startup.
     *
     * @return the number of applied discounts, never negative
     */
    public long discountsAppliedCount() {
        return discountsApplied;
    }

    /**
     * Health/observability surface: how many lookups resolved to the no-discount branch since startup.
     *
     * @return the number of lookups that found no discount, never negative
     */
    public long lookupsWithoutDiscountCount() {
        return lookupsWithoutDiscount;
    }

    /**
     * A readiness probe: the service is ready when its catalog port is wired.
     *
     * @return {@code true} when the service can resolve discounts
     */
    public boolean isReady() {
        return catalog != null;
    }
}
