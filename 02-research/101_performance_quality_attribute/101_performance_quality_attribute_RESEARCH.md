# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Umbrella for Part XIII. Versions/JEPs `⚠ verify at pin`.
> Neutral; honest-limitations met.

---

## Topic
- **Key:** 101 — `01-index/CANDIDATE_POOL.md` · **Title:** Performance as a quality attribute — when it matters, when it doesn't
- **Part:** XIII — Performance & runtime quality · **Tier:** B · umbrella over 102–105 · relates 01/51
- **Primary authorities:** ISO 25010 *Performance Efficiency* (key 01); JFR/JMH/async-profiler (keys 102/104); Knuth's premature-optimization caution.

## 1. Core definition & purpose
Performance is an ISO 25010 quality characteristic (*Performance Efficiency* — time behavior, resource utilization, capacity; key 01), but it's the one most often optimized at the wrong time, in the wrong place, on guesses. This chapter (Part XIII umbrella) frames performance as a quality attribute to be **measured, targeted, and gated like any other** — *when it matters* — while resisting premature optimization that trades readability (key 03) for imaginary gains. The stance: define performance requirements, measure (don't guess), optimize the proven hotspot, and guard against regression (key 105).

## 2. Mechanism (the spine)
- **Performance is a requirement, not a vibe:** define targets (p99 latency, throughput, memory/RSS, startup) tied to actual needs; without targets, "make it fast" is unmeasurable (key 04 parallel).
- **Measure before optimizing:** profile (key 102) to find the *real* hotspot; benchmark (JMH, key 104) to measure a change; never optimize on intuition (the cause of most wasted perf work).
- **The performance toolchain (routes to deep chapters):** profiling = JFR/async-profiler (key 102); allocation/GC hygiene (key 103); microbenchmarking = JMH (key 104); regression gates in CI (key 105). Macro/load testing (key 51) is the system-level sibling.
- **Premature optimization (Knuth):** optimizing before measuring, or micro-optimizing cold paths, adds complexity + bugs for no gain — a quality *anti*-pattern (ties to keys 03/19). "When it doesn't matter" is most code.
- **Modern Java performance context (key 13/22):** virtual threads (key 22) change the concurrency-performance picture; records/escape analysis affect allocation (key 103) — anchor 21, note 25.

## 3. Evidence FOR
- **Performance is a real, standardized quality axis** (ISO 25010) with mature Java measurement tooling (JFR/JMH/async-profiler).
- **Measure-then-optimize is well-established** — profiling consistently shows hotspots aren't where developers guess.
- **Gate-able** — perf regression can be a fitness function (keys 56/105), like other quality properties.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Premature/misplaced optimization is the dominant failure** — most micro-optimizations target cold code and cost readability (key 03) for nothing; "when NOT to optimize" is the chapter's spine.
- **Performance trades against other attributes** — speed vs readability/maintainability/simplicity; optimize only where requirements justify the cost.
- **Microbenchmarks lie** (key 104) — a fast microbenchmark can mislead about real-system behavior; macro/load testing (key 51) + production profiling (key 102) are the truth.
- **Performance ≠ the whole of quality** — a fast but unmaintainable/incorrect system isn't high-quality (key 01); this is one axis among several.
- **Targets must be real** — perf gates on arbitrary numbers (key 04 vanity) waste effort.

## 5. Current status
ISO 25010 performance efficiency stable; JFR/JMH/async-profiler current; virtual threads (key 22) reshape concurrency-perf; perf-regression gating growing. *(JEPs/versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept/umbrella chapter** — figure-led; an illustrative "guessed hotspot vs profiled hotspot" (key 102) showing measure-first.
- **Figure:** Fig 101.1 — the performance discipline loop: set target → profile (find hotspot) → benchmark the fix (JMH) → gate against regression; "most code: don't optimize." Trace to ISO 25010 + Knuth.

## 7. Gap-filling (verification queue)
- ⚠ Knuth "premature optimization" quote — confirm verbatim/attribution.
- ⚠ ISO 25010 Performance Efficiency sub-characteristics (key 01) — already flagged at key 01.
- ⚠ Virtual-threads/escape-analysis performance claims — defer specifics to keys 22/103; keep general here.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | ISO 25010 Performance Efficiency | (key 01) | ☑ characteristic |
| 2 | JFR / JMH / async-profiler | (keys 102/104) | ☑ cross-ref |
| 3 | Knuth — premature optimization | citation | ⚠ verbatim |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Java performance 2026 | JFR gold-standard ~1% overhead; JMH for micro; measure-then-optimize |

---
## Learnings & pipeline suggestions
- Umbrella; route specifics to 102–105 + 51. **Honest centerpiece:** when NOT to optimize (premature-optimization anti-pattern). **Cross-ref:** 01, 03, 19, 22, 51, 102, 103, 104, 105, 56.
