package org.acme.remediation.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The "before" form (Chapter 40, the automation engine). A small utility written in a pre-records,
 * pre-{@code List.of} idiom: it builds an immutable list the verbose way (a mutable {@link ArrayList}
 * populated with {@code add}, then wrapped in {@link Collections#unmodifiableList}). This is exactly the
 * kind of mechanical, type-aware modernization a recipe applies across thousands of files at once — the
 * change is behavior-preserving and identical everywhere it appears.
 *
 * <p>The modernized counterpart, {@code org.acme.remediation.Modernized}, returns the same elements in the
 * same order using {@code List.of}. The test asserts the two are equal, which is what "automation
 * proposes, tests dispose" means in practice: the recipe's output is verified, never trusted blind.
 */
public final class LegacyReleaseNotes {

    private LegacyReleaseNotes() {
    }

    /**
     * Builds the fixed list of remediation-program milestones, the verbose pre-{@code List.of} way.
     *
     * @return an unmodifiable list of the milestone labels, in order
     */
    // tag::before[]
    public static List<String> milestones() {
        List<String> notes = new ArrayList<>();
        notes.add("baseline the past");
        notes.add("gate new code");
        notes.add("pay down hotspots");
        return Collections.unmodifiableList(notes);
    }
    // end::before[]
}
