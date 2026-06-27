# GATE-REPORT — CODE-REVIEW (FLOOR C, second half) — Chapter 55

- **Chapter:** 55 — Giving the Diagram Teeth (enforcing architecture / fitness functions); owner key 55, folds 33 + 56
- **Module:** `08-companion-code/55_enforcing_architecture_fitness_functions/`
- **Draft reviewed:** `03-drafts/55_enforcing_architecture_fitness_functions/55_enforcing_architecture_fitness_functions_v1.md`
- **Reviewer:** code-reviewer agent (senior-PR pass on copy-paste-grade example code)
- **Date:** 2026-06-27
- **Build re-run by reviewer:** `mvn -B -Pquality -f pom.xml clean verify` → **BUILD SUCCESS**, JDK 21.0.11, ArchUnit 1.4.2, 8 tests pass (3 `OrderFlowIntegrationTest` + 5 `ArchitectureFitnessTest`), **0 Checkstyle violations, 0 SpotBugs findings, 0 compiler/javac warnings** (parent enables `-Xlint:all,-processing`).

## Verdict: **PASS-WITH-FIXES**

No BLOCKER. No security, neutrality, invention, or broken-snippet finding. FLOOR C is **not blocked**. Two FIX-level fidelity/test-rigor items below should be applied by the example-builder and re-reviewed; none gate the floor.

---

## Dimension scores

| # | Dimension | Verdict |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 & code quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity (+ originality/attribution) | **FIX** |
| 6 | Neutrality in code | **PASS** |

---

## 1. Correctness — PASS

- Layered sample is wired correctly through every governed layer (`OrderController` → `OrderService` → `OrderRepository` over `..domain..` value types). `OrderFlowIntegrationTest` exercises the happy path **and** the real failure path (`readingAnUnknownOrderTakesTheFailurePathAndCountsIt` asserts `OrderNotFoundException` is thrown, the message carries the id, AND the not-found counter ticks to `1L`). The failure-path test is non-vacuous.
- `OrderService.read` increments `notFoundCount` **inside** the `orElseThrow` supplier, so the counter advances only on the genuinely-empty path — correct, not double-counted.
- Concurrency: `AtomicLong` for the counter and `ConcurrentHashMap` for the store are the right primitives; no raw threads, no leaked mutable state.
- Records validate in compact constructors (`Money` rejects negative minor units and null currency; `OrderId` rejects blank; `Order` null-checks). `Money.plus` guards currency mismatch. No swallowed exceptions; no resource leaks (in-memory only).
- ArchUnit rules are evaluated via `ClassFileImporter` + `rule.check(...)`, the engine-agnostic core path — a deliberate, correct choice to keep the module on the parent's single JUnit-platform version (documented in the class Javadoc and the pom comment).
- Freeze ratchet runs end-to-end: the reviewer confirmed `target/archunit-freeze/stored.rules` + a baseline file are actually written, and the two `.check(LAYERS)` calls pass over a genuinely-breaching import (so they would throw if the freeze did not suppress the recorded violation). The mechanism is real, not faked.

## 2. Idiomatic Java 21 & code quality — PASS

- Uses `java.lang.System.Logger` (JEP 264 platform logger) rather than ad-hoc `System.out` in production code — exactly the idiom the chapter's own coding rule (`NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`) preaches. The only `System.out` writes are inside the *seeded breach* class, which is the point.
- Records for value types; `final` classes; constructor injection of an interface port (`OrderRepository`); `Objects.requireNonNull` with field names on every public boundary. `serialVersionUID` present on the exception. All idiomatic for 21 LTS.
- No anti-patterns: no field injection, no raw types, no blocking-where-it-must-not, no gratuitous abstraction.

## 3. Security — PASS

- Grep for `password|secret|token|apikey|api[_-]?key|passwd|private[_-]?key|BEGIN RSA/PRIVATE|AKIA…` across `src/`, `config/`, `pom.xml`, `README.md`: **no secret literals**.
- No injection sink (no SQL, no shell, no reflection-from-input). The one `Class.forName(...)` reference is a *string inside a Javadoc/README* illustrating the bytecode blind spot — not executed code.
- Exceptions carry only an order id in their message; no internals/stack-trace leakage by design.

## 4. Simplicity & readability — PASS

