/**
 * Chapter 29 — A Build You Can Stand Behind (reproducible builds and license compliance).
 *
 * <p>The chapter's subject lives in the build configuration around this package: a fixed
 * {@code project.build.outputTimestamp} and the reproducible-build plugin make the artifact a pure
 * function of source, and the {@code license-maven-plugin} reads the dependency tree's licenses and gates
 * on an allow-list while generating a {@code THIRD-PARTY} attribution (all in {@code pom.xml}). This small
 * package exists so the build has a real dependency graph to inventory licenses for, and so the chapter's
 * two facets have an in-code analogue that a test can drive:
 *
 * <ul>
 *   <li><b>Technical integrity</b> — {@link org.acme.repro.ReproducibleArtifact} models a reproducible
 *       build: an artifact identified by its bytes' digest, "verified by rebuilding" (compare the
 *       digests). It proves integrity and determinism, not correctness — a reproducible build of buggy
 *       code is still buggy.</li>
 *   <li><b>Legal integrity</b> — {@link org.acme.repro.LicensePolicy} models the allow/deny gate: it
 *       decides pass/block from declared SPDX licenses against an allow-list tuned to the distribution
 *       mode, scanning the full graph, with {@link org.acme.repro.DisallowedLicenseException} as the
 *       failure path.</li>
 * </ul>
 *
 * <p>The license material here is <strong>factual, not legal advice</strong> (the chapter's prominent
 * caveat): the types report what a license <em>declares</em> and which obligation category it falls into;
 * interpreting a specific obligation for a specific distribution is a question for legal counsel, never a
 * build plugin or a Java type.
 */
package org.acme.repro;
