# SCORECARD — Ch 39 "Refactoring, legacy code & modernization" (key 91 + 92 + 93 + 95)

> Part XI OPENER. FOUR merged dossiers (refactoring leads + legacy/seams + strangler-fig + version-migration).
> Tier-A; the craft Part X measured but didn't perform. Main-loop; gates = manual passes. preserve-behavior+
> test-backed+incremental (the one invariant) + ladder-of-scale + two-hats + legacy=no-tests-characterize-first
> + characterization-pins-bugs + strangler-vs-big-bang + migration≠modernization + canon-dating shapes. Draft:
> `91_refactoring_legacy_modernization_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (3 "beat" reworded); in-place-vs-strangler-vs-rewrite framed as a scale ladder (rewrite not forbidden, just argued-against at scale); canon (Fowler/Feathers) read through modern-Java lens not crowned; no tool/approach crowned; the big-bang is named as the recurring anti-pattern, not a strawman to crown against. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (refactoring-without-tests=editing; don't-mix-hats; big-refactoring-is-redesign; characterization-pins-bugs; strangler-stalls-half-done; recipes-incomplete + deps-first; big-jumps-risk; migration≠modernization; not-all-code-worth-changing) + per-scale limits inline + the deep-dive bounded-vs-unbounded-risk center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | Fowler refactoring def + two-hats; Feathers legacy=no-tests + seams + characterization; Fowler strangler-fig 2004; LTS path + OpenRewrite recipes; all names/wordings/recipe-names + per-hop breaking changes carried ⚠ @pin; §7 canon gaps (Fowler 2e, Feathers WELC, Fowler StranglerFig) flagged; Java-25-preview-as-migration-target AHEAD-OF-PIN; OpenRewrite network-gated → REPRO PENDING-RUNTIME. |
| C — COMPILE | ⚠ PENDING (toolchain READY; OpenRewrite migration network-gated → REPRO PENDING-RUNTIME) | four-scale demos (refactor-in-green-steps / legacy-seam-characterize / strangler-façade-flag / OpenRewrite-UpgradeToJava21) module spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the two-year-cancelled-rewrite hook names the enemy (big-bang) the whole chapter argues against; the one-invariant-four-scales structure unifies 4 dossiers cleanly; the ladder (method→under-test→system→platform) is memorable; five CONCEPT callouts (refactoring-loop+two-hats, get-to-a-seam, strangler-stall, automate-bulk-deps-first, +the invariant) anchor it. |
| ACCURACY | 9 | Fowler/Feathers/strangler/migration atoms all attributed; the LTS path + OpenRewrite recipes + JPMS-since-17 correct; −1 for the verify-at-pin surface (refactoring/seam names, recipe names, per-hop breaks) — all flagged + §7 canon gaps; characterization-pins-bugs + migration≠modernization + preview-not-a-target handled precisely. |
| UTILITY | 9 | directly actionable across four scales: the refactoring loop + two-hats, the get-to-a-seam-without-tests technique, the strangler façade+flags+contract-tests recipe, the 6-step migration process (tests-green→deps-first→recipe→residual→matrix→modernize-after); a complete safe-change toolkit. |
| DEPTH | 9 | the one-invariant-at-four-scales synthesis + bounded-per-step-vs-unbounded-all-or-nothing risk (why big-bang loses at scale) + the-test-net-is-the-precondition-and-where-missing-build-it-by-capturing-what-is is senior modernization material, the book-spine safe-change framing; ties to the Part X "debt trending down" measure. |
| READABILITY | 8 | gripping rewrite-fails hook, five callouts, the four-scales-one-invariant synthesis opening Part XI; 5239w — the longest chapter, justified by 4 dossiers + a Tier-A Part-opener, and the ladder structure keeps it navigable; clean manual-craft→automated-engine hand-off. |

**Aggregate 44/50**, none < 6 (Part-opener high; ties the other Tier-A Part chapters). Floors A/B/C-source ✅;
FLOOR-C COMPILE = PENDING (toolchain READY; OpenRewrite network-gated). New shapes: preserve-behavior+test
-backed+incremental (the one invariant) + ladder-of-scale + two-hats + legacy=no-tests-characterize-first +
characterization-pins-bugs + strangler-vs-big-bang + migration≠modernization. **OPENS Part XI (Refactoring &
Legacy).** The craft Part X measured; one invariant at four scales (method/under-test/system/platform), all
against the big-bang. Hands off to Ch 40 (automated change & the remediation playbook, keys 96+94 — the engine).
Reuses canon-dating (Fowler/Feathers + modern Java) + the-test-net-precondition + Boy-Scout disciplines.
