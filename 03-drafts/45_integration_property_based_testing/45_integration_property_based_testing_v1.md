<!--
Dossier key: 45 (owner, leads) + folds 46 — per 01-index/FINAL_INDEX.md Ch 22
Slug: 45_integration_property_based_testing (owner key 45)
Part / arc position: Part V — Testing, Chapter 22 (Part V = Ch 20-24)
Companion module: 08-companion-code/45_integration_property_based_testing/ — EXAMPLE-BUILD = BUILT GREEN (`mvn -B -Pquality verify` = BUILD SUCCESS at JDK 21.0.11; see _EXAMPLE.md). As-built (never-invent): integration is a real in-JVM HTTP collaborator on an ephemeral port; the property layer is a seeded JDK generator + JDK shrinker feeding a JUnit `@ParameterizedTest`. The chapter's named tools — Testcontainers (Docker-gated) and jqwik (maintenance-mode, off the build's pinned dep set) — are cited in prose and flagged cited-not-built in 09-flags/. Spec at foot.
Verified against SOURCE-PIN: 2026-06-20 (re-confirmed against the 2026-06-27 correction — the corrected Spotless/JaCoCo rows are not cited by this chapter; jqwik 1.10.1 + Testcontainers 2.0.5 unchanged). Sources (each tool cited to its OWN docs; ⚠ property-based = approaches, crown none):
- Integration/Testcontainers (45): unit isolates a class; integration checks units together vs REAL collaborators (DB/broker/HTTP). Problem: mocks (Ch21) drift from reality; in-memory substitute (H2 for Postgres) tests a DIFFERENT system. Testcontainers = real dependency in throwaway Docker container; production fidelity + CI-runnable. GenericContainer / module containers (PostgreSQLContainer, KafkaContainer). JUnit5: @Testcontainers(class) + @Container(field) — instance field = per-test; static field = per-class (shared, faster). Module ecosystem (DB/Kafka/RabbitMQ/Elasticsearch/LocalStack/Selenium). Reusable containers (opt-in; NOT for CI per Ch20) + Ryuk sidecar cleanup. HARD dep: working Docker runtime on dev + CI. Test slices (Spring @DataJpaTest, Quarkus @QuarkusTest) cited neutrally as framework features. Fixture quality (build via real API/factories; minimal/intention-revealing). Pyramid middle (Ch20). GAV org.testcontainers:testcontainers + :junit-jupiter + module (e.g. :postgresql), test scope.
- Parameterized/PBT (46, ⚠): example tests check cases you THOUGHT OF. Parameterized (JUnit Jupiter): @ParameterizedTest + @ValueSource/@CsvSource/@EnumSource/@MethodSource/@ArgumentsSource — same body many inputs, you still pick them, kills duplication. PBT (jqwik): @Property + @ForAll → framework GENERATES inputs; Arbitraries/@Provide describe domain; SHRINKING reduces failure to minimal counterexample; own TestEngine on JUnit Platform (composes); @BeforeProperty/@AfterProperty; jqwik-testcontainers integration. Where each fits: parameterized=known finite/table-driven; PBT=invariants/round-trips parse(format(x))==x / commutativity / idempotence / never-throws-on-valid; fuzzing (Jazzer/JQF) adjacent, security-oriented → Part VIII. GAV net.jqwik:jqwik test scope; junit-jupiter-params ships with JUnit.
✓ verified at pin (SOURCE-PIN §3, 2026-06-27): Testcontainers 2.0.5 + jqwik 1.10.1 versions; GAVs org.testcontainers:testcontainers/:junit-jupiter/:postgresql + net.jqwik:jqwik; jqwik MAINTENANCE MODE (recorded in SOURCE-PIN as ⚠ maintenance mode). ✓ verified by build: junit-jupiter-params @ParameterizedTest/@ValueSource/@CsvSource/@MethodSource exist+compile green under JUnit 6 (BOM 6.0.3, module CatalogIntegrationTest/PriceListParameterizedTest/SkuPropertyTest). ⚠ verify-at-pin (prose-only tool-doc atoms, NOT in SOURCE-PIN and NOT exercised by the build — fetch each tool's pinned docs): @Testcontainers/@Container per-test-vs-static semantics; reuse/Ryuk flags; jqwik default @Property tries count + Arbitraries/@Provide API; @EnumSource/@ArgumentsSource (named but not exercised). ⚠ TOOL-VITALITY: jqwik in MAINTENANCE MODE (2026, per SOURCE-PIN §3) — state plainly, adoption-risk, crown none. DOCKER-GATING resolved: Testcontainers NOT built (would redden the Docker-less baseline); integration realized in-JVM; flagged cited-not-built in 09-flags/45_testcontainers_docker_gated_not_built.md.
Routes: pyramid/landscape → Ch20(41); mock-vs-real trade-off → Ch21(44); flakiness (container startup/seed-pinning) → Ch20(49); CI cost/time → CI part(79); coverage/mutation effectiveness → Ch23(47/48); fuzzing/security depth → Part VIII(70); contract testing → Ch24(50).
DRAFT v1 — gates manual; fidelity-vs-cost + inputs-you-pick-vs-generated-ladder + tool-vitality-caveat + docker-gating shapes; EXAMPLE-BUILD = BUILT GREEN (see _EXAMPLE.md).
-->

# The Database That Does Not Exist in Production

*Integration testing against real dependencies, and the inputs no example imagined · Part V*

> A unit test is only as good as the doubles it exercises against and the inputs it was given. Two techniques close exactly those two blind spots.

## Hook

A repository test suite is green. Every persistence method is covered, every test passes, all against an in-memory H2 database standing in for Postgres because H2 is fast and needs no setup. The code ships, and the first real query against the production Postgres throws a syntax error: H2 had silently accepted SQL that Postgres rejects, or returned a different result for a `JSONB` filter, or ordered nulls the other way. The suite was green because it validated a database *that does not exist in production*. Next to it, a parser has forty example tests (every case the author thought of) and it crashes the first time a user types an emoji into a name field, because no example covered a *supplementary-plane* Unicode code point (one of the characters, emoji among them, that sit above the common range and need a surrogate pair to encode). Both suites passed everything asked of them. Both had a blind spot baked in.

Those are the two blind spots this chapter closes, and they are the two limits of the unit tests from the last chapter. A unit test is only as good as **the doubles it exercises against** (mock the wrong contract and the test passes while production fails) and only as good as **the inputs it received** (test the cases that come to mind, and the unexamined one is the one that breaks). **Integration testing** with real dependencies closes the first: instead of a model of the database, use a real one, spun up in a disposable container. **Property-based testing** closes the second: instead of hand-picked inputs, assert an invariant and let the framework generate hundreds of inputs, including the edge cases no enumeration would reach. Both climb and widen the pyramid (Chapter 20), and both trade something (speed, determinism) for coverage of a gap that unit tests structurally cannot reach.

## Overview

**What this chapter covers**

- **Integration testing** with Testcontainers: running a real database or broker in a throwaway Docker container, the JUnit 5 lifecycle, and the fidelity-versus-cost trade.
- **Parameterized testing** (JUnit Jupiter): one test body over many supplied inputs.
- **Property-based testing** (jqwik): generated inputs, invariants, and shrinking to a minimal counterexample, with its honest adoption caveat.
- Where each fits on the pyramid, and the costs each adds.

**What this chapter does NOT cover.** Unit-level mocking and the mock-versus-real decision (Chapter 21). Coverage and mutation (measuring test *effectiveness*, next chapter). Contract testing, the antidote to "mocked the wrong contract" (later). Fuzzing for security robustness (Part VIII). Flakiness mechanics in depth and CI cost (Chapter 20 / the CI part). Each tool is cited to its own docs; the property-based approaches are presented as **trade-offs, crowning none**.

**The core idea:** *a unit test validates the system as modeled with the inputs chosen; integration testing replaces the model with the real thing, and property-based testing replaces those chosen inputs with generated ones, each closing a blind spot the unit layer cannot.*

## How it works

The two blind spots sit on two independent axes, and Figure 22.1 places them side by side: fidelity (how real the collaborator under test is) on one, and input coverage (how much of the input space the test actually reaches) on the other. A unit test sits low on both; integration testing raises the first axis and property-based testing widens the second.

![Figure 22.1 — Two blind-spot axes: fidelity &times; input coverage — A unit test has two structural limits — the collaborator it models, and the inputs it chose.
    Integration testing and property-based testing each close one.](../../05-figures/45_integration_property_based_testing/fig45_1.png)

*Figure 22.1 — Two blind-spot axes: fidelity &times; input coverage — A unit test has two structural limits — the collaborator it models, and the inputs it chose.
    Integration testing and property-based testing each close one.*


### Integration testing: use the real collaborator

A unit test isolates one class; an **integration test** checks that units work together against their **real collaborators**: a database, a message broker, an HTTP dependency. The quality problem it solves is fidelity. Mocks (Chapter 21) can drift from the real collaborator's behaviour, and an in-memory substitute like H2-for-Postgres tests a *different system* than production: a different SQL dialect, different null ordering, different type coercion, different transaction semantics. The bugs that live in those differences are invisible to a suite that never touches the real engine.

**Testcontainers** closes the gap by running the real dependency in a throwaway Docker container for the duration of the test. The test gets production fidelity (the actual Postgres, the actual Kafka) while remaining automated and CI-runnable. The model is small:

- A **container** (a `GenericContainer`, or a module-specific one like `PostgreSQLContainer` or `KafkaContainer`) is started before the test and torn down after; the test connects to the host and mapped port the library exposes.
- **JUnit 5 integration**: `@Testcontainers` on the class plus `@Container` on a field manages the lifecycle. An *instance* field gives a fresh container per test method (maximum isolation); a `static` field shares one container across the class (faster).
- A broad **module ecosystem** ships pre-configured containers for common dependencies (databases, Kafka/RabbitMQ, Elasticsearch, LocalStack, Selenium), and a **Ryuk** sidecar cleans up orphaned containers if a run dies.

> **CONCEPT** *Fidelity versus cost.* A real container catches dialect, driver, and behaviour bugs an in-memory stand-in hides, and gives each run a clean, hermetic dependency with no shared stateful staging database to corrupt. The cost is real too: it needs a working Docker runtime on every dev machine and CI runner, and image pull plus container start adds seconds per run. The decision is per-test. Pay the container's cost where the *interaction with the real dependency is the thing under test*; do not pay it for pure in-process logic a unit test already covers.

This is the middle of the pyramid made trustworthy. Integration tests sit between the many fast unit tests and the few slow end-to-end ones, and Testcontainers is what lets that middle layer test against reality without a shared, mutable staging environment that every team fights over. (Framework "test slices" such as Spring's `@DataJpaTest` and Quarkus's `@QuarkusTest` boot only a subset of the app for faster integration tests; these are useful framework features, cited here neutrally, not endorsements.) The fixture discipline matters as much as the container: build test data through the real API or factories rather than hand-rolled SQL, and keep fixtures minimal and intention-revealing, or the integration test grows its own smells (Chapter 20).

The companion module shows the shape of an integration test against a real collaborator. To stay buildable without a Docker runtime, the real dependency here is a real HTTP catalog service booted in the same JVM on an ephemeral port, and the test drives it through a real client over the wire — so the test exercises the encoding, the status mapping, and the parse a mocked client would skip (a real container is the higher-fidelity option the costs below weigh):

<!-- include: 45_integration_property_based_testing/src/test/java/org/acme/catalog/CatalogIntegrationTest.java#integration-roundtrip -->

### Parameterized testing: the same test over many inputs

Example-based tests check the cases the author thought of. The cheapest step beyond that is the **parameterized test**: one test body run over many supplied inputs, each reported as its own case. JUnit Jupiter's `@ParameterizedTest` draws inputs from `@ValueSource` (literals), `@CsvSource` (tabular rows), `@EnumSource` (enum constants), or `@MethodSource`/`@ArgumentsSource` (computed arguments). This is the direct cure for the copy-pasted near-identical tests that tempt teams to put a loop inside a test method, and a near-free test-quality win, because less duplication means fewer divergent near-tests to drift apart (Chapter 20). The inputs are still hand-picked, so parameterized tests widen *how many* cases run, not *which kinds* no one would have enumerated. One body, a table of known cases:

<!-- include: 45_integration_property_based_testing/src/test/java/org/acme/catalog/PriceListParameterizedTest.java#parameterized-table -->

### Property-based testing: inputs no example imagined

That last gap is what **property-based testing** closes. Instead of asserting a result for a specific input, the test asserts a **property that should hold for all inputs** and lets the framework generate hundreds of them. In Java the tool is **jqwik**:

- `@Property` (in place of `@Test`) with `@ForAll` parameters: jqwik **generates** values, a configurable number of tries per property.
- **Arbitraries** (`Arbitraries`, `@Provide` methods) describe the input domain, and combinators build complex generators from simple ones.
- On failure, jqwik **shrinks** the failing input to a minimal counterexample, reducing a 4,000-character random string to the two characters that actually break the code. Shrinking is the feature that makes property-based testing debuggable rather than a pile of inscrutable random failures.
- It runs as its own `TestEngine` on the JUnit Platform, so it composes alongside Jupiter tests on the same runner (Chapter 21).

> **CONCEPT** *The ladder: inputs chosen → inputs generated.* Example test: the author writes the input and the expected output. Parameterized test: a table of inputs runs the same body. Property test: the test describes the *domain* and the *invariant*, and the framework picks inputs no one would enumerate (empty, boundary, negative, huge, Unicode), then shrinks any failure to its minimal form. Each rung covers more of the input space at the cost of more abstraction in what the assertion checks.

The skill of property-based testing is finding the right **invariant**, a property true for *all* valid inputs. The classic shapes: a **round-trip** (`parse(format(x))` equals `x`), **commutativity** (order does not change the result), **idempotence** (applying twice equals applying once), or the weakest useful one, **never throws on valid input**. A good invariant catches the emoji-in-the-name-field bug because jqwik *will* generate that emoji; a hand-picked example suite does not.

The companion module carries a value object with exactly such a round-trip surface, a SKU that renders to a canonical text form and parses back:

<!-- include: 45_integration_property_based_testing/src/main/java/org/acme/catalog/Sku.java#sku-roundtrip -->

The round-trip property then asserts that pair across generated inputs rather than a few chosen literals. The module builds without a dedicated property library on its classpath, so it realizes the technique with a seeded JDK generator feeding one body and a small shrinker for the minimal counterexample; jqwik is the prose's named Java realization, weighed in the cost discussion below:

<!-- include: 45_integration_property_based_testing/src/test/java/org/acme/catalog/SkuPropertyTest.java#property-roundtrip -->

## Deep dive: where each earns its cost, and what each costs

Both techniques buy coverage of a blind spot, and both have an honest price. The question at the center is knowing when that price is worth paying.

**Integration testing's cost is the Docker dependency and the clock.** The hard requirement is a container runtime wherever the tests run; a locked-down CI environment, or one without Docker-in-Docker, can block Testcontainers outright, and where Docker is absent, a Testcontainers-backed test cannot run at all (in this book's pipeline, such an example is reproduction-gated: marked pending where no runtime is available). Beyond that, containers are slow relative to unit tests. Image pull and startup cost seconds, so over-using them where a unit test suffices inflates CI time and quietly inverts the pyramid into the slow, brittle ice-cream cone (Chapter 20). They also add a flakiness surface of their own: container startup timing, port mapping, and image-registry availability all fail intermittently, which is why pinned image tags and explicit wait strategies are not optional. Finally, one real dependency is not the whole system. An integration test with a real Postgres is not end-to-end and does not replace a small E2E layer. The rule that falls out: reach for a real container exactly where the interaction with the real dependency *is* the behaviour under test (persistence, SQL, serialization, messaging), and nowhere else.

