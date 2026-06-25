/**
 * Chapter 6 — naming, structure and formatting made buildable (the readability fundamentals).
 *
 * <p>The package is the chapter's spine turned into one module: a tool settles the typography a regex
 * can decide (the {@code case} of an identifier, the whitespace, the import order), and a human still
 * settles the meaning a regex cannot reach (whether the name is true). {@link OrderLine} is the
 * conventionally-named, conventionally-formatted result; {@link ReadabilityNotes} records the
 * before-state a reviewer would stall on and exposes the module's failure path. The naming case is
 * machine-checked by the {@code quality} profile (Checkstyle naming modules + Spotless reformat),
 * which is the enforcement surface; the semantic rename is the part only a person can do.
 */
package org.acme.storefront.readability;
