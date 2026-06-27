# INDEPENDENT SCORECARD — Ch 3 "A Map of the Territory" (key 05)

> **Independent (different-model) re-score** per `SCORING.md` ship bar. Deliberately harsh, skeptical
> review: ≥44/50 (88%) awarded only if a senior Java engineer would find the chapter both *excellent*
> AND *error-free*, no cluster < 6, floors A/B/C-source PASS. SCORE-ONLY pass — no draft edits, no lift
> loop applied here. **This is the post-polish re-score** (Pass 0 = 37 LIFT; Pass 1 = 41 LIFT; this is
> Pass 2, scoring the draft after the two line-level CLARITY/READABILITY fixes from the Pass-1 lift list).

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score, post-polish — Pass 2)
- **Dossier key:** 05 (solo) — FINAL_INDEX Ch 3
- **Slug:** `05_java_quality_toolchain`
- **Title:** A Map of the Territory (The Java quality toolchain — a map)
- **Part / arc position:** Part I — Foundations, Chapter 3
- **Artifact scored:** `03-drafts/05_java_quality_toolchain/05_java_quality_toolchain_v1.md`
- **Verified against:** SOURCE-PIN.md — pinned 2026-06-20 (re-checked live this pass)
- **Gate reports read:** `05_java_quality_toolchain_EXAMPLE.md` (build GREEN — `[INFO] BUILD SUCCESS`,
  4 tests / 0 Checkstyle / 0 SpotBugs / JaCoCo written), `05_java_quality_toolchain_CODEREVIEW.md`
  (VERDICT **PASS**, line 11; warning-clean `-Werror` verified via `mvn -X`).
  **`_VERIFY.md`, `_CLARITY.md`, `_AUDIT.md` still do NOT exist on disk** — the source-trace, neutrality,
  and voice scans below were performed first-hand by this reviewer to compensate (this remains the single
  reason ACCURACY is held at 8, not a content defect).
- **Scorer:** chapter-scorer agent (independent, harsh skeptical reviewer)
- **Date:** 2026-06-28
- **Lift-pass #:** Pass 2 (post-polish re-score; Pass 0 = 37, Pass 1 = 41)

---

## What the polish changed (verified in the draft this pass)

The Pass-1 score (41/50) named exactly two in-bounds line-level fixes as the path forward. Both are
confirmed applied, both in-bounds (no new facts, no scope creep, no floor risk):

1. **CLARITY fix #1 — routing-table "Moment" axis aligned + key added — DONE.** Line 59 now carries an
   explicit key: *"The **Moment** column uses the same seven lifecycle moments named above — author-time,
   compile-time, local build, pre-commit, PR / CI, platform / dashboard, runtime / production feedback —
   so this table and §'Two axes' speak one vocabulary."* The table's Moment cells (lines 63–74) now use
   those exact seven tokens, matching §"Two axes" (line 52) verbatim. The vocabulary mismatch that was
   the *only* stated reason CLARITY sat at 8 is gone → **CLARITY 8 → 9**.

