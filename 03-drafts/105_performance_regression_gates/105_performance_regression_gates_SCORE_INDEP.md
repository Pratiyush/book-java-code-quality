# SCORECARD (INDEPENDENT) — Java code quality Book

> Independent (different-model) re-score for auto-approval. Rubric: `00-strategy/SCORING.md` (five
> clusters 1–10 + floors A/B/C). Ship/auto-approval bar: **≥44/50 (88%), no cluster below 6, floors
> A/B/C-source PASS**. Harsh-skeptic pass with a bounded in-bounds lift loop applied to the printed draft.

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard (independent)
- **Dossier key:** 105 (single key, frozen — `01-index/CANDIDATE_POOL.md`)
- **Slug:** `105_performance_regression_gates`
- **Title:** The Thousand Cuts (Performance-regression gates)
- **Part / arc position:** Part XIII — Performance & Observability · **Chapter 44** (middle; Ch 45 closes the Part)
- **Artifact scored:** `03-drafts/105_performance_regression_gates/105_performance_regression_gates_v1.md` (printed Ch 44)
- **Verified against** the pinned authority set (`00-strategy/SOURCE-PIN.md`) — pinned **2026-06-20** (re-check date: 2026-06-28)
- **Gate inputs read whole:** `_EXAMPLE.md` (2026-06-26, build green), `_CODEREVIEW.md` (2026-06-27, PASS-WITH-FIXES), `fig105_1.sources.md`; `SCORING.md`, `NEUTRALITY.md`, `VOICE-GUIDE`, `SOURCE-PIN.md`, `FINAL_INDEX.md`.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 2 (final) — see lift-pass log

---

## The five clusters (final, after Pass 2)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Mechanism ordered and reconstructable (measure → compare-relative-to-baseline-with-statistics → three-way verdict → ratchet); Fig 44.1 is load-bearing and traces the flow; why-before-how holds; the "why three values not two" is explicit. Lifted 8→9 in Pass 1 by glossing `baseline` and `tolerance band` plain-language-first at first use (they were leaned on bare in the hook/Overview before). |
| 2 | **ACCURACY** | **9** | Fully traced. Sole pinned version literal JMH 1.37 = SOURCE-PIN §3. All perf numbers (`200ms`, `4%`, `three milliseconds`, `tripled`) are illustrative-anti-pattern or narrative-hook hypotheticals — none asserted as a timeless measurement (independently re-confirmed by `_CODEREVIEW` hostile numeric scan). Snippets are tag-regions in a green-built module with recorded paths. Tool names kept general per pin. Not 10: the central premise (noise magnitude on shared runners) is honestly general rather than a single pinned figure. |
| 3 | **UTILITY** | **9** | The page a platform/build engineer keeps open while designing a perf gate. Concrete decision frames (relative-not-absolute, flag-then-investigate, nightly-not-PR, ratchet deliberately); runnable companion gate to adapt. Lifted 8→9 in Pass 2 by making the noise-band actionable: it is read off the measurement's confidence-interval half-width (already-verified module behaviour, `_CODEREVIEW` §1), not guessed — the reader now knows *how* to set the band, not just that they need one. |
| 4 | **DEPTH** | **8** | Full mechanism + evidence-for + seven honest limitations + approach-based alternatives + when-to-use, all sourced; the Deep dive genuinely advances the argument (perf gate inverts the un-bypassable-gate instinct: block→investigate). Held at 8, not inflated: a meaningful slice of the depth is cross-reference depth (inherits gate discipline from Chs 20/26/33/34/38); the genuinely novel content (noise→soft posture, micro≠macro at the gate) is strong but compact. Padding DEPTH is out of bounds — held honestly. |
| 5 | **READABILITY** | **9** | Voice holds (no first person, no narration contractions, zero banned filler — confirmed by scan); strong concrete hook; varied rhythm. Lifted 7→9 in Pass 1: em-dash density 9.11 → 7.26/1000 (within the ~8 ceiling) by converting appositive cadences to periods/commas; Fig 44.1 alt-text rewritten to describe the diagram's content (four-stage flow + noise band + knobs) distinct from the caption; the tolerance-band re-gloss de-duplicated. |

