# INDEPENDENT SCORECARD — Ch 19 "Living with findings: false positives, baselines, ratcheting" (key 39)

> **Independent (different-model) re-score** of the step-8 chapter scorecard, per `SCORING.md` (the 88%
> auto-approval bar is scored by an independent gate; a main-loop self-score never approves). Harsh-skeptic
> pass: floors first, five clusters 1–10, bounded in-bounds lift loop if the aggregate is short on cluster
> quality. Companion build state read from `_EXAMPLE.md` (GREEN) + `_CODEREVIEW.md` (PASS-WITH-FIXES, M1
> resolved). **RE-SCORE trigger:** `/pin-source` resolved the Sonar **"Won't Fix"→"Accepted"** rename + the
> `//NOSONAR` rule-blind scope (dated-at-use as of Server **2026.1 LTA**; both doc URLs now cited in the
> draft) — the prior independent score (42/50) capped ACCURACY at 8 on a bundle of @pin atoms that has now
> materially shrunk. Every judgement re-made from the draft + pin; the prior INDEP is not inherited.

---

## Header

- **Mode:** Phase-3 chapter scorecard (step 8) — **INDEPENDENT** re-score (post `/pin-source`)
- **Dossier key:** 39 (frozen — `01-index/CANDIDATE_POOL.md`) · **FINAL_INDEX Ch 19**, Part IV closer
- **Slug:** `39_managing_findings`
- **Title:** Keeping the Gate Honest (Living with findings: false positives, baselines, ratcheting)
- **Artifact scored:** `03-drafts/39_managing_findings/39_managing_findings_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28; pin unchanged)
- **Scorer:** chapter-scorer (independent)
- **Date:** 2026-06-28
- **Prior INDEP:** 42/50 (ACCURACY 8) → **this pass: 43/50 (ACCURACY 8→9)** after the Sonar-rename/`//NOSONAR` atoms resolved
- **Lift-pass #:** 0 (initial post-pin score) → lift loop attempted, see log

---

## What changed since the prior independent score (42/50)

The ACCURACY cap at 8 in the prior INDEP bundled five @pin sub-atoms. Tracking resolution:

| Sub-atom (prior ACCURACY cap) | State now | Evidence |
|---|---|---|
| Sonar **"Won't Fix"→"Accepted"** rename + version boundary (Server 10.4) | **✅ RESOLVED** | web-verify, dated-at-use as of Server **2026.1 LTA**; `www.sonarsource.com/.../whats-new/sonarqube-10-4` + `docs.sonarsource.com/.../2026.1/.../managing` — cited in draft body L88 + back-matter L172; flag `39_sonar_wontfix_accepted_rename_unverified.md` = `✅ RESOLVED` |
| Sonar `//NOSONAR` rule-blind scope (no scoped `<ruleKey>` form) | **✅ RESOLVED** | same web-verify; "suppress all issues on the line … now and in the future", no scoped form documented — draft L88/L172 |
| Tool/plugin **versions + GAVs** at the pin | **✅ RESOLVED** | Central `maven-metadata.xml` 2026-06-27 (checkstyle-plugin 3.6.0, spotbugs-plugin 4.10.2.0, spotbugs/annotations 4.10.2, error_prone_core 2.50.0, NullAway 0.13.4) |
| `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` + `FindBugsFilter` leaf set | **✅ RESOLVED** | FLOOR-C build — verbatim in pinned engine `findbugs.xml` (`category="MALICIOUS_CODE"`); matchers present in engine jar |
| **Maven-plugin DEFAULT values** (`failOnViolation`/`violationSeverity` defaults; `baselineFiles` name + "since 4.7.1.0"; PMD `--suppress-marker` spelling; `Bug code/category` + `Package` element) | **⚠ STILL FLAGGED (residual)** | correctly marked `⚠ @pin` in draft L8/L13/L170/L173; carried in `39_tool_versions_and_suppression_defaults_unverified.md` (flag STAYS OPEN for these) — plugin-default spellings/param-names, not on the SonarSource docs fetched |

