# INDEPENDENT SCORECARD — Ch 43 — model: Claude Sonnet 4.6 — 2026-06-20 (lift pass 1)

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard (re-score after lift pass 1)
- **Dossier key:** 101 (folds 102, 103, 104/51)
- **Slug:** `101_performance_profiling_memory_benchmarking`
- **Title:** Measure, Don't Guess — Performance as a quality attribute, profiling, allocation hygiene, and honest benchmarking with JMH
- **Part / arc position:** Part XIII — Performance & Observability (opener / umbrella, Ch 43)
- **Artifact scored:** `03-drafts/101_performance_profiling_memory_benchmarking/101_performance_profiling_memory_benchmarking_v1.md`
- **Figure artifact:** `05-figures/101_performance_profiling_memory_benchmarking/fig101_1.png` (rendered; sources traced in `fig101_1.sources.md`)
- **Source basis:** `02-research/101_performance_quality_attribute/101_performance_quality_attribute_RESEARCH.md`
- **Scorer:** chapter-scorer agent — Claude Sonnet 4.6 (independent; different model from drafter)
- **Date:** 2026-06-20
- **Lift-pass #:** 1

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | Verdict | Evidence |
|---|---|---|---|
| **A — NEUTRALITY** | No winner crowned; banned phrasings absent; comparative claims cited; no superlative section headings. | **PASS** | Full blocklist scan: no "better than", "unlike X", "the problem with X", "superior", "beats", or any variant found. Tool niches (JFR / async-profiler / commercial) assigned contextually, with JFR's "default first reach" framing justified by stated rationale (always-available, sub-1% overhead), not a verdict — async-profiler and commercial tools each receive strongest case and fair scope. "the book crowns none" is stated explicitly. No comparative superlative in any section heading. Alternatives section is approach-based, not a leaderboard. No new comparative claims introduced by the lift pass. |
| **B — HONEST-LIMITATIONS** | Every feature gets hardest objections + explicit when-NOT-to-use. | **PASS** | Dedicated "Limitations & when NOT to reach for it" section names 10 specific, hard limits. In-line limitations throughout: profiling shows WHERE not WHY/the-fix; sampling is statistical; DCE/constant-folding/no-warmup; micro≠macro (called "the loudest caveat"); escape analysis undercuts naive allocation advice; object pooling usually counterproductive; GC tuning workload-specific; targets must be real. Premature-optimization anti-pattern is the governing constraint, not a footnote. No feature presented as cost-free. |
| **C — SOURCE-TRACE** | Zero invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, quoted claims. | **PASS** | All specific technical claims hedged with pin-verification markers or matching dossier ⚠-marked content. JFR overhead "~1% (verify at the pin)" matches dossier. JEP 509 marked experimental. GC defaults hedged "verify defaults at the pin." JMH 1.37 confined to figure caption/sources file only (SOURCE-PIN.md §3 row ✅ pinned); not asserted in prose (safe — the dossier marks it ⚠verify-at-pin). JMH samples 08/09/10/38 referenced by role, not invented. Knuth paraphrased, not falsely quoted verbatim. Valhalla/value types correctly held AHEAD-OF-PIN. Figure atoms fully traced in `fig101_1.sources.md` — every label, tool name, metric, and annotation maps to the draft or dossier passage. No invented detail detected. |
| **C — COMPILE** | Companion module builds green via `./mvnw -B verify`. | **PENDING** | Separate track per scoring mandate. Front-matter: "EXAMPLE-BUILD = PENDING (toolchain READY: JDK 21.0.11+25.0.3; profiling/JMH runs toolchain+env-gated → REPRO PENDING-RUNTIME)." Does not affect source-trace verdict or aggregate. |
| **C — CODE-REVIEW** | Module passes CODE-REVIEW gate. | **N/A** | Per scoring mandate for this pass. |

