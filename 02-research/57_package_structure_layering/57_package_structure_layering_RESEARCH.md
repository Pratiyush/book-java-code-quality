# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Concept owner; enforcement = key 55, metrics = key 54.
> Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 57 — `01-index/CANDIDATE_POOL.md` · **Title:** Package/module structure & layering — keeping the dependency graph honest
- **Part:** VI · **Tier:** B
- **Primary authorities:** R. C. Martin (package principles — key 04/54); "package by feature vs by layer" practitioner literature; JPMS (key 55); ArchUnit (enforcement).

## 1. Core definition & purpose
How you slice a codebase into packages/modules decides which dependencies are *possible*. Good structure makes the right thing easy and the wrong dependency awkward; bad structure (everything in `com.acme.service`) lets coupling metastasize. This chapter covers the two dominant strategies, the principles for a healthy graph, and how to keep it honest over time — the structural substrate keys 54 (metrics) and 55 (enforcement) operate on.

## 2. Mechanism (the spine)
- **Package-by-layer** (`controller`, `service`, `repository`): familiar, maps to the layered architecture; risk — a feature's code is scattered across packages, and layer packages become low-cohesion buckets.
- **Package-by-feature** (`orders`, `billing`, each self-contained): high cohesion per feature, change stays local; risk — cross-feature sharing needs deliberate API packages. (Present both as trade-offs; crown neither.)
- **Healthy-graph principles:** acyclic dependencies (no cycles — key 54); stable-dependencies direction; a clear public API per module (package-private by default — Java's default access is an under-used encapsulation tool); reuse/release equivalence (Martin's package-cohesion principles, key 04).
- **Module strength ladder:** package convention → package-private encapsulation → ArchUnit-enforced slices (key 55) → JPMS `module-info` (compiler-enforced, key 55) → separate build modules (Maven/Gradle, key 62).
- **Keeping it honest:** ArchUnit slice rules + JDepend in CI (a fitness function, key 56) so structure can't silently erode.

## 3. Evidence FOR
- Structure is the cheapest lever on coupling — a good package graph prevents whole classes of bad dependency before any rule fires.
- Java's package-private default + JPMS give real, compiler-level encapsulation when used.
- Enforceable and measurable (keys 54/55), so it isn't just opinion.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **No universally "correct" structure** — by-feature vs by-layer is context-dependent (team size, domain, app vs library); dogma either way hurts.
- **Restructuring is invasive** — moving packages churns imports and history; do it incrementally (key 91) with tests as a net.
- **Structure ≠ design** — neat packages can still contain bad code; this is one lever, not the whole game.
- **Over-modularization** (premature JPMS / too many build modules) adds ceremony with little payoff for a small app.

## 5. Current status
Both strategies in wide use; by-feature increasingly favored for microservices/modular monoliths; JPMS selectively adopted. Concepts stable.

## 6. Worked example / figure spec
- **Illustrative:** the same mini-app organized by-layer vs by-feature, with the dependency graph (JDepend) for each. Reuses key 54/55 module.
- **Figure:** Fig 57.1 — by-layer vs by-feature package trees + their dependency graphs side by side (trade-off, no winner).

## 7. Gap-filling (verification queue)
- ⚠ Martin's package principles (REP/CCP/CRP, ADP/SDP/SAP) names — confirm verbatim (shared key 04/54).
- ⚠ Java default-access / JPMS exports semantics — confirm against JLS/JEP 261.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Martin — package principles | print (key 04/54) | ⚠ names |
| 2 | JPMS / ArchUnit | openjdk.org/jeps/261 ; archunit.org | ☑ enforcement |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis) | by-layer vs by-feature; acyclic + stable-direction; enforce via ArchUnit/JPMS |

---
## Learnings & pipeline suggestions
- Two-strategies neutral shape (by-layer vs by-feature). **Cross-ref:** 54 (metrics), 55 (enforce), 56 (fitness fn), 62 (build modules), 91 (restructure safely).
