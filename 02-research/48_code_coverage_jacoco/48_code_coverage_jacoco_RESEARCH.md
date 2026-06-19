# RESEARCH DOSSIER — Java Code Quality Book

> Part-V (Tier-B) **single-tool + concept** dossier. The subject is **code coverage** as a quality
> signal, the **JaCoCo** library that measures it for the JVM, and the **gate** question (where, how, and
> whether to fail a build on a coverage number). Row 48 sits in the **47/48 (+80)** merge cluster
> ("mutation testing vs coverage — how good are the tests / how much do we gate"): key 47 (PITest) owns
> *test effectiveness*, key 80 owns *gate strategy / ratcheting / new-code focus*. This chapter owns the
> **coverage mechanism + what coverage does and does NOT tell you + JaCoCo wiring**.
>
> **The load-bearing teaching point is on the PIPELINE-LEARNINGS folklore list:** *"Code coverage % as a
> measure of test quality — necessary, not sufficient."* Coverage is framed throughout as a **find-the-gap
> tool**, never as a quality score; the "coverage = good tests" claim is stated-and-corrected with its
> debunking (Fowler), never asserted. Mutation score (key 47) is the cited complement.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. JaCoCo is `TO-PIN` in `SOURCE-PIN.md` §3, so
> goal/property/DSL **identity, counters, and mechanism** are verified from JaCoCo's own docs while exact
> **version numbers, defaults, and JDK-support boundaries** carry `⚠ verify at pin`. Untraceable atoms →
> `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 48 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Code coverage — JaCoCo; what coverage does and does not tell you; gates
- **Part:** Part V — Testing as a quality pillar (cluster **47/48 (+80)**; key 41 umbrella "test *quality*, not just coverage"; key 47 PITest/mutation owns *effectiveness*; key 80 owns *gate strategy / ratcheting / clean-as-you-code*)
- **Tier:** B (Part V tool + concept chapter) · **Depth band:** Standard (deep single-tool mechanism + a concept chapter on a folklore-laden metric — doc-anchored, debunking cited)
- **Cmp:** no `⚠` glyph in `CANDIDATE_POOL.md` row 48 — but the chapter is **folklore-sensitive**
  (coverage-as-quality is on the folklore list) and touches a neighbour tool (PITest, key 47). Neutrality
  discipline still applies to any tool contrast: JaCoCo gets its strongest case + hardest limitation; the
  *coverage-vs-mutation* "which to trust" verdict is **routed to keys 41/47**; the *gate policy* verdict is
  **routed to key 80**. No tool is crowned. The **subject** (coverage as a *concept*, the testing pyramid,
  what a coverage % means) is discussed freely; **JaCoCo** is the named tool covered in depth.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (verified identity; versions/defaults ⚠ verify at pin):**
  - **JaCoCo core** — `jacoco.org` + `github.com/jacoco/jacoco`. The agent + report engine.
  - **Maven:** `org.jacoco:jacoco-maven-plugin` (GAV) — goals **`prepare-agent`**, **`report`**,
    **`check`**, `prepare-agent-integration`, `report-integration`, `report-aggregate`, `merge`, `dump`,
    `instrument`, `restore-instrumented-classes`, `help` (goal list verified verbatim, maven.html).
  - **Gradle:** the **`jacoco`** core plugin (`id 'jacoco'`) — tasks **`jacocoTestReport`**
    (`JacocoReport`) and **`jacocoTestCoverageVerification`** (`JacocoCoverageVerification`); extension
    property **`toolVersion`** (verified verbatim, Gradle userguide).
  - **The six counters (verified verbatim, counters.html):** **INSTRUCTION** (C0), **BRANCH** (C1),
    **LINE**, **METHOD**, **CLASS**, **COMPLEXITY** (cyclomatic).
  - **The check-rule model (verified verbatim, check-mojo.html):** element ∈ {**BUNDLE, PACKAGE, CLASS,
    SOURCEFILE, METHOD**} (+ GROUP); counter ∈ the six above; value ∈ {**TOTALCOUNT, COVEREDCOUNT,
    MISSEDCOUNT, COVEREDRATIO, MISSEDRATIO**}; `minimum`/`maximum`; `haltOnFailure` (default `true`).
  - **Standards / foundations the chapter rests on (Bucket i — discuss freely, cite, never treat as rivals):**
    the **testing pyramid** (Cohn, *Succeeding with Agile* 2009; Fowler's "TestPyramid" bliki), Fowler's
    "TestCoverage" bliki (the canonical coverage-is-a-gap-finder statement), JLS/JVMS (debug info /
    `LineNumberTable` is what enables line coverage). These are foundations, not comparison targets.
- **Canonical doc page(s):** `jacoco.org/jacoco/trunk/doc/counters.html` (the six counters + cyclomatic
  formula); `.../implementation.html` (on-the-fly agent instrumentation, probes, ASM, exec format);
  `.../maven.html` (plugin GAV + goal list); `.../check-mojo.html` (rule/limit model, `haltOnFailure`);
  `.../mission.html` / `.../faq.html` (scope, what JaCoCo is); Gradle userguide `jacoco_plugin.html`;
  Fowler `martinfowler.com/bliki/TestCoverage.html` and `/bliki/TestPyramid.html`.
- **Canonical source path(s):** traces to `github.com/jacoco/jacoco` (SOURCE-PIN §3, JaCoCo row, `TO-PIN`).
  Companion artifact: `08-companion-code/48_code_coverage_jacoco/`.

---

## 1. Core definition & purpose

**Central claim.** **Code coverage** measures *which* parts of the production code were executed while the
tests ran — it answers "what did my tests touch?", not "are my tests good?". **JaCoCo** (Java Code Coverage)
is the de-facto JVM coverage library: it instruments bytecode, records execution at runtime through
**probes**, and produces per-counter coverage reports (HTML/XML/CSV) and an optional **build-failing check**.
The chapter's spine is the **two halves**: (a) the **mechanism** (how JaCoCo measures coverage — agent,
probes, the six counters), and (b) the **interpretation + gate** (what a coverage % does and does *not* tell
you, and how to wire a coverage gate without inviting the folklore trap that "high coverage = good tests").

**The folklore frame (HARD — this is the chapter's reason to exist).** "Code coverage % as a measure of test
quality" is on the book's **folklore list** (PIPELINE-LEARNINGS): coverage is **necessary, not sufficient**.
Coverage tells you a line *ran*; it cannot tell you the test *asserted* anything meaningful about what ran.
Fowler states it directly: *"Test coverage is of little use as a numeric statement of how good your tests
are"* and *"high coverage numbers are too easy to reach with low quality testing"* — while its real value is
*"a useful tool for finding untested parts of a codebase"* (verified, TestCoverage bliki). The chapter
**states the folklore and corrects it** with the citation; it never asserts coverage as a quality score. The
cited complement that *does* probe test effectiveness is **mutation testing / mutation score** (key 47) — a
neutral, sibling-tool routing, not a crowning.

**Which part of the pinned set provides it.** Coverage measurement is JaCoCo: the agent + the six counters
(`counters.html`), the Maven/Gradle plugins (`maven.html`, Gradle userguide), and the check rule model
(`check-mojo.html`). The interpretation rests on **Bucket-i foundations** — Fowler's coverage bliki and the
testing pyramid (Cohn/Fowler) — cited, not treated as rivals.

**When introduced / version.** JaCoCo is the successor to the EMMA coverage tool (same origin author lineage;
"EMMA history" is color, `⚠ UNVERIFIED` as a precise claim). The relevant pinned facts are the **JDK-support
boundaries**, which are version-sensitive (`⚠ verify at pin`): official **Java 21** support landed in
**0.8.11**; **Java 25** support landed in **0.8.14** (0.8.13 carried *experimental* Java 25 class-file
support) — verified live-line from JaCoCo's release history / change log; confirm the exact version at pin.

**Where it sits in the architecture.** Coverage is a **runtime-measured, build-time-reported** signal: the
JaCoCo **agent runs *with* the JVM that runs the tests** (so it observes real execution), then a **build-step
report goal** turns the recorded execution data into a report and an optional gate. It is therefore a
**post-test / build-time** quality surface (key 75 CI ordering: run tests under the agent, then report/check),
distinct from a static analyzer (Part IV) that never runs the code. The pyramid context (key 41): coverage is
measured across whatever tests run — unit, integration (Testcontainers, key 45), and so on.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 How JaCoCo measures coverage — the agent, probes, and ASM

**Setup / build-time behavior — instrumentation.** JaCoCo uses **on-the-fly bytecode instrumentation** via a
**Java agent** (the `-javaagent` JVM mechanism, available since Java 1.5). Verbatim: *"Coverage information
has to be collected at runtime. For this purpose JaCoCo creates instrumented versions of the original class
definitions. The instrumentation process happens on-the-fly during class loading using so called Java agents"*
(implementation.html). The agent inserts **probes** — measurement points — into the bytecode as classes load;
no source change and no separate build step is required for the common case. (JaCoCo also supports **offline
instrumentation** — the Maven `instrument`/`restore-instrumented-classes` goals — for environments where a
load-time agent can't be attached; that is the §4 caveat path, not the default.)

- **ASM.** JaCoCo manipulates bytecode with the **ASM** library (it does not implement the bytecode spec
  itself): *"Implementing the Java byte code specification would be an extensive and error-prone task.
  Therefore an existing library should be used"* (verified, implementation.html). ASM is shaded into a
  unique package (`org.jacoco.agent.rt_<randomid>`) to avoid classloader/version conflicts with the app's
  own ASM (verified).
- **Class identity.** Each loaded class gets a **CRC64 hash of its raw definition** as identity, so coverage
  is associated with the exact class bytes that ran (verified) — this is why a **stale `.class` / source
  mismatch** breaks line mapping (a §4 sharp edge).

**Active / runtime behavior — probe recording + exec data.** At runtime the instrumented classes hit their
**probes** (boolean execution flags stored in "probe arrays"); the agent's runtime collects them. The
runtime communicates with instrumented code using only standard JRE APIs (notably `Object.equals()`) to
dodge classloader conflicts (verified). Execution data is written to a binary **`.exec`** file (the
`jacoco.exec` produced by the Maven agent; the report goal reads it). Multiple runs can be **merged**
(Maven `merge` goal) and aggregated across modules (`report-aggregate`).

**The six counters (verified verbatim, counters.html) — the heart of "what coverage means."**

- **INSTRUCTION (C0 coverage)** — *"The smallest unit JaCoCo counts are single Java byte code instructions."*
  The base counter; available even without debug info. JaCoCo's headline number is usually instruction %.
- **BRANCH (C1 coverage)** — branches of `if`/`switch`. Verbatim: *"Branch coverage is always available, even
  in absence of debug information in the class files."* This is the counter that exposes **untested
  decision paths** — the one most worth gating on, because 100% line coverage can still miss a branch.
- **LINE** — *"For all class files that have been compiled with debug information, coverage information for
  individual lines can be calculated"*; a line counts as executed when **at least one instruction on it ran**.
  **Requires debug information** (`LineNumberTable`) — the most human-readable counter but the most fragile
  (no debug info → no line coverage; a multi-statement line is all-or-nothing at the line level).
- **METHOD** — a non-abstract method has ≥1 instruction; *"A method is considered as executed when at least
  one instruction has been executed."*
- **CLASS** — *"A class is considered as executed when at least one of its methods has been executed."*
- **COMPLEXITY** — **cyclomatic complexity** per method/class, formula **`v(G) = B − D + 1`** (B = branches,
  D = decision points; JaCoCo's equivalent of `E − N + 2`); JaCoCo additionally reports *covered* vs *missed*
  complexity (verified, counters.html). This ties coverage to key 58 (complexity metrics).

**Teaching point (the counter ladder).** The counters form a strictness ladder: a test that runs a method
gets METHOD coverage but may leave BRANCHes uncovered; INSTRUCTION ≥ LINE granularity; BRANCH is what
catches the untested `else`. The chapter teaches **read BRANCH + INSTRUCTION, not just LINE**, because LINE %
overstates how thoroughly logic was exercised. (No counter measures *assertions* — that gap is the §4 honesty
and the bridge to mutation testing, key 47.)

### 2.2 How JaCoCo runs in a build — Maven goals

The **`org.jacoco:jacoco-maven-plugin`** (GAV; version `⚠ verify at pin`) exposes the goals (verified
verbatim list, maven.html):

- **`prepare-agent`** — binds the JaCoCo agent into the test JVM. It sets a property (conventionally
  `argLine`, consumed by Surefire/Failsafe) so the agent is on the test command line; the docs reference
  `argLine` in the context of surefire/failsafe config (the exact "default property name" is
  `⚠ verify at pin` — see §7). Run in the `initialize`/`test` lifecycle so tests execute under the agent.
- **`report`** — reads `jacoco.exec` and generates the HTML/XML/CSV report (the XML is what SonarQube
  key 35 and CI dashboards ingest).
- **`check`** — evaluates **coverage rules** against thresholds and (by default) **fails the build** — the
  gate goal (see 2.3).
- **`prepare-agent-integration` / `report-integration`** — the integration-test counterparts (Failsafe;
  `jacoco-it.exec`).
- **`merge`** — merges multiple `.exec` files; **`report-aggregate`** — a multi-module aggregate report;
  **`dump`** — dump coverage from a running TCP-server agent; **`instrument` / `restore-instrumented-classes`**
  — the **offline** instrumentation path (replaces `.class` files then restores them); **`help`**.

### 2.3 The gate — the check rule / limit model (verified verbatim, check-mojo.html)

The `check` goal evaluates a list of **rules**; each rule has an **element scope** and a list of **limits**:

| Field | Allowed values (verbatim) |
|---|---|
| `element` | **BUNDLE, PACKAGE, CLASS, SOURCEFILE, METHOD** (+ GROUP) — default **BUNDLE** |
| `counter` | **INSTRUCTION, LINE, BRANCH, COMPLEXITY, METHOD, CLASS** — default **INSTRUCTION** |
| `value` | **TOTALCOUNT, COVEREDCOUNT, MISSEDCOUNT, COVEREDRATIO, MISSEDRATIO** — default **COVEREDRATIO** |
| `minimum` / `maximum` | a ratio `0.0`–`1.0` (or a percentage) |
| `haltOnFailure` | default **`true`** — `false` reports violations without failing the build |

Verified example configuration (check-mojo.html — 80% instruction coverage + zero missed classes):

```xml
<rules>
  <rule>
    <element>BUNDLE</element>
    <limits>
      <limit>
        <counter>INSTRUCTION</counter>
        <value>COVEREDRATIO</value>
        <minimum>0.80</minimum>
      </limit>
      <limit>
        <counter>CLASS</counter>
        <value>MISSEDCOUNT</value>
        <maximum>0</maximum>
      </limit>
    </limits>
  </rule>
