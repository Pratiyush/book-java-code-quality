# GATE REPORT — CODE-REVIEW (FLOOR C, second half)

## Header

- **Gate:** CODE-REVIEW (Step 4b) — technical profile
- **Chapter key:** 105 (frozen key, `01-index/CANDIDATE_POOL.md`) — Chapter 44 in `FINAL_INDEX`
- **Slug:** `105_performance_regression_gates`
- **Draft under review:** `03-drafts/105_performance_regression_gates/105_performance_regression_gates_v1.md`
- **Module reviewed:** `08-companion-code/105_performance_regression_gates/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` (senior-PR review of code readers copy as a published deliverable)
- **Build:** `mvn -B -Pquality -f .../105_performance_regression_gates/pom.xml clean verify` → **BUILD SUCCESS**,
  `Tests run: 10, Failures: 0, Errors: 0`; `You have 0 Checkstyle violations.`; SpotBugs `BugInstance size is 0`;
  compiler **warning-clean** (`javac [debug release 21]`, no WARNING/deprecation/unchecked lines). JDK 21.0.11.
- **Verdict:** **PASS-WITH-FIXES** (all six dimensions PASS; only NIT/MINOR polish items, none blocking FLOOR C)

---

## Verdict rationale

This is exemplary, idiomatic Java-21 teaching code. The gate logic — the runnable, unit-tested heart of the
chapter — is correct, fail-safe, and direction-aware, and every numeric input that touches it is **synthetic
and labelled** or an **illustrative tolerance fraction**; there is **no timeless asserted performance
measurement** anywhere in the deliverable. All four displayed `// tag::` regions are brace-balanced,
paren-balanced, complete (not broken mid-statement), and ≤ 9 lines. No banned NEUTRALITY phrasing appears in
the module or the draft. No hardcoded secret. The findings below are consistency/polish nits only (a Javadoc
"never null" contract not mirrored by a guard; one config file missing the "illustrative" word its sibling
carries; harmless dead default literals). None of them block FLOOR C. Sent back as PASS-WITH-FIXES so the
example-builder can apply the polish; re-review is not required to pass the floor.

---

## The six review dimensions

### 1. Correctness — **PASS**
The branch logic in `RegressionGate.evaluate` is right on every path verified by hand and by test:
- An improvement (`regression <= 0 <= flagTolerance`) passes (`passesOnImprovement`).
- A move within `flagTolerance` passes; a move within the measurement's confidence band passes even when large
  (`noisyRegressionIsNotFailed`, 130 vs 100 with ±40 half-width) — the fail-safe core.
- A regression past the flag band but `< failTolerance` and outside the noise band → `Flag`
  (`flagsSmallRegression`).
- A regression `>= failTolerance` and outside the noise band → `Fail` (`failsLargeRegression`). The "at or
  above fails" Javadoc matches the `<` ternary (a value exactly at `failTolerance` falls through to `Fail`).
- `regressionFraction` divides by `Math.abs(baseline)` and guards the zero-baseline case (returns 0.0 → pass),
  so no divide-by-zero. Direction is honored (latency `current - baseline`, throughput `baseline - current`),
  proven by `throughputIsDirectionAware` (the same +200 number that FAILS for latency PASSES for throughput).
- No resource leak: the only I/O is `GateConfig.load`, which uses try-with-resources on the classpath stream
  and re-throws as `UncheckedIOException` (no swallowed exception). Fail-fast validation in both compact
  constructors (`GateConfig`, `BenchmarkResult`) is exercised by `invalidConfigIsRejected` /
  `invalidResultIsRejected`.
- Tests are non-vacuous: each asserts a concrete verdict subtype, and `flagsSmallRegression` additionally
  asserts the reason text contains `"6.0%"`, pinning the `describe` formatting. The failure path is real
  (`Fail` for a confident regression; `IllegalArgumentException` for invalid construction).

### 2. Idiomatic Java 21 & quality — **PASS**
Sealed `interface GateVerdict permits …` with three `record` variants is the canonical Java-21 modelling of a
closed verdict set. Records for all value types (`Baseline`, `BenchmarkResult`, `GateConfig`), compact
constructors for invariants, `Objects.requireNonNull` on the one reference field the gate stores, `Locale.ROOT`
on `String.format` (locale-correct number formatting — a genuine quality touch), `private static` helpers, a
`final` class. No raw threads, no blocking, no `System.out` logging, no anti-patterns. Externalized config via a
profile-selected properties file mirrors the `%dev`/`%prod` idiom the book teaches. JDK-only runtime is a
deliberate, documented choice that dogfoods the book.

