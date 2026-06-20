# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (JFR vs async-profiler vs commercial — niches). JEPs/
> versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 102 — `01-index/CANDIDATE_POOL.md` · **Title:** Profiling — JFR, async-profiler; finding the real hotspots
- **Part:** XIII · **Tier:** B · **Cmp:** ⚠ · relates 101/103
- **Primary authorities:** JDK Flight Recorder (JFR) + JEPs (incl. JEP 509 JFR CPU-Time Profiling, experimental); async-profiler (`github.com/async-profiler/async-profiler`); Mission Control; commercial (YourKit/JProfiler) as alternatives.

## 1. Core definition & purpose
Profiling answers "where does the time/allocation actually go?" — the prerequisite for any optimization (key 101). Developers' guesses about hotspots are routinely wrong; a profiler shows the truth. This chapter covers the main Java profilers (especially the low-overhead, production-capable ones), how to read their output (flame graphs), and the honest limits (overhead, sampling bias, measuring the wrong thing) — crowning none.

## 2. Mechanism (the spine)
- **JDK Flight Recorder (JFR):** built into the JVM, **~1% overhead**, designed for **production** profiling; records events (CPU, allocation, locks, GC, I/O) to a recording you analyze in **JDK Mission Control** or IDE plugins. The default first reach because it's always available + low-overhead. JEP 509 adds CPU-time profiling (experimental) for more accurate CPU attribution incl. native code.
- **async-profiler:** low-overhead sampling profiler producing **flame graphs**; strong for CPU (uses perf_events), allocation, lock profiling; avoids the safepoint-bias of older samplers; outputs JFR-compatible data.
- **Flame graphs:** the standard visualization — width = time/samples in a call path; read top-down to find the widest (hottest) frames.
- **Commercial (alternatives, neutral):** YourKit, JProfiler — richer UIs/features, paid.
- **What to profile:** CPU (where cycles go), allocation (what creates garbage, key 103), wall-clock/latency (where time waits), locks (contention, key 20). Profile under **realistic load**, ideally in/near production (JFR's strength).
- **Sampling vs instrumentation:** sampling (JFR/async) = low overhead, statistical; instrumentation = exact but high overhead + can distort. Prefer sampling for hotspots.

## 3. Evidence FOR
- **JFR is production-safe** (~1% overhead, built-in) — you can profile real workloads, where the truth lives (vs synthetic microbenchmarks, key 104).
- **async-profiler flame graphs** make hotspots visually obvious + avoid safepoint bias.
- **OSS + built-in** — no spend needed to start (JFR + async-profiler).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Profiling shows *where*, not *why* or *the fix*** — interpretation + a hypothesis + a benchmark (key 104) still required.
- **Sampling is statistical** — short/rare events can be missed; misreading a flame graph (e.g. inlined frames, wall vs CPU) misleads.
- **Profile the wrong thing** — profiling a microbenchmark or an unrealistic load gives hotspots that don't matter in production; profile realistic workloads.
- **Overhead/observer effect** — instrumentation profilers distort; even low overhead can shift timing for latency-sensitive code.
- **⚠ tool niches** — JFR (production/built-in) vs async-profiler (flame graphs/CPU) vs commercial (UX/features); choose by need, crown none.

## 5. Current status
JFR + async-profiler are the mainstream low-overhead Java profilers; JEP 509 (JFR CPU-time profiling) is experimental; Mission Control + IDE plugins for analysis; commercial options current. *(JEPs/versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** capture a JFR recording (or async-profiler flame graph) of a deliberately-hot method, read the flame graph to find it, then confirm a fix with JMH (key 104). **Toolchain-gated** (needs a running JVM). Built green; tag-region snippet of the run command.
- **Figure:** Fig 102.1 — a flame graph with the hot path highlighted + how to read it (width=time, top-down). Trace to async-profiler/JFR docs.

## 7. Gap-filling (verification queue)
- ⚠ JFR overhead figure (~1%), JEP 509 status, async-profiler modes/versions — verify at pin.
- ⚠ Mission Control / IDE plugin specifics — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | JFR + JEP 509 (CPU-time profiling) | openjdk.org/jeps/509 ; docs.oracle.com | ☑ exists; ⚠ overhead/status |
| 2 | async-profiler | github.com/async-profiler/async-profiler | ☑ flame graphs; ⚠ version |
| 3 | JDK Mission Control; YourKit/JProfiler | respective | ☑ roles |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Java profiling 2026 | JFR ~1% built-in production; async-profiler flame graphs; JEP 509 CPU-time |

---
## Learnings & pipeline suggestions
- Neutral tool-niche treatment. Profiling finds *where*; JMH (key 104) measures the *fix*; allocation detail → key 103. **Cross-ref:** 101, 103, 104, 20 (locks), 22.
