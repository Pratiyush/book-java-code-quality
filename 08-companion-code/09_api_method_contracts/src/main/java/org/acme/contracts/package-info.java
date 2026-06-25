/**
 * Chapter 09 — a contract-tight money-transfer domain (A Method Is a Promise).
 *
 * <p>The whole package is {@link org.jspecify.annotations.NullMarked}: every type, parameter and
 * return in this scope is non-null unless a member is explicitly annotated
 * {@link org.jspecify.annotations.Nullable}. Marking the package once moves "this is never null"
 * out of prose and into the signature, where a conforming checker (NullAway, the Checker Framework,
 * the IDE) can verify it. JSpecify is a specification, not a checker, so the annotation states the
 * contract; the tooling that enforces it is Chapter 11's subject.
 */
// tag::nullness-marked[]
@NullMarked
package org.acme.contracts;

import org.jspecify.annotations.NullMarked;
// end::nullness-marked[]