2. **READABILITY fix #2 — the dense line-122 appositive split — DONE.** Line 122 is now three clean
   sentences ("The case for layering is the mechanism already established above, not a count. Because a
   source linter, a bytecode analyzer, and a compile-integrated checker reason over different artefacts,
   a few *complementary* tools each catch what the others structurally cannot. Piling on tools that share
   a vantage mostly multiplies redundant findings and noise.") — the overloaded single sentence is gone.
   READABILITY was already at 9; this removes the last dense clause it named but does not open a path to 10
   (a few necessarily-dense technical sentences remain) → **READABILITY 9 (held)**.

The two Pass-0/Pass-1 ACCURACY defects remain fixed and were re-checked: line 94 routes coverage/mutation
to **Chapter 23** (matches the routing table line 69 and FINAL_INDEX), Ch 33/34 refs correctly scoped;
the unpinned "overlap surprisingly little" empirical claim stays reframed to a mechanism argument with the
ScienceDirect study demoted to non-cited background (line 184).

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | First-hand banned-phrase sweep over the whole draft (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `obvious choice` / `no reason to use`): **0 hits**. No tool crowned. §Alternatives is approach-based (all-in-one platform vs best-of-breed) and explicitly non-crowning — "Neither is 'correct'; the trade-off is operational simplicity versus depth" (line 141). "The menu is not the order" (§Deep dive) refuses a ranking. No comparative superlative in any heading; tool names in headings are comparison-target content, allowed per `NEUTRALITY.md`. Every cross-tool placement traces to that tool's own pinned row. Code-review confirms no banned phrasing in the module/comments/README. |
| **B — HONEST-LIMITATIONS** | **PASS** | A dedicated six-item §Limitations (overlap/noise; build-time cost; false positives erode trust; green stack necessary-not-sufficient; the map is a snapshot; tools do not create culture) plus an explicit when-NOT-to-use in §When to use ("Do not treat the full menu as a checklist to install; that is the fast path to a noisy, slow, ignored gate," line 148). No tool sold cost-free. The companion module encodes a real failure path (LineItem validation) tested three ways (`_CODEREVIEW.md` §exemplary). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace (re-done first-hand):** every version literal in *prose* — g-j-f `1.35.0` (SOURCE-PIN §2 l.72), `spotless-maven-plugin 3.6.0` (§2 l.71, re-pinned 2026-06-27), JDK `21.0.11` (§1 l.53), Maven `3.9.16` (§4 l.93) — traces clean; tool flags (`-Xlint:all`, `-Werror`, `effort`/`threshold`, `prepare-agent`) and `findbugs-maven-plugin → spotbugs-maven-plugin` trace to each tool's pinned docs; engine deltas (Checkstyle 10.26.1, SpotBugs 4.9.3) live ONLY in the pom and are flagged (`09-flags/05_toolchain_plugin_versions.md`, `34`, `48`) and are explicitly NOT asserted in prose. **No invented atom in the prose. No empirical figure asserted as fact.** **COMPILE:** `_EXAMPLE.md` records `[INFO] BUILD SUCCESS` — `Tests run: 4, Failures: 0`; 0 Checkstyle; SpotBugs BugInstance 0; JaCoCo written. **CODE-REVIEW:** `_CODEREVIEW.md` **VERDICT: PASS** (line 11) — warning-clean `-Werror` (verified via `mvn -X`), idiomatic Java 21 record + `List.copyOf`, no secrets, prose↔code fidelity, neutral; 2 optional NITs, neither blocking. |

**All three floors PASS.** The aggregate is live — no floor caps the verdict.

---

## The five clusters (1–10)

| # | Cluster | Score | Justification |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The two-axis mental model (*what* it catches × *when* it runs) is stated cleanly and earns each later move; the source→javac→bytecode ASCII sketch (lines 82–85) makes the "why a layered stack sees more" point concretely; the three distinctions (source/bytecode/compile-integrated; lint vs test; tool vs platform) are crisp, and the CONCEPT callout glosses linter/analyzer/platform well. **Lifted 8→9 this pass:** the one residual CLARITY defect — the routing-table "Moment" column naming the lifecycle axis with a terser vocabulary than the §"Two axes" prose — is fixed by the new seven-moment key (line 59) and the table cells now matching the prose tokens verbatim, so the prose, the table, and the two figures speak one vocabulary and a reader no longer reconciles two naming schemes. Not a 10: a reader still assembles the full picture across two figures plus a table, but nothing now forces *extra* reconciliation work. |
| 2 | **ACCURACY** | **8** | Every *external pinned atom* is clean (re-verified first-hand above): prose version literals, tool flags, and the FindBugs→SpotBugs migration all trace to SOURCE-PIN/each tool's docs; engine deltas are flagged and kept out of prose; no empirical figure is asserted as fact. The two senior-visible defects remain resolved. **Held at 8 (not 9–10), unchanged by the polish and honestly so:** the polish was CLARITY/READABILITY-only and touched neither the facts nor the missing upstream gate. With `_VERIFY.md` absent, the source-trace was re-done in this single pass rather than banked, so I cannot certify the 9–10 anchor ("snippets verified with recorded paths, zero drift") to banked-evidence confidence. This is a **process gap, not a prose weakness** — the fix is banking the VERIFY report, not editing the chapter. |
| 3 | **UTILITY** | **9** | The routing table is a genuine "which tool for which problem → when → which chapter" lookup a lead keeps open while auditing a stack; every destination resolves correctly, and the new Moment-key makes the "when" column self-consistent with the prose, so the table now stands alone as a lookup without cross-referencing two vocabularies. "Start small and layer, expand as each earns its keep" is concrete and actionable; the companion pom makes the layering runnable and green. Just shy of 10 because the chapter is (correctly) a map: real doing is deferred to the chapters it routes to, so a reader acts *from the destinations*, not from this chapter alone. Held at 9. |
| 4 | **DEPTH** | **7** | **Structurally capped — and this is the honest ceiling, not a deficiency to lift.** A map/routing chapter routes depth forward *by design*. It still carries real, unique substance: the three analyzer-vantage distinctions, the platform-vs-best-of-breed trade-off, the mechanism case for layering, the dead-tool (FindBugs→SpotBugs) staleness lesson, and a buildable companion. That is a strong organizing frame plus a section's worth of material — a clean 7 on the rubric's own anchor (the 7–8 band wants "full mechanism + for + against + alternatives + when-to-use, all sourced"; this chapter has the frame and survey but *correctly routes the full mechanism forward* to Parts IV–IX, so it sits at the floor of that band). Unchanged by the polish — and it MUST stay unchanged: adding verified depth here would make it the deep chapters it points to (scope creep / padding, out of bounds). DEPTH has no legitimate in-bounds lever. |
| 5 | **READABILITY** | **9** | Concrete stakes-first hook (the new lead, the thirteen-tool firehose); locked voice held — no first-person/contraction slips in narration; zero banned filler/difficulty words; tables + ASCII + two figures break the grey; em-dash density inside the soft target (~6/1,000). **The line-122 appositive pile-up is now split into three clean sentences,** removing the last dense clause the Pass-1 score named. Held at 9 (was already 9): reads cleanly at full precision, but a few necessarily-dense technical sentences keep it just short of an effortless-throughout 10. |

**Cluster subtotal: 42 / 50**

---

## Verdict

- **Aggregate: 42 / 50** — +1 over Pass 1's 41 (the CLARITY fix landed; READABILITY was already at the
  top of its band), but **still 2 points below the active 88% auto-approval bar (≥44/50)**.
