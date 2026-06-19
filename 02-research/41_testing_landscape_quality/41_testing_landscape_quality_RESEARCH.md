# RESEARCH DOSSIER — Java Code Quality Book

> **Part-V (Tier-A, Foundational/umbrella) dossier — opens the testing pillar.** The subject is the
> **testing landscape and *test quality*** (not just coverage): the test pyramid (Cohn / Fowler-Vocke)
> revisited, the granularity layers (unit → integration → end-to-end), what makes a test *good* (the FIRST
> properties, observable-behaviour-not-implementation, test smells), and the two measurement axes the rest
> of Part V deepens — **coverage** (how much code runs under test, JaCoCo, key 48) vs **mutation score**
> (how much of that code the tests can actually *detect faults* in, PITest, key 47). Row 41 in
> `CANDIDATE_POOL.md` is the **umbrella over 42–52** and is **not** `⚠` comparison-flagged — it surveys
> *concepts* and the Java *tool families* (each cited to its own source), routing every deep tool decision to
> its owning chapter. NEUTRALITY still applies to every named tool (strongest case + hardest limitation,
> cite each tool's own docs, crown none).
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted where a feature changes a testing recommendation.
> All Part-V tool versions are **`TO-PIN`** in `SOURCE-PIN.md` §3, so tool **API/annotation identity** and
> **mechanism** are verified from each tool's own docs while exact **versions / GAV / defaults / thresholds**
> carry `⚠ verify at pin`. **Coverage-%-as-test-quality is on the PIPELINE-LEARNINGS folklore list** — it is
> framed as *necessary, not sufficient*, never asserted as a quality measure. Untraceable atoms → `⚠ UNVERIFIED`
> in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 41 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** The testing landscape & test *quality* (not just coverage) — the pyramid revisited
- **Part:** Part V — Testing as a quality pillar (umbrella over keys 42–52)
- **Tier:** A · **Depth band:** **Foundational** (per CANDIDATE_POOL rubric: keys 01/04/26/41/75) — the
  model/literature (Cohn pyramid; Fowler-Vocke "Practical Test Pyramid"; Meszaros *xUnit Test Patterns* test
  smells) **plus** the primary docs of every Java tool the chapter names (JUnit 5, AssertJ/Hamcrest/Truth,
  Mockito, Testcontainers, jqwik, PITest, JaCoCo, REST-assured/Pact, JMH), each cited to its own pinned source.
- **Cmp:** *(not `⚠`)* — this is a **landscape/umbrella** chapter. The **subject** (the *discipline* of
  testing; the *concepts* of granularity, coverage, test quality; the pyramid as a model) is discussed freely
  (NEUTRALITY "subject vs comparison target"). The **tools** are named as the Java surface for each concept,
  each cited to its own source and routed to its owning chapter (42–52); **no tool is crowned**, and the
  "which assertion lib / when to mock / which coverage gate" verdicts are explicitly **routed** (43/44/48).
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **The pyramid literature (named-canon + primary articles):**
    - **Mike Cohn**, *Succeeding with Agile* (2009) — origin of the three-layer test pyramid (Unit / Service /
      UI). *(Not a SOURCE-PIN §7 canon row — see §7 gap; cited as the originating secondary.)*
    - **Ham Vocke**, "The Practical Test Pyramid" on `martinfowler.com/articles/practical-test-pyramid.html`
      (Martin Fowler's site) — the canonical modern restatement; verified verbatim this scan.
    - **Gerard Meszaros**, *xUnit Test Patterns* (2007) — the test-smell catalogue (Assertion Roulette, Eager
      Test, Mystery Guest, Fragile/Erratic/Slow tests). *(Not a SOURCE-PIN §7 canon row — see §7 gap.)*
  - **Java test tooling (GAV / API identity verified from each tool's own docs; versions `⚠ verify at pin`):**
    - **JUnit 5 (Jupiter)** — `org.junit.jupiter:junit-jupiter` (aggregator), `…:junit-jupiter-params`;
      `@Test`, `@ParameterizedTest`, `@Nested`, `@RepeatedTest`, `@TestFactory`, `@TestTemplate`,
      `@BeforeEach`/`@AfterEach`/`@BeforeAll`/`@AfterAll`. (key 42)
    - **AssertJ / Hamcrest / Truth** — fluent / matcher / fluent assertion libraries (key 43).
    - **Mockito** — `org.mockito:mockito-core`, `…:mockito-junit-jupiter`; `mock()`/`when()`/`verify()` (key 44).
    - **Testcontainers (Java)** — `org.testcontainers:junit-jupiter`; `@Testcontainers`, `@Container` (key 45).
    - **jqwik** — `net.jqwik:jqwik`; `@Property`, `@ForAll`, `@Provide`, `Arbitraries` (key 46).
    - **PITest (PIT)** — `org.pitest:pitest-maven` / `pitest-junit5-plugin`; mutation score, `DEFAULTS`/`STRONGER`
      mutator groups (key 47).
    - **JaCoCo** — `org.jacoco:jacoco-maven-plugin`; instruction/branch/line/method/class/complexity counters (key 48).
    - **REST-assured / Pact** — API & contract testing (key 50).
    - **JMH** — `org.openjdk.jmh:jmh-core`; microbenchmark harness (keys 51/104).
  - **Concepts the rest of Part V rests on (Bucket-i shared foundations, discuss freely):** the FIRST
    properties (Fast / Isolated / Repeatable / Self-validating / Timely), Arrange-Act-Assert (AAA) &
    Given-When-Then, "test observable behaviour, not implementation," solitary-vs-sociable unit tests.
- **Canonical doc page(s):** `martinfowler.com/articles/practical-test-pyramid.html` (pyramid, layers,
  warnings — verbatim this scan); `docs.junit.org/<ver>/user-guide/` (JUnit 5 annotations); `pitest.org`
  (mutation-testing definition + coverage-limitation statement — verbatim this scan);
  `jacoco.org/jacoco/trunk/doc/counters.html` (the six counters — verbatim this scan);
  `java.testcontainers.org/test_framework_integration/junit_5/`; `jqwik.net/docs/current/user-guide.html`;
  `site.mockito.org`; `xunitpatterns.com` (test smells).
- **Canonical source path(s):** concepts live in the literature/articles, not a repo; each tool's API traces
  to that tool's pinned docs/repo (`SOURCE-PIN.md` §3, all `TO-PIN`). Companion artifact:
  `08-companion-code/41_testing_landscape_quality/`.

---

## 1. Core definition & purpose

**Central claim.** A test suite has two independent dimensions a quality program must hold apart:
**how much** of the system is exercised (granularity + coverage) and **how good** each test is at *detecting
a fault when one is introduced* (test quality). A suite can score high on the first and low on the second — the
chapter's load-bearing teaching. The **test pyramid** is the model for the first dimension (group tests by
granularity; have many fast low-level tests and few slow high-level ones); **test quality** (the FIRST
properties, behaviour-not-implementation, freedom from test smells) and **mutation score** are the models for
the second. Coverage % measures *execution*, not *detection* — it is **necessary, not sufficient** (folklore
guard; keys 47/48). This chapter is the umbrella that names every Java tool used to realise both dimensions
and hands each deep topic to its owning chapter (42–52).

**Which part of the pinned set provides it.**
- The **pyramid model** comes from **Mike Cohn**, *Succeeding with Agile* (origin, three layers) and **Ham
  Vocke's** "The Practical Test Pyramid" on martinfowler.com — the modern restatement. Verified verbatim:
  the two rules are *"Write tests with different granularity"* and *"The more high-level you get the fewer tests
  you should have"* (Vocke, martinfowler.com).
- The **test-quality / test-smell** vocabulary comes from **Meszaros**, *xUnit Test Patterns* (Assertion
  Roulette, Eager Test, Mystery Guest, Fragile/Erratic/Slow tests — verified from `xunitpatterns.com`).
- The **measurement axes** come from each tool's own docs: JaCoCo counters (`jacoco.org`) for coverage;
  PITest mutation score (`pitest.org`) for fault-detection. PIT states coverage *"measures only which code is
  executed by your tests. It does not check that your tests are actually able to detect faults in the executed
  code"* (verified verbatim, pitest.org) — the chapter's central distinction, stated by the tool itself.

**When introduced / lineage.** The pyramid is a **2009-era** model (Cohn) restated and widened by Vocke
(adding solitary-vs-sociable, the ice-cream-cone anti-pattern, and the "test behaviour not implementation"
caution). The Java tooling is mature and current (JUnit 5 / Jupiter is the standard harness at the anchor;
PITest and JaCoCo are the standard mutation/coverage tools). Versions `⚠ verify at pin`.

**Where it sits in the architecture.** Tests are **build-time + CI artifacts**: unit/integration tests run
under the build's test phase (Maven Surefire/Failter, Gradle `test`), coverage and mutation reports are
generated post-test, and gates (keys 48/76/80) enforce thresholds in CI. Granularity maps to *where* a test
runs: solitary unit tests (in-JVM, no I/O) → integration tests (real DB/broker via Testcontainers, key 45) →
end-to-end (full stack, slow, flaky — key 49). This chapter frames the map; 42–52 fill each layer.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The pyramid — granularity layers and the two rules

Cohn's pyramid has three layers, bottom-to-top: **Unit tests → Service tests → User-Interface tests**
(verified, Vocke restatement). Vocke's two foundational rules (verbatim, martinfowler.com):

1. *"Write tests with different granularity."*
2. *"The more high-level you get the fewer tests you should have."*

The shape encodes a cost/speed/confidence trade-off: low-level tests are **fast, isolated, cheap, and
plentiful**; high-level tests are **slow, broad, expensive, and few**. Vocke's layer definitions (verified):
- **Unit tests** — *"focused, isolated tests of individual code components with the narrowest scope."*
- **Integration tests** — verify the application *"correctly interacts with external dependencies like
  databases, filesystems, and separate services."*
- **UI / end-to-end tests** — check behaviour through the user interface but *"carry significant maintenance
  costs."* Verbatim on end-to-end: *"End-to-End tests … are notoriously flaky and often fail for unexpected
  and unforeseeable reasons."* (cross-ref key 49 flakiness.)

**The anti-pattern (verified verbatim).** The **"test ice-cream cone"** — *"excessive high-level tests that
become maintenance nightmares and run slowly."* This is the inversion of the pyramid: many brittle UI/E2E
tests, few unit tests. The chapter teaches it as the failure mode the pyramid exists to prevent.

**Solitary vs sociable unit tests (verified).** Vocke distinguishes **solitary** unit tests (stub *all*
collaborators for isolation) from **sociable** unit tests (let the unit interact with *real* collaborators
when practical). This is the conceptual root of the mocking debate (key 44): solitary tests use test doubles
heavily; sociable tests use fewer. Crown neither — both are legitimate (NEUTRALITY).

**Test observable behaviour, not implementation (verified).** Vocke: *"Avoid testing implementation details.
Test observable behaviour instead, not internal code structure."* This is the antidote to **fragile tests**
(Meszaros) that break on refactor without a behaviour change — a key bridge to the mocking-overuse limit (44)
and the refactoring-safety theme (key 91).

### 2.2 Test *quality* — what makes a test good (the second dimension)

The chapter's distinctive teaching is that **coverage and granularity are not test quality**. Quality is:

- **The FIRST properties** (Bucket-i, widely-cited heuristic — *Fast, Isolated/Independent, Repeatable,
  Self-validating, Timely*). Each maps to a Java mechanism: Fast → solitary unit tests, no I/O; Isolated → no
  shared mutable state / order-independence (`@TestMethodOrder`, fresh instances per method); Repeatable → no
  wall-clock / random / network dependency (fixed `Clock`, seeded data, Testcontainers for *controlled*
  external deps); Self-validating → assertions, not console inspection (AssertJ/Hamcrest, key 43); Timely →
  written with (or before) the code. *(FIRST is a community heuristic, not a pinned spec — frame as such;
  see §7.)*
- **Freedom from test smells** (Meszaros, *xUnit Test Patterns*, verified from xunitpatterns.com):
  - **Assertion Roulette** — *"hard to tell which of several assertions within the same test method caused a
    test failure"* (verified). Mitigation: descriptive assertion messages / one logical assertion per concept.
  - **Eager Test** — *"a single test verifies too much functionality … often caused by trying to minimize the
    number of unit tests by verifying many test conditions in a single Test Method"* (verified). A cause of
    Assertion Roulette.
  - **Mystery Guest** — a test depends on *"external resources"* / magic keys not visible in the test itself
    (verified). Mitigation: in-line fixtures / `@TempDir` / Testcontainers with explicit setup.
  - Plus **Fragile Test, Erratic Test (flakiness, key 49), Slow Test** (catalogued, verified list).
- **Mutation score** — the *quantitative* test-quality measure (PITest, key 47): the % of seeded faults the
  suite **kills**. PIT: faults are *"automatically seeded into your code, then your tests are run"*; a mutant
  is **killed** when tests fail, **lived** (survived) when tests pass despite the mutation (verified verbatim).
  This is the empirical answer to "are my tests actually checking anything?"

### 2.3 The Java test-tool families (the landscape — each cited to its own source, routed to its chapter)

| Family | Concept | Key Java surface (API identity verified; GAV/version `⚠ verify at pin`) | Owning chapter |
|---|---|---|---|
| **Test harness** | run & structure tests | **JUnit 5 Jupiter** — `@Test`, `@ParameterizedTest`, `@Nested`, `@RepeatedTest`, `@TestFactory`, `@TestTemplate`, lifecycle `@BeforeEach`/`@AfterEach`/`@BeforeAll`/`@AfterAll`; GAV `org.junit.jupiter:junit-jupiter` (+ `junit-jupiter-params`) | **42** |
| **Assertions** | self-validating + readable | **AssertJ** (fluent `assertThat(x).isEqualTo(...)`), **Hamcrest** (`assertThat(x, is(...))` matchers), **Truth** (Google fluent) | **43** |
| **Test doubles** | isolate the unit | **Mockito** — `mock()`, `when(...).thenReturn(...)`, `verify(...)`; GAV `org.mockito:mockito-core` (+ `mockito-junit-jupiter` for `@ExtendWith(MockitoExtension.class)`) | **44** |
| **Integration** | real external deps | **Testcontainers** — `@Testcontainers`, `@Container` (the extension finds `@Container` fields and drives their lifecycle); GAV `org.testcontainers:junit-jupiter` | **45** |
| **Parameterized / property** | many inputs / invariants | **JUnit `@ParameterizedTest`**; **jqwik** — `@Property`, `@ForAll`, `@Provide`/`Arbitraries`, *shrinking* to a minimal failing case | **46** |
| **Mutation (test quality)** | fault-detection score | **PITest** — `targetClasses`/`targetTests`, `DEFAULTS`/`STRONGER` mutator groups; GAV `org.pitest:pitest-maven` + `pitest-junit5-plugin` | **47** |
| **Coverage** | execution measure | **JaCoCo** — six counters (instructions/branches/lines/methods/classes/complexity); GAV `org.jacoco:jacoco-maven-plugin` | **48** |
| **Contract / API** | service boundaries | **REST-assured** (HTTP API), **Pact** (consumer-driven contracts) | **50** |
| **Performance** | benchmark honestly | **JMH** — `@Benchmark`, warmup/forks; GAV `org.openjdk.jmh:jmh-core` | **51/104** |

**Setup / build-time behavior.** JUnit 5 tests run under **Maven Surefire** (unit) / **Failsafe**
(integration `*IT`) or **Gradle `test`**; JaCoCo attaches a **Java agent** that instruments bytecode to record
which instructions/branches execute, then `report`/`check` goals produce reports and enforce thresholds (key
48); PITest runs *after* a green suite — it needs passing tests, then mutates compiled classes and re-runs the
tests per mutant (expensive — §4). Testcontainers starts **real Docker containers** for integration tests
(`@Container` static fields shared across methods; instance fields per-method — verified, testcontainers.org).

**Active / runtime behavior.** Solitary unit tests run in-JVM with doubles (Mockito); integration tests hit a
**real** DB/broker via Testcontainers; property tests (jqwik) generate many inputs and **shrink** a failure to
its minimal form (verified — *"shrinking … find the simplest failing example"*); mutation runs (PITest) report
the **mutation score** (% killed). A coverage gate fails the build below a line/branch threshold; a mutation
gate fails below a mutation-score threshold. Thresholds are *policy*, not law (folklore guard, §4).

### 2.4 Reference units (annotations / config / coordinates — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `@Test`, `@ParameterizedTest`, `@Nested`, `@RepeatedTest` | JUnit 5 annotation | mark/structure test methods | tool-version | docs.junit.org user guide ✅ (identity); ver ⚠ verify at pin |
| `org.junit.jupiter:junit-jupiter` (+ `-params`) | GAV | aggregator + parameterized support | tool-version | JUnit 5 docs ✅; version ⚠ verify at pin |
| AssertJ `assertThat(x).isEqualTo(...)` | assertion API | fluent | tool-version | assertj.github.io ✅ (identity); ver ⚠ |
| Hamcrest `assertThat(x, is(...))` | matcher API | matcher-based | tool-version | hamcrest.org ✅ (identity); ver ⚠ |
| Mockito `mock()`/`when()`/`verify()` | test-double API | stub + behaviour-verify | tool-version | site.mockito.org ✅ (identity); ver ⚠ |
| `org.mockito:mockito-core`, `mockito-junit-jupiter` | GAV | mocking + JUnit5 extension | tool-version | site.mockito.org ✅; version ⚠ verify at pin |
| `@Testcontainers`, `@Container` | Testcontainers annotation | drives container lifecycle | tool-version | java.testcontainers.org ✅ (verbatim mechanism); ver ⚠ |
| `org.testcontainers:junit-jupiter` | GAV | JUnit 5 integration | tool-version | java.testcontainers.org ✅; version ⚠ verify at pin |
| jqwik `@Property`, `@ForAll`, `@Provide`, `Arbitraries` | property API | generate + shrink | tool-version | jqwik.net ✅ (identity); ver ⚠ |
| jqwik max-discard-ratio | config | default **5** (assumptions) | tool-version | jqwik docs (corroborated) ⚠ verify at pin |
| PITest mutation score | metric | % mutants killed (killed/lived) | tool-version | pitest.org ✅ (verbatim killed/lived) |
| PITest mutator groups | config | `DEFAULTS`, `STRONGER`, explicit (e.g. `CONDITIONALS_BOUNDARY`, `MATH`) | tool-version | pitest.org/quickstart/mutators ✅ (identity); set ⚠ verify at pin |
| `org.pitest:pitest-maven` (+ `pitest-junit5-plugin`) | GAV | mutation runner | tool-version | pitest.org ✅; version ⚠ verify at pin |
| JaCoCo counters (6) | coverage metrics | Instructions/Branches/Lines/Methods/Classes/Complexity | tool-version | jacoco.org counters ✅ (verbatim) |
| `org.jacoco:jacoco-maven-plugin` | GAV | agent + report + check | tool-version | jacoco.org ✅; version ⚠ verify at pin |
| FIRST properties | heuristic | Fast/Isolated/Repeatable/Self-validating/Timely | n/a | community heuristic (not a pinned spec) ⚠ |
| Test smells (Meszaros) | catalogue | Assertion Roulette / Eager Test / Mystery Guest / Fragile / Erratic / Slow | edition | xunitpatterns.com ✅ (verbatim names) |

---

## 3. Evidence FOR

- **The pyramid gives a defensible default distribution, stated by its own canon.** Vocke's two rules
  (*"Write tests with different granularity"*; *"The more high-level you get the fewer tests you should have"*
  — verbatim, martinfowler.com) give teams a concrete, citable target shape: many fast unit tests, fewer
  integration, fewest E2E. It is a *heuristic*, not a law — the chapter presents it as a starting distribution
  to adapt (Vocke himself notes the layers are fuzzy), which is the honest framing.
- **Test quality is measurable, not just felt — mutation score.** PITest answers "do my tests detect faults?"
  empirically: faults are *"automatically seeded … then your tests are run"*; the suite's **mutation score**
  (% killed) is the measure (verified verbatim, pitest.org). PIT positions itself as *"the gold standard
  against which all other types of coverage are measured"* and claims it can *"analyse in minutes what would
  take earlier systems days"* (verified). Cite as PIT's own claim, route the deep treatment to key 47.
- **Coverage is precisely defined and tool-measured — when used as a *necessary* floor.** JaCoCo's six counters
  (Instructions C0, Branches C1, Lines, Methods, Classes, Cyclomatic Complexity — verified verbatim,
  jacoco.org) give exact, reproducible execution measurements. Branch coverage *"for all `if` and `switch`
  statements"* (verbatim) is a stronger floor than line coverage. The strongest case for coverage is as a
  **necessary floor** (untested code is unknown) — never as a sufficiency claim (§4).
- **A complete, mature Java tool surface for every layer.** JUnit 5 Jupiter is the standard harness
  (`@Test`/`@ParameterizedTest`/`@Nested`, verified identity, docs.junit.org); AssertJ/Hamcrest/Truth for
  self-validating assertions (43); Mockito for isolation (44); Testcontainers for *realistic* integration with
  real DBs/brokers (45); jqwik for property-based invariants with **shrinking** to a minimal failing case
  (verified, jqwik.net). Each is first-class and CI-wireable via Maven/Gradle.
- **Realistic integration without mocks-everywhere — Testcontainers.** Verified mechanism: the
  `@Testcontainers` extension *"finds all fields annotated with `@Container` and calls their container lifecycle
  methods"*; static `@Container` fields are *"shared between test methods … started only once"*, instance
  fields *"started and stopped for every test method"* (verified, testcontainers.org). This lets the *middle*
  pyramid layer test against the real dependency, reducing the over-mocking risk (44) and the
  mock-drifts-from-reality risk.
- **A named, citable vocabulary for *bad* tests.** Meszaros's smells (Assertion Roulette, Eager Test, Mystery
  Guest — verified verbatim) give reviewers (key 84) precise language and a refactoring target, so "this test
  is bad" becomes actionable.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each approach's hardest objection + when-NOT-to-use)

**Coverage % as a test-quality measure — the central folklore (PIPELINE-LEARNINGS folklore list).**
*Code coverage % is **not** a measure of test quality — it is necessary, not sufficient.* PIT states it
plainly: coverage *"measures only which code is executed by your tests. It does not check that your tests are
actually able to detect faults in the executed code"*; code can be *"only partially tested by its suite"* while
still executing all branches (verified verbatim, pitest.org). A suite with **100% line coverage and zero
assertions** kills no mutants. The book MUST frame coverage as a *floor* (find untested code), never as proof
of test quality (folklore guard; the debunking is the teaching). When-NOT: do not set a high coverage gate and
treat it as "tests are good" — pair it with mutation testing (47) and review.

**The pyramid is a heuristic, not a law — and its layers are contested.** Vocke's layer names are
deliberately fuzzy; the *exact* unit:integration:E2E ratio is not prescribed and is widely debated (the
"testing trophy"/"honeycomb" are alternative *approaches* that weight integration tests more, citing the cost
and brittleness of heavy mocking). Present these as **approaches**, crown none (NEUTRALITY); the pyramid is one
defensible default, not the only shape. When-NOT: a thin service that is mostly glue to external systems may
rationally invert toward more integration tests — the pyramid is guidance, not a quota.

**The ice-cream cone is the failure mode — but so is mutation-cost avoidance.** Over-investing at the top
(many slow, flaky E2E tests — *"notoriously flaky"*, verified) yields the ice-cream-cone anti-pattern: slow CI,
maintenance nightmares (verified). Conversely, the deeper quality measure (mutation testing) is **expensive**:
PIT re-runs the suite **per mutant**, so a large project's full mutation run can take a long time — its honest
limit (route the cost discussion + incremental/`--changes`/`scmMutationCoverage` mitigations to key 47).
When-NOT: don't run full-project mutation on every commit; scope it to changed code.

**Over-mocking — the solitary-unit trap (route to key 44).** Heavy use of test doubles (the solitary style)
isolates the unit but risks **fragile tests** that assert *implementation* (which methods were called) rather
than *observable behaviour* — Vocke: *"Avoid testing implementation details. Test observable behaviour instead"*
(verified). Mockito's own guidance notes *"overriding stubbing is a potential code smell that points out too
much stubbing"* and to *"avoid over-verification"* (Mockito wiki — `⚠ verify at pin`, route to 44). When-NOT:
don't mock value objects or types you don't own; prefer the real collaborator (or Testcontainers, 45) when the
interaction *is* the behaviour under test.

**Integration tests cost realism in speed and stability.** Testcontainers gives real dependencies but needs a
**Docker runtime**, adds startup latency, and the extension is *"only … tested with sequential test execution
… parallel test execution is unsupported and may have unintended side effects"* (verified, testcontainers.org).
When-NOT: don't push logic-only tests down to the container layer — keep the pyramid base solitary.

**Property-based tests trade determinism for breadth.** jqwik generates random inputs; a default
**max-discard-ratio of 5** means over-filtered generators *fail* the test (corroborated, `⚠ verify at pin`),
and a property can pass for thousands of runs yet miss an edge a hand-written example would catch. When-NOT:
properties suit invariants (round-trips, commutativity), not every behaviour — keep example tests too (46).

**Test smells are partly subjective and partly tool-undetectable.** Some smells (Slow Test) are measurable;
others (Eager Test, Mystery Guest) are review-found judgements with no reliable automated detector (mirrors the
key-19 "tool-found vs review-found" honesty). When-NOT: don't claim a linter "enforces good tests" — label each
smell tool-found vs review-found.

**Shared limits of all testing (the honest centre).** Tests show the presence of bugs, never their absence
(Dijkstra). No coverage %, mutation score, or pyramid shape proves correctness; testing complements — does not
replace — static analysis (Part IV), types, and review (84).

---

## 5. Current status

- **Stable and current at the anchor.** JUnit 5 (Jupiter) is the standard harness on Java 21; JaCoCo and
  PITest are the standard coverage and mutation tools; Testcontainers, Mockito, AssertJ, jqwik are actively
  maintained. *(All Part-V tool versions are `TO-PIN` in `SOURCE-PIN.md` §3 — pin each at `/pin-source`;
  observed live-line examples seen this scan include Testcontainers `junit-jupiter` 1.19.x and a 2.0.x line —
  do not assert a version; `⚠ verify at pin`.)*
- **The pyramid model is stable but actively debated.** The Cohn/Vocke pyramid remains the canonical default;
  the "testing trophy" and "honeycomb" are current **alternative weightings** (more integration tests), driven
  by the cost of over-mocking and faster integration tooling (Testcontainers). Present as approaches, not a
  superseded/winner story.
- **Mutation testing is gaining adoption** as coverage's limitations become widely understood — PIT's own
  framing ("coverage … does not check that your tests are actually able to detect faults") is now mainstream.
  Incremental/PR-scoped mutation (route to 47) is the adoption enabler.
- **Java-version deltas.** Records/sealed types/pattern matching (key 13) make *data-shaped* tests terser and
  enable exhaustive-`switch`-as-test-oracle patterns; **virtual threads (JEP 444, final @21)** change
  concurrency-test strategy (cross-ref keys 22/24); these are notes, not changes to the pyramid model.
- **No deprecations material to the model.** JUnit 4 → 5 is a completed migration story (JUnit 5's `vintage`
  engine runs JUnit 4 tests); cite JUnit 5 as the anchor harness, JUnit 4 as legacy only.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `41_testing_landscape_quality` *(row to be added —
  `DEMO-CATALOG.md` does not yet exist in the repo; see §7 flag, consistent with keys 15/24/25/28/35 catalog
  gaps).*
  - **Demo name:** "Same code, three test suites — coverage 100%, mutation score tells the truth."
  - **Java Quality surface exercised:** one small `org.acme.pricing` class (e.g. a `DiscountPolicy` with
    boundary conditions and a couple of branches). **Suite A**: a *vanity* test that **executes** the method
    (100% JaCoCo line coverage) but asserts almost nothing → PITest reports a **low mutation score** (mutants
    *lived*). **Suite B**: well-targeted JUnit 5 tests (AssertJ assertions, boundary cases via
    `@ParameterizedTest`) → high mutation score. **Suite C**: a jqwik `@Property` for an invariant (e.g.
    discount never exceeds price) + one Testcontainers-backed integration test against a real Postgres for the
    persistence path (the middle pyramid layer). The contrast between A's coverage and A's mutation score **is**
    the chapter's thesis made tactile.
  - **TRY-IT exercise:** run `./mvnw -B verify` to see Suite A reach 100% line coverage (JaCoCo) yet
    **fail the PITest mutation threshold**; then strengthen the assertions and watch the mutation score rise
    while coverage stays 100% — proving coverage and test quality are independent axes. Then add the jqwik
    property and the Testcontainers integration test to populate the pyramid's three layers.
- **Module key / path:** `08-companion-code/41_testing_landscape_quality/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ (anchor) verify at pin |
  | `org.junit.jupiter:junit-jupiter` (+ `junit-jupiter-params`) | the test harness (primary unit) | docs.junit.org (GAV TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | self-validating, readable assertions (key 43) | assertj.github.io (TO-PIN) | ☐ verify at pin |
  | `org.jacoco:jacoco-maven-plugin` | coverage agent + report + `check` gate (key 48) | jacoco.org (TO-PIN) | ☐ verify at pin |
  | `org.pitest:pitest-maven` (+ `pitest-junit5-plugin`) | mutation score + threshold (key 47) | pitest.org (TO-PIN) | ☐ verify at pin |
  | `net.jqwik:jqwik` | property test for the invariant (key 46) | jqwik.net (TO-PIN) | ☐ verify at pin |
  | `org.testcontainers:junit-jupiter` (+ `postgresql`) | real-dependency integration test (key 45) | java.testcontainers.org (TO-PIN) | ☐ verify at pin |
  | `org.mockito:mockito-junit-jupiter` (sparingly) | one solitary test showing a justified double (key 44) | site.mockito.org (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose versions; all test-tool versions managed centrally.
  - **Externalized config / profiles** — JaCoCo `check` rule (line/branch minimum) and PITest
    `mutationThreshold` in the POM/Gradle config; a `*IT` Failsafe binding so integration tests run in a
    separate phase (trace each to the tool's docs).
  - **At least one test** — names the behaviour it asserts (the `DiscountPolicy` boundary behaviour), across
    all three pyramid layers.
  - **Observability / health surface** — the JaCoCo coverage report + the PITest mutation report are the
    observable quality surfaces (key 88 trend); the integration test asserts the persistence health path.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** **Suite A is the failure path** — it reaches 100%
    coverage but **fails the PITest mutation gate** (`./mvnw -B verify` red on the mutation threshold). State
    in the chapter that the green coverage + red mutation score **is** the demonstrated failure path, and that
    it proves the §4 thesis: coverage is necessary, not sufficient.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `vanity-test` | Suite A — 100% line coverage, no real assertions (mutants survive) | `DiscountPolicyVanityTest.java` |
  | `strong-unit` | Suite B — boundary `@ParameterizedTest` + AssertJ; kills the mutants | `DiscountPolicyTest.java` |
  | `property-test` | Suite C — jqwik `@Property` invariant (`@ForAll`) | `DiscountPolicyPropertyTest.java` |
  | `integration-test` | Suite C — `@Testcontainers`/`@Container` Postgres integration | `DiscountRepositoryIT.java` |
  | `quality-gates` | JaCoCo `check` + PITest `mutationThreshold` config | `pom.xml` |

- **Run command:** `./mvnw -B verify` (runs unit + parameterized + property tests via Surefire, the
  Testcontainers `*IT` via Failsafe — requires a Docker runtime — JaCoCo report, then `./mvnw -B
  org.pitest:pitest-maven:mutationCoverage` for the mutation run).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** Suite A — JaCoCo report shows **100% line coverage** for `DiscountPolicy` yet the
  **PITest mutation gate fails** (build red) with surviving mutants; after strengthening assertions — high
  mutation score, green gate; the jqwik property passes (or shrinks a counterexample to its minimal form); the
  Testcontainers `*IT` passes against a real Postgres. Test pass counts green across the three layers.
- **Figure plan** (GUIDELINES §8; **Foundational/umbrella chapter** → image budget ~**2 designed diagrams +
  1 captured screenshot**; NOT a zero-figure chapter — the pyramid and the coverage-vs-mutation contrast are
  inherently visual and load-bearing):
  - **Chapter class:** Foundational/umbrella Part-V chapter (the pyramid model + the two-axis measurement
    framing earn designed diagrams; a real report earns a capture).
  - **Candidate designed diagram(s) + family:**
    - **Fig 41.1 — "The test pyramid (and the ice-cream-cone inversion)" (layered model diagram):** three
      stacked layers Unit → Service/Integration → UI/E2E with the cost/speed/count gradient annotated (many
      fast cheap at the base; few slow expensive at the top), and an inset showing the inverted ice-cream-cone
      anti-pattern. Family = *layered-model / conceptual diagram*. Trace: layers + two rules + ice-cream-cone
      + "E2E notoriously flaky" → Vocke, martinfowler.com (verbatim this scan).
    - **Fig 41.2 — "Two axes of a test suite: coverage (execution) vs mutation score (detection)" (2×2 /
      quadrant diagram):** x-axis = coverage %, y-axis = mutation score; the dangerous quadrant = high
      coverage / low mutation score ("vanity tests"). Family = *quadrant / concept diagram*. Trace: coverage
      definition → JaCoCo counters doc; mutation/killed-lived + "coverage does not check fault detection" →
      pitest.org (verbatim this scan).
  - **Candidate captured surface(s):**
    - **Fig 41.3** — a capture of the companion module's **PITest HTML mutation report** (surviving vs killed
      mutants on `DiscountPolicy`) **paired** with the **JaCoCo coverage report** showing 100% on the same
      class — the two-axis contrast from real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** pyramid layers / two rules / ice-cream-cone / E2E-flaky →
    martinfowler.com practical-test-pyramid (verbatim); coverage counters → jacoco.org/.../counters.html
    (verbatim); mutation definition + killed/lived + coverage-limitation → pitest.org (verbatim); each tool's
    annotation/API label → that tool's own docs.

---

## 7. Gap-filling (verification queue)

- ⚠ **All Part-V tool versions / GAV coordinates** — JUnit 5 (`org.junit.jupiter:junit-jupiter` + `-params`),
  AssertJ (`org.assertj:assertj-core`), Hamcrest, Truth, Mockito (`mockito-core`/`mockito-junit-jupiter`),
  Testcontainers (`org.testcontainers:junit-jupiter` + module artifacts), jqwik (`net.jqwik:jqwik`), PITest
  (`org.pitest:pitest-maven` + `pitest-junit5-plugin`), JaCoCo (`org.jacoco:jacoco-maven-plugin`), REST-assured,
  Pact, JMH (`org.openjdk.jmh:jmh-core`): **all `TO-PIN`** in `SOURCE-PIN.md` §3 → confirm exact latest-stable
  versions + coordinates at pin before stating any version number. API/annotation **identity** verified now.
- ⚠ **jqwik max-discard-ratio default (5)** — corroborated from a secondary; confirm against jqwik's own user
  guide at the pinned version before printing the number.
- ⚠ **PITest mutator-group contents (`DEFAULTS` / `STRONGER` membership)** — the *group names* and example
  mutators (`CONDITIONALS_BOUNDARY`, `MATH`, `INCREMENTS`, `NEGATE_CONDITIONALS`, `VOID_METHOD_CALLS`,
  `RETURN_VALS`) are corroborated; the **exact set in `DEFAULTS` vs `STRONGER`** is version-sensitive → confirm
  from `pitest.org/quickstart/mutators/` at pin. (Defaults differ across PIT versions — folklore guard:
  "never print one mutator set as *the* default.")
- ⚠ **JaCoCo default `check` thresholds** — JaCoCo's `check` goal has **no built-in default minimum** (the team
  sets the rule); never assert "JaCoCo defaults to N% coverage." Confirm the rule syntax at pin.
- ⚠ **FIRST properties** — a widely-cited community heuristic (popularized via *Clean Code* / agile testing
  literature), **not** a pinned spec. Frame as a heuristic with attribution; do not cite a "standard."
- ⚠ **"100% coverage / 0 assertions kills 0 mutants"** — true by construction and demonstrated by the companion
  module; present as a demonstrated property of the demo, not a published figure.
- ⚠ **Testing-trophy / honeycomb** — named as alternative *approaches* (Kent C. Dodds; Spotify honeycomb); any
  factual claim about them needs its own source or must stay at the level of "an alternative weighting." Crown
  none. *(Not SOURCE-PIN rows — cite the originating article if used, else keep conceptual.)*
- ⚠ **Mockito "over-stubbing is a code smell" / "avoid over-verification"** — from Mockito wiki/FAQ; confirm
  verbatim phrasing at the pinned Mockito docs (route the deep treatment to key 44).
- **SOURCE-PIN §7 canon gap (material):** **Mike Cohn, *Succeeding with Agile* (2009)** (pyramid origin) and
  **Gerard Meszaros, *xUnit Test Patterns* (2007)** (test-smell catalogue) are this chapter's originating
  authorities but are **not** SOURCE-PIN §7 named-canon rows. Propose adding both. Filed
  `09-flags/41_testing_canon_not_pinned.md`. (Vocke's article is on martinfowler.com — cite the URL.)
- **DEMO-CATALOG.md** does not yet exist in the repo — add the `41_testing_landscape_quality` row when created
  (same gap as keys 15/24/25/28/35). Filed `09-flags/41_demo_catalog_missing.md`.
- **Open question (draft / Part V routing):** boundary with **key 42** (JUnit 5 deep — owns harness mechanics),
  **43** (assertion libs — route AssertJ/Hamcrest/Truth comparison there), **44** (mocking — route
  over-mocking / solitary-vs-sociable depth there), **45** (Testcontainers depth), **46** (parameterized +
  property/jqwik depth), **47** (mutation/PITest — owns mutation-cost + incremental), **48** (coverage/JaCoCo —
  owns gate config + the coverage-isn't-quality depth, cluster 47/48), **49** (flakiness + test smells depth),
  **50** (contract/API — REST-assured/Pact), **51/104** (JMH performance). Propose: **this** chapter owns the
  *landscape map + the pyramid model + the two-axis (coverage vs mutation) test-quality framing*; every tool's
  deep config routes to its owning chapter.

### Filed to `09-flags/`
- `09-flags/41_testing_tool_versions_unverified.md` — all Part-V tool GAVs/versions/defaults are `TO-PIN`
  (JUnit5/AssertJ/Hamcrest/Truth/Mockito/Testcontainers/jqwik/PITest/JaCoCo/REST-assured/Pact/JMH); API and
  annotation identity verified, but versions, PITest mutator-group contents, jqwik max-discard-ratio, and
  JaCoCo `check` syntax/thresholds are `⚠ verify at pin`.
- `09-flags/41_testing_canon_not_pinned.md` — Cohn *Succeeding with Agile* (2009) and Meszaros *xUnit Test
  Patterns* (2007) are this chapter's originating authorities but are absent from SOURCE-PIN §7 named-canon;
  propose adding both rows.
- `09-flags/41_demo_catalog_missing.md` — `DEMO-CATALOG.md` does not exist; the `41_testing_landscape_quality`
  row cannot yet be created.

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | Article (Fowler site) | "The Practical Test Pyramid" — Ham Vocke: pyramid layers (Unit/Service/UI), two rules ("Write tests with different granularity"; "the more high-level … the fewer tests"), ice-cream-cone anti-pattern, "End-to-End tests … notoriously flaky", solitary-vs-sociable, "test observable behaviour, not implementation" | martinfowler.com/articles/practical-test-pyramid.html | ☑ (verbatim rules + layers + warnings) |
| 2 | PITest doc | mutation testing definition ("Faults … automatically seeded … then your tests are run"), killed/lived, "coverage … does not check that your tests are actually able to detect faults", "gold standard", speed claim | pitest.org (+ /quickstart/mutators/) | ☑ (verbatim definition + coverage-limitation) |
| 3 | JaCoCo doc | the six counters (Instructions C0, Branches C1, Lines, Methods, Classes, Cyclomatic Complexity) verbatim definitions | jacoco.org/jacoco/trunk/doc/counters.html | ☑ (verbatim counters) |
| 4 | JUnit 5 doc | Jupiter annotations — `@Test`, `@ParameterizedTest`, `@Nested`, `@RepeatedTest`, `@TestFactory`, `@TestTemplate`, lifecycle; `junit-jupiter-params` artifact | docs.junit.org/<ver>/user-guide | ☑ (identity; version ⚠ verify at pin) |
| 5 | Testcontainers doc | `@Testcontainers` extension finds `@Container` fields + drives lifecycle; static=shared, instance=per-method; sequential-only caveat | java.testcontainers.org/test_framework_integration/junit_5/ | ☑ (verbatim mechanism + caveat) |
| 6 | jqwik doc | `@Property`, `@ForAll`, `@Provide`/`Arbitraries`; shrinking to "the simplest failing example"; max-discard-ratio | jqwik.net/docs/current/user-guide.html | ☑ (identity + shrinking; ratio ⚠ verify) |
| 7 | Mockito doc | `mock()`/`when()`/`verify()`; "over-stubbing is a code smell"/"avoid over-verification" | site.mockito.org (+ mockito wiki) | ☑ (identity; phrasing ⚠ verify at pin) |
| 8 | AssertJ / Hamcrest / Truth docs | fluent `assertThat().isEqualTo()` / matcher `assertThat(x, is())` / Truth fluent | assertj.github.io / hamcrest.org / truth.dev | ☑ (identity) |
| 9 | xUnit Patterns | test smells — Assertion Roulette, Eager Test, Mystery Guest, Fragile/Erratic/Slow (verbatim names + definitions) | xunitpatterns.com | ☑ (verbatim smell names) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Book (canon) | Mike Cohn, *Succeeding with Agile* (2009) — origin of the test pyramid | (cited; not redistributed) | ☐ corroboration (propose SOURCE-PIN §7 row) |
| 2 | Book (canon) | Gerard Meszaros, *xUnit Test Patterns* (2007) — test-smell catalogue | (cited; not redistributed) | ☐ corroboration (propose SOURCE-PIN §7 row) |
| 3 | Article | "Long Live The Test Pyramid" (Smashing Magazine, 2023) — modern restatement + trophy/honeycomb context | smashingmagazine.com/2023/09/long-live-test-pyramid/ | ☐ color/corroboration |

> Source-quality order applied: the pyramid article (Fowler site) + each tool's own docs → the named-book
> canon (Cohn/Meszaros, dated) → quality secondary articles (color/corroboration only). Every tool claim is
> cited to that tool's own source; the deep "which to choose / when" verdicts are routed to keys 42–52.
> "Live-line" = verified against current docs; re-verify byte-exact after `/pin-source` (pre-pin caveat).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | Read template + exemplars (13, 35) + SOURCE-PIN + NEUTRALITY + PIPELINE-LEARNINGS + CANDIDATE_POOL | repo | structure, depth bar, folklore list ("coverage % is not test quality"), umbrella scope, Part-V clusters |
| 2 | WebSearch test pyramid (Cohn/Fowler/Vocke) | martinfowler.com / secondaries | Cohn origin; Vocke "Practical Test Pyramid"; three layers; "much more low-level unit tests than high-level full-stack" |
| 3 | WebFetch practical-test-pyramid.html | martinfowler.com | **verbatim**: two rules; Unit/Integration/UI defs; ice-cream-cone; "E2E … notoriously flaky"; solitary-vs-sociable; "test observable behaviour, not implementation detail" |
| 4 | WebSearch + WebFetch PITest | pitest.org | **verbatim**: mutation = faults seeded then tests run; killed/lived; "coverage … does not check that your tests are actually able to detect faults"; "gold standard"; speed claim; DEFAULTS/STRONGER mutator groups |
| 5 | WebFetch JaCoCo counters | jacoco.org | **verbatim**: six counters (Instructions C0, Branches C1, Lines, Methods, Classes, Cyclomatic Complexity) + definitions; caveat coverage ≠ correctness |
| 6 | WebSearch JUnit 5 annotations + GAV | docs.junit.org | `@Test`/`@ParameterizedTest`/`@Nested` identity; `junit-jupiter`/`junit-jupiter-params` artifacts (versions ⚠) |
| 7 | WebSearch Testcontainers JUnit5 | java.testcontainers.org | `@Testcontainers`/`@Container` mechanism (static=shared, instance=per-method); sequential-only caveat; `org.testcontainers:junit-jupiter` |
| 8 | WebSearch jqwik | jqwik.net / secondaries | `@Property`/`@ForAll`/`@Provide`/`Arbitraries`; shrinking "simplest failing example"; max-discard-ratio default 5 (⚠) |
| 9 | WebSearch Mockito | site.mockito.org / wiki | `mock()`/`when()`/`verify()`; over-stubbing code smell / avoid over-verification (⚠ verbatim at pin) |
| 10 | WebSearch test smells + FIRST | xunitpatterns.com / Meszaros | Assertion Roulette / Eager Test / Mystery Guest / Fragile/Erratic/Slow (verbatim); FIRST = community heuristic (⚠) |

---
## Learnings & pipeline suggestions
- **Reusable shape — "two-axis test-quality framing" for the Part-V umbrella.** Keep **coverage (execution,
  JaCoCo)** and **mutation score (fault-detection, PITest)** as *independent axes*, and make the dangerous
  quadrant (high coverage / low mutation score = "vanity tests") the chapter's thesis and the companion demo's
  failure path. This operationalizes the folklore-list entry "coverage % is not test quality" as a *demonstrated*
  property, not an asserted one — the strongest way to teach a debunking. Reuse the axis framing in keys 47/48/80.
- **Folklore guard applied (durable principle #2).** "Coverage % as test quality" is stated-and-corrected with
  the debunking quoted from PIT's *own* docs ("coverage … does not check that your tests are actually able to
  detect faults") — the tool itself is the citable source for the limitation, which is stronger than a
  secondary debunking. Also flagged the **defaults traps**: JaCoCo `check` has no built-in coverage minimum;
  PIT's `DEFAULTS`/`STRONGER` mutator membership moves per version — never print one set as "the" default
  (extends the key-19 threshold rule to mutator sets).
- **SOURCE-PIN §7 canon gap (recurring class, cf. key-17 APoSD gap).** Cohn *Succeeding with Agile* (2009) and
  Meszaros *xUnit Test Patterns* (2007) are load-bearing Part-V authorities with no §7 row — propose adding
  both. Pattern: when a Part's foundational chapter (umbrella) relies on a named book, check it has a pin row.
- **Umbrella-routing discipline.** For a Foundational umbrella key (41, like 01/04/26/75), name every tool but
  push every *deep* decision to its owning chapter with an explicit route table (done in §2.3 and §7). This
  keeps the umbrella neutral (no crowning) and prevents the deep tool chapters (42–52) from being pre-empted.
- **Tooling.** `martinfowler.com/articles/practical-test-pyramid.html`, `pitest.org`, and
  `jacoco.org/.../counters.html` all WebFetch cleanly with verbatim atoms (no 403/curl workaround needed —
  contrast the openjdk JEP-403 pattern from keys 11/12/17). The pyramid article and the two tool docs are the
  three primary anchors; everything else routes to a tool's own pinned docs at `/pin-source`.
- **Cross-ref:** keys 42 (JUnit 5), 43 (assertions), 44 (mocking/over-mocking — owns solitary-vs-sociable
  depth), 45 (Testcontainers), 46 (property/parameterized/jqwik), 47 (mutation/PITest — owns cost), 48
  (coverage/JaCoCo — owns gate + coverage-isn't-quality depth; cluster 47/48), 49 (flakiness + test smells),
  50 (contract/API), 51/104 (JMH), 24 (concurrency testing), 76/80 (gates / "clean as you code"), 84 (review
  language for test smells), 88 (quality trend / report observability). Record in merge notes.
