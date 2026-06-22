package org.acme.commerce.order;

import java.util.List;
import org.acme.platform.money.Money;

/**
 * The outbound PORT for pricing an order (Part: architecture). order-service depends on this
 * interface, not on catalog-service directly — so the orchestrator is unit-tested with a fake and
 * wired in production with the HTTP adapter {@link CatalogPricingClient}.
 */
public interface PricingPort {

    /** The total for the given lines; throws an {@code ApiException} if a product is unknown. */
    Money priceOrder(List<OrderItem> items);
}
