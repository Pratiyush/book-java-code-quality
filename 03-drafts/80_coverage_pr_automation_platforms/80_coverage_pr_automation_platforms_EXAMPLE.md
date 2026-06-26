# GATE REPORT — EXAMPLE-BUILD — Chapter 34 (key 80)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — Floor C (SOURCE-TRACE / COMPILE / CODE-REVIEW; this report covers
  SOURCE-TRACE + COMPILE; CODE-REVIEW is a separate gate)
- **Chapter key:** 80 (folds 77, 78) — FINAL_INDEX Ch 34, Part IX
- **Slug:** `80_coverage_pr_automation_platforms`
- **Draft under review:** `03-drafts/80_coverage_pr_automation_platforms/80_coverage_pr_automation_platforms_v1.md`
- **Module built:** `08-companion-code/80_coverage_pr_automation_platforms/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `mvn -B -Pquality verify`, `extract_snippet.sh`, `check_snippets.sh`
- **Scripts run:** `extract_snippet.sh` (×6), `check_snippets.sh`
- **Build state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand)
- **Verdict:** **PASS**

---

## Verdict rationale

The companion module builds green via `mvn -B -Pquality verify` on Java 21.0.11 (both Floor-C guard
preconditions met), all six displayed snippets resolve to bounded tag regions (≤ 9 lines) in compiling
files, and every version/identifier traces to `SOURCE-PIN.md` or is flagged dated-at-use. The two
deviations (JaCoCo 0.8.15 vs the unpublished 0.8.16 pin; the SaaS PR platforms/actions) are recorded in
`09-flags/`, not invented.

---

## FLOOR C guard preconditions (both required for a PASS)

- **(a) Runtime meets the minimum (Java 21+):** `openjdk version "21.0.11" 2026-04-21` — meets the
  `SOURCE-PIN.md` anchor (Java 21 LTS). Maven 3.9.16.
- **(b) Build is GREEN:** `mvn -B -Pquality verify` → **BUILD SUCCESS**.

Exact command (run from the repo root):
```
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
mvn -B -Pquality -f 08-companion-code/80_coverage_pr_automation_platforms/pom.xml clean verify
```
Result line: **`BUILD SUCCESS`** — `Tests run: 32, Failures: 0, Errors: 0, Skipped: 0`; JaCoCo 0.8.15
branch ratio **1.00** against a 0.90 floor; **0** Checkstyle violations; SpotBugs **BugInstance size is
0**; warning-clean (no `[WARNING]` lines).

---

## Tag-includes (displayed snippets) — all resolve, all ≤ 9 lines

| # | Tag spec | Lines | Realizes |
|---|---|---|---|
| 1 | `CoverageGate.java#new-code-gate` | 7 | the diff-scoped new-code bar + ratchet decision (runnable) |
| 2 | `pom.xml#jacoco-pr-report` | 6 | the JaCoCo `report` goal → `jacoco.xml` (the report a platform uploads) |
| 3 | `ci/coverage-pr.yml#coverage-upload-step` | 5 | the platform upload/decoration step (Codecov form, dated-at-use) |
| 4 | `ci/coverage-pr.yml#danger-tests-touched` | 6 | the Danger PR-hygiene rule (PR must touch tests) |
| 5 | `.codecov.yml#codecov-patch-threshold` | 4 | the patch (diff-coverage / new-code) threshold |
| 6 | `.codecov.yml#codecov-bot-comment` | 4 | the diff-scoped bot-comment policy |

`check_snippets.sh 03-drafts/.../80..._v1.md` → **6 marker(s); 6 pass, 0 fail.** Markers inserted into the
draft with one-line lead-ins only (no prose deleted, locked neutral voice), plus a "Snippet tags:" line
before the next-chapter teaser. Tag design: 4 config tags (the chapter is CONFIG-centric) + 2 inside the
build (one Java, one pom) — within the requested 4–7 range.

---

## Enterprise-grade checklist

