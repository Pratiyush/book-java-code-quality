/**
 * The same orders/notify collaboration as {@code org.acme.design.cycle}, with the cycle broken by a
 * dependency inversion.
 *
 * <p>The stable {@code orders} package now owns a small {@code OrderEvents} abstraction and depends
 * only on it. The {@code notify} package implements that interface, so the dependency runs one way —
 * {@code notify} depends on {@code orders}, never the reverse. Both packages can now be built and
 * tested on their own. This is DIP used for what the chapter says it is for: inverting a dependency
 * that pointed the wrong way, not adding an interface for its own sake.
 */
package org.acme.design.inverted;
