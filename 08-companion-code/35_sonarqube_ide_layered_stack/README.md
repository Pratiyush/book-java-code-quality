# Chapter 17 — Composition, Not Accumulation (`sonarqube-ide-layered-stack`)

SonarQube as the platform, the IDE as the first line, and how to layer analyzers into one coherent stack —
at the smallest size that still teaches it. This is a CONFIG-centric chapter: the headline artifacts are
configuration; the runnable, unit-tested part is the composition **decision** the configuration enacts. It
is a child module of the companion-code reactor; it adds no version literals and inherits the runtime and
test-library pins from the aggregator. It has **zero runtime dependencies** — every surface is built on the
JDK alone.

## What this module is, and is not

The chapter spans three layers — author-time, build/CI, and the platform above — so the module ships the
configuration for each plus a runnable model of the composition rule that ties them together:

- **`sonar-project.properties`** — the Sonar rule engine's inputs (`sonar.java.binaries` and the classpath
  `sonar-java` resolves symbols against — required for a multi-file Java project, or analysis fails) and the
  **quality gate** scoped to **new code** (Clean as You Code), with `sonar.qualitygate.wait=true` so the
  scanner **fails the build** when the gate fails. **Configuration**, not run by this module's Maven build.

- **`.editorconfig`** — the **shared author-time first line**: one committed formatting style so every
  developer's IDE and the build's formatter agree, with a **connected-mode** note (bind SonarQube for IDE to
  the team's project to pull the same profile and gate). The author-time layer is a *first line, not a gate*.

- **`ci/sonar-analysis.yml`** — the **CI Sonar step**: the bare analyzers run as the local layered gate, then
  the Sonar platform layer runs above them and applies the gate on new code. **Configuration**, not run by
  this build.

- **The composition model in `org.acme.layered`** — the load-bearing decision made runnable and unit-tested:
  `LayeredStack` assigns **one owner per concern**, refuses a second owner (the redundancy the rule removes),
  reports the owners back **cheap-first** by moment, and surfaces an unowned concern as a coverage gap. The
  `-Pquality` profile is the **local layered gate**: Checkstyle (source view) ordered before SpotBugs
  (bytecode view), so `mvn -Pquality verify` is the local equivalent of the gate (Chapter 27, local/CI
  parity) — the two cannot silently drift.

Sonar is a hosted / continuously-released platform. `00-strategy/SOURCE-PIN.md` §2 pins the Sonar row at
**SonarQube Server 2026.1 LTA (patch 2026.1.3)** and treats the line as version-sensitive: the scanner GAV
version, the named rules' default severities, and edition gating are recorded `⚠ verify at pin`
(`09-flags/35_sonar_versions_and_defaults_unverified.md`). So every Sonar version in the config files is
**dated at use (2026-06)**, never timeless, and no rule severity or edition feature is asserted from memory.
The GitHub Actions in the YAML are hosted/SaaS — SOURCE-PIN §5 records them as "docs as of 2026-06
(rolling)", so they are **dated at use (2026-06)** and flagged for digest pinning at adoption.

## What it demonstrates

| Teaching | Where |
|---|---|
| Substrate × moment: what a tool can see and how fast | `Substrate`, `Moment`, `Analyzer` |
| One owner per concern (composition, not accumulation) | `LayeredStack.assign`, `Concern` |
| Redundancy removed: a second owner is refused | `LayeredStack.assign` (throws) |
| Cheap-first / fail-fast ordering | `LayeredStack.orderedCheapFirst` |
| The local layered gate (source pass then bytecode pass) | `mvn -Pquality verify` (Checkstyle, then SpotBugs) |
| Sonar rule-engine inputs + Clean-as-You-Code gate | `sonar-project.properties` |
| Author-time shared first line + connected mode | `.editorconfig` |
| The CI Sonar step above the bare analyzers | `ci/sonar-analysis.yml` |

## Build and run

```
# fast build (compile + tests), standalone
mvn -B -f pom.xml verify

# with the local layered gate (Checkstyle then SpotBugs) — the local equivalent of the stack
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The Sonar config,
the `.editorconfig`, and the CI YAML are configuration and are validated separately (parseable config), not
by this build — running the Sonar scanner itself needs a live SonarQube Server / Cloud and a token.

## The failure path

`LayeredStack` carries two explicit failure paths, both driven by tests. `assign` **refuses a second owner**
for a concern (`IllegalStateException`) — the redundancy the composition rule removes, surfaced as a
deliberate decision rather than a silent duplicate checker. `requireOwnerOf` **fails loudly on an unowned
concern** (`IllegalStateException`, "a coverage gap") rather than silently skipping it, because an unowned
concern is a blind spot no tool in the stack is watching. At the platform layer, the analogous failure path
is `sonar.qualitygate.wait=true`: the scanner fails the build when the quality gate fails on new code.

## Observability surface

`LayeredStack.ownerCount()` exposes how many concerns have an owner — the coverage metric a dashboard trends;
read against `Concern.values()`, a count below the total means blind spots. `isReady()` is a readiness probe
over coverage: a stack that owns nothing could only pass everything, the silent way a gate stops gating, so
it reports not-ready rather than waving changes through. The Sonar dashboard is the platform-layer
observability surface the chapter describes.

## Honest edges

More tools is not more quality — overlap costs build time without coverage gain, so the stack assigns one
owner per concern and refuses a second. The IDE is a first line, not a gate: local and settings-dependent,
so it is shared (committed `.editorconfig`, connected mode) and backed by CI; "clean in my IDE" is not a
contract. Sonar's flagship security analysis (taint SAST for Java) is a paid-edition / Cloud capability, not
the free Community Build. The debt and rating model rests on configurable conventions (a default
"30 minutes to develop a line") — a coarse trend signal, never a precise figure. And a green gate is a
policy met, not proof of correctness: it does not replace tests (Part V) or runtime security testing. Each
tool here is named to its own documentation, and no tool is crowned.