- **Child of the ONE aggregator, no own version literal / BOM:** PASS — `pom.xml` sets `<parent>` to
  `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`, declares no `<groupId>`/`<version>`, imports no
  BOM (JUnit/AssertJ inherited from the parent's `dependencyManagement`). The one literal,
  `<jacoco.version>0.8.15</jacoco.version>`, is a tool the aggregator does not manage, traced + flagged.
- **Registered in parent `<modules>` only after green + CODE-REVIEW:** NOT registered — parent
  `08-companion-code/pom.xml` is untouched (constraint honoured). Registration is deferred to after the
  CODE-REVIEW gate.
- **Pinned dependency set:** PASS — JaCoCo 0.8.15 (deviation from unpublished 0.8.16 pin, flagged);
  Checkstyle engine 10.26.1 + maven-checkstyle-plugin 3.6.0; spotbugs-maven-plugin 4.9.3.0; JUnit/AssertJ
  via the inherited junit-bom (6.0.3) + AssertJ 3.27.7. SELF-CONTAINED: own `config/` + own `quality`
  profile, matching peers 27/75/48.
- **Externalized config profiles:** PASS — `coverage-dev.properties` / `coverage-prod.properties` loaded
  by `CoverageProfiles` via the `coverage.profile` system property (no hard-coded thresholds).
- **Integration test + harness setup:** PASS — `CoverageProfilesIntegrationTest` loads a profile from the
  classpath and runs the whole gate end to end (properties → policy → verdict). Harness: JUnit Jupiter via
  the aggregator's `junit-bom`; JaCoCo binds the agent through `argLine` and the module sets no `<argLine>`
  in Surefire (which would clobber the agent and silently report 0% coverage) — confirmed by the
  non-zero, 1.00 branch ratio in the report.
- **Observability / health surface:** PASS — `CoverageGate.blockedCount()` (a trendable block-rate
  metric) and `CoverageGate.isReady()` (a readiness probe that reports not-ready for a fail-open policy).
- **Explicit failure path:** PASS — `CoverageGate.evaluate` returns a sealed `CoverageVerdict`
  (Pass/Warn/Block), a `Block` carries the failing rule, and every record rejects malformed input at
  construction (ratio outside `[0,1]`, blank file/message, non-positive line, null map/set).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | JaCoCo `SOURCE-PIN.md` §3 pins 0.8.16, which is unpublished (Maven Central 404; local cache holds only a `.lastUpdated` 404 stub). Built at 0.8.15, the published latest. | MAJOR (pin) | `pom.xml` `<jacoco.version>`; `09-flags/48_jacoco_pin_0816_unpublished.md` + `09-flags/80_coverage_pr_platforms_saas_dated_at_use.md` | Re-pin §3 JaCoCo 0.8.16 → 0.8.15 per the re-pin runbook (single literal to change). |
| 2 | PR coverage platforms (Codecov/Coveralls/Sonar PR analysis), the GitHub Actions, and Danger have no SOURCE-PIN version row. | MINOR (dated-at-use) | `ci/coverage-pr.yml`, `.codecov.yml`; `09-flags/80_coverage_pr_platforms_saas_dated_at_use.md` | Presented as dated-at-use 2026-06; pin marketplace actions to commit digests at adoption / public-push sign-off. |
| 3 | The displayed CI/`.codecov.yml` snippets are illustrative configuration, not run by the Maven build. | NOTE | `ci/coverage-pr.yml`, `.codecov.yml` | Stated in the file headers, the README, and the draft lead-ins; the runnable core is the Java gate + JaCoCo, exercised by `verify`. |

---

## Blockers

**None.** Both Floor-C guard preconditions hold; the two pin items are recorded deviations/dated-at-use
flags, not invented facts.

---

## Gate-specific checks (EXAMPLE)

- [x] Companion artifact builds green via `mvn -B -Pquality verify` at the pin (JaCoCo 0.8.15 deviation
  flagged) on Java 21.0.11.
- [x] Every displayed snippet resolves to a real bounded tag region (≤ 9 lines) in a compiling file
  (`check_snippets.sh`: 6/6 pass).
- [x] FLOOR C source-trace clean: every version/identifier traces to `SOURCE-PIN.md` or is flagged
  (`09-flags/80_...`, `09-flags/48_jacoco_...`).
- [x] LEGAL-IP §5 original-for-this-book: confirmed file-by-file (see below).
- [x] Neutrality-in-code: comments/README give each option its case + limitation, crown none; no banned
  phrasings (`better than` / `unlike X` / `superior` / `beats` / `the problem with X`).

---

## CAPTURE (Step 4c) — subject-native UI screenshots

**No captures planned.** The chapter's figure plan (draft §"How it works") fixes exactly one figure,
`fig80_1.png` — a *designed conceptual diagram* ("Diff-scoping: one discipline across coverage, the
platform check, and PR feedback"), authored as HTML and rendered to PNG separately (it already exists at
`05-figures/80_coverage_pr_automation_platforms/fig80_1.{html,png,sources.md}`). It is not a captured
screenshot. The module has no live subject-native UI surface to capture (the runnable surface is a JDK
gate-policy library; the platform consoles named are SaaS, not run here), consistent with the peer
config-centric modules 27/75/48. No new figure was invented.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: all 22 module files are original work written for this book.

- The 9 Java files (`org.acme.coverage`) are an original domain (coverage delta / sealed verdict /
  externalized policy / diff-scoped gate / bot-comment policy) authored for this chapter — not a
  lightly-edited upstream sample.
- `config/checkstyle/checkstyle.xml` and `config/spotbugs/spotbugs-exclude.xml` follow the book's own
  curated house ruleset (the shared shape across companion modules), not an upstream sample config.
- `ci/coverage-pr.yml` and `.codecov.yml` use the tools' **documented config schemas** (a grammar, not
  copyrightable sample prose) filled with the chapter's own values and comments; no upstream quickstart
  workflow, sample `.codecov.yml`, or NOTICE/header boilerplate was copied.
