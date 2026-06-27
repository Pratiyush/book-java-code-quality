package org.acme.sast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The design-out fix for {@link VulnerableOrderLookup}: the same storefront order lookup with the email
 * passed as a bound parameter. The bound value is data, never parsed as SQL, so the source-to-sink flow
 * SAST traces in the counter-example no longer exists — a {@link PreparedStatement} does not make
 * injection harder, it removes the class (OWASP Top 10:2025 Injection, CWE-89). A taint engine reports a clean
 * path here because there is no untrusted-source-to-dangerous-sink flow left to trace.
 *
 * <p>Input validation (Jakarta Bean Validation, Chapter 9) is useful defense-in-depth but is not a
 * substitute for this: a structurally valid email can still carry SQL syntax, so parameterization is the
 * actual fix. The same shape applies to JPA and Criteria parameter binding. The {@code java.sql} API is
 * used directly so the module stays driver-free and database-free.
 */
final class OrderLookup {

    private final Connection connection;

    OrderLookup(Connection connection) {
        this.connection = connection;
    }

    /**
     * Looks up order ids for an email using a bound parameter.
     *
     * @param email the search value, bound as data and never parsed as SQL
     * @return the matching order ids
     * @throws SQLException if the query fails
     */
    List<String> findOrdersByEmail(String email) throws SQLException {
        // The '?' placeholder is bound as data, so the value can never be parsed as SQL.
        String sql = "SELECT id FROM orders WHERE customer_email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rows = statement.executeQuery()) {
                return collectIds(rows);
            }
        }
    }

    private static List<String> collectIds(ResultSet rows) throws SQLException {
        List<String> ids = new ArrayList<>();
        while (rows.next()) {
            ids.add(rows.getString("id"));
        }
        return ids;
    }
}
