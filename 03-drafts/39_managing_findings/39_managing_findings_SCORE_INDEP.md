# INDEPENDENT SCORECARD — Ch 19 "Keeping the Gate Honest" (key 39)

> **Independent (different-model) re-score** of the step-8 chapter scorecard, per `SCORING.md` (the 88%
> auto-approval bar is scored by an independent gate; a main-loop self-score never approves). Harsh-skeptic
> pass: floors first, five clusters 1–10, bounded in-bounds lift loop if the aggregate is short on cluster
> quality. Companion build state read from `_EXAMPLE.md` (GREEN) + `_CODEREVIEW.md` (PASS-WITH-FIXES, M1
> resolved). **RE-SCORE trigger:** the four residual Maven-plugin-default / baseline atoms that the prior
> independent score (43/50) named as ACCURACY's ceiling are now **web-verified verbatim against the pinned
> plugin docs and cited in the draft** (`failOnViolation` Default `true`, `violationSeverity` Default
> `error`, PMD `--suppress-marker` vs legacy `-suppressmarker`; and a real correction —
> `baselineFiles`→`excludeBugsFile`/`excludeBugsFiles`, List form Since 4.7.1.0). Flag
> `39_tool_versions_and_suppression_defaults_unverified.md` is now `✅ RESOLVED`. Every judgement re-made
> from the draft + pin; the prior INDEP is not inherited.

---

## Header

- **Mode:** Phase-3 chapter scorecard (step 8) — **INDEPENDENT** re-score (post plugin-doc web-verify)
- **Dossier key:** 39 (frozen — `01-index/CANDIDATE_POOL.md`) · **FINAL_INDEX Ch 19**, Part IV closer
- **Slug:** `39_managing_findings`
- **Title:** Keeping the Gate Honest (Living with findings: false positives, suppression, baselines, ratcheting)
- **Artifact scored:** `03-drafts/39_managing_findings/39_managing_findings_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28; pin unchanged)
- **Scorer:** chapter-scorer (independent)
- **Date:** 2026-06-28
- **Prior INDEP:** 43/50 (ACCURACY 9, capped by the residual plugin-default bundle) → **this pass: 43/50
  (ACCURACY held at a now top-of-band 9)** after the four plugin-default/baseline atoms resolved
- **Lift-pass #:** 0 (re-score after plugin-doc web-verify) → lift loop attempted, see log

---

## What changed since the prior independent score (43/50)

The prior INDEP held ACCURACY at 9 and named one ceiling: a "narrow residual of plugin-default
spellings/parameter-names" (`failOnViolation`/`violationSeverity` defaults, the `baselineFiles` name +
"since 4.7.1.0", PMD `--suppress-marker`), recorded as resolvable only at `/pin-source`. That residual is
now resolved. Tracking every ACCURACY sub-atom:

| Sub-atom | State now | Evidence |
|---|---|---|
| `maven-checkstyle-plugin` `failOnViolation` Default `true` | **✅ RESOLVED** | web-verify `check-mojo.html` (plugin **3.6.0**), Default column `true` (raw-HTML grep) — draft L8/L168/L173; flag `39_tool_versions_and_suppression_defaults_unverified.md` = `✅ RESOLVED` |
| `maven-checkstyle-plugin` `violationSeverity` Default `error` | **✅ RESOLVED** | same page, Default column `error` — draft L8/L168/L173 |
| PMD CLI marker `--suppress-marker` (legacy `-suppressmarker`) | **✅ RESOLVED** | web-verify `pmd_userdocs_suppressing_warnings.html` (PMD **7.25.0**) — draft L9/L169/L173 |
| SpotBugs baseline parameter (was claimed `baselineFiles`) | **✅ RESOLVED + CORRECTED** | the real params are `excludeBugsFile` (String) / `excludeBugsFiles` (List, **Since: 4.7.1.0**) — "Bugs found in the baseline files won't be reported"; **no parameter literally named `baselineFiles`** (`spotbugs-maven-plugin` 4.10.2.0 gapidocs, raw-HTML grep). Corrected throughout the draft (L10/L92/L124/L170/L173) — a real fix, not just a citation |
| Sonar "Won't Fix"→"Accepted" rename + `//NOSONAR` rule-blind scope | **✅ RESOLVED (prior pass)** | web-verify, dated-at-use as of Server **2026.1 LTA** — draft L88/L172; flag `39_sonar_wontfix_accepted_rename_unverified.md` = `✅ RESOLVED` |
| Tool/plugin versions + GAVs | **✅ RESOLVED (prior pass)** | Central `maven-metadata.xml` 2026-06-27 |
| `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` + `FindBugsFilter` leaf set | **✅ RESOLVED (FLOOR-C build)** | verbatim in pinned engine `findbugs.xml` (`category="MALICIOUS_CODE"`); matchers present in engine jar |
| SpotBugs `Bug` `code`/`category` attribute forms + the `Package` filter element | **⚠ STILL FLAGGED (genuine residual)** | doc-identity-only; **no matcher in the engine jar / not asserted in the built filter** — correctly marked `⚠ @pin` (draft L10/L13/L170/L173); carried in `09-flags/39_*`. NOT in the resolved flag's scope |
| Checkstyle `@SuppressWarnings` name-normalization | **⚠ STILL FLAGGED (genuine residual)** | doc-identity-only, not asserted as confirmed — correctly marked `⚠ @pin` (draft L13/L173); carried in `09-flags/39_*` |