- No whole file, large contiguous block, or getting-started skeleton was copied from any source or its
  samples. No region is taken substantially verbatim from a specific source file, so no per-file
  attribution is required.

---

## Source path each fact traces to

- JaCoCo plugin + goals (`prepare-agent`/`report`/`check`), counters, check-rule model →
  `SOURCE-PIN.md` §3 (JaCoCo row; built 0.8.15 per the flag); jacoco.org check-mojo / counters docs.
- JUnit Jupiter / AssertJ → `SOURCE-PIN.md` §3 (junit-bom 6.0.3 inherited; AssertJ 3.27.7).
- Checkstyle / SpotBugs engines + plugins → `SOURCE-PIN.md` §2 (matching the peer key-33/key-23 modules).
- GitHub Actions, Codecov/Coveralls/Sonar PR analysis, Danger → `SOURCE-PIN.md` §5 (GitHub Actions dated
  2026-06; the platforms/Danger have no version row) → dated-at-use, `09-flags/80_...`.
- Java 21 anchor runtime → `SOURCE-PIN.md` runtime baseline.
- Chapter claims realized (new-code focus, ratchet, diff-scoping, bot/human division, crown-none) → the
  draft's banked dossier (keys 80/77/78), embedded in the v1 front-matter.

---

## Learnings & pipeline suggestions

1. **The JaCoCo 0.8.16 unpublished-pin deviation is now a recurring, cross-chapter fact** (hit by key 23
   and again here). It should be promoted out of per-build flags into an actual `SOURCE-PIN.md` re-pin
   (0.8.16 → 0.8.15) so each coverage chapter stops re-discovering it. Recommend running the re-pin
   runbook at the next `/pin-source`.
2. **A new-code/diff coverage gate is awkward to make runnable** because true new-vs-old line attribution
   needs a git diff, which a hermetic unit build does not have. Modelling the diff as an injected
   `CoverageDelta` + `ChangedLines` value (the platform's output) keeps the *decision* runnable and
   unit-tested without the build needing VCS history — a clean pattern reusable by any future
   diff-scoped-gate chapter; worth noting in `EXAMPLES-GUIDE`.
3. **JaCoCo's branch floor catches under-tested defensive guards.** The first build failed at 0.84 branch
   coverage purely on fail-fast constructor guards; a dedicated `ValueObjectsTest` exercising both sides
   of every range/blank/null guard is the right fix (it tests the advertised failure path), and is
   preferable to lowering the floor. Worth a note that config-centric modules with rich value objects
   should plan a value-guard test from the start.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 80 gate-run PASS
```
