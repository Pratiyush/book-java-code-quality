package org.acme.remediation;

import java.util.List;

/**
 * The "after" form (Chapter 40, the automation engine) — the result a modernization recipe produces from
 * {@code org.acme.remediation.legacy.LegacyReleaseNotes}. The verbose mutable-list-then-wrap idiom collapses
 * to a single {@link List#of} call: the same elements, the same order, an immutable list, fewer lines and
 * fewer imports. The transformation is behavior-preserving, which is why a recipe can apply it everywhere it
 * appears without changing what the program does.
 *
 * <p>Both forms ship in this module on purpose. The before form is what a recipe scans for; this is what it
 * writes; the test asserts they are equal. That equality is the verification the chapter insists on — the
 * recipe's diff is a pull request to confirm, not a change to trust unread.
 */
public final class Modernized {

    private Modernized() {
    }

    /**
     * The modernized equivalent of the legacy milestone list — same elements and order, built immutably.
     *
     * @return an immutable list of the milestone labels, in order
     */
    // tag::after[]
    public static List<String> milestones() {
        return List.of(
                "baseline the past",
                "gate new code",
                "pay down hotspots");
    }
    // end::after[]
}
