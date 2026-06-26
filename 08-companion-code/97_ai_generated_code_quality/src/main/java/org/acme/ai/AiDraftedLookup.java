package org.acme.ai;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Counter-example standing in for an AI draft: a storefront customer lookup that builds its SQL by
 * string concatenation, so an email value carrying SQL syntax is parsed as SQL rather than treated as
 * data. This is the injection class behind OWASP Top 10:2025 A03 (CWE-89), and it is the chapter's
 * worked instance of vulnerability inheritance — the pattern is well-represented in the public code a
 * model learns from, so the model reproduces it, fluently and with no warning sign.
 *
 * <p>This type is a deliberate teaching counter-example. It is exercised only for behavior by
 * {@code AiGeneratedCodeQualityTest} (against a fake {@link Connection}) and is never wired into a
 * running path. The construction that designs the class out is {@link ReviewedLookup}, the reviewed
 * fix. Working against the JDK {@code java.sql} API shape keeps the module driver-free and
 * database-free; the concatenation mistake and its fix are independent of any particular database, and
 * independent of whether a human or a model typed it — which is the chapter's point.
 */
final class AiDraftedLookup {

    private final Connection connection;

    AiDraftedLookup(Connection connection) {
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
        // The AI draft folds the value into the query text, so it is parsed as SQL, not data — the defect.
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
