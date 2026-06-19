# RESEARCH DOSSIER — Java Code Quality Book

> Part-V (Tier-B) dossier — **testing as a quality pillar**. The subject is **test architecture, flaky
> tests, and test smells**: how a Java test suite is *structured* (the test pyramid; isolation & lifecycle;
> ordering & parallelism), why suites become **non-deterministic (flaky)** and how to find/fix that, and the
> catalogue of **test smells** (the code-quality of the *test code itself*). Row 49 carries **no `⚠`** in
> `CANDIDATE_POOL.md` (it is not a head-to-head tool comparison), but NEUTRALITY still applies wherever tools
> appear (JUnit 5, Awaitility, Testcontainers, Surefire/Failsafe, Develocity): each gets its strongest case
> **and** its hardest limitation, every fact is cited to that tool's own pinned source, no tool is crowned.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. Every testing tool is `TO-PIN` in `SOURCE-PIN.md`
> §3, so **API/annotation/config-key identity** is verified from each tool's own docs while exact **version
> numbers, defaults, and GAV coordinates** carry `⚠ verify at pin`. Empirical flakiness figures (Google /
> Micco, the Luo et al. taxonomy) are **published statistics** cited exactly to their source — never folklore.
> The **folklore guard is load-bearing here**: "coverage % = test quality" is on the PIPELINE-LEARNINGS
> folklore list and is framed correctly (coverage is necessary, not sufficient; see keys 47/48).

---

## Topic
- **Key:** 49 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Test architecture, flakiness & test smells — structuring a suite, killing non-determinism, and the code-quality of test code
- **Part:** Part V — Testing as a quality pillar (cluster 41–52; **key 41** owns the pyramid/landscape umbrella, **47/48** the mutation-vs-coverage effectiveness story, **44** the over-mocking objection, **45** integration/Testcontainers depth, **46** parameterized/property-based)
- **Tier:** B (Part V chapter) · **Depth band:** Standard (concept + concrete JUnit-5/tool mechanism; doc-anchored)
- **Cmp:** *not* flagged `⚠` in `CANDIDATE_POOL.md` row 49 (no per-tool ranking is the chapter's purpose).
  NEUTRALITY discipline still binds every tool mention: JUnit 5, Awaitility, Testcontainers, Maven
  Surefire/Failsafe, Gradle/Develocity each get strongest-case + hardest-limitation, cited to their own
  pinned source; **no tool crowned**; banned phrasings (`better than`, `unlike X`, `superior`, `beats`,
  `the problem with X`) excluded (self-checked — see §9). The **subject** (the *concepts* of test
  isolation, determinism, the pyramid, test-code quality) is discussed freely.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (identity verified; versions/GAV `⚠ verify at pin`):**
  - **JUnit 5 (Jupiter)** — `org.junit.jupiter:junit-jupiter`; the structure & determinism surface:
    `@TestInstance(Lifecycle.PER_METHOD)` (default) vs `Lifecycle.PER_CLASS`; `@TestMethodOrder` + built-in
    `MethodOrderer` (`MethodName`, `DisplayName`, `OrderAnnotation`, `Random`); `@Order`; parallel-execution
    config keys `junit.jupiter.execution.parallel.enabled`, `…parallel.mode.default`,
    `…parallel.mode.classes.default`; `@Execution(ExecutionMode.CONCURRENT|SAME_THREAD)`; `@ResourceLock`
    (`Resources.SYSTEM_PROPERTIES`); `@Isolated`; `@Nested`, `@Tag`, `@Disabled`, `@RepeatedTest`,
    `assertTimeoutPreemptively`. (Source: JUnit 5 User Guide.)
  - **Awaitility** — `org.awaitility:awaitility`; `await().atMost(…).until(…)` polling DSL (default poll
    interval 100 ms — verify at pin) — the async-wait flakiness fix that replaces `Thread.sleep(...)`.
  - **Testcontainers (Java)** — `org.testcontainers:*`; `@Testcontainers` / `@Container` lifecycle
    (static field = once per class), `withReuse(true)` + `testcontainers.reuse.enable=true` (experimental,
    not for CI) — realistic, isolated external state to kill environment-coupling flakiness.
  - **Maven Surefire / Failsafe** — `org.apache.maven.plugins:maven-surefire-plugin` /
    `…maven-failsafe-plugin`; `rerunFailingTestsCount`, `failOnFlakeCount` (Surefire ≥ 3.0.0-M6), the
    `flakyFailure`/`flakyError` XML elements — flaky *detection*, NOT a fix.
  - **Gradle Develocity** (the Gradle build-scan / flaky-test-detection service) — records a **FLAKY**
    outcome when a test fails then passes; named as one approach to flaky tracking (cite Develocity docs;
    GAV/feature gating `⚠ verify at pin`).
  - **Named-canon (secondary):** the **test pyramid** (Mike Cohn, *Succeeding with Agile*, 2009; developed
    by Martin Fowler) and the **ice-cream-cone** anti-pattern; **xUnit Test Patterns** (Gerard Meszaros,
    2007) and **van Deursen et al. (2001)** "Refactoring Test Code" — the test-smell catalogue.
- **Canonical doc page(s):** JUnit 5 User Guide §"Test Instance Lifecycle", §"Test Execution Order",
  §"Parallel Execution" (`docs.junit.org/current/user-guide/`); Awaitility wiki "Usage"; Testcontainers
  "Reusable Containers" + JUnit-5 lifecycle guide (`java.testcontainers.org`); Maven Surefire "Rerun failing
  tests"; Martin Fowler "TestPyramid" (`martinfowler.com/bliki/TestPyramid.html`); xunitpatterns.com (test
  smells); Google Testing Blog / Micco "The State of Continuous Integration Testing @ Google"
  (`research.google.com/pubs/archive/45880.pdf`); van Deursen et al. & Luo et al. (academic, the taxonomy).
- **Canonical source path(s):** companion artifact `08-companion-code/49_test_architecture_flakiness/`.
  Tool sources under `SOURCE-PIN.md` §3 (`junit.org/junit5`, `testcontainers.org`, etc.), all `TO-PIN`.

---

## 1. Core definition & purpose