### 3. Security — **PASS**
Secret-literal scan over `src/`, `README.md`, `pom.xml`, `config/` (password/secret/token/apikey/credential/
private-key/BEGIN-RSA) returns **zero hits**. No injection sink (the only external input is two parsed double
tolerances from a classpath properties file, range-validated in the compact constructor). Error messages name a
profile or a bound, never an internal/stack trace to a caller. No network, no deserialization, no reflection
beyond `getResourceAsStream`.

### 4. Simplicity & readability — **PASS**
Smallest code that teaches the point: 6 main types + package-info, every public type carries a one-line-plus
purpose Javadoc, names are realistic and domain-aligned (`checkout.p99`, `checkout.tps`, `org.acme.perfgate`).
No dead code path readers would copy, no gratuitous abstraction, no unused import (Checkstyle `UnusedImports`
is green), no unused dependency (JDK-only + two test libs). No `Foo`/`Bar`/`tmp`/`TODO`/`FIXME` anywhere.

### 5. Prose↔code fidelity & originality — **PASS**
All four `<!-- include: … -->` directives in the draft resolve to existing tag regions of the named files; each
region is ≤ 9 lines and brace-balanced (table below). Identifiers and comments use canonical names. Tolerance
numbers are labelled **illustrative**; test numbers are labelled **synthetic** — matching the prose's repeated
"synthetic, not measured" claim, so no fabricated performance figure enters the book. JMH 1.37 is named-and-
pinned in prose/POM but deliberately not vendored (REPRO PENDING-RUNTIME), consistent across draft, README,
POM header, and package-info. **Originality (LEGAL-IP §5):** every file is original-for-this-book; no upstream
JMH quickstart/archetype skeleton or NOTICE boilerplate is copied; no region is substantially verbatim from an
upstream file, so no per-file attribution is owed. Confirmed.

### 6. Neutrality in code — **PASS**
Banned-phrase scan (better than / unlike X / the problem with / superior / beats / outperforms / kills /
destroys / blows away / obvious choice over / no reason to use, and the heading superlatives) over the module
text **and** the draft returns **zero hits**. No comment, identifier, log string, or POM name crowns or
disparages any comparator. The Gatling/JMeter/k6 mention is a neutral "tool of the … class," named without
ranking.

---

## Tag-region verification (the displayed deliverable — brace-balance + ≤9-line gate)

| Tag | File | Lines | Braces | Parens | Complete statement? | Verdict |
|---|---|---:|:--:|:--:|:--:|:--:|
| `benchmark-shape` | `BenchmarkResult.java` | 9 | 3=3 | 3=3 | full record + compact ctor | ✅ |
| `baseline-model` | `Baseline.java` | 9 | 2=2 | 4=4 | full Javadoc + `movedTo` | ✅ |
| `regression-gate` | `RegressionGate.java` | 9 | 2=2 | 15=15 | full `evaluate` method | ✅ |
| `gate-pass-fail` | `GateVerdict.java` | 5 | 4=4 | 3=3 | full sealed interface | ✅ |

No snippet is broken mid-statement; the load-bearing `regression-gate` region is the entire gate method.

---

## Build / lint result

- `mvn -B -Pquality clean verify` (standalone, JDK 21.0.11, Maven 3.9.x): **BUILD SUCCESS**.
- Tests: **10 run, 0 failures, 0 errors, 0 skipped** — both metric directions, all three verdict branches, the
  noise band, the baseline ratchet, both config profiles, and two fail-fast construction guards (the failure
  paths).
