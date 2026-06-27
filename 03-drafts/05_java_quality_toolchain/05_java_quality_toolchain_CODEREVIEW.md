# CODE-REVIEW — Chapter 05 (A Map of the Territory) · `java-quality-toolchain`

**Gate:** FLOOR-C, second half (CODE-REVIEW) · companion module review
**Module:** `08-companion-code/05_java_quality_toolchain/`
**Chapter draft:** `03-drafts/05_java_quality_toolchain/05_java_quality_toolchain_v1.md`
**Reviewer:** code-reviewer agent · **Date:** 2026-06-27
**Toolchain used:** JDK 21.0.11 (Homebrew openjdk@21) · Maven 3.9.x · `mvn -B -Pquality verify`

---

## VERDICT: PASS

A small, exemplary, fully-original module whose load-bearing artifact is the `pom.xml` it teaches.
Build is green and **warning-clean**; every displayed `// tag::` region is exemplary, within budget,
and faithful to the prose. No correctness, security, neutrality, originality, or invention findings.
Two NITS only, both optional polish, neither blocking. Cleared for FLOOR C.

---

## Build / lint validation (re-run live this review)

| Check | Result |
|---|---|
| `mvn -B -Pquality -pl 05_java_quality_toolchain -am clean verify` | **BUILD SUCCESS** |
| Tests | 4 run · 0 failures · 0 errors · 0 skipped |
| Checkstyle (source, engine 10.26.1) | **0 violations** |
| SpotBugs (bytecode, plugin 4.9.3.0, effort=Max/threshold=Medium) | **BugInstance size 0 · Error size 0** |
| JaCoCo (0.8.15) | report written → `target/site/jacoco/index.html` (2 classes analyzed) |
| Warning-clean | **Yes** — `-X` debug shows effective javac line `--release 21 ... -Xlint:all,-processing -Werror`; no WARNING/deprecation/unchecked lines emitted, and `-Werror` would have failed the build if any existed |
| Default (fast) build under `-Werror` | green |

**Effective `compilerArgs` (authoritative, from `mvn -X`):** `[-Xlint:all,-processing, -Werror]` — the
module's `<build><plugins>` config cleanly **overrides** the parent `<pluginManagement>` default for
this element; there is **no** duplicate `-Xlint` arg from a list-merge. `-Werror` is genuinely in
effect, so the chapter's central teaching ("the compiler held to every warning, made fatal") is real,
not decorative.

---

## Six review dimensions

