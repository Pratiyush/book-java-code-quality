package org.acme.staticanalysis;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * Move 1 of the ladder — the shape an AST / pattern rule matches.
 *
 * <p>An AST rule reasons about tree <em>structure</em>, not behaviour: it matches the shape of a node
 * (here, a {@code catch} clause whose body is empty) regardless of what the surrounding code does. The
 * swallowed exception below is the canonical example — PMD reports it as {@code EmptyCatchBlock} and
 * Checkstyle as {@code EmptyBlock}, both by walking the parse tree and finding a catch with no
 * statements. The rule cannot tell whether swallowing is safe here; it only sees the shape, which is
 * exactly Move 1's reach and its limit (it sees shape, not behaviour).
 *
 * <p>So this module's own gate stays green while the smell is still visible, the empty body carries an
 * explanatory comment — the house {@code EmptyBlock} check is configured with {@code option="text"},
 * which tolerates a commented block, whereas PMD's {@code EmptyCatchBlock} would still flag the missing
 * handling. That difference is itself the chapter's point: two AST tools draw the "empty" line in
 * different places. {@link #parseQuantitySafely(String)} shows the handled form the smell hides.
 */
public final class AstSmellDemo {

    private static final Logger LOG = System.getLogger(AstSmellDemo.class.getName());

    private AstSmellDemo() {
    }

    /**
     * Parses a quantity, swallowing any parse failure — the AST-level smell. A malformed input is
     * silently turned into {@code 0}, so the caller never learns the value was rejected.
     *
     * @param raw the textual quantity, possibly malformed
     * @return the parsed quantity, or {@code 0} when {@code raw} cannot be parsed
     */
    // tag::ast-smell[]
    public static int parseQuantity(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ignored) {
            // SMELL (PMD EmptyCatchBlock): the exception is swallowed; the failure vanishes
        }
        return 0;
    }
    // end::ast-smell[]

    /**
     * Parses a quantity, handling the failure instead of swallowing it — the shape the smell hides.
     *
     * @param raw the textual quantity, possibly malformed
     * @return the parsed quantity, or {@code 0} when {@code raw} cannot be parsed
     */
    public static int parseQuantitySafely(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            LOG.log(Level.DEBUG, "rejected quantity {0}: {1}", raw, e.getMessage());
            return 0;
        }
    }
}