- Smallest code that teaches the point. Every public type carries a one-line purpose Javadoc; every package has a `package-info.java` describing its role in the layering. Realistic names (`OrderController`, `InMemoryOrderRepository`, `LegacyReportWriter`) — no `Foo`/`Bar`/`tmp`; package is the canonical `org.acme.storefront`.
- No dead code. `LegacyReportWriter.controller()` exists specifically to keep the `..web..` edge from being optimised away and is documented as such — justified, not dead.
- Dependencies are minimal and each is justified in a pom comment: ArchUnit core (the one pinned version literal), `slf4j-nop` (matched to ArchUnit's transitive `slf4j-api` 2.0.17 to silence the no-provider warning), JUnit, AssertJ. SpotBugs exclude filter is intentionally empty with a reason. No unused deps.

## 5. Prose↔code fidelity (+ originality/attribution) — FIX

- **Snippet integrity (the BLOCKER-class check) — all clear.** All five displayed `// tag::` regions are brace-balanced, ≤9 lines, complete statements (no mid-statement fragment), and free of banned NEUTRALITY words:
  - `layered-rule` = 9 lines (at the ceiling, within budget), braces 0/0, terminates on `;`.
  - `no-cycles-rule` = 2 lines; `coding-rule` = 1; `freezing-ratchet` = 2; `seeded-breach` = 8 lines, braces 1/1 (a complete constructor).
  - Every draft `<!-- include: …#tag -->` marker resolves to an existing, well-formed tag pair; no orphan tags.
- **GAV / version / API atoms trace to pin.** ArchUnit `com.tngtech.archunit:archunit:1.4.2` (SOURCE-PIN §2, built green); `archunit.properties` `cycles.maxNumberToDetect=100` / `cycles.maxNumberOfDependenciesPerEdge=20` as set in the module; layered/slices/coding-rule/freeze API all exercised by the running test. No invented identifiers.
- **Originality / attribution (LEGAL-IP §5).** All files are original-for-this-book over the canonical `org.acme.storefront` domain — not a copied upstream ArchUnit quickstart. No substantially-verbatim upstream sample; no attribution comment required.

**FIX 5a (overclaim — prose says the layered rule catches the seeded `..web..` edge, but the code never demonstrates that).** Draft line 123 ("…the kind of upward/cross-boundary edge a `layeredArchitecture()` rule rejects…") and the `LegacyReportWriter` class Javadoc (lines 11–16) both assert the seeded `..web..` field is rejected by `layeredArchitecture()`. The actual seeded-breach test (`seededBreachIsDetectedButDoesNotFailTheBuild`, test lines 76–84) runs **only** `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` and asserts on `"LegacyReportWriter"`; no test ever runs the layered rule over `LAYERS`. Moreover `LegacyReportWriter` lives in `..governance..`, which is **not a declared layer**, and the rule uses `consideringOnlyDependenciesInLayers()` — so even if the layered rule were run over the breaching import, a class in an undeclared package would not trip `whereLayer("Web").mayNotBeAccessedByAnyLayer()`. The `System.out` half of the breach is genuinely caught; the `..web..`-field half is **not** demonstrated and, as scoped, would not be caught by the rule as written. Fix: soften the prose + Javadoc to claim only what is proven (the coding-rule breach is detected and reported with the class named), OR add a test that puts the breach inside a declared layer / uses `consideringAllDependencies()` and asserts the layered rule reports it. Severity: FIX (fidelity overstatement; the code itself is correct).

## 6. Neutrality in code — PASS

- Grep of all source, config, pom, and README for the blocklist (`better than`, `unlike X`, `the problem with`, `superior`, `beats`, `outperforms`, `kills`, `destroys`, `blows away`, `…in the dust`, `obvious choice over`, `no reason to use`): **none**. Comments describe ArchUnit/JPMS/SpotBugs mechanisms without crowning; the seeded-breach and limit comments are descriptive.

---

## Findings table

| Sev | File:line | Issue | Fix |
|---|---|---|---|
| FIX | draft `55_…_v1.md:123` + `LegacyReportWriter.java:11-16` | Prose & Javadoc claim `layeredArchitecture()` rejects the seeded `..web..` field edge, but no test runs the layered rule over the breach, and the breach class sits in an undeclared `..governance..` package under `consideringOnlyDependenciesInLayers()`, so the rule as written would not catch that edge. Only the `System.out` (coding-rule) breach is actually demonstrated. | Reword prose+Javadoc to claim only the coding-rule detection that is proven, OR add a layered-rule test that places the edge in a declared layer (or switches to `consideringAllDependencies()`) and asserts it is reported. |
| FIX | `ArchitectureFitnessTest.java:87-100` | The freezing test's only hard assertion is `assertThat(ratcheted).isNotNull()`. The two `.check(LAYERS)` calls do exercise the real record-then-no-new ratchet (non-vacuous — they would throw if freeze failed to suppress), but the test never asserts the *discriminating* half of a ratchet: that a genuinely NEW violation still fails. | Strengthen: after the baseline run, assert that introducing a new console-access violation (or a second rule with an un-recorded breach) still throws via the frozen rule — so the test proves "old suppressed, new caught," the full ratchet contract. |
| NIT | parent `08-companion-code/pom.xml:106-110` | Parent sets `-Xlint:all,-processing` but not `-Werror`/`failOnWarning`, so warnings would not fail the build. Module is warning-clean today regardless, so no action for ch 55; note for the toolchain owner if a strict-warning floor is intended. | Optional: add `<failOnWarning>true</failOnWarning>` at the aggregator if the floor wants warnings to be hard. Out of scope for this chapter. |

## Build / lint result

- `mvn -B -Pquality -f pom.xml clean verify` → **BUILD SUCCESS** (reviewer re-ran twice from clean).
- Tests: **8 / 8 pass** (3 integration incl. failure path; 5 architecture fitness functions). At least one integration test per public behavior, failure path included — satisfied.
- Checkstyle: **0 violations**. SpotBugs (effort Max, threshold Medium): **0 findings**. Compiler: **0 warnings** under `-Xlint:all`.
- Freeze store artifact written under `target/archunit-freeze/` — ratchet mechanism confirmed live.

## Are the five fitness functions real + meaningful?

- **Layered rule** — real `layeredArchitecture()` over four genuinely-distinct packages with a one-way access policy, checked against a conforming import; meaningful (would fail on a real upward edge). PASS — see FIX 5a only re: the *seeded-breach* claim, not this rule.
- **No-cycles** — real `slices().matching(...).beFreeOfCycles()`; meaningful cardinal-sin guard. PASS.
- **Coding rule** — real `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`; and crucially it is the rule the *seeded breach* actually trips, with the offending class asserted by name — a real, non-vacuous detection. PASS.
- **Freezing ratchet** — real `FreezingArchRule.freeze(...)` writing an actual baseline store; exercised end-to-end. Meaningful but **assertion-light** (see FIX above). PASS-with-FIX.
- **Seeded breach** — genuinely non-conforming class; its `System.out` breach is detected and asserted. The `..web..`-field half is illustrative but not rule-proven (FIX 5a). PASS-with-FIX.

---

## Learnings & pipeline suggestions

1. **Seeded-breach claims must be test-proven, edge by edge.** When a chapter says "rule X rejects breach Y," CODE-REVIEW should confirm a test actually runs rule X over an import containing Y and asserts the failure. Here the `System.out` edge is proven but the `..web..` edge is only asserted in prose — a class subtle to spot because the breach class is real and one of its two breaches *is* caught. Suggest adding to EXAMPLES-GUIDE: "every breach a snippet/prose names must be the subject of an assertion, not merely present in a sample class."
2. **ArchUnit scope semantics are a fidelity trap.** `consideringOnlyDependenciesInLayers()` + a breach class in an *undeclared* package silently means the layered rule ignores it. Worth a note in the companion conventions: when demonstrating a layered violation, either place it in a declared layer or use `consideringAllDependencies()`, and say which.
3. **Freeze/ratchet tests should assert both halves.** A ratchet test that only asserts the baseline run passes is half a test; the load-bearing property is "new violations still fail." Recommend a standard two-phase ratchet test shape (record baseline → introduce new breach → assert it still fails) for any future freezing example.
4. **No JDK on PATH; wrapper absent in module.** `java_home` finds nothing and the module has no `mvnw`; the reviewer had to point `JAVA_HOME` at `/opt/homebrew/opt/openjdk@21`. The build is reproducible once JAVA_HOME is set, but a committed `mvnw`/toolchain note at the aggregator would make the CODE-REVIEW build step turnkey.