| # | Dimension | Score | Note |
|---|---|---|---|
| 1 | Correctness | **PASS** | Integer-cent money (no float), defensive copy via `List.copyOf`, fail-fast validation in the compact constructor, no leaks, no swallowed exceptions. Tests assert real behavior incl. the failure path. |
| 2 | Idiomatic Java 21 | **PASS** | `record` with compact-constructor validation; `List.copyOf` for an unmodifiable defensive snapshot; `final class`; `mapToLong` stream sum; `var` at call sites in tests. No anti-patterns (no raw threads, no `System.out`, no `printStackTrace`). |
| 3 | Security | **PASS** | No hardcoded secrets/tokens/keys (grep clean across `src/`, `config/`, `pom.xml`). Public constructors validate input; exceptions carry precise, non-leaky messages (echo only the offending numeric value). No injection sink, no network/IO surface. |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point. Every public type/method carries a one-line purpose Javadoc; realistic names (`Cart`, `LineItem`, `subtotalCents`, `totalCents`, `isReady`) — no `Foo`/`tmp`. No dead code; `org.acme.toolchain` is the house example package, not a placeholder. |
| 5 | Prose↔code fidelity + originality | **PASS** | All 5 prose `include:` directives resolve to existing tags; all 5 tag regions ≤9 lines and say what the prose says. Versions (engine 10.26.1, SpotBugs plugin 4.9.3.0, JaCoCo 0.8.15, google-java-format 1.35.0), GAVs, FindBugs→SpotBugs, and the two-pin split all trace to the pin/flags. Code is original-for-this-book (the book's own storefront-cart domain), not an upstream sample. |
| 6 | Neutrality in code | **PASS** | No banned comparative phrasing (`better than` / `superior` / `unlike X` / `the problem with X` / `beats`) anywhere in code, config, comments, or README. Comparative language in comments ("complementary", "different vantage points") is neutral. |

---

## Findings by severity

### BLOCKER — none
### HIGH — none
### MEDIUM — none

### LOW / NIT (optional polish — non-blocking)

| Sev | File:line | Issue | Suggested fix |
|---|---|---|---|
| NIT | `pom.xml:96–101` (`compiler-flags` tag) | The module re-states `-Xlint:all,-processing`, which the aggregator already supplies via `<pluginManagement>` (`08-companion-code/pom.xml:108–110`). Effective args confirm an *override*, not a duplicate — so the build is correct — but a reader copying this region in isolation may not realize the lint arg is inherited and that listing it here is what makes `-Werror` land alongside it. The intent ("`-Werror` added here so the chapter can show the failure-promoting flag as a self-contained region") is sound; this is purely a teaching-clarity note. | Optional: a one-line comment that the `-Xlint` line restates the inherited default so the override stays complete, OR rely on the existing pom prose comment (lines 88–90) which already explains it. No code change required. |
| NIT | `config/spotbugs/spotbugs-exclude.xml` (whole file) | The exclude filter is intentionally empty (an explanatory-comment-only `<FindBugsFilter>`). This is the *right* call and is well justified in the comment, but an empty filter file referenced by `excludeFilterFile` is a pattern some readers may misread as "required boilerplate." | Optional: none needed — the comment already explains why the empty file is kept as a home for a future reviewed false positive. Leave as is. |

---

## What is exemplary (call-outs for the example-builder to preserve)

- **The failure path is a real failure path, tested three ways.** `LineItem`'s compact constructor
  rejects blank SKU, negative `unitCents`, and non-positive `quantity` with distinct messages; the test
  asserts each via `assertThatThrownBy(...).hasMessageContaining(...)` rather than a vacuous "throws
  something." (`LineItem.java:24–34`, `CartTest.java:40–52`.)
- **Representation-exposure handled on the code's own merits, not by suppression.** `Cart` takes a
  `List.copyOf` snapshot and the test proves it (`copiesLinesDefensively`, `CartTest.java:54–65`), which
  is *why* the SpotBugs exclude filter can be legitimately empty. The module practices what the chapter
  preaches (Ch 16 representation-exposure), making the green SpotBugs run meaningful, not configured-away.
- **Money as integer cents, never `double`** — exact arithmetic and nothing for SpotBugs' FP detectors
  to flag; the comment ties it back to the chapter's reasoning (`LineItem.java:9–11`).
- **`isReady()` is a genuine observability/readiness surface**, not a contrived getter: it distinguishes
  "wired but empty" from "ready," and the comment explains *why* it is not just `totalCents() == 0`
  (`Cart.java:50–59`). Good teaching of intent over coincidence.
- **The pom is the deliverable and it is honestly annotated.** Every version split vs SOURCE-PIN is
  explained inline and cross-linked to its `09-flags/` entry (all three referenced flag files exist);
  the two-pin Checkstyle engine override and the profile split (default `-Werror` vs opt-in `-Pquality`)
  match the proven-green peer modules (27/62/75). The reference-only Spotless config is correctly
  *isolated* from the live build — `${spotless.maven.plugin.version}` appears only in the reference XML,
  so the green build never depends on an undefined property.

---

## FLOOR-C disposition

**FLOOR C CODE-REVIEW: PASS** — module is correct, idiomatic, secure, original, neutral, prose-faithful,
green and warning-clean; the two NITS are optional and do not block. Combined with the EXAMPLE-BUILD
(COMPILE) PASS, FLOOR C is satisfied for Chapter 05.

---

## Learnings & pipeline suggestions

1. **No Maven wrapper (`mvnw`) exists in `08-companion-code/`**, and the review shell had `JAVA_HOME`
   unset with no `java` on PATH; the build only ran after pointing `JAVA_HOME` at the Homebrew
   `openjdk@21`. Both the agent prompt and several module docs/specs reference `./mvnw -B verify`, which
   does not exist here (the repo uses system `mvn`). Suggest either committing a Maven wrapper to the
   aggregator or updating the example-build/code-review runbooks to say `mvn` + an explicit JDK-21
   `JAVA_HOME` step, so the gate is reproducible without local guesswork.
2. **The `compilerArgs` override-vs-merge question is a recurring trap** for modules that re-declare the
   compiler plugin under a `pluginManagement` parent. Here it resolves correctly to an override (verified
   via `mvn -X`), but a `combine.children` change upstream could silently start *appending* and duplicate
   `-Xlint`. Worth a one-line note in `EXAMPLES-GUIDE` / the peer-module convention: when restating
   `compilerArgs` in a child, confirm the effective vector with `mvn -X` rather than assuming override.
3. **Pattern to promote:** "empty-but-kept exclude filter + a domain written so the analyzer is quiet on
   its own merits" is a clean, teachable way to show a green analyzer run that isn't configured-away.
   Good candidate to standardize across the analyzer-chapter modules.
