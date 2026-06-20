# SCORECARD — Ch 36 "Release quality" (key 83)

> Part IX CLOSER (Ch 33-36 complete). Single concise dossier. Main-loop; gates = manual passes.
> release-is-shift-right + a-defect-will-slip-limit-the-blast-radius + decouple-deploy-from-release +
> post-release-feedback-closes-the-loop + safe-release≠good-code shapes. Draft: `83_release_quality_v1.md`.
> Pin 2026-06-20. Hand-off opens Part X.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (case-sensitive); canary/blue-green/flags as complementary techniques (no method crowned); DORA framed as evidence not decree; subject-not-comparison (release-process concepts); no products ranked. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (safe-release≠good-code; needs-infra+good-metrics/blind-without-observability; flags=debt-if-not-cleaned; rollback-not-always-clean/migrations; feedback-only-if-acted-on; gates-verify-artifact-not-design; continuous-monitoring-because-release-clean-isn't-permanent) + the deep-dive limit-of-limiting-damage center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | release gates + progressive delivery (canary/blue-green/flags) + post-release feedback + DORA stability from DORA/progressive-delivery refs; SBOM/sign → Part VII; all DORA-bands + canary/flag-tooling + signing specifics carried ⚠ @pin; shift-right/shift-left loop framed; release/infra = artifacts not buildable. |
| C — COMPILE | ⚠ PENDING (process/infra — config artifacts, not a buildable module) | release-checklist + feature-flag snippet spec'd; release/infra artifacts. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the pristine-pipeline-catastrophic-release hook (a logic bug big-bang to 100%) frames the whole; the shift-right framing + the three-parts (gates/progressive-delivery/feedback) structure is crisp; three CONCEPT callouts (decouple-deploy-from-release, regression→fix+test+gate, +the shift-right-loop) anchor a synthesis closer. |
| ACCURACY | 8 | release-gates/canary/blue-green/flags/feedback/DORA all sourced; −2 for the verify-at-pin surface (DORA bands, canary/flag tooling, signing-at-release) — all flagged; safe-release≠good-code + rollback-not-clean + flags-become-debt stated precisely; appropriately a synthesis (defers SBOM/observability/DORA depth). |
| UTILITY | 9 | directly actionable: the release-gate checklist, canary/blue-green/flags to limit blast radius, decouple-deploy-from-release, regression→fix+test+gate loop, backward-compatible migrations for stateful rollback, remove-flags-after-rollout; DORA stability as the measure; a complete release-resilience setup. |
| DEPTH | 8 | the shift-left+shift-right-are-one-cycle synthesis + the deploy/release-decoupling resolving the last speed-vs-stability tension + the limit-of-limiting-damage (damage-control ≠ defect-prevention) is solid senior release material, closing the CI/CD arc on resilience; −2 as the single source dossier is concise Tier-B. |
| READABILITY | 8 | strong catastrophic-release hook, three callouts, the shift-right-closes-the-loop synthesis closing Part IX; 3785w — right for a concise synthesis closer; clean Part IX→X (machine→people) hand-off. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (process/infra artifacts).
New shapes: release-is-shift-right + a-defect-will-slip-limit-the-blast-radius + decouple-deploy-from-release +
post-release-feedback-closes-the-loop + safe-release≠good-code. **CLOSES Part IX (CI/CD & Quality Gates, Ch
33-36 — 4 chapters, all drafted).** Completes the CI/CD arc on resilience; shift-right closes the loop with
the shift-left gates (Ch 33-35). Hands off to Ch 37 (Part X — Process, People & Metrics; code review/standards/
docs, keys 84+86+89). The shift-left+shift-right-are-one-cycle + damage-control≠prevention are the distinctive notes.
