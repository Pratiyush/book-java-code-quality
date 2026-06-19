# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Synthesizes the metric definitions banked in key 04; this
> chapter owns the *design concept*. Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 54 — `01-index/CANDIDATE_POOL.md` · **Title:** Coupling, cohesion & dependency direction — the structural metrics
- **Part:** VI — Architecture & design governance · **Tier:** B
- **Primary authorities:** Chidamber & Kemerer (CBO/RFC/LCOM — key 04); R. C. Martin package metrics (Ca/Ce/Instability/Abstractness/Distance — key 04); JDepend / ckjm (Java tools). Constantine & Yourdon (origin of coupling/cohesion).

## 1. Core definition & purpose
Coupling (how much a unit depends on others) and cohesion (how focused a unit is) are the two oldest, most durable structural quality concepts — they are what ISO *modularity* and *modifiability* (key 01) reduce to in practice. **Low coupling + high cohesion = cheap, safe change.** This chapter turns the concept into something a Java team can see and direct: the metrics (key 04), the *direction* of dependencies (stable things shouldn't depend on volatile things), and how to keep the dependency graph honest.

## 2. Mechanism (the spine)
- **Cohesion** (intra-unit): a class/package whose members serve one purpose. Java smell of low cohesion: a `Util` grab-bag; metric: **LCOM** (key 04, with its caveats).
- **Coupling** (inter-unit): efferent (`Ce`, what I depend on) vs afferent (`Ca`, what depends on me); class-level **CBO/RFC** (key 04). Java smells: feature envy, shotgun surgery (key 19).
- **Dependency direction & stability** (Martin): **Instability I = Ce/(Ca+Ce)**; the **Stable Dependencies Principle** — depend in the direction of stability; the **main sequence** (Abstractness vs Instability) with the *zone of pain* (stable+concrete) and *zone of uselessness* (abstract+unused). **DIP** (key 53) is how you invert a bad-direction dependency.
- **Cycles** are the cardinal sin — a dependency cycle couples a whole cluster into one unchangeable blob; detect with ArchUnit `slices().should().beFreeOfCycles()` (key 55) or JDepend.
- **Tools:** ckjm (CK), JDepend (package Ca/Ce/I/A/D), ArchUnit (enforce direction/cycles), Sonar (coupling views). *(versions `⚠ verify at pin`.)*

## 3. Evidence FOR
- Decades of empirical fault-prediction link high coupling / low cohesion to defects (key 04 studies).
- Directly enforceable: cycles and layer-direction are gate-able with ArchUnit (key 55), unlike fuzzy principles.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Metrics are proxies** (key 04) — low CBO with terrible names is still unreadable; don't optimize the number.
- **Some coupling is necessary** — zero coupling = nothing talks; the goal is *appropriate, directed* coupling, not minimum.
- **LCOM is contested** (multiple definitions; key 04) — use cohesion qualitatively more than as a hard gate.
- **Package metrics need a sensible package design first** — they measure the structure you have, not the one you should have.

## 5. Current status
Concepts stable; tools (ckjm, JDepend, ArchUnit, Sonar) current. The enforceable subset (cycles, layering) is mainstream via ArchUnit.

## 6. Worked example / figure spec
- **Companion/illustrative:** JDepend + ArchUnit on a small multi-package module showing a cycle, then the refactor (introduce an interface to invert direction). Build candidate (ties to key 55 module).
- **Figure:** Fig 54.1 — Martin's main sequence plane (A vs I) with zones of pain/uselessness + sample packages. Trace to Martin (key 04).

## 7. Gap-filling (verification queue)
- ⚠ Martin's I/A/D formulas + SDP/SAP statements — confirm verbatim (shared with key 04).
- ⚠ Coupling/cohesion origin (Constantine/Yourdon, *Structured Design*) — confirm attribution.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | C&K + Martin package metrics | (key 04 sources) | ☑ concept; ⚠ formulas |
| 2 | JDepend / ckjm | clarkware JDepend; github.com/dspinellis/ckjm | ☑ tools |
| 3 | Constantine & Yourdon — *Structured Design* | print | ⚠ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis of key 04) | CBO/RFC/LCOM + Ca/Ce/I/A/D + cycles |

---
## Learnings & pipeline suggestions
- This chapter owns the *concept*; key 04 owns the *metric definitions*; key 55 owns *enforcement*. Keep the split to avoid triplicating.
- **Cross-ref:** 01 (modularity), 04 (metrics), 53 (DIP), 55 (ArchUnit enforce), 57 (layering), 19 (smells).
