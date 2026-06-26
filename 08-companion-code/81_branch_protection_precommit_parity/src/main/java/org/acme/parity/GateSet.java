package org.acme.parity;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A named set of quality-check names — for example the checks a developer can run locally (the
 * pre-commit hooks plus the local build) or the checks CI requires (the required status checks wired
 * into branch protection). The chapter's parity property is a relationship between two of these sets:
 * every check the trunk's gate requires must have a local counterpart, so "green locally" can predict
 * "green in CI" instead of being a hope.
 *
 * <p>An immutable value (Item 17). The check names are kept verbatim, because parity is decided by
 * matching names exactly: a check named one thing locally and another in CI is not in parity, and the
 * point of the comparison is to surface that drift rather than paper over it. The order callers pass is
 * preserved so a reported drift lists checks in a stable, readable order.
 */
public final class GateSet {

    private final String name;
    private final Set<String> checkNames;

    /**
     * Creates a named gate set over a copy of the given check names.
     *
     * @param name       what this set represents, for example {@code "local"} or {@code "ci"}, never {@code null}
     * @param checkNames the check names in this set, never {@code null} and containing no {@code null}
     */
    public GateSet(String name, Set<String> checkNames) {
        this.name = Objects.requireNonNull(name, "name");
        Objects.requireNonNull(checkNames, "checkNames");
        Set<String> copy = new LinkedHashSet<>();
        for (String check : checkNames) {
            copy.add(Objects.requireNonNull(check, "checkNames must not contain null"));
        }
        this.checkNames = Set.copyOf(copy);
    }

    /**
     * Returns whether this set contains a check by exact name.
     *
     * @param checkName the check name to look for, never {@code null}
     * @return {@code true} if a check with that exact name is in this set
     */
    public boolean contains(String checkName) {
        return checkNames.contains(Objects.requireNonNull(checkName, "checkName"));
    }

    /** @return what this set represents, for example {@code "local"} or {@code "ci"} */
    public String name() {
        return name;
    }

    /** @return an unmodifiable view of the check names in this set */
    public Set<String> checkNames() {
        return checkNames;
    }
}
