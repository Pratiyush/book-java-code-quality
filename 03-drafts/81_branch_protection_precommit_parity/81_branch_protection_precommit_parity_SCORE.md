# SCORECARD — Ch 35 "Branch protection, trunk-based dev & pre-commit parity" (key 81 + 82)

> Part IX (Ch 33-36). Two merged dossiers (branch-protection/trunk-based/merge-queues leads ⚠ + pre-commit/
> parity section). Both concise main-loop dossiers. Main-loop; gates = manual passes. gate-needs-teeth-and
> -speed + branch-protection-makes-it-unbypassable + trunk-based-demands-trustworthy-gates + merge-queue-keeps
> -main-green + feedback-latency-ladder + hooks-are-feedback-not-enforcement shapes. Draft:
> `81_branch_protection_precommit_parity_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (1 "unlike" → "whereas"); branching strategy explicitly crowns no model (trunk-based DORA-evidenced for delivery, not decreed; GitFlow legit for regulated/OSS/release-train); merge-queue-vs-require-up-to-date as alternatives; IDE/pre-commit/CI as feedback-vs-enforcement not ranked. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (trunk-based-isn't-just-commit-to-main; branching-context-dependent; merge-queues-add-latency/cost; over-strict-protection-backfires; hooks-bypassable+install-dependent; slow-hooks-disabled; parity-hard; onboarding-friction) + the deep-dive enforcement-vs-feedback distinction + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | branch-protection/required-checks + trunk-based (DORA) + merge-queue (GitHub GA) from their docs; pre-commit framework + parity (wrapper + pins) + --no-verify from pre-commit.com/Git; all merge-queue/branch-protection specifics + DORA wording + pre-commit config carried ⚠ @pin; feedback-latency-ladder; CI/git-config = artifacts not buildable. |
| C — COMPILE | ⚠ PENDING (config artifacts, not a buildable module) | branch-protection config + merge-queue note + feature-flag + .pre-commit-config.yaml (Spotless+gitleaks+fast-Checkstyle, parity) spec'd; config artifacts. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the excellent-gate-but-main-red-half-the-time hook (three named failures: no teeth/no collision-safety/no keyboard-speed) frames the whole; the teeth-and-speed structure is crisp; three CONCEPT callouts (trunk-based-demands-trustworthy-gate, feedback-latency-ladder, hooks-are-feedback-not-enforcement) anchor it. |
| ACCURACY | 8 | branch-protection/trunk-based/merge-queue + pre-commit/parity atoms all sourced; −2 for the verify-at-pin surface (merge-queue/branch-protection specifics, DORA wording, pre-commit config) — all flagged; trunk-based-precondition + hooks-feedback-not-enforcement stated precisely; DORA framed as evidenced-not-decreed. |
| UTILITY | 9 | directly actionable: branch-protection required-check recipe, merge-queue for high-merge-rate, trunk-based + feature-flags (given a trustworthy gate), the feedback-latency ladder (push each check leftmost), fast-checks-only-in-the-hook, wrapper+pins for parity, never-rely-on-a-hook-as-the-gate; a complete workflow setup. |
| DEPTH | 8 | the teeth-and-speed-map-onto-the-two-gate-deaths + enforcement-vs-velocity-reconciliation (DORA speed-stability, both-or-neither) + the enforcement-vs-feedback distinction (deliberately-skippable vs deliberately-unbypassable) is solid senior workflow material; −2 vs 9s as both dossiers are concise Tier-B. |
| READABILITY | 8 | strong three-failures hook, the feedback-latency ladder, three callouts, the teeth-and-speed synthesis; 4013w — right for two concise dossiers; clean hand-off to release quality (always-green main → safe releases). |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (config artifacts). New shapes:
gate-needs-teeth-and-speed + branch-protection-makes-it-unbypassable + trunk-based-demands-trustworthy-gates +
merge-queue-keeps-main-green + feedback-latency-ladder + hooks-are-feedback-not-enforcement. Gives the Ch 33/34
gate teeth (unbypassable) + speed (keyboard); resolves enforcement-vs-velocity (teeth+speed both). Hands off to
Ch 36 (release quality, key 83). ⚠ branching row crowns no model (DORA evidenced not decreed). Reuses
shift-left + DORA + feedback-latency disciplines; the enforcement-vs-feedback distinction is the distinctive note.
