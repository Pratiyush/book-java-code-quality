# The Base of the Pyramid

*JUnit as the harness, assertion libraries that diagnose, and test doubles done with discipline · 42 (folds 43, 44) · Part V*

> Two tests of the same code: one asserts what the code produced and survives a refactor; the other mocks everything, checks call order, and shatters when a private method is renamed. Same coverage. One is an asset.

## Hook

Picture two unit tests of the same `OrderService`. The first arranges an order, calls `place`, and asserts the saved total with clear intent; when it fails it says "expected total 9000 but was 8100." The second mocks every collaborator, verifies the exact sequence of calls, and when it fails says "expected: `<true>` but was: `<false>`." Then a developer renames a private helper and reorders two internal calls — no behaviour change at all. The first test stays green; the second goes red, and its message says nothing. Same code, same coverage. The first test is an asset; the second is a maintenance liability that will eventually be deleted or `@Disabled` in frustration.

That gap between a unit test that helps and one that rots is decided by three small choices. The **harness** (JUnit) that runs and structures the test; the **assertions** that state the expectation, readable when a test fails; and the **test doubles** that isolate the unit from its collaborators. Each has a discipline that separates the asset from the liability: structure that aids reading rather than obscuring it, assertions that *diagnose* rather than merely pass, and doubles used on the right collaborators in the right way rather than everywhere. Most tests live at this base of the pyramid (Chapter 20), so getting these three right is most of what test quality is in practice.

## Overview

**What this chapter covers**

