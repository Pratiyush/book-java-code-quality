# GATE REPORT — CODE-REVIEW — Chapter 110 (the book's final chapter)

## Header

- **Gate:** CODE-REVIEW (FLOOR C, second half)
- **Chapter key:** 110 (frozen key; FINAL_INDEX Ch 47 — closes Part XIV + the whole book)
- **Slug:** `110_maturity_model_roadmap`
- **Module under review:** `08-companion-code/110_maturity_model_roadmap/`
- **Draft under review:** `03-drafts/110_maturity_model_roadmap/110_maturity_model_roadmap_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer agent (senior-PR review of reader-copyable code)
- **Build run:** `mvn -B -Pquality -f pom.xml clean verify` on JDK 21.0.11 (`openjdk@21`) — BUILD SUCCESS; 12 tests / 0 failures; 0 Checkstyle violations; 0 SpotBugs findings; no compiler warnings.
- **Verdict:** **PASS**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21 that a reader can paste with confidence. It builds green and
warning-clean from a clean slate, the closing honesty of the chapter is encoded in the *code path under
test* (not just comments), every displayed `// tag::` region is brace-balanced (or a clean opening
excerpt), ≤9 lines, and free of banned NEUTRALITY phrasing. No hardcoded secret, no DORA band/statistic
asserted in code or config, no neutrality violation, no invented fact. There are no BLOCKER or MAJOR
findings. Two MINOR/NOTE items are recorded for polish only; neither blocks FLOOR C.

---

## Six dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 & code quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity (incl. originality/attribution) | **PASS** |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `overallLevel` is `min` over `countedStage` (the LOWEST, never an average) — matches the prose claim exactly; a stalled dimension is discounted via `policy.countedStage` → `DimensionRating.effectiveStage` (→ FOUNDATIONS when outcomes not improving). The "wall of green can't hide a fire" invariant is real and unit-pinned (`overallLevelIsTheLowestDimension`, `stalledDimensionIsDiscountedInTheLevel`).
- `recommend` ordering is the documented priority: RestoreOutcomes (vanity-ladder guard) → Sustain (past threshold) → Advance (start where the pain is). The deliberate asymmetry — the RestoreOutcomes filter tests the *claimed* `r.stage()` while `advanceOrSustain` works the *discounted* `countedStage` — is sound: a stalled advanced dimension (claimed > FOUNDATIONS) is always caught by the RestoreOutcomes filter before discounting could mislead Advance/Sustain; the only stalled dimensions that bypass the filter are those claimed at FOUNDATIONS, which cannot be a vanity climb. No path lets a stalled fire reach Sustain under prod.
- `Stage.next()` correctly clamps at SUSTAIN_EVOLVE (returns `this`, no AIOOBE). `RoadmapPolicy.load` handles the missing-resource case explicitly (throws IAE), closes the stream via try-with-resources, and wraps IOException as UncheckedIOException — no resource leak, no swallowed exception.
- Tests: 12 tests / 27 assertions, none vacuous. The failure path is genuinely exercised — RestoreOutcomes (`recommendsRestoringOutcomesBeforeClimbing`), empty-ratings rejection, unknown-profile rejection, null-component rejection — and the dev/prod profile divergence is asserted on identical fixtures.

### 2. Idiomatic Java 21 & code quality — PASS
- Records (`DimensionRating`, `RoadmapPolicy`) with compact constructors validating via `Objects.requireNonNull`; a sealed `NextStep` with record permits — modern, intention-revealing, exhaustive-by-design.
- Streams with `Comparator.comparingInt(Stage::order)`, `min(...).orElseThrow()`, and an `Optional.<NextStep>map(...).orElseGet(...)` chain that reads cleanly. Enum-with-data (`Stage`, `Dimension`) is the right tool.
- No anti-patterns: no `System.out`/`printStackTrace`, no raw threads, no blocking. Concurrency is expressed with `AtomicLong` + a `volatile int` for the two dashboard gauges.
- DI/config idiom: policy is externalized to `maturity-<profile>.properties` selected by a system property — the framework-agnostic `%dev`/`%prod` shape the guide asks for, on the JDK alone.

### 3. Security — PASS
- No hardcoded secret/password/token/key. The only "secret" hits are the *name of a practice* ("secrets pre-commit", "secrets detection") in Stage/Dimension descriptions — not credential values.
- Input is validated on the public surface: empty ratings rejected, null components rejected, unknown profile rejected. No injection sink (no SQL, no reflection on user input, no command exec). `RoadmapPolicy.load` reads a fixed-name classpath resource derived from a profile string, surfaced only through `Stage.valueOf`/`Boolean.parseBoolean` with safe defaults; an unknown profile fails closed with a clear message that leaks no internals/stack trace.

### 4. Simplicity & readability — PASS
- Smallest code that teaches the point; zero runtime dependencies (JDK only), so the module dogfoods the book and builds fast. No dead code, no unused deps, no placeholder names (no Foo/Bar/tmp/TODO). Every public type carries a one-line purpose Javadoc that reads cold. `@throws IllegalArgumentException` on `overallLevel`/`recommend` is accurate (both route through `requireNonEmpty`).

### 5. Prose↔code fidelity (+ originality) — PASS
- All five displayed tag anchors in the draft (`roadmap-stages`, `overall-level`, `recommend`, `roadmap-policy`, `next-step`) resolve to exactly one `tag::`/`end::` pair each in source; the set of source tags equals the set of draft include anchors — no orphan tag, no dangling include.
- **Snippet integrity (CRITICAL): all PASS.**
  - `roadmap-stages` (3 ln) — complete method, brace-balanced.
  - `overall-level` (4 ln) — one complete `return ... .orElseThrow();` statement, balanced.
  - `recommend` (7 ln) — one complete `return ... .orElseGet(...);` statement; lambda parens/braces balanced.
  - `roadmap-policy` (5 ln) — clean opening excerpt: `public record RoadmapPolicy(...) {` + the two profile constants; closing brace intentionally outside the region.
  - `next-step` (5 ln) — clean opening excerpt: `public sealed interface NextStep ... {` + the `Advance` record; rest of the type outside the region.
  - None broken mid-statement; all ≤9 lines.