**The lift the trigger claims is real and correctly applied.** The four named atoms are not merely "cited";
one was a genuine error caught and fixed (`baselineFiles` does not exist — the draft now uses
`excludeBugsFile`/`excludeBugsFiles`), which is exactly the kind of correction that *protects* ACCURACY
rather than inflating it. The four most reader-visible, version-sensitive plugin atoms are now fully traced
and build-corroborated (`failOnViolation=true` behaviour is demonstrated by the build going red on a new
finding; `violationSeverity`/`excludeFilterFile` are accepted by the live pinned plugins).

**Why ACCURACY does NOT move 9→10.** Two genuinely-residual atoms remain honestly open in the draft body
*and* back-matter (the SpotBugs `Bug code/category` attribute forms + `Package` element; Checkstyle
`@SuppressWarnings` name-normalization). They are doc-identity-only, **not asserted** as confirmed, and
correctly carried in `09-flags/39_*` — but the ACCURACY-10 anchor is literally "fully traced … **zero
remaining flags**," and two flags remain. Resolving four of the six residual atoms moves the 9 to the **top
of its band** (defensible, build-corroborated, every open item flagged with zero drift) but does not cross
the strict 10 threshold. Awarding 10 here would require either over-claiming the two open atoms (a FLOOR-C
source-trace risk) or pretending a flag that is open is closed — neither is honest. ACCURACY = a confident,
top-of-band **9**.

---

## The three content-floors — checked FIRST (all must PASS)

| Floor | PASS / FAIL | Evidence line |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Whole-draft banned-phrase scan (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `wins` / `the best <tool>`): **0 verdict hits** (the only `OFF`/`wins`-substring grep matches sit inside code identifiers `-Xep:Check:OFF`, "last flag … wins", and the scoped phrase "wrong for *this* project" — none a tool ranking). Draft states "**no tool is crowned**" (L39). Each tool's suppression surface is a different realization of the *same* four levers; narrow-vs-broad is a per-mechanism discipline, not a per-tool verdict; the "which analyzer to layer" question is routed out to Ch 17. Every cross-tool fact cited to that tool's own pinned docs (back-matter L168–173). |
| **B — HONEST-LIMITATIONS** | **PASS** | Six dedicated limitation bullets (§"Limitations & when NOT to reach for it", L136–141), each with an explicit when-NOT: per-finding suppression silences-the-future + rot; rule-tuning global-off hides local truth; baseline freezes-bugs + drift + count-cap order-blindness; ratchet needs-accurate-boundary + cold-legacy + gameable; tools-record-not-decide; debt-about-debt. Reinforced by the deep-dive "honest edge" (L130) and a full §"When to use what" table (L153–160). Every lever sold with its cost; no feature presented cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **(source-trace)** Zero invented atoms; every rule key/flag/annotation/filter element/bug pattern/GAV traces to its tool's pinned docs, is build-verified, or is honestly marked `⚠ @pin` and carried in `09-flags/39_*`. The four formerly-residual plugin-default/baseline atoms are now verified verbatim and cited (and `baselineFiles` corrected to `excludeBugsFile`/`excludeBugsFiles`); the two remaining `⚠ @pin` atoms are flagged, never asserted. The `NP_…` at L73 is an *elided illustration* of the annotation shape, not a claimed pattern. **(compile)** `_EXAMPLE.md`: `mvn -B -Pquality verify` → **BUILD SUCCESS**, JDK 21.0.11, 15 tests, 0 Checkstyle / 0 SpotBugs reported; both silencing controls verified **load-bearing** (remove → red; restore → green); `check_snippets.sh` re-confirmed **7 markers, 7 pass, 0 fail**. **(code-review)** `_CODEREVIEW.md`: **PASS-WITH-FIXES** — no BLOCKER, no security / neutrality / invented-fact finding; all 7 `tag::` regions brace-balanced, ≤9 lines, complete. M1 (doc-vs-pom contradiction) **resolved** 2026-06-27; M2/M3 are MINOR polish, non-blocking. All three conditions hold. |