</rules>
```

(9 displayed lines max per LEGAL-IP — the snippet above is 16 lines and is the verified doc example; the
displayed listing in the chapter MUST be a tag-region ≤ the snippet ceiling, e.g. just the inner two
`<limit>` blocks, traced to this file. See §6 displayed-snippet tie.)

**Gate teaching (routes to key 80).** The `value` choice is the design lever: **`COVEREDRATIO` minimum** is
the familiar "≥ X%"; **`MISSEDCOUNT maximum`** ("no *new* missed class/line") is the ratchet-friendly form
that key 80 (clean-as-you-code / new-code focus) prefers, because an absolute-percentage gate punishes legacy
code wholesale. The chapter states the trade-off; the *policy* verdict (whole-codebase % vs new-code ratchet
vs no gate) is **key 80's** to crown — this chapter only shows the *mechanism* both policies use.

### 2.4 How JaCoCo runs in a build — Gradle

The Gradle **`jacoco`** plugin (`id 'jacoco'`, verified verbatim) adds, when the Java plugin is applied:
- **`jacocoTestReport`** (type `JacocoReport`) — *"Generates code coverage report for the test task."*
- **`jacocoTestCoverageVerification`** (type `JacocoCoverageVerification`) — *"Verifies code coverage metrics
  based on specified rules for the test task."* *"The build fails if any of the configured rules are not met."*
- DSL (verified verbatim):
  ```gradle
  jacocoTestCoverageVerification {
      violationRules {
          rule { limit { minimum = 0.5 } }
      }
  }
  ```
- Extension property **`toolVersion`** pins the JaCoCo version (doc example value **`0.8.14`** — live-line,
  `⚠ verify at pin`). Default report dir `build/reports/jacoco` (verified).

### 2.5 Reference units (goals / tasks / counters / config — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `org.jacoco:jacoco-maven-plugin` | Maven plugin (GAV) | the coverage plugin | tool-version | maven.html ✅ (GAV); version ⚠ verify at pin |
| `prepare-agent` | Maven goal | binds agent; sets `argLine` property | n/a | maven.html ✅ (goal); property name ⚠ verify at pin |
| `report` | Maven goal | HTML/XML/CSV from `jacoco.exec` | n/a | maven.html ✅ |
| `check` | Maven goal | evaluates rules; fails build | n/a | maven.html ✅ / check-mojo.html ✅ |
| `merge` / `report-aggregate` | Maven goals | combine/aggregate `.exec` | n/a | maven.html ✅ |
| `instrument` / `restore-instrumented-classes` | Maven goals | offline instrumentation | n/a | maven.html ✅ |
| `jacoco` (Gradle) | core plugin | `id 'jacoco'` | n/a | Gradle userguide ✅ |
| `jacocoTestReport` | Gradle task | `JacocoReport` | n/a | Gradle userguide ✅ (verbatim) |
| `jacocoTestCoverageVerification` | Gradle task | `JacocoCoverageVerification`; build fails on rule miss | n/a | Gradle userguide ✅ (verbatim) |
| `toolVersion` | Gradle extension prop | JaCoCo version (e.g. `0.8.14`) | tool-version | Gradle userguide ✅; value ⚠ verify at pin |
| INSTRUCTION (C0) | counter | base; no debug info needed | n/a | counters.html ✅ (verbatim) |
| BRANCH (C1) | counter | `if`/`switch`; available w/o debug info | n/a | counters.html ✅ (verbatim) |
| LINE | counter | **requires debug info**; ≥1 instr on line | n/a | counters.html ✅ (verbatim) |
| METHOD / CLASS | counters | ≥1 instruction / ≥1 method executed | n/a | counters.html ✅ (verbatim) |
| COMPLEXITY | counter | cyclomatic `v(G)=B−D+1`; covered/missed | n/a | counters.html ✅ (formula verbatim) |
| `element` | check field | BUNDLE/PACKAGE/CLASS/SOURCEFILE/METHOD/GROUP | n/a | check-mojo.html ✅ |
| `counter` | check field | the six counters; default INSTRUCTION | n/a | check-mojo.html ✅ |
| `value` | check field | TOTALCOUNT/COVEREDCOUNT/MISSEDCOUNT/COVEREDRATIO/MISSEDRATIO | n/a | check-mojo.html ✅ |
| `haltOnFailure` | check field | default **true** | n/a | check-mojo.html ✅ |
| Java agent / probes / `.exec` | mechanism | on-the-fly instrumentation; CRC64 id; ASM (shaded) | n/a | implementation.html ✅ (verbatim) |

---

## 3. Evidence FOR

- **Zero-touch measurement of real execution.** JaCoCo's on-the-fly agent instruments at class load — no
  source change, no separate compile — and observes the *actual* JVM run of the tests (verified,
  implementation.html). This makes coverage cheap to add to any Maven/Gradle/CI build.
- **Finds untested code — its genuine, defensible value.** Fowler: coverage is *"a useful tool for finding
  untested parts of a codebase … it helps you find which bits of your code aren't being tested"* (verified,
  TestCoverage bliki). This is the strongest case the chapter makes for coverage and the use it should be
  put to — gap-finding, not scoring.
- **Multiple granularities, not one number.** Six counters (INSTRUCTION/BRANCH/LINE/METHOD/CLASS/COMPLEXITY,
  verified verbatim) let a team read coverage at the right resolution — **BRANCH** exposes untested decision
  paths that LINE % hides; **COMPLEXITY** ties coverage to cyclomatic complexity (key 58).
- **Branch coverage without debug info.** *"Branch coverage is always available, even in absence of debug
  information"* (verified) — robust where line coverage degrades.
- **First-class build + gate integration.** Maven (`prepare-agent`/`report`/`check`) and Gradle
  (`jacocoTestReport`/`jacocoTestCoverageVerification`) wire coverage and a build-failing gate into any
  pipeline (verified, maven.html / Gradle userguide). The XML report is the standard feed for SonarQube
  (key 35) and dashboards (key 88).
- **Flexible, ratchet-friendly gate model.** The `value` axis (COVEREDRATIO vs MISSEDCOUNT) and the element
  scopes (BUNDLE→METHOD) let a team gate on a whole-project % *or* "no new uncovered code" (the new-code
  ratchet key 80 favours) — verified, check-mojo.html.
- **Maturity / ubiquity.** JaCoCo is the de-facto JVM coverage library, actively maintained, tracking JDK
  releases (Java 21 in 0.8.11; Java 25 in 0.8.14 — live-line, ⚠ verify at pin).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — coverage's + JaCoCo's hardest objections + when-NOT-to-use)

**The folklore — coverage % ≠ test quality (the chapter's central honesty).**
- Coverage records that a line/branch **executed**, not that any test **asserted** anything about the result.
  A test with no assertions (or wrong assertions) still earns full coverage. Fowler, verbatim: *"Test
  coverage is of little use as a numeric statement of how good your tests are"*; *"high coverage numbers are
  too easy to reach with low quality testing"* (TestCoverage bliki). **Never present a coverage % as a test-
  quality score** (folklore list, PIPELINE-LEARNINGS). The tool that actually probes whether tests *detect*
  changes is **mutation testing / mutation score** — key 47 (cite as the complement; crown neither).
- **Coverage targets get gamed.** Fowler: *"If you make a certain level of coverage a target, people will
  try to attain it"* — and the cheapest way to hit a target is assertion-free tests. He adds he would
  *"expect a coverage percentage in the upper 80s or 90s"* from thoughtful testing but would *"be suspicious
  of anything like 100% — it would smell of someone writing tests to make the coverage numbers happy"*
  (verified verbatim). The chapter states: a *number* is a smell-detector for gaps, not a target to chase.

**JaCoCo's own sharp edges (cited to JaCoCo docs/behavior).**
- **Line coverage needs debug info.** *"For all class files that have been compiled with debug information"*
  (verified) — strip `LineNumberTable` (e.g. obfuscation, `-g:none`) and **line/source coverage vanishes**;
  only INSTRUCTION/BRANCH survive. Multi-statement lines are all-or-nothing at the LINE counter.
- **Stale `.class` / source mismatch.** JaCoCo identifies classes by **CRC64 of the raw bytes** (verified);
  reporting against `.class` files that don't match the analyzed source produces wrong/empty line mapping —
  a common CI failure when modules aren't rebuilt cleanly.
- **`argLine` clobbering.** `prepare-agent` injects the agent via the `argLine` property; a project that sets
  `<argLine>` directly in Surefire/Failsafe **overwrites** JaCoCo's value and silently produces **0%
  coverage**. The fix is to use `@{argLine}` late-replacement / combine the values (mechanism `⚠ verify at
  pin`; documented community pitfall, not a JaCoCo-doc verbatim — see §7).
- **Generated / framework code inflates or distorts numbers.** Records' synthetic accessors, lambdas, enum
  `values()`, builder/Lombok-generated code, and exhaustive-`switch`/record-pattern bytecode all show up;
  0.8.11+ *filters* some synthetic bytecode for switch expressions and record patterns (live-line,
  ⚠ verify at pin), but coverage of generated members still skews raw %. Exclusion config
  (`<excludes>` / `jacoco.excludes`) is needed and is itself a number-tuning lever.
- **JDK-version lag.** JaCoCo must add support per JDK; running tests on a **JDK newer than the agent
  supports** can fail instrumentation. Java 21 → 0.8.11, Java 25 → 0.8.14 (live-line, ⚠ verify at pin) — pin
  the JaCoCo version to the JDK the build runs on.

**When NOT to reach for coverage (the honest when-NOT).**
- **Do not gate on a high whole-codebase % for a large legacy codebase** — it blocks all work until a debt is
  paid; prefer a **new-code ratchet** (MISSEDCOUNT / clean-as-you-code, key 80) or no gate plus trend.
- **Do not treat coverage as evidence the tests are good** — if the question is "do my tests catch bugs?",
  reach for **mutation testing** (key 47), not a higher coverage target.
- **Do not chase 100%** — the marginal lines (trivial getters, unreachable defensive branches,
  framework glue) cost more than they protect, and 100% invites assertion-free padding (Fowler).
- **A throwaway / spike** rarely justifies a coverage gate.

**Competing approach *inside* the field — neutral framing (verdict → keys 41/47/80).** Coverage (JaCoCo) and
**mutation testing** (PITest, key 47) measure **different things**: coverage measures *what executed*;
mutation score measures *whether the tests would fail if the code were broken*. They are **complementary,
not ranked** — mutation testing typically runs the test suite many times (one per mutant) so it costs far
more compute (key 47's honesty), while coverage is a single instrumented run. **No tool is crowned** here:
the "how good are the tests" verdict is keys 41/47's; the "what to gate" verdict is key 80's. Every cross-
tool fact is cited to the named tool's own pinned source.

**Shared limit (the honest centre).** A coverage number is a **necessary-not-sufficient** signal: high
coverage with weak assertions is worse than honest medium coverage, because it manufactures false confidence.
The chapter's whole job is to keep the reader from reading a green % as a quality verdict.

---

## 5. Current status

- **Active and current at the anchor.** JaCoCo is actively maintained and is the de-facto JVM coverage
  library. *(Exact latest-stable version is `TO-PIN` in `SOURCE-PIN.md` §3 — the 0.8.x line is current; the
  Gradle doc example uses `0.8.14`. Pin the exact version at `/pin-source`.)*
- **JDK tracking (live-line, ⚠ verify at pin).** Official **Java 21** support: **0.8.11**; official **Java
  25**: **0.8.14** (0.8.13 = *experimental* Java 25 class-file support). 0.8.11 also added **filtering of
  synthetic bytecode for exhaustive switch expressions and record patterns** so reports don't show phantom
  uncovered branches for those Java-21 features. Confirm exact version↔JDK mapping at pin.
- **Stable mechanism.** The agent / probe / six-counter / check-rule model has been stable across the 0.8.x
  line; the moving parts are JDK support and synthetic-bytecode filters (per-release).
- **Ecosystem position.** JaCoCo's XML report is the standard coverage feed for SonarQube (key 35), CI
  coverage badges, and dashboards (key 88); Gradle ships the `jacoco` plugin in-box; the Maven plugin is the
  standard Maven coverage path. No deprecation of the goal/task surface observed at the anchor.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `48_code_coverage_jacoco` *(row to be added —
  `DEMO-CATALOG.md` does not yet exist in the repo; see §7 flag, consistent with keys 15/24/25/33/35).*
  - **Demo name:** "The 100% trap — full line coverage, untested logic, and the gate that catches it."
  - **Java Quality surface exercised:** a small `org.acme.coverage` module with a class containing a
    branching method (e.g. a price/discount calculator with an `if/else` and a `switch`). **Two test
    classes:** (a) a **coverage-padding** test that *calls* the method but **asserts nothing** (or asserts
    only the no-op) — it produces high **LINE/INSTRUCTION** coverage yet leaves a **BRANCH** uncovered and,
    crucially, would not catch a bug; (b) a **real** test that asserts each branch's result. JaCoCo runs via
    the **Maven** `prepare-agent`/`report`/`check` goals; the `check` rule gates on **BRANCH COVEREDRATIO**
    (not just INSTRUCTION) so the padding test **fails the gate** even at high line %. A `MISSEDCOUNT maximum`
    rule is shown as the ratchet alternative (route to key 80). The README points the reader to key 47
    (mutation) to show that even the "real" test can be probed further. *(Goals/counters/rule fields verified;
    JaCoCo version ⚠ verify at pin.)*
  - **TRY-IT exercise:** run `./mvnw -B verify`; observe the **report** (LINE ~100%, BRANCH < 100%); see the
    **`check` gate fail** on the BRANCH limit. Then (1) lower the gate to INSTRUCTION-only and watch it pass
    *with the bug still present* — the folklore made tactile; (2) add the branch assertions and watch BRANCH
    go green; (3) flip the gate to `MISSEDCOUNT maximum=0` to show the ratchet form (key 80). This makes
    "coverage is necessary, not sufficient" something the reader *feels*.
- **Module key / path:** `08-companion-code/48_code_coverage_jacoco/`
- **Intended dependencies (verified identity; versions ⚠ verify at pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ verify at pin |
  | `org.jacoco:jacoco-maven-plugin` (goals `prepare-agent`, `report`, `check`) | the coverage + gate engine (primary unit) | maven.html / check-mojo.html (GAV TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (canonical at pin) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions (the "real" test) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.apache.maven.plugins:maven-surefire-plugin` | runs tests under the agent via `argLine` | maven.html (argLine note) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `jacoco-maven-plugin` version managed (no loose version).
  - **Externalized config / profiles** — the `<rules>` block in the POM (BRANCH `COVEREDRATIO minimum` as the
    default gate; a `MISSEDCOUNT maximum` ratchet shown via a Maven profile, trace each field to check-mojo.html);
    `jacoco.excludes` showing exclusion is a number-tuning lever (the §4 honesty in config).
  - **At least one test** — the **real** branch-asserting test (names the behavior it asserts per branch).
  - **Observability / health surface** — the JaCoCo **HTML report** (`target/site/jacoco/index.html`) is the
    observability surface; the **XML report** is the feed key 35/88 ingest.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **coverage-padding test + BRANCH gate** is the
    failure path — the `check` goal **fails the build** (`haltOnFailure=true`) on the uncovered branch *despite*
    high line coverage. State in the chapter that the gate failing **is** the demonstrated failure path, and
    that its limit is the folklore itself: switching the gate to INSTRUCTION-only makes the same buggy code pass.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `branch-method` | the branching method under test (the logic coverage must exercise) | `Pricing.java` |
  | `padding-test` | the assertion-free test that earns line coverage but not real testing (folklore) | `PricingPaddingTest.java` |
  | `real-test` | the branch-asserting test that earns BRANCH coverage honestly | `PricingTest.java` |
  | `jacoco-check` | the `<rule>`/`<limit>` BRANCH gate (≤9-line tag-region of the check config) | `pom.xml` |

