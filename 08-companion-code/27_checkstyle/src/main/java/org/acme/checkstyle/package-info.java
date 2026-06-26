/**
 * Chapter 27 — encoding a written standard as a gate (Checkstyle).
 *
 * <p>This package is a small storefront slice held to the curated house ruleset in
 * {@code config/checkstyle/checkstyle.xml}. The ruleset's rule blocks are the chapter's displayed
 * snippets (tag regions in that XML), and this Java is what those rules govern, written so the module
 * passes its own gate. The point the chapter argues is shown two ways:
 *
 * <ul>
 *   <li><strong>A style-clean module is consistently formatted, not thereby correct.</strong> Every
 *       type here satisfies the naming, import-hygiene, brace, and size rules, yet the same module is
 *       also held to a SpotBugs gate — Checkstyle reads one source file with no type information and
 *       cannot, in its own docs, "determine the type of an expression" or "the full inheritance
 *       hierarchy of type," so passing it is necessary, never sufficient.</li>
 *   <li><strong>Suppress with a reason, never disable the check.</strong>
 *       {@link org.acme.checkstyle.PriceFormatter} carries a per-site
 *       {@code @SuppressWarnings("checkstyle:...")} that records why one local exception is reviewed,
 *       honoured by the {@code SuppressWarningsFilter} wired on the {@code Checker}.</li>
 * </ul>
 *
 * <p>{@link org.acme.checkstyle.CatalogItem} is the record whose component names the
 * {@code RecordComponentName} rule governs; {@link org.acme.checkstyle.PricingRules} holds the
 * {@code UPPER_SNAKE} constants the {@code ConstantName} rule governs; {@link org.acme.checkstyle.Catalog}
 * is the read surface tying them together. No tool is crowned here — Checkstyle owns the style/convention
 * layer, and the cross-tool composition is Chapter 17's.
 */
package org.acme.checkstyle;