**Central claim.** The quality of a test suite is not the same as its *coverage number*. A suite is good when
it is (a) **well-architected** — the right test at the right level (the pyramid), each test **isolated** so
it can run in any order and in parallel; (b) **deterministic** — it passes iff the code is correct, never
"sometimes red" (the absence of **flakiness**); and (c) **clean as code** — free of **test smells** that make
test code hard to read, trust, and maintain. This chapter is the "quality of the tests themselves" pillar; it
sits beside, not inside, the effectiveness pillar (mutation/coverage, keys 47/48) and the mocking-discipline
pillar (key 44).

Three subjects, three primary problems:
1. **Test architecture** — *where* a behaviour is tested. The **test pyramid** (Cohn, developed by Fowler):
   "fewer high-level tests and more unit tests that are fast, cheap, and reliable," many fast unit tests at
   the base, fewer service/integration tests above, fewest UI/end-to-end at the top. The named
   anti-pattern is the **ice-cream cone** — an inverted pyramid where effort piles into slow, brittle
   UI/end-to-end tests (Fowler/Savoia). *(Cohn/Fowler are named-canon secondary sources — §8.)*
2. **Flakiness** — a **flaky test** is one whose pass/fail outcome is **non-deterministic for the same code**
   (it "fails randomly"). Flaky tests destroy the signal value of the suite: a red build no longer reliably
   means "the code is broken." Micco's Google study reports almost **16%** of Google's tests "have some level
   of flakiness," and that when a test transitions pass→fail in post-submit CI, **84%** of the time it is a
   flake, not a real regression (cited exactly — §3).
3. **Test smells** — recurring poor patterns in *test code* (van Deursen et al. 2001; catalogued by Meszaros,
   xUnit Test Patterns): **Assertion Roulette**, **Eager Test**, **Mystery Guest**, **General Fixture**, and
   others. These are the code-smell catalogue (key 19) applied to the test source.

**Which part of the pinned set provides the mechanism.** The *enforcement & control surface* is **JUnit 5**:
test isolation (instance lifecycle), determinism controls (ordering, parallelism, resource locks), and the
async-wait fix lives in **Awaitility**; external-state isolation in **Testcontainers**; flaky *detection* in
**Surefire/Failsafe** (`rerunFailingTestsCount`) and **Develocity**. The *taxonomies* (pyramid, smell
catalogue, the ten flakiness root-cause categories) are named-canon / published-study secondary sources.

**Where it sits in the architecture.** This is **test-time / build-time** quality: the suite runs under the
build's test phase (Surefire = unit, Failsafe = integration), and these controls shape *how* it runs
(serial vs parallel, fresh vs shared instance, real vs mocked external state). It does not change production
code behaviour; it changes the *reliability of the quality signal*.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Test architecture — the pyramid and the unit-vs-integration split (build wiring)

The pyramid is a *ratio* heuristic, not a law: more cheap fast tests low, fewer expensive slow tests high.
In Maven/Gradle it maps to a **build-phase split**:
- **Unit tests** run under **Maven Surefire** (`maven-surefire-plugin`) in the `test` phase — fast, in-JVM,
  no external services.
- **Integration tests** run under **Maven Failsafe** (`maven-failsafe-plugin`) in `integration-test`/`verify`
  — slower, may start Testcontainers, a web server, etc. The Surefire/Failsafe split is itself the
  pyramid's build expression (cite each plugin's own docs). *(Plugin coordinates `⚠ verify at pin`.)*

Honest limitation built into the model: the pyramid is a *guideline*, and Fowler himself notes the layer
names are fuzzy and the shape is a "rule of thumb" — teams over-applying it can under-test genuine
integration seams. The ice-cream cone is the failure mode of ignoring it; "all unit, no integration" is the
opposite failure (false confidence — key 44 over-mocking ties in). Crown neither shape: state the trade-off.

### 2.2 Test isolation — the JUnit 5 instance lifecycle (the determinism foundation)

JUnit's default isolation is the root cause-prevention for **order-dependency** flakiness:

- **Default (`Lifecycle.PER_METHOD`):** "JUnit creates a new instance of each test class before executing
  each test method to allow individual test methods to be executed in isolation" (verified, User Guide). A
  fresh instance per method means instance fields cannot leak state between tests.
- **`@TestInstance(Lifecycle.PER_CLASS)`:** "a new test instance will be created once per test class"
  (verified). This *shares* instance state across methods — convenient (one `@BeforeAll` non-static
  method) but it **reintroduces the order-dependency risk** the per-method default removes; the test author
  now owns isolation. This is the §4 honest limitation of PER_CLASS.

The teaching point: **shared mutable state between tests is the primary engine of order-dependent
flakiness.** Order-dependency is the single most-cited flakiness category in empirical work (Gruber et al.
found it responsible for **59%** of flaky tests in a Python corpus — cite exactly; language-specific, not a
Java number, framed as such).

### 2.3 Test ordering — `@TestMethodOrder` and the "random by default is good" stance

- **Order is deliberately under-specified.** By default JUnit does not guarantee method execution order — a
  design choice that *surfaces* hidden inter-test dependencies (a suite that only passes in one order is
  buggy). To control order, annotate with **`@TestMethodOrder`** and a **`MethodOrderer`**; built-ins:
  **`MethodOrderer.MethodName`**, **`MethodOrderer.DisplayName`**, **`MethodOrderer.OrderAnnotation`** (uses
  `@Order(n)`), and **`MethodOrderer.Random`** (verified, User Guide).
- **`MethodOrderer.Random`** is the flakiness-hunting tool: running methods in random order each build makes
  an order-dependent test fail *visibly* instead of hiding. (`@Order` is the opposite — pinning an order,
  appropriate only for genuinely sequential scenario tests, which then must be honest about the coupling.)

### 2.4 Parallel execution — speed vs new flakiness, and how JUnit guards it

Parallelism is the classic *speed-for-determinism-risk* trade-off; JUnit 5 makes it opt-in and bounded:

- **Enable:** set `junit.jupiter.execution.parallel.enabled=true` (config property). Default execution mode
  per node: `junit.jupiter.execution.parallel.mode.default`; class-level mode:
  `…parallel.mode.classes.default` (verified, User Guide).
- **Per-element control:** **`@Execution(ExecutionMode.CONCURRENT)`** / **`@Execution(SAME_THREAD)`**.
- **Built-in guard against the new race:** "The default execution mode is applied to all nodes … with a few
  notable exceptions, namely test classes that use the `Lifecycle.PER_CLASS` mode or a `MethodOrderer`. In
  the former case, test authors have to ensure that the test class is thread-safe; in the latter, concurrent
  execution might conflict with the configured execution order. Thus, in both cases, test methods … are only
  executed concurrently if the `@Execution(CONCURRENT)` annotation is present" (verified verbatim, User
  Guide). JUnit *refuses* to silently parallelise the two shapes most likely to break.
