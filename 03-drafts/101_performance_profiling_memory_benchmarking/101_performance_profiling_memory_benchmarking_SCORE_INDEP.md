# SCORECARD (INDEPENDENT) — key 101 · Ch 43 "Measure, Don't Guess"

> Independent (different-model) score per `SCORING.md`. Harsh-skeptic pass. Step-8 chapter scorecard.
> Five clusters 1–10 + floors A/B/C. Ship bar = ≥44/50, no cluster <6, floors A/B/C-source PASS.
> Supersedes the 2026-06-20 Sonnet-4.6 independent score (written before EXAMPLE-BUILD + CODE-REVIEW
> landed: it marked COMPILE PENDING / CODE-REVIEW N/A). Both gates have since PASSed (2026-06-26/27).

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 101 (frozen; owner, folds 102 + 103 + 51 + 104) — `FINAL_INDEX` Ch 43, OPENS Part XIII
- **Slug:** `101_performance_profiling_memory_benchmarking`
- **Title:** Measure, Don't Guess — performance as a quality attribute
- **Part / arc position:** Part XIII — Performance & Observability (opener / umbrella, Ch 43)
- **Artifact scored:** `03-drafts/101_performance_profiling_memory_benchmarking/101_performance_profiling_memory_benchmarking_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (JMH **1.37** re-confirmed 2026-06-27). Re-check date: 2026-06-28.
- **Scorer:** chapter-scorer agent (independent, harsh-skeptic)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (after one in-bounds ACCURACY pass)

---

## The five clusters (score 1–10) — FINAL (after Lift Pass 1)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The "instrument that lies" spine genuinely unifies the four movements; the measure→profile→benchmark→gate loop (Fig 43.1) carries the structure; each CONCEPT callout earns the next. A reader new to JMH can reconstruct *why* naive benchmarks lie (DCE / constant-folding / no-warmup) and what the three defenses do. Not 10: the memory section packs generational hypothesis + escape analysis + leak prevention + GC tuning densely into one section. |
| 2 | **ACCURACY** | **9** | Every load-bearing fact traces to the pin; JEP 509 correctly @pin-flagged as *experimental*; JFR ~1% carries "(verify at the pin)"; no timeless asserted perf number (10×/tenfold explicitly labelled the DCE *lie*); Valhalla AHEAD-OF-PIN never asserted. **Lift Pass 1** corrected the back-matter routing pointers that had contradicted the (correct) body. |
| 3 | **UTILITY** | **9** | Keep-open-while-working chapter: the loop, the wrong/right JMH pair backed by a green compiled module, the when-to-use list, and the explicit "most code: don't optimize" verdict. Decisions are concrete and backed by code, not plausible-looking fragments. |
| 4 | **DEPTH** | **8** | Full mechanism + for + against + alternatives + when-to-use across four sub-domains, all sourced; the deep-dive's unifying-discipline argument is non-obvious and earns its place. Honest cap: as a 4-key merge it is breadth-over-depth in spots (GC internals, escape-analysis specifics, async-profiler internals are named, not deep-dived) — right for an umbrella opener, but an 8. Not lifted: more depth needs new material = out of bounds (padding/new facts). |
| 5 | **READABILITY** | **9** | Em-dash density 5.54/1000 (ceiling 8); zero banned filler/difficulty words; locked voice held; CONCEPT callouts + posed-question device used well; clean forward hand-off. No jargon wall. |

**Cluster subtotal:** **44 / 50** (was 43/50 at Pass 0; ACCURACY 8→9).

---

## The three content-floors (PASS / FAIL — independent of score)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan CLEAN over body + code (`better than / unlike X / superior / beats / the problem with X` etc. = 0). Profilers "crowned none"; alternatives framed as niches/complementary (JFR vs async-profiler vs commercial; sampling vs instrumentation; JMH vs hand-rolled `nanoTime`; allocation-reduction vs pooling; micro vs macro). "Avoids the safepoint bias of older samplers" describes async-profiler's own mechanism, not a verdict on a named rival. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" section, 10 explicit when-NOT bullets; every feature carries costs (profiling shows where-not-why-or-the-fix; pooling usually backfires; micro≠macro = "the loudest caveat"; GC-tuning workload-specific; targets-must-be-real; naive benchmarks lie). No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE:** every atom → pin (`_EXAMPLE.md` source-trace table); JMH 1.37 = SOURCE-PIN §3 row; zero invented atoms; no asserted timeless perf number; JEP 509 @pin-flagged; Valhalla AHEAD-OF-PIN not asserted. **COMPILE:** `_EXAMPLE.md` — `mvn -B -Pquality verify` BUILD SUCCESS on JDK 21.0.11 (12 tests, 0 Checkstyle, 0 SpotBugs, warning-clean); JMH 1.37 harness compiles (built, not run); 5/5 snippet tags resolve ≤9 lines (verified live: state-setup 8, lying 6, honest 6, blackhole 7, allocation-reduced 6). **CODE-REVIEW:** `_CODEREVIEW.md` PASS — 0 blocker / 0 major / 3 optional nits, 6 dimensions PASS, perf-number audit clean. |

All three floors **PASS**. Code untouched by the lift → FLOOR C state unchanged from the gate reports; no rebuild / `check_snippets` owed (`git status` confirms only the draft `.md` changed for key 101).

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — 44/50, no cluster below 6, all three floors PASS. Ready for the human approval gate (Step 12).
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** A disciplined, neutral, source-traced umbrella opener with a genuinely unifying "instrument that lies" spine and a green, code-reviewed companion module; the one reader-facing accuracy defect (stale back-matter chapter pointers) was corrected in one in-bounds pass to clear the 44 bar.

---

## Flagged weakest cluster (drove the lift loop)

- **Weakest at Pass 0:** tie at 8 — ACCURACY and DEPTH.
- **Why ACCURACY was the lift target (not DEPTH):** DEPTH could only rise by adding material (out of bounds — padding / new facts / scope creep). ACCURACY had a precise, zero-floor-risk, already-verified-material fix: the back-matter "sources & traceability" routing block carried **dossier-key-era chapter numbers that contradicted the locked FINAL_INDEX and the (correct) body** — `resources → Ch 8` (key 16 is Ch 10), `IDE inspections → Ch 9` (key 36 is Ch 17), `records/value → Ch 4` (keys 10/13 are Ch 8/5). Reader-facing pointers resolving to the wrong chapter are an accuracy defect.
- **Highest-leverage move:** re-map the three stale pointers to FINAL_INDEX (Ch 8→10, Ch 9→17, Ch 4→8), keeping the correct frozen dossier keys.

---

## Line-level fixes (applied in Lift Pass 1)

| # | Cluster | Location (section · ¶) | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | ACCURACY | Back matter · Memory/allocation bullet | `unclosed-resources Ch 8` + `Records/value (Ch 4/5 key 10/13)` — keys 16/10/13 live in Ch 10 / Ch 8 / Ch 5 | → `unclosed-resources Ch 10`; `Records/value (Ch 8/5 key 10/13)` |
| 2 | ACCURACY | Back matter · Benchmarking bullet | `IDE-final-quickfix breaks it (Ch 9 key 36)` — key 36 is Ch 17; body already says "Chapter 17" | → `(Ch 17 key 36)` |
| 3 | ACCURACY | Back matter · Routing line | `resources → Ch 8 (16)`; `records/value → Ch 4/5 (10/13)`; `IDE inspections → Ch 9 (36)` | → `Ch 10 (16)`; `Ch 8/5 (10/13)`; `Ch 17 (36)` |

> Narrative body required **no** change — every body chapter pointer (1, 2, 5, 10, 17, 23, 38) and Part ref (III, IX, XIII) already resolved correctly against FINAL_INDEX (incl. the load-bearing "Chapter 17's inspections" and "try-with-resources, Chapter 10"). No code touched; em-dash density (5.54/1000) and banned-term scans unaffected; FLOOR C unchanged.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIM | SOURCE-TRACE/COMPILE/CODE-REVIEW | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | LIFT | initial independent score (C9 A8 U9 D8 R9); weakest = ACCURACY/DEPTH tie at 8 |
| 1 | 2026-06-28 | **44 / 50** | PASS | PASS | PASS | **SHIP** | corrected 3 stale back-matter chapter pointers → FINAL_INDEX (ACCURACY 8→9); body unchanged, code untouched |

---

## Learnings & pipeline suggestions

- **The back-matter key-shorthand block is a recurring accuracy trap.** The narrative body used correct FINAL_INDEX chapter numbers throughout, but the back-matter "sources & traceability" routing line preserved **dossier-key-era chapter numbers** (`Ch 8`/`Ch 9`/`Ch 4`) that the lock to 47 chapters had remapped. The keys were right; the chapter numerals were stale, and they contradicted the body. Recommend a scripted `lint_citations` rule: for every back-matter `Ch N (key K)` pair, assert that `FINAL_INDEX` maps key `K` to chapter `N` (the merge map makes this fully checkable), and flag any mismatch — greppable, and it would have caught all three at draft time.
- **Lift-target selection under the bound matters.** With two clusters tied at the weakest score, the rubric's in-bounds constraint (no new material) is what breaks the tie: DEPTH was unliftable without padding, ACCURACY had a verified-material fix. Worth a one-line note in `SCORING.md`'s lift-loop section that when the weakest is a tie, prefer the cluster with an in-bounds fix that risks no floor.
- **JEP 509 / Valhalla discipline held cleanly** — experimental status flagged, AHEAD-OF-PIN never asserted; the only perf "numbers" are either flagged (`~1%` + "verify at the pin"), labelled-as-the-lie (`10×` DCE), or target-types (`p99`). Recommend citing module 101 as the reference shape in `EXAMPLES-GUIDE` for honest-benchmark + no-asserted-number modules.
- **Stale independent scorecard hazard:** the prior `_SCORE_INDEP.md` (2026-06-20, Sonnet 4.6) predated EXAMPLE-BUILD/CODE-REVIEW and recorded COMPILE PENDING / CODE-REVIEW N/A. A re-score after those gates land must supersede it; recommend `status.py` flag a `_SCORE_INDEP.md` older than the chapter's `_EXAMPLE.md`/`_CODEREVIEW.md` as stale.
- Append to `00-strategy/PIPELINE-LEARNINGS.md`.
