# Chapter 42 — The Base of the Pyramid (`unit-testing-assertions-mocking`)

A small order/pricing service plus a JUnit Jupiter harness that puts the chapter's three disciplines
into one buildable module: the **harness** (JUnit), the **assertions** that diagnose, and the **test
doubles** used on the right collaborator. It is a child module of the companion-code reactor; it sets
`<parent>`, carries no version literal of its own, and inherits the runtime and the JUnit/AssertJ
pins from the aggregator. Mockito and Hamcrest are added as explicit test-scope dependencies because
the aggregator does not manage them — each version literal traces to `SOURCE-PIN.md` §3.

## What it demonstrates

| Discipline | Where in the code | Tag |
|---|---|---|
| The harness: `@ExtendWith(MockitoExtension.class)` + `@Mock`/`@InjectMocks` wiring | `OrderServiceTest` | `mockito-setup` |
| The harness: Arrange-Act-Assert with `@DisplayName` | `OrderServiceTest` | `aaa-structure` |
| A **query** collaborator → a **stub** (state verification) | `OrderServiceTest` | `stub-a-query` |
| A **command** collaborator → a **verify** (behaviour verification) | `OrderServiceTest` | `verify-a-command` |
| A **value object** → used **real**, never mocked | `OrderServiceTest` | `value-not-mocked` |
| The same fact in three assertion styles (built-in / AssertJ / Hamcrest) | `AssertionStylesTest` | `four-assertion-styles` |
| The over-mock smell: an over-specified `InOrder` check (brittle, green) | `OverMockedOrderServiceTest` | `over-mock-smell` |

The collaborators are deliberately different kinds. `PriceCatalog` is a query, so the tests stub it
and assert the resulting state. `PaymentGateway` is a command whose side effect is the point, so the
tests verify the interaction. `Money`, `LineItem` and `Receipt` are value objects, used real — mocking
them would replace verifiable data with a stand-in that proves nothing.

## The four assertion styles

The chapter contrasts four assertion approaches; three are compiled in `AssertionStylesTest`:

- **JUnit built-in** (`assertEquals` / `assertThrows` / `assertAll`): zero extra dependency, basic
  failure messages, soft assertions via `assertAll`.
- **AssertJ** (`assertThat(x).isEqualTo(...)`): fluent, IDE-discoverable, `assertThatThrownBy` for
  exceptions; the aggregator manages its version (3.27.7).
- **Hamcrest** (`assertThat(x, is(equalTo(...)))`): matcher composition, declarative; pinned here
  (3.0).
- **Truth** (`assertThat(x).isEqualTo(...)`, Google): a deliberately small, consistent API focused on
  readable failure messages. It is shown here in prose rather than compiled, because it is outside
  this module's authorized dependency set and pulls a heavier transitive tree; the chapter's
  comparison table covers all four. None of the four is crowned — a team picks one primary library and
  lives with the trade-off.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. Running
under JDK 21 prints a Mockito self-attach advisory (the inline mock maker attaches its Byte Buddy
agent at runtime); it is informational, not a build warning, and a production build would add the
agent explicitly per Mockito's documentation.

## The failure path

The explicit failure path is shown two ways. In `OrderServiceTest`, the `FailurePath` group rejects an
empty order and an unknown SKU with a typed `OrderRejectedException` carrying a stable reason code,
and asserts that a rejected order never reaches the payment gateway (`verifyNoInteractions`); a
declined charge surfaces the gateway's typed `PaymentDeclinedException`.

In `OverMockedOrderServiceTest`, the over-mock anti-pattern is shown as a passing-but-brittle test: an
`InOrder` check that pins the internal call order rather than the outcome. It runs green today, but a
refactor that reorders those internal calls would break it though nothing observable changed — the
chapter's hook made concrete. The other over-mock smell, a dead stub, is documented in the same class:
adding a `when(...)` the production code never calls makes `STRICT_STUBS` (the `MockitoExtension`
default) fail that test with `UnnecessaryStubbingException`. The dead stub is kept out of the running
test so the build stays green; the failure it would cause is Mockito's built-in over-mocking guard.

## Observability surface

`placedCount()` and `rejectedCount()` expose running counts of placed and contract-rejected orders,
and `isReady()` is a readiness probe over the two wired collaborator ports — small, illustrative seams
the later observability chapter builds on. The Surefire test report is the module's other
observability surface; by design the unit tests log nothing real, which is itself the teaching
boundary between a unit test and the integration tests of the next chapter.
