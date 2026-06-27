# INDEPENDENT SCORECARD — Ch 47 / key 110 — model: Claude Opus 4.8 — 2026-06-28 (lift pass 2; supersedes the 2026-06-20 Sonnet-4.6 score)

> Independent (different-model) score + bounded lift loop. Harsh-skeptic pass. Rubric: `00-strategy/SCORING.md`
> (five clusters 1–10 + floors A/B/C + the 44/50 ship bar). Form: `templates/SCORE-TEMPLATE.md`.

## Header

- **Mode:** [x] Phase-3 chapter scorecard (step 8) — **independent re-score**
- **Dossier key:** 110 (single key)
- **Slug:** `110_maturity_model_roadmap`
- **Title:** Where to Start, and How to Keep Going (a code-quality maturity model & adoption roadmap)
- **Part / arc position:** Part XIV — Capstone & Synthesis · Chapter 47 (THE FINAL CHAPTER; closes the book)
- **Artifact scored:** `03-drafts/110_maturity_model_roadmap/110_maturity_model_roadmap_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build green), `_CODEREVIEW.md` (CODE-REVIEW PASS); flag `09-flags/85_dora_*`
- **Verified against:** SOURCE-PIN (Java 21.0.11 anchor; corrected pin 2026-06-27) + FINAL_INDEX (LOCKED 2026-06-20)
- **Scorer:** chapter-scorer (independent — Claude Opus 4.8, not the authoring model)
- **Date:** 2026-06-28
- **Lift-pass #:** 2 (cleared bar; ≤3)

---

## The five clusters (final, after lift pass 2)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Mechanism explained cleanly and ordered cheapest-first: five stages each earning the next, plus the model's spine (overall level = the LOWEST dimension; discount the stalled dimension → RestoreOutcomes; Sustain past threshold). The "why" (Goodhart, incremental-not-big-bang) is explicit. CONCEPT callouts + fig 47.1 + 5 verified snippets carry the spine; not a wall of grey text. |
| 2 | **ACCURACY** | **9** | Every tool/version traces (FLOOR C report clean); the load-bearing DORA "capabilities-over-levels" framing is correctly **attributed + hedged + flagged** (flag 85), with **no DORA performance band or statistic asserted** anywhere. After lift pass 1, all ~30 chapter cross-references resolve to the LOCKED FINAL_INDEX (the one drifted pointer — ArchUnit — corrected). Snippets verified to real ≤9-line tag regions with recorded paths. |
| 3 | **UTILITY** | **9** | The page a team keeps open when deciding where to start: a Monday-first sequence, an explicit "start where the pain is," a runnable model that refuses vanity climbing, and a When-to-use / Never list. Directly answers "where do I start, what's next?". |
| 4 | **DEPTH** | **8** | Full mechanism + the closing-honesty limitations (6 when-NOT bullets) + approach-based alternatives + when-to-use, all sourced; the thesis is encoded in a tested code path, not just prose. Honestly capped at 8: pure synthesis by design (no new primary atoms) — lifting further would require out-of-bounds new facts/padding. Not word-count-padded. |
| 5 | **READABILITY** | **9** | Locked voice holds throughout (third person, no narration contractions, terms glossed, callouts sparing). After lift pass 2 the **reader-facing prose em-dash density is 7.0/1,000** (within the ~8 target; the pass-0 "10.1" was inflated by counting back-matter sourcing/routing-arrow metadata as prose). Send-off lands; rhythm varied. |

**Cluster subtotal: 44 / 50**

---

## The three content-floors (all PASS)

| Floor | PASS/FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan over the whole draft: zero hits (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms`). The chapter **crowns nothing** — it orders practices, naming the chapter for each, and explicitly states "there is no best tool" / "Crown nothing." The "best" hits are "its best engineers" (people, in the hook) and the anti-crowning thesis. CODE-REVIEW dim. 6 (neutrality-in-code) PASS. |
| **B — HONEST-LIMITATIONS** | **PASS** | A dedicated "Limitations & when NOT to reach for it" section: maturity-as-vanity-metric, roadmap-is-a-default-not-a-plan (start where YOUR pain is, not Stage 0 dogmatically), tools-without-culture-fail, more-maturity≠more-value-past-a-point, everything-here-is-dated, no-"done." Each carries an explicit when-NOT-to-use. The limitations are also encoded in the model (RestoreOutcomes / Sustain). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | (1) Source-trace: zero invented atoms; the one framing fact (DORA capabilities-over-levels) is attributed + `⚠ verify-at-pin` (flag 85), no band asserted; the only version skews are the already-flagged reactor-wide set (flag 05). (2) COMPILE: `mvn -B -Pquality -f .../110_maturity_model_roadmap/pom.xml clean verify` = BUILD SUCCESS, 12 tests / 0 Checkstyle / 0 SpotBugs, warning-clean, Java 21.0.11 (`_EXAMPLE.md`). (3) CODE-REVIEW: **PASS**, no BLOCKER/MAJOR, all six dimensions PASS (`_CODEREVIEW.md`). All 5 displayed tag regions confirmed present in source. |