**Property-based testing's costs are subtler, and one is about the tool's future.** Writing good properties is genuinely hard. The discipline is finding the invariant, and a weak property (`doesn't throw`) tests almost nothing while looking thorough; there is a real learning curve before a team writes properties that earn their slowness. Generated inputs are non-deterministic, so reproducing a CI failure requires pinning the seed; without it, a property failure can itself become a flaky test. Hundreds of generated cases also cost more time than a handful of examples, so property tests belong on invariants, not on every behaviour.

> **CONCEPT** *Tool vitality is part of the decision.* As of 2026, jqwik's own project status describes it as in **maintenance mode** (functional and Platform-integrated, but not under active feature development). That does not make it unusable; it is a stable, working library. But adopting any tool means weighing its trajectory, and a senior team should factor maintenance-mode status into a heavy commitment the same way it weighs any dependency's health. The book states this plainly and crowns nothing: property-based testing is a valuable *technique*, and jqwik is its current Java realization, with this caveat attached.

Neither technique replaces what came before. Property-based testing *complements* example tests (properties for invariants, examples for specific known behaviours and regression cases), and integration tests *complement* unit tests rather than replacing them; pushing everything to the integration layer is the ice-cream cone again. The shape of a healthy suite is the pyramid filled out across both axes: many fast unit tests with hand-picked and parameterized inputs at the base, fewer integration tests against real dependencies in the middle, property tests layered wherever an invariant exists to assert, and a thin E2E cap. Each tool covers a blind spot the others cannot (the faked collaborator, the unimagined input), and the cost of each is the price of seeing into that blind spot.

