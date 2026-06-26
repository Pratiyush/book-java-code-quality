/**
 * Chapter 41 — a small reservation domain for the determinism axis (How Much vs How Good).
 *
 * <p>The chapter's opener maps the testing landscape against two axes: <em>how much</em> of the system
 * runs under test (the pyramid, coverage) and <em>how good</em> the tests are at noticing a wrong
 * answer (fault detection), with a second half on whether the suite's signal can be <em>trusted</em> at
 * all — architecture, flakiness, and test smells. The deep tool mechanics are owned by later chapters
 * (JUnit/AssertJ/Mockito by Chapter 42, integration and property-based testing by Chapter 45, coverage
 * and mutation by Chapter 48). This package gives the one half no later chapter owns — determinism —
 * something concrete to act on.
 *
 * <p>{@link org.acme.testdiscipline.ReservationService} confirms seat reservations. It is shaped to
 * carry exactly the properties the chapter's flaky-to-deterministic matrix targets:
 *
 * <ul>
 *   <li>a <em>time-dependent</em> decision — expiry is computed against an injected
 *       {@link java.time.Clock}, never a direct {@code Instant.now()}, so a test pins time with
 *       {@link java.time.Clock#fixed(java.time.Instant, java.time.ZoneId)};</li>
 *   <li>an <em>unordered collection</em> result — {@link org.acme.testdiscipline.Reservation} holds its
 *       seats in a {@code Set}, so a test asserts membership order-independently rather than depending
 *       on iteration order;</li>
 *   <li>an explicit <em>failure path</em> — a blank or seatless reservation is rejected with a typed
 *       {@link org.acme.testdiscipline.ReservationRejectedException} carrying a stable code.</li>
 * </ul>
 *
 * <p>The test sources put each determinism fix from the chapter's matrix into one runnable place, using
 * only JUnit Jupiter, AssertJ, and the JDK.
 */
package org.acme.testdiscipline;
