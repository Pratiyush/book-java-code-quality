package org.acme.commerce.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.http.HttpApp;
import org.acme.platform.http.Request;
import org.acme.platform.http.Response;
import org.acme.platform.json.Json;

/**
 * The HTTP surface of order-service (Part: API design). It parses requests, delegates to
 * {@link OrderService}, and maps the order's lifecycle onto status codes — 201 on create, 200 when a
 * payment is approved, 402 when it is declined.
 *
 * <ul>
 *   <li>{@code POST /orders}            → 201 the created order (422 if a product cannot be priced)
 *   <li>{@code GET  /orders/{id}}       → 200 the order, 404 when unknown
 *   <li>{@code POST /orders/{id}/pay}   → 200 paid, 402 declined, 404 when unknown
 * </ul>
 */
public final class OrderApi {

    private OrderApi() {
    }

    public static HttpApp newApp(OrderService service, int port) {
        HttpApp app = new HttpApp("order-service", port);
        app.post("/orders", request -> Response.created(service.place(parseItems(request)).toBody()));
        app.get("/orders/{id}", request -> Response.ok(service.require(request.pathParam("id")).toBody()));
        app.post("/orders/{id}/pay", request -> payOrder(service, request));
        return app;
    }

    private static Response payOrder(OrderService service, Request request) {
        Map<String, Object> body = request.jsonBody();
        Order order = service.pay(
            request.pathParam("id"),
            Json.requireString(body, "pan"),
            Json.requireString(body, "idempotencyKey"));
        int status = order.status() == OrderStatus.PAID ? 200 : 402;
        return Response.json(status, order.toBody());
    }

    private static List<OrderItem> parseItems(Request request) {
        Object raw = request.jsonBody().get("items");
        if (!(raw instanceof List<?> list) || list.isEmpty()) {
            throw ApiException.badRequest("items-missing", "'items' must be a non-empty array");
        }
        List<OrderItem> items = new ArrayList<>();
        for (Object element : list) {
            if (!(element instanceof Map<?, ?> item)) {
                throw ApiException.badRequest("item-malformed", "each item must be an object");
            }
            items.add(new OrderItem(stringField(item, "productId"), intField(item, "quantity")));
        }
        return items;
    }

    private static String stringField(Map<?, ?> item, String field) {
        if (item.get(field) instanceof String s && !s.isBlank()) {
            return s;
        }
        throw ApiException.badRequest("field-missing", "missing or blank item field: " + field);
    }

    private static int intField(Map<?, ?> item, String field) {
        if (item.get(field) instanceof Number n) {
            return Math.toIntExact(n.longValue());
        }
        throw ApiException.badRequest("field-missing", "missing or non-numeric item field: " + field);
    }
}
