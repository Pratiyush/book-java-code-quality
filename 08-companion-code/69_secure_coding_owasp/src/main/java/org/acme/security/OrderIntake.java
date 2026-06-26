package org.acme.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * The design-out fix for {@link VulnerableOrderIntake}. The primary fix, {@link #parse(String)},
 * never reconstitutes an arbitrary object graph: it parses the request into the fixed-shape
 * {@link OrderRequest} record, so request bytes can only ever become that one validated type — the
 * elimination a safe data format (JSON, Protobuf with polymorphic typing disabled) provides, and the
 * hierarchy <em>Effective Java</em> (Chapter 5) states: prefer alternatives to Java serialization.
 *
 * <p>{@link #readFiltered} shows the mitigation for when native serialization is genuinely
 * unavoidable: an {@link ObjectInputFilter} (JEP 290, Java 9) allow-list constrains which classes
 * may be deserialized. An allow-list is a mitigation of an evolving gadget-chain threat, not the
 * elimination {@link #parse(String)} gives — it works only while the list stays correct as gadget
 * chains change.
 */
public final class OrderIntake {

    private OrderIntake() {
    }

    /**
     * Parses a request line of {@code key=value;...} fields into a validated {@link OrderRequest}.
     * The bytes can only ever become this one fixed-shape record, never an arbitrary object graph.
     *
     * @param requestLine the request payload as text (the shape a JSON decoder maps onto a DTO)
     * @return the validated order request
     * @throws IllegalArgumentException if a required field is missing or malformed
     */
    // tag::deser-dto[]
    public static OrderRequest parse(String requestLine) {
        Map<String, String> fields = splitFields(requestLine);
        String customerId = require(fields, "customerId");
        String sku = require(fields, "sku");
        // The bytes become this one fixed-shape record, never an arbitrary object graph.
        return new OrderRequest(customerId, sku, parseQuantity(require(fields, "quantity")));
    }
    // end::deser-dto[]

    /**
     * Reads a serialized object under an allow-list filter — the mitigation when native
     * serialization cannot be removed. Only the permitted classes may be reconstituted.
     *
     * @param requestBytes the untrusted request payload
     * @return the reconstituted object, constrained to the allow-list
     * @throws IOException            if the stream cannot be read or the filter rejects a class
     * @throws ClassNotFoundException if a permitted class is not on the classpath
     */
    // tag::deser-filter[]
    public static Object readFiltered(byte[] requestBytes) throws IOException, ClassNotFoundException {
        // JEP 290 allow-list: only these classes may be reconstituted; everything else is rejected.
        ObjectInputFilter allowList = ObjectInputFilter.Config.createFilter("java.lang.String;!*");
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(requestBytes))) {
            in.setObjectInputFilter(allowList);
            return in.readObject();
        }
    }
    // end::deser-filter[]

    private static Map<String, String> splitFields(String requestLine) {
        Map<String, String> fields = new HashMap<>();
        for (String pair : requestLine.split(";")) {
            int eq = pair.indexOf('=');
            if (eq > 0) {
                fields.put(pair.substring(0, eq).trim(), pair.substring(eq + 1).trim());
            }
        }
        return fields;
    }

    private static String require(Map<String, String> fields, String key) {
        String value = fields.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("missing field: " + key);
        }
        return value;
    }

    private static int parseQuantity(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("quantity is not a number: " + value, e);
        }
    }
}