---

## Verdict

**Phase-3 chapter scorecard: SHIP.**

**One-line rationale:** 44/50, no cluster below 6, all three floors PASS on an independent score after a 2-pass in-bounds lift loop — clears the 88% auto-approval bar; the load-bearing DORA framing is handled honestly (attributed, hedged, flagged 85, no band asserted), so accuracy is not bought by overstating an unverified claim.

---

## Flagged weakest cluster (resolved to bar)

- **Weakest at pass 0:** ACCURACY — 7 (one cross-reference drifted from the LOCKED FINAL_INDEX).
- **Why it was weakest:** for a pure-synthesis closer whose entire value is "the whole book mapped into an order," a wrong chapter pointer is a content defect, not cosmetic.
- **Highest-leverage move (applied):** correct ArchUnit's pointer to Chapter 26 (keys 55/33) — it was cited as "Chapters 16, 25"; Ch 16 is the four bug/style analyzers (keys 27–30) and Ch 25 is SOLID/coupling, while ArchUnit + fitness functions live in Ch 26. Fixed in body + front-matter + back-matter.

---

## Line-level fixes (the lift list — all applied)

| # | Cluster/floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | ACCURACY | "How it works" Stage-2 CONCEPT (line 47) + front-matter (line 7) + back-matter (line 132) | ArchUnit cited as "Chapters 16, 25"; FINAL_INDEX places ArchUnit (keys 55/33) in **Ch 26**, and Ch 16 = the four analyzers (keys 27–30) | Changed all three to **Chapter 26** | DONE (pass 1) |
| 2 | READABILITY | Overview (line 34) | Gratuitous appositive em-dash pair around the DORA-lens clause | Converted to comma-bracketed clause | DONE (pass 2) |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 41 (9/7/9/8/8) | PASS | PASS | PASS | LIFT-LOOP | initial independent score; ArchUnit cross-ref drift found vs LOCKED FINAL_INDEX; em-dash density measured 10.1/1k (later shown inflated by back-matter metadata) |
| 1 | 2026-06-28 | 43 (9/9/9/8/8) | PASS | PASS | PASS | LIFT-LOOP | ACCURACY 7→9: ArchUnit pointer corrected to Ch 26 in body + front-matter + back-matter; FLOOR A re-scanned clean; no code touched (prose-only) |
| 2 | 2026-06-28 | **44 (9/9/9/8/9)** | PASS | PASS | PASS | **SHIP** | READABILITY 8→9: appositive em-dash pair converted; prose-only density confirmed **7.0/1k** (within ~8 target). Bar cleared in 2 passes |

---

## Learnings & pipeline suggestions

- **Em-dash density must be measured on the reader-facing prose surface, not the back-matter sourcing block.** This chapter's back-matter traceability apparatus (routing arrows `→`, the dense single-bullet source map) carried ~15 em-dashes and a routing-arrow cadence that inflated the whole-file figure to 10.1/1k while the actual chapter prose was 7.0/1k — already within target. Recommend the AUDIT em-dash scan (and the drafter's prune pass) exclude the `## Back matter — sources & traceability` block, or report prose-only and metadata separately, so a synthesis chapter with a heavy source map is not falsely flagged.
- **For a pure-synthesis chapter the cross-references ARE the load-bearing facts.** The only real accuracy defect was a chapter pointer drifting from the LOCKED FINAL_INDEX (ArchUnit → "16, 25" instead of 26). A closer that maps the whole book should get a scripted cross-ref lint: extract every "Chapter NN / key NN" claim and resolve it against `01-index/FINAL_INDEX.md`; a mismatch is an ACCURACY ding even when no tool/version is wrong. Worth adding to `reconcile_facts` or a small new `check_xrefs.sh`.
- **DEPTH is correctly capped, not penalized, for synthesis-by-design.** An 8 here is the honest ceiling: the chapter adds no new primary atom on purpose, and lifting DEPTH would have meant inventing material — out of bounds. The loop correctly pivoted to READABILITY rather than padding DEPTH.
- **Encoding a chapter's limitations in a tested code path (RestoreOutcomes / Sustain / lowest-not-average) made FLOOR B and the ACCURACY/UTILITY judgement fast and unambiguous.** Confirms the example-builder/code-reviewer learning: for closer/capstone modules, require the honest-limitations to live in the code path under test, not only in prose.
- Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
