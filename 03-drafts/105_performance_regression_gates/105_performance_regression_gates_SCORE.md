# SCORECARD — Ch 44 "Performance-regression gates" (key 105)

> Part XIII MIDDLE (Ch 43-45; Ch 45 observability CLOSES Part XIII). Single dossier. Delivers the MACRO/load
> half Ch 43 left open + the gate protecting measured performance. Shapes: performance-is-a-fitness-function
> -but-a-noisy-one · noise-is-the-enemy/flag-then-investigate (the flaky-gate lesson for perf) ·
> relative-to-baseline-not-absolute · gate-on-macro-what-users-feel · thresholds-from-requirements-not-round
> -numbers · green-gate≠fast-enough · perf-gate-is-slow-so-nightly-not-PR · baseline-ratchet.
> Draft: `105_performance_regression_gates_v1.md`. Pin 2026-06-20.

---

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard
- **Dossier key:** 105 (frozen key, `01-index/CANDIDATE_POOL.md`) — Chapter 44 in `FINAL_INDEX`
- **Slug:** `105_performance_regression_gates`
- **Title:** The Thousand Cuts (performance-regression gates: load/macro testing + the noise-aware gate)
- **Part / arc position:** Part XIII — Performance & Observability, Ch 44 of 43-45 (middle)
- **Artifact scored:** `03-drafts/105_performance_regression_gates/105_performance_regression_gates_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-27)
- **Scorer:** chapter-scorer agent — **main-loop SELF-score** (does NOT auto-approve; an independent
  different-model re-score is required to clear the 88% bar into `04-approved/`)
- **Date:** 2026-06-27
- **Lift-pass #:** 1 (voice/readability/clarity lift applied this pass)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---:|---|
| 1 | **CLARITY** | 9 | The thousand-cuts hook (3 ms × 100 PRs → tripled p99, no culprit commit) frames *why* perf needs a gate a human cannot be; the load-testing → gate → noise → placement spine is ordered and each step earns the next. Three CONCEPT callouts (fitness-function · noise-is-the-enemy/flag-then-investigate · the gate measuring what users feel) anchor the mechanism. Lift added a figure-intro before Fig 44.1 (was dropped cold) and a plain gloss of *tolerance band* on first load-bearing use, closing the two remaining clarity gaps. |
| 2 | **ACCURACY** | 8 | Gate mechanics, the three levels (micro/macro/resource), and noise handling all trace to the dossier + cross-refs (JMH 1.37 Ch 43; load testing; CI gates Ch 33; DORA Ch 38). Thresholds are explicitly ILLUSTRATIVE, not asserted; test numbers labelled SYNTHETIC; the `200ms` is quoted only as the absolute-threshold anti-pattern the gate rejects; the 3 ms / tripled-p99 is the narrative hook, not a measurement of this system. −2: single-dossier topic; live JMH/load-runner is REPRO PENDING-RUNTIME (modelled, honestly recorded). No invented atom. |
| 3 | **UTILITY** | 9 | A complete, honest perf-gate recipe a working developer can act on: load/macro-test the system, gate relative-to-baseline with tolerance bands + confidence intervals on a stable env, flag-then-investigate small diffs, run nightly/dedicated not on the PR fast path, ratchet the baseline on sanctioned changes, derive thresholds from real requirements. Backed by a green, unit-tested companion module realizing the gate itself. |
| 4 | **DEPTH** | 8 | The noise-inverts-the-gate-posture argument (block → investigate; the one thing that does NOT transfer from a correctness gate; deliberately softer, inverting the un-bypassable instinct of Ch 35) plus green-means-stable-not-good (the gate protects against regression, not against being slow, and gates a proxy) is sharp senior material. −2: a focused single-key middle chapter, not a contested/foundational deep-dive. |
| 5 | **READABILITY** | 8 | Locked third-person voice held throughout; no narration contractions, no second person. Prose-body em-dash density **6.68 / 1000** (below the ~8 target — measured, not mass-converted). Lift removed two self-narration tells ("the point of the next section", "the value of seeing it that way") and one defensive "not just" + one narration contraction ("doesn't") in the back matter. Three callouts and the fitness-function-with-a-noise-problem synthesis carry the rhythm; clean pre-release-gate → production-observability hand-off. |

**Cluster subtotal:** **42 / 50** (none < 6)

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ☑ PASS | Scripted banned-phrase sweep over the full draft = **0 hits** (better than / unlike X / the problem with / superior / beats / outperforms / kills / destroys / blows away / obvious-choice-over / no-reason-to-use). The comparative axes (macro-vs-micro gate · flag-then-investigate-vs-hard-block · relative-vs-absolute · nightly-vs-per-PR · pre-release-gate-vs-monitoring) are framed as right-tool-for-the-noise trade-offs, never crownings. No load-test tool crowned (Gatling/JMeter/k6 named as a neutral class, "cite the specific tool when naming one"). Perf framed as one fitness function among several. Confirmed by CODE-REVIEW dim 6 (zero banned hits in module + draft). |
| **B — HONEST-LIMITATIONS** | ☑ PASS | Dedicated `## Limitations & when NOT to reach for it` (7 items: noise→flaky→disabled; green-gate≠fast-enough; microbenchmark-regression≠real; slow+infra-cost / not-every-team; thresholds-from-requirements; protects-regression-not-design; baselines-need-management) + the deep-dive noise-inverts-the-posture + green-means-stable-not-good center + the explicit `**Not when:**` verdict ("a low-traffic internal tool does not justify the infrastructure"). Every feature carries its cost and a when-NOT. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ☑ PASS | **SOURCE-TRACE:** zero invented rule IDs/keys/flags/GAV/version/benchmark figures — thresholds ILLUSTRATIVE, test numbers SYNTHETIC, JMH 1.37 named-and-pinned but not vendored, live run recorded REPRO PENDING-RUNTIME (`_EXAMPLE.md`, `_CODEREVIEW.md` dim 5). **COMPILE:** `mvn -B -Pquality verify` → **BUILD SUCCESS**, 10 tests / 0 fail, 0 Checkstyle, 0 SpotBugs, warning-clean, JDK 21.0.11 (`_EXAMPLE.md` FLOOR-C guard; `_CODEREVIEW.md` build line). **CODE-REVIEW:** **PASS-WITH-FIXES** — all six dimensions PASS; only NIT/MINOR polish (Javadoc "never null" not mirrored by a guard; one properties file missing the "illustrative" word; dead default literals), none blocking FLOOR C. All 4 displayed tag regions brace-balanced, ≤9 lines, complete statements. |

