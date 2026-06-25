package org.acme.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The chapter's earned-assertion lesson made runnable (Effective Java Item 32): {@code @SafeVarargs}
 * is a promise the author has verified, not a switch that silences a warning.
 *
 * <p>A varargs parameter of a non-reifiable type (such as {@code List<String>...}) secretly creates a
 * generic array — a {@code List<String>[]} — which the language otherwise forbids, so the compiler
 * allows the method but warns about possible heap pollution (JLS 4.12.2). Two methods sit side by
 * side to show where the warning is right and where it is not:
 *
 * <ul>
 *   <li>{@link #flatten(List[])} only <em>reads</em> the array and never lets a reference to it
 *       escape, so the promise holds and {@code @SafeVarargs} discharges the warning honestly.</li>
 *   <li>{@link #dangerous(List[])} writes a foreign element into the array through an {@code Object[]}
 *       alias — genuine heap pollution. It carries <em>no</em> {@code @SafeVarargs}: the warning is
 *       correct, and annotating it away would ship the corruption to the caller. A test proves the
 *       {@link ClassCastException} it causes, so the hazard is demonstrated, not just described.</li>
 * </ul>
 *
 * <p>The corruption in {@link #dangerous(List[])} stays inside the method — the array is neither
 * stored in a field nor returned — so the demonstration needs no analyzer suppression; it is the
 * compiler's heap-pollution warning, and the runtime failure, that carry the lesson.
 */
public final class VarargsHeapPollution {

    private VarargsHeapPollution() {
    }

    /**
     * Flattens several lists into one, reading every element and returning a fresh list.
     *
     * @param lists the lists to flatten, never {@code null}; each is only read from
     * @param <T>   the element type
     * @return a new list holding every element of every input, in order, never {@code null}
     * @throws NullPointerException if {@code lists} is {@code null}
     * @implSpec Reads each input list and copies its elements into a new {@link ArrayList}. The
     *     varargs array is never written to and never escapes, so {@code @SafeVarargs} is earned.
     */
    @SafeVarargs
    public static <T> List<T> flatten(List<? extends T>... lists) {
        Objects.requireNonNull(lists, "lists");
        List<T> flat = new ArrayList<>();
        for (List<? extends T> list : lists) {      // read-only: the promise @SafeVarargs makes
            flat.addAll(list);
        }
        return flat;
    }

    /**
     * A deliberately UNSAFE varargs method, preserved as a counter-example. It poisons its own
     * generic array, so reading an element back throws {@link ClassCastException} (Item 32's hazard).
     *
     * @param stringLists lists supplied by the caller; corrupted in place by this method
     * @throws ClassCastException always, when the poisoned element is read back as a {@code String}
     */
    // tag::unsafe-varargs[]
    public static void dangerous(List<String>... stringLists) {     // NO @SafeVarargs: it is unsafe
        List<Integer> ints = List.of(42);
        Object[] array = stringLists;               // a List<String>[] aliased as Object[]
        array[0] = ints;                            // heap pollution: an Integer list in slot 0
        String first = stringLists[0].get(0);       // ClassCastException — 42 is not a String
        throw new AssertionError("unreachable: " + first);
    }
    // end::unsafe-varargs[]
}
