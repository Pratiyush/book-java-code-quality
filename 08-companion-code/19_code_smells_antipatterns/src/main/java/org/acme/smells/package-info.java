/**
 * Chapter 19 — code smells and Java anti-patterns (Names for What's Wrong).
 *
 * <p>The package turns the chapter's organizing unit — the <strong>smell &rarr; refactoring &rarr;
 * detecting-rule</strong> triple — into one buildable module. Two worked smells ship as matched
 * before/after pairs so a test can prove the refactoring changes structure without changing
 * behaviour:
 *
 * <ul>
 *   <li><strong>Long Method.</strong> {@link org.acme.smells.OrderServiceSmelly#placeOrder} runs long
 *       and nests several conditionals deep — the shape Sonar {@code java:S3776} (cognitive
 *       complexity) and PMD {@code NcssCount} measure. {@link org.acme.smells.OrderService#placeOrder}
 *       resolves it with Fowler's <em>Extract Function</em> and guard clauses, one named step per
 *       concern.</li>
 *   <li><strong>Exposing internal representation.</strong> {@link org.acme.smells.OrderLeaky} stores
 *       and returns its mutable {@code List<LineItem>} directly — the smell SpotBugs reports as
 *       {@code EI_EXPOSE_REP} (on the leaking accessor; {@code EI_EXPOSE_REP2} is the store-side
 *       pattern), which is also a latent mutation bug. {@link org.acme.smells.Order} closes it with the
 *       defensive copy of Effective Java Item 50 ({@code List.copyOf}).</li>
 * </ul>
 *
 * <p>The pairing carries the chapter's honesty about detection: the representation leak is found by a
 * tool and is a real bug, so the build reports it (via a single reviewed suppression on the
 * counter-example); the Long Method is a metric smell whose threshold is a convention tools disagree
 * on, and the house Checkstyle/SpotBugs gate here does not measure it at all — which is the chapter's
 * point that different tools see different smells, shown rather than asserted. A pattern or refactoring
 * is justified by the problem it solves, never by its name.
 */
package org.acme.smells;
