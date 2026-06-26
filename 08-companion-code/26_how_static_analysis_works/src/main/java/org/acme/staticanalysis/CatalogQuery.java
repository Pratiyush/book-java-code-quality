package org.acme.staticanalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A stand-in query seam — the <em>sink</em> the chapter's taint examples flow into, with no real
 * datastore so the module stays self-contained and JDK-only.
 *
 * <p>Move 4 models a sink as a dangerous operation (a SQL query, a shell command). This type plays that
 * role two ways so {@link TaintFlowDemo} can show both sides of the taint thesis: {@link #runRaw(String)}
 * accepts a fully-built command string (the unsafe sink a taint engine warns about when an untrusted
 * value reaches it), and {@link #runParameterized(String, Map)} accepts a fixed template plus separately
 * bound parameters (the sanitizer shape, where the untrusted value never becomes part of the command).
 * The "execution" here is a deterministic in-memory lookup; the security point is structural — where the
 * untrusted value goes, not what the store does with it.
 */
public final class CatalogQuery {

    private final Map<String, List<String>> rowsByCategory;

    /**
     * Creates a query seam over an in-memory catalogue.
     *
     * @param rowsByCategory product names keyed by category, never {@code null}
     */
    public CatalogQuery(Map<String, List<String>> rowsByCategory) {
        this.rowsByCategory = Map.copyOf(rowsByCategory);
    }

    /**
     * The unsafe sink: runs a command string that the caller has already assembled. A taint engine
     * flags any path on which an untrusted value reaches this argument, because the value has become
     * part of the command text.
     *
     * @param rawCommand the fully-assembled command, treated opaquely here
     * @return the rows the command names, or an empty list when it names none
     */
    public List<String> runRaw(String rawCommand) {
        String category = lastToken(rawCommand);
        return new ArrayList<>(rowsByCategory.getOrDefault(category, List.of()));
    }

    /**
     * The safe sink: runs a fixed template with the untrusted value supplied as a bound parameter, so
     * the value is data the engine never parses as command text — the barrier that clears the taint.
     *
     * @param template   the fixed command template (no untrusted text), never {@code null}
     * @param parameters the bound parameters, the untrusted values kept out of the command
     * @return the rows the parameter names, or an empty list when it names none
     */
    public List<String> runParameterized(String template, Map<String, String> parameters) {
        String category = parameters.getOrDefault("category", "");
        return new ArrayList<>(rowsByCategory.getOrDefault(category, List.of()));
    }

    private static String lastToken(String command) {
        int quote = command.lastIndexOf('\'');
        if (quote >= 0) {
            int open = command.lastIndexOf('\'', quote - 1);
            if (open >= 0 && open < quote) {
                return command.substring(open + 1, quote);
            }
        }
        int space = command.lastIndexOf(' ');
        return space >= 0 ? command.substring(space + 1) : command;
    }
}
