/**
 * A two-package dependency cycle — the cardinal sin the chapter names — kept small enough to read.
 *
 * <p>The {@code orders} sub-package depends on {@code notify} to announce a placed order, and
 * {@code notify} depends back on {@code orders} to read the order's summary. Each package needs the
 * other to compile, so neither can be understood, tested, or shipped on its own: the two are one
 * indivisible blob. Compare {@code org.acme.design.inverted}, where a single owned abstraction breaks
 * the loop. Detecting and gating such a cycle (ArchUnit, JDepend) is the next chapter's subject; this
 * package only makes the shape concrete.
 */
package org.acme.design.cycle;
