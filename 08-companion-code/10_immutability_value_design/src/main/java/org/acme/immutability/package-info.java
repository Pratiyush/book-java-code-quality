/**
 * Chapter 10 — immutability and value-based design (Objects That Don't Change Their Mind).
 *
 * <p>The package gathers the chapter's three instruments of immutability into one buildable module:
 * <strong>records</strong> as transparent value carriers (JEP 395), <strong>immutable
 * collections</strong> ({@code List.of}/{@code Map.ofEntries}/{@code copyOf}), and
 * <strong>defensive copies</strong> (Effective Java Item 50) for the mutable component a record does
 * not freeze on its own. Beside the correct types sit two deliberate counter-examples —
 * {@link org.acme.immutability.OrderLeaky} and {@link org.acme.immutability.BrokenPrice} — kept so a
 * test can prove the failure the prose describes, not merely assert that it would happen.
 *
 * <p>The four {@code Object}/interface contracts the JDK's own collections depend on
 * ({@code equals}, {@code hashCode}, {@code Comparable.compareTo}, {@code toString}) are exercised
 * here too: a record derives the first three correctly by construction, while {@code Comparable}
 * stays the developer's responsibility and is implemented with overflow-safe {@link java.util.Comparator}
 * combinators rather than hand subtraction.
 */
package org.acme.immutability;
