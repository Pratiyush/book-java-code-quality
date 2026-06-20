# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). GC/JVM specifics `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 103 — `01-index/CANDIDATE_POOL.md` · **Title:** Memory & allocation hygiene — GC awareness, leak prevention, escape analysis
- **Part:** XIII · **Tier:** B · relates 16/102/101
- **Primary authorities:** JVM/GC docs (G1/ZGC/Parallel); the generational hypothesis; JFR allocation profiling (key 102); resource management (key 16).

## 1. Core definition & purpose
On the JVM you don't manage memory manually, but allocation behavior still drives performance (GC pressure, latency pauses) and correctness (leaks → OOM). **Memory hygiene** — being deliberate about what you allocate and what you retain — is a quality practice that prevents a class of production incidents. This chapter covers GC awareness, leak prevention, and allocation reduction *where it matters* (guided by profiling, key 102) — without premature micro-optimization (key 101).

## 2. Mechanism (the spine)
- **The generational hypothesis + GC:** most objects die young; generational collectors (G1 default, ZGC for low-latency, Parallel for throughput) exploit this. Allocation is cheap; *garbage* (short-lived churn) creates GC pressure → pauses/latency. Know your collector's trade-off (throughput vs pause).
- **Allocation hygiene (where profiling, key 102, says it matters):** avoid needless allocation in hot paths (e.g. per-request object churn, autoboxing in loops, string concatenation in loops → `StringBuilder`, unnecessary stream materialization); reuse where safe; size collections to avoid resize churn. **Measure first** — don't micro-optimize cold code (key 101).
- **Escape analysis / JIT:** the JIT can stack-allocate or scalar-replace objects that don't escape — so "allocation" isn't always heap allocation; another reason to *measure* (JFR allocation profiling) rather than assume.
- **Leak prevention (correctness):** the common Java "leaks" are unbounded caches/collections, unremoved listeners, `ThreadLocal` not cleared (esp. with pooled/virtual threads, key 22), and unclosed resources (key 16 — try-with-resources). Heap dumps + JFR/leak tools diagnose; weak/soft references and bounded caches (key 23) prevent.
- **Records & value-based design (keys 10/13):** immutable small objects + (future) value types affect allocation; note value classes are AHEAD-OF-PIN (key 10/13).

## 3. Evidence FOR
- **Allocation reduction in real hotspots cuts GC pressure + latency** — a measured, high-leverage fix for throughput/latency-sensitive Java.
- **Leak prevention avoids a real incident class** (OOM, gradual latency creep) — bounded caches + cleared ThreadLocals + try-with-resources (key 16) are concrete, durable practices.
- **Tooling exists** — JFR allocation profiling (key 102) + heap dumps make allocation/leaks visible.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Premature allocation micro-optimization is the trap** (key 101) — obsessing over allocation in cold paths costs readability (key 03) for no gain; the JIT (escape analysis) may already handle it. Profile first.
- **Object pooling is usually counterproductive** on modern JVMs (cheap allocation + good GC) — a classic anti-optimization that adds complexity + bugs; reserve for genuinely expensive objects.
- **GC tuning is deep + workload-specific** — blind flag-tuning often hurts; defaults + the right collector choice beat cargo-culted flags. Measure.
- **Leaks are subtle** — slow leaks pass tests and surface in production; needs monitoring (key 108) + heap analysis, not just code review.
- **Memory hygiene ≠ correctness/quality overall** — one axis; readability/correctness still primary.

## 5. Current status
G1 default; ZGC for low-latency; generational hypothesis stable; escape analysis in HotSpot; JFR allocation profiling current; value types (Valhalla) AHEAD-OF-PIN (don't assert). *(GC/JVM specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** a hot method with per-call allocation churn (autoboxing/string concat) profiled (JFR allocation, key 102), then reduced; show GC pressure before/after. **Toolchain-gated.** Built green; tag-region snippet.
- **Figure:** Fig 103.1 — allocation → GC pressure → latency, with the hygiene levers (reduce churn / bound caches / clear ThreadLocals / close resources) and the "measure first / pooling rarely helps" caveats. Trace to GC docs + JFR.

## 7. Gap-filling (verification queue)
- ⚠ Default GC (G1) + ZGC/Parallel trade-offs at the pinned JDK — verify at pin.
- ⚠ Escape-analysis/scalar-replacement specifics — confirm against JDK docs.
- ⚠ Value types (Valhalla) — AHEAD-OF-PIN, never assert (keys 10/13/14 flagged).

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | JVM GC docs (G1/ZGC) | docs.oracle.com | ⚠ specifics |
| 2 | JFR allocation profiling | (key 102) | ☑ cross-ref |
| 3 | try-with-resources / caches | (keys 16/23) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | allocation/GC hygiene 2026 | generational hypothesis; memory hygiene + monitoring; measure-first |

---
## Learnings & pipeline suggestions
- **Honest centerpieces:** measure-first (key 101); object pooling usually counterproductive on modern JVMs. Valhalla AHEAD-OF-PIN. **Cross-ref:** 16 (resources), 23 (caches), 22 (ThreadLocal/virtual threads), 102 (profile), 101, 10/13 (records/value), 108 (monitor leaks).
