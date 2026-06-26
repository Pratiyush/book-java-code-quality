# Chapter 41 — How Much vs How Good (`testing-landscape-quality`)

The opener of Part V maps the testing landscape against two axes — *how much* of the system runs under
test (the pyramid, coverage) and *how good* the tests are at noticing a wrong answer (fault detection) —
and a second half on whether the suite's signal can be **trusted** at all: test architecture, flakiness,
and test smells.

Because this is the umbrella chapter, the deep mechanics of the individual tools belong to the chapters
that follow, and this module deliberately does **not** rebuild them:

- the JUnit / AssertJ / Mockito harness is **Chapter 42** (`unit-testing-assertions-mocking`);
- integration, property-based testing, and Testcontainers are **Chapter 45** (`integration-property-based-testing`);
- coverage and mutation (JaCoCo / PITest), including the vanity-suite-vs-strong-suite demonstration, are
  **Chapter 48** (`coverage-mutation-effectiveness`).

What this module makes tactile is the one half no later chapter owns: the **determinism axis**. The same
small reservation domain is put under tests that each realize one fix from the chapter's flaky-to-
deterministic root-cause matrix, plus the test-architecture and test-smell material from the chapter's
second half. Every API used is pinned JUnit Jupiter, AssertJ, or the JDK — no tool routed to a later
chapter is reintroduced, and the module adds no dependency of its own.

It is a child module of the companion-code reactor: it sets `<parent>`, carries no version literal of
its own, and inherits the runtime and the JUnit/AssertJ pins from the aggregator (`SOURCE-PIN.md`:
anchor JDK 21, JUnit 6.x line, AssertJ 3.27.7).

## What it demonstrates

| Idea (from the chapter) | Where in the code | Tag |
|---|---|---|
| Inject a `Clock` so a `now()` decision is reproducible | `ReservationService` | `clock-injection` |
| A time-dependent assertion pinned with `Clock.fixed` | `ClockInjectionTest` | `clock-fixed` |
| An unordered (`Set`) result checked with `containsExactlyInAnyOrder` | `UnorderedCollectionTest` | `order-independent` |
| Per-method isolation + `MethodOrderer.Random` to hunt coupling | `IsolationOrderTest` | `per-method-isolation` |
| Await an async result by polling, not `Thread.sleep` (bounded by `assertTimeoutPreemptively`) | `AsyncWaitTest` | `poll-not-sleep` |
| Assertion Roulette vs `assertAll` (the test-smell catalogue) | `AssertionClarityTest` | `assert-all` |

The flaky-to-deterministic fixes map onto the chapter's matrix one for one:

| Root cause (chapter matrix) | This module's deterministic fix |
|---|---|
| Time (`now()` in an assertion) | inject a `Clock` and assert against `Clock.fixed(...)` (`ClockInjectionTest`) |
| Unordered collections (`HashSet` order) | order-independent assertion `containsExactlyInAnyOrder` (`UnorderedCollectionTest`) |
| Test-order dependency | per-method lifecycle (default), hunted with `MethodOrderer.Random` (`IsolationOrderTest`) |
| Async wait (`Thread.sleep` and hope) | poll the real condition under a timeout budget (`AsyncWaitTest`) |

The async-wait row is shown JDK-only: it is the principle behind **Awaitility** (`await().atMost(...).until(...)`),
which the chapter names as the library form and which is owned by a later chapter — so the dependency is
not added here, only the idea it embodies.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports the tests passing and, under `-Pquality`, zero Checkstyle violations and zero
SpotBugs findings.

## The failure path

The explicit failure path (`ReservationServiceTest.FailurePath`) rejects a blank id and a seatless
reservation with a typed `ReservationRejectedException` carrying a stable, branchable reason code, and
asserts that a rejected reservation is never counted as confirmed; a negative hold window is rejected by
the expiry check. This is the chapter's honest-limitations floor shown in code, not prose: a real error
path that a test drives.

## Externalized configuration

The behaviour a deployment would change — the seat-hold window and the async poll budget — lives in
`src/main/resources/config.properties`, not in Java, with `%dev` and `%prod` profiles in the book's
`%profile.key` convention (req. 2 of the examples guide). `ReservationConfig` reads the profile selected
by the `reservation.profile` system property, and `ReservationServiceTest.ConfigProfiles` exercises both
profiles.

## Observability surface

`ReservationService` exposes `confirmedCount()` and `rejectedCount()` as running counts and `isReady()`
as a readiness probe over its wired clock — small, illustrative seams the later observability chapter
builds on. The Surefire test report is the module's other observability surface; by design these tests
log nothing real, which is itself the teaching boundary between a unit test and an integration test.

## Scope note

Coverage and mutation gates (JaCoCo, PITest) are intentionally **not** configured in this module: they
are the subject of Chapter 48, whose module shows them on a behaviour-rich method. Adding them here would
duplicate that chapter and blur the routing the opener sets up. This module's contribution is the
determinism half of the chapter's thesis — *a green suite is not the same as a good suite* — made
runnable.
