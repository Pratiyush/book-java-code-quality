# INDEPENDENT SCORECARD — Ch 19 "Living with findings: false positives, baselines, ratcheting" (key 39)

> **Independent (different-model) re-score** of the step-8 chapter scorecard, per `SCORING.md` (the 88%
> auto-approval bar is scored by an independent gate; a main-loop self-score never approves). This is the
> harsh-skeptic pass: floors first, five clusters 1–10, bounded in-bounds lift loop if the aggregate is
> short on cluster quality. Companion build state read from `_EXAMPLE.md` (GREEN) + `_CODEREVIEW.md`
> (PASS-WITH-FIXES, M1 resolved). The prior self-score (`_SCORE.md`, 42/50, COMPILE then PENDING) is **not**
> inherited — every judgement re-made from the draft + pin.

---

## Header

- **Mode:** Phase-3 chapter scorecard (step 8) — **INDEPENDENT** re-score
- **Dossier key:** 39 (frozen — `01-index/CANDIDATE_POOL.md`) · **FINAL_INDEX Ch 19**, Part IV closer
- **Slug:** `39_managing_findings`
- **Title:** Keeping the Gate Honest (Living with findings: false positives, baselines, ratcheting)
- **Artifact scored:** `03-drafts/39_managing_findings/39_managing_findings_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28; pin unchanged)
- **Scorer:** chapter-scorer (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (initial independent score) → lift loop attempted, see log

---

## The three content-floors — checked FIRST (all must PASS)

| Floor | PASS / FAIL | Evidence line |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Whole-draft banned-phrase scan (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `kills` / `destroys` / `blows away` / `obvious choice over` / `wins` / `the best <tool>`): **0 real hits** (the grep matches at L11/90/171 are substrings inside code identifiers `-Xep:Check:OFF`, `CastToNonNullMethod`, and the scoped phrase "wrong for *this* project" — none is a verdict). Draft states "**no tool is crowned**" (L39). Each tool's suppression surface is presented as a different realization of the *same* four levers; narrow-vs-broad is a per-mechanism discipline, not a per-tool ranking; "which analyzer to layer" verdict is routed out to Ch 17. Every cross-tool fact is cited to that tool's own pinned docs (back-matter L168–173). |
| **B — HONEST-LIMITATIONS** | **PASS** | Six dedicated limitation bullets (§"Limitations & when NOT to reach for it", L136–141), each with an explicit when-NOT: suppression silences-the-future + rot; rule-tuning global-off hides local truth; baseline freezes-bugs + drift + count-cap order-blindness; ratchet needs-accurate-boundary + cold-legacy + gameable; tools-record-not-decide; debt-about-debt. Reinforced by the deep-dive "honest edge" paragraph (L130) and a full §"When to use what" decision table (L153–160). The chapter IS a limitations-driven practice — every lever sold with its cost. No feature presented cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **(source-trace)** Zero invented atoms; every rule key/flag/annotation/filter element/bug pattern/GAV traces to its tool's pinned docs or is flagged. The genuine @pin surface (per-tool `failOnViolation`/`violationSeverity` defaults, `baselineFiles` "since 4.7.1.0", Sonar "Won't Fix"→"Accepted" rename + version boundary, `//NOSONAR <ruleKey>` scoping, PMD `--suppress-marker` spelling) is **honestly marked ⚠ @pin** in back-matter (L170, L172–173) and carried in `09-flags/39_sonar_wontfix_accepted_rename_unverified.md` + `09-flags/39_tool_versions_and_suppression_defaults_unverified.md`; nothing asserted as confirmed that is not. The `NP_…` at L73 is an *elided illustration* of the annotation shape, not a claimed pattern. **(compile)** `_EXAMPLE.md`: `mvn -B -Pquality verify` → **BUILD SUCCESS**, JDK 21.0.11, 15 tests, 0 Checkstyle / 0 SpotBugs reported; both silencing controls verified **load-bearing** (remove → red; restore → green). `check_snippets.sh` re-run this pass: **7 markers, 7 pass, 0 fail**. **(code-review)** `_CODEREVIEW.md`: **PASS-WITH-FIXES** — no BLOCKER, no security / neutrality / invented-fact finding; all 7 `tag::` regions brace-balanced, ≤9 lines, complete. M1 (doc-vs-pom contradiction) **resolved** 2026-06-27; M2/M3 are MINOR polish on published code, non-blocking. FLOOR C holds on all three conditions. |

