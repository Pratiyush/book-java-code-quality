package org.acme.logistics.fulfilment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.acme.platform.error.ApiException;
import org.acme.platform.json.Json;

/**
 * A validated request to fulfil an order (Part: input validation).
 *
 * @param orderId the order to fulfil
 * @param lines   the lines to reserve and ship; non-empty, copied defensively
 */
public record FulfilmentRequest(String orderId, List<Line> lines) {

    public FulfilmentRequest {
        lines = List.copyOf(lines);
    }

    /** One fulfilment line: a SKU and how many to reserve. */
    public record Line(String sku, int quantity) {
    }

    public static FulfilmentRequest fromJson(Map<String, Object> body) {
        String orderId = Json.requireString(body, "orderId");
        Object raw = body.get("lines");
        if (!(raw instanceof List<?> rawLines) || rawLines.isEmpty()) {
            throw ApiException.badRequest("lines-missing", "'lines' must be a non-empty array");
        }
        List<Line> lines = new ArrayList<>();
        for (Object element : rawLines) {
            if (!(element instanceof Map<?, ?> line)) {
                throw ApiException.badRequest("line-malformed", "each line must be an object");
            }
            Object sku = line.get("sku");
            Object quantity = line.get("quantity");
            if (!(sku instanceof String s) || s.isBlank() || !(quantity instanceof Number n) || n.intValue() <= 0) {
                throw ApiException.unprocessable("line-invalid", "each line needs a sku and a positive quantity");
            }
            lines.add(new Line(s, n.intValue()));
        }
        return new FulfilmentRequest(orderId, lines);
    }
}
