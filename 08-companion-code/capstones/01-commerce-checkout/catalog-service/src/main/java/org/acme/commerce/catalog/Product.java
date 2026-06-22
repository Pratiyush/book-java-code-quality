package org.acme.commerce.catalog;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.acme.platform.money.Money;

/**
 * A catalog product (Part: value modelling). Immutable; the price is {@link Money}, never a bare
 * number, so a currency travels with every amount and no caller can multiply two different
 * currencies by accident.
 *
 * @param id    the stable product identifier
 * @param name  the display name
 * @param price the unit price
 */
public record Product(String id, String name, Money price) {

    public Product {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(price, "price");
    }

    /** The JSON projection returned by the API and consumed by order-service when pricing. */
    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("name", name);
        body.put("priceMinor", price.minorUnits());
        body.put("currency", price.currency());
        return body;
    }
}