**Cluster subtotal: 44 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan over printed prose = **0 hits** (independent run); `_CODEREVIEW` §6 = 0 hits over code+prose. Chapter is a mechanism description, not a tool comparison. The "Alternatives" section is approach-based ("Macro/load gate vs micro/JMH gate", "Flag-then-investigate vs hard-block") — no winner crowned. Gatling/JMeter/k6 named as "a tool of the … class," unranked. The "meaningful level" wording describes which *level* a gate is most useful at (what users feel), traceable to the dossier, not a product verdict. No unsourced cross-subject claim. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" with seven hard objections (noise→flaky gates; green-gate≠fast-enough; micro≠macro; cost+infra with explicit when-NOT "a low-traffic internal tool may not"; threshold-arbitrariness; gate≠design; baseline-management). "When to use what" closes with an explicit "Not when." No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms; only version literal JMH 1.37 is pinned; perf numbers illustrative/hypothetical, none timeless; figure sources trace every atom. **Compile:** `_EXAMPLE.md` 2026-06-26 — `mvn -B -Pquality verify` → **BUILD SUCCESS**, 10 tests, 0 Checkstyle, 0 SpotBugs (JDK 21.0.11), reproduced clean. **Code-review:** `_CODEREVIEW.md` 2026-06-27 — all six dimensions PASS, **PASS-WITH-FIXES** (NIT/MINOR polish only, none blocking the floor). Live JMH/load-runner = REPRO PENDING-RUNTIME (perf-env-gated *measurement run*, correctly recorded — not a build failure; gate logic fully built+tested). Code untouched by this lift loop → no rebuild required; 4 include directives intact (re-verified). |

**All three floors PASS.**

---

## Cross-reference validation (vs `01-index/FINAL_INDEX.md`)

All printed "Chapter NN" cross-refs resolve correctly; none miswired:

| Cited | FINAL_INDEX chapter | Used for | OK |
|---|---|---|---|
| Ch 20 | The testing landscape & test quality (key 49) | flaky-test failure mode | ✓ |
| Ch 26 | Enforcing architecture: ArchUnit & fitness functions (key 56) | fitness-function pattern | ✓ |
| Ch 33 | Designing the CI pipeline & quality gates (key 75/79) | CI gate/pipeline + placement | ✓ |
| Ch 34 | Coverage strategy, PR automation & CI platforms (key 80) | coverage ratchet analogue | ✓ |
| Ch 35 | Branch protection, trunk-based dev & pre-commit parity (key 81/82) | un-bypassable gates | ✓ |
| Ch 38 | Metrics, dashboards & rolling out quality (key 85/87/88) | dashboard/trend + vanity metric | ✓ |
| Ch 43 | Performance as quality: profiling, memory & benchmarking (key 101/104) | "previous chapter"; real targets | ✓ |

"Previous chapter" = Ch 43 (performance) and "next chapter" = Ch 45 (observability) are both correct. The dossier-comment note ("dossier says closes Part XIII but FINAL_INDEX = Ch 45 closes") lives only in the non-printed provenance header; the **printed** hand-off and subtitle correctly treat Ch 44 as the middle handing off to Ch 45. No printed-prose fix required.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — 44/50, no cluster below 6, all three floors PASS. Clears the ≥44/50 (88%) auto-approval bar on an independent score.

**One-line rationale:** Honest, well-built perf-regression-gate chapter; floors solid (green build, CODE-REVIEW PASS, zero banned phrasing, no timeless numbers); lifted from 40 → 44 by two in-bounds passes (READABILITY, then UTILITY) using only already-verified material — no padding, no invented facts.

---

## Flagged weakest cluster (final state)

- **Weakest cluster:** DEPTH — score **8** (tie-floor of the final five).
- **Why it is the weakest:** A meaningful share of its depth is cross-reference depth (inherited gate discipline) rather than net-new perf-gate substance; the novel content is strong but compact.
- **Single highest-leverage move to lift it (NOT taken — would risk padding/scope):** a worked end-to-end calibration example (choosing a band width from a real CI variance distribution) would add genuine depth, but only if it came with a *verified* measurement — which is exactly the REPRO PENDING-RUNTIME the chapter honestly defers. Adding it without a real run would invent a number (FLOOR C risk). Correctly left for the live-run pass, not forced now.