**All three floors PASS.** No floor failure — the miss below is cluster-quality only (1 point), so the
bounded lift loop (not a prose/scope fix) is the correct instrument.

---

## The five clusters (independent, harsh-skeptic — 1–10)

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | "How a gate dies" hook (the `-Dcheckstyle.skip=true` death spiral) → triage-tree table (finding-type → lever) → four levers ordered narrow→broad, each anchored by a CONCEPT callout; Figure 19.1 (introduced L45 before it appears) carries the scope ladder; the deep dive walks one concrete adopt→baseline→ratchet→catch-the-new-bug→suppress arc as a runnable event. The *why* (undecidability → "false positives are not a defect; they are a property", L28) is explicit. A reader new to suppression mechanics can reconstruct the discipline. 10 reserved for effortless-from-nothing; dense back-matter + a couple of long compounds hold it at a strong 9. |
| 2 | **ACCURACY** | **9** | **Held at a now top-of-band 9 (the four-atom residual resolved this pass).** Dense, correct suppression-atom identity across six tools, each cited to its own docs; module GREEN at the pin with both controls load-bearing; `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` verbatim in the pinned engine; Sonar rename + `//NOSONAR` scope web-verified dated-at-use; **and now `failOnViolation` Default `true` / `violationSeverity` Default `error` / PMD `--suppress-marker` web-verified, plus the `baselineFiles`→`excludeBugsFile`/`excludeBugsFiles` (Since 4.7.1.0) correction** — verbatim against the pinned plugin docs, cited in body + back-matter. Zero drift — every still-open atom is flagged. **Held at 9, not 10:** two genuine residual atoms (SpotBugs `Bug code/category` attribute forms + the `Package` filter element — no matcher in the engine jar; Checkstyle `@SuppressWarnings` name-normalization) legitimately remain `⚠ @pin` (L13/L173), so the "zero remaining flags" 10 anchor cannot yet be claimed. No flagged atom over-claimed to manufacture a 10; the one real error (`baselineFiles`) was caught and fixed, not papered over. |
| 3 | **UTILITY** | **9** | Triage tree + narrowest-lever discipline + baseline-then-ratchet adoption recipe (no flag-day) + justification-required + suppressions-as-reviewable-debt + the "broad suppression would have hidden the new bug" contrast are directly operational; the runnable module + TRY-IT failure path + the §"When to use what" decision table (L153–160) is the page a team keeps open while wiring a gate. |
| 4 | **DEPTH** | **8** | Four-lever scope ladder + suppression-is-a-claim-that-needs-evidence + debt-about-debt + the honest-edge set (baseline freezes real bugs, count-cap order-blind, finding-set drift, cold-legacy, gameable ratchet) + executable load-bearing proof = senior operations material. Sits at 8 (not 9) because it is a focused **single-dossier** practice chapter, not a multi-dossier synthesis. **Not padded toward the bar.** |
| 5 | **READABILITY** | **8** | Vivid failure-story hook, two tables, two CONCEPT callouts, "keep the gate honest" through-line, plain-language-first glosses on baseline/ratchet/gate-health, locked voice held, no filler, clean Part IV→V hand-off. The dense back-matter and a few long compound sentences (L92, L130) keep it a clean, paced 8, short of "effortless at full precision" (9–10). |