- **Floors:** A PASS · B PASS · C PASS (source-trace clean first-hand, build GREEN, CODE-REVIEW PASS).
- **No cluster below 6** (lowest is DEPTH 7).

**Phase-3 chapter scorecard: LIFT-LOOP — but read the honest call below.** Floors all PASS, so this is a
cluster-quality shortfall, not a floor return. The shortfall is **2 points**, and — stated plainly, as the
task asks — **this is a genuinely excellent chapter that is structurally capped below 44 for map-chapter
reasons.** Both in-bounds line-level fixes from the Pass-1 lift list are now spent; they were the only
legitimate prose levers, and both have been correctly applied. The remaining 2-point gap is carried by:

- **DEPTH 7** — a hard structural ceiling. A map chapter cannot go deeper without becoming the chapters it
  routes to. Lifting it would be padding, which the rubric forbids and which I will not reward.
- **ACCURACY 8** — capped by a *missing banked `_VERIFY.md`*, a process gap. The facts are clean; the cap
  is "evidence not banked," not "evidence wrong." Banking the VERIFY report would justify 9 and clear the
  bar at 43–44 — but that is an upstream-gate action, not a prose revision and not this scorer's to invent.

**Neither remaining gap is liftable by an in-bounds prose pass.** CLARITY and READABILITY are now at 9 with
their named defects fixed; UTILITY is at 9 and capped by the map form; pushing any of them to 10 would
require effortless-at-full-precision / keep-open-and-act-from-this-page-alone quality that a deliberately
thin orientation chapter does not aim for. **This is the structural-cap case: do not pad the map, do not
lower the bar.**

**One-line rationale:** The two targeted polish fixes landed cleanly (CLARITY 8→9, READABILITY split done),
taking the chapter to 42/50 — an excellent map chapter that sits 2 short of the 88% bar for two
non-prose-liftable reasons: a by-design DEPTH ceiling (7) and an ACCURACY cap (8) driven solely by the
absent upstream `_VERIFY.md`; this is a hold/route-to-human-gate candidate, not a chapter to inflate.

---

## Flagged weakest cluster

- **Weakest cluster:** DEPTH — score **7**.
- **Why it is the weakest:** It is the lowest cluster and its ceiling is *structural*, not defect-driven: a
  map/routing chapter routes depth forward by design, so it cannot be lifted without becoming the deep
  chapters it points to (scope creep / padding — out of bounds). There is **no legitimate in-bounds lever
  for DEPTH on this chapter.**
- **Honest read for the human gate:** the two in-bounds prose fixes are spent and correctly applied;
  CLARITY/READABILITY/UTILITY are all at 9 and capped by the map form. The only path to 44 that does not
  pad the chapter is **banking the missing `_VERIFY.md`** (an upstream-gate action), which would justify
  ACCURACY 8→9 and land the aggregate at 43, then a final CLARITY/UTILITY edge toward 10 *if* genuinely
  earned. If that VERIFY report is not banked and the chapter cannot otherwise reach 44 without inflating
  the map, **this is a hold / human-gate decision, not a chapter to pad.** Do not lower the bar; do not
  pad the map. A book-level note may be warranted: the 88% bar does not model the structural DEPTH ceiling
  of map/routing chapters (see Learnings).

---

## Line-level fixes (the lift list — for the drafter, in-bounds)