## Limitations & when NOT to reach for it

- **Testcontainers needs Docker, everywhere.** A locked-down CI runner or a no-Docker environment blocks it; where the runtime is absent the test cannot run. When NOT to use: pure in-process logic a unit test already covers, where a container adds cost and nothing else.
- **Containers are slow and add flakiness.** Image pull and startup cost seconds; startup timing, port mapping, and registry availability flake. Over-use inverts the pyramid into the ice-cream cone (Chapter 20) and inflates CI time. Pin image tags and use explicit wait strategies; keep the base of the pyramid unit-level.
- **One real dependency is not end-to-end.** A real Postgres in a test does not exercise the whole system; integration tests do not replace a small E2E layer.
- **Reusable containers are not for CI.** Container reuse speeds local runs but can leak state between runs and is unsupported in CI (Chapter 20); do not enable it there to cut build time.
- **Good properties are hard to write.** A weak invariant (`doesn't throw`) looks thorough and tests little; property-based testing has a real learning curve, and properties cost more time than examples. Use them for invariants, not every case.
- **Generated inputs need seed-pinning.** Non-deterministic generation means a property failure is not reproducible without a pinned seed; without it, the property test is itself a flake.
- **jqwik is in maintenance mode (2026).** Functional and stable, but not under active feature development. Weigh that trajectory before a heavy commitment, as with any dependency's health.
- **Neither replaces example tests or unit tests.** Properties complement examples; integration complements (does not supplant) unit tests. The whole suite is the pyramid, not any single layer.

