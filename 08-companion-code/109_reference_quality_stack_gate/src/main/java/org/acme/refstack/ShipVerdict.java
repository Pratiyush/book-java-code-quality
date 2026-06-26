package org.acme.refstack;

import java.util.List;

/**
 * The single verdict the reference gate composes from every stage's outcome: may this change ship, or
 * not? The capstone's point is that the whole stack and the four-stage ladder reduce to one
 * un-ambiguous answer at the merge line — not a wall of per-tool reports a human has to total up, but a
 * composed ship / no-ship decision a required status check can read.
 *
 * <p>There are two variants, and the asymmetry is deliberate. {@code Ship} means no stage raised a
 * blocking finding under the active ladder; it does <em>not</em> mean the code is good — the stack
 * catches the mechanical, never the substantive, and a clean gate is the floor a human review builds on,
 * not the ceiling (Chapters 37, 1). {@code NoShip} carries the blocking stage outcomes so the failure is
 * actionable: a developer sees which stage blocked and why, not a bare red mark.
 */
// tag::ship-verdict[]
public sealed interface ShipVerdict permits ShipVerdict.Ship, ShipVerdict.NoShip {

    /** No blocking finding under the active ladder — the mechanical floor is clear, human review decides the rest. */
    record Ship(String reason) implements ShipVerdict { }

    /** One or more stages raised a blocking finding — the change does not ship until they are addressed. */
    record NoShip(String reason, List<StageOutcome> blocking) implements ShipVerdict {
    // end::ship-verdict[]

        /**
         * Compact constructor: the blocking list is defensively copied to an immutable list (Effective
         * Java, Item 50), so the verdict cannot be mutated through the reference the caller passed in and
         * its accessor cannot hand out a mutable internal list. A verdict is a fact about a run; it never
         * changes after it is composed.
         */
        public NoShip {
            blocking = List.copyOf(blocking);
        }
    }
}