- **Run command:** `./mvnw -B verify` (the `check` goal binds in `verify`; HTML report under `target/site/jacoco/`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the padding test only — report shows LINE ~100% / BRANCH < 100%, **`check` fails
  the build** on the BRANCH limit (red); with the real test added — BRANCH green, gate passes, build green,
  test pass count green; flipping to INSTRUCTION-only gate shows the buggy variant passing (the teaching point).
- **Figure plan** (GUIDELINES §8; **standard Part-V tool+concept chapter** → image budget ~**1–2 designed
  diagrams + 1 captured screenshot**; not a zero-figure chapter — the JaCoCo report and the counter ladder are
  inherently visual):
  - **Chapter class:** standard Part-V tool+concept chapter with a strong visual surface (the HTML report
    earns a capture; the agent→probe→counter mechanism and the counter ladder earn diagrams).
  - **Candidate designed diagram(s) + family:**
    - **Fig 48.1 — "How JaCoCo measures coverage" (mechanism/flow diagram):** test JVM → JaCoCo **agent**
      (on-the-fly instrumentation, **probes** via ASM) → **`.exec`** data → **`report`** goal → HTML/XML +
      the six **counters**; with the `check` gate branching off. Family = *tool-pipeline / data-flow diagram*.
      Trace: agent/probes/ASM/exec → implementation.html; counters → counters.html; goals → maven.html.
    - **Fig 48.2 — "The counter ladder: what each counter does and doesn't tell you" (concept diagram):** a
      branching method with the same code annotated under LINE (looks ~100%) vs BRANCH (one path missed) vs
      "assertion present? — coverage can't see it" (the folklore boundary, with a pointer to mutation score
      key 47). Family = *concept / annotated-code diagram*. Trace: counter definitions → counters.html;
      coverage-≠-quality → Fowler TestCoverage bliki (Bucket-i foundation).
  - **Candidate captured surface(s):**
    - **Fig 48.3** — a capture of the companion module's **JaCoCo HTML report** (`index.html` / a source view)
      showing the green/red/yellow line+branch highlighting on the branching method — the padding-test run
      (line green but a yellow/red branch diamond) paired with the real-test run (all green). Capture only
      real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every counter label → counters.html; agent/probe/exec/ASM →
    implementation.html; goal/rule/limit labels → maven.html + check-mojo.html; the coverage-≠-quality
    annotation → Fowler TestCoverage bliki; the mutation-complement pointer → key 47 (PITest, cite at draft).

---

## 7. Gap-filling (verification queue)

- ⚠ **JaCoCo version / GAV** — `org.jacoco:jacoco-maven-plugin` and the Gradle `toolVersion` are `TO-PIN` in
  `SOURCE-PIN.md` §3. The 0.8.x line is current; doc example uses `0.8.14`. Confirm exact latest-stable
  version at pin before printing any version number. (Goal/task/counter/rule **identity** is verified now.)
- ⚠ **JDK-support boundaries** — Java 21 → 0.8.11, Java 25 → 0.8.14, 0.8.13 = experimental Java 25: verified
  live-line from JaCoCo release history / change log; re-confirm exact version↔JDK mapping at pin. Never assert
  a JaCoCo version supports a JDK without the change-log row.
- ⚠ **`prepare-agent` property name** — the agent is injected via a property consumed by Surefire/Failsafe
  (conventionally `argLine`); the docs reference `argLine` in the surefire/failsafe context. Confirm the exact
  default property name `prepare-agent` sets (and the `@{argLine}` late-replacement guidance) at pin.
- ⚠ **`argLine`-clobbering pitfall** — the "directly setting `<argLine>` overwrites JaCoCo and yields 0%
  coverage; fix with `@{argLine}`" behavior is a well-known community pitfall, **not** a JaCoCo-doc verbatim
  statement. Confirm against the JaCoCo Maven FAQ / Surefire docs at pin before asserting the fix syntax.
- ⚠ **Synthetic-bytecode filtering** — 0.8.11 "filters synthetic bytecode for exhaustive switch expressions
  and record patterns"; the exact list of filters (records accessors, lambdas, string-switch, try-with-
  resources) is version-sensitive → confirm the filter list at the pinned version's change log.
