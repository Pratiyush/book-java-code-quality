# CODE-REVIEW — Chapter 07 (naming, structure & formatting) — companion module

- **Module:** `08-companion-code/07_naming_structure_formatting/`
- **Chapter draft:** `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
- **Gate:** FLOOR-C, second half (CODE-REVIEW). EXAMPLE-BUILD already PASS (built green).
- **Reviewer:** code-reviewer agent (senior-PR lens; readers copy this verbatim)
- **Date:** 2026-06-27
- **Toolchain used:** Homebrew `openjdk@21` (21.0.11) via `JAVA_HOME`; system `mvn` (no `mvnw` wrapper at the aggregator — see note).

## Verdict: PASS-WITH-FIXES

The runnable code is exemplary and idiomatic for the Java 21 anchor: immutable records with compact-constructor validation, a `static final` constant that is *genuinely* deeply immutable (the chapter's load-bearing distinction), `System.Logger` instead of stdout, a real fail-fast failure path with a non-vacuous failure-path test, and five tight `// tag::` regions that all extract and all sit within the 9-line ceiling. No correctness, security, neutrality, or invention defect was found. The fixes are all **draft back-matter labels** plus **one inaccurate config-file comment** — prose↔code fidelity, not code defects. None blocks FLOOR-C.

## Build & lint result (re-run, not trusted from record)

`mvn -B -Pquality -f pom.xml clean verify` (JDK 21.0.11):

- **BUILD SUCCESS.** Tests run: 6, Failures: 0, Errors: 0, Skipped: 0.
- **Checkstyle:** 0 violations (engine 10.26.1 via the two-pin plugin override; `includeTestSourceDirectory=true`).
- **SpotBugs:** BugInstance size 0, Error size 0 (effort Max, threshold Medium).
- **Warning-clean:** clean `compile`/`test-compile` under the parent's `-Xlint:all,-processing` produced **no** javac warnings.
- **Failure path is visibly exercised:** the run prints two `System.Logger` WARNING lines (blank SKU; quantity 1000 over the 999 ceiling) from the rejected-line tests.
- **Secret scan:** no `password|secret|token|apikey|private_key|aws_|BEGIN PRIVATE` literals in `src`/`config`/`pom.xml`/`.editorconfig`.
- **Neutrality scan (code+config+README):** none of the banned phrasings (`better than` / `unlike X` / `superior` / `beats` / `the problem with` / `inferior` …) present.

## Six dimensions

| # | Dimension | Result | Notes |
|---|---|---|---|
| 1 | Correctness | **PASS** | Compact constructors validate all components; `Money.times`/`OrderLine` reject negatives/blank/out-of-range; no resource leaks; the one `catch` logs-and-rethrows (does not swallow). Tests assert real values and real throws — non-vacuous. |
| 2 | Idiomatic Java 21 | **PASS** | `record` value types, compact constructors, `Objects.requireNonNull` with field-name messages, `System.Logger` (no stdout/`printStackTrace`), `final` class + private ctor for the utility holder. Zero runtime deps — dogfoods the book. |
| 3 | Security | **PASS** | No secrets; public entry points validate input and fail fast; no injection sink; exceptions carry domain messages only (no internals/stack traces leaked). |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point; realistic `storefront` domain names (no `Foo`/`tmp`); every public type carries a purpose Javadoc; no dead code, no unused deps. |
| 5 | Prose↔code fidelity & originality | **FIX** | All five displayed `tag::` regions exist, extract, and are <=9 lines; the back-matter snippet-tag list matches exactly. Code is original (not an upstream sample/quickstart). Three label mismatches + one inaccurate config comment — see findings F1–F4. |
| 6 | Neutrality in code | **PASS** | No comment/identifier/log string crowns or disparages a comparator. The Checkstyle and Spotless comments explicitly state style values are *a choice, not a truth* (100 vs 120 vs AOSP). |

## Findings by severity

