<!--
Dossier key: 41 (owner, leads) + folds 49 — per 01-index/FINAL_INDEX.md Ch 20
Slug: 41_testing_landscape_quality (owner key 41)
Part / arc position: Part V — Testing, Chapter 20 (OPENS Part V; umbrella over 42-52)
Companion module: 08-companion-code/41_testing_landscape_quality/ (the determinism axis — flaky→deterministic + isolation + smell; coverage-vs-mutation routed to 48, not duplicated) — ✅ EXAMPLE-BUILD = BUILT (green at JDK 21.0.11; mvn -Pquality verify: 15 tests, 0 Checkstyle, 0 SpotBugs). As-built spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (each tool cited to its OWN docs; deep verdicts ROUTED to 42-52; no tool crowned):
- Pyramid (41): Cohn Succeeding with Agile 2009 (origin, 3 layers Unit/Service/UI) + Vocke "The Practical Test Pyramid" martinfowler.com — two rules VERBATIM: "Write tests with different granularity" + "The more high-level you get the fewer tests you should have". Unit="focused, isolated tests of individual code components"; Integration="correctly interacts with external dependencies"; E2E "notoriously flaky and often fail for unexpected and unforeseeable reasons" (verbatim). Ice-cream cone="excessive high-level tests that become maintenance nightmares and run slowly" (verbatim). Solitary vs sociable. "Test observable behaviour, not implementation" (verbatim). Trophy/honeycomb = alternative weightings (crown none).
- Two-axis test quality (41): COVERAGE (execution; JaCoCo 6 counters Instructions/Branches/Lines/Methods/Classes/Complexity verbatim) vs MUTATION SCORE (fault-detection; PITest faults "automatically seeded then your tests are run", killed/lived). PIT VERBATIM: coverage "measures only which code is executed by your tests. It does not check that your tests are actually able to detect faults in the executed code". 100% coverage + 0 assertions kills 0 mutants. Coverage = necessary not sufficient (FOLKLORE GUARD). FIRST (Fast/Isolated/Repeatable/Self-validating/Timely — community heuristic, NOT a spec). Test smells (Meszaros xUnit Test Patterns): Assertion Roulette/Eager Test/Mystery Guest/General Fixture.
- Tool families (landscape, routed): JUnit5 Jupiter (@Test/@ParameterizedTest/@Nested) → Ch21; AssertJ/Hamcrest/Truth → Ch21; Mockito → Ch21; Testcontainers (@Testcontainers/@Container) → Ch22(45); jqwik (@Property/@ForAll, shrinking) → Ch(46); PITest → Ch(47); JaCoCo → Ch(48); REST-assured/Pact → Ch(50); JMH → Ch(51).
- Architecture/flakiness/smells (49): JUnit5 isolation — PER_METHOD default ("new instance of each test class before executing each test method ... in isolation" verbatim) vs PER_CLASS (shares state, reintroduces order risk). Surefire(unit)/Failsafe(integration *IT) = pyramid build expression. @TestMethodOrder + MethodOrderer (MethodName/DisplayName/OrderAnnotation/Random); Random surfaces coupling. Parallel: junit.jupiter.execution.parallel.enabled; @Execution(CONCURRENT|SAME_THREAD); JUnit REFUSES to silently parallelise PER_CLASS/MethodOrderer (verbatim guard); @ResourceLock/@Isolated. Flakiness: Micco/Google "almost 16%" tests flaky + "84%" of pass→fail post-submit transitions are flakes not regressions. Luo et al. 2014 (FSE) TEN root causes (async wait, concurrency, test order dependency, resource leak, network, time, IO, randomness, floating-point, unordered collections) → Java fix per cause (Awaitility await().atMost().until() replaces Thread.sleep; Clock.fixed for time; containsExactlyInAnyOrder for unordered; etc.). Surefire rerunFailingTestsCount (JUnit5 since 3.0.0-M4) + failOnFlakeCount (since 3.0.0-M6) + flakyFailure/flakyError XML = DETECT not cure. assertTimeoutPreemptively. Smells (van Deursen 2001 / Meszaros): Assertion Roulette/Eager Test/Mystery Guest/General Fixture — mostly review-found.
⚠ verify-at-pin: all Part-V tool versions/GAVs; jqwik max-discard-ratio (5); PITest DEFAULTS/STRONGER mutator membership; JaCoCo check has NO built-in default threshold; Awaitility 100ms poll; Surefire 3.0.0-M4/M6; Micco 16%/84% (cite paper not blog); Luo ten-category list; Gruber 59% order-dependency is PYTHON-specific (not Java/universal). ⚠ AHEAD-OF-PIN: JUnit User-Guide 6.x doc-drift (pin = 5.x line); structured concurrency JEP505 preview@25. SOURCE-PIN §7 canon gaps: Cohn 2009 + Meszaros 2007 not pinned rows. FIRST = heuristic not spec.
Routes: JUnit5 depth → Ch21(42); assertions → Ch21(43); mocking/over-mocking/solitary-vs-sociable → Ch21(44); Testcontainers depth → 45; property/jqwik → 46; mutation/PITest cost → 47; coverage/JaCoCo gate + coverage≠quality depth → 48; flakiness quarantine process → CI part(79); concurrency repro/JCStress → Ch14(24); review language → 84; trend dashboards → 88.
DRAFT v1 — gates manual; two-axis-test-quality + pyramid-as-heuristic + cause→determinism-fix-matrix + test-smell-card + umbrella-routing shapes; PART V OPENER. EXAMPLE-BUILD pending.
-->

