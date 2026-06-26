/**
 * Chapter 28 — Knowing What You Ship.
 *
 * <p>The chapter's subject lives in the build configuration around this package (the CycloneDX SBOM
 * generator and the OWASP Dependency-Check gate in {@code pom.xml}, the CI stage in {@code ci/}). This
 * small package exists so the build has a real dependency graph to inventory and scan, and so the
 * chapter's three questions have an in-code analogue that a test can drive:
 *
 * <ul>
 *   <li><b>Do I know what's in it?</b> — {@link org.acme.supplychain.ComponentInventory} models the SBOM:
 *       a complete component list that turns "am I affected by CVE-X?" into a query. It is an inventory,
 *       not a defense — it makes a team fast, it fixes nothing.</li>
 *   <li><b>Is anything known-vulnerable?</b> — {@link org.acme.supplychain.VulnerabilityGate} models SCA:
 *       it decides pass/block from findings against a CVSS threshold, honours reviewed suppressions, and
 *       separates "vulnerable" from "exploitable" (an unreachable finding is triaged, not a fire).</li>
 * </ul>
 *
 * <p>The third question (can I trust how it was built? — provenance/SLSA) is described in the chapter and
 * the README as a maturity ladder; it needs signing/attestation infrastructure this illustrative module
 * does not stand up, so it is not faked in code.
 */
package org.acme.supplychain;
