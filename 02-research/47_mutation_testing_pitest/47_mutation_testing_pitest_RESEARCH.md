# RESEARCH DOSSIER — Java Code Quality Book

> Part-V (Tier-B) **testing-as-a-quality-pillar** dossier. Subject: **mutation testing** as a way to
> *measure test effectiveness* (not code execution), realized in Java by **PITest (PIT)**. This chapter is
> the "how good are your tests?" half of the cluster `47/48` (per `CANDIDATE_POOL.md` row 47: "relates 48";
> §209: "47/48 (+80) — mutation testing vs coverage are the 'how good are the tests / how much do we gate'
> pair"). The spine is: **line coverage tells you what ran; mutation testing tells you what your tests would
> catch.** The folklore guard — *"code coverage % is a measure of test quality"* — is on the
> `PIPELINE-LEARNINGS.md` list ("necessary, not sufficient; use mutation score, keys 47/48") and is framed,
> not asserted, here. Coverage depth is owned by **key 48 (JaCoCo)**; gate policy by **key 80**; the
> mutation-vs-coverage *verdict* is shared across 47/48 and crowns neither metric.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. PITest, JUnit 5, JaCoCo versions are all `TO-PIN`
> in `SOURCE-PIN.md` §3, so **mutator names, status codes, goal names, config keys, and GAV *identity*** are
> verified from PITest's own docs/site, while exact **version numbers, plugin-compatibility rows, and
> threshold defaults** carry `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED` in §7 + flagged to
> `09-flags/`.

---

## Topic
- **Key:** 47 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Mutation testing — PITest (PIT); measuring test *effectiveness*
- **Part:** Part V — Testing as a quality pillar (cluster 41–49; **key 44** doubles/mocking, **key 45**
  integration/Testcontainers, **key 46** parameterized/property-based, **key 48** coverage/JaCoCo (the cluster
  pair), **key 49** test architecture/flakiness; gate policy → **key 80**)
- **Tier:** B (Part V tool/technique chapter) · **Depth band:** Standard (deep single-technique; one tool —
  PITest — anchors the mechanism, with coverage cited to JaCoCo's own source per key 48)
- **Cmp:** not `⚠`-flagged in `CANDIDATE_POOL.md`, but **neutrality is still load-bearing on a *metric*
  comparison**: the chapter contrasts **mutation score** vs **line/branch coverage** as two *measures*. Both
  are the subject (Bucket i — quality concepts/metrics, discussed freely), not rival products; neither is
  crowned. Where PITest is contrasted with JaCoCo as *tools*, each claim is cited to that tool's own pinned
  source (PITest docs / jacoco.org — key 48). The §4 "when NOT to reach for mutation testing" is mandatory.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (verified identity; versions `⚠ verify at pin`):**
  - **PITest core / Maven plugin** — `org.pitest:pitest-maven` (Maven plugin); goals **`mutationCoverage`**
    (analysis + HTML report) and **`scmMutationCoverage`** / `report` (verified, pitest.org Maven quickstart).
    Engine source `github.com/hcoles/pitest`.
  - **PITest Gradle plugin** — id **`info.solidsoft.pitest`** (the `szpak/gradle-pitest-plugin`), exposing the
    **`pitest { }`** extension and a **`pitest`** task (verified, Gradle Plugin Portal + repo).
  - **PITest JUnit 5 test-plugin** — `org.pitest:pitest-junit5-plugin` (added as a `pitestPlugin` dependency
    in Maven, or via `junit5PluginVersion` in Gradle) — **required** for PITest to run JUnit 5 (Jupiter) tests
    (verified, repo README). *(Plugin↔Jupiter version matrix `⚠ verify at pin`.)*
  - **Mutators** (verified names from pitest.org/quickstart/mutators): the **DEFAULTS** group —
    `CONDITIONALS_BOUNDARY`, `INCREMENTS`, `INVERT_NEGS`, `MATH`, `NEGATE_CONDITIONALS`, `VOID_METHOD_CALLS`,
    `EMPTY_RETURNS`, `FALSE_RETURNS`, `TRUE_RETURNS`, `NULL_RETURNS`, `PRIMITIVE_RETURNS`; the **STRONGER**
    and **ALL** groups add `REMOVE_CONDITIONALS`, `EXPERIMENTAL_SWITCH`, `INLINE_CONSTS`,
    `CONSTRUCTOR_CALLS`, `NON_VOID_METHOD_CALLS`, `REMOVE_INCREMENTS`, the `EXPERIMENTAL_*` family, and the
    **deprecated `RETURN_VALS`** (superseded by the returns-mutator set).
  - **Mutant statuses** (verified, basic-concepts page): **KILLED, SURVIVED, NO_COVERAGE, TIMED_OUT,
    MEMORY_ERROR, RUN_ERROR, NON_VIABLE**.
  - **Coverage tool cited for the contrast (key 48's primary):** **JaCoCo** — `org.jacoco:jacoco-maven-plugin`
    (`SOURCE-PIN.md` §3, `TO-PIN`). Cite to jacoco.org; the coverage *deep dive* is key 48.
  - **Test harness the technique sits on (Bucket i):** **JUnit 5 (Jupiter)** — `@Test`, `@ParameterizedTest`,
    `@DisplayName`; AssertJ `assertThat(...)` (keys 41–43 own these; cited, not re-taught).
- **Canonical doc page(s):** `pitest.org/` (definition; "gold standard test coverage"), `pitest.org/quickstart/`
  (entry / supported build tools), `pitest.org/quickstart/basic_concepts/` (mechanism + status codes +
  equivalent mutants), `pitest.org/quickstart/mutators/` (mutator groups), `pitest.org/quickstart/maven/`
  (Maven plugin GAV + goals + config), `pitest.org/quickstart/incremental_analysis/` (history files);
  Gradle plugin portal `plugins.gradle.org/plugin/info.solidsoft.pitest`; `github.com/pitest/pitest-junit5-plugin`
  (JUnit 5 plugin GAV + compatibility).
- **Canonical source path(s):** engine `github.com/hcoles/pitest`; JUnit5 plugin `github.com/pitest/pitest-junit5-plugin`;
  Gradle plugin `github.com/szpak/gradle-pitest-plugin` (SOURCE-PIN §3, PITest row, `TO-PIN`). Companion
  artifact: `08-companion-code/47_mutation_testing_pitest/`.

---

## 1. Core definition & purpose

**Central claim.** Mutation testing measures the *fault-detecting power* of a test suite, not which lines it
runs. PITest "works by **introducing faults (or mutations) into your code, then [running] your tests**"; if a
test fails, the mutation is **killed**; if every test still passes, the mutation **survived** (verified,
pitest.org). A surviving mutant is a small, deliberate behavioural change that *no test noticed* — direct
evidence of a gap in the assertions, not just in the lines executed. The **mutation score** is "the
percentage of mutations killed" (verified). PITest is described by its own site as "a state of the art
mutation testing system, providing **gold standard test coverage** for Java and the jvm" (verified, quoted).

**The problem it solves (and the folklore it corrects — frame, do not assert as fact).** Line/branch coverage
reports *which code was executed* by tests, but "does **not** check that your tests are actually able to
**detect faults**" (verified, pitest.org). A test that calls a method but asserts nothing (or asserts the
wrong thing) still earns 100% line coverage yet kills no mutants. PITest closes this gap: a covered line whose
mutants all *survive* is covered-but-untested. This is the precise framing the book uses for the folklore-list
entry **"code coverage % as a measure of test quality — necessary, not sufficient"** (`PIPELINE-LEARNINGS.md`;
cross-ref key 48): coverage is a *lower bound* (uncovered code is certainly untested), mutation score is a
*stronger signal* of assertion quality. Neither is crowned "the" quality measure — see §4.

**Which part of the pinned set provides it.** The technique is realized by **PITest (PIT)** (`github.com/hcoles/pitest`),
run via the Maven plugin (`org.pitest:pitest-maven`, goal `mutationCoverage`) or the Gradle plugin
(`info.solidsoft.pitest`). Mutators (the fault catalogue), status codes, and the score are PITest's own,
verified from pitest.org.

**When introduced / lineage.** Mutation testing is a long-standing academic technique (1970s); PITest's site
positions itself against earlier JVM systems **Jester** and **Jumble**, the speed gain coming from running
only the tests that *cover* each mutated line rather than the whole suite per mutant (verified, basic-concepts).
*(Exact PITest release history / first-release date `⚠ verify at pin`; the "state of the art" / "gold
standard" phrasings are PITest's own marketing, quoted as the project's self-description, not asserted as an
independent ranking.)*

**Where it sits in the architecture.** Mutation testing is a **post-compile, post-test analysis**: PITest
needs **compiled bytecode** (it mutates bytecode, not source) and a **green test suite** to run against each
mutant. It runs *after* tests pass, typically as a **separate CI stage** (slower than the unit-test stage),
not on every developer save. It is build-time/CI tooling, complementary to the coverage stage (key 48) and the
gate (key 80).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Setup / build-time behavior — what PITest does before scoring

PITest operates on **compiled `.class` bytecode** and a **passing test suite**. The pipeline (verified,
pitest.org basic-concepts):

1. **Traditional line-coverage pass first.** "Before it starts mutation testing, PIT performs a **traditional
   line coverage analysis** for the tests" (verified). This produces, for every line, the set of tests that
   execute it, plus per-test timings.
2. **Apply mutators to the bytecode.** PITest applies "a configurable set of **mutation operators (or
   mutators)** to the byte code" (verified). Each mutator makes one small change — e.g. the
   `CONDITIONALS_BOUNDARY` mutator "replace[s] the relational operators … e.g. `>=` becomes `>`" (verified
   sense); `NEGATE_CONDITIONALS` inverts a condition; `MATH` swaps an arithmetic operator; `VOID_METHOD_CALLS`
   removes a call; the *returns* family (`EMPTY_RETURNS`/`FALSE_RETURNS`/`TRUE_RETURNS`/`NULL_RETURNS`/
   `PRIMITIVE_RETURNS`) replaces return values. Each application yields one **mutant**.
3. **Targeting.** `targetClasses` selects which production classes to mutate; `targetTests` selects which test
   classes may kill mutants (verified, Maven config). Without targeting, PITest mutates the whole project
   (slow on large codebases — §4).

### 2.2 Active / runtime behavior — killing mutants

For each mutant, PITest does NOT re-run the whole suite. It "uses [the coverage] data along with the **timings
of the tests** to pick a set of test cases targeted at the mutated code" (verified) — only tests that cover the
mutated line are candidates, run fastest-first. Each mutant resolves to one **status** (all six verified
verbatim from basic-concepts):

| Status | Meaning (verified phrasing) |
|---|---|
| **KILLED** | "A test caught the mutation successfully" — at least one covering test failed. |
| **SURVIVED** | "The mutation was not detected by the covering test" — tests ran but none failed → an assertion gap. |
| **NO_COVERAGE** | "Same as Survived except there were no tests that exercised the line" — not even executed. |
| **TIMED_OUT** | Mutation caused an (likely infinite) loop, e.g. removing a loop increment → killed by a timeout. |
| **MEMORY_ERROR** | Mutation increased memory consumption beyond limits. |
| **RUN_ERROR** | Something went wrong running the mutant (sometimes a non-viable mutation). |
| **NON_VIABLE** | "Bytecode was in some way invalid" and could not be loaded by the JVM. |

**Scoring.** **Mutation score = killed mutants ÷ total mutants** (verified: "percentage of mutations killed").
`NO_COVERAGE` mutants count against the score (they survived *and* weren't even run). PITest's HTML report
also reports **test strength** = killed ÷ (mutants with coverage) — i.e. it factors out the uncovered mutants
to isolate "of the code my tests *do* touch, how strong are the assertions" (verified the dashboard reports
both score and test strength; exact test-strength denominator `⚠ verify at pin` against the report docs).
`TIMED_OUT`, `MEMORY_ERROR`, and `RUN_ERROR` are typically *counted as killed* (the mutant was detected by a
runtime failure), while `NON_VIABLE` is excluded — exact accounting `⚠ verify at pin`.

### 2.3 Equivalent mutants — the unavoidable noise floor

"Not all mutations will behave differently than the unmutated class" (verified). An **equivalent mutant**
produces logically identical behaviour (e.g. a change that no input can distinguish), so **no test can ever
kill it** — yet it counts as a survivor, depressing the score. Detecting equivalence is, in general,
**undecidable** (NP-hard in practice — academic corroboration, arxiv survey; cite as background, not a PITest
claim). PITest mitigates the most common class by **not mutating lines that call common logging frameworks**
(verified: it "avoid[s]" mutating logging calls because such mutants are usually equivalent). This is the §4
honest limit: a mutation score below 100% is *expected* and partly irreducible — never chase 100%.

### 2.4 Build-tool wiring (Maven / Gradle / JUnit 5)

**Maven** (verified, pitest.org Maven quickstart) — GAV **`org.pitest:pitest-maven`**; the worked goal is
**`mutationCoverage`** (run as `mvn org.pitest:pitest-maven:mutationCoverage`, or bound to a phase). The
`report` goal (since v1.1.6) produces the Maven-site report. JUnit 5 support requires adding
`org.pitest:pitest-junit5-plugin` as a **`pitestPlugin`** dependency inside the plugin's `<dependencies>`
(verified, junit5-plugin README). Example (≤9 lines, verified shape from the Maven quickstart):

```xml
<plugin>
  <groupId>org.pitest</groupId>
  <artifactId>pitest-maven</artifactId>
  <configuration>
    <targetClasses><param>com.acme.store.*</param></targetClasses>
    <targetTests><param>com.acme.store.*</param></targetTests>
  </configuration>
</plugin>
```

**Gradle** (verified, Gradle Plugin Portal `info.solidsoft.pitest`) — apply the plugin and configure the
**`pitest { }`** extension; run the **`pitest`** task. JUnit 5 is enabled by **`junit5PluginVersion`** (which
adds the `org.pitest:pitest-junit5-plugin` dependency and sets the test plugin to "junit5" — verified). Example
(≤9 lines):

```groovy
plugins { id 'info.solidsoft.pitest' version '<pin>' }   // ⚠ version verify at pin
pitest {
  targetClasses = ['com.acme.store.*']
  junit5PluginVersion = '<pin>'                          // ⚠ verify at pin
  mutationThreshold = 80                                  // fail build below 80%
}
```

**Thresholds & build-failure (the gate hook — cross-ref key 80).** `mutationThreshold` fails the build if the
mutation score is below the value; `coverageThreshold` fails on line coverage; `testStrengthThreshold` fails on
test strength (verified the three threshold params exist, Maven config). Defaults / whether unset means "no
gate" are `⚠ verify at pin`.

### 2.5 Incremental analysis (cost control — §4 mitigation)

`withHistory` stores results in `java.io.tmpdir` to skip unchanged work; `historyInputFile`/`historyOutputFile`
(Maven) and `historyInputLocation`/`historyOutputLocation` (Gradle) place the history file explicitly
(verified, incremental-analysis page). On a re-run PITest re-infers status for unchanged classes/tests in three
cases (verified): a previously-detected infinite loop on an unchanged class; a previously-killed mutant whose
tested class *and* killing test are unchanged; a previously-surviving mutant with no new covering tests. The
docs flag a caveat: it only tracks "the strongest of these dependencies — changes to super classes and outer
classes," and the safety of the inference is "currently unproven" though believed rare to break (verified). So
incremental mode trades a small soundness risk for speed — a §4 honesty point.

### 2.6 Reference units (mutator / config / status / coordinate — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `org.pitest:pitest-maven` | Maven plugin (GAV) | goal `mutationCoverage` (+ `report`) | tool-version | pitest.org Maven quickstart ✅ (id+goal); version ⚠ |
| `info.solidsoft.pitest` | Gradle plugin id | `pitest { }` ext; `pitest` task | tool-version | Gradle Plugin Portal ✅; version ⚠ |
| `org.pitest:pitest-junit5-plugin` | test-plugin (GAV) | required for JUnit 5; `pitestPlugin` dep / `junit5PluginVersion` | tool-version | junit5-plugin README ✅; matrix ⚠ |
| `targetClasses` / `targetTests` | config (filter) | glob of classes to mutate / tests to run | n/a | Maven quickstart ✅ (verbatim) |
| `mutationThreshold` | config (gate) | fail build below N% mutation score | n/a | Maven config ✅; default ⚠ |
| `coverageThreshold` | config (gate) | fail build below N% line coverage | n/a | Maven config ✅; default ⚠ |
| `testStrengthThreshold` | config (gate) | fail build below N% test strength | n/a | Maven config ✅; default ⚠ |
| `mutators` | config | mutator set: DEFAULTS / STRONGER / ALL / named | tool-version | mutators page ✅ |
| DEFAULTS group | mutator set | `CONDITIONALS_BOUNDARY`,`INCREMENTS`,`INVERT_NEGS`,`MATH`,`NEGATE_CONDITIONALS`,`VOID_METHOD_CALLS`,`EMPTY_RETURNS`,`FALSE_RETURNS`,`TRUE_RETURNS`,`NULL_RETURNS`,`PRIMITIVE_RETURNS` | tool-version | mutators page ✅ (verbatim names) |
| STRONGER / ALL adds | mutator set | `REMOVE_CONDITIONALS`,`EXPERIMENTAL_SWITCH`,`INLINE_CONSTS`,`CONSTRUCTOR_CALLS`,`NON_VOID_METHOD_CALLS`,`REMOVE_INCREMENTS`,`EXPERIMENTAL_*` | tool-version | mutators page ✅ |
| `RETURN_VALS` | mutator (deprecated) | "superseded by the new returns mutator set" | tool-version | mutators page ✅ (verbatim, deprecated) |
| Mutant statuses (7) | result enum | KILLED/SURVIVED/NO_COVERAGE/TIMED_OUT/MEMORY_ERROR/RUN_ERROR/NON_VIABLE | tool-version | basic-concepts ✅ (verbatim) |
| Mutation score | metric | killed ÷ total mutants | tool-version | pitest.org ✅ ("% killed") |
| Test strength | metric | killed ÷ mutants-with-coverage | tool-version | report dashboard ✅; denom ⚠ |
| `withHistory` / `historyInputFile`/`historyOutputFile` | config (incremental) | cache results in `java.io.tmpdir` / explicit path | n/a | incremental page ✅ |
| `outputFormats` | config | HTML / XML / CSV | n/a | Maven config ✅ |
| `org.jacoco:jacoco-maven-plugin` | coverage tool (GAV) | the line/branch-coverage contrast (key 48 owns) | tool-version | jacoco.org (SOURCE-PIN §3, TO-PIN) ⚠ |

---

## 3. Evidence FOR

- **Measures assertion quality, not execution.** PITest's defining contribution: a mutant that survives a
  *covered* line is direct evidence the tests run the code but don't *check* its behaviour. PITest "introduc[es]
  faults … then [runs] your tests," scoring on "the percentage of mutations killed" (verified, pitest.org) —
  catching the asserts-nothing test that 100% line coverage hides. This is the chapter's strongest case and the
  precise correction to the coverage-as-quality folklore (key 48).
- **Coverage-guided test selection makes it tractable.** PITest runs a line-coverage pass first, then per
  mutant runs only the covering tests, fastest-first (verified, basic-concepts) — the speed advantage over
  whole-suite-per-mutant systems (Jester/Jumble, named by PITest's own docs). This is what makes mutation
  testing viable on real Java projects.
- **A concrete, named fault catalogue.** The mutators are explicit and configurable: DEFAULTS (11 named
  operators, verified verbatim), STRONGER and ALL groups, and named/`EXPERIMENTAL_*` operators — teams can
  scope the fault model to their risk (verified, mutators page). Each surviving mutant points at a *specific*
  unchecked behaviour.
- **First-class build & gate integration.** Maven (`org.pitest:pitest-maven`, goal `mutationCoverage`), Gradle
  (`info.solidsoft.pitest`, `pitest` task), JUnit 5 (`pitest-junit5-plugin`), and CLI/Ant are supported
  (verified, pitest.org); `mutationThreshold`/`coverageThreshold`/`testStrengthThreshold` fail the build,
  wiring the score into CI (cross-ref key 80).
- **HTML report localizes the gap.** PITest produces an HTML report colouring each line by mutant outcome
  (killed/survived/no-coverage) and a dashboard with overall mutation score + test strength (verified) — the
  developer sees exactly which line's assertions are weak.
- **Cost controls exist.** Incremental analysis (`withHistory` + history files) and `targetClasses` scoping cut
  re-run cost (verified, incremental page) — partially answering the headline objection (§4).
- **Maturity / ecosystem.** PITest is actively maintained (`github.com/hcoles/pitest`), with build-tool
  plugins, a JUnit 5 plugin, and IDE integrations (Eclipse/IntelliJ named by pitest.org); research engines such
  as **Descartes** (pseudo-tested-method detection) build on it (arxiv corroboration). *(Adoption-scale figures
  not asserted — no pinned count.)*

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — the hardest objections + when-NOT-to-use)

**Mutation testing's hardest objections (cited to PITest's own docs / the research literature).**
- *Computational cost is the headline limit.* Even with PITest's optimizations, mutation testing runs the
  covering tests **once per mutant**, generating many mutants per method — orders of magnitude slower than a
  unit-test run; the literature reports "nearly two hours … for a relatively small system (47 KLOC)" (arxiv
  corroboration, framed as published finding, exact figure cited not asserted). This is why it belongs in a
  *separate, scoped* CI stage, not the inner dev loop.
- *Equivalent mutants put a hard ceiling below 100%.* Some mutants are behaviourally identical to the original
  ("Not all mutations will behave differently" — verified) and **cannot be killed by any test**; detecting them
  is undecidable in general. PITest filters the common logging-call case but cannot eliminate the class. A team
  that gates on "100% mutation score" will chase irreducible survivors — a documented anti-goal.
- *Survivors need human triage, not auto-fix.* A surviving mutant is a *hypothesis* of a weak test; the
  developer must decide whether it reveals a real gap, an equivalent mutant, or a low-value behaviour not worth
  asserting (logging, toString). Like static-analysis findings (key 39), un-triaged survivors erode trust in
  the score.
- *Flaky / non-deterministic tests corrupt the score.* PITest assumes a deterministic green suite; a flaky test
  (key 49) can mark a mutant killed or survived inconsistently. Time-, thread-, or order-dependent tests make
  the score noisy.
- *Incremental mode trades soundness for speed.* `withHistory` only tracks "the strongest of these
  dependencies — changes to super classes and outer classes," and the inference is "currently unproven"
  (verified) — a re-used result *could* be stale if an untracked dependency changed. Use full runs for the
  authoritative score; incremental for fast feedback.

**When NOT to reach for mutation testing (the honest when-NOT).**
- *On every commit / in the inner loop* — too slow; run it nightly, on a release branch, or on changed modules
  via `targetClasses` + incremental.
- *Before the suite is green and stable* — mutation score is meaningless on a flaky or failing suite (key 49).
- *As a vanity gate at 100%* — equivalent mutants make this unachievable; pick a pragmatic threshold per
  module.
- *On code with little behaviour to assert* — DTOs, generated code, trivial getters yield low-value mutants;
  exclude via `excludedClasses`/`excludedMethods`.
- *As a replacement for coverage or for integration testing* — it complements them (see neutral framing).

**Competing measures *inside* the subject — neutral framing (no metric crowned; verdict shared 47/48).**
**Line/branch coverage** (key 48, JaCoCo) and **mutation score** measure *different things* and answer
different questions: coverage answers "what code did the tests execute?" (a necessary lower bound — uncovered
code is certainly untested); mutation score answers "would the tests *detect* a behavioural change in the code
they execute?" (an assertion-strength signal). They are **complementary, not ranked**: coverage is cheap and
fast (every-commit gate-able, key 80); mutation testing is expensive and deeper (periodic). Each tool's claims
are cited to its own source (coverage → jacoco.org / key 48; mutation → pitest.org). The book frames the pair,
per `PIPELINE-LEARNINGS.md`, as **"coverage % is necessary, not sufficient; mutation score is a stronger but
costlier signal"** — and crowns neither. Mocking interplay: a heavily-mocked test (key 44) can kill mutants in
the unit-under-test while asserting nothing about real collaborators — mutation score on over-mocked code can
flatter; cross-ref key 44's "when (not) to mock."

**Shared limit of all test-adequacy metrics (the honest centre).** No single number proves a suite is "good."
Mutation score can be gamed (assert just enough to kill mutants), reflects only the *encoded* mutators, and
says nothing about whether the *requirements* are tested. It is a strong *signal*, not a proof of correctness —
the same humility the book applies to coverage and to the Maintainability Index (key 04 folklore guard).

---

## 5. Current status

- **Active and current at the anchor.** PITest is actively developed (`github.com/hcoles/pitest`); the Maven
  plugin, the Gradle plugin (`info.solidsoft.pitest`), and the `pitest-junit5-plugin` track releases.
  *(Exact latest-stable versions are `TO-PIN` in `SOURCE-PIN.md` §3 — pin PITest core, the Maven plugin, the
  Gradle plugin, and the JUnit5 plugin together; the JUnit5-plugin↔Jupiter↔PITest compatibility matrix moves
  per release and is `⚠ verify at pin`.)*
- **JUnit 5 support is plugin-gated.** Running Jupiter tests requires `pitest-junit5-plugin`; the
  plugin-version → JUnit-Platform → required-PITest mapping is version-sensitive (verified the matrix exists;
  exact rows `⚠ verify at pin`). A common setup trap: JUnit 5 tests silently "no-coverage" if the plugin is
  missing or mismatched — a teaching point.
- **Java-version coverage.** PITest mutates JVM bytecode and tracks JDK releases; it runs on the anchor (21)
  and forward LTS (25). Whether new language constructs (records, sealed types, pattern-matching `switch`,
  `EXPERIMENTAL_SWITCH`) are fully mutated is version-sensitive → `⚠ verify at pin`; any rule/mutator targeting
  a preview construct is `⚠ AHEAD-OF-PIN`.
- **Mutator evolution.** The *returns* mutator set (`EMPTY_RETURNS`/`FALSE_RETURNS`/…/`PRIMITIVE_RETURNS`)
  superseded the older `RETURN_VALS` (now **deprecated** — verified). Cite the current returns mutators; never
  cite `RETURN_VALS` as current. `EXPERIMENTAL_*` mutators are explicitly experimental — label as such.
- **Ecosystem direction.** Research/extension engines (e.g. **Descartes** for pseudo-tested methods; the
  `pitmp`/Arcmutate commercial extensions) build on PITest. Mention as direction only; any feature outside the
  pinned OSS PITest is `⚠ verify at pin` / out of scope.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `47_mutation_testing_pitest` *(row to be added —
  `DEMO-CATALOG.md` does not yet exist in the repo; see §7 flag, consistent with keys 15/24/25/33).*
  - **Demo name:** "Coverage was green, the mutants survived — finding the asserts-nothing test with PITest."
  - **Java Quality surface exercised:** a small `com.acme.store.pricing` module with a behaviour-rich method
    (e.g. `Discount.apply(Order)` using a boundary condition `qty >= 10`, an arithmetic `price * (1 - rate)`,
    and an early `return`). Ship **two test classes**: (a) a *weak* test that calls the method and asserts only
    that it returns non-null / does not throw → **100% line coverage in JaCoCo but low mutation score** (the
    `CONDITIONALS_BOUNDARY`, `MATH`, and returns mutators survive); (b) a *strong* test with AssertJ
    `assertThat(...)` boundary + value assertions → mutants killed, high mutation score. PITest runs via the
    **Maven** `mutationCoverage` goal with the `pitest-junit5-plugin`; JaCoCo runs alongside to show the
    coverage-vs-mutation contrast (key 48). *(Mutator names + statuses verified; threshold defaults
    `⚠ verify at pin`.)*
  - **TRY-IT exercise:** run `./mvnw -B test jacoco:report` → observe ~100% line coverage with the weak test;
    run `./mvnw -B org.pitest:pitest-maven:mutationCoverage` → observe **surviving mutants** on the covered
    lines and a low mutation score; open the PITest HTML report to see which lines are red; then add the strong
    AssertJ assertions and re-run → mutants killed, score climbs, `mutationThreshold` gate passes. This makes
    "coverage ≠ test quality" tactile in one module.
- **Module key / path:** `08-companion-code/47_mutation_testing_pitest/`
- **Intended dependencies (verified identity @pin; versions ⚠ verify at pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ verify at pin |
  | `org.pitest:pitest-maven` (goal `mutationCoverage`) | runs mutation testing (primary unit under study) | pitest.org Maven quickstart | ☐ verify at pin |
  | `org.pitest:pitest-junit5-plugin` (`pitestPlugin` dep) | lets PITest run Jupiter tests (required) | junit5-plugin README | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | the test harness (canonical at pin) | `SOURCE-PIN.md` §3 | ☐ verify at pin |
  | `org.assertj:assertj-core` | the value/boundary assertions that kill mutants | `SOURCE-PIN.md` §3 | ☐ verify at pin |
  | `org.jacoco:jacoco-maven-plugin` | the line-coverage contrast (key 48) | jacoco.org | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose versions; PITest/JUnit5-plugin versions managed.
  - **Externalized config / profiles** — `targetClasses`/`targetTests` scoping; `mutationThreshold` (the gate);
    a `pitest` Maven profile so the slow stage is opt-in (not on the default `verify`); trace each key to the
    PITest Maven docs.
  - **At least one test** — names the behavior it asserts: the **strong** test asserts the discount boundary
    (`qty == 9` vs `qty == 10`) and the computed price (kills `CONDITIONALS_BOUNDARY` + `MATH` mutants).
  - **Observability / health surface** — the **PITest HTML report** (per-line mutant outcomes + dashboard score)
    is the observability surface; pair with the JaCoCo HTML report to show the contrast.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **weak-test variant** is the failure path — it
    achieves 100% line coverage yet **fails the `mutationThreshold` gate** because mutants survive. State in the
    chapter that the gate failing *despite green coverage* IS the demonstrated failure path, and note the limit:
    chasing 100% is futile because at least one **equivalent mutant** (e.g. on a logging call PITest already
    skips, or a no-op) may remain — the §4 honesty.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `under-test` | the behaviour-rich method (boundary + math + return) PITest mutates | `Discount.java` |
  | `weak-test` | the asserts-little test (100% coverage, mutants survive — failure path) | `DiscountWeakTest.java` |
  | `strong-test` | the AssertJ boundary/value test that kills the mutants | `DiscountTest.java` |
  | `pitest-config` | the `pitest-maven` plugin block: `targetClasses`, `junit5-plugin` dep, `mutationThreshold` | `pom.xml` |

- **Run command:** `./mvnw -B test org.pitest:pitest-maven:mutationCoverage -Ppitest`
  (the slow mutation stage behind a profile; HTML report under `target/pit-reports/`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the weak test — JaCoCo ~100% line coverage, but PITest reports **SURVIVED**/
  **NO_COVERAGE** mutants and a low mutation score, **failing the `mutationThreshold` gate** (build red);
  with the strong test — mutants KILLED, mutation score above the threshold, gate passes; the PITest HTML
  report shows the previously-red lines turn green.
- **Figure plan** (GUIDELINES §8; **standard Part-V technique chapter with a strong visual surface** → image
  budget ~**1–2 designed diagrams + 1 captured screenshot**; not a zero-figure chapter — the
  coverage-vs-mutation contrast and the PITest report are inherently visual):
  - **Chapter class:** standard Part-V technique chapter; the mechanism (coverage pass → mutate → run covering
    tests → status) earns a flow diagram; the HTML report earns a capture.
  - **Candidate designed diagram(s) + family:**
    - **Fig 47.1 — "How PITest scores a suite" (pipeline/flow diagram):** compiled bytecode → **line-coverage
      pass** (which tests hit which line) → **apply mutators** (one mutant per operator per site) → for each
      mutant **run only the covering tests** → resolve to a **status** (KILLED / SURVIVED / NO_COVERAGE / …) →
      **mutation score + test strength**. Family = *process/pipeline flow diagram*. Trace: pass order +
      test selection → basic-concepts page; statuses → basic-concepts (verbatim); score → pitest.org.
    - **Fig 47.2 — "Covered ≠ tested" (concept diagram):** one code line shown (a) green under JaCoCo line
      coverage but (b) with a *surviving* mutant under PITest, beside a fixed assertion that turns the mutant
      red (killed). Family = *concept/contrast diagram* (no metric crowned — two complementary measures).
      Trace: coverage semantics → jacoco.org (key 48); mutant survival → pitest.org "does not check that your
      tests are actually able to detect faults" (verbatim).
  - **Candidate captured surface(s):**
    - **Fig 47.3** — a capture of the **PITest HTML report** from the companion module: the per-line mutant
      colouring (a surviving-mutant red line under the weak test, then green/killed after the strong test) and
      the dashboard mutation score + test strength. Capture real tool output only (technical profile allows
      tool screenshots).
  - **Source trace per depicted claim:** pipeline order + statuses → `pitest.org/quickstart/basic_concepts/`;
    mutators → `pitest.org/quickstart/mutators/`; score/"detect faults" → `pitest.org/`; coverage contrast →
    jacoco.org (key 48); the report capture → the companion module's `target/pit-reports/`.

---

## 7. Gap-filling (verification queue)

- ⚠ **PITest / plugin versions & GAV coordinates** — `org.pitest:pitest-maven`, `info.solidsoft.pitest`
  (Gradle), `org.pitest:pitest-junit5-plugin`, PITest core: all `TO-PIN` in `SOURCE-PIN.md` §3. Plugin
  *identity*, goal names (`mutationCoverage`), extension (`pitest { }`), task (`pitest`) verified; **exact
  latest-stable versions + the JUnit5-plugin↔Jupiter↔PITest compatibility matrix** are `⚠ verify at pin`.
- ⚠ **Threshold defaults** — `mutationThreshold`, `coverageThreshold`, `testStrengthThreshold` verified to
  *exist*; their default values (and whether unset = no gate) are `⚠ verify at pin` from the Maven config docs.
- ⚠ **Test-strength denominator** — verified the report shows mutation score *and* test strength; the exact
  test-strength formula (killed ÷ mutants-with-coverage vs another denominator) is `⚠ verify at pin` against the
  report-format docs.
- ⚠ **Status accounting in the score** — verified the 7 statuses; whether `TIMED_OUT`/`MEMORY_ERROR`/`RUN_ERROR`
  count as killed and `NON_VIABLE` is excluded from the denominator is `⚠ verify at pin`.
- ⚠ **Exact mutator semantics** — DEFAULTS/STRONGER/ALL group membership + names verified verbatim; the precise
  per-mutator transformation (e.g. the full `CONDITIONALS_BOUNDARY` relational-operator table, the `MATH`
  operator map) is `⚠ verify at pin` from the mutators page detail.
- ⚠ **"two hours / 47 KLOC" cost figure** — secondary (arxiv) corroboration only; cite as a published finding
  with its source, never assert as a general fact (folklore-guard discipline). Do not generalize.
- ⚠ **PITest "state of the art" / "gold standard" phrasings** — these are PITest's *own self-description*
  (pitest.org), quoted as such; never present as an independent benchmark/ranking (neutrality — no crowning).
- ⚠ **Java 25 / preview-construct mutation coverage** — whether records / sealed / pattern-`switch` /
  `EXPERIMENTAL_SWITCH` are fully mutated at 25 is `⚠ verify at pin`; any preview-targeting mutator is
  `⚠ AHEAD-OF-PIN`.
- ⚠ **Descartes / Arcmutate / commercial extensions** — outside pinned OSS PITest; mention as direction only,
  `⚠ verify at pin`, never an anchor fact.
- **Open question (draft / Part V routing):** boundary with **key 48** (owns coverage/JaCoCo depth — route ALL
  "what coverage measures / line vs branch / coverage gates" content there; this chapter cites coverage only
  for the contrast), **key 80** (owns the *gate policy* — route "what % to gate at, ratcheting" there; this
  chapter wires `mutationThreshold` as the mechanism), **key 44** (over-mocking flattering the score), **key 49**
  (flakiness corrupting the score), **key 46** (property-based tests as another way to kill mutants). Propose:
  **this** chapter owns mutation testing mechanism + PITest + the "what mutation score signals" framing; cite
  48 for coverage, 80 for gate policy.
- **DEMO-CATALOG.md** does not yet exist in the repo — add the `47_mutation_testing_pitest` row when created
  (same gap noted by keys 15/24/25/33).

### Filed to `09-flags/`
- `09-flags/47_pitest_versions_and_defaults_unverified.md` — PITest core, `pitest-maven`,
  `info.solidsoft.pitest` (Gradle), `pitest-junit5-plugin` are `TO-PIN`; goal/extension/task/mutator/status
  *identity* verified, but exact **versions, the JUnit5-plugin compatibility matrix, threshold defaults, the
  test-strength denominator, and status-to-score accounting** are `⚠ verify at pin`.
- `09-flags/47_mutation_cost_figure_and_self-description.md` — the "~2 hours / 47 KLOC" cost figure is
  secondary (arxiv) corroboration, cite-with-source-never-assert; PITest's "state of the art"/"gold standard"
  are self-descriptions quoted as such, never an independent ranking (neutrality).

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | PITest site | Home — mutation testing definition; killed/lived; "% of mutations killed"; "does not check that your tests are actually able to detect faults"; "gold standard" (self-description) | pitest.org | ☑ (verbatim definition + score) |
| 2 | PITest doc | Basic concepts — line-coverage-first; mutators on bytecode; coverage-guided test selection; KILLED/SURVIVED/NO_COVERAGE/TIMED_OUT/MEMORY_ERROR/RUN_ERROR/NON_VIABLE (verbatim); equivalent mutants; skips logging-call mutations; Jester/Jumble lineage | pitest.org/quickstart/basic_concepts | ☑ (statuses + mechanism verbatim) |
| 3 | PITest doc | Mutators — DEFAULTS (11 named), STRONGER, ALL groups; `EXPERIMENTAL_*`; `RETURN_VALS` deprecated ("superseded by the new returns mutator set") | pitest.org/quickstart/mutators | ☑ (group membership + names) |
| 4 | PITest doc | Maven quickstart — `org.pitest:pitest-maven`; goals `mutationCoverage`/`report`; config `targetClasses`/`targetTests`/`mutationThreshold`/`coverageThreshold`/`testStrengthThreshold`/`mutators`/`outputFormats`/`withHistory`/`timestampedReports` | pitest.org/quickstart/maven | ☑ (GAV + goal + config keys) |
| 5 | PITest doc | Incremental analysis — `withHistory` (`java.io.tmpdir`), `historyInputFile`/`historyOutputFile`; the 3 skip cases; "strongest of these dependencies … currently unproven" caveat | pitest.org/quickstart/incremental_analysis | ☑ (params + caveat verbatim) |
| 6 | PITest source | PIT engine (mutators, statuses, scoring) | github.com/hcoles/pitest | ☑ (repo; bytes ⚠ verify at pin) |
| 7 | PITest source | JUnit 5 plugin — `org.pitest:pitest-junit5-plugin`; `pitestPlugin` dep / `junit5PluginVersion`; plugin↔Platform↔PITest compatibility table | github.com/pitest/pitest-junit5-plugin | ☑ (GAV + matrix exists; rows ⚠) |
| 8 | Gradle plugin | `info.solidsoft.pitest` — plugin id; `pitest { }` extension; `pitest` task; `junit5PluginVersion` adds `pitest-junit5-plugin` | plugins.gradle.org/plugin/info.solidsoft.pitest ; github.com/szpak/gradle-pitest-plugin | ☑ (id + extension; version ⚠) |
| 9 | JaCoCo (key 48) | line/branch coverage — cited for the coverage-vs-mutation contrast only | jacoco.org (SOURCE-PIN §3, TO-PIN) | ☐ verify at pin (key 48 owns) |

### Accessible / Further reading (corroboration / color only)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | arXiv | "Assessing and Improving the Mutation Testing Practice of PIT" (cost, equivalent mutants) | arxiv.org/pdf/1601.02351 | ☐ corroboration only (figures ⚠) |
| 2 | arXiv | "Descartes: A PITest Engine to Detect Pseudo-Tested Methods" | arxiv.org/pdf/1811.03045 | ☐ corroboration only (ecosystem direction) |
| 3 | Baeldung | "Mutation Testing with PITest" (tutorial walkthrough) | baeldung.com/java-mutation-testing-with-pitest | ☐ color only (not a factual source) |

> Source-quality order applied: PITest's own docs/site → PITest source/plugin repos → Gradle Plugin Portal →
> recognized standards (none here beyond the JVM) → research papers (corroboration/cost figures, cite-with-
> source) → tutorials (color only). The coverage-vs-mutation comparison cites JaCoCo to jacoco.org (key 48);
> neither metric is crowned. "Live-line" = verified against current docs; re-verify byte-exact after
> `/pin-source`.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch home | pitest.org | mutation/killed/lived definition; "% of mutations killed"; "does not check that your tests are actually able to detect faults"; "gold standard" (self-desc) |
| 2 | WebFetch mutators | pitest.org/quickstart/mutators | DEFAULTS (11 named), STRONGER, ALL groups; `EXPERIMENTAL_*`; `RETURN_VALS` deprecated/superseded by returns set |
| 3 | WebFetch Maven quickstart | pitest.org/quickstart/maven | `org.pitest:pitest-maven`; goals `mutationCoverage`/`report`; config keys (targetClasses/targetTests/thresholds/mutators/outputFormats/withHistory); example XML |
| 4 | WebFetch quickstart index | pitest.org/quickstart | entry index; CLI/ant/maven; Gradle/Eclipse/IntelliJ support (no mechanism detail on this page) |
| 5 | WebFetch basic concepts | pitest.org/quickstart/basic_concepts | line-coverage-first; mutators on bytecode; coverage-guided test selection; 7 statuses verbatim; equivalent mutants + logging-skip; Jester/Jumble |
| 6 | WebSearch Gradle plugin id | plugins.gradle.org / szpak repo | `info.solidsoft.pitest`; `pitest { }`; `junit5PluginVersion` adds `org.pitest:pitest-junit5-plugin` |
| 7 | WebFetch junit5 plugin repo | github.com/pitest/pitest-junit5-plugin | `org.pitest:pitest-junit5-plugin`; Maven `pitestPlugin` dep + Gradle `junit5PluginVersion`; compatibility table (plugin/PITest/Platform) |
| 8 | WebSearch limitations/cost | arxiv / baeldung / softengbook | mutation score = killed ÷ (total − equivalent); cost (~2h/47KLOC); equivalent mutants undecidable; test strength = fault-detection capability |
| 9 | WebFetch incremental analysis | pitest.org/quickstart/incremental_analysis | `withHistory` (`java.io.tmpdir`); history input/output params; 3 skip cases; "strongest dependencies … currently unproven" caveat |
| 10 | WebFetch Gradle quickstart | pitest.org/quickstart/gradle | 404 — Gradle config sourced from Gradle Plugin Portal + szpak repo instead (flagged) |

---
## Learnings & pipeline suggestions
- **Reusable shape — "metric pair: what-it-measures × what-it-misses, crown neither" for keys 47/48 (and 04).**
  Mutation score and coverage are two *measures* of the same goal (test adequacy). The clean, neutral structure
  is a 2-column contrast: each metric's *question answered* (coverage = "what ran"; mutation = "what would be
  caught"), its *cost*, and its *blind spot* (coverage = no assertion check; mutation = equivalent mutants +
  cost + only-encoded-faults). Frames the folklore entry "coverage% ≠ test quality" as *necessary-not-
  sufficient* without crowning mutation score either (it too can be gamed / is undecidable at 100%). Reuse the
  shape for key 48 and the metric-card pattern (key 04).
- **Folklore guard reused (coverage-as-quality).** The `PIPELINE-LEARNINGS.md` folklore entry is the chapter's
  spine, not an aside: 100% line coverage with an asserts-nothing test is the canonical worked example (weak vs
  strong test in the companion module). State coverage as a *lower bound*, mutation score as a *stronger
  signal*, both as signals not proofs — never "mutation testing is the real measure."
- **Self-description ≠ ranking (NEW, neutrality).** PITest's site calls itself "state of the art" and "gold
  standard." These are the *project's own* words — quote them as self-description, never as an independent
  benchmark crowning PITest over other systems (the Jester/Jumble lineage is historical context, not a
  comparison to crown). Filed in `09-flags/47_mutation_cost_figure_and_self-description.md`. Sibling of the
  "no crowning from marketing" rule.
- **Plugin-gated JUnit 5 is a setup-trap atom.** PITest needs `org.pitest:pitest-junit5-plugin` to run Jupiter
  tests at all; a missing/mismatched plugin makes JUnit 5 tests "no-coverage" silently. The plugin↔Jupiter↔
  PITest version matrix is the never-invent atom here (analogue of the Sonar rule-key-with-prefix and the
  library `Since:` atoms) — cite identity now, the matrix `verify at pin`. Filed in
  `09-flags/47_pitest_versions_and_defaults_unverified.md`.
- **Status-enum verbatim discipline.** The 7 PITest statuses (KILLED/SURVIVED/NO_COVERAGE/TIMED_OUT/
  MEMORY_ERROR/RUN_ERROR/NON_VIABLE) are verbatim from basic-concepts and are never-invent atoms; how each
  rolls into the score (killed vs excluded) moves and is `verify at pin`. Same identity-vs-default split as the
  rule-ID-vs-severity rule (keys 09/16/19).
- **Tooling.** pitest.org pages WebFetch cleanly for definitions/mutators/statuses/Maven config; the
  `quickstart/gradle/` page **404'd** — source Gradle facts from the Gradle Plugin Portal
  (`plugins.gradle.org/plugin/info.solidsoft.pitest`) + `github.com/szpak/gradle-pitest-plugin` instead.
  Research papers (arxiv) are the cleanest source for cost/equivalent-mutant *background* but their figures are
  corroboration-only (cite-with-source).
- **Cross-ref:** keys 41–43 (JUnit 5 / assertions — the harness mutation testing sits on), 44 (mocking —
  over-mocking can flatter mutation score), 46 (property-based — another way to kill mutants), **48 (coverage/
  JaCoCo — the cluster pair; owns coverage depth)**, 49 (flakiness corrupts the score), **80 (gate policy —
  owns what-% / ratcheting; this chapter owns `mutationThreshold` as the mechanism)**, 04 (metric-card /
  folklore guard). Record in merge notes (cluster 47/48 +80).
</content>
</invoke>
