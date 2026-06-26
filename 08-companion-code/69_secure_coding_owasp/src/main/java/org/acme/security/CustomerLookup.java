package org.acme.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The design-out fix for {@link VulnerableCustomerLookup}: the same storefront lookup with the email
 * passed as a bound parameter. The bound value is data, never parsed as SQL, so the injection class
 * (OWASP Top 10:2025 A03 / CWE-89) is eliminated rather than mitigated — a {@link PreparedStatement}
 * does not make injection harder, it removes it.
 *
 * <p>Input validation (Jakarta Bean Validation, Chapter 9) is useful defense-in-depth but is not a
 * substitute for this: a structurally "valid" string can still inject, so parameterization is the
 * actual fix. The same shape applies to JPA and Criteria parameter binding. The {@code java.sql}
 * API is used directly so the module stays driver-free and database-free.
 */
final class CustomerLookup {

    private final Connection connection;

    CustomerLookup(Connection connection) {
        this.connection = connection;
    }

    /**
     * Looks up customer ids for an email using a bound parameter.
     *
     * @param email the search value, bound as data and never parsed as SQL
     * @return the matching customer ids
     * @throws SQLException if the query fails
     */
    List<String> findIdsByEmail(String email) throws SQLException {
        // tag::sql-prepared[]
        // The '?' placeholder is bound as data, so the value can never be parsed as SQL.
        String sql = "SELECT id FROM customers WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rows = statement.executeQuery()) {
                return collectIds(rows);
            }
        }
        // end::sql-prepared[]
    }

    private static List<String> collectIds(ResultSet rows) throws SQLException {
        List<String> ids = new ArrayList<>();
        while (rows.next()) {
            ids.add(rows.getString("id"));
        }
        return ids;
    }
}