| Sev | Location | Issue | Fix (for example-builder / reconciler) |
|---|---|---|---|
| **Minor** | `config/spotbugs/spotbugs-exclude.xml:8` | Comment claims "the one type that **stores a collection** copies it in and copies it out (Item 50)" — but **no type in this module holds a collection or array** (`Money`: `long`,`String`; `OrderLine`: `String`,`int`,`Money`). The described defensive-copy scenario does not exist; a reader copying this file inherits a comment about code that isn't there. Item 50 citation itself is correct ("Make defensive copies when needed"). | Drop the collection/Item-50 clause; the true reason no suppression is needed is simply "the domain types are immutable records/values, so EI_EXPOSE_REP/REP2 stay quiet." (Code-only fix; not a draft edit.) |
| **Minor** | draft `…_v1.md:234` (back-matter) | "a **repo-root** `.editorconfig`" — the file is at the **module** root (`08-companion-code/07_naming_structure_formatting/.editorconfig`); there is no repo-root one, and `root = true` is module-scoped on purpose. Body prose (lines 120–122) correctly says "module root", so the back-matter contradicts the body. | Change "repo-root" → "module-root" in the back-matter spec line. (Draft fix; code/location is correct and idiomatic.) |
| **Minor** | draft `…_v1.md:234` (back-matter) | "a **JUnit 5 (Jupiter)** test" — the parent aggregator pins `junit-bom 6.0.3` (the JUnit 6 line). The test correctly uses the Jupiter API (right for JUnit 6), but the version label is stale. | Change "JUnit 5 (Jupiter)" → "JUnit (Jupiter)" or "JUnit 6 (Jupiter)" to match the pinned BOM. (Draft/reconcile fix; code is correct.) |
| **Nit** | `src/main/java/.../OrderLine.java:47` | The out-of-range `IllegalArgumentException` message line is **exactly 120 chars** — at the Checkstyle `LineLength` ceiling. Legal and idiomatic, but a hair cramped for a teaching deliverable. | Optional: split the message string across two lines for breathing room. No action required. |
| **Nit** | aggregator `08-companion-code/` | No `mvnw` wrapper at the aggregator root; the build relies on a system `mvn` + a `JAVA_HOME` pointing at JDK 21. The agent brief and README both reference `./mvnw` / `mvn`; build reproduced cleanly with system `mvn`. | Out of scope for this chapter; note for the maintainer if wrapper-pinning is desired repo-wide. No action required for FLOOR-C. |

## What's exemplary (call-outs worth keeping)

- **The `CONSTANT_CASE` teaching is enforced by construction, not just asserted.** `OrderLine.MAX_QUANTITY_PER_LINE` is an `int` (deeply immutable) — a genuine constant per Google Java Style §5.2.4 — and the test `acceptsQuantityAtTheConstantCeiling` ties the constant to behavior. This is the chapter's hardest distinction, demonstrated rather than described.
- **The `naming-bad` before-state is carried inside a comment so the naming gate stays green** — the failure *is* the lesson, and `ReadabilityNotes`'s own Javadoc explains why it's text, not code. Elegant resolution of "show the bad code without failing the build."
- **Failure-path test is real and non-vacuous:** `failsFastOnBlankSku`, `failsFastOnQuantityAboveTheCeiling`, `failsFastOnNullUnitPrice` call the production `validatedLine`, assert the exception *type* and a message substring, and the log proves the path executed.
- **The reference-config decision is honest and traceable:** Spotless is shown as `config/spotless/spotless-reference.xml` (not wired live) because the pinned "Spotless 3.6.0" is the Gradle-plugin line, not a Maven coordinate — documented in `09-flags/34_spotless_maven_plugin_version_unresolved.md` (present, updated 2026-06-27), in the POM comment, the reference-config header, and the README. No invented version literal. This is exactly the right call for a "never invent a version" book.
- **Empty SpotBugs exclude filter kept with a reason** — models the chapter-16 discipline "suppress with a reason, never disable a detector" by having *zero* suppressions and saying why.
- **Neutral by construction in code comments:** both the Checkstyle ruleset header and the Spotless reference state the 120-column choice is "this team's cited choice, not a universal truth," naming Google/AOSP/palantir without crowning any.

## FLOOR-C disposition

**FLOOR-C: PASS-WITH-FIXES — does NOT block.** Build green + warning-clean, failure-path test real, no security/neutrality/invention finding; the four open items are three draft-label corrections and one inaccurate config comment, all safe to apply without re-architecting. Apply F1–F3, then no re-review of code logic is required (F1 is a comment-only edit).

## Learnings & pipeline suggestions

1. **Config-file comments need the same fidelity bar as code.** F1 (a spotbugs-exclude comment describing a collection/defensive-copy that the module doesn't contain) is the kind of "plausible boilerplate that doesn't match this module" that compiles and lints clean but misleads a copying reader. Suggest the example-builder checklist add: "every comment that asserts a code fact (a field type, a defensive copy, a suppressed detector) must be true *of this module*."
2. **Back-matter spec line drifts from the body and from the parent POM.** Two of three FIX items (`repo-root`, `JUnit 5`) are the chapter's *own* back-matter contradicting its body / the pinned BOM. Suggest the reconciler cross-check the "Companion module (BUILT GREEN …)" spec sentence against (a) the module file tree and (b) the aggregator's pinned dependency versions, since that sentence is hand-written and easy to leave stale when the parent pin moves (JUnit 5 → 6).
3. **Toolchain discoverability.** No `mvnw` at the aggregator and no JDK on the default PATH meant the reviewer had to locate `openjdk@21` by hand. A committed `mvnw` (or a one-line "build with `JAVA_HOME=/opt/homebrew/opt/openjdk@21`" in `08-companion-code/README`) would make the gate reproducible without discovery. Low priority, repo-wide.