- **JUnit** as the test substrate: its three-module architecture, the Jupiter programming model, and the edition shift (JUnit 6 current, JUnit 5 still everywhere).
- **Assertion libraries** (JUnit's built-ins, AssertJ, Hamcrest, Truth) and the quality axes that distinguish them (crown none).
- **Test doubles and Mockito**: the five-double taxonomy, state versus behaviour verification, Mockito's mechanism and its built-in quality guard, and the contested **classicist-versus-mockist** schools.
- The disciplines throughout: behaviour-not-implementation, specific-not-weak assertions, mock-roles-not-values.

**What this chapter does NOT cover.** Integration testing with real dependencies (Testcontainers), which deliberately uses the *real* collaborator (next chapter). Property-based and parameterized testing in depth (later). The coverage/mutation folklore ("a green mock test proves correctness"), whose depth lives with the effectiveness chapters. Contract testing (Pact). Each tool is cited to its own docs; **no library and no TDD school is crowned**.

**One idea to hold**: *the harness runs the test, the assertion diagnoses the failure, and the double isolates the unit, and each is an asset only when it tracks observable behaviour, not the implementation's internal shape.*

## How it works

Two structures carry the rest of the chapter, and Figure 21.1 draws the first. It shows JUnit's layering: one `TestEngine` API on a shared Platform, with Jupiter and other engines plugging into it. That single API is the reason one runner can execute several kinds of test side by side.

![Figure 21.1 — JUnit Platform: three-module architecture — One TestEngine API lets build tools run Jupiter, jqwik, and others on a single Platform. JUnit 6.1.0 current (min Java 17).](figures/fig42_1.png)

*Figure 21.1 — JUnit Platform: three-module architecture — One TestEngine API lets build tools run Jupiter, jqwik, and others on a single Platform. JUnit 6.1.0 current (min Java 17).*

Figure 21.2 draws the second: the five kinds of test double laid out from inert to behaviour-checking, with the state-verification-versus-behaviour-verification hinge marked at the centre. The sections that follow walk both structures in turn.

![Figure 21.2 — The five test-double taxonomy — Fowler / Meszaros spectrum: Dummy → Mock. The hinge: state verification (stub) vs. behaviour verification (mock).](figures/fig42_2.png)

*Figure 21.2 — The five test-double taxonomy — Fowler / Meszaros spectrum: Dummy → Mock. The hinge: state verification (stub) vs. behaviour verification (mock).*

### JUnit: the substrate everything plugs into

JUnit is the de-facto JVM unit-testing framework, and its quality relevance is twofold. It is the **execution substrate** nearly every other quality tool plugs into: coverage (JaCoCo), mutation (PITest), property testing (jqwik), and Testcontainers all run *on the JUnit Platform*. The *way* tests are written in it is itself a code-quality surface: readable tests get maintained, unreadable ones rot.

> **EDITION** *JUnit 6 is the current major line* (6.0 GA 2025-09-30; 6.1.0 GA 2026-05), raising the floor to Java 17 and unifying Platform, Jupiter, and Vintage under one version, with Vintage (the JUnit 3/4 compatibility engine) now deprecated. **JUnit 5 ("Jupiter")** is the prior, still-ubiquitous line. The Jupiter programming model is largely shared across 5 and 6, so the guidance here holds for both; the few 6-only changes (the Java-17 floor, some relocated APIs) are migration costs a Java-8/11 codebase should weigh before upgrading. Treat JUnit 6 as current, note 5 where teams remain on it — the same edition discipline the book applies to any versioned authority.

That layering is the **three-module architecture**:

- **JUnit Platform** — the foundation that launches test engines on the JVM and defines the `TestEngine` API that build tools and IDEs target. This is *why* one runner can execute Jupiter tests and jqwik property tests side by side: these are different engines on one platform.
- **JUnit Jupiter** — the programming and extension model for writing tests, plus its own `TestEngine`.
- **JUnit Vintage** — a `TestEngine` that runs legacy JUnit 3/4 tests on the Platform (deprecated in 6; migration-only).

The Jupiter model itself is a small, quality-relevant surface: lifecycle annotations (`@Test`, `@BeforeEach`/`@AfterEach`, `@BeforeAll`/`@AfterAll`), `@DisplayName` for readable failure output, `@Nested` to group related cases, `@Tag` for selective CI runs, and `@Disabled` (with a reason). Built-in assertions (`assertEquals`, `assertThrows`, `assertAll` to report several failures at once) cover the basics. **Parameterized tests** (`@ParameterizedTest` with `@ValueSource`/`@CsvSource`/`@MethodSource`/`@EnumSource`) kill the duplicated near-identical tests that tempt people to put loops in test methods. The **extension model** (`@ExtendWith` plus the `Extension` API) is the single hook that Mockito, Testcontainers, and Spring all plug into, having replaced JUnit 4's split between runners and rules.

> **CONCEPT** *The Platform/Engine split is the quality point.* Because every tool targets one `TestEngine` API, the whole test ecosystem composes on a single runner. Choosing JUnit is not choosing one library; it is choosing the substrate the rest of Part V's tools assume.

What a *quality* JUnit test looks like is independent of all that machinery: Arrange-Act-Assert (or Given-When-Then) structure, one logical assertion-concept per test, a descriptive `@DisplayName`, no logic (loops/conditionals) in the test body (that is what `@ParameterizedTest` is for) and the FIRST properties from Chapter 20. The three blocks read as one intent — arrange the inputs, act once, assert the outcome:

```java
        // Arrange
        when(catalog.priceOf("widget")).thenReturn(Optional.of(WIDGET_PRICE));
        // Act
        Receipt receipt = service.place("order-1", List.of(new LineItem("widget", 2)));
        // Assert
        assertThat(receipt.total()).isEqualTo(new Money(5_000L, "USD"));
```

The honest limit: JUnit *runs* tests; it does not make them good. A green JUnit suite can be assertion-light, slow, flaky, or testing a mock. The framework is necessary, not sufficient. The next two sections address what it leaves to the author.

### Assertions: the line that is read when a test fails

An assertion is where a test states its expectation, and it is the most-read line when the test fails. A weak assertion turns a failure into a debugging session; a specific one turns it into a diagnosis. `assertTrue(order.getTotal().equals(expected))` fails with "expected `<true>` but was `<false>`," which is useless. The same check expressed well names both values. Java offers four approaches, and the right framing is a set of trade-offs a team weighs, not a ranking:

| Approach | Shape | Strength | Cost |
|---|---|---|---|
| **JUnit built-in** | `assertEquals(a,b)`, `assertThrows`, `assertAll` | zero extra dependency; ships with JUnit | basic failure messages |
| **AssertJ** | fluent `assertThat(x).isEqualTo(…).contains(…)` | IDE-discoverable via autocomplete; rich type-specific assertions; `assertThatThrownBy`; `SoftAssertions` | one more dependency; breadth can tempt over-assertion |
| **Hamcrest** | matcher `assertThat(x, is(equalTo(…)))` | composable matchers (`allOf`, `hasItem`); declarative | matcher names must be known/imported; quieter development today |
| **Truth** (Google) | fluent `assertThat(x).isEqualTo(…)` | deliberately small, consistent API; readable failure messages | smaller surface may lack a niche assertion |

The axes that actually differentiate them (the right thing to compare, each against the library's own docs) are **failure-message quality**, **discoverability** (a fluent chain autocompletes; a matcher name must be remembered), **type-safety**, **domain extensibility** (custom assertions for domain types), and **dependency weight**. None of these crowns a winner; a team picks one *primary* library for consistency and lives with the trade-off. The same equality fact, stated three ways, shows how little the styles differ on a passing line and how much the choice is about the failure line:

```java
        // JUnit built-in: zero extra dependency, basic failure message.
        assertEquals(5_000L, TOTAL.minorUnits());
        // AssertJ: fluent, IDE-discoverable after assertThat(.
        org.assertj.core.api.Assertions.assertThat(TOTAL.minorUnits()).isEqualTo(5_000L);
        // Hamcrest: matcher composition, reads declaratively.
        assertThat(TOTAL.minorUnits(), is(equalTo(5_000L)));
```

The honest limit cuts across all four: **a library does not fix a weak assertion.** `assertThat(list).isNotNull()` is exactly as empty as `assertNotNull(list)` — readability tooling cannot supply an expectation the author did not write. And the opposite failure is real too: AssertJ's breadth can tempt over-specific assertions that break on benign change, which is itself a test smell (Chapter 20). The discipline is to assert the *behaviour that matters*, specifically, and nothing more.

### Test doubles: isolating the unit, and the discipline of doing it

A **test double** is a stand-in for a real collaborator, so a unit can be exercised in isolation: fast, deterministic, without the collaborator's cost (a database, the network, the clock, randomness). Fowler, following Meszaros, names **five kinds** along a spectrum from inert to behaviour-checking, and the vocabulary matters because teams use these words loosely:

- **Dummy** — "passed around but never actually used … just used to fill parameter lists."
- **Fake** — "working implementations, but … take some shortcut which makes them not suitable for production" (an in-memory repository).
- **Stub** — "provide canned answers to calls made during the test."
- **Spy** — "stubs that also record some information based on how they were called."
- **Mock** — "objects pre-programmed with expectations which form a specification of the calls they are expected to receive."

> **CONCEPT** *State verification versus behaviour verification.* A **stub** answers questions — the test checks the *state* of the system after the method runs (state verification). A **mock** asserts an *interaction* — that the unit made the right calls on its collaborator (behaviour verification). "Mock objects always use behaviour verification, while a stub can go either way." This is the hinge of the whole topic, because behaviour verification couples a test to *how* the code collaborates, so it breaks on a refactor even when the behaviour is unchanged (the second test from the hook).

**Mockito** is the Java library that creates these at runtime. In JUnit, `@ExtendWith(MockitoExtension.class)` initializes `@Mock`, `@Spy`, `@Captor`, and `@InjectMocks` fields before each test (replacing the old `openMocks` call):

```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private PriceCatalog catalog;   // a query port → stubbed
    @Mock private PaymentGateway gateway; // a command port → verified

    @InjectMocks private OrderService service; // the unit under test, wired with the mocks
```
 Stubbing uses `when(mock.query()).thenReturn(value)` (and the `do*` family, `doReturn`/`doThrow`/`doNothing`, for void methods and spies), and verification uses `verify(gateway).charge(eq(id), any())`, `verify(mock, times(2))`, `never()`, or an `InOrder` checker. Argument matchers carry a classic pitfall: if any argument uses a matcher, *all* must (mix raw values with `eq(...)`), or Mockito throws `InvalidUseOfMatchersException`. An unstubbed method returns type-appropriate empties (`0`/`false`/`null`/empty collection/`Optional.empty()`), which is why a stub-only test rarely NPEs. (Since Mockito 5.0.0 the inline mock maker is the default, so final classes, static methods, and constructors are mockable from `mockito-core` with no opt-in; `mockStatic`/`mockConstruction` are a sharp edge that usually signals a dependency that should be injected instead.)

> **CONCEPT** *Strict stubbing is a built-in quality guard.* `MockitoExtension` defaults to `Strictness.STRICT_STUBS`: an unused stub fails the test (`UnnecessaryStubbingException`), and a stubbed call whose arguments never match raises `PotentialStubbingProblem`. A dead stub usually means the test or the code has drifted, and strict stubbing makes that *fail the build* instead of rotting silently — Mockito's own answer to the over-mocking smell.

## Deep dive: the two schools, and when not to mock

The hardest question about doubles is not *how* but *whether*. The field genuinely disagrees. Two schools of TDD use the same platform differently, and the book crowns neither.

The **classicist** school (sometimes "Chicago" or "Detroit") "uses real objects if possible and a double if it's awkward to use the real thing," favouring **state verification** with doubles only at awkward boundaries (DB, network, clock). Its strongest case: tests assert outcomes, so they survive refactoring of internals, with few brittle interaction assertions. Its hardest objection: for collaboration-heavy designs the real object graph can be large and slow to assemble, and a failure points less precisely at the offending unit.

The **mockist** school ("London") "will always use a mock for any object with interesting behaviour," favouring **behaviour verification** and outside-in design, pairing with Freeman and Pryce's "mock roles, not objects." Its strongest case: it drives clean roles and interfaces and pinpoints exactly which collaboration failed; the tests document the protocol between objects. Its hardest objection: behaviour verification couples tests to the implementation's call structure, so these tests are brittle under refactoring — and over-applied, it produces what DHH named "test-induced design damage," indirection added solely to make code mockable.

> **CONCEPT** *The choice follows the collaborator, not the school.* The useful decision is not "which school applies" but "what is this collaborator?": **value objects** (`record`s, immutable data) are used real, never mocked (mocking them is a recognized smell); **pure queries** read cleanest as a **stub** (state-style); **commands** whose side effect is the point are legitimately a **mock** with `verify`; **third-party types the team does not own** get a thin **adapter** (owned, mocked, following "mock types you own"), or a vendor-provided **fake**. The two-schools debate is, in practice, a debate about how often collaborators fall into each bucket.

A query collaborator answers a question, so it reads cleanest as a stub and the test checks the resulting state:

```java
        // PriceCatalog answers a question, so stub it and assert the resulting state.
        when(catalog.priceOf("widget")).thenReturn(Optional.of(WIDGET_PRICE));

        Receipt receipt = service.place("order-1", List.of(new LineItem("widget", 3)));

        assertThat(receipt.total().minorUnits()).isEqualTo(7_500L);
```

A command collaborator's side effect is the point, so the interaction with it is verified — behaviour verification, with the coupling cost the next paragraph names:

```java
        // PaymentGateway's side effect is the point, so verify the interaction happened.
        verify(gateway).charge(eq("order-9"), any(Money.class));
```

A value object is not a collaborator to double at all; it is data, used real:

```java
        // Money is a value object: construct it directly. Mocking it would prove nothing.
        Money price = new Money(1_000L, "USD");
        when(catalog.priceOf("widget")).thenReturn(Optional.of(price));

        Receipt receipt = service.place("order-1", List.of(new LineItem("widget", 4)));

        assertThat(receipt.total()).isEqualTo(new Money(4_000L, "USD"));
```

This frames the honest centre of the whole topic. **A green mock test does not prove the code works.** A mock returns exactly what it was told to; if the *assumption* about the real collaborator is wrong, the unit test passes while the integration fails. Mocking the wrong contract is invisible until an integration test (next chapter) or a contract test runs. Mock-heavy unit-test counts are necessary, not sufficient, evidence of correctness (the effectiveness chapters carry the depth). A double is a *model* of the real collaborator, and the test is only as good as the model. So the when-NOT list is concrete: do not mock value objects, do not mock the class under test, do not mock pure logic that can be called directly, and do not reach for a mock where realism is the point (persistence, SQL, serialization). That is the real-collaborator job of the next layer.

Put the three together and the asset-versus-liability gap from the hook resolves into three habits: structure tests so they read (JUnit), assert the behaviour that matters specifically (assertions), and isolate with the right double on the right collaborator (Mockito), always tracking observable behaviour rather than the implementation's internal shape. A test built that way survives the refactor; a test built the other way is the one that gets deleted.

## Limitations & when NOT to reach for it

- **JUnit runs tests; it does not make them good.** A green suite can be assertion-light, slow, flaky, or testing a mock — necessary, not sufficient. And JUnit 6 raises the floor to Java 17 and relocates some APIs; a Java-8/11 codebase has a real migration cost (Vintage is deprecated, so 3/4 tails need genuine migration). When NOT to upgrade yet: an older-JDK codebase with a large JUnit 4 tail.
- **Over-granular structure hurts readability.** Deep `@Nested` trees and heavy parameterization are tools, not goals; structure that obscures the test is a net negative.
- **A library does not fix a weak assertion.** `isNotNull()` asserts nothing about behaviour; the most-read line on failure is only as good as the expectation written into it. Mixing assertion libraries in one codebase hurts consistency; pick one primary.
- **Behaviour verification couples tests to implementation.** A `verify`-heavy test breaks on a refactor that changes call structure without changing behaviour; strict stubbing catches dead stubs but cannot fix an over-specified interaction test. The over-mock smell, pinned and passing, is the liability — an `InOrder` check that fixes the internal call sequence rather than the outcome:

```java
        // Brittle: this asserts HOW the unit collaborates, not WHAT it produced. A refactor that
        // reorders these internal calls breaks the test though behaviour is unchanged.
        InOrder ordered = inOrder(catalog, gateway);
        ordered.verify(catalog).priceOf("widget");
        ordered.verify(gateway).charge(eq("order-1"), any(Money.class));
```

- **A green mock test is not proof of correctness.** Mocks encode the test author's assumptions about a collaborator, not its real behaviour; the wrong assumption passes the unit test and fails in production until integration/contract tests run.
- **Static/constructor mocking is a sharp edge.** `mockStatic`/`mockConstruction` exist but are scope/thread-bound and usually signal a dependency that should be injected; reserve them for un-injectable legacy seams.
- **Doubles cannot find integration defects** (wrong SQL, serialization, wiring) by construction. That is the higher pyramid layers' job. Mocking is isolation, not proof of correctness.

## Alternatives & adjacent approaches

- **Real collaborators / integration tests** (next chapter, Testcontainers): when the interaction *is* the behaviour under test, use the real dependency instead of a double.
- **Fakes over mocks**: a hand-written or vendor in-memory fake (a `Map`-backed repository) often reads cleaner than a pile of stubs for a collaborator called many ways.
- **Parameterized and property-based testing** (later): replace duplicated example tests and assert invariants over generated inputs.
- **Contract testing** (Pact): verify that the assumption about a service boundary matches the provider's reality — the antidote to "mocked the wrong contract."
- **The built-in assertions alone**: for simple value checks, JUnit's `assertEquals`/`assertThrows` need no extra dependency; add a fluent library where it pays.

These layer rather than compete: built-ins for the simple case, a fluent library where readability pays, doubles for isolation at owned boundaries, and real collaborators where realism is the point.

## When to use what

- **Harness:** JUnit (Jupiter), version 6 for new work, version 5 where the codebase already is; `@ParameterizedTest` instead of loops, `@Nested`/`@DisplayName` for readable structure, `@ExtendWith` for tool integration.
- **Assertions:** one primary library team-wide: AssertJ for fluent breadth, Truth for small/readable, Hamcrest for composable matchers, built-ins for zero-dependency simple checks. Assert behaviour specifically; never `isNotNull` as the whole test.
- **A query collaborator:** a **stub** (state verification).
- **A command collaborator whose side effect matters:** a **mock** with `verify`.
- **A value object / `record`:** the real instance — never a mock.
- **A third-party type:** a thin **owned adapter**, mocked, or a vendor **fake**.
- **When realism is the point** (persistence, SQL, wiring): a real collaborator in an integration test — not a mock.

## Hand-off to the next chapter

The doubles in this chapter buy isolation by *replacing* the real collaborator — exactly right at the base of the pyramid, and exactly wrong when the collaborator's real behaviour is under test. A mock of a repository proves the code calls `save`; it proves nothing about whether the SQL is valid, the schema matches, or serialization round-trips. The next chapter moves up a layer to **integration testing with real dependencies** (Testcontainers spinning up a real database or broker in a disposable container), where the test uses the genuine collaborator instead of a model of it, trading speed for the realism that catches the defects doubles cannot see. This is the deliberate complement to everything here: mock at owned boundaries, integrate where realism is the point.

## Back matter — sources & traceability

- **JUnit** (`docs.junit.org`; `github.com/junit-team/junit-framework`): JUnit 6 current (6.0 GA 2025-09-30, 6.1.0 GA 2026-05; min Java 17; Platform/Jupiter/Vintage one version; Vintage deprecated — version/min-Java verified at SOURCE-PIN 2026-06-27, ⚠ full 5→6 breaking-change list verify-at-pin); three-module architecture (Platform `TestEngine` API / Jupiter / Vintage); Jupiter model (`@Test`/lifecycle/`@DisplayName`/`@Nested`/`@Tag`/`@Disabled`; `assertEquals`/`assertThrows`/`assertAll`; `@ParameterizedTest` + `@ValueSource`/`@CsvSource`/`@MethodSource`/`@EnumSource`; `@ExtendWith` + `Extension`); GAV `org.junit.jupiter:junit-jupiter` / `junit-bom`; Surefire `useJUnitPlatform`. Patterns AAA/GWT, FIRST.
- **Assertions** (⚠ crown none) — JUnit built-in `Assertions` (`docs.junit.org`); AssertJ fluent `assertThat().isEqualTo/.contains/.hasSize`, `assertThatThrownBy`, `SoftAssertions` (`assertj.github.io`, `org.assertj:assertj-core`); Hamcrest matcher `assertThat(x, is(equalTo()))`, `allOf`/`hasItem` (`hamcrest.org`, `org.hamcrest:hamcrest`); Truth fluent + readable messages (`truth.dev`, `com.google.truth:truth`). Axes: failure-message quality / discoverability / type-safety / domain extensibility / dependency weight. *(API identity + versions verified — AssertJ 3.27.7, Hamcrest 3.0, Truth 1.4.5 at SOURCE-PIN 2026-06-27; built-in/AssertJ/Hamcrest compiled green in the companion, Truth prose-only; ⚠ Truth design-goal verbatim still verify-at-pin.)*
- **Doubles/Mockito** (⚠ + contested practice) — five doubles + state-vs-behaviour verbatim from Fowler, *Mocks Aren't Stubs* (`martinfowler.com/articles/mocksArentStubs.html`, crediting Meszaros *xUnit Test Patterns* 2007). Mockito (`site.mockito.org`; Javadoc `javadoc.io/doc/org.mockito/mockito-core`): `@ExtendWith(MockitoExtension.class)`+`@Mock`/`@InjectMocks`/`@Spy`/`@Captor`; `when().thenReturn/thenThrow/thenAnswer`; `do*().when()`; `BDDMockito given/willReturn`; `verify(times/never/atLeast/atMost)`/`verifyNoMoreInteractions`/`InOrder`; `ArgumentMatchers` all-or-none (`InvalidUseOfMatchersException`); `ArgumentCaptor`; `RETURNS_DEFAULTS`; `Strictness` LENIENT/WARN/STRICT_STUBS (`MockitoExtension` default STRICT_STUBS → `UnnecessaryStubbingException`/`PotentialStubbingProblem`); `mockStatic`/`mockConstruction` (inline maker, scoped); 5.0.0 inline-maker default. GAV `mockito-core`/`mockito-junit-jupiter` (`mockito-inline` legacy). Two schools: classicist (state) vs mockist (behaviour; Freeman&Pryce "mock roles, not objects"/"mock types you own"; DHH "test-induced design damage" attributed). *(API identity + version 5.23.0 verified at SOURCE-PIN 2026-06-27; inline-maker-5.0.0-default boundary, `MockitoExtension` default `STRICT_STUBS`, and the `@ExtendWith`+`@Mock`/`@InjectMocks`/`when().thenReturn`/`do*().when()`/`verify`/`never`/`verifyNoInteractions`/`InOrder`/`eq`/`any` idioms compiled green in the companion. ⚠ still verify-at-pin: Mockito Java floor (11), per-version standalone (non-extension) strictness default, `@InjectMocks` precedence wording, `RETURNS_DEFAULTS` class name, `mockStatic`/`mockConstruction` scope wording.)*
- **Routing** — use-real-collaborator/Testcontainers → next chapter (45); property/parameterized depth → later (46); "green mock test ≠ correctness" / coverage folklore depth → effectiveness chapters (47/48); over-mocking-as-smell + flakiness → Ch 20 (49); contract testing (Pact) → later (50); legacy `mockStatic` seams → key 92; readability principle → Ch 2 (03). SOURCE-PIN gaps: Meszaros 2007 + Freeman&Pryce 2009 not §7 rows; EasyMock/JMockit/Spock not pinned (crown none; cite-own-source or cut).

## Next chapter teaser

A mock proves the code *calls* the repository; it says nothing about whether the SQL is valid or the schema matches. The next chapter moves up the pyramid to integration testing, where the test uses a *real* database or message broker (spun up in a disposable container with Testcontainers) instead of a model of one. That is where the defects surface that doubles, by construction, cannot find: the wrong query, the broken mapping, the serialization that does not round-trip.