> Both Pass-1 prose fixes are now APPLIED (see "What the polish changed"). No further *in-bounds prose*
> fix remains that would move the aggregate — the residual gap is not a prose problem. The one remaining
> action is an upstream-gate action, not a drafter edit:

| # | Cluster / floor | Location | Issue | Fix (owner) |
|---|---|---|---|---|
| 1 | ACCURACY (process, not prose) | upstream gate (no `_VERIFY.md` on disk) | ACCURACY is held at 8 solely because the source-trace was re-done first-hand this pass, not banked. The facts are clean. | **Bank `05_java_quality_toolchain_VERIFY.md`** (source-verifier, Step 5) recording each prose atom → its pin. Justifies ACCURACY 8→9 (aggregate → 43). **Not a drafter prose edit.** |

> Note (process, not a chapter fix): no `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exist for this chapter.
> This independent pass re-did the source-trace, neutrality, and voice scans itself; the upstream gates
> should bank their own reports before the whole-book Step 16 gate. This is the single reason ACCURACY is
> held at 8 rather than 9.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 37 / 50 | PASS | PASS | PASS (trace clean · build GREEN · CR PASS) | LIFT-LOOP | initial independent score |
| 1 | 2026-06-28 | 41 / 50 | PASS | PASS | PASS (trace clean · build GREEN · CR PASS) | LIFT-LOOP | line-94 coverage/mutation route corrected to Ch 23; unpinned empirical claim reframed to mechanism; em-dash density 9.3→6.1/1,000 → ACCURACY 6→8, UTILITY 8→9, READABILITY 8→9. CLARITY held 8 (Moment-axis mismatch), DEPTH held 7. |
| 2 (re-score) | 2026-06-28 | 42 / 50 | PASS | PASS | PASS (trace clean first-hand · build GREEN · CR PASS) | LIFT-LOOP (structural cap) | Both Pass-1 prose fixes verified applied: (a) routing-table "Moment" axis aligned to the seven named lifecycle moments + explicit key (line 59) → CLARITY 8→9; (b) line-122 dense appositive split into three sentences → READABILITY held 9. ACCURACY held 8 (missing `_VERIFY.md`, process gap), DEPTH held 7 (map-chapter structural ceiling). +1 net. 2 short of 44; remaining gap is NOT in-bounds-prose-liftable. |

---

## Learnings & pipeline suggestions

- **The structural-cap case is now concrete and should be logged in PIPELINE-LEARNINGS.** This chapter is
  genuinely excellent — floors all PASS, every named defect fixed, three clusters at 9 — and it *still*
  cannot reach the 88% bar by in-bounds prose, because (i) DEPTH is structurally capped at 7 for a
  map/routing chapter and (ii) the only remaining ACCURACY point is a *banked-evidence* gap, not a prose
  gap. The lift loop is the wrong instrument once the prose levers are spent: looping a third prose pass
  here would only invite padding. The right disposition is **hold → human gate**, or **bank the missing
  upstream gate** (which is the genuine, non-padding path to 44). Recommend the rubric/PIPELINE explicitly
  carve a "structurally-capped map/routing chapter" disposition so this is routed to the human gate rather
  than looped into inflation.
- **A missing `_VERIFY.md` is now demonstrably worth a full ACCURACY point at the bar.** A clean-but-unbanked
  source-trace caps ACCURACY at 8; banking it justifies 9 and, here, moves the chapter from 42 to 43 —
  i.e. the single cheapest, fully in-bounds path toward the bar is a *process* action, not a prose edit.
  The status tooling should surface "scored but missing upstream gate reports" so this cap is never
  mistaken for a content weakness and the cheap fix is not overlooked. Strongly recommend running
  source-verify (Step 5) on key 05 before any further scoring.
- **`check_xrefs.sh` would have made the Pass-0 line-94 defect impossible** (every "Chapter NN" cross-ref
  resolved against FINAL_INDEX titles; every concern→chapter mapping cross-checked between a chapter's
  table and its prose). Still the cheapest guard against this defect class recurring across the remaining
  map chapters — re-raised because the chapter that needed it is the one that exposes the structural cap.
- **Re-verify FLOOR C live, every independent pass.** Confirmed GREEN via `_EXAMPLE.md` (`BUILD SUCCESS`,
  4/0/0, 0 Checkstyle, 0 SpotBugs, JaCoCo written) + `_CODEREVIEW.md` VERDICT: PASS (warning-clean
  `-Werror` verified via `mvn -X`) rather than trusting a stale summary. Gate reports for this chapter are
  slug-prefixed (`05_java_quality_toolchain_EXAMPLE.md`), not bare `_EXAMPLE.md` — check both naming
  conventions before concluding a report is absent.
