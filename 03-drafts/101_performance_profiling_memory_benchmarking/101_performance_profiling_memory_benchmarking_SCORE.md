# SCORECARD — Ch 43 "Performance as quality: profiling, memory & benchmarking" (key 101 + 102 + 103 + 51 + 104)

> Part XIII OPENER / umbrella. FIVE merged dossiers (perf-as-quality leads/umbrella + profiling §B + memory/
> allocation §C + benchmarking §D = 104⊕51 merged). Tier-A; the biggest fold yet. Main-loop; gates = manual.
> measure-dont-guess(the spine) + the-instruments-that-lie(profiling-defeats-guessed-hotspot + JMH-defeats-naive
> -benchmark) + performance-is-a-requirement-not-a-vibe + premature-optimization-is-the-dominant-failure/most
> -code-dont + micro≠macro + allocation-hygiene-where-it-matters/pooling-rarely-helps + escape-analysis-another
> -reason-to-measure + performance-trades-against-readability + performance≠whole-of-quality shapes. Draft:
> `101_performance_profiling_memory_benchmarking_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (3 "beat(s) cargo-culted" + 1 "teaching beat" reworded); JFR/async-profiler/commercial "crown none"; JMH-vs-hand-rolled + micro/profiler/load as complementary instruments not ranked; sampling-vs-instrumentation framed by need; no tool/collector crowned. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (most-code-shouldn't-be-optimized; never-optimize-on-a-guess; perf-trades-against-other-attributes; profiling-shows-where-not-the-fix; naive-benchmarks-lie; micro≠macro; pooling-backfires; GC-tuning-workload-specific; targets-must-be-real; Valhalla-AHEAD-OF-PIN) + the deep-dive most-important-decision-is-not-to-optimize center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | ISO 25010 + Knuth + JFR/JEP 509 + async-profiler + GC (G1/ZGC) + JMH samples (08/09/10/35/38) + Costa et al. all attributed; JFR overhead/JEP-509-status/GC-defaults/JMH-version+defaults carried ⚠ @pin; **Valhalla value types AHEAD-OF-PIN, never asserted**; §7 canon gaps (ISO/Knuth/JMH/testing-pyramid) flagged; profiling/JMH runs → REPRO PENDING-RUNTIME. |
| C — COMPILE | ⚠ PENDING (toolchain READY; profiling/JMH runs env-gated → REPRO PENDING-RUNTIME) | guessed-vs-profiled-hotspot + allocation-churn-reduced + JMH-wrong(DCE)-vs-right(Blackhole) module spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the wasted-week-on-the-guessed-hotspot + phantom-10x-from-DCE hook names both lies the chapter defeats; the four-movement (target/profile/memory/benchmark) structure organizes 5 dossiers cleanly; six CONCEPT callouts (requirement-not-vibe, flame-graph, generational-hypothesis, escape-analysis, why-naive-lies+JMH, micro≠macro) anchor it. |
| ACCURACY | 9 | profiling/GC/allocation/JMH atoms all attributed; the JIT-DCE/constant-folding/escape-analysis mechanisms precise; −1 for the verify-at-pin surface (JFR overhead, GC defaults, JMH version/defaults) — all flagged + Valhalla AHEAD-OF-PIN held; the-IDE-final-breaks-the-benchmark beat correct. |
| UTILITY | 9 | directly actionable across the whole loop: set targets (p99/throughput/RSS), JFR/async-profiler + read-flame-graphs, allocation hygiene (StringBuilder/sized-collections) where-profiled, leak prevention (bounded caches/cleared ThreadLocals/try-with-resources), JMH done right (warmup/fork/Blackhole/@State), don't-optimize-most-code; a complete performance discipline. |
| DEPTH | 9 | the instrument-that-lies unifying frame (intuition + naive-timer both actively deceptive, not merely incomplete) + the-same-discipline-as-coverage/metrics (necessary-not-sufficient, earn-the-signal) + the-most-important-decision-is-not-to-optimize is senior performance material, tying performance into the book's folklore-guard spine. |
| READABILITY | 8 | gripping two-lies hook, six callouts, the instrument-that-lies + don't-optimize synthesis opening Part XIII; 5449w — the longest chapter, justified by 5 dossiers + a Tier-A Part-opener, and the four-movement structure keeps it navigable; clean find-and-fix → keep-from-regressing hand-off. |

**Aggregate 44/50**, none < 6 (Part-opener high; ties the other Tier-A Part chapters). Floors A/B/C-source ✅;
FLOOR-C COMPILE = PENDING (toolchain READY; profiling/JMH runs env-gated). New shapes: measure-dont-guess +
the-instruments-that-lie + performance-is-a-requirement-not-a-vibe + premature-optimization-is-the-dominant
-failure + micro≠macro + allocation-hygiene-where-it-matters/pooling-rarely-helps + escape-analysis-another
-reason-to-measure + performance-trades-against-readability + performance≠whole-of-quality. **OPENS Part XIII
(Performance & Observability).** Performance as a measured quality attribute; the two instruments that lie
(intuition + naive benchmark) defeated by profiling + JMH; the same earn-the-signal discipline as coverage/
metrics. Hands off to Ch 44 (performance-regression gates, key 105 — keep it from regressing; the macro half).
The instrument-that-lies + most-important-decision-is-not-to-optimize are the distinctive notes.