All gateable floors: PASS. No floor failure. Scoring proceeds.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Justification |
|---|---|---|---|
| 1 | **CLARITY** | **8** | Fig 101.1 is now present and placed correctly at the head of "How it works" — the four-step loop (set target → profile → benchmark → gate) is rendered as a visual before the mechanism sections that unpack it, resolving the primary CLARITY cap from pass 0. Self-narration that diluted the deep-dive mechanism delivery is excised. The "Why before how" gate is satisfied throughout; CONCEPT callouts carry the mechanism cleanly; the "instrument that lies" metaphor unifies the four movements. One remaining gap keeps this at 8 rather than 9: the JMH mechanism is explained entirely in prose — no illustrative snippet shows the DCE failure mode or Blackhole idiom, so a reader cannot see the defended form beside the naive form. The prose explanation is sufficient for understanding but the chapter falls short of the "reader can reconstruct it from the chapter alone" 9-anchor. |
| 2 | **ACCURACY** | **8** | No new claims introduced by the lift pass. All specific facts carry pin-verification markers or match dossier ⚠-flagged content. JMH 1.37 correctly confined to figure sources only (not in prose body). Figure atoms traced exhaustively in `fig101_1.sources.md` — every element in the diagram traces to a draft or dossier passage, with ⚠ atoms (JFR ~1%, Knuth attribution, JMH 1.37) explicitly flagged. No invented rule IDs, API signatures, GAV coordinates, or benchmark figures detected. Valhalla AHEAD-OF-PIN discipline maintained. Score holds from pass 0. |
| 3 | **UTILITY** | **7** | Decision frame ("When to use what") and the 10-item limitations section remain concrete and actionable. The figure adds navigability — the loop diagram functions as a decision tool for when to enter the performance discipline loop. However, the central JMH utility gap remains: no code block demonstrates the wrong benchmark (DCE) versus the right benchmark (Blackhole + non-final @State + forks), which is the chapter's sharpest teaching instrument. A reader wanting to apply JMH guidance cannot see the pattern; they must carry prose description to implementation without a visible example. This is the single remaining blocker preventing a higher utility score. Score unchanged from pass 0. |
| 4 | **DEPTH** | **8** | No change in substance. The four-key umbrella covers genuine depth: second-tier JMH pitfalls (IDE final quick-fix re-enables constant folding, @Setup(Level.Invocation) "almost always wrong"), escape analysis as a reason to measure rather than assume, ThreadLocal risk with virtual threads, the epistemological framing (evidence requiring defended methodology). Cross-chapter routing is meaningfully placed. Score holds from pass 0. |
| 5 | **READABILITY** | **8** | The voice lift resolved the specific violations identified in pass 0. Self-narration excised from the deep-dive: "naming the thing that makes it hard is what turns 'use a profiler and JMH' into a worldview" gone; "This is the same shape as the book's deepest recurring theme, now in its sharpest form" gone; "The honest center, which governs the whole part" gone — the deep-dive now leads with the point rather than announcing it. The redundant "Next chapter teaser" section (near-duplicate of "Hand-off to the next chapter") is removed; the close is clean and forward-pulling. No narration-level contractions, no first-person, no filler phrases. Hook is concrete and stakes-first. Em-dash density remains moderately above the ~8/1,000-word soft target in the deep-dive but does not rise to an automatic flag; the prune removes sentences that were themselves em-dash carriers. The locked voice now holds throughout without lapses. |

**Cluster subtotal: 39 / 50**

---

## Verdict

**Aggregate 39/50** — all floors PASS; ship bar is ≥35/50 with no cluster below 6 (minimum cluster is 7); bar cleared.

**Ship-bar check:**

| Condition | Status |
|---|---|
| All three floors PASS | YES — A PASS · B PASS · C-source PASS (C-compile PENDING separate track) |
| Aggregate ≥ 35/50 | YES — 39/50 |
| No single cluster below 6 | YES — floor is 7 (UTILITY and prior CLARITY) |
| APPROVE threshold (≥45/50, 90%) | NO — 39/50 = 78% |

**Verdict: LIFT** — bar cleared at 39/50 (78%); all floors pass; the chapter is pipeline-shippable at the standard bar but does not reach the 90% independent-approve threshold requested for this re-score.

---

## Remaining blockers to 90% (≥45/50)

The gap is 6 points. With floors all passing, the path is cluster lift. The scores now standing are 8/8/7/8/8.

