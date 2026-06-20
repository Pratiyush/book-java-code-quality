# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Merge cluster 51/104 (51 = perf-testing-as-quality; this =
> JMH microbenchmark discipline). Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 104 — `01-index/CANDIDATE_POOL.md` · **Title:** Benchmarking discipline — JMH microbenchmarks done honestly
- **Part:** XIII · **Tier:** B · cluster 51/104 · relates 101/102/105
- **Primary authorities:** JMH (`github.com/openjdk/jmh`); Oracle "Avoiding Benchmarking Pitfalls on the JVM"; Costa et al. "Bad Practices in JMH Benchmarks" (TSE); JMH samples.

## 1. Core definition & purpose
Measuring small Java code accurately is deceptively hard: the JVM's JIT, dead-code elimination, and constant folding will quietly invalidate a naive `System.nanoTime()` benchmark, producing confident lies. **JMH (Java Microbenchmark Harness)** is the official OpenJDK tool that controls for these effects. This chapter covers benchmarking *discipline* — using JMH correctly to get trustworthy numbers — and the loud caveat that even correct microbenchmarks can mislead about real-system performance (defer to profiling, key 102, and load testing, key 51, for the truth).

## 2. Mechanism (the spine)
- **Why naive benchmarks lie:** the JIT optimizes away unused results (**dead-code elimination**), folds **constants**, and inlines/optimizes loops; without warmup the JIT hasn't compiled the code yet; GC/other JVM effects add noise. A hand-rolled timer measures the optimizer, not your code.
- **JMH's controls:** `@Benchmark` methods; **warmup** iterations (let the JIT compile); **forking** (fresh JVM per fork, avoid profile pollution); `@State` for shared state; **`Blackhole`** (consume results so they aren't dead-code-eliminated) / return the result; `@OperationsPerInvocation`; modes (throughput/avg-time) + time units; report with error/CI.
- **The pitfalls (cite the literature):** returning nothing (dead code); using constants (constant folding); `@Setup(Level.Invocation)` (JMH docs warn it's "almost always wrong" — JIT may merge setup+benchmark); too few warmup/forks; benchmarking trivial code the JIT erases. Costa et al. catalog bad practices empirically.
- **How to run it:** separate benchmark module/source set; run offline (not in the normal test run); compare variants with overlapping confidence intervals in mind (don't over-read a 2% diff).
- **Cluster 51/104:** key 51 = macro/load/perf-testing as a quality gate; this = micro-level JMH discipline. Cross-reference; don't duplicate.

## 3. Evidence FOR
- **JMH is the official, correct tool** — it controls for the JVM effects that make naive microbenchmarks worthless.
- **Trustworthy comparisons** — with warmup/forks/Blackhole + reported error, JMH gives defensible micro-level numbers for a specific change.
- **Well-documented pitfalls** — the failure modes are known and avoidable (samples + literature).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central)
- **Microbenchmarks lie about systems** — a faster method in JMH may not move (or may even hurt) real end-to-end performance (cache, contention, I/O, real data dominate). Production profiling (key 102) + load testing (key 51) are the truth; JMH answers a narrow question.
- **Easy to misuse** — dead-code/constant-folding/setup-level mistakes produce confident-but-wrong numbers; the discipline is non-trivial (literature shows widespread bad practice).
- **Over-reading small diffs** — noise + overlapping confidence intervals mean a 1–3% "win" is often nothing; report error, don't cherry-pick.
- **Time cost** — proper JMH (warmup + forks) is slow; reserve for changes where micro-performance genuinely matters (key 101 — most code doesn't).
- **Hardware/JVM-specific** — results don't always transfer across machines/JDKs; state the environment.

## 5. Current status
JMH is the standard OpenJDK microbenchmark harness; its pitfalls are well-documented; used with profiling (key 102) + load testing (key 51). *(Versions/annotations verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module:** a JMH benchmark done wrong (no Blackhole → dead-code-eliminated, misleading) vs done right (Blackhole + warmup + forks), showing how the naive version lies. **Toolchain-gated** (separate benchmark run). Built green; tag-region snippet.
- **Figure:** Fig 104.1 — the JMH controls (warmup/fork/Blackhole/state) mapped to the JVM effect each defeats (JIT/dead-code/constant-folding/profile-pollution) + "micro ≠ macro" caveat. Trace to JMH docs + Oracle pitfalls article.

## 7. Gap-filling (verification queue)
- ⚠ JMH annotations/modes + Blackhole API + `@Setup(Level.Invocation)` warning wording — verify at pin against JMH docs/samples.
- ⚠ Costa et al. TSE "bad practices" + Oracle pitfalls specifics — confirm.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | JMH (OpenJDK) | github.com/openjdk/jmh | ☑; ⚠ API |
| 2 | Oracle — Avoiding Benchmarking Pitfalls on the JVM | oracle.com/technical-resources | ☑ pitfalls |
| 3 | Costa et al. — Bad Practices in JMH (TSE) | research paper | ⚠ confirm |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | JMH 2026 | dead-code/constant-folding/@Setup(Invocation) pitfalls; warmup/fork/Blackhole; "microbenchmarks lie" |

---
## Learnings & pipeline suggestions
- Cluster 51/104: micro (JMH) here, macro/load (key 51). **Loudest caveat:** micro ≠ macro; profile (key 102) for truth. **Cross-ref:** 51, 101, 102, 105, 03 (don't trade readability on a microbench whim).