**All three floors PASS.** No floor failure — the miss below is cluster-quality only, so the bounded lift loop (not a prose/scope fix) is the correct instrument.

---

## The five clusters (independent, harsh-skeptic — 1–10)

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | "How a gate dies" hook → triage-tree table (finding-type → lever) → four levers ordered narrow→broad, each anchored by a CONCEPT callout; Figure 19.1 carries the scope ladder; the deep dive walks one concrete adopt→baseline→ratchet→suppress arc as a runnable event. The *why* (undecidability → "false positives are a property, not a defect") is explicit. A reader new to suppression mechanics can reconstruct the discipline. |
| 2 | **ACCURACY** | **8** | Dense, correct suppression-atom identity across six tools, each cited to its own docs; module GREEN at the pin with both controls load-bearing; `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` confirmed verbatim in the pinned engine. **Capped at 8 (not 9): a real surface of version/default atoms legitimately carries ⚠ @pin** (plugin defaults, `baselineFiles` since-4.7.1.0, Sonar rename + version boundary, `//NOSONAR` scoping, PMD marker spelling) — all flagged, none drifted, but a full-trace 9 cannot be claimed while load-bearing defaults remain plugin-doc-unconfirmed. **Honest flagged-atom cap; not inflated, no @pin atom invented.** |
| 3 | **UTILITY** | **9** | Triage tree + narrowest-lever discipline + baseline-then-ratchet adoption recipe (no flag-day) + justification-required + suppressions-as-reviewable-debt + the "broad suppression would have hidden the new bug" contrast are directly operational; the runnable module + TRY-IT failure path is the page a team keeps open while wiring a gate. |
| 4 | **DEPTH** | **8** | Four-lever scope ladder + suppression-is-a-claim-that-needs-evidence + debt-about-debt + order-blind / drift / cold-legacy / count-cap-gameable honest edges + executable load-bearing proof = senior operations material. Sits at 8 (not 9) because it is a focused **single-dossier** practice chapter, not a multi-dossier synthesis. **Not padded toward the bar.** |
| 5 | **READABILITY** | **8** | Vivid failure-story hook, two tables, two CONCEPT callouts, "keep the gate honest" through-line, plain-language-first glosses on baseline/ratchet/gate-health, locked voice held, no filler, em-dash density **7.09/1000 (under the ~8 ceiling)**, clean Part IV→V hand-off. A clean, paced 8; the dense back-matter and a few long compound sentences (L92, L130) keep it short of "effortless at full precision" (9–10). |

**Cluster subtotal: 42 / 50** — no cluster below 6.

---

## Ship-bar verdict

- **Floors A / B / C-source:** all PASS → auto-approval floor condition satisfied.
- **Aggregate:** **42 / 50 (84%)** — **below the 88% (≥44/50) auto-approval bar**, no cluster below 6.
- **Verdict: LIFT-LOOP attempted → no in-bounds lift available → CUT-to-human-gate.** Does not auto-approve.

A 42/50 with all floors green is not a floor failure; it is a cluster-quality shortfall, so the bounded
lift loop is the right instrument. It was run (below) and produced **zero verified improvement** because
every cluster already sits at its honest in-bounds ceiling.

---

## Flagged weakest cluster (lift target)

- **Weakest *liftable* cluster:** READABILITY — 8. (ACCURACY and DEPTH are also 8 but are **not** in-bounds-liftable: ACCURACY is capped by legitimately-flagged @pin atoms — lifting requires confirming them, forbidden; DEPTH is a single-dossier practice chapter — lifting requires padding, forbidden.)
- **Why it is the weakest liftable one:** the prose is clean but the back-matter is dense and two compound sentences (L92, L130) run long, holding it at a solid-8 rather than an effortless-9.
- **Single highest-leverage in-bounds move considered:** trim the two long compound sentences; tighten back-matter. **Outcome of attempting it:** the named in-bounds levers are *already satisfied* — em-dash under ceiling, Figure 19.1 introduced before it appears (L45), baseline/ratchet/gate-health each glossed plain-language-first (L92/L102/L116), all FINAL_INDEX cross-refs correct (Ch 15/16/17/18 verified against the index; "the CI part"→keys 76/80, "a later chapter"→key 87 routed, not mis-numbered), no banned filler. A cosmetic trim of two sentences does not honestly move a clean 8 to a 9, and no 9 is awarded that the prose does not earn.