# How Much vs How Good

*The testing landscape, the pyramid, and the two axes of a test suite — plus architecture, flakiness, and test smells · 41 (folds 49) · Part V (opener)*

> A suite can run every line of the codebase and still catch nothing. Coverage measures how much executes; it says nothing about whether a test would notice if the answer were wrong.

## Hook

A team is proud of its 100% line coverage. Every method runs under test; the badge is green; the coverage gate passes on every commit. Then someone changes a `>=` to a `>` in a discount calculation — a real, money-losing bug — and the entire suite stays green. Not one test fails. How? Because the tests *execute* the discount method (that is what coverage measures) but they never *assert* the discount is right. They call the code and check almost nothing. Run mutation testing against that suite and the truth comes out: it kills 30% of seeded faults. The code is 100% covered and barely tested.

A test suite has **two independent dimensions**. One is *how much* of the system runs under test: granularity and coverage. The other is *how good* each test is at noticing when the answer is wrong: fault detection. A suite can score high on the first and low on the second, and most teams measure only the first because it is the one with a number attached. This opening chapter maps the testing landscape against both axes: the **test pyramid** as the model for *how much* and *where*, **coverage versus mutation score** as the two measurements of *how good*, and (in the second half) **test architecture, flakiness, and test smells**, the disciplines that decide whether the suite's signal can be trusted at all. Each deep decision is handed to the chapter that owns it. The thesis throughout: *a green suite is not the same as a good suite.*

## Overview

**What this chapter covers**

- The **test pyramid** (Cohn/Vocke): granularity layers, the two rules, the ice-cream-cone anti-pattern, and why it is a heuristic rather than a law.
- The **two axes of test quality**: coverage (execution) versus mutation score (fault detection), and why coverage is *necessary, not sufficient*.
- A map of the **Java test-tool families**, each routed to its owning chapter.
- **Test architecture and isolation**, **flakiness** (and the Java fix per root cause), and **test smells** — the quality of the test code itself.

**What this chapter does NOT cover.** The deep mechanics of any single tool: JUnit 5, assertions, and mocking (Chapter 21); Testcontainers, property-based testing, mutation, and coverage gating each get their own chapter later in Part V. This is the landscape and the quality framing; the deep "which assertion library, when to mock, what coverage threshold" verdicts are routed, not made here. Every tool is cited to its own docs; **no tool is crowned**.

One idea carries the whole chapter: *coverage shows what ran; mutation score shows what the tests would catch; determinism and clarity determine whether the result can be trusted. Only the first has a number attached, which is exactly why teams over-trust it.*

## How it works

![Fig 41.1 &mdash; The test pyramid — Two rules (Vocke / Cohn): &ldquo;Write tests with different granularity&rdquo; and
    &ldquo;The more high-level you get the fewer tests you should have.&rdquo;](../../05-figures/41_testing_landscape_quality/fig41_1.png)

*Fig 41.1 &mdash; The test pyramid — Two rules (Vocke / Cohn): &ldquo;Write tests with different granularity&rdquo; and
    &ldquo;The more high-level you get the fewer tests you should have.&rdquo;*