- Prose claims map to code: "lowest dimension, never an average" → `min` over `countedStage`; "discount a stalled stage" → `effectiveStage`; "requireOutcomes knob (on in prod)" → `prod` properties `roadmap.require-outcomes=true`; "sustainAtStage threshold" → `roadmap.sustain-at-stage=GOVERN_OBSERVE` (prod) / `SUSTAIN_EVOLVE` (dev); sealed advance/restore-outcomes/sustain result → `NextStep` permits. Faithful.
- **DORA discipline honored:** no DORA performance band, tier name, or numeric statistic appears anywhere in code or config — DORA is not even named in the code (the dimension is the generic "delivery and stability metrics"). This matches the draft's standing `⚠ verify-at-pin` guard (09-flags/85) and its assertion that the body asserts NO DORA band.
- **Originality:** original-for-this-book. Domain `org.acme.maturity` with distinct types is a different concern from the Ch 46 peer's `org.acme.refstack` (ship/no-ship gate) — not a rename or reskin. No upstream quickstart/sample lifted; no copied NOTICE/header boilerplate.

### 6. Neutrality in code — PASS
- Full banned-phrase scan over every `.java`/`.properties`/`.xml`/`.md` in the module (better than / unlike X / the problem with X / superior / beats / outperforms / kills / destroys / obvious choice over / no reason to use, plus wins/loses/best/worst): **zero hits**. No comment, identifier, log string, or config value crowns or disparages any tool. Tool names that appear (Checkstyle, SpotBugs, Error Prone, SonarQube, SBOM, SAST) are named as practices the roadmap sequences, never ranked.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | The two observability gauges are individually thread-safe (`AtomicLong` + `volatile int`) but updated as two separate writes in `recommend` (`incrementAndGet()` then `recordLowest(...)`), so under concurrent calls `assessmentsRun()` and `lowestDimensionStageOrder()` can be momentarily inconsistent *with each other* (or reflect a different concurrent call's lowest). Each gauge alone is correct and the README frames them as independently-trended dashboard signals, so this is not a defect for the documented use — worth a one-line note for a reader who copies this as an atomic snapshot. | NOTE | `MaturityAssessment.java:75-76, 137-139` | Optional: add a one-line comment that the two gauges are independently-trended, not a consistent snapshot; or, if a snapshot is ever wanted, return both from a single synchronized read. No change required to pass. |
| 2 | `Roadmap.describe(Stage)` is a public method with full Javadoc but is exercised by tests only via the substring `"a default not a rung"` and is not shown in any displayed snippet. It is correct and harmless, but is the one public surface that exists mainly to assert the carve-out string. | MINOR | `Roadmap.java:39-41` | Optional polish: leave as-is (it documents the carve-out in the view), or fold the carve-out assertion into the stage descriptions. Non-blocking. |

---

## Blockers

**None.** No security, neutrality, invention, or snippet-integrity finding. FLOOR C (second half) is not blocked.

---

## Build / lint result

- `mvn -B -Pquality -f pom.xml clean verify` (JDK 21.0.11): **BUILD SUCCESS**.
- Tests: 12 run, 0 failures, 0 errors, 0 skipped.
- Checkstyle (engine 10.26.1 behind plugin 3.6.0 — the two-pin lesson): **0 violations**.
- SpotBugs (4.9.3.0, effort=Max, threshold=Medium): **0 findings**; exclude filter is empty (zero reviewed suppressions, by design).
- Compiler: no warnings on a clean build.
- Secret scan: **0** hardcoded credentials. DORA-band/statistic scan in code+config: **0**.

---

## Gate-specific checks (CODE-REVIEW)

- [x] Correctness / idiomatic / security / simplicity / prose-code-fidelity / neutrality-in-code all PASS.
- [x] `./mvnw -B verify` equivalent green and warning-clean (run via `mvn -Pquality`, no wrapper in tree; JDK 21.0.11).
- [x] At least one integration test per public behavior, INCLUDING the failure path (RestoreOutcomes + the three rejection paths).
- [x] No hardcoded-secret literal anywhere in the tree.
- [x] Every displayed `// tag::` region brace-balanced or a clean opening excerpt, ≤9 lines, no banned word.
- [x] No DORA performance band asserted as fact in code; no undated/unattributed maturity or DORA statistic in code or config.

---

## Learnings & pipeline suggestions

- The "clean opening excerpt" pattern (record/sealed-interface header + first member, closing brace outside the region) is the right way to display a type's shape inside the ≤9-line cap without a broken-mid-statement snippet. Worth promoting to EXAMPLES-GUIDE as an explicit, named, allowed snippet shape alongside "complete method" and "complete statement".
- Encoding a chapter's *limitations* in the code path under test (lowest-not-average; discount-the-stalled-stage; sustain-past-threshold) — not only in comments — is a strong fidelity pattern for synthesis/closer chapters and made this review fast and unambiguous. Recommend it as a checklist item for capstone/closer modules.
- This module has no Maven wrapper in its own tree and relies on a system `mvn`; JAVA_HOME was unset in the review shell. Minor reviewer-ergonomics note: a one-line "JDK 21 required; set JAVA_HOME" in module READMEs (or a wrapper at the aggregator) would smooth the CODE-REVIEW build step. Non-blocking.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 110 gate-run PASS
```
