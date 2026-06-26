# Chapter 33 — A Gate the Team Keeps On (`ci-pipeline-quality-gates`)

The CI pipeline and its quality gate, at the smallest size that still teaches them. This is a
CONFIG-centric chapter: the pipeline itself is illustrative CI configuration; the runnable, unit-tested
part is the gate **policy** the pipeline enforces. It is a child module of the companion-code reactor;
it adds no version literals and inherits the runtime and test-library pins from the aggregator. It has
**zero runtime dependencies** — every surface is built on the JDK alone.

## What this module is, and is not

Two artifacts, one module, kept in lock-step:

- **`ci/quality-gates.yml`** — an illustrative GitHub Actions pipeline that wires the gates the chapter
  teaches: fail-fast stages ordered cheap-to-expensive (compile and fast lint, then unit tests and
  coverage, then heavier static analysis and a dependency scan), a **pull-request-fast versus
  main-full split** that defers the expensive checks off the critical path, **`~/.m2` dependency
  caching** and **parallel execution** (`-T`) for speed, and a **clean-as-you-code quality gate** wired
  as the required status check. This file is **configuration** — it is **not** run by this module's
  Maven build. Its stages run the same `mvn` commands a developer runs locally.

- **The gate policy in `org.acme.cigate`** — the load-bearing decision the chapter argues for, made
  runnable and unit-tested: `QualityGate.evaluate` scopes findings to new code (clean-as-you-code),
  blocks on new high-severity findings, and warns on the rest. This is the **local equivalent** of the
  CI gate, so `mvn -Pquality verify` exercises both the same static-analysis stage the YAML names and
  the gate logic the YAML wires — the two cannot silently drift (Chapter 27, local/CI parity).

Action and tool versions are pinned where `00-strategy/SOURCE-PIN.md` pins them (the Java 21 anchor;
the analyzers and coverage tool in the pom). The GitHub Actions in the YAML (`actions/checkout`,
`actions/setup-java`, `actions/cache`) are hosted/SaaS — SOURCE-PIN §5 records them as "docs as of
2026-06 (rolling)", so they are **dated at use (2026-06)**, not timeless, and are flagged for digest
pinning at adoption (see `09-flags/`).

## What it demonstrates

| Teaching | Where |
|---|---|
| Fail-fast, cheap-to-expensive stage ordering | `ci/quality-gates.yml` jobs `build-and-lint` → `test-and-coverage` → `static-and-security` |
| PR-fast vs main/nightly split (fast feedback) | `ci/quality-gates.yml` `on:` triggers + `deep-checks-main-nightly` |
| Dependency caching + parallel execution (speed) | `ci/quality-gates.yml` `actions/cache` step + `mvn -T 1C` |
| Clean-as-you-code (new vs whole-repo) | `QualityGate.evaluate`, `GatePolicy.cleanAsYouCode` |
| Block-versus-warn policy | `GateDecision` (sealed: `Pass` / `Warn` / `Block`) |
| Externalized config profiles (`dev` / `prod`) | `GatePolicy`, `cigate-*.properties` |
| The local equivalent of the CI static-analysis gate | `mvn -Pquality verify` (Checkstyle + SpotBugs) |

## Build and run

```
# fast build (compile + tests), standalone
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs) — the local equivalent of the CI gate
mvn -B -Pquality -f pom.xml verify

# select a gate-policy profile (default dev)
mvn -B -Dcigate.profile=prod -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The CI YAML
is configuration and is validated separately as parseable YAML, not by this build.

## The failure path

`QualityGate.evaluate` is the explicit failure path: it returns a `GateDecision` — a sealed type with
`Pass`, `Warn`, and `Block` variants — rather than a bare boolean. The third variant is the chapter's
policy point: the gate **blocks** the build only on a new finding at or above the policy's block
severity; a less-severe new finding is **warned** (surfaced, not blocking); and a pre-existing finding
is out of scope under clean-as-you-code, so a change that adds nothing new **passes** even atop a
mountain of legacy debt — gating whole-repo absolutes would stop every pull request (Chapter 19).
`Block` carries the worst new finding's rule id so the failure is actionable, not a bare red mark.

## Observability surface

`QualityGate.blockedCount()` exposes a running count of evaluations that blocked a merge — the headline
metric a dashboard trends the way it trends pipeline duration (a block rate stuck at zero may mean the
gate is too loose; one stuck high, too strict). `isReady()` is a readiness probe over the wired policy:
a gate with no policy could only fail open, the silent way a gate stops gating, so it reports not-ready
rather than waving changes through.

## Honest edges

The gate blocks new-code findings only — whole-repo absolutes block every PR on inherited debt
(Chapter 19), the number-one way a gate is routed around. A green gate means "no detected policy
violations on new code," not good code: design and logic still need human review (Chapter 84). A gate
measures a proxy, so one gated on a number gets gamed — coverage percentage to assertion-free tests
(Chapter 23). The cache that keeps the pipeline fast can false-green on a bad key, so a clean build on
the trunk backstops it. Parallel execution surfaces flaky tests with hidden shared state — a real bug
the speedup exposed, not a regression (Chapter 20). And no pipeline fixes a culture that rubber-stamps
red builds or `[skip ci]`s (Chapter 1): the gate enforces policy, it does not create the will to care.
