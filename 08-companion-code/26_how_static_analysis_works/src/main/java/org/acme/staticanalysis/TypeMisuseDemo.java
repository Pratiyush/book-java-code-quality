package org.acme.staticanalysis;

import java.util.List;

/**
 * Move 2 of the ladder — the mistake only a type-aware analysis sees.
 *
 * <p>Move 1 sees a token spelled {@code contains}; Move 2 resolves the receiver's type to
 * {@code List<Long>} and the argument's type to {@code int} (boxed to {@code Integer}), and knows the
 * two can never be equal — so the call always returns {@code false}. The call compiles: {@code
 * List.contains(Object)} accepts any argument, so {@code javac} alone says nothing. A checker that
 * augments the compiler's type analysis (the kind Error Prone runs inside {@code javac}) flags it as
 * {@code CollectionIncompatibleType}, because it resolves the element type the raw signature throws
 * away. That is the whole value of Move 2 over Move 1: knowing the types separates a real bug from a
 * call that merely looks fine.
 *
 * <p>The mistake is caught here twice over by type resolution: Error Prone names it
 * {@code CollectionIncompatibleType} inside {@code javac}, and SpotBugs reaches the same conclusion on
 * the bytecode and reports {@code GC_UNRELATED_TYPES}. SpotBugs raising it is the Move-2 lesson made
 * concrete in this module's own gate, so the finding carries a single reviewed suppression with a reason
 * (it is a deliberate teaching counter-example, not a defect to ship). The shape is kept as a small
 * query so it has no other effect on the build; {@link #containsQuantity(List, long)} shows the
 * type-correct form, which no tool flags.
 */
public final class TypeMisuseDemo {

    private TypeMisuseDemo() {
    }

    /**
     * Reports whether the catalogue holds an id — using a mismatched element type, the bug. The
     * {@code int} argument boxes to {@code Integer}, which never equals any {@code Long} element, so
     * this query is always {@code false} regardless of the list's contents.
     *
     * @param ids      the catalogue of {@code Long} ids
     * @param targetId the id to look for, mistakenly typed {@code int}
     * @return always {@code false} — the type-misuse the chapter's Move 2 catches
     */
    // tag::type-misuse[]
    public static boolean catalogueHas(List<Long> ids, int targetId) {
        // MISUSE (Error Prone CollectionIncompatibleType): an int never equals a Long element,
        // so this contains(...) is always false — a logic bug javac alone accepts.
        return ids.contains(targetId);
    }
    // end::type-misuse[]

    /**
     * Reports whether the catalogue holds an id — with matching types, the corrected form.
     *
     * @param ids      the catalogue of {@code Long} ids
     * @param targetId the id to look for, correctly typed {@code long}
     * @return {@code true} when {@code targetId} is present
     */
    public static boolean containsQuantity(List<Long> ids, long targetId) {
        return ids.contains(targetId);
    }
}
