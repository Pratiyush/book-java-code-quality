/**
 * Chapter 11 — a null-safe storefront pricing domain (The Value That Isn't There).
 *
 * <p>The whole package is {@link org.jspecify.annotations.NullMarked}: every type, parameter and
 * return in this scope is non-null unless a member is explicitly annotated
 * {@link org.jspecify.annotations.Nullable}. Marking the package once flips the default from "any
 * reference might be null" to "no reference is null here," so the few places null is genuinely
 * allowed have to declare it — the contract moves out of prose and into the signature, where a
 * conforming checker (NullAway, the Checker Framework, the IDE) can verify it. JSpecify is a
 * specification, not a checker: the annotation states the contract, and the tool that enforces it
 * is wired into the build per team. Build-time proof is the chapter's third lever.
 */
// tag::nullmarked-package[]
@NullMarked
package org.acme.storefront.pricing;

import org.jspecify.annotations.NullMarked;
// end::nullmarked-package[]