- Checkstyle (engine 10.26.1, plugin 3.6.0): **0 violations**. SpotBugs (4.9.3.0, effort Max): **0 findings**,
  empty reviewed-filter (no suppression needed — the chapter's point made by the build).
- Compiler: **warning-clean** (`release 21`; no deprecation/unchecked/raw-type warnings).
- Module is correctly **NOT yet registered** in the aggregator `<modules>` list (registered-last rule; joins on
  CODE-REVIEW PASS).

---

## Findings

| # | Severity | Location | Issue | Suggested fix (example-builder) |
|---|---|---|---|---|
| 1 | MINOR | `BenchmarkResult.java:23-30`, `Baseline.java:18`, `RegressionGate.java:49` | Javadoc promises "never `null`" on `metric`/`unit`/`direction` record components and on `evaluate`'s `baseline`/`current` params, but only `RegressionGate`'s ctor enforces non-null (`Objects.requireNonNull(config)`). A null `baseline`/`current` would NPE at `RegressionGate.java:50` rather than fail the documented contract cleanly. The tested paths never pass null, and `metric`/`unit` are never dereferenced by the gate, so this is a teaching-code consistency gap, not a defect — but this book teaches null-safety (Ch 11). | Add `Objects.requireNonNull(metric, "metric")` etc. to the two record compact constructors, and `requireNonNull(baseline)`/`requireNonNull(current)` at the top of `evaluate`. Keep the snippet ≤ 9 lines (the `evaluate` guard can be one line; the record ctors are outside the tag region). |
| 2 | NIT | `src/main/resources/perfgate-dev.properties` | The `prod` properties file carries the "These numbers are ILLUSTRATIVE …" caveat; the `dev` file does not repeat the word "illustrative" (it says "wide and forgiving"). package-info/README/POM all label tolerances illustrative, so the deliverable is covered, but the two sibling files read inconsistently. | Add a one-line `# Numbers are ILLUSTRATIVE — set per service (Chapter 43).` to `perfgate-dev.properties` to match its sibling. |
| 3 | NIT | `GateConfig.java:71-72` | The `getProperty(..., "0.05")` / `"0.10"` fallback defaults are dead in practice — both property files define both keys — and `"0.10"` happens to equal the prod fail tolerance, which could read as a hidden coupling. Harmless. | Optional: drop the string defaults and treat a missing key as a config error (symmetry with the missing-file `IllegalArgumentException`), or add a `// fallback only; both shipped profiles set these` comment. |

No BLOCKER, no MAJOR. No security, neutrality, invention, broken-snippet, or timeless-perf-number finding.

---

## Critical-check results (explicitly cleared)

- **Brace balance / ≤9 lines / not-broken-mid-statement on every displayed snippet:** PASS (table above).
- **Banned NEUTRALITY word in any deliverable text (code, comments, README, POM, draft prose):** NONE.
- **Timeless asserted performance NUMBER:** NONE. Every number that touches the gate is a synthetic labelled
  fixture (test) or an illustrative tolerance fraction (properties); the only concrete `200ms` is quoted inside
  `Baseline.java:5` as the *absolute-threshold anti-pattern the gate rejects*, and the "p99 tripled / three
  milliseconds" in `package-info.java:5` is the narrative hook (a hypothetical), neither asserted as a
  measurement of this system.
- **Hardcoded secret:** NONE.
- **Failure-path test exercises the real failure path:** YES (`Fail` verdict + two fail-fast construction
  exceptions).

---

## Learnings & pipeline suggestions

- **A "never null" Javadoc tag on a record component is a cheap reviewer trip-wire.** Finding #1 recurs whenever
  a record documents a non-null contract its compact constructor doesn't enforce. Suggest EXAMPLES-GUIDE add a
  one-liner: in this book specifically (Ch 11 teaches null-safety), any `@param … never null` on a record must
  be backed by a `requireNonNull` in the compact constructor, or the phrase dropped — the code must practice
  what the chapter preaches.
- **The synthetic/illustrative labelling held up to a hostile numeric scan.** The triple-labelling pattern (POM
  header + package-info + every test-method comment) meant a grep for measurement-shaped literals surfaced only
  guarded range bounds and clearly-captioned fractions. Worth keeping as the standing pattern for the other
  perf/benchmark modules (keys 101/104), and worth noting that the *properties* layer should carry the
  illustrative caveat in **every** profile file, not only the strict one (finding #2).
- **Registered-last discipline is observable and was correct here.** The module builds green standalone while
  absent from the aggregator `<modules>` list; CODE-REVIEW PASS is the trigger to register it. The two-stage
  EXAMPLE-BUILD → CODE-REVIEW split is working as intended.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
