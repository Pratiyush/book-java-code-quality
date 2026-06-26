package org.acme.storefront.money.catalog;

import java.util.Objects;
import org.acme.storefront.money.Money;

/**
 * A conforming domain type: it carries price as {@link Money}, the dedicated value type, so it satisfies
 * the house invariant. The clean rules run over this package and pass; it is the "good fixture" the rule
 * tests assert is reported silently.
 */
public final class PricedItem {

    private final String sku;
    private final Money price;

    /**
     * @param sku   the stock-keeping unit, never {@code null}
     * @param price the unit price as money, never {@code null}
     */
    public PricedItem(String sku, Money price) {
        this.sku = Objects.requireNonNull(sku, "sku");
        this.price = Objects.requireNonNull(price, "price");
    }

    /** @return the stock-keeping unit */
    public String sku() {
        return sku;
    }

    /** @return the unit price as money */
    public Money price() {
        return price;
    }
}
