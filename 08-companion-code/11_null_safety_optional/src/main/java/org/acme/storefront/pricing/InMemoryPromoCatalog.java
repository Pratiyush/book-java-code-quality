package org.acme.storefront.pricing;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The in-memory adapter for {@link PromoCatalog} — the runnable default.
 *
 * <p>Backed by a {@link ConcurrentHashMap} so the catalog can be read from several threads at once. A
 * lookup miss returns an empty {@link Optional} (never {@code null}), honouring the port's return
 * contract: the absence is in the type, so {@link Map#get(Object)} returning {@code null} is converted
 * to empty here, at the boundary, rather than leaking a raw null to callers (Item 54). A production
 * deployment supplies a datastore-backed adapter instead.
 */
public final class InMemoryPromoCatalog implements PromoCatalog {

    private final ConcurrentHashMap<String, Discount> discounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Discount> lookup(String code) {
        Objects.requireNonNull(code, "code");
        return Optional.ofNullable(discounts.get(code));
    }

    /**
     * Registers (or replaces) the discount a promo code grants.
     *
     * @param discount the discount to register, keyed by its code, never {@code null}
     * @throws NullPointerException if {@code discount} is {@code null}
     */
    public void register(Discount discount) {
        Objects.requireNonNull(discount, "discount");
        discounts.put(discount.code(), discount);
    }
}
