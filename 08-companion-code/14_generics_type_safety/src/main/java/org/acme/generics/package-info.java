/**
 * Chapter 14 — type-safety the compiler enforces (Let the Compiler Carry It).
 *
 * <p>The package gathers the chapter's type-safety craft into runnable code. {@link
 * org.acme.generics.Stack} is a generic container whose two bulk operations show the variance
 * mnemonic PECS — Producer-Extends, Consumer-Super (Effective Java Item 31): {@code pushAll} reads
 * from a producer typed {@code Iterable<? extends E>}, and {@code popAll} writes into a consumer typed
 * {@code Collection<? super E>}. Erasure forces exactly one cast — creating the backing array of a
 * non-reifiable type — and that single unprovable spot is discharged with a narrowest-scope {@link
 * java.lang.SuppressWarnings} carrying a proof comment (Item 27), not spread across the class.
 *
 * <p>{@link org.acme.generics.VarargsHeapPollution} carries the chapter's earned-assertion lesson
 * (Item 32): a genuinely safe varargs method annotated {@code @SafeVarargs} beside a deliberately
 * unsafe one that leaks the generic array, whose resulting heap pollution a test proves rather than
 * merely describes. The annotation is "I have verified this is safe," never "make the warning go
 * away" — so the unsafe method is left unannotated and the compiler's warning stands.
 *
 * <p>Nothing here re-checks types at run time; the safety is a build-time property the compiler and
 * the {@code -Pquality} analyzers assert. The aggregator compiles with {@code -Xlint:all}, so a raw
 * type, an unchecked conversion, or a varargs leak surfaces as a warning during the build — the
 * chapter's "health surface" for type-safety, in the absence of a runtime endpoint.
 */
package org.acme.generics;