## Alternatives & adjacent approaches

- **An in-memory fake or H2**: faster than a container and dependency-free, acceptable when the *dialect-specific* behaviour genuinely does not matter; the honest trade is lower fidelity (it tests a different engine).
- **Framework test slices** (Spring `@DataJpaTest`, Quarkus `@QuarkusTest`): boot a subset of the app for faster integration tests than a full-context boot.
- **Contract testing** (Pact, later): verifies that the assumed service boundary matches the provider's reality, complementing integration tests for cross-service correctness.
- **Coverage-guided fuzzing** (Jazzer/JQF, Part VIII): generates inputs to maximize coverage or trigger crashes; overlaps property-based generation but is security/robustness-oriented.
- **Example and parameterized tests**: still the right tool for specific known behaviours, regression cases, and table-driven finite input sets; property-based testing sits on top, not instead.

These layer into one suite: examples and parameterized tests for known cases, properties for invariants, real-dependency integration tests where fidelity matters, fakes/slices where it does not, and fuzzing at the security boundary.

## When to use what

- **The interaction with a real dependency is the behaviour under test** (SQL, persistence, messaging, serialization): an integration test against a real container (Testcontainers).
- **Dialect-specific behaviour genuinely does not matter:** an in-memory fake or slice, faster and lower fidelity, stated honestly.
- **A known, finite set of input cases / table-driven tests:** a `@ParameterizedTest`.
- **An invariant true for all valid inputs** (round-trip, commutativity, idempotence, never-throws): a jqwik `@Property`, weighing its maintenance-mode status.
- **Reproducing a generated failure in CI:** pin the seed.
- **Untrusted-input robustness/security:** fuzzing (Part VIII), not property-based testing.
- **A specific known behaviour or regression:** a plain example test; do not over-engineer it into a property.