All three floors PASS. The lift altered no floor-bearing content: the FLOOR A sweep stayed at 0, the FLOOR B section + when-NOT verdict are intact, and no verified atom / snippet-include directive / `// tag::` region / perf-number label was touched.

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — would clear the legacy ≥35/50 bar, but the active **auto-approval bar is ≥44/50 on an
      INDEPENDENT score**; this self-score (42/50) does not auto-approve.
- [x] **LIFT-LOOP → hand to INDEPENDENT score** — floors A/B/C PASS; cluster quality lifted this pass; the
      chapter now needs the independent (different-model) re-score to clear 88% into `04-approved/`.
- [ ] **CUT** — n/a; no floor failure, no structural defect.

**One-line rationale:** All three floors PASS and the prose is on-voice after the lift; the aggregate (42/50,
none < 6) clears the legacy inclusion bar but sits 2 points under the active 88% auto-approval bar, so the
chapter routes to an independent re-score rather than auto-approving on this self-score.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY and DEPTH are tied at **8** — both capped by the chapter being a single-dossier
  middle chapter (ACCURACY also by the honest REPRO PENDING-RUNTIME on the live run).
- **Why it is the weakest:** Both are structurally bounded, not defective — the topic is focused and one of its
  two facts (a live measurement) is environment-gated and correctly modelled rather than faked.
- **Single highest-leverage move to lift it:** Out of bounds for the lift loop (would require a new verified
  source or a runnable live benchmark = new fact / scope). Left as-is; the honest REPRO label is the correct
  call over inventing a number. Recommend the independent scorer weigh whether the macro/load level warrants
  one more sourced cross-reference from Ch 43's measurement material (already-verified, in-bounds for a drafter).

---

## Line-level fixes (the lift list — applied this pass)

