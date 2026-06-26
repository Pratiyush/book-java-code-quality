# Chapter 27 — The Foundation Under Every Gate (`build-dependency-hygiene`)

A module whose load-bearing artifact is its `pom.xml`. The chapter's subject is the build and the
dependency graph it assembles, so the demonstration lives in the build configuration: the Maven
Enforcer rules, a single source of version truth, and the versions plugin. A small `org.acme.hygiene`
package gives the Enforcer a real compile/test graph to resolve and rule on, so the configuration is
exercised rather than merely declared. It is a child module of the companion-code reactor; it adds no
version literals beyond the two plugin-version properties (flagged below) and inherits the runtime and
test-library pins from the aggregator.

## What it demonstrates

| Hygiene mechanism | Where in the module |
|---|---|
| Convergence as a hard build event (`dependencyConvergence`) | `pom.xml` — Enforcer, tag `enforcer-convergence` |
| Catch a silent transitive downgrade (`requireUpperBoundDeps`) | `pom.xml` — Enforcer, tag `enforcer-upper-bound` |
| Ban moving versions and house-banned deps (`bannedDependencies`) | `pom.xml` — Enforcer, tag `enforcer-banned` |
| Single source of version truth (imported BOM) | `pom.xml` — `<dependencyManagement>`, tag `dep-management-bom` |
| The local half of currency (available-update report) | `pom.xml` — versions plugin, tag `versions-plugin` |
| The always-on half of currency (group + schedule) | `renovate.json`, `dependabot.yml` |

The same discipline is mirrored in code: `PinnedDependency` rejects `LATEST`/`RELEASE`/ranges at the
value boundary, and `DependencyCatalog` makes a divergent version for a known key a typed
`ConvergenceException` — the in-code analogue of what the Enforcer does to the build.

## Build and run

```
# fast build (compile + tests); the Enforcer ALWAYS runs (hygiene is not opt-in)
mvn -B -f ../pom.xml -pl 62_build_dependency_hygiene -am verify

# with the static-analysis gate (Checkstyle + SpotBugs) as well
mvn -B -Pquality -f ../pom.xml -pl 62_build_dependency_hygiene -am verify

# the local currency view (lists newer versions; changes nothing)
mvn -f pom.xml versions:display-dependency-updates
```

A green run reports the Enforcer rules passing, the tests passing, and (under `-Pquality`) zero
Checkstyle violations and zero SpotBugs findings.

## The failure path

Convergence is a hard build event, demonstrated twice. In the build, the `dependencyConvergence` rule
fails `verify` the moment two transitive paths resolve one `groupId:artifactId` to different versions;
the fix is to pin one version in `<dependencyManagement>`, which is exactly why every version here flows
from a single source of truth and the graph converges. In code, `DependencyCatalog.manage` throws
`ConvergenceException` — carrying the conflicting key and both versions — when a second, differing
version is added for a known key, rather than silently letting one win. The honest edge the chapter
names holds in both places: these checks prove the versions *agree*; they say nothing about whether a
dependency is maintained or free of a CVE (the next chapter's scan).

## Observability surface

`DependencyCatalog.convergenceRejectionCount()` exposes a running count of adds turned away to keep the
graph converged, and `isReady()` is a readiness probe — a catalog is a usable single source of truth
only once it manages at least one coordinate.

## Notes on pins

The exact `maven-enforcer-plugin` and `versions-maven-plugin` version literals are not separately pinned
in `SOURCE-PIN.md` (its build row reads "Maven 3.9.16 (+ enforcer, versions plugins)"). They are held as
the `enforcer.plugin.version` / `versions.plugin.version` properties and flagged in
`09-flags/62_enforcer_versions_plugin_versions_unpinned.md`; the values used resolve cleanly on the
pinned toolchain. Renovate and Dependabot are `⚠ rolling` in `SOURCE-PIN.md` (no fixed version), so the
config schema is pinned at point of use, per that row.
