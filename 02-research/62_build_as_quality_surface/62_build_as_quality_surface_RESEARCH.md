# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` Maven vs Gradle — NEUTRALITY balance + non-crowning,
> each cited to its own docs. Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 62 — `01-index/CANDIDATE_POOL.md` · **Title:** The build as a quality surface — Maven vs Gradle quality practice
- **Part:** VII — Build, dependencies & supply chain · **Tier:** B · **Cmp:** ⚠
- **Primary authorities:** Apache Maven (`maven.apache.org`), Gradle (`docs.gradle.org`); their quality-relevant plugins (Surefire/Failsafe, Enforcer; Gradle native test/`check`). Ties to key 05 (toolchain map).

## 1. Core definition & purpose
The build is where every quality gate actually runs — formatters, linters, analyzers, tests, coverage, scanners (key 05) all hang off Maven/Gradle. A disciplined build is itself a quality artifact: reproducible, fast, fail-fast, and identical locally and in CI (key 82). This chapter covers what makes a *build* high-quality and contrasts the two dominant tools neutrally — each is a reasonable choice; the quality practices matter more than the pick.

## 2. Mechanism (the spine)
- **The build lifecycle as gate host:** bind checks to phases (Maven: `validate`→`compile`→`test`→`verify`; Gradle: the `check` task aggregates verification). Order cheap/fast checks first (format, lint) → heavier (SpotBugs, Sonar) → coverage/mutation last (keys 75/79).
- **Maven** — declarative POM, convention-over-configuration, stable lifecycle; quality plugins: `maven-surefire`/`failsafe` (tests), `maven-enforcer-plugin` (ban rules: dependency convergence, banned deps, JDK/version requirements), the analyzer plugins (checkstyle/pmd/spotbugs/jacoco). Reproducible-builds support (key 67).
- **Gradle** — programmable (Groovy/Kotlin DSL), incremental builds + build cache (speed), `check` lifecycle, version catalogs (key 63); quality via the same analyzer plugins.
- **Quality properties of a good build (tool-agnostic):** reproducible (pinned versions, no `LATEST`/ranges — key 67); fast (incremental/cache/parallel — key 79); fail-fast with clear errors; local↔CI parity (key 82); a single source of dependency truth (BOM/catalog — key 63); the wrapper committed (`mvnw`/`gradlew`) so everyone builds identically.
- **The wrapper** pins the build-tool version itself — a reproducibility + onboarding win for both.

## 3. Evidence FOR
- Centralizing gates in the build = one place to enforce quality, reused by IDE + CI (shift-left, key 06).
- Maven's convention + Enforcer make org-wide rules easy; Gradle's incremental build + cache make large builds fast.
- Both have first-class plugins for every analyzer in key 05.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Build complexity is itself a quality risk** — an over-engineered Gradle build (logic sprawl) or a 2,000-line POM is hard to maintain; the build needs the same discipline as code.
- **Maven** rigidity can fight non-standard needs; **Gradle's** programmability can become unreadable build logic (each its own cost — ⚠, crown neither).
- **Slow builds erode quality** — if `verify` takes 30 min, developers skip it locally (key 79/82); speed is a quality concern, not a luxury.
- **Plugin/version drift** — unpinned plugin versions make builds non-reproducible (key 67).
- Tool choice is rarely worth a migration on its own; the practices port across both.

## 5. Current status
Maven and Gradle both current, both with full quality-plugin ecosystems and wrappers; version catalogs (Gradle) and reproducible-build support (both) are current concerns. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (the reference project, key 05/109):** the staple-stack build wired in Maven (and noted Gradle equivalent) with phase-bound gates + Enforcer convergence rule + wrapper. Built green `./mvnw -B verify`; tag-region snippet of the plugin block.
- **Figure:** Fig 62.1 — build lifecycle with quality gates bound to phases (fast→slow), one column Maven, one Gradle (parallel, no winner). Trace to each tool's docs.

## 7. Gap-filling (verification queue)
- ⚠ Maven/Gradle versions, lifecycle phase names, Enforcer rule names, `check` task semantics — verify at pin.
- ⚠ Reproducible-build flags (Maven) — confirm (key 67).

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Apache Maven docs | maven.apache.org | ☑ lifecycle/enforcer; ⚠ version |
| 2 | Gradle docs | docs.gradle.org | ☑ check/cache/catalogs; ⚠ version |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis + key 05) | both tools host the same gates; reproducibility/speed/parity are the quality axes |

---
## Learnings & pipeline suggestions
- Neutral two-tool treatment; emphasize tool-agnostic build-quality properties. **Cross-ref:** 05 (map), 63 (deps), 67 (reproducible), 79 (speed), 82 (parity), 75/76 (CI gates).