## Hand-off to the next chapter

The last three chapters built a suite across the pyramid: unit tests with doubles, integration tests with real dependencies, properties over generated inputs. But a suite's *size* and *spread* still say nothing about whether it would catch a bug. A thousand tests that assert nothing meaningful pass exactly as greenly as a thousand that assert everything. The next chapter measures what the suite is actually worth: **coverage** (how much of the code runs under test, the necessary floor) and **mutation testing** (how much of that code the tests can actually *detect faults* in, the truth about effectiveness, previewed in Chapter 20). That is where the two-axis framing from the start of Part V gets its tools, and where "is this suite any good?" finally gets a measured answer.

## Back matter — sources & traceability

- **Integration/Testcontainers** (`java.testcontainers.org`; `github.com/testcontainers/testcontainers-java`): real-dependency integration vs mock-drift / in-memory-substitute infidelity; `GenericContainer` / `PostgreSQLContainer` / `KafkaContainer`; JUnit 5 `@Testcontainers` + `@Container` (instance = per-test, static = per-class shared); module ecosystem (DB/Kafka/RabbitMQ/Elasticsearch/LocalStack/Selenium); reusable containers (opt-in, not-for-CI per Ch 20) + Ryuk cleanup; HARD Docker-runtime dependency (dev + CI); test slices (Spring `@DataJpaTest`, Quarkus `@QuarkusTest`) cited neutrally; fixture quality (real API/factories, minimal). Pyramid middle (Ch 20). GAV `org.testcontainers:testcontainers` + `:junit-jupiter` + module (e.g. `:postgresql`), test scope. *(model/JUnit5 verified; **version 2.0.5 verified** against SOURCE-PIN §3; `@Container` per-test-vs-static semantics + reuse/Ryuk flags remain ⚠ @pin — prose-only, not exercised by the build, re-confirm at each tool's pinned docs. Docker-gating resolved: cited-not-built — integration realized in-JVM, flagged in `09-flags/45_testcontainers_docker_gated_not_built.md`.)*
- **Parameterized/property-based** (⚠ approaches, crown none) — JUnit Jupiter `@ParameterizedTest` + `@ValueSource`/`@CsvSource`/`@EnumSource`/`@MethodSource`/`@ArgumentsSource` (`docs.junit.org`; `junit-jupiter-params`). jqwik (`jqwik.net`; `github.com/jqwik-team/jqwik`): `@Property` + `@ForAll`, `Arbitraries`/`@Provide`, shrinking to minimal counterexample, own `TestEngine` on the JUnit Platform, `@BeforeProperty`/`@AfterProperty`, `jqwik-testcontainers`. Invariant shapes: round-trip `parse(format(x))==x` / commutativity / idempotence / never-throws-on-valid. GAV `net.jqwik:jqwik`, test scope. *(model verified; **jqwik version 1.10.1 verified** against SOURCE-PIN §3; default `@Property` tries count + `Arbitraries`/`@Provide` API remain ⚠ @pin — prose-only, not on the build's dep set, re-confirm at jqwik's pinned docs. The JUnit param-source annotations the build exercises (`@ParameterizedTest`/`@ValueSource`/`@CsvSource`/`@MethodSource`) are confirmed unchanged + green under JUnit 6. **jqwik maintenance-mode (2026)** verified against SOURCE-PIN §3 — tool-vitality caveat, stated plainly, crown none.)*
- **Adjacent** — fuzzing (Jazzer/JQF) named as coverage-guided, security-oriented → Part VIII (70). Routing: pyramid/landscape → Ch 20 (41); mock-vs-real → Ch 21 (44); flakiness (container startup, seed-pinning) → Ch 20 (49); coverage/mutation effectiveness → Ch 23 (47/48); CI cost → CI part (79); contract testing → Ch 24 (50).

**Companion module (built — `mvn -B -Pquality verify` green at Java 21):** `08-companion-code/45_integration_property_based_testing/` — (a) **integration** against a real collaborator: a `CatalogClient` driving a real `CatalogApi` HTTP service over the wire, booted in the same JVM on an ephemeral port, exercising the encoding / status mapping / parse a mocked client would miss (the fidelity gap); (b) a `@ParameterizedTest` table over a finite input set; (c) a **property-based** round-trip invariant (`parse(format(x)) == x`) over a seeded JDK generator, plus a shrinker that reduces a found failure to its minimal counterexample. **Failure path / honest edge:** the not-found lookup surfaces a typed `CatalogLookupException` over real HTTP, and the shrinking test reduces a deliberately-buggy validator's failure to its minimal counterexample (1000). **As-built note (never-invent / honest realization):** the chapter's named tools, **Testcontainers** (Docker-gated → REPRO PENDING-RUNTIME where no container runtime exists) and **jqwik** (maintenance-mode, off the build's pinned dependency set), are cited in the prose and flagged in `09-flags/` as cited-not-built; the module realizes the same two techniques (a real in-JVM collaborator; generated inputs + shrinking) with the already-pinned stack so the build is green with no infrastructure dependency. Both are recorded in the gate report `45_integration_property_based_testing_EXAMPLE.md`.

**Snippet tags:** `sku-roundtrip` (`Sku.java`); `integration-roundtrip` (`CatalogIntegrationTest.java`); `parameterized-table` (`PriceListParameterizedTest.java`); `property-roundtrip` (`SkuPropertyTest.java`) — 4 tags, each ≤9 lines, all bound into the prose above via tag-include markers and verified green by `check_snippets.sh`.

## Next chapter teaser

A suite can be large, well-spread across the pyramid, and built with real dependencies and generated inputs, and still catch almost nothing, because size is not effectiveness. The next chapter measures what the suite is actually worth: coverage as the necessary floor (how much code runs under test) and mutation testing as the real verdict (how many seeded faults the tests detect). It turns the two-axis framing from the opening of Part V into two tools and one uncomfortable number.
