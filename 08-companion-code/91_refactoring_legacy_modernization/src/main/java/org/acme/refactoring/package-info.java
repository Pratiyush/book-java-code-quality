/**
 * Chapter 39 — changing code without breaking it (refactoring, legacy code, the strangler fig).
 *
 * <p>The package turns the chapter's one invariant — <strong>preserve behaviour, verify it with tests,
 * move in small steps</strong> — into one buildable module, at two of the four scales the chapter
 * teaches and a sketch of a third:
 *
 * <ul>
 *   <li><strong>Getting legacy code under test.</strong> {@link org.acme.refactoring.LegacyShippingCalculator}
 *       {@code new}s up its own rate source and prices in loose primitives — code that, as written, has
 *       no seam for a test. {@link org.acme.refactoring.RateTable} is extracted as a seam (Feathers'
 *       <em>Extract Interface</em>) and a parameterized constructor (Feathers' <em>Parameterize
 *       Constructor</em>) lets a test inject a known rate, all behaviour-preserving. A characterization
 *       test then pins the legacy method's current behaviour — including a faithfully-reproduced
 *       rounding quirk — because a characterization test captures what the code <em>does</em>, not what
 *       it <em>should</em> do.</li>
 *   <li><strong>Refactoring under the net.</strong> {@link org.acme.refactoring.ShippingCalculator} is
 *       the modernized refactor: the same result expressed with a record input
 *       ({@link org.acme.refactoring.Parcel}), a {@link org.acme.refactoring.ServiceLevel} enum, a
 *       sealed {@link org.acme.refactoring.Quote} hierarchy, {@code Optional}, and streams — the
 *       modern-Java idioms (Chapter 5) that supersede some 2018-era manual catalog steps. A
 *       behaviour-preservation test proves it returns the identical charge as the legacy method for
 *       every parcel, quirk and all.</li>
 *   <li><strong>Strangling a system.</strong> {@link org.acme.refactoring.StranglerRouter} is a routing
 *       façade that sends each request to the legacy or the modern path behind a flag — the incremental
 *       cutover at system scale, with the flag as the rollback lever.</li>
 * </ul>
 *
 * <p>The package carries the chapter's honesty about safe change: the characterization test pins
 * <em>current</em> behaviour including a bug-shaped quirk (a net, not a specification — review the
 * captured behaviour); modernizing the idiom is the refactor while changing the rounding would be a
 * behaviour change wearing the wrong hat; and a strangler must be finished, because a half-strangled
 * stall maintains two systems and a router forever. The named canon (Fowler, Feathers) is read through
 * a modern-Java lens, and which idioms the language now supersedes is verified at the pin, not asserted.
 */
package org.acme.refactoring;
