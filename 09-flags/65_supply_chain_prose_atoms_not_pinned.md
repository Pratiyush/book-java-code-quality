# FLAG — key 65 (VERIFY): supply-chain prose atoms NOT in the SOURCE-PIN table → left marked, not asserted as pinned

**Raised:** 2026-06-27 (Chapter 28 / key 65 — deferred-marker resolution at SOURCE-VERIFY)
**Severity:** material (security/legal content — must be factual, never asserted beyond the pin)
**Cross-ref:** `09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md` (the cyclonedx-maven-PLUGIN version + live-scan REPRO, filed at EXAMPLE-BUILD).

## Context — what WAS confirmed at this pass (so this flag is only the residue)

Against SOURCE-PIN.md §4 (re-pinned **2026-06-27**) + the BUILT module
`08-companion-code/65_dependency_scanning_sbom_supply_chain/`, these atoms are CONFIRMED and their
deferred markers were removed from the draft:

| Atom | Pin / evidence |
|---|---|
| CycloneDX **1.6** SBOM spec | SOURCE-PIN §4; verified in the built `target/bom.json` (`"specVersion": "1.6"`, `"bomFormat": "CycloneDX"`, serial number + `commons-lang3 3.18.0` purl) |
| OWASP **Dependency-Check 12.2.2** | SOURCE-PIN §4; resolved in the green build (`dependency-check:12.2.2:check`); `failBuildOnCVSS` + `suppressionFiles/suppressionFile` config parses |
| **SPDX = ISO/IEC 5962:2021** | SOURCE-PIN §4 (exact) |
| **SLSA v1.0** (build/source/dependencies tracks; roadmap-not-tool) | SOURCE-PIN §4 |
| CycloneDX (OWASP, security-focused) vs SPDX (Linux Foundation, licensing/provenance) | SOURCE-PIN §4 |
| **Grype** (Anchore) / **Trivy** (Aqua) / **Snyk** (commercial, SaaS) vendor+line rows | SOURCE-PIN §4 (`Grype ~0.108–0.110` / `Trivy 0.71.0` / Snyk rolling) |

## The residual atoms — NOT pinned, LEFT marked in the draft, not stated as pinned fact

These are named in the chapter's prose at the **concept/mechanism level only** (no version literal, no API
signature, no quoted legal text), which is defensible as factual description. They are **not** in the
SOURCE-PIN table and so cannot be traced to a pinned identifier; the draft keeps a `⚠ @pin` marker on each
and asserts none as a pinned fact:

1. **`org.cyclonedx:cyclonedx-maven-plugin` version literal** (`2.9.2` used in the module) — only the
   *spec* (1.6) is pinned. Full detail in the cross-ref flag. At `/pin-source`, add a plugin-version row to
   SOURCE-PIN §4 beside the "CycloneDX 1.6" spec pin (same plugin/engine two-pin split as Checkstyle).
2. **Syft** — named as a generator that "outputs both formats". SOURCE-PIN names Syft only in the Grype row
   context (Grype pairs with Syft); there is **no own Syft version pin**. Prose-level only; no version
   asserted.
3. **OWASP Dependency-Track** — named as a server that consumes CycloneDX SBOMs for continuous monitoring.
   **No version pin** in SOURCE-PIN. Prose-level only.
4. **Snyk exact version** — SOURCE-PIN marks Snyk SaaS/rolling (no fixed version). Dated-at-use; vendor +
   "commercial, reachability in some tiers" is confirmed, the exact build is not.
5. **Provenance toolchain specifics — in-toto attestation / Sigstore cosign / GitHub Actions OIDC** — named
   as the provenance/attestation mechanism. **None is a SOURCE-PIN row.** The prose stays at the mechanism
   level ("records the build's inputs and steps", "signs the artifact", "keyless signing") — no version, no
   command flags, no API. The companion module does NOT stand up signing/attestation (described, not faked).
6. ~~**US Executive Order 14028 / EU Cyber Resilience Act**~~ — **✅ RESOLVED 2026-06-28** (web-verified +
   pinned). Both are now SOURCE-PIN **§4a** rows: **EO 14028** (signed 2021-05-12; 86 FR 26633; §4(e) directs
   agencies to require vendor SBOMs — federalregister.gov/d/2021-10460 + nist.gov) and the **EU CRA**
   (Regulation (EU) 2024/2847, in force 2024-12-10, main obligations 2027-12-11; **Annex I, Part II** SBOM
   "covering at the very least the top-level dependencies", recitals 22 + 77 — eur-lex.europa.eu/eli/reg/2024/2847/oj).
   The draft now cites both with dated identifiers, **dated-at-use + factual-not-legal-advice** (mirroring
   Ch 29 / `LEGAL-IP-RULES.md`); scope/obligations/timelines still vary by jurisdiction → counsel, not the book.
   Also resolved at the same pass: **CycloneDX 1.6** (released 2024-04-09, ECMA-424), **SPDX = ISO/IEC 5962:2021**,
   and **SLSA v1.0** level definitions (Build L0–L3; "Supply-chain Levels for Software Artifacts") — see §4a +
   atom #7 below.
7. **SLSA exact level definitions / per-track level numbers** — SLSA v1.0 and its build/source/dependencies
   tracks are pinned; the chapter describes the ladder qualitatively ("provenance exists" up to "hardened,
   non-falsifiable, hermetic builds") and does NOT assert specific numbered level requirements, which would
   need the slsa.dev/spec/v1.0 text checked clause-by-clause.
8. **NVD / OSV / GitHub Advisory roles + CPE-matching exact wording** — mechanism-level (these are
   vulnerability *databases*, not pinned-version authorities). The roles described (NVD = CVEs, OSV =
   open-source-focused, GitHub Advisory; CPE-based fuzzy matching → false positives) are standard and
   accurate; exact phrasing is not traced to a pinned doc snapshot.

## REPRO boundary (carried, by design)

The **live OWASP Dependency-Check scan verdict** is **REPRO PENDING-RUNTIME**: the scan downloads the NVD
database over the network on first run, so an offline build cannot produce the scan result. It is wired as
an opt-in `-Pscan` profile, never in the default or `-Pquality` build; the gating build is offline +
deterministic (SBOM generation + static analysis are fully offline and built green).

## Disposition

- **Do not** invent a version, clause, or signature for any residual atom.
- Prose stays at the mechanism/concept level where these appear; no atom is asserted as a pinned fact.
- At the next `/pin-source`, decide whether to add SOURCE-PIN rows for Syft, Dependency-Track, the
  cyclonedx-maven-plugin, and (optionally) a dated reference for EO 14028 / EU CRA + the SLSA level text;
  then re-trace.

**Filed by:** source-verifier, Chapter 28 (key 65), deferred-marker resolution (2026-06-27).
