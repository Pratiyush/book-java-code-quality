# GATE-REPORT — CODE-REVIEW (FLOOR C, second half)

- **Chapter / dossier:** 38 (folds 40) — *Teaching the Build Your Rules* — Part IV, Ch 18
- **Module under review:** `08-companion-code/38_custom_rules_codegen_lombok/`
- **Draft reviewed:** `03-drafts/38_custom_rules_codegen_lombok/38_custom_rules_codegen_lombok_v1.md`
- **Reviewer:** code-reviewer agent (senior-PR pass on published, reader-copied deliverable)
- **Date:** 2026-06-27
- **Toolchain:** JDK 21.0.11 (Homebrew openjdk@21) / Maven (homebrew `mvn`, parent pins compiler.release=21)

## Verdict: **PASS-WITH-FIXES**

Builds green and warning-clean, all seven displayed tag-regions are brace-balanced, ≤9 lines, and
statement-complete (no mid-statement fragments, no duplicate/imbalanced end-tags). Every load-bearing
API symbol traces to a pinned jar. Two non-blocking FIX findings: a stale "Chapter 88" cross-reference
repeated in three places (the observability chapter is **Chapter 45** in `FINAL_INDEX.md`), and one
minor prose↔code gap (`@CheckReturnValue` shown but not mentioned in prose). **No BLOCKER. No
security, neutrality, invention, or originality finding. FLOOR C is not blocked.**

## Build / lint result

| Check | Result |
|---|---|
| `mvn -B -Pquality clean verify` (this module) | **BUILD SUCCESS** |
| Tests run | **14**, Failures 0, Errors 0, Skipped 0 (MoneyRuleTest 11 + MoneyArchTest 3) |
| Checkstyle (10.26.1 engine, curated house ruleset, incl. test sources) | **0 violations** |
| SpotBugs (4.9.3.0, effort=Max, threshold=Medium) | **BugInstance size is 0** |
| Compiler warnings (`-Xlint:all,-processing`) | **0 `[WARNING]` lines** — no unchecked/rawtypes/deprecation |
| Hardcoded-secret grep (password/secret/token/apikey/key) | **none** (only false positive: the word "tokens" in a Javadoc sentence) |

Matches the recorded `_EXAMPLE.md` claim (14 tests, 0 Checkstyle, 0 SpotBugs) exactly.

## Tag-region verification (CRITICAL CHECK — all PASS)

Each `// tag::` region extracted and analyzed; every include directive in the draft maps to exactly one
open + one close marker (no duplicates, no imbalance).

| Tag | File | Lines | Brace Δ | Paren Δ | Starts on | Ends on | Verdict |
|---|---|---|---|---|---|---|---|
| `hand-written-guard` | MoneyGuards.java | 7 | 0 | 0 | method decl | `}` | PASS |
| `reflective-inspector` | MoneyApiInspector.java | 9 | 0 | 0 | method decl | `}` | PASS |
| `errorprone-annotation-fence` | LegacyMoneyFactory.java | 8 | 0 | 0 | `@RestrictedApi(` | `}` | PASS |
| `archunit-predicate` | MoneyArchRules.java | 8 | 0 | 0 | field assignment | `};` | PASS |
| `archunit-condition` | MoneyArchRules.java | 9 | 0 | 0 | `check(...)` method | `}` | PASS |
| `archunit-rule` | MoneyArchTest.java | 3 | 0 | 0 | `ArchRule rule =` | `;` | PASS |
| `record-money` | Money.java | 9 | 0 | 0 | record decl | `}` | PASS |

All ≤9 lines (max 9), all balanced, none a mid-statement fragment. `archunit-condition` is the
`@Override check(...)` method lifted from inside the anonymous `ArchCondition` and reads as a coherent
complete method, which matches how the prose displays it ("the constraint").

## Six dimensions

### 1. Correctness — **PASS**
- The record `Money(BigDecimal, Currency)` makes the invariant structural (no `double` constructor
  exists); compact ctor adds null/negative guards. `MoneyGuards.of` uses `new BigDecimal(String)`
  (exact) and `LegacyMoneyFactory` uses `BigDecimal.valueOf(double)` (avoids the `new BigDecimal(double)`
  binary-rounding trap) — both correct and consistent with the prose.
- Failure-path tests are real, not vacuous: ArchUnit breach asserts `AssertionError` + message contains
  `LegacyOrderLine` + `floating-point money`; blank input, negative amount, and unknown profile each
  assert `IllegalArgumentException`. The honest-limit edge (`ErasedMoneyHolder` via `List<Double>`) is
  asserted invisible to the reflective inspector — the chapter's layering point, proven in code.
- The Error Prone fence test is exemplary: it asserts `@RestrictedApi` is NOT runtime-visible
  (`isAnnotationPresent` → false) yet IS present as a descriptor in the compiled class file. Verified
  against the 2.36.0 jar: `@RestrictedApi` carries only `@Target` (no `@Retention`), so it defaults to
  CLASS retention — the test's claim is empirically correct and the green build confirms it executes.
- No resource leaks (`MoneyPolicy.load()` and the class-file reader both use try-with-resources). No
  swallowed exceptions; `IOException` wrapped as `UncheckedIOException`, missing resource as
  `IllegalStateException`.

### 2. Idiomatic Java 21 & code quality — **PASS**
- `record` for `Money`/`MoneyViolation`; `enum Severity`; pattern `instanceof` in `HandWrittenMoney.equals`;
  `var` in tests; `AtomicLong` for the concurrent counter; private constructors on the utility/factory
  holders. Each public type carries a one-line purpose Javadoc.
- ArchUnit core (not archunit-junit5) driven via `ClassFileImporter` + `rule.check(...)` from plain
  JUnit — the pom comment justifies this to stay on the parent's single JUnit platform version. Sound.
