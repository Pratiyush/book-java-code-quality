# Chapter 34 — Making the Gate Real for the Developer (`coverage-pr-automation-platforms`)

A coverage strategy that improves tests instead of gaming them, the platforms that run the pipeline,
and getting the verdict onto the pull request. This is a CONFIG-centric chapter: most of what it
teaches lives in illustrative configuration; the runnable, unit-tested part is the load-bearing
decision that configuration enforces. It is a child module of the companion-code reactor; it adds one
version literal (JaCoCo, which the aggregator does not manage) and inherits the runtime and
test-library pins from the parent. Its runnable surface is built on the JDK alone.

## What this module is, and is not

Two kinds of artifact, one module, kept in lock-step:

- **Configuration the chapter teaches** — not run by this build:
  - `ci/coverage-pr.yml` — a GitHub Actions pipeline that runs the tests with JaCoCo, uploads the
    report to a PR coverage platform that decorates the pull request, and runs a Danger process check.
  - `.codecov.yml` — the platform's config: a **patch** (diff-coverage, new-code) threshold and a
    diff-scoped bot-comment policy. Codecov's schema is shown; Coveralls and SonarQube PR analysis
    express the same two ideas in their own schemas.

- **The runnable decision the configuration enforces** — built and unit-tested in `org.acme.coverage`:
  - `CoverageGate` — the diff-scoped, ratcheting gate: gate the coverage of **new** code against a
    bar, fail when **overall** coverage drops, leave pre-existing legacy untouched. Returns a sealed
    `CoverageVerdict` (pass / warn / block), never a bare boolean.
  - `PrCommentPolicy` — the bot-comment policy: comment only on lines the pull request changed, and
    filter by severity.

`mvn -Pquality verify` runs JaCoCo over that code **and** the gate logic the config enforces, so the
prose and the build cannot silently drift (Chapter 34 diff-scoping; Chapter 27 local/CI parity).

## Version and source pinning (read before trusting a number)

- **JaCoCo** is built at **0.8.15**. `SOURCE-PIN.md` §3 pins 0.8.16, but that artifact is not published
  (Maven Central 404s; the local cache holds only a 404 stub for 0.8.16 and a real jar for 0.8.15).
  0.8.15 is the published latest and covers the anchor JDK 21 (support since 0.8.11) and the forward
  JDK 25 (since 0.8.14). The same deviation the peer key-23 module hit; see
  `09-flags/48_jacoco_pin_0816_unpublished.md`.
- **The PR coverage platforms** (Codecov, Coveralls, SonarQube PR analysis) and the **GitHub Actions**
  in the YAML are hosted/SaaS with no `SOURCE-PIN.md` version row; they are **dated at use (2026-06)**,
  not pinned, and their marketplace actions must be pinned to a commit digest at adoption. See
  `09-flags/80_coverage_pr_platforms_saas_dated_at_use.md`. No platform/action version is asserted as
  timeless.

## What it demonstrates

| Teaching | Where |
|---|---|
| New-code focus (gate the diff, not the legacy mountain) | `CoverageGate.evaluate`, `.codecov.yml` `patch` |
| Ratchet (overall coverage may only go up) | `CoverageGate.evaluate`, `CoveragePolicy.ratchet` |
| Overall as a warn-only floor, never a hard bar | `CoverageGate` (warn path), `.codecov.yml` `project.informational` |
| The coverage report a platform uploads | `pom.xml` JaCoCo `report` goal → `jacoco.xml` |
| The platform upload/decoration step | `ci/coverage-pr.yml` `coverage` job |
| Diff-scoped bot comment (comment only on changed lines) | `PrCommentPolicy`, `.codecov.yml` `comment` |
| Severity-filtering (keep the bot signal, not noise) | `PrCommentPolicy`, `Severity.atLeast` |
| A process (PR-hygiene) rule, distinct from a code linter | `ci/coverage-pr.yml` Danger step |
| Externalized config profiles (`dev` / `prod`) | `CoverageProfiles`, `coverage-*.properties` |

## Build and run

```
# build + tests + JaCoCo coverage report and floor
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs) — the local equivalent of the CI gate
mvn -B -Pquality -f pom.xml verify

# select a coverage-gate profile (default dev)
mvn -B -Dcoverage.profile=prod -f pom.xml test
```

A green run reports the tests passing, the JaCoCo branch floor met, zero Checkstyle violations, and
zero SpotBugs findings. The CI YAML and `.codecov.yml` are configuration, validated separately, not by
this build.

## The failure path

`CoverageGate.evaluate` is the explicit failure path: it returns a sealed `CoverageVerdict` rather than
a boolean. It **blocks** only on a new finding the chapter would block on — new-code coverage under the
bar, or overall coverage dropping while the ratchet is on — **warns** when overall coverage is below an
aspirational target (surfaced, not blocking), and **passes** an otherwise-clean change even atop a
mountain of legacy debt, because gating whole-repo absolutes would stop every pull request (Chapter
19). A `Block` carries the failing rule so the PR comment is actionable. The records reject malformed
input (a ratio outside `[0, 1]`, a blank file, a non-positive line) at construction.

## Observability surface

`CoverageGate.blockedCount()` exposes a running count of merges the gate has blocked — the headline
metric a dashboard trends the way it trends pipeline duration (a block rate stuck at zero may mean the
bar is too low; one stuck high, too strict). `isReady()` is a readiness probe over the wired policy: a
policy that gates nothing (a zero bar with no ratchet) could only ever pass — the silent way a gate
stops gating — so it reports not-ready rather than waving every change through.

## Honest edges

Coverage is a floor, not a goal: a fully-covered but assertion-free method passes the coverage gate yet
tests nothing, so the gate treats the number as a floor and leaves quality to mutation testing (Chapter
23) and review (Chapter 84). New-code coverage can be gamed too with trivial tests; the bar is
necessary, not sufficient. A green PR check means "no detected policy violation on the diff," not good
code — design and logic still need a human. A diff-scoped bot is mandatory: an un-scoped or over-eager
bot is muted, and a muted bot is a disabled gate. The three PR tools overlap (reviewdog, Sonar, Danger
can triple-report), so responsibilities are split: lint output to reviewdog, the gate to Sonar, process
rules to Danger. And the platform choice is usually dictated by where the code lives, not a free pick;
the gate design ports across GitHub Actions, GitLab CI, and Jenkins, and the book crowns none.
