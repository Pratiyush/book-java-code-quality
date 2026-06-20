# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Legal nuance — present factually, not as legal advice;
> versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 68 — `01-index/CANDIDATE_POOL.md` · **Title:** License compliance as a quality concern
- **Part:** VII · **Tier:** B · relates 65/66
- **Primary authorities:** SPDX license list (`spdx.org/licenses`); license-scanning tools — `license-maven-plugin`, Gradle license plugins, FOSSA/ScanCode; SBOM license data (CycloneDX/SPDX, key 66).

## 1. Core definition & purpose
Every dependency carries a license, and the wrong license in the wrong place is a real liability (copyleft obligations, redistribution restrictions, attribution requirements). License compliance is a *quality of the dependency graph*: knowing what you depend on legally, catching policy violations automatically, and producing the attributions you owe. It rides the same machinery as SCA (key 65) and SBOM (key 66) — the component inventory already exists; license compliance reads the license field.

## 2. Mechanism (the spine)
- **Identify licenses:** every component's license, normalized to **SPDX identifiers** (e.g. `Apache-2.0`, `MIT`, `GPL-3.0-only`, `LGPL-2.1`); SBOMs (key 66) carry this; license plugins extract it from POM metadata / scan source.
- **Categorize by obligation:** permissive (Apache/MIT/BSD — attribution only), weak copyleft (LGPL/MPL/EPL — file/library-level share-alike), strong copyleft (GPL/AGPL — derivative-work obligations). The risk depends on *how you distribute* (SaaS vs shipped binary — AGPL notably reaches network use).
- **Policy gate:** define an allow/deny policy (e.g. ban GPL in a proprietary shipped product); the `license-maven-plugin` (or FOSSA/ScanCode) checks dependency licenses against it and **fails the build** on a violation — a fitness function (key 56) for legal risk.
- **Produce attributions:** generate the THIRD-PARTY/NOTICE file (aggregated licenses) the permissive licenses require — automatable from the same data.
- **Where it runs:** build/CI gate + the SBOM's license section for downstream consumers.

## 3. Evidence FOR
- **Automatable from existing inventory** — once you have an SBOM/SCA (keys 65/66), license checking is nearly free incremental value.
- **Catches policy violations before shipping** — far cheaper than discovering a GPL dependency in a proprietary product post-release.
- **SPDX standardization** makes licenses machine-comparable; attribution files generate automatically.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Not legal advice** — tools detect declared licenses; they don't interpret your specific obligations. License *strategy* needs legal counsel; the book stays factual and says so.
- **Detection is imperfect** — missing/ambiguous/multi-license/relicensed components, and dual-licensed deps, confuse scanners; POM-declared license ≠ actual license sometimes. Verify high-risk findings.
- **Obligation depends on use** — the same license is fine in one context (internal tool) and a problem in another (redistributed SDK); a blanket deny-list can block harmless deps (tune the policy).
- **Transitive surprises** — a permissive direct dep can pull a copyleft transitive; scan the full graph (key 63).
- **License ≠ security** — a permissively-licensed dependency can still be vulnerable (key 65) or unmaintained.

## 5. Current status
SPDX license identifiers are the standard; license-maven-plugin / Gradle plugins / FOSSA / ScanCode current; SBOM-driven license compliance growing with supply-chain mandates (key 66). *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** `license-maven-plugin` enforcing an allow-list (fail on a banned license) + generating a THIRD-PARTY notice file. Built green; tag-region snippet of the policy.
- **Figure:** Fig 68.1 — license obligation spectrum (permissive → weak copyleft → strong copyleft) mapped to distribution mode (internal/SaaS/shipped) and the resulting risk. Trace to SPDX + license texts (factual, not advice).

## 7. Gap-filling (verification queue)
- ⚠ SPDX identifiers + license-plugin GAV/config — verify at pin.
- ⚠ Obligation summaries (LGPL/MPL/GPL/AGPL) — state factually + flag as "not legal advice"; confirm wording against the license texts/SPDX.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | SPDX license list | spdx.org/licenses | ☑ identifiers |
| 2 | license-maven-plugin / FOSSA / ScanCode | respective docs | ☑ roles; ⚠ version |
| 3 | SBOM license data (CycloneDX/SPDX) | (key 66) | ☑ cross-ref |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (supply-chain + synthesis) | SPDX ids; SBOM carries licenses; permissive/copyleft categories; policy-gate tools |

---
## Learnings & pipeline suggestions
- Flag "**not legal advice**" prominently (consistent with never-invent/honesty). Reuses SBOM/SCA inventory. **Cross-ref:** 63 (graph), 65 (SCA), 66 (SBOM), 56 (gate), LEGAL-IP-RULES.md (the book's own IP rules).
