# SCORECARD — Ch 28 "Dependency scanning, SBOM & supply-chain security" (key 65 + 66)

> Part VII (Ch 27-29). Two merged dossiers (SCA/vuln-scanning leads ⚠ + SBOM/supply-chain section ⚠). Both
> concise main-loop dossiers. Main-loop; gates = manual passes. SCA-vs-SAST-distinction + three-questions
> -about-other-peoples-code + multi-tool-crown-none + two-SBOM-formats-crown-none + inventory-not-defense +
> vulnerable≠exploitable shapes. Draft: `65_dependency_scanning_sbom_supply_chain_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (1 "worse than" reworded); 5 SCA tools as different approaches (table, "crown none"); CycloneDX vs SPDX "both legitimate, crown neither, some need both"; SLSA framed as a ladder not a product; the three questions are complementary not ranked; each tool/standard cited to its own source. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (SCA-known-only; false-positives-erode-gate; vulnerable≠exploitable; DB-lag/coverage; SBOM-inventory-not-defense; SBOM-accuracy-gaps; SLSA-higher-levels-costly; format+signing-overhead) + the three-CONCEPT honest edges + the deep-dive "necessary-not-sufficient, enables-not-fixes" center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | SCA mechanism (CPE/NVD/OSV/GitHub Advisory) + tool roles; CycloneDX/SPDX (ISO/IEC 5962:2021) + Syft/Dependency-Track; in-toto/cosign/OIDC + SLSA; EO 14028 + EU CRA; all tool versions/GAVs/CVSS-config + SPDX-edition + SLSA-levels + compliance-specifics carried ⚠ @pin (standards-edition discipline); SCA-vs-SAST distinction explicit; scan network-gated → REPRO PENDING-RUNTIME. |
| C — COMPILE | ⚠ PENDING (toolchain READY; scan needs network/DB → REPRO PENDING-RUNTIME) | OWASP Dependency-Check fail-on-vuln + reviewed suppression + CycloneDX SBOM module spec'd; scan needs DB download; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the Log4Shell "where are we using it?" hook frames the whole; the three-questions structure (known-vulnerable?/what's-in-it?/can-I-prove-it?) organizes 2 dossiers cleanly; three CONCEPT callouts (point-in-time-not-enough, inventory-not-defense, +chain) + the inventory→scan→attest chain deep dive anchor it. |
| ACCURACY | 8 | SCA/SBOM/provenance/SLSA atoms all sourced; −2 for the broad verify-at-pin surface (all tool versions, SPDX ISO edition, SLSA levels, EO-14028/EU-CRA specifics) — all flagged with standards-edition discipline; vulnerable≠exploitable + SBOM-accuracy-gaps stated precisely. |
| UTILITY | 9 | directly actionable: SCA-gate + continuous-monitoring + bot-remediation; suppress-FPs-with-justification; triage-by-reachability-not-first-come; generate-SBOM-every-build; provenance-attest-verify-at-deploy; SLSA-start-low-climb; the Log4Shell-answer-is-a-query is a concrete readiness target. |
| DEPTH | 8 | the inventory→scan→attest chain (each enables the next) + determinism-is-the-precondition-for-securability + enables-not-fixes is solid senior supply-chain material; the SCA-vs-SAST boundary is drawn cleanly; −2 vs 9s as both source dossiers are concise Tier-B. |
| READABILITY | 8 | strong Log4Shell hook, two tables (SCA tools + implicit formats), three callouts, the three-questions-one-chain synthesis; 3861w — right for two concise dossiers; clean hand-off to reproducible builds (provenance needs reproducibility). |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; scan
network-gated → REPRO PENDING-RUNTIME). New shapes: SCA-vs-SAST-distinction + three-questions-about-other
-peoples-code + multi-tool-crown-none + two-SBOM-formats-crown-none + inventory-not-defense + vulnerable≠
exploitable. Builds on Ch 27 (deterministic tree = precondition); hands off to Ch 29 (reproducible builds &
license compliance — provenance needs reproducibility; SBOM also inventories licenses, keys 67+68). Two ⚠ rows
(SCA multi-tool; SPDX/CycloneDX) both crown-neither. Log4Shell as the load-bearing motivating incident.
