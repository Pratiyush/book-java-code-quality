# FLAG — key 80 (folds 77, 78) — PR coverage platforms + CI actions in the companion config are SaaS/rolling, dated-at-use (not version-pinned)

- **Raised:** 2026-06-26 by `example-builder` during the Chapter 34 EXAMPLE-BUILD (Floor C).
- **Severity:** minor-to-material (the platform/CI configuration is illustrative, not run by the Maven
  build; but the platforms, actions, schemas, and versions it names must not read as timeless or pinned
  facts).
- **Floor:** C (SOURCE-TRACE) — every version/identifier traces to the pin or is marked dated-at-use.

## Issue

The chapter names PR coverage/decoration **platforms** and CI **actions** that `SOURCE-PIN.md` does not
pin as versioned artifacts:

- **Codecov, Coveralls, SonarQube PR analysis** — hosted PR coverage/decoration services. `SOURCE-PIN.md`
  has **no row** for Codecov or Coveralls as versioned artifacts; SonarQube is pinned as a **server**
  (§2, Server 2026.1 LTA) but its PR-decoration behaviour is part of the hosted/continuously-released
  product surface. These are presented as **dated at use (2026-06)**, not pinned.
- **GitHub Actions** (`actions/checkout`, `actions/setup-java`, `codecov/codecov-action`) — SaaS/rolling;
  `SOURCE-PIN.md` §5 records GitHub Actions as "docs as of 2026-06 (rolling)". The marketplace actions
  carry mutable major-version tags (`@v4`, `@v5`); a fully reproducible pin is `@<full-40-char-commit-sha>`
  plus a release comment. **Not done here** because the file is illustrative config and SOURCE-PIN treats
  GitHub Actions as rolling.
- **Danger** (`danger.systems`) — a continuously-released tool with no `SOURCE-PIN.md` row; the
  `npx danger ci` invocation names no version. Dated-at-use 2026-06.

## Atoms affected (in `08-companion-code/80_coverage_pr_automation_platforms/`)

- `ci/coverage-pr.yml` — `actions/checkout@v4`, `actions/setup-java@v4` (SaaS, dated-at-use 2026-06),
  `codecov/codecov-action@v5` (SaaS PLATFORM action, dated-at-use 2026-06, pin to a digest at adoption),
  `npx danger ci` (no pinned version, dated-at-use). The GitLab CI / Jenkins mapping note names no
  versioned identifier. JaCoCo's `jacoco.xml` path is the one pinned-tool coordinate (built at 0.8.15;
  see the JaCoCo flag below).
- `.codecov.yml` — the `coverage.status.project` / `coverage.status.patch` / `comment` keys use Codecov's
  documented config schema **as of 2026-06**; they are dated-at-use, not asserted as a timeless schema.
  The `target: 80%` patch threshold is the chapter's own illustrative new-code bar (clean-as-you-code),
  not a value claimed from any tool's defaults.

## Related (carried from the peer key-23 build)

JaCoCo is built at **0.8.15**, not the `SOURCE-PIN.md` §3 pin of 0.8.16, because 0.8.16 is unpublished
(Maven Central 404; the local cache holds only a `.lastUpdated` 404 stub for 0.8.16 and a real jar for
0.8.15). This is the same deviation recorded in `09-flags/48_jacoco_pin_0816_unpublished.md`; it is a
deviation forced by artifact availability, not an invented version. 0.8.15 covers the anchor JDK 21
(support since 0.8.11) and the forward JDK 25 (since 0.8.14).

## What was NOT done (never-invent / SOURCE-PIN floor)

No platform/action/tool version was invented beyond `SOURCE-PIN.md` + the dossier; no Codecov/Coveralls/
Sonar version, GitHub Actions digest, or Danger version was asserted; no upstream sample workflow or
`.codecov.yml` was copied (the config is original-for-this-book). SaaS/rolling/unpinned atoms are
presented as dated-at-use, not timeless. The chapter prose treats the platforms and PR tools as a
multi-tool survey crowning none and routes platform specifics across GitHub Actions / GitLab / Jenkins,
so no unpinned platform fact is asserted as settled in the prose.

## EXAMPLE-BUILD handling (2026-06-26)

The module builds green via `mvn -B -Pquality verify` (32 tests, JaCoCo branch ratio 1.00 against a
0.90 floor, 0 Checkstyle violations, 0 SpotBugs findings, warning-clean). The 4 config snippet tags
(`coverage-upload-step`, `danger-tests-touched`, `codecov-patch-threshold`, `codecov-bot-comment`)
display only the illustrative configuration; the 2 tags inside the build (`new-code-gate` in
`CoverageGate.java`, `jacoco-pr-report` in `pom.xml`) realize the runnable core and the report goal. The
displayed config snippets carry inline comments marking the SaaS platforms/actions as dated-at-use.

## Required action

At `/pin-source` / SOURCE-VERIFY (Step 5) and before any public push (COMPANION-REPO §5): decide whether
to add SOURCE-PIN rows for Codecov/Coveralls (or keep them dated-at-use), pin the GitHub Actions
(including `codecov/codecov-action`) to commit-sha digests, re-confirm Codecov's config-schema keys
against the platform's own docs, and re-confirm the JaCoCo coordinate per the JaCoCo re-pin runbook. The
GitLab CI / Jenkins coverage-decoration equivalents are described in a note only and assert no versioned
fact.

## Status

OPEN — resolve at `/pin-source` / SOURCE-VERIFY (Step 5); revisit at public-push sign-off.