- **Shared-resource serialisation:** **`@ResourceLock`** (e.g. `Resources.SYSTEM_PROPERTIES`) serialises
  tests touching the same shared resource; **`@Isolated`** runs a class entirely alone. These are the
  determinism levers for tests that *cannot* be made independent (env vars, system properties, a singleton).

This is the chapter's clearest "speed has a cost" honesty: enabling parallelism can *create* flakiness in
tests that secretly share state — the fix is `@ResourceLock`/`@Isolated`, not disabling parallelism wholesale.
(Cross-ref key 20/23 thread-safety: a flaky parallel test often exposes a real data race in the code under
test, not just a test bug.)

### 2.5 Flakiness root causes — the empirical taxonomy and the Java fix per cause

Luo et al. (*An Empirical Analysis of Flaky Tests*, FSE 2014) split flaky root causes into **ten
categories**: **async wait, concurrency, test order dependency, resource leak, network, time, IO,
randomness, floating-point operations, unordered collections** (cited exactly). The Java fix per cause:

| Root cause | Java symptom | Determinism fix (Java tool) |
|---|---|---|
| **Async wait** | `Thread.sleep(2000)` hoping the work finished | **Awaitility** `await().atMost(…).until(condition)` — polls (default 100 ms — verify at pin) and proceeds the instant the condition holds (cite Awaitility wiki) |
| **Test order dependency** | passes alone, fails in suite | per-method lifecycle (default); hunt with `MethodOrderer.Random`; never lean on `@Order` to mask coupling |
| **Concurrency** | races in the code under test | `@Execution`, `@ResourceLock`; reproduce/grade with JCStress (key 24) |
| **Network / IO** | hitting a real host/disk | **Testcontainers** (controlled, disposable real services), or a fake at a seam |
| **Time** | `LocalDateTime.now()` in an assertion | inject a `Clock` (`Clock.fixed`), assert against it |
| **Randomness** | unseeded `Random`/UUID in assertions | seed the RNG; property-based tests record the failing seed (key 46) |
| **Unordered collections** | asserting `HashMap`/`HashSet` iteration order | assert with order-independent matchers (AssertJ `containsExactlyInAnyOrder`, key 43) |
| **Resource leak** | leaked port/file from a prior test | close in `@AfterEach`/try-with-resources (key 16); fresh fixtures |

`assertTimeoutPreemptively(...)` bounds a hang into a deterministic failure rather than a CI timeout
(verified, User Guide — preemptive variant runs the executable in a separate thread).

### 2.6 Detecting flakiness — rerun-and-quarantine, and its honest limit

- **Maven Surefire / Failsafe `rerunFailingTestsCount`:** "set the `rerunFailingTestsCount` property to be a
  value larger than 0, and tests will be run until they pass or the number of reruns has been exhausted"
  (verified, Surefire docs). Supported for JUnit 5.x "since version 3.0.0-M4." The report XML gains
  **`flakyFailure`/`flakyError`** elements per re-run. Since **3.0.0-M6**, `rerunFailingTestsCount` can be
  paired with **`failOnFlakeCount`** to fail the build when more than N tests flake (verified). *(Plugin
  version `⚠ verify at pin`.)*
- **Gradle / Develocity:** "If a test passes and then fails, Develocity will record a FLAKY outcome" — a
  history/quarantine surface (cite Develocity docs; feature gating `⚠ verify at pin`).

**The hard objection (HONEST-LIMITATIONS, load-bearing).** Rerun-until-green is **detection and triage, not a
cure**: auto-retrying flaky tests *hides* the non-determinism and can mask a real intermittent bug in the
code under test (the very race a flaky test sometimes reveals — §2.4). The discipline is: rerun to *detect
and quarantine*, then **fix the root cause**, not to make red builds green. Google's mitigation is to *track
and quarantine* flakes, then deflake — not to retry indefinitely. State this explicitly so a reader does not
read `rerunFailingTestsCount` as a fix.

### 2.7 Test smells — the code-quality of test code (the catalogue)

Test smells are the key-19 smell catalogue applied to *test code* (van Deursen et al. 2001; Meszaros, xUnit
Test Patterns). Each entry: name → symptom → refactoring.

| Smell | Symptom (verified definition) | Refactoring |
|---|---|---|
| **Assertion Roulette** | "hard to tell which of several assertions … caused a test failure … one cannot determine exactly which assertion had failed" | give each assertion a message; one logical assertion per test; AssertJ `assertThat` chains / `assertAll` (key 43) |
| **Eager Test** | "verifying too much functionality in a single Test Method … checks more than one method of the class" | split into focused tests (one behaviour each) |
| **Mystery Guest** | "the test reader is not able to see the cause and effect between fixture and verification … part of it is done outside the Test Method" (external file/DB) | inline the fixture, or make the dependency explicit (Testcontainers, a builder) |
| **General Fixture** | "building or referencing a larger fixture than is needed to verify the functionality" | minimal per-test fixtures; avoid a giant shared `@BeforeEach` |
| **(others, attributed)** | Lazy Test, Sensitive Equality, Indirect Testing, Test Code Duplication (van Deursen et al.) | see source; map each to a refactoring |

Definitions above are quoted from xunitpatterns.com / the test-smell literature; attribute the catalogue,
don't assert it as Java-tool output. *Tool-found vs review-found honesty (key-19 lesson):* some test smells
have detectors (e.g. PMD's JUnit ruleset flags some patterns; **`tsDetect`** is a research test-smell
detector — cite if used, version `⚠ verify at pin`), but most are **review-found**, not gated. Don't imply
full automated enforcement.