---

## Bounded lift loop — log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 42 / 50 | PASS | PASS | PASS | LIFT-LOOP | initial independent score |
| 1 (attempted) | 2026-06-28 | 42 / 50 | PASS | PASS | PASS | **no lift available** | Weakest liftable cluster = READABILITY. Examined every in-bounds lever named for this chapter: em-dash density (7.09/1000 — already under ceiling), Figure 19.1 intro (already present, L45), gloss of baseline/ratchet/gate-health (already plain-language-first, L92/L102/L116), FINAL_INDEX cross-refs (all correct). **No remaining in-bounds defect to fix.** ACCURACY cannot lift (flagged @pin atoms — inventing confirmation is forbidden); DEPTH cannot lift (single-dossier — padding is forbidden); CLARITY/UTILITY already 9 (10 is reserved). A pass that changes nothing real is not a pass; no cosmetic edit was made to manufacture a fake +2. |

**Result after the attempted pass: 42/50, unchanged.** Per `SCORING.md` ("do not lower the bar to pass
it" / "a floor failure is never lifted by this loop"), and because no honest in-bounds material exists to
raise ACCURACY past its flagged-atom cap or DEPTH past its single-dossier ceiling, the chapter is **2
points short of the auto-approval bar with no in-bounds path to close the gap**.

---

## Recommendation to the human gate

This is a genuinely well-built chapter (clean floors, two clusters at 9, a runnable load-bearing module).
It misses the 88% auto-approval bar by 2 points for one honest reason: **ACCURACY is capped at 8 by a
real, correctly-flagged surface of @pin version/default atoms** that cannot be confirmed without the
pinned plugin docs, and the remaining 8s (DEPTH single-dossier; READABILITY clean-but-not-effortless)
have no in-bounds lift. Two legitimate routes, both human/owner decisions (not scorer decisions):

1. **Resolve the @pin atoms at `/pin-source`** (confirm the plugin defaults, `baselineFiles` since-4.7.1.0,
   Sonar rename + version boundary, `//NOSONAR` scoping, PMD marker spelling against the *pinned* plugin
   docs). Confirming those would lift ACCURACY 8→9 honestly (full-trace, zero remaining flags) and carry
   the aggregate to 43–44; a re-score after that pass could clear the bar **without inventing anything**.
   This is the highest-leverage, in-discipline path and is recorded here as the standing recommendation.
2. **Accept at 42/50 at the Step-12 human gate** as a deliberate editorial call for a focused Part-IV
   closer whose ACCURACY ceiling is set by genuine pin-deferred atoms, not by weak content.

Flag for the human gate: `09-flags/` (this scorecard + the two existing key-39 @pin flags). **Not
auto-approved.**

---

## Learnings & pipeline suggestions

- **A "flagged-atom cap" is a real ACCURACY ceiling, and it is correct to hold it.** Key 39's ACCURACY
  honestly cannot exceed 8 while load-bearing plugin defaults sit ⚠ @pin — and the right move is to hold
  the 8 and route the lift to `/pin-source`, **not** to inflate the score or invent confirmation. Suggest
  `SCORING.md` note explicitly that "a cluster whose only ceiling is correctly-flagged pin-deferred atoms
  is lifted at `/pin-source`, never in the prose lift loop" — so a future scorer does not pad around it.
- **The bounded lift loop can legitimately return "no in-bounds lift available."** When every cluster is at
  its honest ceiling (9s at the realistic max for a focused chapter; 8s capped by flags or single-dossier
  scope), the disciplined output is a documented zero-change pass that routes to the human gate — not a
  manufactured cosmetic edit re-scored as +2. Worth stating in the lift-loop section that a no-op pass is a
  valid, recordable result.
- **An independent re-score should re-run `check_snippets` even on an untouched draft.** Confirmed 7/7 here
  cheaply; catches any marker drift between the self-score and the independent pass at near-zero cost.
- **Cross-ref verification against the LOCKED `FINAL_INDEX` is fast and high-value.** All Ch 15/16/17/18
  references and the keys-76/80/87 routings resolve correctly against the index; doing this at score time
  (not only at AUDIT) catches the cheapest class of post-lock drift.