- ⚠ **EMMA lineage / "JaCoCo succeeds EMMA"** — color/history; `⚠ UNVERIFIED` as a precise claim. Cite only
  if confirmed from a primary source; otherwise omit.
- ⚠ **Exclusion config keys** — `<excludes>` / `jacoco.excludes` glob syntax (`**/dto/**`, etc.) is verified
  to exist conceptually but the exact property/element name + glob rules are `⚠ verify at pin` (agent vs report
  vs check excludes differ).
- **Open question (draft / Part V routing):** boundary with **key 41** (umbrella — owns "test *quality*, not
  just coverage; the pyramid"), **key 47** (PITest/mutation — owns *test effectiveness / mutation score*; route
  the "coverage isn't enough, use mutation" depth there, cite as complement, crown neither), **key 80** (owns
  *gate strategy* — whole-codebase % vs new-code ratchet vs clean-as-you-code; route ALL "what threshold / how
  to gate" policy there; this chapter shows the *mechanism* both use), **key 35** (SonarQube ingests the XML
  report + has its own coverage gate — cite Sonar's own source), **key 58** (cyclomatic complexity — JaCoCo's
  COMPLEXITY counter), **key 88** (dashboards/trend). Propose: **this** chapter owns *coverage mechanism +
  what coverage does/doesn't tell you + JaCoCo wiring*; cite 47 for effectiveness, 80 for gate policy.
