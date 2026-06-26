# GATE REPORT — EXAMPLE-BUILD — Chapter 48

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW; CODE-REVIEW pending as a
  separate gate)
- **Chapter key:** 48 (owner; folds 47) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `48_coverage_mutation_effectiveness`
- **Draft under review:** `03-drafts/48_coverage_mutation_effectiveness/48_coverage_mutation_effectiveness_v1.md`
- **Module built:** `08-companion-code/48_coverage_mutation_effectiveness/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `mvn -B -Pquality verify`, `check_snippets.sh`, `extract_snippet.sh`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand)
- **Verdict:** **PASS** (Floor-C build clause PASS; two flags raised to `09-flags/`)

---

## Verdict rationale

The module builds green standalone at the anchor JDK and warning-clean, all four displayed snippets
resolve to ≤9-line tag regions inside the compiled files, and every coverage/mutation atom traces to
SOURCE-PIN.md or to a flagged pin discrepancy. The chapter's thesis (a covered line can be untested)
is demonstrated by the build itself: scoped to the weak test, PITest leaves mutants surviving and
fails the mutation gate; the strong test kills them while line coverage is unchanged. One MAJOR flag
is raised — the pinned JaCoCo 0.8.16 is not published, so the module builds on the real latest
(0.8.15) — and one MINOR flag on the PITest JUnit-5-plugin version matrix.

---

## FLOOR C guard — the two preconditions (both logged, both hold)

- **(a) Runtime meets the minimum (Java 21+).** Version line:
  `openjdk version "21.0.11" 2026-04-21 / OpenJDK Runtime Environment Homebrew (build 21.0.11)`.
  Maven `Apache Maven 3.9.16`, `Java version: 21.0.11`. Minimum is Java 21 (anchor LTS) — **met**.
- **(b) Build finished GREEN.**
  Command: `mvn -B -Pquality -f pom.xml clean verify` (run from
  `08-companion-code/48_coverage_mutation_effectiveness/`).
  Result line: **`[INFO] BUILD SUCCESS`** — `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0`;
  `All coverage checks have been met.`; `You have 0 Checkstyle violations.`; `BugInstance size is 0`.

Both preconditions hold → Floor-C build clause **PASS**.

---

## Enterprise-grade checklist

| Element | How the module satisfies it | Evidence |
|---|---|---|
| Child of the ONE aggregator, no own version/BOM | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no `<groupId>`/`<version>` literal; inherits JUnit (junit-bom 6.0.3) + AssertJ (3.27.7) from the aggregator | `pom.xml` |
| Pinned dependency set | JaCoCo `0.8.15` (pin is 0.8.16 — unpublished, flagged), PITest `1.25.3` (pinned), `pitest-junit5-plugin 1.2.3` (matrix flagged); single property each | `pom.xml` `<properties>` |
| Externalized config / profiles | JaCoCo `check` rule (BRANCH `COVEREDRATIO` ≥ 0.90 + `MISSEDCOUNT` max 0) externalized in the POM; `pitest` profile (opt-in slow stage); `quality` profile (Checkstyle + SpotBugs) reading `config/` | `pom.xml`, `config/` |
| At least one integration/mechanism test + harness setup | 12 tests over the chapter's mechanism; JUnit Jupiter via the auto-detected JUnitPlatformProvider; JaCoCo `prepare-agent` sets `argLine` and the module sets no `<argLine>` (so the agent is not clobbered) — confirmed `argLine set to -javaagent:...jacocoagent...` in the build log before the test run | `DiscountTest`, `DiscountWeakTest`, `MoneyTest` |
| Observability / health surface | `Discount.discountsApplied()` counter; the JaCoCo HTML+XML report (`target/site/jacoco/`) and the PITest report (`target/pit-reports/`) are the two observability surfaces | `Discount.java`, reports |
| Explicit failure path (HONEST-LIMITATIONS in code) | Weak test scoped under PITest leaves boundary/math/returns mutants **SURVIVED** → mutation gate fails (15 mutations, 5 killed = 33%); input-validation guards reject bad arguments | `DiscountWeakTest`, `Discount.validate`, `Money` ctor |

---

## The thesis, demonstrated by the build (the chapter's payoff)

| Run | Line coverage (mutated classes) | Mutation score | Gate | Build |
|---|---|---|---|---|
| Weak test only (`-DtargetTests=...DiscountWeakTest`) | covered, lines execute | 15 mutations, 5 killed = **33%** (test strength 38%) | `mutationThreshold` 80% **not met** | **FAILURE** (the failure path) |
| Full suite (weak + strong) | 17/17 = **100%** | 15 mutations, 13 killed = **87%** (test strength 87%) | met | **SUCCESS** |

The invariance the chapter rests on holds: adding the strong assertions raised the mutation score
from 33% to 87% while the covered lines were identical. JaCoCo per-class branch coverage:
`Discount` 8/8 branches, `Money` 4/4 branches (BRANCH_MISSED = 0 for both) — so the BRANCH gate
passes green with the full suite, and the prose's "read BRANCH, not only LINE" is the gate the module
actually enforces.

---

## Snippet (tag-include) verification

`check_snippets.sh 03-drafts/48_coverage_mutation_effectiveness/48_coverage_mutation_effectiveness_v1.md`
→ **4 marker(s); 4 pass, 0 fail.**

| Tag | File | Resolved lines (≤9) | Marker location in draft |
|---|---|---|---|
| `under-test` | `src/main/java/org/acme/effectiveness/Discount.java` | 6 | Deep dive — "The method under both tools is the boundary, the arithmetic, and the early return:" |
| `weak-test` | `src/test/java/org/acme/effectiveness/DiscountWeakTest.java` | 6 | Deep dive — "The weak test runs every one of those lines yet asserts only that the result is non-null…" |
| `strong-test` | `src/test/java/org/acme/effectiveness/DiscountTest.java` | 4 | Deep dive — "The strong test executes the identical lines but pins both sides of the boundary…" |
| `jacoco-check` | `pom.xml` | 5 | How it works / the gate — "The companion module gates on BRANCH so an untested decision path fails the build even at high line coverage:" |

Each marker has a one-line lead-in ending in a colon; no prose was deleted; the "Snippet tags:" line
was added to the draft's companion-module spec, and that spec line updated to BUILT-GREEN.

---

## Source trace (every atom → pin or flag)

- **JaCoCo goals/counters/check-rule model** — `prepare-agent` / `report` / `check`; the six counters;
  `element`/`counter`/`value`/`minimum`/`maximum`/`haltOnFailure` → JaCoCo docs (counters.html,
  maven.html, check-mojo.html), verified-verbatim in `02-research/48_code_coverage_jacoco/`.
- **JaCoCo version** — SOURCE-PIN.md §3 pins **0.8.16**; built on **0.8.15** (the real latest;
  0.8.16 is a 404 on Central). Flagged → `09-flags/48_jacoco_pin_0816_unpublished.md`.
- **PITest GAV + goal + config + mutators + statuses** — `org.pitest:pitest-maven` goal
  `mutationCoverage`; `targetClasses`/`targetTests`/`mutationThreshold`; CONDITIONALS_BOUNDARY / MATH /
  NEGATE_CONDITIONALS / returns mutators; KILLED/SURVIVED statuses → pitest.org, verified in
  `02-research/47_mutation_testing_pitest/`. Version **1.25.3** = SOURCE-PIN.md §3 (resolves).
- **pitest-junit5-plugin** — required for Jupiter; version **1.2.3** (not separately pinned). Flagged
  → `09-flags/48_pitest_junit5_plugin_matrix_verify_at_pin.md`.
- **JUnit (junit-bom 6.0.3) / AssertJ 3.27.7** — inherited from the aggregator (SOURCE-PIN.md §3).
- **Runtime** — Java 21.0.11 (anchor LTS), SOURCE-PIN.md runtime baseline.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file. Every file is original work written for this book: the `Discount`/`Money`
domain, the weak/strong/Money test classes, `package-info.java`, the README, and the Checkstyle/
SpotBugs config (which follow the book's own peer-module house-ruleset shape, not an upstream sample).
No JaCoCo or PITest quickstart skeleton, sample file, or NOTICE/header boilerplate was copied. The
`jacoco-check` XML uses the documented `<limit>` config schema (element/counter/value — a
configuration shape, not copyrightable prose) with original values and comments. No region is taken
substantially verbatim from a specific upstream source file, so no per-file attribution is required.

---

## Captured screenshots (Step 4c)

The chapter's figure plan (research §6) is **1–2 designed diagrams + 1 captured screenshot** — the
captured surface is the JaCoCo/PITest HTML report. The designed diagrams (Fig 48.1) are authored
separately as HTML→PNG and are not this gate's job. The single planned **capture** (the JaCoCo HTML
source view + the PITest report) is **deferred**: figure capture is a separate sub-step and the figure
plan for this chapter was authored at draft time; the running surfaces exist and are reproducible
(`target/site/jacoco/index.html` after `verify`; `target/pit-reports/` after `-Ppitest`). Recorded
here so the figure step can capture them from the green module. No new figures were invented.

> Capture status: **planned (1 screenshot), not yet captured** — the report surfaces are live and
> reproducible from the built module; capture + sidecar to be produced in the figure sub-step.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | SOURCE-PIN.md pins JaCoCo 0.8.16, which is unpublished (404 on Central; real latest 0.8.15) | MAJOR | `SOURCE-PIN.md` §3; `pom.xml` `<jacoco.version>` | Built on 0.8.15 (nearest real, covers JDK 21/25); re-pin SOURCE-PIN.md to 0.8.15 per runbook. Flagged. |
| 2 | `pitest-junit5-plugin` version not separately pinned; the plugin↔Platform↔PITest matrix moves | MINOR | `pom.xml` `<pitest.junit5.version>` | Used 1.2.3 (current latest); pin explicitly at `/pin-source`. Opt-in profile only — does not affect the default green build. Flagged. |
| 3 | Removed an unused `Money.times` method (dead code; left 2 branches permanently uncovered) | NOTE | `Money.java` | Removed so the BRANCH gate stays meaningful and green; not load-bearing for the chapter. |

## Blockers

**None.** (Finding 1 is a pin discrepancy resolved by building on the real latest version and
flagging for re-pin; it does not block the green build.)

---

## Floor-C verdict

**PASS** — runtime ≥ Java 21 (21.0.11) and `mvn -B -Pquality verify` GREEN (both guard preconditions
logged above); zero invented atoms (every coverage/mutation atom traces to the pin or to a raised
flag); all four snippets resolve to ≤9-line tag regions; LEGAL-IP §5 original-for-this-book confirmed.
CODE-REVIEW is a separate gate (run next); the module must be added to the parent `<modules>` list
ONLY after green build **and** CODE-REVIEW pass — it is intentionally **not** registered yet.

---

## Learnings & pipeline suggestions

- **A pinned version can be ahead of the authority's real release channel.** SOURCE-PIN.md pinned
  JaCoCo 0.8.16, but only 0.8.15 is published. The honest path at build time is to verify the pin
  against the real channel, build on the nearest real version, and flag the discrepancy for a
  deliberate re-pin — never invent the unpublished artifact and never silently substitute. Recommend a
  cheap `/pin-source` post-check that resolves each pinned GAV against Central metadata so a phantom
  version is caught before a chapter prints it.
- **"Failure path" for a metric chapter = scope the gate to the weak test.** The cleanest way to make
  "a covered line can be untested" tactile is one method + a weak and a strong test in one green
  module, with the failure demonstrated by scoping the mutation run (`-DtargetTests=...WeakTest`) so
  the full build stays green while the thesis is reproducible on demand. Reuse for any
  necessary-not-sufficient metric pairing (coverage 48, MI key 04).
- **Keep the demonstrated gate honest: remove dead code rather than relax the threshold.** An unused
  helper left permanent uncovered branches; deleting it let the BRANCH gate sit at a strict
  `MISSEDCOUNT max 0` instead of being loosened to accommodate code nothing calls.
- **pitest-junit5-plugin is a real setup-trap atom.** The plugin is required for Jupiter and is easy to
  forget; the build proved it works (7 tests examined, 100% line coverage for mutated classes — not the
  silent no-coverage failure). The version belongs in SOURCE-PIN.md alongside PITest, not left implicit.
