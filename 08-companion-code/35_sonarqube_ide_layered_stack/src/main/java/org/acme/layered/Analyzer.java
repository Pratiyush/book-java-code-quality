package org.acme.layered;

import java.util.Objects;

/**
 * One analyzer in a stack, described by what it reads and when it runs — the two coordinates that decide
 * what it can see and how fast. The name is the tool's own canonical name; the substrate and moment place
 * it in the substrate-by-moment matrix. Two analyzers with the same {@code (substrate, moment)} cell cover
 * the same ground, which is the redundancy the composition rule removes by assigning one owner per concern.
 *
 * <p>An immutable value (Item 17): an analyzer's description is a fact, fixed once stated. The name is kept
 * verbatim so a stack points at the exact tool a reader can look up, not a paraphrase.
 *
 * @param name      the tool's canonical name, never {@code null} or blank
 * @param substrate the program representation it reads, never {@code null}
 * @param moment    the moment it runs, never {@code null}
 */
public record Analyzer(String name, Substrate substrate, Moment moment) {

    /** Compact constructor: every component is required and the name is non-blank, so no half-described
     *  analyzer can exist. */
    public Analyzer {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(substrate, "substrate");
        Objects.requireNonNull(moment, "moment");
        if (name.isBlank()) {
            throw new IllegalArgumentException("analyzer name must not be blank");
        }
    }
}