- **DEMO-CATALOG.md** does not yet exist in the repo — add the `48_code_coverage_jacoco` row when created
  (flag to catalog owner; same gap noted by keys 15/24/25/33/35).

### Filed to `09-flags/`
- `09-flags/48_jacoco_versions_and_jdk_support_unverified.md` — JaCoCo GAV / Gradle `toolVersion` are
  `TO-PIN`; goal/task/counter/check-rule **identity** + the agent/probe/ASM mechanism + Fowler coverage
  quotes are verified, but exact **version numbers, the Java 21→0.8.11 / Java 25→0.8.14 JDK-support mapping,
  the `prepare-agent` property name, the `argLine`-clobber fix syntax, the synthetic-bytecode filter list,
  and the exclusion-config keys** are `⚠ verify at pin`.

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | JaCoCo doc | Counters — INSTRUCTION(C0)/BRANCH(C1)/LINE/METHOD/CLASS/COMPLEXITY; line needs debug info; branch w/o debug info; `v(G)=B−D+1` | jacoco.org/jacoco/trunk/doc/counters.html | ☑ (six counters + formula verbatim) |
| 2 | JaCoCo doc | Implementation — on-the-fly agent instrumentation, probes, ASM (shaded), CRC64 class id, `.exec` runtime | jacoco.org/jacoco/trunk/doc/implementation.html | ☑ (agent/probes/ASM verbatim) |
| 3 | JaCoCo doc | Maven plugin — GAV `org.jacoco:jacoco-maven-plugin`; goals prepare-agent/report/check/merge/report-aggregate/instrument/… | jacoco.org/jacoco/trunk/doc/maven.html | ☑ (GAV + goal list); version ⚠ |
| 4 | JaCoCo doc | check goal — element/counter/value enums; `minimum`/`maximum`; `haltOnFailure` (default true); example rule | jacoco.org/jacoco/trunk/doc/check-mojo.html | ☑ (rule model + example verbatim) |
| 5 | Gradle doc | JaCoCo plugin — `id 'jacoco'`; `jacocoTestReport`/`jacocoTestCoverageVerification`; `violationRules` DSL; `toolVersion` | docs.gradle.org/current/userguide/jacoco_plugin.html | ☑ (tasks + DSL verbatim) |
| 6 | JaCoCo source/releases | Release history / change log — Java 21→0.8.11, Java 25→0.8.14; synthetic-bytecode filters | github.com/jacoco/jacoco/releases ; jacoco.org/jacoco/trunk/doc/changes.html | ☑ (live-line; ⚠ verify exact mapping at pin) |
| 7 | Fowler bliki (Bucket-i foundation) | "TestCoverage" — coverage finds untested code; "little use as a numeric statement of how good your tests are"; coverage targets get gamed | martinfowler.com/bliki/TestCoverage.html | ☑ (verbatim quotes) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Fowler bliki (Bucket-i) | "TestPyramid" — where coverage is measured across the test layers | martinfowler.com/bliki/TestPyramid.html | ☐ (pyramid context) |
| 2 | Book canon (Bucket-i) | Mike Cohn, *Succeeding with Agile* (2009) — the testing pyramid origin | (cited, not redistributed) | ☐ corroboration |
| 3 | PITest doc (key 47 — cite at draft) | Mutation testing as the test-effectiveness complement to coverage | pitest.org | ☐ route to key 47 |

