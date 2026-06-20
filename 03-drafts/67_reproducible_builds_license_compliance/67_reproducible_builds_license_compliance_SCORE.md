# SCORECARD — Ch 29 "Reproducible builds & license compliance" (key 67 + 68)

> Part VII CLOSER (Ch 27-29 complete). Two merged dossiers (reproducible-builds leads + license-compliance
> section). Both concise main-loop dossiers; license = factual NOT legal advice (per the book's legal-IP rules).
> Main-loop; gates = manual passes. build-integrity-two-facets + non-determinism-source→fix-table +
> obligation-spectrum-by-distribution-mode + reproducibility-makes-provenance-meaningful + not-legal-advice
> shapes. Draft: `67_reproducible_builds_license_compliance_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; reproducible-build + license tools presented neutrally (no tool crowned); obligation spectrum is factual categorization not a ranking of licenses; permissive/copyleft framed by distribution-context not "good/bad licenses"; each tool/standard cited to its own source. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (repro last-mile-diminishing-returns + toolchain-variance + proves-integrity-not-correctness + decays-silently; license NOT-legal-advice + detection-imperfect + obligation-depends-on-distribution + transitive-surprise + license≠security) + the not-legal-advice CONCEPT + the deep-dive necessary-not-sufficient center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | reproducibility mechanism (outputTimestamp/file-ordering/locale/pinning) + verify-by-rebuild from reproducible-builds.org/Maven; SPDX identifiers + obligation categories + policy-gate tools; all flags/GAVs/SPDX-ids/obligation-summaries carried ⚠ @pin; "not legal advice" stated prominently per legal-IP rules; SLSA-dependence cross-ref Ch 28. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | outputTimestamp + build-twice-diff bit-identical + license-maven-plugin allow-list + THIRD-PARTY NOTICE module spec'd (flagship already pins plugins); not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the provenance-over-a-moving-target hook ties directly to Ch 28 and frames reproducibility's purpose; the two-facets-of-build-integrity (technical + legal) unifies 2 unrelated-looking dossiers; the non-determinism source→fix table + obligation spectrum land cleanly; two CONCEPT callouts (verify-by-rebuild, not-legal-advice) anchor it. |
| ACCURACY | 8 | reproducibility + SPDX/obligation atoms sourced; −2 for the verify-at-pin surface (outputTimestamp/plugin GAVs/Gradle flags, SPDX ids, obligation summaries) — all flagged; "not legal advice" + obligation-depends-on-distribution + AGPL-reaches-SaaS handled precisely; license categories stated factually. |
| UTILITY | 9 | directly actionable: outputTimestamp + pin-plugins + fixed-locale, build-twice-and-diff verify, hermetic for higher SLSA; SPDX-off-the-SBOM, obligation-by-distribution-mode, license policy gate scanning full graph, auto THIRD-PARTY NOTICE; the reproducibility-makes-provenance-meaningful link is a concrete why. |
| DEPTH | 8 | the two-facets-of-one-integrity synthesis + reproducibility-as-culmination-of-pinning (pinned→deterministic→bit-identical→attestable) + license-almost-free-on-the-SBOM is solid senior build-integrity material; −2 vs 9s as both dossiers are concise Tier-B. |
| READABILITY | 8 | strong moving-target hook, source→fix table + obligation spectrum, two callouts, the two-facets deep dive closing Part VII; 3661w — right for two concise dossiers; clean Part VII→VIII (inward to your own code) hand-off. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY). New shapes:
build-integrity-two-facets + non-determinism-source→fix-table + obligation-spectrum-by-distribution-mode +
reproducibility-makes-provenance-meaningful + not-legal-advice. **CLOSES Part VII (Build, Dependencies &
Supply Chain, Ch 27-29 — 3 chapters, all drafted).** Reproducibility-completes-the-pinning-arc (27→28→29);
license rides the SBOM. Hands off to Ch 30 (Part VIII — Security & SAST; secure coding & OWASP, keys 69+72+74).
The "not legal advice" discipline + reproducibility-makes-provenance-meaningful are the chapter's distinctive notes.
