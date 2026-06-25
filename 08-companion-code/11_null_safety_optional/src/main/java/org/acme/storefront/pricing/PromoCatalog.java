package org.acme.storefront.pricing;

import java.util.Optional;

/**
 * The lookup port for promo codes (a hexagonal seam: one in-memory adapter ships, a datastore adapter
 * slots in behind the same interface).
 *
 * <p>The lookup returns {@link Optional} rather than {@code null} so "there may be no such promo code"
 * is a fact in the signature, not an undocumented runtime surprise. A caller is steered to handle the
 * absent case before touching a discount — the dereference the chapter's hook describes never gets the
 * chance to compile (Item 55).
 */
public interface PromoCatalog {

    /**
     * Finds the discount a promo code grants.
     *
     * @param code the promo code to look up, never {@code null}
     * @return the discount if the code is known, otherwise an empty {@code Optional} — never
     *     {@code null}
     */
    Optional<Discount> lookup(String code);
}