> Source-quality order applied: JaCoCo's own docs → JaCoCo source/release history → Gradle's own docs
> (JaCoCo plugin is in-box) → recognized foundations (Fowler bliki, the testing pyramid — Bucket-i) →
> secondary/community (the `argLine`-clobber pitfall — corroboration, ⚠ verify at pin). The coverage-vs-
> mutation verdict is cite-to-PITest's-own-source and routed to key 47; the gate-policy verdict to key 80.
> "Live-line" = verified against current docs; re-verify byte-exact after `/pin-source` (pre-pin caveat).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch counters | jacoco.org/.../counters.html | INSTRUCTION(C0)/BRANCH(C1)/LINE/METHOD/CLASS/COMPLEXITY verbatim; line needs debug info; branch available w/o debug info; cyclomatic `v(G)=B−D+1` |
| 2 | WebFetch implementation | jacoco.org/.../implementation.html | on-the-fly agent instrumentation verbatim; probes; ASM (shaded `org.jacoco.agent.rt_<id>`); `Object.equals()` runtime trick; CRC64 class id |
| 3 | WebFetch maven plugin | jacoco.org/.../maven.html | GAV `org.jacoco:jacoco-maven-plugin`; goal list verbatim (prepare-agent/report/check/merge/report-aggregate/instrument/restore/dump/help/…); argLine referenced in surefire/failsafe context (property name ⚠) |
| 4 | WebFetch check goal | jacoco.org/.../check-mojo.html | element/counter/value enums; defaults BUNDLE/INSTRUCTION/COVEREDRATIO; `minimum`/`maximum`; `haltOnFailure` default true; 80%-instr + 0-missed-class example verbatim |
| 5 | WebFetch Gradle plugin | docs.gradle.org/.../jacoco_plugin.html | `id 'jacoco'`; `jacocoTestReport`(JacocoReport)/`jacocoTestCoverageVerification`(JacocoCoverageVerification) verbatim; `violationRules{rule{limit{minimum}}}`; `toolVersion` (example 0.8.14); reports dir |
| 6 | WebFetch Fowler TestCoverage | martinfowler.com/bliki/TestCoverage.html | "little use as a numeric statement…"; "high coverage numbers are too easy to reach with low quality testing"; "useful tool for finding untested parts"; targets get gamed; upper 80s/90s; suspicious of 100% — all verbatim |
| 7 | WebSearch JaCoCo version / Java 21/25 | github.com/jacoco/jacoco releases + changes.html | Java 21 official → 0.8.11; Java 25 official → 0.8.14 (0.8.13 experimental); 0.8.11 filters synthetic bytecode for switch expressions + record patterns (live-line; ⚠ verify at pin) |

