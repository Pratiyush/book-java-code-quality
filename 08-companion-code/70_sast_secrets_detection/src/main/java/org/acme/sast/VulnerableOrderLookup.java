package org.acme.sast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Counter-example: a storefront order lookup that builds its SQL by string concatenation, so the email
 * value flows untouched from the request into the query text. This is the source-to-sink flow that SAST
 * taint analysis traces and that the chapter's Semgrep rule ({@code config/semgrep/java-injection.yml})
 * targets: untrusted input (the source) reaches {@link Statement#executeQuery(String)} (the dangerous
 * sink) with no parameterization in between. It is the injection class behind OWASP Top 10:2025 A03
 * (CWE-89).
 *
 * <p>This type is a deliberate teaching counter-example. It is exercised only for behavior by
 * {@code SastSecretsTest} (against a fake {@link Connection}) and is never wired into a running path —
 * {@link SastSecretsGate} uses {@link OrderLookup} exclusively. The finding it raises is suppressed
 * narrowly, with a reason, in {@code config/spotbugs/spotbugs-exclude.xml}; the construction that designs
 * the class out is {@link OrderLookup}. Working against the JDK {@code java.sql} API shape keeps the
 * module driver-free and database-free; the concatenation mistake and its fix are independent of any
 * particular database.
 */
final class VulnerableOrderLookup {

    private final Connection connection;

    VulnerableOrderLookup(Connection connection) {
        this.connection = connection;
    }

    /**
     * Looks up order ids for an email by concatenating the value straight into the query text.
     *
     * @param email the search value, treated here as if it were trusted SQL — the defect SAST traces
     * @return the matching order ids
     * @throws SQLException if the query fails
     */
    // tag::sql-sink[]
    List<String> findOrdersByEmail(String email) throws SQLException {
        // The email flows from request to query text untouched: the taint source reaches the SQL sink.
        String sql = "SELECT id FROM orders WHERE customer_email = '" + email + "'";
        try (Statement statement = connection.createStatement();
                ResultSet rows = statement.executeQuery(sql)) {       // dangerous sink: SAST flags this path
            return collectIds(rows);
        }
    }
    // end::sql-sink[]

    private static List<String> collectIds(ResultSet rows) throws SQLException {
        List<String> ids = new ArrayList<>();
        while (rows.next()) {
            ids.add(rows.getString("id"));
        }
        return ids;
    }
}