The most reader-visible, version-sensitive item in the prior bundle (the Sonar rename — it shapes the Lever-1
Sonar bullet, the deep-dive **Accepted** guidance, and the "When to use what" row) is now fully traced and
dated-at-use. The residual is a narrow surface of plugin-default *spellings/parameter-names*, all flagged
(none drifted), and several are independently corroborated by the green build (e.g. `failOnViolation=true`
behaviour is demonstrated by the build going red on a new finding). This honestly lifts ACCURACY 8→9.

---

## The three content-floors — checked FIRST (all must PASS)

| Floor | PASS / FAIL | Evidence line |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Whole-draft banned-phrase scan (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `kills` / `destroys` / `blows away` / `obvious choice over` / `wins` / `the best <tool>`): **0 real hits** (grep matches at L12/90/171 are substrings inside code identifiers `-Xep:Check:OFF`, `CastToNonNullMethod`, and the scoped phrase "wrong for *this* project" — none a verdict). Draft states "**no tool is crowned**" (L39). Each tool's suppression surface is a different realization of the *same* four levers; narrow-vs-broad is a per-mechanism discipline, not a per-tool ranking; the "which analyzer to layer" verdict is routed out to Ch 17. Every cross-tool fact cited to that tool's own pinned docs (back-matter L168–173). |
| **B — HONEST-LIMITATIONS** | **PASS** | Six dedicated limitation bullets (§"Limitations & when NOT to reach for it", L136–141), each with an explicit when-NOT: suppression silences-the-future + rot; rule-tuning global-off hides local truth; baseline freezes-bugs + drift + count-cap order-blindness; ratchet needs-accurate-boundary + cold-legacy + gameable; tools-record-not-decide; debt-about-debt. Reinforced by the deep-dive "honest edge" (L130) and a full §"When to use what" table (L153–160). Every lever sold with its cost; no feature cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **(source-trace)** Zero invented atoms; every rule key/flag/annotation/filter element/bug pattern/GAV traces to its tool's pinned docs, is build-verified, or is honestly marked `⚠ @pin` and carried in `09-flags/39_*`. The residual plugin-default atoms (`failOnViolation`/`violationSeverity` defaults, `baselineFiles` "since 4.7.1.0", PMD `--suppress-marker` spelling, `Bug code/category` + `Package`) are flagged, never asserted as confirmed. The `NP_…` at L73 is an *elided illustration* of the annotation shape, not a claimed pattern. **(compile)** `_EXAMPLE.md`: `mvn -B -Pquality verify` → **BUILD SUCCESS**, JDK 21.0.11, 15 tests, 0 Checkstyle / 0 SpotBugs reported; both silencing controls verified **load-bearing** (remove → red; restore → green); `check_snippets.sh` re-confirmed **7 markers, 7 pass, 0 fail**. **(code-review)** `_CODEREVIEW.md`: **PASS-WITH-FIXES** — no BLOCKER, no security / neutrality / invented-fact finding; all 7 `tag::` regions brace-balanced, ≤9 lines, complete. M1 (doc-vs-pom contradiction) **resolved** 2026-06-27; M2/M3 are MINOR polish, non-blocking. All three conditions hold. |

**All three floors PASS.** No floor failure — the miss below is cluster-quality only, so the bounded lift loop (not a prose/scope fix) is the correct instrument.

---

