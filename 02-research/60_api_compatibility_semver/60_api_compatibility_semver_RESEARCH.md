# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` comparison-sensitive (revapi vs japicmp). NEUTRALITY:
> each tool its own case + limit, cited to its own docs, no crown. Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 60 — `01-index/CANDIDATE_POOL.md` · **Title:** Library & shared-module API quality — semantic versioning, binary/source compatibility (revapi, japicmp)
- **Part:** VI · **Tier:** B · **Cmp:** ⚠
- **Primary authorities:** SemVer (semver.org); revapi (`revapi.org`); japicmp (`siom79.github.io/japicmp`); Java binary-compatibility rules (JLS ch.13). Adjacent: japi-compliance-checker, Clirr (legacy).

## 1. Core definition & purpose
For library and shared-module authors, **API quality = not breaking your consumers unexpectedly**. Java has precise *binary compatibility* rules (JLS ch.13) distinct from *source compatibility*, and **semantic versioning** is the contract that communicates change (MAJOR = breaking, MINOR = additive, PATCH = fixes). This chapter covers designing evolvable APIs and the tools that **mechanically detect** incompatible changes and tell you which version part to bump — turning "did we break someone?" from hope into a build gate (a fitness function, key 56).

## 2. Mechanism (the spine)
- **Binary vs source compatibility:** a change can be source-compatible but binary-incompatible (e.g. some signature/constant changes); JLS ch.13 defines binary compatibility precisely. Consumers who don't recompile care about *binary*.
- **SemVer contract:** MAJOR.MINOR.PATCH; a breaking change demands a MAJOR bump. The tools below *compute* the required bump from the actual API diff.
- **revapi:** API analysis & change-tracking; not limited to Java classes (configs/schemas too); runs standalone, as a Maven plugin, or as a library; categorizes changes by severity.
- **japicmp:** compares two JARs for binary **and** source incompatibilities; `--semantic-versioning` reports which version part to increment; Maven option `breakBuildBasedOnSemanticVersioning` fails the build when the bump doesn't match the changes.
- **Gate it:** run the check in CI against the last released artifact; fail the build on an undeclared breaking change. API-design hygiene: minimize public surface (package-private by default, key 57), prefer additive evolution, `@Deprecated(forRemoval, since)` with a migration path.

## 3. Evidence FOR
- **Mechanical detection catches what human review misses** for compatibility — humans overlook binary-only breaks; both tools detect them and map to SemVer.
- **Build-breaking gate** turns the SemVer contract into something enforced, not aspirational (fitness function, key 56).
- Both tools are **actively maintained** with Maven/Gradle integration.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **They detect *signature* breaks, not *behavioural* ones** — a method that keeps its signature but changes semantics passes the check and still breaks consumers (need tests + changelog).
- **Config/tuning cost** — excluding intentional breaks, internal packages, and generated code takes setup; noisy reports get ignored (key 39).
- **SemVer is a discipline, not magic** — the tool suggests a bump; humans still must honor it and communicate migration.
- **revapi vs japicmp** differ in scope/approach (revapi broader-than-Java, japicmp JAR-diff-focused) — a team chooses by need; neither is "best" (⚠).
- Overkill for an app with no external consumers (when-NOT-to-use: a leaf service no one depends on as a library).

## 5. Current status
Both revapi and japicmp actively maintained; SemVer the de-facto contract; JLS ch.13 the authority for binary compatibility. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module:** a tiny library v1→v2 with a deliberate binary-incompatible change; japicmp (or revapi) in CI flags it and demands a MAJOR bump. Built green; the failing report shown. Strong build candidate.
- **Figure:** Fig 60.1 — source vs binary compatibility matrix + the SemVer-bump decision the tools compute. Trace to JLS ch.13 + tool docs.

## 7. Gap-filling (verification queue)
- ⚠ revapi/japicmp current versions, exact options (`--semantic-versioning`, `breakBuildBasedOnSemanticVersioning`) — verify at pin.
- ⚠ JLS ch.13 binary-compatibility specifics (which changes are binary-incompatible) — cite from JLS (standards-edition rule).
- ⚠ SemVer spec version — cite semver.org.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | revapi | revapi.org ; github.com/revapi/revapi | ☑ scope; ⚠ version |
| 2 | japicmp | siom79.github.io/japicmp | ☑ semver option; ⚠ version |
| 3 | SemVer; JLS ch.13 | semver.org ; docs.oracle.com/javase/specs | ⚠ confirm |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | revapi japicmp compatibility 2026 | both maintained; japicmp --semantic-versioning + break-build option; revapi broader-than-Java |

---
## Learnings & pipeline suggestions
- Neutral two-tool treatment (revapi/japicmp), each cited to its own docs. **Cross-ref:** API design at code level → key 09; contract testing → key 50; deprecation/migration → keys 13/95; minimize surface → key 57; gate → key 56/76.