| Blocker | Cluster affected | Current score → target | Fix type |
|---|---|---|---|
| **No JMH illustrative snippet.** No code block shows the DCE failure mode (discard result, constant input) alongside the defended form (Blackhole / non-final @State). The most actionable teaching moment in a chapter about "benchmarks that lie" is described only in prose. | UTILITY (7 → 9+), with secondary lift to CLARITY (8 → 9) | prose-fixable (add ≤9-line illustrative snippet per VOICE-GUIDE; labeled "illustrative — companion module PENDING"; no floor risk) |
| **Em-dash density in deep-dive remains above ~8/1,000-word soft target.** Not a floor failure; soft target. A line-edit pass on the deep-dive paragraphs — converting remaining em-dash appositives to periods or commas — would tighten READABILITY further. | READABILITY (8 → 9) | prose-fixable |
| **Deep-dive length-to-new-substance ratio.** The deep-dive is substantive but carries some recapitulation of the "instrument that lies" framing already established in the hook and mechanism sections. Tightening the deep-dive to its epistemological argument (evidence vs ground truth; the cost of earned measurement) without re-narrating the mechanisms would raise the delight-beat density. | CLARITY (8 → 9), READABILITY (8 → 9) | prose-fixable |

**Realistic ceiling at lift pass 2** (one in-bounds pass on UTILITY only, targeting the snippet gap): UTILITY 7 → 9, CLARITY 8 → 9 (the snippet carries mechanism illustration that the prose-only explanation cannot), aggregate 41/50 (82%). A second pass on READABILITY/CLARITY (em-dash + deep-dive tighten) could reach 43/50 (86%). Reaching 45/50 (90%) would require all three, and the DEPTH score (currently 8) would need to move — which is unlikely without material the EXAMPLE-BUILD will supply. The practical 90%-path runs through the EXAMPLE-BUILD completion, which lifts ACCURACY to 9, UTILITY to 9, and CLARITY to 9 once the companion module exists and snippets are tag-region verified.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT (bar cleared; below 90%) | Initial independent score |
| 1 | 2026-06-20 | 39 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT (bar cleared; below 90%) | Fig 101.1 added (CLARITY +1); self-narration excised from deep-dive (READABILITY +1); redundant close section removed (READABILITY +1); UTILITY and DEPTH unchanged (snippet gap remains; depth substance unchanged) |

---

## Learnings & pipeline suggestions

1. **Figure arrival is a genuine CLARITY lever.** The CLARITY jump from 7 to 8 is directly attributable to the figure placement — not to prose revision. This confirms the pipeline-learnings entry from pass 0: figure-plan completion should be tracked as a gate input before scoring, not after, because a missing figure caps CLARITY mechanically even when the prose is otherwise sound.

2. **Voice-lift items are checkable in one pass if the lift list is precise.** All seven lift-list items from pass 0 were addressed in lift pass 1. The precision of the lift list (section, paragraph, specific phrase) made verification straightforward. The chapter-scorer should always produce line-level lift lists with section + paragraph anchors, not general directions.

3. **The 90%-independent-approve threshold is structurally harder than the 70%-ship-bar for concept chapters.** A concept/umbrella chapter without a companion code module in hand will score UTILITY at most 8 before EXAMPLE-BUILD completes, because the "a reader can act on this" bar requires something runnable to inspect. This is correct behavior — the threshold is calibrated to completed chapters, not PENDING ones. The pipeline should not expect an umbrella chapter with REPRO PENDING-RUNTIME to reach 90% until the module exists.

4. **Em-dash density in deep-dives is a recurring signal.** The deep-dive sections across multiple chapters in Part XIII accumulate em-dash appositives above the ~8/1,000-word soft target. A light mechanical pass (convert em-dash appositive → period or comma where the interruption adds nothing) should be a standing step before the readability gate for any section labeled "Deep dive."

5. **Two-point aggregate gain from a targeted voice/figure pass is achievable and repeatable.** The pass 0 → pass 1 lift (+2, from 37 to 39) came from two targeted fixes (figure + voice prune) without any new facts or scope creep. This is the correct shape of the bounded lift loop — it does not require rewriting the chapter, only removing what violates the law.