### 2.8 Reference units (API / config keys / coordinates — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `@TestInstance(Lifecycle.PER_METHOD)` | lifecycle | **default**; new instance per method | tool-version | JUnit User Guide ✅ (verbatim) |
| `@TestInstance(Lifecycle.PER_CLASS)` | lifecycle | one instance per class (shares state) | tool-version | JUnit User Guide ✅ (verbatim) |
| `@TestMethodOrder` + `MethodOrderer` | ordering | `MethodName`/`DisplayName`/`OrderAnnotation`/`Random` | tool-version | JUnit User Guide ✅ |
| `@Order(n)` | ordering | used by `MethodOrderer.OrderAnnotation` | tool-version | JUnit User Guide ✅ |
| `junit.jupiter.execution.parallel.enabled` | config key | parallelism off by default | tool-version | JUnit User Guide ✅ |
| `…parallel.mode.default` / `…parallel.mode.classes.default` | config key | per-node / per-class mode | tool-version | JUnit User Guide ✅ |
| `@Execution(ExecutionMode.CONCURRENT\|SAME_THREAD)` | annotation | per-element execution mode | tool-version | JUnit User Guide ✅ |
| `@ResourceLock` (`Resources.SYSTEM_PROPERTIES`) | annotation | serialise shared-resource tests | tool-version | JUnit User Guide ✅ |
| `@Isolated` | annotation | run class entirely alone | tool-version | JUnit User Guide ✅ |
| `@RepeatedTest`, `assertTimeoutPreemptively` | annotation/assert | repeat N; bound a hang to a failure | tool-version | JUnit User Guide ✅ |
| `await().atMost(…).until(…)` | Awaitility DSL | poll (default 100 ms — ⚠ verify at pin) | tool-version | Awaitility wiki ✅ (DSL); default ⚠ |
| `@Testcontainers` / `@Container` | annotation | static field = once per class | tool-version | Testcontainers docs ✅ |
| `withReuse(true)` + `testcontainers.reuse.enable=true` | reuse | **experimental; not for CI** | tool-version | Testcontainers "Reusable Containers" ✅ (verbatim caveat) |
| `rerunFailingTestsCount` | Surefire/Failsafe param | 0 (off); JUnit5 since 3.0.0-M4 | tool-version | Surefire docs ✅; version ⚠ verify |
| `failOnFlakeCount` | Surefire param | since 3.0.0-M6 | tool-version | Surefire docs ✅; version ⚠ verify |
| `flakyFailure` / `flakyError` | report XML element | added when rerun > 0 | tool-version | Surefire docs ✅ |
| Develocity FLAKY outcome | service feature | fail-then-pass → FLAKY | service-version | Develocity docs ✅; gating ⚠ verify |

---

## 3. Evidence FOR

- **Per-method isolation is the default — flakiness prevention by construction.** JUnit "creates a new
  instance of each test class before executing each test method to allow individual test methods to be
  executed in isolation" (verified verbatim). The framework's default fights order-dependency for you.
- **Random ordering surfaces hidden coupling.** `MethodOrderer.Random` (verified built-in) turns a latent
  order-dependent bug into a visible failure — the suite tells you it is non-deterministic instead of hiding
  it behind a lucky default order.
- **Parallelism is bounded, not reckless.** JUnit 5 refuses to silently parallelise `PER_CLASS` /
  `MethodOrderer` classes (verified verbatim §2.4), and offers `@ResourceLock`/`@Isolated` for shared state —
  so you can buy speed without blindly inheriting races.
- **The async-wait fix is concrete.** Awaitility's `await().atMost(…).until(…)` polling DSL replaces
  `Thread.sleep` — it "lets you check frequently if the execution is complete and … continue running right
  away" (cite Awaitility). This converts the #1 timing-flake source into a deterministic wait.
- **Flaky detection has first-class build support.** Surefire/Failsafe `rerunFailingTestsCount` +
  `failOnFlakeCount` (≥ 3.0.0-M6) and the `flakyFailure`/`flakyError` XML (verified, Surefire docs) give a
  build-native way to *quantify* flakiness; Develocity records a FLAKY outcome over history (verified).
- **Published flakiness figures (cited exactly, not folklore).** From Micco / Google's CI study
  ("The State of Continuous Integration Testing @ Google"): "almost **16%** of all tests at Google exhibit
  some level of flakiness"; when a test transitions pass→fail in post-submit CI, "**84%** of the time it is
  a flaky test, not a real regression." These are *published statistics*, cited to the source — the chapter's
  empirical anchor for "flakiness is common and expensive."
- **The pyramid is a long-standing, named heuristic.** Introduced by Mike Cohn (*Succeeding with Agile*,
  2009), developed by Martin Fowler ("TestPyramid"): "fewer high-level tests and more unit tests that are
  fast, cheap, and reliable" — with the **ice-cream cone** as the named inverse anti-pattern (Fowler/Savoia).
- **The test-smell catalogue is decades-mature.** van Deursen et al. (2001) "Refactoring Test Code" and
  Meszaros' *xUnit Test Patterns* (2007) give named, refactorable smells (Assertion Roulette, Eager Test,
  Mystery Guest, General Fixture) — the test code gets the same quality vocabulary as production code (key 19).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each lever's hardest objection + when-NOT)

**Each control's hardest objection (cited to its own source / behaviour).**
- *`Lifecycle.PER_CLASS` reintroduces order risk.* Sharing one instance across methods (verified) means
  instance fields can leak between tests — the author now owns isolation. *When NOT:* don't use PER_CLASS
  merely to avoid `static` in `@BeforeAll`; the convenience can buy back the flakiness the per-method default
  removed.
- *Parallel execution can create flakiness.* Enabling
  `junit.jupiter.execution.parallel.enabled=true` runs tests concurrently; tests that secretly share state
  (system properties, singletons, the DB) can race. The fix is `@ResourceLock`/`@Isolated`, but a suite full
  of locks is barely parallel — *when NOT:* don't parallelise a suite riddled with shared state until the
  state is isolated; you'll trade flakiness for speed you can't actually use.
