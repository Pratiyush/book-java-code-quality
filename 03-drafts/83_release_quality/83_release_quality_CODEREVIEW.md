# GATE REPORT — CODE-REVIEW — Chapter 36 (key 83) `release_quality`

## Header

- **Gate:** CODE-REVIEW (Step 4b, FLOOR-C second half) — senior PR review of published companion code
- **Chapter key:** 83 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `83_release_quality`
- **Draft under review:** `03-drafts/83_release_quality/83_release_quality_v1.md`
- **Module path:** `08-companion-code/83_release_quality/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Scripts run:** `mvn -B -Pquality verify` (re-run, GREEN); per-tag `awk` extraction + brace/paren balance; `release/release-gate.sh` (ready / blocked / rc paths); secret + NEUTRALITY greps; tag-resolution check
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21, and teaches the release-readiness gate cleanly. It builds
GREEN and warning-clean on the pinned toolchain (JDK 21.0.11, Maven 3.9.16): 17 tests, 0 Checkstyle, 0
SpotBugs ("No errors/warnings found"). All six displayed `// tag::` regions are <= 9 lines, brace/element
balanced, and free of banned NEUTRALITY words; every SemVer claim is correct and honestly scoped. There
are **no BLOCKERs** and **no security/neutrality/invention findings** — FLOOR C is not blocked. Two MINOR
fixes are listed (a regex-dot false-positive note in the illustrative shell gate, and a missing
explicit-thread-safety test for `SemanticVersion.parse` immutability) plus NOTEs; they are recommended
polish, not gate blockers. PASS-WITH-FIXES because the listed MINORs should be applied by the
example-builder before approval, but none endangers a floor.

---

## Review dimensions

| # | Dimension | Verdict | Note |
|---|---|---|---|
| 1 | Correctness | **PASS** | Logic right; all-failures-collected (not first-fail); sealed exhaustive switch; immutable values; `Objects.requireNonNull` guards; no resource leak (`try-with-resources` on the properties stream); no swallowed exception (`IOException` -> `UncheckedIOException` with cause). Tests assert real behavior incl. the failure path. |
| 2 | Idiomatic Java 21 | **PASS** | Records, sealed interface + permits, switch over enum, `var`, `AtomicBoolean`/`AtomicLong`, `EnumSet`, `List.copyOf` defensive copy. No raw threads, no `System.out` in library code, no anti-patterns. |
| 3 | Security | **PASS** | Zero hardcoded secrets (grep clean). No injection sink in library code. `release-gate.sh` is `set -euo pipefail`, reads env vars with safe defaults, quotes expansions. Error messages name the failed check, not internals/stack traces. |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point; zero runtime deps; no dead code; realistic names (`checkout-v2`, `multi-currency carts`); every public type carries a one-line purpose Javadoc. Empty SpotBugs filter kept with a documented reason. |
| 5 | Prose<->code fidelity | **PASS** | All 6 displayed includes resolve to real bounded tag regions (open+close markers, 1:1). Prose claims match code: "collecting every one the candidate fails" = collect-all loop; "sealed type... exact list of what failed" = `ReleaseDecision`; "never a development -SNAPSHOT" = `semver-release`. SemVer atoms (MAJOR/MINOR/PATCH; -SNAPSHOT = pre-release) correct. Unpinned plugin/action versions correctly flagged, not invented. |
| 6 | Neutrality in code | **PASS** | No banned phrasing (`better than` / `unlike X` / `superior` / `beats` / `the problem with X`) in any comment, identifier, log string, or config. Grep clean across src/release/ci/config/README. |

---

## Build / lint result

```
$ export JAVA_HOME=/opt/homebrew/opt/openjdk@21/...; export PATH=.../maven/bin:$JAVA_HOME/bin:$PATH
$ mvn -B -Pquality verify   (08-companion-code/83_release_quality)
  Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
  You have 0 Checkstyle violations.
  BugInstance size is 0      (SpotBugs: "No errors/warnings found")
  BUILD SUCCESS
```
- Build GREEN and warning-clean (re-run by this gate, not taken on trust). **PASS.**
- Integration test per public behavior incl. failure path: `ReleaseReadinessTest` (9 cases) drives the
  full stack (profile load -> candidate -> decision); `blocksSnapshotVersion`,
  `blocksMissingChangelogEntry`, `blocksWhenCiNotGreen`, and `blockedDecisionListsAllFailures` exercise
  the REAL failure path and assert the exact failed-check list (not vacuous). `unknownProfileIsRejected`
  asserts the load-time `IllegalArgumentException`. **PASS.**