| # | Cluster / lever | Location | Issue | Fix applied |
|---|---|---|---|---|
| 1 | CLARITY / (d) figure-intro | `## How it works`, before Fig 44.1 | Figure dropped in cold under the heading with no prose introducing it (VOICE-GUIDE: place the prose immediately before the listing; name what it shows). | Added one introducing sentence naming what Fig 44.1 traces (measurement → baseline → relative move → three-way verdict). |
| 2 | CLARITY / (c) gloss term | `### Handling noise`, noise CONCEPT callout | *tolerance band* used at first load-bearing point without a plain gloss. | Added an inline gloss: "a margin around the baseline inside which a difference is treated as noise, not a regression." |
| 3 | READABILITY / (f) self-narration | comparison para before `regression-gate` include | "and — the point of the next section — fails *safe*" pointed at structure (the next heading already announces noise). | Removed the structural pointer; kept "fails *safe* when the move could be noise." |
| 4 | READABILITY / (f) self-narration | `## Deep dive`, first para | "The value of seeing it that way is that it inherits…" announced a framing. | Tightened to "Seen that way, it inherits the whole book's gate discipline at once." |
| 5 | READABILITY / (b)+(e) | back-matter Companion-module para | Defensive "not just a number" + narration contraction "doesn't". | "not just a number" → "alongside the measured value"; "doesn't replace it" → "does not replace it." |

> **Em-dash lever (a) — NOT applied, by design.** Prose-body density measured **6.45 / 1000 before** the lift
> (target ~8); the lever instruction is "convert only if above ~8." No mass conversion was performed. The one
> gloss appositive added in fix #2 moved the body density to **6.68 / 1000**, still under target. Most of the
> file's em-dashes live in the metadata comment header and the back-matter reference machinery, which are not
> prose cadence and out of scope for the lever.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---:|---|---|---|---|---|
| 0 | 2026-06-20 | 42 / 50 | PASS | PASS | source PASS; COMPILE pending | self-score | initial self-score (pre-EXAMPLE-BUILD wording, legacy ≥35 bar) |
| 1 | 2026-06-27 | 42 / 50 | PASS | PASS | **all PASS** (BUILD SUCCESS 10/10; CODE-REVIEW PASS-WITH-FIXES) | LIFT-LOOP → independent score | EXAMPLE-BUILD + CODE-REVIEW landed green → FLOOR C fully PASS; voice/readability/clarity lift: figure-intro added, *tolerance band* glossed, two self-narration tells removed, one defensive "not just" + one narration contraction fixed. Floors A/B/C and all verified atoms/snippet-includes/tag-regions/perf-number labels intact. Aggregate unchanged (quality consolidated at 42; the lift moved nothing below 6 and added no padding). |

---

## Learnings & pipeline suggestions

- **Measure the lever's metric before pulling it.** The em-dash brief said "convert only if above ~8/1000."
  The *whole-file* count (46 dashes / 9.53 per 1000 over 3254 words) reads over target, but the **prose-body**
  count (17 dashes / 6.45 per 1000) is comfortably under — the excess lives in the metadata comment header and
  the dense back-matter sources/companion machinery. Mass-converting on the whole-file number would have churned
  on-voice prose against a passing measurement. Suggest EXAMPLES/AUDIT tooling compute em-dash density on the
  prose body (strip the HTML metadata comment, the include directives, the figure caption, and the back-matter
  reference block) rather than the raw file, so the gate flags genuine cadence overuse and not reference density.
- **The cheapest readability defects cluster in the back matter.** Both the defensive "not just" and the only
  narration contraction ("doesn't") sat in the Companion-module paragraph, where prose discipline tends to slip
  because it reads as a record, not chapter text. Worth a one-line note in the drafter/auditor checklist: the
  back-matter sources + companion paragraphs are graded prose under the locked voice too — sweep them for
  contractions and defensive "is not just"/"is not merely" the same as the body.
- **A figure under a fresh `## How it works` heading is the recurring drop-cold spot.** When the diagram is the
  first thing under the section heading, the figure-intro sentence is easy to omit. Suggest the figure-designer
  hand-off (or the drafter) always leave the one-sentence intro in place when a figure opens a section, so the
  scorer is not adding it at the lift stage.
- **Self-score vs independent score is load-bearing here.** This chapter clears the legacy ≥35 inclusion bar at
  42/50 with floors green, but the active auto-approval bar is ≥44/50 on an *independent* score — a 2-point gap
  the self-score cannot close on its own. The honest route is the independent re-score, not lifting the
  self-score by 2 points to "pass" it.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