---
## Learnings & pipeline suggestions
- **Reusable shape — "the metric and its folklore" for any number-that-gets-gamed chapter.** A folklore-laden
  metric chapter (coverage 48; MI key 04; complexity key 58; DORA key 85) is cleanest organized as **two
  halves**: (1) the **honest mechanism** (what the tool actually measures — JaCoCo's six counters, verified
  verbatim), and (2) the **what-it-does-and-doesn't-tell-you** half that states the folklore and corrects it
  with a cited debunking (Fowler) plus the named complement that fills the gap (mutation score, key 47). Keeps
  the folklore list operational and the chapter from asserting the very myth it teaches against. Reuse for keys
  04/58/85.
- **"Necessary-not-sufficient pairing" routing.** Coverage (48) and mutation testing (47) are a
  complementary pair, not a ranking — coverage = *what ran*, mutation = *would a break be caught*. Frame as
  complements, crown neither, route the "how good are the tests" verdict to keys 41/47 and the "what to gate"
  verdict to key 80. A clean instance of the cluster-aware routing the 47/48(+80) cluster needs.
- **Counter-granularity discipline (atom).** "Coverage" is not one number — JaCoCo has SIX counters with
  distinct meanings (INSTRUCTION/BRANCH/LINE/METHOD/CLASS/COMPLEXITY, verified verbatim). The teaching atom is
  **BRANCH over LINE**: LINE % overstates thoroughness; BRANCH exposes the untested `else`. Always name the
  counter, never write "coverage %" unqualified. Extends the "no number-from-memory" discipline (key 19) to
  *which counter* a percentage refers to.