- Hardcoded-secret grep (password/secret/token/apikey/private-key/AWS-key): **0 hits.** **PASS.**

---

## Displayed snippet audit (the CRITICAL check)

Every displayed `// tag::` region: <= 9 lines, brace/element-balanced (or a clean opening excerpt), no
banned NEUTRALITY word.

| Tag | File | Lines | Balanced | Banned words | Verdict |
|---|---|---|---|---|---|
| `release-readiness` | `ReleaseReadiness.java` | 6 | braces 2/2, parens 6/6 | none | PASS |
| `release-decision` | `ReleaseDecision.java` | 8 | braces 4/4, parens 3/3 | none | PASS |
| `semver-release` | `SemanticVersion.java` | 8 | braces 3/3, parens 4/4 | none | PASS |
| `changelog-entry` | `release/CHANGELOG.md` | 7 | markdown, well-formed | none | PASS |
| `release-policy` | `release-prod.properties` | 5 | key=value, complete | none | PASS |
| `feature-flag` | `FeatureFlag.java` | 8 | braces 2/2, parens 6/6 | none | PASS |

Two further buildable tags exist but are NOT displayed in the draft (so not gate-critical), both verified
sound for completeness: `release-gate-sh` (`release/release-gate.sh`, 7 lines, `case`/`esac` balanced) and
`release-gate-step` (`ci/release.yml`, 7 lines, valid YAML step). No mid-statement breaks anywhere.

**Conclusion: no broken-mid-statement snippet, no over-length region, no banned word in any displayed
region. No snippet BLOCKER.**

---

## SemVer correctness (explicitly confirmed)

- `isRelease()` => `preRelease == null`: a normal/release version has no pre-release suffix (semver.org
  §9). **CORRECT.**
- `isSnapshot()` => `"SNAPSHOT".equals(preRelease)`: `-SNAPSHOT` is Maven's development/pre-release
  convention; code+comments attribute it to Maven (not to semver.org). **CORRECT.**
- MAJOR=breaking / MINOR=additive / PATCH=fix (Javadoc + `SEMVER-POLICY.md`): matches semver.org §§7-8.
  **CORRECT.**
- Regex `^(\d+)\.(\d+)\.(\d+)(?:-([0-9A-Za-z.-]+))?$`: the Javadoc explicitly calls this "a pragmatic
  subset of the semver.org grammar... enough to separate a release from a pre-release." It omits build
  metadata (`+...`) and does not enforce semver.org §9's no-leading-zero / non-empty pre-release-identifier
  rule — but it does not CLAIM full conformance, so the scope is honest, not wrong. Runtime behavior
  confirmed: `2.4.1` release, `2.5.0-SNAPSHOT` snapshot, `3.0.0-rc.1` pre-release-not-release, `v2`
  rejected. **CORRECT and honestly bounded.**

