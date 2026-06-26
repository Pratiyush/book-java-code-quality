package org.acme.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Counter-example: a storefront customer lookup that builds its SQL by string concatenation, so an
 * email value carrying SQL syntax is parsed as SQL rather than treated as data. This is the
 * injection class behind OWASP Top 10:2025 A03 (CWE-89): untrusted input concatenated into an
 * interpreter that then executes it.
 *
 * <p>This type is a deliberate teaching counter-example. It is exercised only for behavior by
 * {@code SecureCodingTest} (against a fake {@link Connection}) and is never wired into a running
 * path. The construction that designs the class out is {@link CustomerLookup}. Working against the
 * JDK {@code java.sql} API shape keeps the module driver-free and database-free; the concatenation
 * mistake and its fix are independent of any particular database.
 */
final class VulnerableCustomerLookup {

    private final Connection connection;

    VulnerableCustomerLookup(Connection connection) {
        this.connection = connection;
    }

    /**
     * Looks up customer ids for an email by concatenating the value straight into the query text.
     *
     * @param email the search value, treated here as if it were trusted SQL — the defect
     * @return the matching customer ids
     * @throws SQLException if the query fails
     */
    // tag::sql-concat[]
    List<String> findIdsByEmail(String email) throws SQLException {
        // Concatenating the value into the query text lets it be parsed as SQL, not data — the defect.
        String sql = "SELECT id FROM customers WHERE email = '" + email + "'";
        try (Statement statement = connection.createStatement();
                ResultSet rows = statement.executeQuery(sql)) {
            return collectIds(rows);
        }
    }
    // end::sql-concat[]

    private static List<String> collectIds(ResultSet rows) throws SQLException {
        List<String> ids = new ArrayList<>();
        while (rows.next()) {
            ids.add(rows.getString("id"));
        }
        return ids;
    }
}