- **Gate-mechanism vs gate-policy split.** This chapter owns the JaCoCo `check`/`violationRules` **mechanism**
  (element/counter/value/`haltOnFailure`, verified); key 80 owns the **policy** (whole-% vs MISSEDCOUNT ratchet
  vs clean-as-you-code). Don't crown a threshold here — show both `value` forms and route the verdict. Mirrors
  the key-35→key-37 "mechanism here, verdict there" routing.
- **JaCoCo version↔JDK is the never-invent atom.** Like key-23's library `Since:` and key-13's JEP `Release`,
  JaCoCo's **JDK support is per-version** (Java 21→0.8.11, Java 25→0.8.14) — never assert a JaCoCo version
  supports a JDK without the change-log row. Filed `09-flags/48_jacoco_versions_and_jdk_support_unverified.md`.
- **Tooling.** `jacoco.org/jacoco/trunk/doc/*.html` (counters/implementation/maven/check-mojo) and the Gradle
  userguide WebFetch cleanly (verbatim counters, goal list, rule model, DSL) — no 403/curl workaround needed.
  The `prepare-agent` exact property name and the `argLine`-clobber fix are NOT verbatim in the fetched docs →
  flagged `⚠ verify at pin` (re-check the JaCoCo Maven FAQ + Surefire docs at pin).
- **Cross-ref:** keys 41 (test-quality umbrella / pyramid), **47 (mutation — the effectiveness complement,
  cite PITest's own source, crown neither)**, **80 (gate strategy / ratcheting / clean-as-you-code — route
  policy verdict there)**, 35 (SonarQube ingests JaCoCo XML + own coverage gate), 58 (cyclomatic complexity),
  88 (dashboards/trend), 45 (integration tests are also measured), 75 (CI ordering: tests-under-agent → report
  → check). Record in merge-cluster (47/48+80) notes.
</content>
</invoke>