`release-gate.sh` shell SemVer check `case "$VERSION" in *-SNAPSHOT|*-*)`: the `*-*` arm catches every
hyphenated pre-release (so `*-SNAPSHOT` is redundant-but-explicit). Verified: `2.4.0` passes, all
hyphenated forms blocked. **CORRECT** (matches the Java `isRelease()` semantics).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Changelog grep interpolates the version into a regex with **unescaped dots**, so `[2.4.0]` also matches a heading like `[2X4X0]` (dots act as `.` wildcards). Theoretical false-positive only; the version is a controlled tag and real changelogs do not carry such headings. Illustrative-config file, not run by the build. | MINOR | `release/release-gate.sh:31` (`grep -q "\[$VERSION\]"`) | Use a fixed-string match or escape dots: `grep -qF "[$VERSION]"` (fixed-string `-F` is the clean fit and keeps the line <= 9 budget). |
| 2 | No test asserts `SemanticVersion`/`ReleaseCandidate`/`ReleaseDecision` immutability or thread-safety explicitly; the chapter (and the FeatureFlag Javadoc) lean on thread-safety, but only `FeatureFlag` is behaviorally tested for flip semantics (not concurrency). Correctness is sound by construction (records + `List.copyOf` + `EnumSet.copyOf`); this is coverage polish, not a defect. | MINOR | `ReleaseDecisionTest` (absent); `ReleaseReadinessTest` | Add one test asserting `Blocked(list)` defensively copies (mutate the source list after construction, assert `failures()` unchanged) so the "immutable, never null" comment is enforced by a test. |
| 3 | `ci/release.yml` uses unpinned SaaS actions (`actions/checkout@v4`, `actions/setup-java@v4`) and the release/versions Maven plugins are not separately pinned. | NOTE (already flagged, non-blocking) | `ci/release.yml:36-37`; `09-flags/83_release_versioning_plugin_versions_unpinned.md` | Already correctly flagged + dated-at-use in the file's own comments and the flag file; not invoked by the green build. Pin to tag+digest at adoption. No action for this gate. |
| 4 | `release-gate.sh` and `ci/release.yml` `env` blocks hardcode `CI_GREEN: "true"` / `SIGNED_WITH_SBOM: "true"` / `SMOKE_TESTED: "true"` as literals. These are pedagogical placeholders (commented "set by the ... step") for evidence a real pipeline supplies, not secrets or asserted facts. | NOTE | `ci/release.yml:47-49` | None required — comments already say a real pipeline sets these from its own systems. |
| 5 | Module not yet registered in the aggregator `<modules>` list. | NOTE (by design) | `08-companion-code/pom.xml` | Register only after this CODE-REVIEW passes (agent rule 6) — the intended next step. |

---

## Blockers

**None.** Build GREEN and warning-clean, all six displayed regions <= 9 lines + balanced + banned-word-free,
SemVer claims correct, zero hardcoded secrets, zero neutrality violations in code, zero invented atoms
(unpinned versions correctly flagged). No security/neutrality/invention finding. **FLOOR C is not
blocked.**

- [x] No BLOCKER-severity finding.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity / prose-code-fidelity /
      neutrality-in-code all PASS.
- [x] Build re-run GREEN + warning-clean (`mvn -B -Pquality verify`).
- [x] >= 1 integration test per public behavior incl. the REAL failure path (asserts the exact
      failed-check list; not vacuous).
- [x] Hardcoded-secret grep clean.
- [x] Every displayed tag region <= 9 lines, brace/element-balanced, no banned NEUTRALITY word.
- [x] SemVer claims confirmed correct and honestly scoped.
- [x] LEGAL-IP §5 originality: confirmed original-for-this-book (no verbatim upstream lift; shares only
      the book's own house pattern from peers 62/67/75); no unattributed copy.

---

## Learnings & pipeline suggestions

- **Shell snippets that interpolate a value into a `grep` regex are a recurring correctness trap** (the
  unescaped-dot false-positive here). Recommend `EXAMPLES-GUIDE` add a one-line rule: in displayed shell
  gates, prefer `grep -F` / `grep -qF` for literal matches unless a regex is the point. Cheap, removes a
  whole class of subtle finding.
- **The "tested-policy-core + illustrative-config" shape (peers 62/67/75/83) reviews very cleanly** — the
  sealed `Decision` + `EnumSet` policy + defensive copies make SpotBugs representation-exposure detectors
  pass with an empty (documented) filter, which is itself a teachable artifact. Worth naming as the
  canonical shape for "a gate/policy made runnable" chapters (echoes the EXAMPLE-BUILD learning).
- **Immutability/thread-safety claimed in Javadoc should be pinned by at least one test** when the chapter
  leans on it (finding #2). Recommend a soft guideline: if a comment asserts "immutable, never null" or
  "thread-safe", a test should demonstrate it, so prose-code-fidelity extends to the non-functional claims.

(Append to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log (final step)

```
.claude/scripts/log_action.sh code-reviewer 4b 83 gate-run PASS-WITH-FIXES
```
(Run by the orchestrator after this report lands.)