---

## Line-level fixes (applied in this lift loop)

| # | Cluster | Location | Issue (before) | Fix (applied) |
|---|---|---|---|---|
| 1 | READABILITY | Fig 44.1 image alt-text | alt-text duplicated the caption verbatim (accessibility miss) | rewrote alt-text to describe the diagram's content — the four-stage flow (Measure→Compare→Decide→Baseline) + threaded noise band + four knobs — distinct from the caption; every element traces to `fig105_1.sources.md` |
| 2 | CLARITY | Hook ¶ + Overview bullet | `baseline` / `tolerance band` leaned on before a plain-language gloss | added plain gloss at first use ("a *baseline* — the last accepted measurement of how fast the system runs"; tolerance band = "the margin around the baseline inside which a difference is treated as noise rather than a regression"); de-duplicated the later re-gloss in the noise CONCEPT |
| 3 | READABILITY | hook, How-it-works intro, noise §, Overview | em-dash density 9.11/1000 (over ~8 ceiling); appositive cadence over-used | converted appositive em-dashes to periods/commas at 5 sites → 7.26/1000 (within ceiling); no meaning lost |
| 4 | UTILITY | `regression-gate` snippet lead-in | "fails safe when the move could be noise" stated but not made actionable | added one applied sentence: the band is read off the measurement's confidence-interval half-width (already-verified module behaviour, `_CODEREVIEW` §1), not guessed — reader now knows *how* to set it; no new number introduced |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | **40** (C8·A9·U8·D8·R7) | PASS | PASS | PASS | LIFT-LOOP | initial independent score; weakest = READABILITY (em-dash 9.11/1000, alt-text dupes caption, terms ungloss­ed-first) |
| 1 | 2026-06-28 | **43** (C9·A9·U8·D8·R9) | PASS | PASS | PASS | LIFT-LOOP | READABILITY pass: alt-text de-duplicated + content-describing; baseline/tolerance-band glossed plain-first + re-gloss removed; em-dash 9.11→7.42/1000. Lifted R 7→9 and CLARITY 8→9. Now-weakest = UTILITY/DEPTH (tie 8) |
| 2 | 2026-06-28 | **44** (C9·A9·U9·D8·R9) | PASS | PASS | PASS | **SHIP** | UTILITY pass: made the noise-band actionable (read off the measurement's half-width — verified module behaviour, no new fact). Lifted U 8→9. Bar cleared at 44; DEPTH held honestly at 8 (no padding). 1 pass to spare. |

---

## Learnings & pipeline suggestions

- **Alt-text duplicating the caption is a recurring, cheap-to-catch accessibility miss** that no current scripted gate flags. The figure-designer produces a strong caption and reuses it as alt-text; the two should serve different jobs (caption = identifying label/thesis; alt-text = a content description of the diagram for a reader who cannot see it). Suggest the FIGURE-GUIDE require alt-text to *describe the rendered content* and a lint to flag alt==caption.
- **Plain-language-first glossing slips most on terms that appear first in the subtitle/hook**, where the writer is in scene-setting mode and the term lands before its definition. The structural fix is to gloss at the first *body* use even when the term debuted in the title. Worth a one-liner in VOICE-GUIDE's glossing section: "a term used in the subtitle/hook is glossed at its first body occurrence, not assumed."
- **The em-dash ceiling is a reliable, mechanical lift lever.** A draft sitting 9–9.5/1000 routinely drops under 8 with ~4–5 appositive→period conversions and zero meaning loss; the appositive cadence is the densest single AI tell. Worth surfacing the per-draft em-dash count to the drafter *before* the scoring gate so it is fixed at draft time.
- **The honest-deferral of a measured number protected the floor here.** The single move that would most lift DEPTH (a worked band-calibration on a real variance distribution) is exactly the REPRO PENDING-RUNTIME the chapter defers; forcing it now would have invented a benchmark figure and failed FLOOR C. The lift loop correctly stopped at 44 rather than reaching for a ninth DEPTH point through fabrication — a good worked example that "do not pad / do not invent" and "never lower the bar" can coexist: lift the cluster that has a real in-bounds move, hold the one that does not.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
