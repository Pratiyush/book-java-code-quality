/**
 * Chapter 13 — thread-safety and the Java Memory Model, made buildable (The Bug That Passes Every
 * Test).
 *
 * <p>The package holds one deliberately data-racy counter — the bug that passes every test — beside
 * the corrected forms, so a test can prove the lost update the prose describes rather than merely
 * assert it. Each correct type establishes one of the happens-before edges the JLS guarantees
 * (SE 21 &sect;17.4.5): {@link org.acme.concurrency.SynchronizedCounter} the monitor edge,
 * {@link org.acme.concurrency.AtomicCounter} a compare-and-swap via {@code java.util.concurrent},
 * {@link org.acme.concurrency.ServiceConfiguration} the {@code final}-field publication guarantee
 * (&sect;17.5), and {@link org.acme.concurrency.LazyResource} the initialization-on-demand holder
 * idiom that takes laziness and safety from class-initialization locking.
 *
 * <p>The racy counter is the module's single reviewed SpotBugs suppression: its
 * {@code VO_VOLATILE_INCREMENT} finding (with the broader
 * {@code AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE} the pinned SpotBugs also raises for
 * {@code volatile++}) is the lesson, named in the exclude filter and pointed at the test that
 * demonstrates it, never disabled wholesale. Every other type stays clean. The module is JDK-only at
 * runtime apart from the one CLASS-retained {@code @GuardedBy} annotation.
 */
package org.acme.concurrency;