## The five clusters (independent, harsh-skeptic — 1–10)

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | "How a gate dies" hook → triage-tree table (finding-type → lever) → four levers ordered narrow→broad, each anchored by a CONCEPT callout; Figure 19.1 carries the scope ladder; the deep dive walks one concrete adopt→baseline→ratchet→suppress arc as a runnable event. The *why* (undecidability → "false positives are a property, not a defect") is explicit. A reader new to suppression mechanics can reconstruct the discipline. 10 reserved for effortless-from-nothing. |
| 2 | **ACCURACY** | **9** | **Lifted 8→9 this pass.** Dense, correct suppression-atom identity across six tools, each cited to its own docs; module GREEN at the pin with both controls load-bearing; `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` confirmed verbatim in the pinned engine; **and the previously-capping Sonar rename + `//NOSONAR` scope now web-verified, dated-at-use (Server 2026.1 LTA), cited in body + back-matter.** Zero drift — every still-open atom is flagged. **Held at 9, not 10:** a narrow residual of plugin-default spellings/param-names (`failOnViolation`/`violationSeverity` defaults, `baselineFiles` "since 4.7.1.0", PMD `--suppress-marker`) legitimately stays `⚠ @pin`, unconfirmable without the pinned plugin docs — so "fully traced, zero remaining flags" (the 10 anchor) cannot yet be claimed. Honest, build-corroborated 9; no flagged atom treated as resolved to manufacture a 10. |
| 3 | **UTILITY** | **9** | Triage tree + narrowest-lever discipline + baseline-then-ratchet adoption recipe (no flag-day) + justification-required + suppressions-as-reviewable-debt + the "broad suppression would have hidden the new bug" contrast are directly operational; the runnable module + TRY-IT failure path is the page a team keeps open while wiring a gate. |
| 4 | **DEPTH** | **8** | Four-lever scope ladder + suppression-is-a-claim-that-needs-evidence + debt-about-debt + order-blind / drift / cold-legacy / count-cap-gameable honest edges + executable load-bearing proof = senior operations material. Sits at 8 (not 9) because it is a focused **single-dossier** practice chapter, not a multi-dossier synthesis. **Not padded toward the bar.** |
| 5 | **READABILITY** | **8** | Vivid failure-story hook, two tables, two CONCEPT callouts, "keep the gate honest" through-line, plain-language-first glosses on baseline/ratchet/gate-health, locked voice held, no filler, em-dash density **7.09/1000 (under the ~8 ceiling)**, clean Part IV→V hand-off. A clean, paced 8; the dense back-matter and a few long compound sentences (L92, L130) keep it short of "effortless at full precision" (9–10). |

**Cluster subtotal: 43 / 50** — no cluster below 6.

---

## Ship-bar verdict

- **Floors A / B / C-source:** all PASS → auto-approval floor condition satisfied.
- **Aggregate:** **43 / 50 (86%)** — **below the 88% (≥44/50) auto-approval bar by 1 point**, no cluster below 6.
- **Verdict: LIFT-LOOP attempted → no in-bounds lift available → route to human gate.** Does not auto-approve.

A 43/50 with all floors green is not a floor failure; it is a 1-point cluster-quality shortfall, so the
bounded lift loop is the right instrument. It was run (below) and produced **zero verified improvement** —
the one cluster that could close the gap (ACCURACY 9→10) is gated by genuinely pin-deferred plugin-default
atoms that the prose loop is forbidden to confirm, and the remaining 8s have no in-bounds lift.

---

## Flagged weakest cluster (lift target)

- **Weakest *liftable* cluster:** READABILITY — 8. (DEPTH is also 8 but is **not** in-bounds-liftable — single-dossier practice chapter; lifting requires padding, forbidden. ACCURACY at 9 cannot lift in the prose loop — the only ceiling is correctly-flagged @pin plugin-default atoms; confirming them is a `/pin-source` act, forbidden here.)
- **Why it is the weakest liftable one:** the prose is clean but the back-matter is dense and two compound sentences (L92, L130) run long, holding it at a solid-8 rather than an effortless-9.
- **Single highest-leverage in-bounds move considered:** trim the two long compound sentences; tighten back-matter. **Outcome of attempting it:** the named in-bounds levers are *already satisfied* — em-dash under ceiling, Figure 19.1 introduced before it appears (L45), baseline/ratchet/gate-health each glossed plain-language-first (L92/L102/L116), all FINAL_INDEX cross-refs correct (Ch 15/16/17/18 verified against the index; "the CI part"→keys 76/80, "a later chapter"→key 87 routed, not mis-numbered), no banned filler. A cosmetic trim of two sentences does not honestly move a clean 8 to a 9, and no 9 is awarded the prose does not earn.

---

## Bounded lift loop — log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | LIFT-LOOP | initial post-`/pin-source` independent score (ACCURACY 8→9 vs prior INDEP) |
| 1 (attempted) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | **no lift available** | Weakest liftable cluster = READABILITY. Examined every in-bounds lever named for this chapter: em-dash density (7.09/1000 — already under ceiling), Figure 19.1 intro (already present, L45), gloss of baseline/ratchet/gate-health (already plain-language-first), FINAL_INDEX cross-refs (all correct). **No remaining in-bounds defect to fix.** ACCURACY cannot lift in the prose loop (residual @pin plugin-default atoms — confirming them is a `/pin-source` act, forbidden here); DEPTH cannot lift (single-dossier — padding forbidden); CLARITY/UTILITY/ACCURACY already 9 (10 reserved). A pass that changes nothing real is not a pass; no cosmetic edit was made to manufacture a fake +1. |

