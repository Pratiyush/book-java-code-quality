# INDEPENDENT SCORECARD — Ch 3 "A Map of the Territory" (key 05)

> **Independent (different-model) re-score** per `SCORING.md` ship bar. Deliberately harsh, skeptical
> review: ≥44/50 (88%) awarded only if a senior Java engineer would find the chapter both *excellent*
> AND *error-free*, no cluster < 6, floors A/B/C-source PASS. This is a SCORE-ONLY pass — no draft edits,
> no lift loop applied. **This is the post-lift re-score** (the v1 draft was lifted after Pass 0).

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score, post-lift)
- **Dossier key:** 05 (solo) — FINAL_INDEX Ch 3
- **Slug:** `05_java_quality_toolchain`
- **Title:** A Map of the Territory (The Java quality toolchain — a map)
- **Part / arc position:** Part I — Foundations, Chapter 3
- **Artifact scored:** `03-drafts/05_java_quality_toolchain/05_java_quality_toolchain_v1.md`
- **Verified against:** SOURCE-PIN.md — pinned 2026-06-20 (re-checked live this pass)
- **Gate reports read:** `05_java_quality_toolchain_EXAMPLE.md` (build GREEN — `BUILD SUCCESS`),
  `05_java_quality_toolchain_CODEREVIEW.md` (VERDICT **PASS**, line 11). Build artifacts under
  `08-companion-code/05_java_quality_toolchain/target/` re-verified live this pass.
  **`_VERIFY.md`, `_CLARITY.md`, `_AUDIT.md` do NOT exist on disk** — the source-trace, neutrality, and
  voice scans below were performed first-hand by this reviewer to compensate.
- **Scorer:** chapter-scorer agent (independent, harsh skeptical reviewer)
- **Date:** 2026-06-28
- **Lift-pass #:** post-lift re-score (Pass 0 was 37/50 LIFT-LOOP; this pass scores the lifted draft)

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | First-hand banned-phrase sweep over the whole draft (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `obvious choice` / `no reason to use`): **0 hits**. No tool crowned. The §Alternatives passage is approach-based (all-in-one platform vs best-of-breed) and explicitly non-crowning — "Neither is 'correct'; the trade-off is operational simplicity versus depth" (line 141). "The menu is not the order" (§Deep dive) refuses a ranking. No comparative superlative in any heading. Every cross-tool placement traces to that tool's own pinned row. |
| **B — HONEST-LIMITATIONS** | **PASS** | A dedicated six-item §Limitations (overlap/noise; build-time cost; false positives erode trust; green stack necessary-not-sufficient; the map is a snapshot; tools do not create culture) plus an explicit when-NOT-to-use in §When to use ("Do not treat the full menu as a checklist to install; that is the fast path to a noisy, slow, ignored gate," line 148). No tool sold cost-free. The companion module also encodes a real failure path (LineItem validation) tested four ways. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** every version literal asserted in *prose* (g-j-f `1.35.0`, `spotless-maven-plugin 3.6.0`, JDK `21.0.11`, Maven `3.9.16`) traces to SOURCE-PIN §1/§2/§4; tool flags (`-Xlint:all`, `-Werror`, `effort`/`threshold`, `prepare-agent`) and `findbugs-maven-plugin → spotbugs-maven-plugin` trace to each tool's docs; engine deltas live only in the pom and are flagged — `09-flags/05_toolchain_plugin_versions.md`, `34_spotless_maven_plugin_version_unresolved.md`, `48_jacoco_pin_0816_unpublished.md` all confirmed on disk. **No invented atom in the prose.** The previously-flagged unpinned empirical claim is now reframed (see ACCURACY) so no unpinned figure is asserted as fact. **COMPILE:** live-verified GREEN — `surefire-reports` shows `Tests run: 4, Failures: 0, Errors: 0`; `checkstyle-result.xml` 0 `<error>`; `spotbugsXml.xml` 0 `<BugInstance>`; JaCoCo report written; `_EXAMPLE.md` records `[INFO] BUILD SUCCESS`. **CODE-REVIEW:** `_CODEREVIEW.md` **VERDICT: PASS** (line 11) — warning-clean `-Werror`, idiomatic Java 21 record + `List.copyOf`, no secrets, prose↔code fidelity, neutral. |

