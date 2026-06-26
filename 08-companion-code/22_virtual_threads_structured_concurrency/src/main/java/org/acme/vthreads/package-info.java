/**
 * Chapter 22 — virtual threads, the pinning trap, and verifying concurrent code (Cheap Threads, Same
 * Rules).
 *
 * <p>The package gathers the chapter's virtual-thread surfaces in one buildable module on the Java 21
 * anchor (JEP 444, GA): a {@code newVirtualThreadPerTaskExecutor()} I/O fan-out (the stable surface),
 * the pinning trap ({@code synchronized} around a blocking call, which pins the carrier on Java 21)
 * beside its {@code ReentrantLock} fix, a deliberately inconsistently-synchronized counter whose
 * unguarded read races even on virtual threads, the guarded counter that closes the race, and a
 * deterministic latch-driven harness that exposes the race repeatably.
 *
 * <p>Two status facts the chapter keeps separate are reflected in what this package does and does not
 * contain. Virtual threads are <em>GA</em> at the anchor, so they are used directly. Structured
 * concurrency is <em>preview</em> through Java 25 (JEP 453 through JEP 505) and its API changed shape
 * across previews, so no type here depends on {@code StructuredTaskScope}; the structured-concurrency
 * concept is shown with stable APIs instead, and the preview API is flagged in the prose, not compiled.
 * On Java 24 and later, JEP 491 removed {@code synchronized} pinning; the pinning demonstration here is
 * therefore dated to the Java 21 anchor it is built on.
 */
package org.acme.vthreads;