- *Rerun-until-green hides bugs (the load-bearing objection).* `rerunFailingTestsCount` is **detection, not a
  cure**: auto-retrying a flaky test can mask a genuine intermittent defect (a real race in the code under
  test). *When NOT:* never use reruns as a way to keep a known-flaky test in the green path indefinitely —
  quarantine and deflake instead (Google's own model is track-and-quarantine, then fix).
- *Testcontainers reuse is explicitly not for CI.* "Reusable containers are not suited for CI usage and as an
  experimental feature not all Testcontainers features are fully working"; reuse requires
  `testcontainers.reuse.enable=true` in the local `~/.testcontainers.properties` (verified verbatim). *When
  NOT:* don't enable reuse in CI to speed builds — it can leak state between runs and is unsupported there.
- *Awaitility has a sharp edge.* It "catches all uncaught exceptions by default," so migrating from
  `Thread.sleep` can change which exceptions surface (cite Awaitility). And a too-short `atMost` reintroduces
  the very timeout-flake it was meant to remove. *When NOT:* don't set `atMost` so tight it depends on
  machine speed.
- *The pyramid is a heuristic, not a law.* Fowler frames the layer names as fuzzy and the shape as a "rule of
  thumb." *When NOT:* don't treat a literal 70/20/10 ratio as a target, and don't let "push everything down
  to unit tests" become over-mocking (key 44) that tests mocks, not behaviour. Crown neither pyramid nor any
  rebalanced shape — state the trade-off (test speed/cost vs realism/confidence).
- *Test smells are mostly review-found.* Most catalogue entries have no reliable automated detector (key-19
  honesty); a chapter must not imply full gating. Some patterns are flagged by PMD's JUnit rules or research
  tools (`tsDetect`), but the catalogue is primarily a *review* vocabulary.

**The folklore guard (load-bearing — PIPELINE-LEARNINGS folklore list).** "Code coverage % as a measure of
test quality" is **folklore**: coverage is *necessary, not sufficient*. A 100%-covered suite can still be
flaky, full of test smells, and assert nothing meaningful (Assertion-free / Eager tests). Test *quality* is
measured by **effectiveness** (mutation score, key 47), **determinism** (flake rate, this chapter), and
**clarity** (smell-free, this chapter) — not the coverage number alone (keys 47/48). State coverage's role
correctly; never present a coverage % as proof of test quality.

**Shared limit (the honest centre).** None of these controls make a test *correct* — they make it
*deterministic and legible*. A deterministic test that asserts the wrong thing is still a bad test; that is
the effectiveness pillar's job (mutation testing, key 47). This chapter is necessary but not sufficient.

---

## 5. Current status

- **JUnit 5 (Jupiter) — current, stable.** The lifecycle/ordering/parallel surface above is stable in the
  Jupiter line; parallel execution is documented as a stable feature (was experimental in early 5.x — the
  config keys are now standard). *Exact JUnit 5 version is `TO-PIN` in `SOURCE-PIN.md` §3 — newer docs (6.x
  in some User-Guide URLs encountered) are AHEAD of the testing pin until pinned; verify the section text at
  the pinned 5.x version. Any 6.x-only API is `⚠ AHEAD-OF-PIN`.* JUnit 5 runs on Java 17+ (anchor 21 OK).
- **Awaitility — stable.** Polling DSL stable; default poll interval (100 ms) `⚠ verify at pin`.
- **Testcontainers — stable core; reuse experimental.** Reuse is explicitly experimental + not-for-CI
  (verified). Version `⚠ verify at pin`.
- **Surefire/Failsafe — stable.** `rerunFailingTestsCount` (JUnit5 since 3.0.0-M4), `failOnFlakeCount`
  (since 3.0.0-M6) are released; plugin version `⚠ verify at pin`.
- **Flakiness as a discipline — growing.** A large empirical/academic literature (Luo et al. 2014 taxonomy;
  the 2021 *A Survey of Flaky Tests*, ACM Comput. Surv.) and industrial tooling (Develocity flaky detection,
  Gradle/Maven rerun, IDE flaky markers) have matured since ~2016. Figures are *published studies*, cited
  with date and source — never restated as a universal constant.
- **Java-25 deltas.** Virtual threads (JEP 444, final @21) and structured concurrency (JEP 505, **preview**
  @25 — `⚠ AHEAD-OF-PIN`, never assert as stable; cross-ref keys 22/24) change *how* concurrent code under
  test is written, which intersects concurrency-flakiness; mention as direction, anchor on 21.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `49_test_architecture_flakiness` *(row to be added —
  `DEMO-CATALOG.md` does not yet exist in the repo; see §7 flag, consistent with keys 24/25/35 catalog-gap
  notes).*
  - **Demo name:** "From flaky to deterministic — diagnosing and fixing a non-deterministic Java suite."
  - **Java Quality surface exercised:** a small `org.acme.orders` module whose tests demonstrate **four
    seeded flakiness causes and their fixes**: (1) an **async-wait** test that uses `Thread.sleep` (flaky)
    refactored to **Awaitility** `await().atMost(…).until(…)`; (2) an **order-dependent** pair of tests
    sharing a static field (flaky under `MethodOrderer.Random`) fixed by removing shared state / per-method
    lifecycle; (3) a **time** flake (`LocalDateTime.now()` in an assertion) fixed by injecting
    `Clock.fixed`; (4) an **unordered-collection** flake (asserting `HashSet` order) fixed with AssertJ
    `containsExactlyInAnyOrder`. An integration test uses **Testcontainers** (`@Testcontainers`/`@Container`)
    for a real Postgres to show environment isolation. The build wires **Surefire** (unit) +
    **Failsafe** (integration) with `rerunFailingTestsCount` set to *detect* (not mask) any residual flake.
    Each test also illustrates a **test smell fixed**: an Assertion-Roulette test (many bare asserts) becomes
    an `assertAll` / messaged-assert test (key 43).
  - **TRY-IT exercise:** run the suite with `MethodOrderer.Random` enabled and watch the order-dependent test
    fail intermittently; then apply the isolation fix and watch it pass in any order. Set
    `rerunFailingTestsCount=2` and observe the `flakyFailure` XML on a deliberately-left-flaky test, proving
    detection ≠ fix. This makes "determinism is engineered, not hoped for" tactile.
- **Module key / path:** `08-companion-code/49_test_architecture_flakiness/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ (anchor) verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | the harness + isolation/ordering/parallel surface (primary unit) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | order-independent + readable assertions (anti-Assertion-Roulette) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.awaitility:awaitility` | async-wait fix (replaces `Thread.sleep`) | awaitility wiki (TO-PIN) | ☐ verify at pin |
  | `org.testcontainers:postgresql` (+ `testcontainers`, `junit-jupiter`) | real disposable external state | testcontainers.org (TO-PIN) | ☐ verify at pin |
  | `org.apache.maven.plugins:maven-surefire-plugin` (`rerunFailingTestsCount`) | unit phase + flaky detection | Surefire docs (TO-PIN) | ☐ verify at pin |
  | `org.apache.maven.plugins:maven-failsafe-plugin` | integration phase | Failsafe docs (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose versions; plugin/dep versions managed.
  - **Externalized config / profiles** — `junit-platform.properties` carrying
    `junit.jupiter.execution.parallel.enabled` and the mode keys; Surefire `rerunFailingTestsCount` /
    `failOnFlakeCount` in the POM; Testcontainers config kept out of CI-reuse (the experimental caveat noted
    in a comment). Trace each key to its tool's doc.
  - **At least one test** — names the behaviour it asserts (the order-independent `OrderProcessor` behaviour).
  - **Observability / health surface** — the Surefire/Failsafe report (`flakyFailure`/`flakyError` XML) is
    the flakiness observability surface; the chapter points at it (key 88 CI dashboards).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **seeded order-dependent test** run under
    `MethodOrderer.Random` is the demonstrated failure path — it fails intermittently *by design* to prove
    flakiness, then the isolation fix makes it deterministic. State that `rerunFailingTestsCount` would
    *hide* this and is intentionally used only to *detect*, not to mask (the §4 honesty in code).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `flaky-sleep` | the `Thread.sleep` async test (failure path) | `AsyncProcessorTest.java` |
  | `fixed-awaitility` | the Awaitility `await().atMost().until()` deterministic version | `AsyncProcessorTest.java` |
  | `order-dependent` | the shared-static order-dependent pair + `@TestMethodOrder(Random)` | `OrderLeakTest.java` |
  | `isolation-fix` | per-method isolation / `Clock.fixed` / `containsExactlyInAnyOrder` fixes | `OrderProcessorTest.java` |
  | `parallel-config` | `junit-platform.properties` parallel keys + `@ResourceLock` | `junit-platform.properties` / `SysPropTest.java` |
  | `rerun-config` | Surefire `rerunFailingTestsCount` + `failOnFlakeCount` | `pom.xml` |

- **Run command:** `./mvnw -B verify` (Failsafe integration tests need Docker for Testcontainers).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the seeded flakes + `MethodOrderer.Random`, the order-dependent test fails
  intermittently (and the `rerunFailingTestsCount` rerun records a `flakyFailure`); after the isolation
  fixes, the suite is green in any order and any thread, with the Testcontainers integration test passing
  against a real Postgres. Test pass count green; `flakyFailure` count → 0 once root causes are fixed.
- **Figure plan** (GUIDELINES §8; **standard Part-V concept+mechanism chapter** → image budget ~**2 designed
  diagrams + 0–1 captured screenshot**; not a zero-figure chapter — the pyramid and the flake-fix flow are
  inherently spatial):
  - **Chapter class:** standard Part-V chapter with strong conceptual diagrams and a small captured surface.
  - **Candidate designed diagram(s) + family:**
    - **Fig 49.1 — "The test pyramid and the ice-cream cone" (architecture/ratio diagram):** the pyramid
      (many unit → fewer integration → fewest UI/e2e) beside the inverted ice-cream-cone anti-pattern, with
      the Surefire/Failsafe build-phase mapping. Family = *layered-ratio / anti-pattern-contrast diagram*.
      Trace: pyramid + ice-cream cone → Fowler "TestPyramid" / Cohn (named-canon); phase mapping → Surefire/
      Failsafe docs.
    - **Fig 49.2 — "Flakiness root causes → the Java fix" (taxonomy/flow diagram):** the Luo et al. ten
      categories on the left, the JUnit-5/Awaitility/Testcontainers/Clock fix per cause on the right, with
      the "rerun = detect, not fix" caveat called out. Family = *taxonomy → remedy map*. Trace: ten
      categories → Luo et al. 2014; each fix → that tool's own doc (Awaitility/JUnit/Testcontainers).
  - **Candidate captured surface(s):**
    - **Fig 49.3 (optional)** — a capture of a Surefire report / `flakyFailure` XML (or an IDE's
      run-test-N-times / flaky marker) showing detection. Capture only real tool output (technical profile
      allows tool screenshots).
  - **Source trace per depicted claim:** pyramid labels → Fowler/Cohn; phase split → Surefire/Failsafe docs;
    lifecycle/ordering/parallel labels → JUnit 5 User Guide; ten root causes → Luo et al. 2014; per-cause fix
    → each tool's doc; the 16%/84% figures → Micco/Google CI study.

---

## 7. Gap-filling (verification queue)

- ⚠ **Testing-tool versions / GAVs** — `org.junit.jupiter:junit-jupiter`, `org.awaitility:awaitility`,
  `org.testcontainers:*`, `maven-surefire-plugin`/`maven-failsafe-plugin`: all `TO-PIN` in `SOURCE-PIN.md` §3
  → confirm exact latest-stable versions + coordinates at pin before stating any number. API/annotation/
  config-key *identity* verified from each tool's docs.
- ⚠ **JUnit User-Guide version drift (AHEAD-OF-PIN risk)** — several User-Guide URLs encountered are **6.x**
  (e.g. `docs.junit.org/6.1.0/…`); the testing pin is a 5.x line (`TO-PIN`). Re-verify the lifecycle/ordering/
  parallel section *text* at the **pinned 5.x** version; any API present only in 6.x is `⚠ AHEAD-OF-PIN`.
- ⚠ **Awaitility default poll interval (100 ms)** — stated in secondary sources; confirm verbatim against the
  Awaitility wiki/Javadoc at pin.
- ⚠ **Surefire `rerunFailingTestsCount` JUnit-5 support version (3.0.0-M4) / `failOnFlakeCount` (3.0.0-M6)** —
  verified from Surefire docs as "since" versions; re-confirm at the pinned plugin version.
- ⚠ **Google/Micco figures (16% / 84% / 1.5%)** — verified to the Google CI study + Google Testing Blog
  (the blog post itself is qualitative; the percentages trace to Micco's "State of CI Testing @ Google"
  slides/paper, `research.google.com/pubs/archive/45880.pdf`). Cite the *paper/slides* as the source of the
  numbers, the blog as the narrative — re-confirm the exact figures against the PDF at draft.
- ⚠ **Flakiness-category percentages (order-dependency 59%)** — Gruber et al. is a **Python** corpus number;
  cite as language-specific, never as a Java/universal figure. The **ten-category taxonomy** is Luo et al.
  2014 (FSE) — confirm the category list verbatim at draft.
- ⚠ **Test-smell definitions** — Assertion Roulette / Eager Test / Mystery Guest / General Fixture quoted
  from xunitpatterns.com / van Deursen et al.; re-confirm verbatim wording (avoid quote-drift, keys 19/25)
  and attribute each. **Test-smell detector tools** (`tsDetect`; PMD JUnit rules) — names/versions
  `⚠ verify at pin`; most smells are review-found, not gated (key-19 honesty).
- ⚠ **Develocity FLAKY-outcome feature gating** — verified the FLAKY-outcome behaviour from Develocity docs;
  exact feature/edition gating `⚠ verify at pin`.
- **Open question (draft / Part V routing):** boundary with **key 41** (pyramid/landscape umbrella — keep the
  *deep* pyramid framing there; this chapter uses it as context, owns flakiness + smells + isolation
  mechanics), **key 42** (JUnit 5 structure/quality patterns — coordinate on lifecycle overlap), **key 44**
  (over-mocking — the "all unit, no integration" failure mode; route the mocking verdict there), **key 45**
  (Testcontainers depth — this chapter uses it only for state-isolation), **key 47/48** (effectiveness vs
  coverage — route "coverage ≠ quality" depth there; this chapter states the folklore correctly), **key 24**
  (concurrency reproduction/JCStress — a concurrency flake often = a real race), **key 79** (relates, per
  CANDIDATE_POOL — likely CI/flaky-quarantine process; coordinate the quarantine workflow), **key 88** (CI
  dashboards / flaky reporting). Propose: **this** chapter owns *suite architecture + determinism + test-code
  smells*; cite 41 for the pyramid, 47/48 for effectiveness, 44 for mocking, 24 for concurrency repro.
- **DEMO-CATALOG.md** does not yet exist — add the `49_test_architecture_flakiness` row when created (flag to
  catalog owner; same gap as keys 24/25/35).

### Filed to `09-flags/`
- `09-flags/49_testing_tool_versions_unverified.md` — JUnit 5 / Awaitility / Testcontainers / Surefire /
  Failsafe GAVs + defaults (Awaitility 100 ms poll; Surefire 3.0.0-M4/M6 support versions) are `TO-PIN`;
  API/annotation/config-key identity verified, exact versions `⚠ verify at pin`. Includes the **JUnit
  User-Guide 6.x AHEAD-OF-PIN** drift caveat (pin is 5.x; re-verify section text at the pinned version).
- `09-flags/49_flakiness_figures_unverified.md` — the Google/Micco figures (16% flaky / 84% of pass→fail
  transitions / 1.5% example) trace to Micco's "State of CI Testing @ Google" PDF + Google Testing Blog;
  the blog is qualitative, numbers from the paper/slides — re-confirm exact figures + the Luo et al.
  ten-category list + the (Python-specific) 59% order-dependency figure verbatim at draft; cite as published
  studies with dates, never as universal constants (folklore guard).

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | JUnit doc | User Guide — Test Instance Lifecycle (PER_METHOD default / PER_CLASS verbatim) | docs.junit.org/current/user-guide (Test Instance Lifecycle) | ☑ (verbatim) |
| 2 | JUnit doc | User Guide — Test Execution Order (`@TestMethodOrder`; `MethodName`/`DisplayName`/`OrderAnnotation`/`Random`; `@Order`) | docs.junit.org/…/writing-tests/test-execution-order | ☑ |
| 3 | JUnit doc | User Guide — Parallel Execution (config keys; `@Execution`; `@ResourceLock`/`Resources.SYSTEM_PROPERTIES`; `@Isolated`; PER_CLASS/MethodOrderer guard verbatim) | docs.junit.org/…/writing-tests/parallel-execution | ☑ (guard verbatim) |
| 4 | Awaitility | Usage wiki — `await().atMost(…).until(…)` polling DSL; catches uncaught exceptions by default | github.com/awaitility/awaitility/wiki/Usage | ☑ (DSL); default 100 ms ⚠ |
| 5 | Testcontainers | Reusable Containers (experimental) — `withReuse(true)`, `testcontainers.reuse.enable=true`, "not suited for CI" verbatim | java.testcontainers.org/features/reuse | ☑ (verbatim caveat) |
| 6 | Testcontainers | JUnit-5 container lifecycle — `@Testcontainers`/`@Container` (static = once per class) | testcontainers.com/guides/testcontainers-container-lifecycle | ☑ |
| 7 | Maven | Surefire — Rerun failing tests (`rerunFailingTestsCount`; JUnit5 since 3.0.0-M4; `failOnFlakeCount` since 3.0.0-M6; `flakyFailure`/`flakyError` XML) | maven.apache.org/surefire/maven-surefire-plugin/examples/rerun-failing-tests.html | ☑ (versions ⚠ verify) |
| 8 | Martin Fowler | TestPyramid (Cohn origin; "fewer high-level … more unit tests"; ice-cream cone) | martinfowler.com/bliki/TestPyramid.html | ☑ (named-canon secondary) |
| 9 | Google (Micco) | The State of Continuous Integration Testing @ Google — 16% flaky / 84% of pass→fail transitions are flakes / 1.5% example | research.google.com/pubs/archive/45880.pdf | ☑ (figures — re-confirm @draft) |
| 10 | Google Testing Blog | Flaky Tests at Google and How We Mitigate Them (track/quarantine/rerun narrative) | testing.googleblog.com/2016/05/flaky-tests-at-google-and-how-we.html | ☑ (qualitative) |
| 11 | xUnitPatterns | Assertion Roulette / Obscure Test (Mystery Guest) definitions (Meszaros) | xunitpatterns.com | ☑ (definitions verbatim — re-confirm @draft) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Academic | Luo, Hariri, Eloussi, Marinov — *An Empirical Analysis of Flaky Tests* (FSE 2014) — ten root-cause categories | mir.cs.illinois.edu/lamyaa/publications/fse14.pdf | ☐ (taxonomy — confirm list @draft) |
| 2 | Academic | *A Survey of Flaky Tests* (ACM Comput. Surv. 2021) | dl.acm.org/doi/fullHtml/10.1145/3476105 | ☐ corroboration |
| 3 | Academic | van Deursen, Moonen, van den Bergh, Kok — *Refactoring Test Code* (2001) — test smell origin | (TestSmells literature) | ☐ corroboration |
| 4 | Develocity | Flaky Test Detection Guide — FLAKY outcome on fail-then-pass | docs.gradle.com/enterprise/flaky-test-detection | ☐ (gating ⚠ verify) |
| 5 | Baeldung | Thread.sleep() vs Awaitility.await() (color/teaching) | baeldung.com/java-thread-sleep-vs-awaitility-await | ☐ color only |

> Source-quality order applied: each tool's own docs (JUnit/Awaitility/Testcontainers/Surefire) → named-canon
> (Fowler/Cohn/Meszaros) → published studies (Micco/Google, Luo et al., the 2021 survey) → vendor
> service docs (Develocity) → teaching blogs (color only). Empirical figures are cited to the *study*, never
> restated as universal constants. "Live-line" = verified against current docs; re-verify byte-exact after
> `/pin-source`.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebSearch JUnit 5 ordering/lifecycle/parallel | docs.junit.org | `@TestMethodOrder` + 4 MethodOrderers; PER_METHOD default vs PER_CLASS; parallel config keys; PER_CLASS/MethodOrderer concurrency guard verbatim |
| 2 | WebSearch flaky-test definition / order dependency | ACM survey / Luo / Gruber | flaky = non-deterministic; ten root-cause categories (Luo et al. 2014); order-dependency 59% (Python, Gruber) |
| 3 | WebFetch JUnit User Guide (current/6.1.0) | docs.junit.org | lifecycle/ordering/parallel/`@ResourceLock`/`@Isolated`/`@RepeatedTest`/`assertTimeoutPreemptively` confirmed; **6.x URL = AHEAD-OF-PIN caveat** |
| 4 | WebFetch Google Testing Blog | testing.googleblog.com | blog is qualitative; numbers deferred to the CI study (flag) |
| 5 | WebSearch Micco / 16% / 84% / 1.5% | research.google.com CI study | 16% tests flaky; 84% of pass→fail transitions are flakes; 1.5%-per-run example (1000 tests → ~15 red) |
| 6 | WebSearch test-smell catalogue | xunitpatterns.com / testsmells.org | Assertion Roulette / Eager Test / Mystery Guest / General Fixture definitions (Meszaros / van Deursen) |
| 7 | WebSearch Awaitility / Testcontainers reuse | awaitility wiki / java.testcontainers.org | `await().atMost().until()` (100 ms poll); catches uncaught exceptions by default; `@Testcontainers`/`@Container`; `withReuse(true)`+`testcontainers.reuse.enable=true` "not for CI" verbatim |
| 8 | WebSearch test pyramid / ice-cream cone | martinfowler.com / Cohn | pyramid (Cohn origin, Fowler developed); ice-cream cone anti-pattern (Fowler/Savoia) |
| 9 | WebSearch Surefire rerun / flaky detect | maven.apache.org Surefire | `rerunFailingTestsCount` (JUnit5 since 3.0.0-M4); `failOnFlakeCount` (since 3.0.0-M6); `flakyFailure`/`flakyError` XML; Develocity FLAKY outcome |
| 10 | Read CANDIDATE_POOL row 49 + Part V | 01-index | row 49 no `⚠`; relates 79; cluster 41–52 (41 umbrella, 47/48 effectiveness, 44 mocking, 45 Testcontainers) |

---
## Learnings & pipeline suggestions
- **Reusable shape — "cause → determinism-fix" matrix for any flakiness/non-determinism chapter.** Organise
  on the **published root-cause taxonomy** (Luo et al. ten categories) × **the Java tool that removes each
  cause** (Awaitility for async-wait, per-method lifecycle + `MethodOrderer.Random` for order-dependency,
  Testcontainers for network/IO, injected `Clock` for time, seeded RNG for randomness, order-independent
  assertions for unordered collections). Makes the HONEST-LIMITATIONS floor structural (each fix has a sharp
  edge; rerun = detect-not-cure) and keeps NEUTRALITY easy (each tool cited to its own doc, none crowned).
  Reuse for keys 24 (concurrency repro) and 51 (perf-test variance).
- **"Detection ≠ cure" is the load-bearing honesty for flakiness.** `rerunFailingTestsCount` /
  `failOnFlakeCount` / Develocity FLAKY are *quantify-and-quarantine* surfaces; auto-retry can mask a real
  intermittent bug. State this explicitly so a reader does not read rerun as a fix. New durable teaching
  point — pairs with the key-04 folklore guard.
- **Folklore guard reused (PIPELINE-LEARNINGS list, load-bearing here).** "Coverage % = test quality" is
  folklore: coverage is necessary, not sufficient; test quality = effectiveness (mutation, key 47) +
  determinism (this chapter) + clarity (smell-free, this chapter). Framed correctly; route the depth to 47/48.
- **Published-figure discipline (extends the standards/edition rule to empirical stats).** The Google
  16%/84%/1.5% figures and the Luo et al. ten-category taxonomy are *published studies* — cite the
  paper/slides (not the qualitative blog) with date, and mark the Python-specific 59% order-dependency figure
  as language-specific, never universal. Reinforces "no folklore-as-fact": these are real numbers *with a
  source*, the opposite of the debunked 1:10:100 curve. Filed `09-flags/49_flakiness_figures_unverified.md`.
- **JUnit User-Guide version-drift trap (NEW, sibling of the Sonar/IDE doc-version notes).** Live JUnit
  User-Guide URLs resolve to **6.x** while the testing pin is a 5.x line (`TO-PIN`) — re-verify the section
  *text* at the pinned 5.x version; any 6.x-only API is `⚠ AHEAD-OF-PIN`. Filed in
  `09-flags/49_testing_tool_versions_unverified.md`. Extends the moving-target policy to *doc-site* versions.
- **Test smells = key-19 smell-card applied to test code.** Reuse the key-19 smell-card shape (name attributed
  → symptom → refactoring → tool-found vs review-found) for the test-smell catalogue; most are review-found,
  not gated — don't imply automated enforcement.
- **Cross-ref:** keys 41 (pyramid/landscape umbrella), 42 (JUnit 5 structure), 43 (AssertJ — anti-Assertion-
  Roulette), 44 (over-mocking — the "all unit" failure mode), 45 (Testcontainers depth), 46 (property-based —
  seeded randomness), 47/48 (effectiveness vs coverage folklore), 24 (concurrency flake = real race / JCStress),
  79 (relates — CI/quarantine process), 88 (CI dashboards / flaky reporting). Record in merge notes.
