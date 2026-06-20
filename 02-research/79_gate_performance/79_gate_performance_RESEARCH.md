# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 79 — `01-index/CANDIDATE_POOL.md` · **Title:** Performance of the gate — build caching, incremental analysis, parallelism
- **Part:** IX · **Tier:** B · relates 75/49
- **Primary authorities:** Maven/Gradle build-cache + parallel docs (key 62); Gradle build cache / configuration cache; CI caching docs (key 77); analyzer incremental modes (Sonar/SpotBugs).

## 1. Core definition & purpose
Gate **speed is a quality concern**, not a luxury: a pipeline too slow to get fast feedback (key 75) gets bypassed, and slow local builds (key 82) make developers skip checks. This chapter covers making the gate fast enough that the team keeps it on — caching, incremental analysis, parallelism, and test-suite speed — so quality enforcement and developer velocity (DORA, key 85) reinforce rather than fight.

## 2. Mechanism (the spine)
- **Build caching:** Gradle build cache (local + remote) + configuration cache; Maven incremental/`-o` + reactor; CI dependency caching (`~/.m2`, Gradle caches) — don't re-download/re-build unchanged work.
- **Incremental analysis:** run analyzers on changed modules/files where supported; Sonar PR analysis on new code (keys 76/80); avoid full-repo re-scan every PR.
- **Parallelism:** Maven `-T` (parallel modules), Gradle parallel + workers, JUnit parallel test execution; CI job parallelism/sharding (split tests across runners).
- **Test-suite speed:** fast unit tests in the inner loop, slower integration/Testcontainers (key 45) in a later stage; quarantine/fix flaky tests (key 49) that waste retries; avoid unnecessary container starts.
- **Stage placement:** cheap checks on PR, expensive (full mutation/DAST/perf) on main/nightly (key 75) — keep the blocking PR path short.
- **Measure it:** track pipeline duration as a metric (a meta-quality signal); a creeping build time is debt (key 59).

## 3. Evidence FOR
- **Fast gates stay enabled** — the practical precondition for every other gate in the book; speed protects adherence (key 06).
- **Caching/incremental/parallel are well-supported** in Maven/Gradle + CI — mostly configuration, big wins.
- **Speed + stability reinforce** (DORA, key 02/85) — a fast gate enables frequent small merges (trunk-based, key 81).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Caching can mask staleness/incorrectness** — a bad cache key can skip work that should run (false green); cache invalidation is famously hard. Verify cache correctness; clean builds on main.
- **Parallelism surfaces flakiness/ordering bugs** — tests with hidden shared state fail under parallel execution (key 49); needs isolated tests.
- **Incremental analysis can miss cross-module effects** — a change's impact outside the changed file; periodic full scans (nightly) backstop.
- **Optimization has diminishing returns + complexity cost** — an over-tuned build is itself hard to maintain (key 62); optimize the actual bottleneck, not by guess.
- **Sharding/remote-cache infra has its own cost.**

## 5. Current status
Build caches (Gradle remote/config cache, Maven reactor), CI caching, parallel test execution, and Sonar PR-incremental analysis are current standard practice. *(Flags/versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** CI caching of `~/.m2` + parallel test config + a fast-PR / full-main split; show pipeline-time before/after. Config artifact (verified for consistency).
- **Figure:** Fig 79.1 — where time goes in a gate (deps/compile/tests/analysis) → the lever for each (cache/incremental/parallel/stage-split). Trace to Maven/Gradle/CI docs.

## 7. Gap-filling (verification queue)
- ⚠ Gradle build/config cache + Maven `-T` flags, JUnit parallel config, CI cache syntax — verify at pin.
- ⚠ Sonar incremental/PR-analysis specifics (key 76) — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Gradle build/configuration cache | docs.gradle.org | ☑; ⚠ flags |
| 2 | Maven parallel/reactor; CI caching | maven.apache.org; (key 77) | ☑; ⚠ flags |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis) | cache + incremental + parallel + stage-split; speed protects adherence |

---
## Learnings & pipeline suggestions
- Frame **speed as a quality enabler** (protects every other gate). Parallelism ↔ flakiness link to key 49. **Cross-ref:** 75, 76, 80, 82, 45, 49, 59, 85.
