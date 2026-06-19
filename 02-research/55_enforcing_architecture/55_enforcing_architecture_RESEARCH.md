# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). ArchUnit tool deep-dive lives in key 33; this chapter owns
> *architecture enforcement as a discipline* (cluster 33/55/56). Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 55 — `01-index/CANDIDATE_POOL.md` · **Title:** Enforcing architecture — ArchUnit rules, module boundaries, JPMS
- **Part:** VI · **Tier:** B · merge cluster 33/55/56
- **Primary authorities:** ArchUnit (`archunit.org`, key 33); JPMS (JLS/JEP 261 — Java 9 module system); jQAssistant/Deptective/Spring Modulith (cited as alternatives, neutral).

## 1. Core definition & purpose
Architecture decays because nothing stops a developer from importing across a boundary you only documented in a wiki. **Enforcement** turns architecture from intention into an executable rule that fails the build on violation — "executable architecture." This chapter covers the mechanisms (ArchUnit rules as tests, JPMS module boundaries, and the alternatives) and the honest limits of trying to encode design as a gate.

## 2. Mechanism (the spine)
- **ArchUnit** (key 33): architecture rules expressed as JUnit tests — layer access (`layeredArchitecture()`), package dependency (`noClasses().that()...should().dependOnClassesThat()`), cycle-freedom (`slices().should().beFreeOfCycles()`), naming. Runs on the JUnit Platform (key 42), so it gates in CI like any test.
- **JPMS** (Java Platform Module System, Java 9+, JEP 261): `module-info.java` with `exports`/`requires` enforces boundaries **at compile and runtime** — strong encapsulation the compiler checks. Stronger than test-based rules but higher adoption cost; many apps stay on the classpath.
- **Alternatives (neutral):** jQAssistant (graph/Cypher queries over the codebase), Deptective (compiler plugin), Spring Modulith (module verification for Spring apps), Maven/Gradle module structure + enforcer. Each cited to its own docs; none crowned.
- **Layering enforcement** is the most common use: controller→service→repository one-way; cross-layer or upward imports fail.

## 3. Evidence FOR
- **Makes architecture executable** — a rule in CI catches drift the moment it's introduced, not at the next big-bang review (shift-left, key 06).
- **ArchUnit is low-friction** — plain tests, no new runtime; adoptable incrementally (start with cycle-freedom).
- **JPMS gives compiler-enforced** strong encapsulation where the team commits to it.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Rules encode *known* constraints only** — they can't catch a bad design that obeys the rules; enforcement ≠ good architecture.
- **Maintenance cost / false friction** — over-strict rules block legitimate change and get `@ArchIgnore`'d into irrelevance (mirrors suppression discipline, key 39).
- **JPMS adoption is heavy** — modularizing a legacy app + non-modular dependencies is a real migration; many teams rationally stay on the classpath (when-NOT-to-use).
- **ArchUnit runs post-compile** (it analyzes bytecode/sources as tests) — slower feedback than the compiler; large rule sets add CI time (key 79).

## 5. Current status
ArchUnit is the mainstream Java choice; JPMS is available since Java 9 but selectively adopted; Spring Modulith/jQAssistant active in their niches. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module** (shared with key 54): a layered mini-app + an ArchUnit test enforcing layer direction and cycle-freedom; show a violation failing the build. Built green; tag-region snippet.
- **Figure:** Fig 55.1 — the enforcement spectrum: convention → ArchUnit test → JPMS compiler boundary (increasing strength + cost). Trace to ArchUnit/JPMS docs.

## 7. Gap-filling (verification queue)
- ⚠ ArchUnit rule API names/versions (shared with key 33) — verify at pin.
- ⚠ JPMS = JEP 261, Java 9 — confirm against JDK docs.
- ⚠ Spring Modulith / jQAssistant / Deptective current status — confirm if cited beyond a mention.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | ArchUnit | archunit.org (key 33) | ☑ rules; ⚠ version |
| 2 | JPMS (JEP 261 + JLS) | openjdk.org/jeps/261 | ⚠ confirm |
| 3 | Spring Modulith / jQAssistant | respective docs | ☑ named alts |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | evolutionary architecture / ArchUnit | ArchUnit = go-to Java architecture-testing lib |

---
## Learnings & pipeline suggestions
- Keep ArchUnit *tool tutorial* in key 33; *enforcement discipline* here; *fitness functions* in key 56 — three angles, one cluster, no triplication.
- **Cross-ref:** 33, 54, 56, 57, 39 (suppression), 79 (CI cost), 06 (shift-left).