**Result after the attempted pass: 43/50, unchanged.** Per `SCORING.md` ("do not lower the bar to pass it"
/ "a floor failure is never lifted by this loop"), and because the only honest path to 44 is confirming the
residual plugin-default atoms (a `/pin-source` act, not a prose edit), the chapter is **1 point short of the
auto-approval bar with no in-bounds path to close the gap**.

---

## Recommendation to the human gate

This is a genuinely well-built chapter (clean floors, three clusters at 9, a runnable load-bearing module).
After `/pin-source` resolved the Sonar rename + `//NOSONAR` scope, it now misses the 88% auto-approval bar by
**a single point**, for one honest reason: **ACCURACY is held at 9 (not 10) by a narrow residual of
plugin-default spellings/parameter-names** (`failOnViolation`/`violationSeverity` defaults, `baselineFiles`
"since 4.7.1.0", PMD `--suppress-marker`) that cannot be confirmed without the pinned plugin docs, and the
remaining 8s (DEPTH single-dossier; READABILITY clean-but-not-effortless) have no in-bounds lift. Two
legitimate routes, both human/owner decisions (not scorer decisions):

1. **Resolve the residual plugin-default atoms at `/pin-source`** (confirm `failOnViolation`/`violationSeverity`
   defaults, `baselineFiles` param name + "since 4.7.1.0", and the PMD `--suppress-marker` spelling against the
   *pinned* plugin docs). Confirming those would lift ACCURACY 9→10 honestly (full-trace, zero remaining flags)
   and carry the aggregate to 44 — clearing the bar **without inventing anything**. This is the highest-leverage,
   in-discipline path and is recorded here as the standing recommendation.
2. **Accept at 43/50 at the Step-12 human gate** as a deliberate editorial call for a focused Part-IV closer
   whose ACCURACY ceiling is set by genuine pin-deferred plugin-default atoms, not by weak content.

Flag for the human gate: `09-flags/` (this scorecard + the open key-39 plugin-default flag
`39_tool_versions_and_suppression_defaults_unverified.md`). **Not auto-approved.**

---

## Learnings & pipeline suggestions

- **`/pin-source` is the right instrument for an ACCURACY ceiling, and it works.** Key 39's ACCURACY moved
  8→9 the moment the Sonar rename + `//NOSONAR` scope were web-verified and dated-at-use — exactly the path
  the prior INDEP recorded as the standing recommendation. This confirms the earlier suggestion: a cluster
  whose only ceiling is correctly-flagged pin-deferred atoms is lifted at `/pin-source`, never in the prose
  lift loop. Worth promoting into `SCORING.md` so a future scorer neither pads around such a cap nor inflates it.
- **A partially-resolved @pin bundle is worth scoring atom-by-atom, not all-or-nothing.** The prior 8 bundled
  five sub-atoms; four resolved (Sonar rename, `//NOSONAR` scope, versions/GAVs, the engine bug patterns) and
  only the plugin-default spellings remain. Tracking each sub-atom's state (the table at the top of this card)
  is what lets ACCURACY rise to a defensible 9 while still honestly withholding the 10 — rather than staying
  frozen at 8 because *some* flag is open, or jumping to 10 because *most* resolved.
- **The bounded lift loop can still legitimately return "no in-bounds lift available" at 43/50.** One point
  short, with the only liftable route gated by pin-deferred atoms and the rest at honest ceilings, the
  disciplined output remains a documented zero-change pass routed to the human gate — not a manufactured +1.
- **Re-run `check_snippets` even on an untouched draft at re-score.** Confirmed 7/7 again this pass at
  near-zero cost; catches any marker drift between scoring passes.
- **Cross-ref verification against the LOCKED `FINAL_INDEX` is fast and high-value.** All Ch 15/16/17/18
  references and the keys-76/80/87 routings resolve correctly against the index; doing this at score time
  catches the cheapest class of post-lock drift.
