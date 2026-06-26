# Chapter 36 — When Quality Meets Reality (`release-quality`)

Release quality at the smallest size that still teaches it. This is a CONFIG-centric chapter: the release
*process* is illustrative configuration; the runnable, unit-tested part is the release-readiness **gate**
the process enforces. It is a child module of the companion-code reactor; it adds no version literals and
inherits the runtime and test-library pins from the aggregator. It has **zero runtime dependencies** —
every surface is built on the JDK alone.

## What this module is, and is not

The runnable, tested core, plus the release configuration it enforces, kept in lock-step:

- **The release-readiness gate in `org.acme.release`** — the load-bearing decision the chapter argues
  for, made runnable and unit-tested. `ReleaseReadiness.evaluate` asserts the chapter's release
  preconditions against an externalized profile policy: the version is a release and not a `-SNAPSHOT`
  (semver, key 60), the changelog carries an entry for it, every CI gate is green on the release commit,
  the artifact is signed/attested with an SBOM (Part VII, key 66), and the staged build passed its smoke
  test. It returns `ReleaseDecision` — `Ready`, or `Blocked` naming exactly the failed checks. This is the
  **local equivalent** of the release gate the workflow wires, so `mvn -Pquality verify` exercises both
  the static-analysis stage and the gate logic — they cannot silently drift (Chapter 27, local/CI parity).

- **The release configuration** the gate enforces, none of it run by this build:
  - `release/SEMVER-POLICY.md` — the semantic-versioning contract (`MAJOR.MINOR.PATCH`, semver.org).
  - `release/CHANGELOG.md` — a [Keep a Changelog](https://keepachangelog.com) file; the gate's
    `CHANGELOG_ENTRY` check is satisfied only when the released version has an entry here.
  - `ci/release.yml` — an illustrative GitHub Actions release workflow: build green → release gate → sign
    + SBOM → progressive rollout (canary / feature flag) → post-release feedback loop.
  - `release/release-gate.sh` — the gate's checks in shell form, the same decision as a pipeline step.

`FeatureFlag` is the small companion mechanism the chapter names: deploy dark, release gradually,
kill-switch off — decoupling *deploy* from *release*.

Action and tool versions are pinned where `00-strategy/SOURCE-PIN.md` pins them (the Java 21 anchor; the
analyzers in the pom). The GitHub Actions in the YAML (`actions/checkout`, `actions/setup-java`) are
hosted/SaaS — SOURCE-PIN §5 records them as "docs as of 2026-06 (rolling)", so they are **dated at use
(2026-06)** and flagged for digest pinning at adoption. The Maven release/versions plugin versions a real
tag-and-deploy step would invoke are **not** separately pinned by SOURCE-PIN — see
`09-flags/83_release_versioning_plugin_versions_unpinned.md`.

## What it demonstrates

| Teaching | Where |
|---|---|
| Release gates assert the artifact, not the code | `ReleaseReadiness.evaluate`, `ReleaseCheck` |
| A blocked release names exactly what failed | `ReleaseDecision` (sealed: `Ready` / `Blocked`) |
| Semver: a release is not a `-SNAPSHOT` (key 60) | `SemanticVersion.isRelease` / `isSnapshot` |
| Changelog convention (Keep a Changelog) | `release/CHANGELOG.md` |
| Externalized config profiles (`dev` / `prod`) | `ReleasePolicy`, `release-*.properties` |
| Decouple deploy from release (flag + kill-switch) | `FeatureFlag` |
| Progressive delivery + feedback loop (process) | `ci/release.yml` jobs |
| The local equivalent of the CI static-analysis gate | `mvn -Pquality verify` (Checkstyle + SpotBugs) |

## Build and run

```
# fast build (compile + tests), standalone
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs) — the local equivalent of the CI gate
mvn -B -Pquality -f pom.xml verify

# select a release-policy profile (default dev)
mvn -B -Drelease.profile=prod -f pom.xml verify

# run the release-gate shell directly (illustrative; not part of the Maven build)
RELEASE_VERSION=2.4.0 CI_GREEN=true SIGNED_WITH_SBOM=true SMOKE_TESTED=true ./release/release-gate.sh
```

A green build reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The CI YAML
and the shell script are configuration and are validated separately, not by this build.

## The failure path

`ReleaseReadiness.evaluate` is the explicit failure path: it returns a `ReleaseDecision` — a sealed type
with `Ready` and `Blocked` variants — rather than a bare boolean. A release ships only when every
precondition the active profile requires is met; otherwise the decision is `Blocked`, carrying the exact
list of failed checks (a snapshot version, a missing changelog entry, a red pipeline, an unsigned
artifact) so the failure is actionable, not a bare refusal. There is no third state — a release is green
on every required check or it does not go out.

## Observability surface

`ReleaseReadiness.blockedCount()` exposes a running count of evaluations that blocked a release — the
headline metric a dashboard trends the way it trends change-failure rate (DORA, key 85). `isReady()` is a
readiness probe over the wired policy: a gate with no policy could only fail open (release everything),
the silent way a release gate stops gating, so it reports not-ready rather than waving artifacts out.

## Honest edges

A safe release process limits the **damage** of a defect; it does not prevent one — the prevention is the
rest of the book (types, tests, analysis, secure coding, and the human review of Chapter 84 that catches
the logic flaw no gate sees). Progressive delivery needs observability or its canary analysis is blind
(Part XIII). A feature flag becomes debt if it is not removed after rollout — a removal discipline, like
any debt. Rollback is not always clean: stateful changes need backward-compatible (expand-contract)
migrations, because a raw rollback cannot reverse a schema change. The post-release feedback loop is
theatre unless an incident is actually triaged into a fix, a test, and where warranted a new gate. And a
release gate verifies the artifact, not the design: a green release is the traceable thing the pipeline
produced, not proof the feature is correct.
