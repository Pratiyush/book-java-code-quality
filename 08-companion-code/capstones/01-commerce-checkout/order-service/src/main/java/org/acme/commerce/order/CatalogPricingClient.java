package org.acme.commerce.order;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.acme.platform.client.ServiceClient;
import org.acme.platform.error.ApiException;
import org.acme.platform.json.Json;
import org.acme.platform.money.Money;

/**
 * The HTTP adapter that prices an order by calling catalog-service (Part: service composition). It
 * fetches each product's authoritative price and sums the lines — order-service never trusts a
 * client-supplied price, which is the access-control reason pricing lives behind the catalog and not
 * in the order request.
 *
 * <p>An unknown product surfaces as catalog-service's 404, which this adapter translates into a 422
 * Unprocessable Entity for the order caller — the order cannot be priced, but the request was
 * well-formed.
 */
public final class CatalogPricingClient implements PricingPort {

    private final ServiceClient client;
    private final URI catalogBaseUri;

    public CatalogPricingClient(ServiceClient client, URI catalogBaseUri) {
        this.client = client;
        this.catalogBaseUri = catalogBaseUri;
    }

    @Override
    public Money priceOrder(List<OrderItem> items) {
        Money total = null;
        for (OrderItem item : items) {
            Money line = priceLine(item);
            total = total == null ? line : total.plus(line);
        }
        return total; // items is guaranteed non-empty by Order's invariant
    }

    private Money priceLine(OrderItem item) {
        ServiceClient.Reply reply = client.getJson(catalogBaseUri.resolve("/products/" + item.productId()));
        if (reply.status() == 404) {
            throw ApiException.unprocessable("product-unknown",
                "cannot price unknown product " + item.productId());
        }
        if (!reply.isSuccess()) {
            throw new IllegalStateException("catalog-service error: HTTP " + reply.status());
        }
        Map<String, Object> body = reply.jsonObject();
        Money unitPrice = Money.of(Json.requireLong(body, "priceMinor"), Json.requireString(body, "currency"));
        return unitPrice.times(item.quantity());
    }
}
