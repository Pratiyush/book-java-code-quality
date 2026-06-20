<!--
Dossier key: 42 (owner, leads) + folds 43 + 44 — per 01-index/FINAL_INDEX.md Ch 21
Slug: 42_unit_testing_assertions_mocking (owner key 42)
Part / arc position: Part V — Testing, Chapter 21 (Part V opened at Ch 20)
Companion module: 08-companion-code/ (a JUnit harness + 4 assertion styles + the right-double-for-the-job + over-mock failure path) — ⚠ EXAMPLE-BUILD = PENDING (toolchain READY: JDK 21.0.11+25.0.3). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (each tool/library cited to its OWN docs; comparison rows ⚠ crown none):
- JUnit (42): EDITION — JUnit 6 current (6.0 GA 2025-09-30; 6.1.0 GA 2026-05; min Java 17; Platform/Jupiter/Vintage share one version; Vintage DEPRECATED); JUnit 5 "Jupiter" prior + still ubiquitous; Jupiter programming model largely shared 5→6 (call out 6-only changes). Three-module architecture: Platform (TestEngine API — build tools/IDEs target it; why one runner runs Jupiter+jqwik), Jupiter (programming + extension model + its engine), Vintage (3/4 on Platform, deprecated). Model: @Test/@BeforeEach/@AfterEach/@BeforeAll/@AfterAll/@DisplayName/@Nested/@Tag/@Disabled; built-in Assertions (assertEquals/assertThrows/assertAll); @ParameterizedTest (junit-jupiter-params; @ValueSource/@CsvSource/@MethodSource/@EnumSource); @ExtendWith + Extension API = single hook replacing JUnit4 runners+rules (Mockito/Testcontainers/Spring use it). GAV org.junit.jupiter:junit-jupiter (api+params+engine) or junit-bom. Run via maven-surefire useJUnitPlatform. Patterns: AAA/GWT; one assertion-concept/test; @DisplayName; no logic in tests; FIRST.
- Assertions (43, ⚠): JUnit built-in (assertEquals/assertThrows/assertAll — zero dep, basic messages, soft via assertAll); AssertJ (fluent assertThat(x).isEqualTo/.contains/.hasSize; IDE-discoverable; assertThatThrownBy; SoftAssertions; org.assertj:assertj-core); Hamcrest (matcher assertThat(x, is(equalTo())); compose allOf/hasItem; org.hamcrest:hamcrest); Truth (Google fluent; readable failure messages; small consistent API; com.google.truth:truth). Neutral axes: failure-message quality / discoverability / type-safety / domain extensibility / dependency weight. Crown none.
- Mocking/doubles (44, ⚠ + contested practice): five doubles (Fowler/Meszaros, VERBATIM) — Dummy "passed around but never actually used", Fake "working implementations, but ... not suitable for production", Stub "canned answers", Spy "stubs that also record ... how they were called", Mock "pre-programmed with expectations which form a specification of the calls". State vs behavior verification (mock=behavior, stub=either; spy=stub+behavior). Mockito: @ExtendWith(MockitoExtension.class)+@Mock/@InjectMocks/@Spy/@Captor; when().thenReturn/thenThrow/thenAnswer; do*().when() (void/spy); BDDMockito given/willReturn; verify(times/never/atLeast/atMost)/verifyNoMoreInteractions/InOrder; ArgumentMatchers any/anyString/eq/argThat (ALL-OR-NONE rule → InvalidUseOfMatchersException); ArgumentCaptor; RETURNS_DEFAULTS (0/false/null/empty/Optional.empty); Strictness LENIENT/WARN/STRICT_STUBS (MockitoExtension default STRICT_STUBS → UnnecessaryStubbingException/PotentialStubbingProblem); mockStatic→MockedStatic/mockConstruction→MockedConstruction (inline maker, scoped, sharp edge — inject instead); Mockito 5.0.0 inline maker DEFAULT (final/static/ctor from core, no opt-in). GAV mockito-core/mockito-junit-jupiter; mockito-inline LEGACY. Two schools (crown neither): classicist/Chicago (real objects, state verification; refactor-resilient; objection big slow graphs) vs mockist/London (doubles+behavior; drives roles, pinpoints; objection couples-to-implementation, "test-induced design damage" DHH attributed); when-each by collaborator: value→real(don't mock), query→stub, command→mock, un-owned→owned adapter ("mock types you own"; Freeman&Pryce).
⚠ verify-at-pin: all GAVs/versions; JUnit6 patch + 5→6 breaking-change list + min-Java-17; whether @MethodSource/Assertions sigs changed in 6; Mockito version + inline-maker 5.0.0 boundary + Java floor (11) + per-version standalone strictness default + @InjectMocks precedence wording + RETURNS_DEFAULTS class name + mockStatic scope; AssertJ/Hamcrest/Truth versions; Truth design-goal verbatim. ⚠ AHEAD-OF-PIN: none material (JUnit 6 IS current). SOURCE-PIN gaps: Meszaros xUnit Test Patterns 2007 + Freeman&Pryce GOOS 2009 not §7 rows; EasyMock/JMockit/Spock not pinned (crown none, cite-own-source or cut).
Routes: Testcontainers/use-real-collaborator → Ch22(45); property/parameterized depth → 46; mutation/coverage folklore depth ("green mock test ≠ correctness") → 47/48; over-mocking-as-smell + flakiness → Ch20(49); contract testing (mock-wrong-contract→Pact) → 50; legacy mockStatic seams → 92; readability principle → Ch2(03).
DRAFT v1 — gates manual; three-module-architecture + four-options-neutral-axes + five-doubles-spectrum + two-schools + state-vs-behavior + edition-discipline shapes; EXAMPLE-BUILD pending.
-->

# The Base of the Pyramid

*JUnit as the harness, assertion libraries that diagnose, and test doubles done with discipline · 42 (folds 43, 44) · Part V*

> Two tests of the same code: one asserts what the code produced and survives a refactor; the other mocks everything, checks call order, and shatters when you rename a private method. Same coverage. One is an asset.

## Hook

Picture two unit tests of the same `OrderService`. The first arranges an order, calls `place`, and asserts the saved total — clear intent, and when it fails it says "expected total 9000 but was 8100." The second mocks every collaborator, verifies the exact sequence of calls, and when it fails says "expected: `<true>` but was: `<false>`." Then a developer renames a private helper and reorders two internal calls — no behaviour change at all. The first test stays green; the second goes red, and its message tells you nothing. Same code, same coverage. The first test is an asset; the second is a maintenance liability that will eventually be deleted or `@Disabled` in frustration.

That gap — between a unit test that helps and one that rots — is decided by three small choices, and this chapter is about all three. The **harness** (JUnit) that runs and structures the test; the **assertions** that state what you expect, and that you read when it fails; and the **test doubles** that isolate the unit from its collaborators. Each has a discipline that separates the asset from the liability: structure that aids reading rather than obscuring it, assertions that *diagnose* rather than merely pass, and doubles used on the right collaborators in the right way rather than everywhere. Most of your tests live at this base of the pyramid (Chapter 20), so getting these three right is most of what test quality is in practice.

## Overview

**What this chapter covers**

- **JUnit** as the test substrate: its three-module architecture, the Jupiter programming model, and the edition shift (JUnit 6 current, JUnit 5 still everywhere).
- **Assertion libraries** — JUnit's built-ins, AssertJ, Hamcrest, Truth — and the quality axes that distinguish them (crown none).
- **Test doubles and Mockito**: the five-double taxonomy, state versus behaviour verification, Mockito's mechanism and its built-in quality guard, and the contested **classicist-versus-mockist** schools.
- The disciplines throughout: behaviour-not-implementation, specific-not-weak assertions, mock-roles-not-values.

**What this chapter does NOT cover.** Integration testing with real dependencies — Testcontainers — which deliberately uses the *real* collaborator (next chapter). Property-based and parameterized testing in depth (later). The coverage/mutation folklore — "a green mock test proves correctness" — whose depth lives with the effectiveness chapters. Contract testing (Pact). Each tool is cited to its own docs; **no library and no TDD school is crowned**.

**If you hold one idea**, hold this: *the harness runs the test, the assertion diagnoses the failure, and the double isolates the unit — and each is an asset only when it tracks observable behaviour, not the implementation's internal shape.*

## How it works

### JUnit: the substrate everything plugs into

JUnit is the de-facto JVM unit-testing framework, and its quality relevance is twofold. It is the **execution substrate** nearly every other quality tool plugs into — coverage (JaCoCo), mutation (PITest), property testing (jqwik), and Testcontainers all run *on the JUnit Platform* — and the *way* tests are written in it is itself a code-quality surface: readable tests get maintained, unreadable ones rot.

> **EDITION** *JUnit 6 is the current major line* (6.0 GA 2025-09-30; 6.1.0 GA 2026-05), raising the floor to Java 17 and unifying Platform, Jupiter, and Vintage under one version, with Vintage (the JUnit 3/4 compatibility engine) now deprecated. **JUnit 5 ("Jupiter")** is the prior, still-ubiquitous line. The Jupiter programming model is largely shared across 5 and 6, so the guidance here holds for both; the few 6-only changes (the Java-17 floor, some relocated APIs) are migration costs a Java-8/11 codebase should weigh before upgrading. Treat JUnit 6 as current, note 5 where teams remain on it — the same edition discipline the book applies to any versioned authority.

The load-bearing mental model is the **three-module architecture**:

- **JUnit Platform** — the foundation that launches test engines on the JVM and defines the `TestEngine` API that build tools and IDEs target. This is *why* one runner can execute Jupiter tests and jqwik property tests side by side: they're different engines on one platform.
- **JUnit Jupiter** — the programming and extension model for writing tests, plus its own `TestEngine`.
- **JUnit Vintage** — a `TestEngine` that runs legacy JUnit 3/4 tests on the Platform (deprecated in 6; migration-only).

The Jupiter model itself is a small, quality-relevant surface: lifecycle annotations (`@Test`, `@BeforeEach`/`@AfterEach`, `@BeforeAll`/`@AfterAll`), `@DisplayName` for readable failure output, `@Nested` to group related cases, `@Tag` for selective CI runs, and `@Disabled` (with a reason). Built-in assertions (`assertEquals`, `assertThrows`, `assertAll` to report several failures at once) cover the basics. **Parameterized tests** (`@ParameterizedTest` with `@ValueSource`/`@CsvSource`/`@MethodSource`/`@EnumSource`) kill the duplicated near-identical tests that tempt people to put loops in test methods. And the **extension model** — `@ExtendWith` plus the `Extension` API — is the single hook that Mockito, Testcontainers, and Spring all plug into, having replaced JUnit 4's split between runners and rules.

> **CONCEPT** *The Platform/Engine split is the quality point.* Because every tool targets one `TestEngine` API, the whole test ecosystem composes on a single runner. Choosing JUnit isn't choosing one library; it's choosing the substrate the rest of Part V's tools assume.

What a *quality* JUnit test looks like is independent of all that machinery: Arrange-Act-Assert (or Given-When-Then) structure, one logical assertion-concept per test, a descriptive `@DisplayName`, no logic (loops/conditionals) in the test body — that's what `@ParameterizedTest` is for — and the FIRST properties from Chapter 20. The honest limit: JUnit *runs* tests; it does not make them good. A green JUnit suite can be assertion-light, slow, flaky, or testing a mock. The framework is necessary, not sufficient — which is what the next two sections address.

### Assertions: the line you read when it fails

An assertion is where a test states its expectation, and it is the most-read line when the test fails. A weak assertion turns a failure into a debugging session; a specific one turns it into a diagnosis. `assertTrue(order.getTotal().equals(expected))` fails with "expected `<true>` but was `<false>`" — useless. The same check expressed well names both values. Java offers four approaches, and the right framing is a set of trade-offs a team weighs, not a ranking:

| Approach | Shape | Strength | Cost |
|---|---|---|---|
| **JUnit built-in** | `assertEquals(a,b)`, `assertThrows`, `assertAll` | zero extra dependency; ships with JUnit | basic failure messages |
| **AssertJ** | fluent `assertThat(x).isEqualTo(…).contains(…)` | IDE-discoverable via autocomplete; rich type-specific assertions; `assertThatThrownBy`; `SoftAssertions` | one more dependency; breadth can tempt over-assertion |
| **Hamcrest** | matcher `assertThat(x, is(equalTo(…)))` | composable matchers (`allOf`, `hasItem`); declarative | matcher names must be known/imported; quieter development today |
| **Truth** (Google) | fluent `assertThat(x).isEqualTo(…)` | deliberately small, consistent API; readable failure messages | smaller surface may lack a niche assertion |

The axes that actually differentiate them — and the right thing to compare, each against the library's own docs — are **failure-message quality**, **discoverability** (a fluent chain autocompletes; a matcher name must be remembered), **type-safety**, **domain extensibility** (custom assertions for your types), and **dependency weight**. None of these crowns a winner; a team picks one *primary* library for consistency and lives with the trade-off.

The honest limit cuts across all four: **a library does not fix a weak assertion.** `assertThat(list).isNotNull()` is exactly as empty as `assertNotNull(list)` — readability tooling cannot supply an expectation the author didn't write. And the opposite failure is real too: AssertJ's breadth can tempt over-specific assertions that break on benign change, which is itself a test smell (Chapter 20). The discipline is to assert the *behaviour that matters*, specifically, and nothing more.

### Test doubles: isolating the unit, and the discipline of doing it

A **test double** is a stand-in for a real collaborator, so a unit can be exercised in isolation — fast, deterministic, without the collaborator's cost (a database, the network, the clock, randomness). Fowler, following Meszaros, names **five kinds** along a spectrum from inert to behaviour-checking, and the vocabulary matters because teams use these words loosely:

- **Dummy** — "passed around but never actually used … just used to fill parameter lists."
- **Fake** — "working implementations, but … take some shortcut which makes them not suitable for production" (an in-memory repository).
- **Stub** — "provide canned answers to calls made during the test."
- **Spy** — "stubs that also record some information based on how they were called."
- **Mock** — "objects pre-programmed with expectations which form a specification of the calls they are expected to receive."

> **CONCEPT** *State verification versus behaviour verification.* A **stub** answers questions — you check the *state* of the system after the method runs (state verification). A **mock** asserts an *interaction* — that the unit made the right calls on its collaborator (behaviour verification). "Mock objects always use behaviour verification, while a stub can go either way." This is the hinge of the whole topic, because behaviour verification couples a test to *how* the code collaborates, so it breaks on a refactor even when the behaviour is unchanged — the second test from the hook.

**Mockito** is the Java library that creates these at runtime. In JUnit, `@ExtendWith(MockitoExtension.class)` initializes `@Mock`, `@Spy`, `@Captor`, and `@InjectMocks` fields before each test (replacing the old `openMocks` call). You *stub* with `when(mock.query()).thenReturn(value)` (and the `do*` family — `doReturn`/`doThrow`/`doNothing` — for void methods and spies), and *verify* with `verify(gateway).charge(eq(id), any())`, `verify(mock, times(2))`, `never()`, or an `InOrder` checker. Argument matchers carry a classic pitfall: if any argument uses a matcher, *all* must (mix raw values with `eq(...)`), or Mockito throws `InvalidUseOfMatchersException`. An unstubbed method returns type-appropriate empties (`0`/`false`/`null`/empty collection/`Optional.empty()`), which is why a stub-only test rarely NPEs. (Since Mockito 5.0.0 the inline mock maker is the default, so final classes, static methods, and constructors are mockable from `mockito-core` with no opt-in — though `mockStatic`/`mockConstruction` are a sharp edge usually signalling you should *inject* a collaborator instead.)

> **CONCEPT** *Strict stubbing is a built-in quality guard.* `MockitoExtension` defaults to `Strictness.STRICT_STUBS`: an unused stub fails the test (`UnnecessaryStubbingException`), and a stubbed call whose arguments never match raises `PotentialStubbingProblem`. A dead stub usually means the test or the code has drifted, and strict stubbing makes that *fail the build* instead of rotting silently — Mockito's own answer to the over-mocking smell.

## Deep dive: the two schools, and when not to mock

The hardest question about doubles isn't *how* but *whether* — and here the field genuinely disagrees. Two schools of TDD use the same platform differently, and the book crowns neither.

The **classicist** school (sometimes "Chicago" or "Detroit") "uses real objects if possible and a double if it's awkward to use the real thing," favouring **state verification** with doubles only at awkward boundaries (DB, network, clock). Its strongest case: tests assert outcomes, so they survive refactoring of internals, with few brittle interaction assertions. Its hardest objection: for collaboration-heavy designs the real object graph can be large and slow to assemble, and a failure points less precisely at the offending unit.

The **mockist** school ("London") "will always use a mock for any object with interesting behaviour," favouring **behaviour verification** and outside-in design, pairing with Freeman and Pryce's "mock roles, not objects." Its strongest case: it drives clean roles and interfaces and pinpoints exactly which collaboration failed; the tests document the protocol between objects. Its hardest objection: behaviour verification couples tests to the implementation's call structure, so they're brittle under refactoring — and over-applied, it produces what DHH named "test-induced design damage," indirection added solely to make code mockable.

> **CONCEPT** *The choice follows the collaborator, not the school.* The useful decision isn't "which school am I" but "what is this collaborator?" — **value objects** (`record`s, immutable data) are used real, never mocked (mocking them is a recognized smell); **pure queries** read cleanest as a **stub** (state-style); **commands** whose side effect is the point are legitimately a **mock** with `verify`; **third-party types you don't own** get a thin **adapter you own** that you mock ("mock types you own"), or a vendor-provided **fake**. The two-schools debate is, in practice, a debate about how often your collaborators fall into each bucket.

This frames the honest centre of the whole topic. **A green mock test does not prove the code works.** A mock returns exactly what you told it to; if your *assumption* about the real collaborator is wrong, the unit test passes while the integration fails. Mocking the wrong contract is invisible until an integration test (next chapter) or a contract test runs — which is why mock-heavy unit-test counts are necessary, not sufficient, evidence of correctness (the effectiveness chapters carry the depth). A double is a *model* of the real collaborator, and the test is only as good as the model. So the when-NOT list is concrete: don't mock value objects, don't mock the class under test, don't mock pure logic you can just call, and don't reach for a mock where realism is the point (persistence, SQL, serialization) — that's the real-collaborator job of the next layer.

Put the three together and the asset-versus-liability gap from the hook resolves into three habits: structure tests so they read (JUnit), assert the behaviour that matters specifically (assertions), and isolate with the right double on the right collaborator (Mockito), always tracking observable behaviour rather than the implementation's internal shape. A test built that way survives the refactor; a test built the other way is the one that gets deleted.

## Limitations & when NOT to reach for it

- **JUnit runs tests; it doesn't make them good.** A green suite can be assertion-light, slow, flaky, or testing a mock — necessary, not sufficient. And JUnit 6 raises the floor to Java 17 and relocates some APIs; a Java-8/11 codebase has a real migration cost (Vintage is deprecated, so 3/4 tails need genuine migration). When NOT to upgrade yet: an older-JDK codebase with a large JUnit 4 tail.
- **Over-granular structure hurts readability.** Deep `@Nested` trees and heavy parameterization are tools, not goals; structure that obscures the test is a net negative.
- **A library doesn't fix a weak assertion.** `isNotNull()` asserts nothing about behaviour; the most-read line on failure is only as good as the expectation written into it. Mixing assertion libraries in one codebase hurts consistency — pick one primary.
- **Behaviour verification couples tests to implementation.** A `verify`-heavy test breaks on a refactor that changes call structure without changing behaviour; strict stubbing catches dead stubs but can't fix an over-specified interaction test.
- **A green mock test isn't proof of correctness.** Mocks encode your assumptions about a collaborator, not its real behaviour; the wrong assumption passes the unit test and fails in production until integration/contract tests run.
- **Static/constructor mocking is a sharp edge.** `mockStatic`/`mockConstruction` exist but are scope/thread-bound and usually signal a dependency that should be injected; reserve them for un-injectable legacy seams.
- **Doubles can't find integration defects** (wrong SQL, serialization, wiring) by construction — that's the higher pyramid layers' job. Mocking is isolation, not a proof of correctness.

## Alternatives & adjacent approaches

- **Real collaborators / integration tests** (next chapter, Testcontainers): when the interaction *is* the behaviour under test, use the real dependency instead of a double.
- **Fakes over mocks**: a hand-written or vendor in-memory fake (a `Map`-backed repository) often reads cleaner than a pile of stubs for a collaborator you call many ways.
- **Parameterized and property-based testing** (later): replace duplicated example tests and assert invariants over generated inputs.
- **Contract testing** (Pact): verify that your assumption about a service boundary matches the provider's reality — the antidote to "mocked the wrong contract."
- **The built-in assertions alone**: for simple value checks, JUnit's `assertEquals`/`assertThrows` need no extra dependency; add a fluent library where it pays.

These layer rather than compete: built-ins for the simple case, a fluent library where readability pays, doubles for isolation at owned boundaries, and real collaborators where realism is the point.

## When to use what

- **Harness:** JUnit (Jupiter) — 6 for new work, 5 where the codebase already is; `@ParameterizedTest` instead of loops, `@Nested`/`@DisplayName` for readable structure, `@ExtendWith` for tool integration.
- **Assertions:** one primary library team-wide — AssertJ for fluent breadth, Truth for small/readable, Hamcrest for composable matchers, built-ins for zero-dependency simple checks. Assert behaviour specifically; never `isNotNull` as the whole test.
- **A query collaborator:** a **stub** (state verification).
- **A command collaborator whose side effect matters:** a **mock** with `verify`.
- **A value object / `record`:** the real instance — never a mock.
- **A third-party type:** a thin **owned adapter** you mock, or a vendor **fake**.
- **When realism is the point** (persistence, SQL, wiring): a real collaborator in an integration test — not a mock.

## Hand-off to the next chapter

The doubles in this chapter buy isolation by *replacing* the real collaborator — which is exactly right at the base of the pyramid, and exactly wrong when the collaborator's real behaviour is what you need to test. A mock of a repository proves your code calls `save`; it proves nothing about whether your SQL is valid, your schema matches, or your serialization round-trips. The next chapter moves up a layer to **integration testing with real dependencies** — Testcontainers spinning up a real database or broker in a disposable container — where the test uses the genuine collaborator instead of a model of it, trading speed for the realism that catches the defects doubles can't see. It's the deliberate complement to everything here: mock at the boundary you own, integrate where realism is the point.

## Back matter — sources & traceability

- **JUnit** (`docs.junit.org`; `github.com/junit-team/junit-framework`): JUnit 6 current (6.0 GA 2025-09-30, 6.1.0 GA 2026-05; min Java 17; Platform/Jupiter/Vintage one version; Vintage deprecated — ⚠ exact patch + 5→6 change list + min-Java @pin); three-module architecture (Platform `TestEngine` API / Jupiter / Vintage); Jupiter model (`@Test`/lifecycle/`@DisplayName`/`@Nested`/`@Tag`/`@Disabled`; `assertEquals`/`assertThrows`/`assertAll`; `@ParameterizedTest` + `@ValueSource`/`@CsvSource`/`@MethodSource`/`@EnumSource`; `@ExtendWith` + `Extension`); GAV `org.junit.jupiter:junit-jupiter` / `junit-bom`; Surefire `useJUnitPlatform`. Patterns AAA/GWT, FIRST.
- **Assertions** (⚠ crown none) — JUnit built-in `Assertions` (`docs.junit.org`); AssertJ fluent `assertThat().isEqualTo/.contains/.hasSize`, `assertThatThrownBy`, `SoftAssertions` (`assertj.github.io`, `org.assertj:assertj-core`); Hamcrest matcher `assertThat(x, is(equalTo()))`, `allOf`/`hasItem` (`hamcrest.org`, `org.hamcrest:hamcrest`); Truth fluent + readable messages (`truth.dev`, `com.google.truth:truth`). Axes: failure-message quality / discoverability / type-safety / domain extensibility / dependency weight. *(API identity verified; versions ⚠ @pin; Truth design-goal verbatim ⚠ @pin.)*
- **Doubles/Mockito** (⚠ + contested practice) — five doubles + state-vs-behaviour verbatim from Fowler, *Mocks Aren't Stubs* (`martinfowler.com/articles/mocksArentStubs.html`, crediting Meszaros *xUnit Test Patterns* 2007). Mockito (`site.mockito.org`; Javadoc `javadoc.io/doc/org.mockito/mockito-core`): `@ExtendWith(MockitoExtension.class)`+`@Mock`/`@InjectMocks`/`@Spy`/`@Captor`; `when().thenReturn/thenThrow/thenAnswer`; `do*().when()`; `BDDMockito given/willReturn`; `verify(times/never/atLeast/atMost)`/`verifyNoMoreInteractions`/`InOrder`; `ArgumentMatchers` all-or-none (`InvalidUseOfMatchersException`); `ArgumentCaptor`; `RETURNS_DEFAULTS`; `Strictness` LENIENT/WARN/STRICT_STUBS (`MockitoExtension` default STRICT_STUBS → `UnnecessaryStubbingException`/`PotentialStubbingProblem`); `mockStatic`/`mockConstruction` (inline maker, scoped); 5.0.0 inline-maker default. GAV `mockito-core`/`mockito-junit-jupiter` (`mockito-inline` legacy). Two schools: classicist (state) vs mockist (behaviour; Freeman&Pryce "mock roles, not objects"/"mock types you own"; DHH "test-induced design damage" attributed). *(API identity verified; version/inline-maker-5.0.0 boundary/Java-floor-11/per-version standalone strictness/`@InjectMocks` precedence/`RETURNS_DEFAULTS` class name/`mockStatic` scope ⚠ @pin.)*
- **Routing** — use-real-collaborator/Testcontainers → next chapter (45); property/parameterized depth → later (46); "green mock test ≠ correctness" / coverage folklore depth → effectiveness chapters (47/48); over-mocking-as-smell + flakiness → Ch 20 (49); contract testing (Pact) → later (50); legacy `mockStatic` seams → key 92; readability principle → Ch 2 (03). SOURCE-PIN gaps: Meszaros 2007 + Freeman&Pryce 2009 not §7 rows; EasyMock/JMockit/Spock not pinned (crown none; cite-own-source or cut).

**Companion module (spec — ⚠ EXAMPLE-BUILD = PENDING; toolchain READY):** `08-companion-code/42_unit_testing_assertions_mocking/` — a JUnit 6/Jupiter harness over an `OrderService` showing AAA, `@DisplayName`, `@Nested`, `@ParameterizedTest`, `assertThrows`/`assertAll`; the *same* failing assertion in all four styles (built-in/AssertJ/Hamcrest/Truth) with failure messages side by side; and the right-double-for-the-job: a **stub** of a query `PriceCatalog`, a **verify** of a command `PaymentGateway`, and a `Money` `record` used **real** (not mocked), under `@ExtendWith(MockitoExtension.class)` with default `STRICT_STUBS`. **Failure path:** an over-mocked variant adds a dead stub → `UnnecessaryStubbingException` fails the build; and an over-specified `InOrder`/`verify` test breaks after an internal-call-order refactor with behaviour unchanged — the demonstrated cost of behaviour-over-verification. This module seeds the shared test harness reused by later Part V chapters.

## Next chapter teaser

A mock proves your code *calls* the repository; it says nothing about whether the SQL is valid or the schema matches. The next chapter moves up the pyramid to integration testing, where the test uses a *real* database or message broker — spun up in a disposable container with Testcontainers — instead of a model of one. It's where you find the defects that doubles, by construction, never can: the wrong query, the broken mapping, the serialization that doesn't round-trip.
