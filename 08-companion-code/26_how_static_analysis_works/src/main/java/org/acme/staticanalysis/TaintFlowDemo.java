package org.acme.staticanalysis;

import java.util.List;
import java.util.Map;

/**
 * Move 4 of the ladder — taint tracking, and the sanitizer that breaks it.
 *
 * <p>Taint analysis is data-flow whose propagated fact is "this value is attacker-controlled." It needs
 * four roles: a <strong>source</strong> (where untrusted data enters), a <strong>sink</strong> (a
 * dangerous operation), a <strong>sanitizer</strong> (a step that makes the value safe), and the
 * <strong>flow steps</strong> that spread taint. {@link #lookupTainted(CatalogQuery, String)} carries a
 * value from a source (a request parameter) through a concatenation (a flow step — the value is "derived
 * from" the input, so taint propagates) into a raw sink, which is the flow a taint engine reports. The
 * defining move over plain data-flow is that taint follows the value across the concatenation even
 * though the exact characters change.
 *
 * <p>{@link #lookupSafe(CatalogQuery, String)} is the same lookup with the value bound as a parameter:
 * the untrusted value never becomes part of the command text, so the sanitizer breaks the source&rarr;sink
 * path and the finding clears. The two methods are the chapter's taint thesis stated as a before/after.
 */
public final class TaintFlowDemo {

    private TaintFlowDemo() {
    }

    /**
     * Looks up products by category, splicing the untrusted category straight into the command — the
     * taint flow. The {@code categoryParam} is the source; the concatenation is the flow step that
     * carries taint into the raw sink.
     *
     * @param query         the query seam (the sink lives here)
     * @param categoryParam the untrusted category from a request, the taint source
     * @return the product names in that category
     */
    // tag::taint-flow[]
    public static List<String> lookupTainted(CatalogQuery query, String categoryParam) {
        // TAINT: categoryParam (source) is concatenated into the command (flow step) and reaches
        // runRaw (sink) unsanitized — the source -> sink path a taint engine reports.
        String command = "SELECT name FROM product WHERE category = '" + categoryParam + "'";
        return query.runRaw(command);
    }
    // end::taint-flow[]

    /**
     * Looks up products by category with the value bound as a parameter — the sanitized counterpart.
     * The command template carries no untrusted text, so the taint never reaches the sink.
     *
     * @param query         the query seam (the sink lives here)
     * @param categoryParam the untrusted category from a request, kept out of the command text
     * @return the product names in that category
     */
    // tag::taint-fixed[]
    public static List<String> lookupSafe(CatalogQuery query, String categoryParam) {
        // SANITIZED: the template is fixed; categoryParam is bound as data, never command text, so
        // the source -> sink taint path is broken (the barrier the engine recognizes).
        String template = "SELECT name FROM product WHERE category = :category";
        return query.runParameterized(template, Map.of("category", categoryParam));
    }
    // end::taint-fixed[]
}