![Fig 41.2 &mdash; Coverage vs. mutation score: two independent axes of test quality — Coverage measures execution; mutation score measures fault detection. They are independent.
    A suite can reach 100% coverage and kill zero mutants.](../../05-figures/41_testing_landscape_quality/fig41_2.png)

*Fig 41.2 &mdash; Coverage vs. mutation score: two independent axes of test quality — Coverage measures execution; mutation score measures fault detection. They are independent.
    A suite can reach 100% coverage and kill zero mutants.*


### The pyramid: how much, and where

The **test pyramid** (Mike Cohn, *Succeeding with Agile*, 2009; restated by Ham Vocke in "The Practical Test Pyramid" on Martin Fowler's site) is the model for the *how much* axis. It rests on two rules, verbatim: *"Write tests with different granularity"* and *"The more high-level you get the fewer tests you should have."* The shape (many tests at the bottom, few at the top) encodes a cost/speed/confidence trade-off across three layers:

- **Unit tests** — *"focused, isolated tests of individual code components with the narrowest scope."* Fast, cheap, plentiful; the base.
- **Integration/service tests** — verify the application *"correctly interacts with external dependencies like databases, filesystems, and separate services."* Slower, fewer.
- **UI / end-to-end tests** — exercise behaviour through the whole stack; the fewest, because they *"carry significant maintenance costs"* and, in Vocke's words, are *"notoriously flaky and often fail for unexpected and unforeseeable reasons."*

The named failure mode is the **ice-cream cone**: the pyramid inverted. *"Excessive high-level tests that become maintenance nightmares and run slowly,"* with few unit tests propping up a fat layer of brittle E2E ones. The pyramid exists to prevent it.

> **CONCEPT** *The pyramid is a heuristic, not a quota.* Vocke's layer names are deliberately fuzzy and the shape is a rule of thumb, not a 70/20/10 law. Alternative weightings (the "testing trophy," the "honeycomb") deliberately put *more* weight on integration tests, arguing that heavy mocking at the unit layer tests mocks rather than behaviour. These are different approaches with different trade-offs (test speed/cost versus realism/confidence); none is crowned. A thin service that is mostly glue to external systems may rationally hold more integration tests than the classic pyramid suggests.

Two of Vocke's framings carry into the rest of Part V. **Solitary versus sociable** unit tests: a solitary test stubs *all* collaborators for isolation; a sociable one lets the unit interact with real collaborators where practical, which is the conceptual root of the mocking debate (Chapter 21). And the antidote to brittle tests: *"Avoid testing implementation details. Test observable behaviour instead, not internal code structure."* A test that asserts *which methods were called* breaks on every refactor; a test that asserts *what the code produced* survives.

### The two axes of test quality

Granularity is the *how much*. The harder, more valuable axis is *how good*, and it has two distinct measurements that teams routinely conflate.

**Coverage** measures *execution*: which instructions, branches, and lines ran while the tests executed. JaCoCo, the standard Java coverage tool, reports six counters (instructions, branches, lines, methods, classes, and cyclomatic complexity), and branch coverage (every `if` and `switch` arm) is a stronger floor than line coverage. Coverage is precise, reproducible, and genuinely useful as a *floor*: code that never runs under test is code whose behaviour is entirely unknown.

**Mutation score** measures *fault detection*. A mutation tool (PITest) automatically seeds small faults into the code under test (flipping a conditional, changing a `+` to a `-`, replacing a return value), then reruns the suite against each mutated version. If a test fails, the mutant is **killed** (the fault was detected); if every test still passes, the mutant **lived** (the suite is blind to that fault). The mutation score is the percentage killed: an empirical answer to whether the tests would notice a bug.

> **CONCEPT** *Coverage is execution; mutation score is detection — and they are independent.* PITest states the gap in its own words: coverage *"measures only which code is executed by your tests. It does not check that your tests are actually able to detect faults in the executed code."* The hook's vanity suite proves it by construction: 100% line coverage with zero meaningful assertions kills *zero* mutants. Coverage is **necessary, not sufficient** — a floor that finds untested code, never proof that the tested code is well tested.

This is the most important debunking in Part V, and it is why a coverage percentage should never be reported as a test-quality number. (The deep treatment of coverage gating lives in its own chapter; mutation cost and how to scope it to changed code, in another. The folklore is corrected here; the mechanics are routed.) Alongside these quantitative measures sits the qualitative heuristic **FIRST** (Fast, Isolated, Repeatable, Self-validating, and Timely): a useful community checklist, not a formal standard.

### The Java test-tool landscape

Part V's tools map onto the two axes, each named here and deepened in its own chapter:

| Family | Concept | Java surface | Owning chapter |
|---|---|---|---|
| Test harness | run & structure tests | **JUnit 5** (`@Test`, `@ParameterizedTest`, `@Nested`, lifecycle) | 21 |
| Assertions | self-validating + readable | **AssertJ** / Hamcrest / Truth | 21 |
| Test doubles | isolate the unit | **Mockito** (`mock`/`when`/`verify`) | 21 |
| Integration | real external deps | **Testcontainers** (`@Testcontainers`/`@Container`) | later |
| Property-based | invariants over many inputs | **jqwik** (`@Property`/`@ForAll`, shrinking) | later |
| Mutation | fault detection | **PITest** | later |
| Coverage | execution | **JaCoCo** (six counters) | later |
| Contract/API | service boundaries | REST-assured / Pact | later |
| Performance | honest benchmarks | **JMH** | later |

In the build, these split along the pyramid: unit tests run under **Maven Surefire** in the `test` phase; integration tests (`*IT`) under **Maven Failsafe** in `verify`. That Surefire/Failsafe split *is* the pyramid expressed in the build.

## Deep dive: test architecture, flakiness, and test smells

Coverage and mutation score show whether the tests *check* enough. Three more disciplines determine whether the suite's signal can be *trusted*: it must be well-architected (isolated), deterministic (not flaky), and clean as code (smell-free).

**Isolation is the determinism foundation.** JUnit 5's default, `@TestInstance(Lifecycle.PER_METHOD)`, *"creates a new instance of each test class before executing each test method to allow individual test methods to be executed in isolation."* A fresh instance per method means instance fields cannot leak state between tests, which prevents order-dependency by construction. `Lifecycle.PER_CLASS` shares one instance across methods (convenient for a non-static `@BeforeAll`) but reintroduces exactly that leak risk; the author now owns isolation. Shared mutable state between tests is the primary engine of order-dependent flakiness, which is why JUnit also offers `MethodOrderer.Random`: running methods in a different order each build makes a hidden inter-test dependency fail visibly instead of passing by luck.

<!-- include: 41_testing_landscape_quality/src/test/java/org/acme/testdiscipline/IsolationOrderTest.java#per-method-isolation -->

Parallel execution is the classic speed-for-determinism-risk trade. JUnit 5 makes it opt-in (`junit.jupiter.execution.parallel.enabled=true`) and **refuses to silently parallelise** the two shapes most likely to break: classes using `PER_CLASS` or a `MethodOrderer` run concurrently only if `@Execution(CONCURRENT)` is added explicitly. For tests that cannot be made independent (system properties, a singleton), `@ResourceLock` serialises access and `@Isolated` runs a class alone.

**Flakiness** is the disease isolation prevents. A flaky test passes or fails non-deterministically for the same code, which hollows out the suite's entire value: a red build no longer reliably means the code is broken. The scale is well documented. Google's CI study (Micco) reports that almost **16%** of Google's tests exhibit some flakiness, and that when a test transitions from pass to fail in post-submit CI, **84%** of the time it is a flake, not a real regression. Luo et al. (FSE 2014) catalogue ten root causes, and each has a concrete Java fix:

| Root cause | Java fix |
|---|---|
| Async wait (`Thread.sleep` and hope) | **Awaitility** `await().atMost(…).until(condition)` — polls and proceeds the instant it holds |
| Test-order dependency | per-method lifecycle (default); hunt with `MethodOrderer.Random` |
| Time (`now()` in an assertion) | inject a `Clock` (`Clock.fixed`) and assert against it |
| Unordered collections (`HashSet` order) | order-independent assertions (AssertJ `containsExactlyInAnyOrder`) |
| Concurrency races | `@Execution`/`@ResourceLock`; reproduce with a concurrency stress harness (Chapter 14) |
| Network / IO | controlled, disposable real services (Testcontainers) |

Three of these fixes are mechanical enough to show. The time fix starts at the seam: the code under test reads the clock that is handed to it, never the wall clock, so a test can pin it.

<!-- include: 41_testing_landscape_quality/src/main/java/org/acme/testdiscipline/ReservationService.java#clock-injection -->

A test then supplies `Clock.fixed`, and the time-dependent assertion is reproducible on every run:

<!-- include: 41_testing_landscape_quality/src/test/java/org/acme/testdiscipline/ClockInjectionTest.java#clock-fixed -->

An unordered result is asserted by membership, not iteration order, so a `HashSet`'s ordering cannot make it flake:

<!-- include: 41_testing_landscape_quality/src/test/java/org/acme/testdiscipline/UnorderedCollectionTest.java#order-independent -->

And an async result is awaited by polling the real condition under a hard budget, not by sleeping a guessed duration (`Awaitility` is the library form of the same idea):

<!-- include: 41_testing_landscape_quality/src/test/java/org/acme/testdiscipline/AsyncWaitTest.java#poll-not-sleep -->

> **CONCEPT** *Detecting a flake is not curing it.* Maven Surefire's `rerunFailingTestsCount` (and `failOnFlakeCount`, which fails the build when too many tests flake) re-runs failures until they pass and records `flakyFailure` entries in the report. That is *detection and quarantine*, not a fix. Auto-retrying a flaky test can **mask a real intermittent bug** in the code under test, the very race the flake was revealing. The discipline is to rerun to *detect and quarantine*, then fix the root cause; never to keep a known-flaky test in the green path indefinitely.

**Test smells** are the last discipline: the code-quality of the test code itself, catalogued by van Deursen et al. (2001) and Meszaros (*xUnit Test Patterns*, 2007). The idea mirrors the production-code smell catalogue (Chapter 12), applied to tests:

- **Assertion Roulette** — many bare assertions in one test, so a failure does not say *which* expectation broke. Fix: a message per assertion, or one logical assertion per test (`assertAll`), which runs every check and reports all failures together:

<!-- include: 41_testing_landscape_quality/src/test/java/org/acme/testdiscipline/AssertionClarityTest.java#assert-all -->
- **Eager Test** — one test verifying too much, checking several behaviours at once. Fix: split into focused tests.
- **Mystery Guest** — the test depends on an external resource (a file, a shared DB row) not visible in the test itself, so cause and effect cannot be traced locally. Fix: inline the fixture, or make the dependency explicit.
- **General Fixture** — a giant shared setup larger than any one test needs. Fix: minimal per-test fixtures.

Most test smells are *review-found*, not tool-gated: a vocabulary for code review (Chapter 84's territory), not automated enforcement. That is the honest boundary. A linter can flag a few patterns, but "is this test testing the right thing?" remains a human judgment.

Put the axes together and "test quality" resolves into four things, only one of which has a number attached: coverage (necessary floor), effectiveness (mutation score, the real measure of detection), determinism (no flakiness), and clarity (smell-free). A suite that is 100% covered but flaky, smell-ridden, and assertion-light is the green badge that lies — the testing equivalent of the skipped gate from the last chapter. Part V is about building the other three.

## Limitations & when NOT to reach for it

- **Coverage % is not test quality.** It measures execution, not detection; a fully covered suite can kill no mutants. When NOT: never set a high coverage gate and conclude "the tests are good"; pair it with mutation testing and review. The number is a floor, not a verdict.
- **The pyramid is a heuristic, not a law.** Do not enforce a literal layer ratio; a glue-heavy service may rationally hold more integration tests. Pushing everything down to the unit layer can become over-mocking that tests doubles rather than behaviour (Chapter 21). Crown neither the pyramid nor a rebalanced shape.
- **Mutation testing is expensive.** PITest reruns the suite per mutant, so a full-project run is slow. When NOT: do not run full mutation on every commit — scope it to changed code (its owning chapter covers how).
- **`PER_CLASS` and parallelism buy convenience/speed with determinism risk.** Shared instances reintroduce order coupling; parallel runs race on shared state. The fixes (`@ResourceLock`/`@Isolated`) exist, but a suite full of locks is barely parallel. Isolate the state first.
- **Rerun-until-green hides bugs.** `rerunFailingTestsCount` is detection, not a cure; using it to keep a flaky test green can mask a genuine intermittent defect.
- **Integration realism costs speed and stability.** Testcontainers needs a Docker runtime, adds startup latency, and its reuse feature is explicitly not for CI. Keep logic tests at the solitary base.
- **Test smells are mostly subjective and review-found.** Do not claim a linter "enforces good tests"; label each smell tool-found versus review-found.
- **Tests show the presence of bugs, never their absence** (Dijkstra). No coverage %, mutation score, or pyramid shape proves correctness; testing complements static analysis, types, and review — it does not replace them.

## Alternatives & adjacent approaches

- **The testing trophy / honeycomb** — alternative suite shapes weighting integration tests more heavily; reasonable when unit-level mocking would test doubles instead of behaviour.
- **Static analysis and types** (Part IV) — catch whole classes of defect without running the code; complementary to, not a substitute for, tests.
- **Property-based testing** (later in Part V) — assert invariants over generated inputs rather than hand-picked examples, with shrinking to a minimal failing case.
- **Contract testing** (REST-assured / Pact) — verify service boundaries without full end-to-end stacks, easing the top of the pyramid.
- **Code review** — the home for the test-quality judgments no tool makes (is this testing the right behaviour? is this smell worth fixing?).

These compose into one program: types and static analysis remove defect classes, the pyramid distributes the tests, coverage finds the untested, mutation measures the detection, isolation and the flakiness fixes keep the signal trustworthy, and review covers the judgment.

## When to use what

- **To decide *where* a behaviour is tested:** the pyramid: unit at the base, integration for real-dependency seams, E2E sparingly.
- **To find untested code:** coverage (JaCoCo), as a necessary floor, never as a quality verdict.
- **To measure whether tests actually detect faults:** mutation score (PITest), scoped to changed code.
- **To keep the suite deterministic:** per-method isolation, `MethodOrderer.Random` to hunt coupling, `Clock.fixed`/seeded RNG/order-independent assertions per flake root cause, Awaitility instead of `Thread.sleep`.
- **To detect (not cure) flakiness:** Surefire rerun + flake reporting. Then fix the root cause.
- **To keep test code clean:** the smell catalogue as a review vocabulary.
- **When the question is "is this testing the right thing?":** code review, not a tool.

## Hand-off to the next chapter

This chapter mapped the landscape and the two axes; the next one builds the base of the pyramid in depth. **Chapter 21** is the unit-testing toolkit: **JUnit 5** as the harness (its lifecycle, parameterized and nested tests), paired with the **assertion libraries** (AssertJ, Hamcrest, Truth) that make a test self-validating and readable instead of an Assertion-Roulette pile of bare checks, and **Mockito** for the test doubles that isolate a unit — including the over-mocking trap this chapter previewed, where a solitary test asserts implementation instead of behaviour. That is where the abstractions here (solitary versus sociable, observable behaviour, the FIRST properties) become concrete code.

## Back matter — sources & traceability

- **Pyramid** — Cohn, *Succeeding with Agile* (2009, origin; ⚠ propose SOURCE-PIN §7 canon row); Vocke, "The Practical Test Pyramid" (`martinfowler.com/articles/practical-test-pyramid.html`): two rules + layer definitions + ice-cream-cone + "E2E notoriously flaky" + solitary-vs-sociable + "test observable behaviour, not implementation" (all verbatim this scan). Trophy/honeycomb named as alternative approaches (crown none).
- **Two axes** — JaCoCo six counters (`jacoco.org/.../counters.html`, verbatim; `check` has NO built-in default threshold ⚠); PITest mutation = faults "automatically seeded … then your tests are run", killed/lived, and the verbatim limitation "coverage … does not check that your tests are actually able to detect faults" (`pitest.org`); coverage = necessary-not-sufficient (folklore guard). FIRST = community heuristic, not a spec. Test smells (Meszaros, *xUnit Test Patterns* 2007; van Deursen et al. 2001 — ⚠ propose §7 canon row): Assertion Roulette / Eager Test / Mystery Guest / General Fixture (verbatim names).
- **Tool families** (identity verified, each cited to its own docs; versions ⚠ @pin): JUnit 5 `org.junit.jupiter:junit-jupiter`; AssertJ/Hamcrest/Truth; Mockito; Testcontainers `@Testcontainers`/`@Container`; jqwik `@Property`/`@ForAll`/shrinking; PITest; JaCoCo; REST-assured/Pact; JMH.
- **Architecture/flakiness/smells** (key 49) — JUnit 5 User Guide: `PER_METHOD` default ("new instance … before executing each test method … in isolation", verbatim) vs `PER_CLASS`; `@TestMethodOrder`/`MethodOrderer.Random`; parallel keys + the verbatim PER_CLASS/MethodOrderer non-parallelise guard; `@ResourceLock`/`@Isolated`; `assertTimeoutPreemptively`. Surefire/Failsafe split; `rerunFailingTestsCount` (JUnit5 since 3.0.0-M4) + `failOnFlakeCount` (since 3.0.0-M6) + `flakyFailure`/`flakyError` (detect, not cure). **Micco/Google CI study**: ~16% flaky; 84% of pass→fail post-submit transitions are flakes (cite the paper `research.google.com/pubs/archive/45880.pdf`, not the blog; ⚠ re-confirm @draft). **Luo et al. (FSE 2014)** ten root-cause categories. Awaitility `await().atMost().until()` (default 100ms poll ⚠). Gruber 59% order-dependency = Python-specific (⚠ not Java/universal).
- **Routing** — JUnit5/assertions/mocking depth → Ch 21 (keys 42/43/44); Testcontainers → later (45); property/jqwik → later (46); mutation/PITest cost → later (47); coverage/JaCoCo gate + coverage≠quality depth → later (48); flakiness quarantine process → CI part (79); concurrency repro → Ch 14 (24); review language → key 84; trend dashboards → key 88. JUnit User-Guide 6.x doc-drift = AHEAD-OF-PIN (pin is 5.x); structured concurrency JEP 505 preview@25 = AHEAD-OF-PIN.

**Companion module (BUILT — green at JDK 21.0.11, Maven 3.9.16; `mvn -B -Pquality verify` = 15 tests, 0 Checkstyle, 0 SpotBugs):** `08-companion-code/41_testing_landscape_quality/` (`testing-landscape-quality`). As the Part V *opener*, this module scopes to the one half of the chapter no later module owns — the **determinism axis** — rather than duplicating the chapters it routes to. The coverage-vs-mutation demonstration the original spec sketched (a `DiscountPolicy` under a vanity suite vs a strong suite, JaCoCo + PITest) is owned by **Chapter 48** (`coverage-mutation-effectiveness`, already built with exactly that `Discount` + weak/strong suites + a PITest profile); jqwik + Testcontainers are owned by **Chapter 45**; the JUnit/AssertJ/Mockito harness by **Chapter 42**. Building those again here would duplicate them and blur the opener's routing. Instead, a small reservation domain is put under tests that realize, one for one, the chapter's flaky-to-deterministic matrix and its test-architecture/smell material: an injected `Clock` + `Clock.fixed` (a `now()` flake made deterministic), an order-independent assertion `containsExactlyInAnyOrder` (a `HashSet` flake made deterministic), per-method isolation with `MethodOrderer.Random` (order-dependency hunted), poll-until-it-holds bounded by `assertTimeoutPreemptively` (the JDK form of Awaitility's async-wait fix), and `assertAll` against Assertion Roulette. **Failure path:** a blank-id / seatless reservation is rejected with a typed `ReservationRejectedException` carrying a stable code, driven by a test (`ReservationServiceTest.FailurePath`). The module adds no dependency of its own (JUnit Jupiter + AssertJ + JDK only), so nothing routed to a later chapter is reintroduced.

**Snippet tags:** `IsolationOrderTest#per-method-isolation`, `ReservationService#clock-injection`, `ClockInjectionTest#clock-fixed`, `UnorderedCollectionTest#order-independent`, `AsyncWaitTest#poll-not-sleep`, `AssertionClarityTest#assert-all` — each a `// tag::…[]` region inside the compiled file, resolved into the prose above by the manuscript's include markers; all ≤ 9 lines (checked by `check_snippets.sh`).

## Next chapter teaser

The base of the pyramid is where most tests live, and it has its own toolkit: a harness to run and structure tests, assertions to make them self-validating and readable, and test doubles to isolate the unit under test. The next chapter goes deep on all three — JUnit 5, the assertion libraries, and Mockito — including the discipline that separates a test that survives a refactor from one that shatters on it: asserting behaviour, not implementation.