**Cluster subtotal: 43 / 50** — no cluster below 6.

---

## Ship-bar verdict

- **Floors A / B / C-source:** all PASS → auto-approval floor condition satisfied.
- **Aggregate:** **43 / 50 (86%)** — **below the 88% (≥44/50) auto-approval bar by 1 point**, no cluster below 6.
- **Verdict: LIFT-LOOP attempted → no in-bounds lift available → route to human gate.** Does not auto-approve.

A 43/50 with all floors green is not a floor failure; it is a 1-point cluster-quality shortfall, so the
bounded lift loop is the right instrument. It was run (below) and produced **zero verified improvement**.
The trigger's resolution of the four plugin-default atoms genuinely strengthened ACCURACY's 9 (it is now
top-of-band and build-corroborated) but did not cross to 10, because two doc-identity atoms remain honestly
flagged in the draft — and the prose loop is forbidden from confirming or over-claiming them. DEPTH and
READABILITY at 8 have no in-bounds lift (the former without padding, the latter without a fake +1).

---

## Flagged weakest cluster (lift target)

- **Weakest *liftable* cluster:** READABILITY — 8. (DEPTH is also 8 but is **not** in-bounds-liftable — single-dossier practice chapter; lifting requires padding, forbidden. ACCURACY at 9 cannot lift in the prose loop — its only ceiling is two correctly-flagged doc-identity `@pin` atoms; confirming them is a `/pin-source` / source-verify act, and over-claiming them is a FLOOR-C source-trace risk — both forbidden here.)
- **Why it is the weakest liftable one:** the prose is clean but the back-matter is dense and two compound sentences (L92, L130) run long, holding it at a solid-8 rather than an effortless-9.
- **Single highest-leverage in-bounds move considered:** trim the two long compound sentences; tighten the back-matter. **Outcome of attempting it:** the named in-bounds levers are *already satisfied* — em-dash density under ceiling, Figure 19.1 introduced before it appears (L45), baseline/ratchet/gate-health each glossed plain-language-first (L92/L102/L116), all FINAL_INDEX cross-refs correct (Ch 15/16/17/18 verified against the index; "the CI part"→keys 76/80, "a later chapter"→key 87 routed, not mis-numbered), no banned filler. A cosmetic trim of two sentences does not honestly move a clean 8 to a 9, and no 9 is awarded the prose does not earn.

---

## Bounded lift loop — log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (prior INDEP) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | LIFT-LOOP | ACCURACY 9, capped by the four-atom plugin-default residual; recommended `/pin-source` |
| 0 (this re-score) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | LIFT-LOOP | four plugin-default/baseline atoms now web-verified + cited (`baselineFiles`→`excludeBugsFile`/`excludeBugsFiles` corrected); ACCURACY's 9 moves to top-of-band but does **not** reach 10 — two doc-identity `@pin` atoms remain open in the draft |
| 1 (attempted) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | **no lift available** | Weakest liftable cluster = READABILITY. Every in-bounds lever named for this chapter is already satisfied (em-dash under ceiling; Figure 19.1 intro present, L45; baseline/ratchet/gate-health plain-language-first; FINAL_INDEX cross-refs all correct). **No remaining in-bounds defect to fix.** ACCURACY cannot lift in the prose loop (two residual doc-identity `@pin` atoms — confirming them is a `/pin-source` act, over-claiming them a FLOOR-C risk); DEPTH cannot lift (single-dossier — padding forbidden); CLARITY/UTILITY/ACCURACY already 9 (10 reserved). A pass that changes nothing real is not a pass; no cosmetic edit was made to manufacture a fake +1. |

