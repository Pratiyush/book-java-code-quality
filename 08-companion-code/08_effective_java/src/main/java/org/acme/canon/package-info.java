/**
 * Chapter 05 — the load-bearing Effective Java idioms read forward into Java 21 (The Canon, Dated).
 *
 * <p>Each type in this package is one idiom from the canon, carrying the verdict the chapter assigns
 * it: {@link org.acme.canon.LegacyPoint} is the hand-written immutable value class (the boilerplate
 * records retired); {@link org.acme.canon.Point} is the same data as a one-line record (JEP 395),
 * the <em>served by a feature</em> verdict; {@link org.acme.canon.Temperature} is a record whose
 * compact constructor still enforces an invariant, the nuance that records do not retire Item 17;
 * {@link org.acme.canon.PricingPolicy} is a single-element enum singleton (Item 3), <em>stands</em>;
 * and {@link org.acme.canon.Shape} plus {@link org.acme.canon.Areas} are a sealed hierarchy (JEP
 * 409) handled by an exhaustive pattern-matching switch (JEP 441), the <em>reinforced</em> verdict.
 *
 * <p>The named book is a secondary authority (SOURCE-PIN §7): where a JEP has overtaken a 3rd-edition
 * idiom, the primary source dates it. The principles are kept; the idioms are updated.
 */
package org.acme.canon;
