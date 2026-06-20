# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` SPDX vs CycloneDX — NEUTRALITY balance. Versions/spec
> editions `⚠ verify at pin` (standards-edition discipline).

---

## Topic
- **Key:** 66 — `01-index/CANDIDATE_POOL.md` · **Title:** Software supply chain — SBOM (CycloneDX/SPDX), provenance, SLSA
- **Part:** VII · **Tier:** B · relates 65/67
- **Primary authorities:** CycloneDX (OWASP/ecma), SPDX (Linux Foundation, ISO/IEC 5962:2021), SLSA (`slsa.dev`), in-toto/Sigstore (cosign); Syft (SBOM gen). Compliance: US EO 14028, EU Cyber Resilience Act.

## 1. Core definition & purpose
Supply-chain attacks (compromised deps, build-system tampering) target what you *ship*, not what you *write*. The defenses: a **SBOM** (Software Bill of Materials — a complete component inventory) so you can answer "am I affected?" in minutes; **provenance/attestation** so you can prove how an artifact was built; and **SLSA**, a maturity framework for build integrity. This chapter frames supply-chain quality and the Java-relevant tooling — increasingly a *compliance requirement*, not optional.

## 2. Mechanism (the spine)
- **SBOM formats (⚠, both legitimate):** **CycloneDX** (OWASP; security-focused — components, services, vulnerabilities, licenses) and **SPDX** (Linux Foundation; **ISO/IEC 5962:2021**; broad licensing/provenance scope). Generate from the build: Maven/Gradle CycloneDX plugins, or **Syft** (outputs both). Consume/monitor with OWASP **Dependency-Track** (key 65).
- **Provenance & attestation:** an **in-toto attestation** records build inputs/steps; **Sigstore cosign** signs artifacts/attestations; GitHub Actions OIDC supplies keyless signing identity. Verify provenance at deploy time.
- **SLSA** (Supply-chain Levels for Software Artifacts): a tiered framework (build integrity levels) — hardened build platform, provenance generation, non-falsifiable attestations. A roadmap, not a single tool.
- **Where it fits the gate:** generate SBOM on build → scan it (key 65) → sign artifact + attest provenance → publish SBOM with the release (key 83). Continuous monitoring re-scans the SBOM as new CVEs land.
- **Compliance drivers:** US **EO 14028** and the **EU Cyber Resilience Act** make SBOMs mandatory in many sectors.

## 3. Evidence FOR
- **SBOM = fast incident response** — "are we affected by CVE-X?" becomes a query, not an archaeology project (the Log4Shell lesson).
- **Provenance/SLSA raise the bar on build tampering** — signed attestations make a swapped artifact detectable.
- **Standardized + increasingly required** — CycloneDX/SPDX are stable standards; compliance mandates make adoption non-optional.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **An SBOM is an inventory, not a defense** — it *enables* response; it doesn't fix anything. A generated-and-ignored SBOM is theatre.
- **Accuracy gaps** — SBOM tools miss dynamically-loaded / shaded / vendored components; the "adherence gap" between standards and tools is documented. Trust but verify completeness.
- **SLSA higher levels are costly** — hardened, hermetic builds + attestation infra are real investment; most teams start low (when-NOT-to-over-invest: a small internal app).
- **Format choice (⚠)** — CycloneDX (security) vs SPDX (licensing/ISO) differ in emphasis; some orgs need both; crown neither.
- **Signing key/identity management** is its own operational burden.

## 5. Current status
CycloneDX + SPDX both current/standardized; SLSA + Sigstore + in-toto the current provenance stack; SBOM generation now compliance-driven (EO 14028, EU CRA). *(Spec editions/versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** generate a CycloneDX SBOM in the Maven build + (concept) sign with cosign; feed the SBOM to a scan (key 65). SBOM artifact produced on `verify`; tag-region snippet of the plugin.
- **Figure:** Fig 66.1 — supply-chain pipeline: build → SBOM (CycloneDX/SPDX) → scan (key 65) → sign + attest (SLSA/cosign) → publish/monitor. Trace to slsa.dev + format specs.

## 7. Gap-filling (verification queue)
- ⚠ SPDX = ISO/IEC 5962:**2021**; CycloneDX spec/ECMA status — confirm against the spec text (standards-edition rule).
- ⚠ SLSA current levels/version; in-toto/cosign specifics — verify at pin.
- ⚠ EO 14028 / EU CRA SBOM-requirement specifics — confirm before asserting scope.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | CycloneDX / SPDX specs | cyclonedx.org ; spdx.dev | ☑ scope; ⚠ edition |
| 2 | SLSA | slsa.dev | ☑ framework; ⚠ levels |
| 3 | Syft / cosign / Dependency-Track | anchore/syft; sigstore; owasp.org | ☑ roles |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | supply-chain 2026 | CycloneDX(security)/SPDX(ISO 5962:2021); Syft both; SLSA via in-toto+cosign+OIDC; EO14028/EU CRA mandates |

---
## Learnings & pipeline suggestions
- Neutral SBOM-format treatment. Keep *scanning* in key 65, *inventory+provenance* here. **Cross-ref:** 65 (SCA), 67 (reproducible builds), 83 (release), 73 (security gate).