**Result after the attempted pass: 43/50, unchanged.** Per `SCORING.md` ("do not lower the bar to pass
it"), the chapter is **1 point short of the auto-approval bar with no in-bounds path to close the gap**:
the only liftable route to 44 was ACCURACY 9→10, and that is now gated solely by two correctly-flagged
doc-identity atoms (a `/pin-source` / source-verify act, not a prose edit).

---

## Recommendation to the human gate

This is a genuinely well-built chapter (clean floors, three clusters at 9, a runnable load-bearing module),
and the trigger's plugin-doc web-verify was the right move — it resolved four ACCURACY atoms and even caught
a real error (`baselineFiles` does not exist; corrected to `excludeBugsFile`/`excludeBugsFiles`). It still
misses the 88% auto-approval bar by **a single point**, for one honest reason: **ACCURACY is held at 9 (not
10) by two doc-identity atoms** — the SpotBugs `Bug code/category` attribute forms + the `Package` filter
element (no matcher in the engine jar), and Checkstyle `@SuppressWarnings` name-normalization — that are
correctly flagged, not asserted, and cannot be confirmed without the pinned tool versions; the remaining 8s
(DEPTH single-dossier; READABILITY clean-but-not-effortless) have no in-bounds lift. Two legitimate routes,
both human/owner decisions (not scorer decisions):

1. **Resolve the two residual doc-identity atoms at `/pin-source` / source-verify** (assert the SpotBugs
   `Bug` `code`/`category` attribute forms + the `Package` filter element against the pinned engine, and
   Checkstyle `@SuppressWarnings` name-normalization against the pinned Checkstyle docs). Confirming those —
   the *last* two flags on this chapter — would lift ACCURACY 9→10 honestly (full-trace, zero remaining
   flags) and carry the aggregate to 44, clearing the bar **without inventing anything**. This is the
   highest-leverage, in-discipline path and is recorded here as the standing recommendation.
2. **Accept at 43/50 at the Step-12 human gate** as a deliberate editorial call for a focused Part-IV closer
   whose ACCURACY ceiling is set by two genuine pin-deferred doc-identity atoms, not by weak content.

Flag for the human gate: `09-flags/` (this scorecard + the open key-39 doc-identity residual carried in the
draft back-matter / `09-flags/39_*`). The four-atom plugin-default flag
`39_tool_versions_and_suppression_defaults_unverified.md` is now `✅ RESOLVED`. **Not auto-approved.**

---

## Learnings & pipeline suggestions

- **The `/pin-source` lift path works a second time, and it self-corrects.** Resolving the four
  plugin-default atoms not only retired the prior ACCURACY ceiling — it caught a genuine error
  (`baselineFiles` is not a real `spotbugs-maven-plugin` parameter; the real names are
  `excludeBugsFile`/`excludeBugsFiles`). This is the strongest argument for the rubric's "confirm at the
  pinned doc, never from memory" discipline: the act of verifying *protects* ACCURACY, it does not merely
  decorate it. Worth a one-line note in `SCORING.md` that an ACCURACY ceiling flagged `⚠ @pin` is lifted at
  `/pin-source` (where the verification can also correct a wrong atom), never in the prose lift loop.
- **Atom-by-atom @pin tracking keeps a 9 honest across re-scores.** The residual bundle has now been scored
  down from six sub-atoms to two over three passes (42→43→43). Tracking each sub-atom's state (the table at
  the top of this card) is what lets ACCURACY sit at a defensible *top-of-band* 9 — neither frozen low
  because *some* flag is open, nor inflated to 10 because *most* resolved. The hard line is the literal 10
  anchor ("zero remaining flags"): two open flags = no 10, full stop.
- **A resolved trigger does not automatically move the aggregate.** The trigger correctly resolved four
  atoms, but because two doc-identity atoms remained, ACCURACY moved *within* its band (to top-of-9), not
  *across* it (to 10) — so the aggregate held at 43. A scorer should resist the pull to "reward the work
  with +1": the score tracks the artifact's state against the anchor, not the effort spent.
- **The bounded lift loop can legitimately return "no in-bounds lift available" at 43/50 — again.** One
  point short, the only liftable route gated by pin-deferred atoms and the rest at honest ceilings, the
  disciplined output remains a documented zero-change pass routed to the human gate — not a manufactured +1.
- **Re-run `check_snippets` even on an untouched draft at re-score.** Confirmed 7/7 again this pass at
  near-zero cost; catches any marker drift between scoring passes.
- **Cross-ref verification against the LOCKED `FINAL_INDEX` is fast and high-value.** All Ch 15/16/17/18
  references and the keys-76/80/87 routings resolve correctly against the index; doing this at score time
  catches the cheapest class of post-lock drift.