- Minor: `MoneyPolicyHealth.record(...)` returns the new total but every caller ignores it; harmless and
  arguably useful API, not a defect.

### 3. Security — **PASS**
- No hardcoded secrets/passwords/tokens/keys (grep clean). No injection sink; no network/SQL surface.
- `Currency.getInstance(currency)` throws `IllegalArgumentException` on a bad ISO code — input validated
  at the boundary. No internal/stack-trace leakage to any external surface (this is a library module).

### 4. Simplicity & readability — **PASS**
- Smallest code that teaches the point; no dead code, no unused deps (slf4j-nop is justified to silence
  ArchUnit's SLF4J "no providers" warning; version matched to ArchUnit's transitive 2.0.17). No
  placeholder names (no Foo/Bar/tmp/TODO); realistic `org.acme.storefront` domain.
- The two-fixture design (`catalog.PricedItem` clean / `legacy.LegacyOrderLine` breach) keeps every rule
  green-by-detection rather than green-by-passing-a-rule-that-should-fail. Clear and honest.

### 5. Prose↔code fidelity & originality — **FIX** (minor; no invention)
- **Traceable:** `@RestrictedApi(explanation, link, allowlistAnnotations)` and `@CheckReturnValue` exist
  verbatim in the pinned `error_prone_annotations-2.36.0.jar`; `SimpleConditionEvent.violated(Object,
  String)`, `FreezingArchRule.freeze/.check`, `JavaClass.isEquivalentTo/getMethods/getPackageName` exist
  in `archunit-1.4.2.jar`. Both versions are SOURCE-PIN rows (ArchUnit 1.4.2 ✅; Error Prone line pinned
  + NullAway 0.13.4 anchors the 2.36.0 annotation coordinate). Both jars present in local `~/.m2`.
- **Originality (LEGAL-IP §5): PASS.** Original-for-this-book — the book's own shared storefront domain;
  the `@RestrictedApi` usage is a fresh money-factory application, not Error Prone's published sample. No
  verbatim-lift markers needed; none missing.
- **FIX (F1):** stale "Chapter 88" cross-reference in `MoneyViolation.java:7`, `MoneyPolicyHealth.java:10`,
  and `README.md:62`. `FINAL_INDEX.md` line 82 places the observability chapter at **Chapter 45**
  (keys 106+107+108); there is no Chapter 88 in the book of record, and the draft body never cites one. A
  reader reading the code cold cannot follow this pointer.
- **FIX (F2, trivial):** `@CheckReturnValue` is shown in the `errorprone-annotation-fence` snippet but the
  surrounding prose (draft ~L128-130) never names or motivates it. Either add a half-sentence ("its result
  is a value, so `@CheckReturnValue` forbids discarding it") or drop the annotation from the displayed
  region. Not an invention — the annotation is pinned and real.

### 6. Neutrality in code — **PASS**
- No banned comparative phrasing (`better than` / `unlike X` / `superior` / `beats` / `the problem with
  X`) anywhere in src/README/pom/config. Every comment frames each realization with its strongest case
  AND its hardest limit; package-info and class Javadocs explicitly state "no realization is crowned" and
  route the which-tool verdict to Chapter 17. Exemplary.

## Findings table

| Sev | File:line | Issue | Fix |
|---|---|---|---|
| FIX | MoneyViolation.java:7; MoneyPolicyHealth.java:10; README.md:62 | "Chapter 88" cross-ref — observability chapter is Chapter 45 in FINAL_INDEX; no Ch 88 exists | Replace "Chapter 88" with the correct observability chapter number (45) in all three places |
| FIX | LegacyMoneyFactory.java:42 (snippet) + draft ~L128-130 | `@CheckReturnValue` shown in displayed snippet but unexplained in prose | Add a half-sentence motivating it, or drop it from the tag region |
| NIT | MoneyPolicyHealth.java:31 | `record(...)` return value ignored by all callers | Optional: keep (useful API) or make void; harmless either way |

- **BLOCKERS:** none.
- **Counts:** 0 BLOCKER · 0 FAIL · 2 FIX · 1 NIT · 0 security · 0 neutrality · 0 invention · 0 originality.

## Learnings & pipeline suggestions
- The repo's `check_snippets.sh` reports "0 marker(s)" for this module — it scans a different marker
  syntax than the `// tag::name[]` / `// end::name[]` AsciiDoc style these files use, so it provides **no
  coverage** for `tag::` regions. Recommend teaching `check_snippets.sh` the `tag::`/`end::` form (or
  adding a sibling check) so brace-balance/≤9-line/duplicate-end-tag verification is automated rather than
  reviewer-manual on every code chapter.
- The "Chapter 88" slip is a recurring class of defect: **dossier keys vs. final chapter numbers** drift
  in code comments. A cheap lint — grep companion-code comments for `Chapter NN` and diff against
  `FINAL_INDEX.md`'s chapter set — would catch these before review.
- No Maven wrapper (`mvnw`) exists at `08-companion-code/` though the draft and README both invoke
  `./mvnw`. Reviewers must fall back to a system `mvn` + a hand-set `JAVA_HOME`. Either commit the wrapper
  or update the docs to `mvn`, so the documented build command works as written for a reader who copies it.

---
**ORCHESTRATOR FIX — 2026-06-27.** Stale "Chapter 88" cross-reference corrected to
"Chapter 45" (the observability chapter in FINAL_INDEX) in MoneyViolation.java,
MoneyPolicyHealth.java, and README.md (no Chapter 88 exists). Comment/README-only;
rebuilt green. The `@CheckReturnValue` motivation note remains a MINOR for the lift pass.