**All three floors PASS.** The aggregate is therefore live — no floor caps the verdict.

---

## Lift verification (the two Pass-0 ACCURACY defects + the READABILITY nit)

The Pass-0 score (37/50) flagged ACCURACY as the weakest cluster on two defects, plus a READABILITY
em-dash nit. All three are confirmed fixed in the lifted v1, in-bounds (no new facts, no scope creep):

1. **Internal cross-reference contradiction — FIXED.** Line 94 now routes "coverage and mutation gates
   near the end" to **"(Chapter 23)"**, matching the routing table (line 69: Test adequacy → **23**) and
   FINAL_INDEX (Ch 23 = "Coverage, mutation & test effectiveness"). The old "Chapters 33, 34" route is
   gone. The surviving Ch 33/34 references are now correctly scoped: line 94 "in CI (Chapter 34)" =
   CI platforms (FINAL_INDEX Ch 34 = "Coverage strategy, PR automation & CI platforms"); line 133
   build-time/CI-slowness cost → Chapter 33 (FINAL_INDEX Ch 33 = "Designing the CI pipeline & quality
   gates"). Every "Chapter NN" cross-ref now resolves to the right destination.

2. **Unpinned load-bearing empirical claim — REFRAMED.** The Pass-0 sentence ("Independent studies …
   overlap *surprisingly little*") is gone. Line 122 now argues from mechanism, not from an unpinned
   figure: "because a source linter, a bytecode analyzer, and a compile-integrated checker reason over
   different artefacts, a few *complementary* tools each catch what the others structurally cannot." The
   ScienceDirect study (line 184) is explicitly demoted — "(not pinned; read as background, not as a
   cited figure)." No empirical figure is asserted as fact anywhere in the chapter.

3. **Em-dash density — IMPROVED.** Recomputed over narration (HTML comments + fenced code stripped):
   **6.1 per 1,000 words** (14 dashes / 2,310 words), comfortably under the ~8/1,000 soft target. Was
   9.3/1,000 at Pass 0.

---

## The five clusters (1–10)

| # | Cluster | Score | Justification |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The two-axis mental model (*what* it catches × *when* it runs) is stated cleanly and earns each later move; the source→javac→bytecode ASCII sketch (lines 82–85) makes the "why a layered stack sees more" point concretely; the three distinctions (source/bytecode/compile-integrated; lint vs test; tool vs platform) are crisp and the CONCEPT callout glosses linter/analyzer/platform well. Held at 8 (not 9) by the one CLARITY nit untouched by the lift: the routing table's "Moment" axis ("author / pre-commit / CI") and the §"Two axes" prose name the same lifecycle axis with slightly different vocabularies (the prose's seven named moments vs the table's terser labels), so a reader reconciling the two figures works marginally harder than necessary. |
| 2 | **ACCURACY** | **8** | Every *external pinned atom* is clean (verified above) — strong, and unchanged from Pass 0. The two senior-visible defects that capped this at 6 are both resolved in-bounds: (a) the unpinned "overlap surprisingly little" empirical claim is reframed to a mechanism argument with the ScienceDirect study demoted to non-cited background — no figure asserted as fact; (b) the body's coverage/mutation route now reads **Chapter 23**, matching the chapter's own table and FINAL_INDEX, with the surviving Ch 33/34 references correctly scoped to CI-pipeline and CI-platform topics. Held at 8 (not 9–10) because two upstream evidence gates (`_VERIFY.md` absent) were re-done first-hand here rather than banked; full traced-with-recorded-paths confidence wants the VERIFY report on disk. |
| 3 | **UTILITY** | **9** | The routing table is a genuine "which tool for which problem → when → which chapter" lookup a lead keeps open while auditing a stack, and now every destination resolves correctly — the table's value *is* its destinations, and the line-94 mis-route that previously sent a reader to the wrong chapter is gone. The "start small and layer, expand as each earns its keep" guidance is concrete and actionable; the companion pom makes the layering runnable and green. Just shy of 10 because the chapter is (correctly) a map: real doing is deferred to the chapters it routes to, so a reader acts *from the destinations*, not from this chapter alone. |
| 4 | **DEPTH** | **7** | Appropriately and deliberately thin (a map chapter routes depth forward by design), but it still carries real substance: the three analyzer-vantage distinctions, the platform-vs-best-of-breed trade-off, the mechanism case for layering, the dead-tool (FindBugs→SpotBugs) staleness lesson, and a buildable companion. This is a 7 on its own terms — a section's worth of unique material plus a strong organizing frame — not a 9; almost all genuine depth is explicitly deferred to Parts IV–IX, which is the right call but is what a map chapter is. Unchanged by the lift (the reframe traded an unpinned figure for a mechanism argument — net-neutral on depth). |
| 5 | **READABILITY** | **9** | Concrete stakes-first hook (the new lead, the thirteen-tool firehose); locked voice held — no first-person/contraction slips in narration; zero banned filler/difficulty words; tables + ASCII + two figures break the grey. The em-dash density that held Pass 0 at 8 is now **6.1/1,000** (was 9.3), inside the soft target — the appositive dashes were tightened to periods/commas with no content change. Reads cleanly at full precision; just short of 10 by a couple of dense appositive clauses in §"The menu is not the order" (line 122). |

**Cluster subtotal: 41 / 50**

---

## Verdict

- **Aggregate: 41 / 50** — clears the legacy step-3 inclusion floor (≥35) and is a strong lift from 37,
  but **still below the active 88% auto-approval bar (≥44/50)** by **3 points**.
- **Floors:** A PASS · B PASS · C PASS (source-trace clean, build GREEN, CODE-REVIEW PASS).
- **No cluster below 6** (lowest is DEPTH 7).

**Phase-3 chapter scorecard: LIFT-LOOP.** Floors all PASS, so this is not a floor return — it is a
cluster-quality shortfall of **3 points** below the bar. The lift fixed the two ACCURACY defects exactly
as targeted (ACCURACY 6→8, dragging UTILITY 8→9 and READABILITY 8→9 with it), but a map chapter's
deliberate thinness caps DEPTH at 7 and the headroom in CLARITY/ACCURACY is now genuinely earned, not
defect-driven. Remaining gap is the hard part: lifting a map chapter past 88% without padding it past
what a map should be.

**One-line rationale:** The lift landed cleanly — both flagged defects resolved in-bounds and READABILITY
improved as a bonus — taking the chapter from 37 to 41, but it sits 3 short of the 88% bar, gated chiefly
by a map chapter's by-design DEPTH ceiling (7) and a residual CLARITY vocabulary mismatch.

---

## Flagged weakest cluster

- **Weakest cluster:** DEPTH — score **7**.
- **Why it is the weakest:** It is the lowest cluster and, unlike CLARITY, its ceiling is structural: a
  map/routing chapter routes depth forward by design, so it cannot be lifted by adding more material
  without becoming the deep chapters it points to (scope creep / padding — out of bounds). The next
  highest-leverage *in-bounds* move is therefore not DEPTH but **CLARITY** (the one residual defect a
  pass can legitimately fix): align the routing table's "Moment" column vocabulary to the seven named
  lifecycle moments in §"Two axes" so the prose and the two figures speak one vocabulary. That is a real,
  bounded clarity gain; DEPTH has no comparable in-bounds lever.
- **Honest read for the human gate:** after the next CLARITY pass, realistic headroom is CLARITY 8→9 and
  perhaps READABILITY/UTILITY edging toward 10 — plausibly reaching ~43–44. If a third pass cannot clear
  44 without padding DEPTH past what a map chapter should carry, this is a **cut/hold candidate for the
  human gate** rather than a chapter to inflate. Do not lower the bar; do not pad the map.

---

## Line-level fixes (the lift list — for the drafter, in-bounds)

| # | Cluster / floor | Location (§ · ¶ · line) | Issue | Fix |
|---|---|---|---|---|
| 1 | CLARITY | §"Two axes" (lines 50–53) vs routing-table "Moment" column (lines 61–74) | The lifecycle moments are named with two vocabularies between prose (seven named moments) and table (terser "author / pre-commit / CI" etc.). | Align the table's "Moment" labels to the seven named moments in the prose (or add a one-line key) so the two figures use one vocabulary. No content change. |
| 2 | READABILITY | §"The menu is not the order" · line 122 | Two dense appositive clauses in one long sentence. | Optionally split the line-122 sentence at the em-dash into two sentences; purely a pacing tweak, no content change. |

> Note (process, not a chapter fix): no `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exist for this
> chapter. This independent pass did the source-trace, neutrality, and voice scans itself, but the
> upstream gates should bank their own reports before the whole-book Step 16 gate — this is the single
> reason ACCURACY is held at 8 rather than 9.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 37 / 50 | PASS | PASS | PASS (trace clean · build GREEN · CR PASS) | LIFT-LOOP | initial independent score |
| 1 (re-score) | 2026-06-28 | 41 / 50 | PASS | PASS | PASS (trace clean · build GREEN · CR PASS) | LIFT-LOOP | Drafter's lift verified: (a) line-94 coverage/mutation route corrected to **Ch 23** (matches table + FINAL_INDEX); Ch 33/34 refs now correctly scoped → ACCURACY 6→8, UTILITY 8→9. (b) unpinned "overlap surprisingly little" empirical claim reframed to a mechanism argument, ScienceDirect study demoted to non-cited background → no figure asserted as fact. (c) em-dash density 9.3→6.1/1,000 → READABILITY 8→9. CLARITY held at 8 (Moment-axis vocabulary mismatch untouched); DEPTH held at 7 (map-chapter ceiling). +4 net. Still 3 short of the 88% bar. |

---

## Learnings & pipeline suggestions

- **The lift loop worked exactly as designed here — name one cluster, hand back one bounded pass, re-score
  all five.** Fixing the two ACCURACY defects lifted three clusters (ACCURACY, UTILITY, READABILITY)
  because the defects were cross-cutting (a wrong cross-ref dents both the fact and the table that depends
  on it). Worth noting in PIPELINE-LEARNINGS: the highest-leverage lift target is the cluster whose
  defects are *shared* by other clusters, not necessarily the single lowest score.
- **Map/routing chapters have a structural DEPTH ceiling the ship bar does not account for.** This chapter
  is correct as written and still cannot clear 88% without padding past what a map should be. A scripted
  `check_xrefs.sh` (every "Chapter NN" cross-ref resolved against FINAL_INDEX titles; every concern→chapter
  mapping cross-checked between a chapter's table and its prose) would have caught the Pass-0 line-94
  contradiction automatically and is the cheapest guard against this class of defect recurring. Strongly
  recommend adding it before the remaining map chapters are scored.
- **The ACCURACY-held-at-8-for-missing-VERIFY signal is real and should be visible.** A draft scored with
  no `_VERIFY/_CLARITY/_AUDIT` on disk forces the independent scorer to re-do those scans and legitimately
  caps ACCURACY at 8 (cannot certify full traced-with-recorded-paths confidence from a one-pass re-do).
  The status tooling should surface "scored but missing upstream gate reports" so this cap is not mistaken
  for a content weakness — the fix is banking the upstream gate, not editing the prose.
- **Re-verify FLOOR C live, every independent pass.** Re-running the build-artifact checks (surefire 4/0,
  checkstyle 0, spotbugs 0, JaCoCo written) plus reading `_CODEREVIEW.md` VERDICT: PASS confirmed the
  green state independently rather than trusting a stale summary. Note for future scorers: the gate
  reports for this chapter are slug-prefixed (`05_java_quality_toolchain_EXAMPLE.md`), not bare
  `_EXAMPLE.md` — check both naming conventions before concluding a report is absent.
